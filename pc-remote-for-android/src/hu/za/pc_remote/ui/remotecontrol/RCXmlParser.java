package hu.za.pc_remote.ui.remotecontrol;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import hu.za.pc_remote.common.KeyCode;
import hu.za.pc_remote.common.RCAction;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderAdapter;
import android.util.Log;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/13/11
 * Time: 3:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class RCXmlParser extends XMLReaderAdapter {

    Context context;

    private static final String tableTag = "table";
    private static final String rowTag = "row";
    private static final String buttonTag = "button";
    private static final String buttonText = "text";
    private static final String buttonKeyCode = "key";
    private static final String touchPadTag = "touchpad";
    private static final String keyboardTag = "keyboard";

    private View result = null;

    private Keyboard keyboard = null;
    private TouchPad touchPad = null;
    private TableLayout table = null;
    private TableRow row = null;

    public static RCXmlParser getNewInstance(Context context) throws SAXException, ParserConfigurationException {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        return new RCXmlParser(xr, context);
    }

    private RCXmlParser(XMLReader xmlReader, Context context) {
        super(xmlReader);
        this.context = context;
    }

    public View getResult() {
        return result;
    }

    @Override
    public void startElement(String uri,
                             String localName,
                             String qName,
                             Attributes atts) throws SAXException {
        Log.i("start element", localName);

        if (localName.equals(tableTag)) {
            table = new TableLayout(context);
        } else if (localName.equals(rowTag)) {
            if (table != null) {
                row = new TableRow(context);
                table.addView(row);
            }
        } else if (localName.equals(buttonTag)) {
            if (row != null) {
                RCAction action = new RCAction();
                action.type = RCAction.Type.KEY_PRESS;
                action.arguments = new Serializable[1];
                String key = atts.getValue(buttonKeyCode);
                try {
                    action.arguments[0] = KeyCode.valueOf(key);
                } catch (IllegalArgumentException iae) {
                    if (key.length() > 0)
                        action.arguments[0] = new Integer((int) key.charAt(0));
                }
                RCButton button = new RCButton(action, context);
                button.setText(atts.getValue(buttonText));
                row.addView(button);
            }
        } else if (localName.equals(touchPadTag)) {
            touchPad = new TouchPad(context);
        } else if (localName.equals(keyboardTag)) {
            keyboard = new Keyboard(context);
        }
    }

    @Override
    public void endElement(String uri,
                           String localName,
                           String qName) throws SAXException {
        Log.i("end element", localName);

        if (localName.equals(tableTag)) {
            result = table;
        } else if (localName.equals(touchPadTag)) {
            result = touchPad;
        } else if (localName.equals(keyboardTag)) {
            result = keyboard;
        }
    }
}
