package hu.za.pc_remote.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import hu.za.pc_remote.R;
import hu.za.pc_remote.transport.ConnectionHandlingService;
import hu.za.pc_remote.ui.remotecontrol.SelectLayout;
import hu.za.pc_remote.ui.layoutmanager.LayoutManager;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 9/22/11
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */
public class MainMenu extends Activity {


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final Intent i = new Intent(this, ConnectionHandlingService.class);
        startService(i);

        setContentView(R.layout.mainmenu);

        Button button = (Button) findViewById(R.id.connSettingsButton);
        button.setOnClickListener(new MainMenuEntry(ConnectionSettings.class));

        button = (Button) findViewById(R.id.touchpadButton);
        button.setOnClickListener(new MainMenuEntry(SelectLayout.class));

        button = (Button) findViewById(R.id.layoutsMenuButton);
        button.setOnClickListener(new MainMenuEntry(LayoutManager.class));

        button = (Button) findViewById(R.id.exitButton);
        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                stopService(i);
                finish();
            }
        });
    }


    private class MainMenuEntry implements View.OnClickListener {
        private Class clazz;

        public MainMenuEntry(Class clazz) {
            this.clazz = clazz;
        }

        public void onClick(View view) {
            startActivity(new Intent(MainMenu.this, clazz));
        }
    }
}