package hu.za.pc_remote.ui.remotecontrol;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import hu.za.pc_remote.common.RCAction;
import hu.za.pc_remote.transport.ConnectionHandlingService;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/6/11
 * Time: 9:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class RCButton extends Button implements View.OnClickListener {

    private RCAction prototype;
    private Context context;

    public RCButton(RCAction prototype, Context context) {
        super(context);
        this.context = context;
        this.prototype = prototype;
        setOnClickListener(this);
    }

    public void onClick(View view) {
        Intent i = new Intent(ConnectionHandlingService.RC_INTENT_ACTION);
        i.putExtra(ConnectionHandlingService.INTENT_DATA_EXTRA_KEY, prototype);
        context.sendBroadcast(i);
    }
}
