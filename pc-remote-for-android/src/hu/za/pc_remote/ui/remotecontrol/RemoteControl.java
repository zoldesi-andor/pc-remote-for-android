package hu.za.pc_remote.ui.remotecontrol;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import hu.za.pc_remote.layoutsmanagement.FileManager;
import hu.za.pc_remote.layoutsmanagement.LayoutListItem;
import hu.za.pc_remote.transport.ConnectionHandlingService;
import hu.za.pc_remote.ui.ConnectionSettings;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;


/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/22/11
 * Time: 6:51 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteControl extends Activity {
    public static final String LayoutItemKey = "layoutItem";

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className, IBinder service) {
            ConnectionHandlingService connService = ((ConnectionHandlingService.LocalBinder) service).getService();
            if (!connService.hasTransportManager()) {
                //Show connection activity
                Intent i = new Intent(RemoteControl.this, ConnectionSettings.class);
                startActivity(i);
            }
        }

        public void onServiceDisconnected(ComponentName componentName) {
            //Nothing to do
        }

    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent starterIntent = getIntent();
        LayoutListItem item = (LayoutListItem) starterIntent.getSerializableExtra(LayoutItemKey);

        if (item != null){
            bindService(new Intent(this, ConnectionHandlingService.class), mConnection, Context.BIND_AUTO_CREATE);
            setContentView(getView(item));
        }
    }

    public View getView(LayoutListItem item) {
        Log.i("getViews", "Started");

        View result = null;

        if (FileManager.isStorageAvailable()) {
            Log.i("getViews", "Storage avaliable");
            try {
                FileReader fr = FileManager.getReader(item);
                RCXmlParser parser = RCXmlParser.getNewInstance(this);
                Xml.parse(fr, parser);
                result = parser.getResult();

            } catch (IOException e) {
                Log.e("getView", "error while parsing layout xml", e);
            } catch (SAXException e) {
                Log.e("getView", "error while parsing layout xml", e);
            } catch (ParserConfigurationException e) {
                Log.e("getView", "error while parsing layout xml", e);
            }
        }
        return result;
    }
}