package hu.za.pc_remote.ui.remotecontrol;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import hu.za.pc_remote.common.KeyCode;
import hu.za.pc_remote.common.RCAction;
import hu.za.pc_remote.transport.ConnectionHandlingService;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 11/5/11
 * Time: 1:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class Keyboard extends EditText {
    Context context = null;

    public Keyboard(Context context) {
        super(context);
        this.context = context;

        addTextChangedListener(new ChangeHandler());
    }

    public class ChangeHandler implements TextWatcher {

        public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
            Log.i("text changed", "chars: " + charSequence + " start: " + start + " before: " + before + " count: " + count);
            Serializable arg = null;

            if (before == count) {
                return;
            }

            if (before > count) {
                arg = KeyCode.VK_BACK;
            } else if (charSequence.length() > start + count - 1) {
                char c = charSequence.charAt(start + count - 1);
                arg = char2KeyCode(c);
            }

            if (arg == null)
                return;

            Intent i = new Intent(ConnectionHandlingService.RC_INTENT_ACTION);
            RCAction extra = new RCAction();
            extra.type = RCAction.Type.KEY_PRESS;
            extra.arguments = new Serializable[]{arg};
            i.putExtra(ConnectionHandlingService.INTENT_DATA_EXTRA_KEY, extra);
            context.sendBroadcast(i);
        }

        public void afterTextChanged(Editable editable) {

        }

        private Serializable char2KeyCode(char c) {

            switch (c) {
                case '+':
                    return KeyCode.VK_ADD;
                case '-':
                    return KeyCode.VK_SUBTRACT;
                case ',':
                    return KeyCode.VK_OEM_COMMA;
                case '.':
                    return KeyCode.VK_OEM_PERIOD;
                case ' ':
                    return KeyCode.VK_SPACE;
                case '/':
                    return KeyCode.VK_DIVIDE;
                case '*':
                    return KeyCode.VK_MULTIPLY;
            }

            return new Integer((int) c);
        }
    }
}
