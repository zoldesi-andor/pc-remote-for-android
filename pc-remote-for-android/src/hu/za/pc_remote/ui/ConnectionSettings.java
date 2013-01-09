package hu.za.pc_remote.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.*;
import hu.za.pc_remote.R;
import hu.za.pc_remote.transport.ConnectionHandlingService;
import hu.za.pc_remote.transport.Disconnector;
import hu.za.pc_remote.transport.TransportManager;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Set;
import java.util.UUID;

import android.os.Handler;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/22/11
 * Time: 6:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConnectionSettings extends Activity {

    private static final String tag = "ConnectionSettings";
    private boolean mIsBound;
    private static final UUID mServiceUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private ConnectionHandlingService mConnService;
    private ArrayAdapter mDevicesAdapter;
    private String mAddress;
    private BluetoothSocket socket;

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {
            String info = mDevicesAdapter.getItem(arg2).toString();
            mAddress = info.substring(info.length() - 17);
            final ProgressDialog dialog = ProgressDialog.show(ConnectionSettings.this, "", getString(R.string.loading), true);

            final Handler handler = new Handler() {
                public void handleMessage(Message msg) {
                    dialog.dismiss();
                    if(mConnService.hasTransportManager()){
                        setDisconnectLayout();
                    }
                    else{
                        Toast.makeText(ConnectionSettings.this, getText(R.string.FailedToConnect), Toast.LENGTH_SHORT).show();
                    }
                }
            };

            new Thread() {
                @Override
                public void run() {
                    BluetoothDevice device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(mAddress);
                    connectToDevice(device);
                    handler.sendEmptyMessage(0);
                }
            }.start();
        }
    };

    private View.OnClickListener mDisconnectListener = new View.OnClickListener() {
        public void onClick(View view) {
            if (mConnService != null) {
                mConnService.setTransportManager(null, null, null);
                setConnectLayout();
            }
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            mConnService = ((ConnectionHandlingService.LocalBinder) service).getService();
            if (mConnService.hasTransportManager()) {
                setDisconnectLayout();
            } else {
                setConnectLayout();
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            mConnService = null;
            Toast.makeText(ConnectionSettings.this, "Disconnect",
                    Toast.LENGTH_SHORT).show();
        }
    };

    void doBindService() {
        bindService(new Intent(ConnectionSettings.this,
                ConnectionHandlingService.class), mConnection, Context.BIND_AUTO_CREATE);


        mIsBound = true;
    }

    void doUnbindService() {
        if (mIsBound) {
            unbindService(mConnection);
            mIsBound = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        doUnbindService();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doBindService();
    }

    private void setConnectLayout() {
        setContentView(R.layout.connectlayout);

        ListView listView = (ListView) findViewById(R.id.devicelist);
        mDevicesAdapter = new ArrayAdapter(this, R.layout.simplelistitem);
        listView.setAdapter(mDevicesAdapter);
        listView.setOnItemClickListener(mDeviceClickListener);

        Set<BluetoothDevice> devices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        if (devices.size() > 0) {
            findViewById(R.id.devicelist).setVisibility(View.VISIBLE);
            for (BluetoothDevice device : devices) {
                mDevicesAdapter.add(device.getName() + "\n" + device.getAddress());
            }
        } else {
            String noDevices = "No paired device found";
            listView.setOnItemClickListener(null);
            mDevicesAdapter.add(noDevices);
        }
    }

    private void setDisconnectLayout() {
        setContentView(R.layout.disconnectlayout);
        TextView textView = (TextView) findViewById(R.id.connectedToTextView);
        if (mConnService != null) {
            textView.setText(
                    new StringBuilder(getText(R.string.connectedText))
                            .append(mConnService.getConnectionInfo())
            );
        }

        Button button = (Button) findViewById(R.id.disconnectButton);
        button.setOnClickListener(mDisconnectListener);
    }

    public void connectToDevice(BluetoothDevice device) {
        try {
            socket = device.createInsecureRfcommSocketToServiceRecord(mServiceUUID);
            //socket = device.createRfcommSocketToServiceRecord(mServiceUUID);
            socket.connect();
            TransportManager transportManager = new TransportManager(
                    new ObjectOutputStream(socket.getOutputStream()));
            mConnService.setTransportManager(
                    transportManager,
                    String.format("%s (%s)", device.getName(), device.getAddress()),
                    new Disconnector() {
                        public void Disconnect() {
                            try {
                                socket.close();
                            } catch (IOException e) {
                                Log.e(tag, "Failed to close Socket:", e);
                            }
                        }
                    });
        } catch (IOException e) {
            closeSocket();
            Log.e(tag, "Failed to open Socket:", e);

        }
    }

    private void closeSocket() {
        if (socket != null)
            try {
                socket.close();
            } catch (IOException e1) {
                Log.e(tag, "Failed to close socket", e1);
            }
    }
}