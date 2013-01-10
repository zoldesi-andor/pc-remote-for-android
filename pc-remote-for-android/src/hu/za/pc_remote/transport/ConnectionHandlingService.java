package hu.za.pc_remote.transport;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import hu.za.pc_remote.common.RCAction;
import hu.za.pc_remote.utils.Utils;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InterfaceAddress;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/22/11
 * Time: 6:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionHandlingService extends Service {
    private static final String tag = "ConnectionHandlingService";
    public static final String RC_INTENT_ACTION = "RC_INTENT_ACTION";
    public static final String INTENT_DATA_EXTRA_KEY = "data";
    public static final String INTENT_ADDRESS_EXTRA_KEY = "address";

    private RCActionReceiver mRcActionReceiver = null;
    private TransportManager mTransportManager = null;
    private String connectionInfo;
    private Disconnector disconnector;

    private LocalBinder mBinder = new LocalBinder();


    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void onCreate() {
        if (mRcActionReceiver == null) {
            mRcActionReceiver = new RCActionReceiver();
        }
        IntentFilter filter = new IntentFilter(RC_INTENT_ACTION);
        registerReceiver(mRcActionReceiver, filter);
    }

    public void onDestroy() {
        if(mTransportManager != null){
            mTransportManager.Stop();
        }

        if(disconnector != null){
            disconnector.Disconnect();
        }

        if (mRcActionReceiver != null) {
            unregisterReceiver(mRcActionReceiver);
        }
    }

    private class RCActionReceiver extends BroadcastReceiver {
        private static final String tag = "RCActionReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            RCAction data = (RCAction) intent.getSerializableExtra(INTENT_DATA_EXTRA_KEY);
            sendData(data);
            Log.i(tag, String.format("%s action received", data.type.toString()));
        }
    }

    public void sendData(RCAction data){
        if (mTransportManager != null && mTransportManager.isAlive()) {
            mTransportManager.Send(data);
        } else {
            Log.e(tag, "Trying to write data to uninitialized ObjectOutputStream");
            ConnectionHandlingService.this.stopSelf();
        }
    }


    public class LocalBinder extends Binder {
        public ConnectionHandlingService getService() {
            return ConnectionHandlingService.this;
        }
    }

    public void setTransportManager(TransportManager manager, String ConnectionInfo, Disconnector disconnector) {
        this.connectionInfo = ConnectionInfo;
        if(mTransportManager != null){
            mTransportManager.Stop();
        }
        if(this.disconnector != null){
            this.disconnector.Disconnect();
        }
        this.disconnector = disconnector;
        mTransportManager = manager;
    }

    public boolean hasTransportManager(){
        return mTransportManager != null;
    }

    public String getConnectionInfo(){
        return connectionInfo;
    }
}
