package hu.za.pc_remote.desktop_agent;

import hu.za.pc_remote.common.KeyCode;
import hu.za.pc_remote.common.RCAction;
import org.apache.log4j.Logger;


import java.awt.event.KeyEvent;
import java.io.File;

import static hu.za.pc_remote.common.RCAction.Type.*;


/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/24/11
 * Time: 8:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class EventSender {

    private static Logger logger = Logger.getLogger(EventSender.class);

    static {
        String currentDir = new File(".").getAbsolutePath();
        String dll = null;
        if (System.getProperty("os.name").startsWith("Windows")) {
            if (System.getProperty("os.arch").contains("64")) {
                dll = "inputsender_x64.dll";
            } else {
                dll = "inputsender_x86.dll";
            }
        }
        if (dll == null) {
            throw new RuntimeException("Not supported OS");
        }
        logger.debug(System.getProperty("os.name"));
        logger.debug(System.getProperty("os.arch"));
        logger.debug("loading lib: " + currentDir + "\\" + dll);
        System.load(currentDir + "\\" + dll);
    }


    native void sendMouseMove(int x, int y);

    native void sendMouseClick(int button);

    native void sendKeyPress(int key);

    public void send(RCAction action) {
        if (logger.isDebugEnabled()) {
            logger.debug("Sending Event:" + action);
        }

        switch (action.type) {
            case MOUSE_MOVE:
                float x = (Float) action.arguments[0];
                float y = (Float) action.arguments[1];

                sendMouseMove((int) x, (int) y);

                break;
            case MOUSE_CLICK:
                sendMouseClick((Integer) action.arguments[0]);
                break;
            case KEY_PRESS:
                Object o = action.arguments[0];
                if (o instanceof KeyCode) {
                    sendKeyPress(((KeyCode) o).getCode());
                } else if (o instanceof Integer) {
                    char c = Character.toUpperCase((char) ((Integer) o).intValue());

                    if (logger.isDebugEnabled()) {
                        logger.debug("Sending KeyPress:" + c + " (int:" + (int) c);
                    }

                    sendKeyPress((int) c);
                }
                break;
            case COMMAND:
                logger.debug("Command arived " + action);
                break;
        }
    }

}
