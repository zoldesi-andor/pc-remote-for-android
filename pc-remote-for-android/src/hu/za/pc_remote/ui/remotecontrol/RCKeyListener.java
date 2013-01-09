package hu.za.pc_remote.ui.remotecontrol;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/15/11
 * Time: 8:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class RCKeyListener implements View.OnKeyListener {

    private Context context;

    public RCKeyListener(Context context){
        this.context = context;
    }

    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        Log.i("Key Pressed", keyEvent.getCharacters());
        Toast.makeText(context, keyEvent.getCharacters(), Toast.LENGTH_SHORT);
        return true;
    }
}
