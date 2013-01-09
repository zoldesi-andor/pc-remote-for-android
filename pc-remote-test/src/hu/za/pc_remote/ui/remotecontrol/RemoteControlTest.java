package hu.za.pc_remote.ui.remotecontrol;

import android.app.Application;
import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import android.text.AndroidCharacter;
import android.util.Xml;
import android.view.View;
import android.widget.TableLayout;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/11/11
 * Time: 11:31 PM
 * To change this template use File | Settings | File Templates.
 */
public class RemoteControlTest extends ActivityInstrumentationTestCase2<RemoteControl> {

    private RemoteControl mActivity;

    public RemoteControlTest() {
        super("hu.za.pc_remote.ui.remotecontrol", RemoteControl.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testParser() throws Exception {

        String testXML =
               "<rc>\n" +
               "    <table>\n" +
               "        <row>\n" +
               "            <button key=\"VK_UP\" text=\"Vol +\"/>\n" +
               "            <button key=\"VK_DOWN\" text=\"Vol -\"/>\n" +
               "        </row>\n" +
               "        <row>\n" +
               "            <button key=\"F\" text=\"F\"/>\n" +
               "            <button key=\"O\" text=\"O\"/>\n" +
               "        </row>\n" +
               "    </table>\n" +
               "</rc>";

        RCXmlParser parser = RCXmlParser.getNewInstance(getActivity());
        Xml.parse(testXML, parser);

        View v = parser.getResult();
        assertTrue("Not instance of TableLayout", v instanceof TableLayout);
        TableLayout tableLayout = (TableLayout) v;
        assertTrue("Wrong number of children", tableLayout.getChildCount() == 2);

    }
}
