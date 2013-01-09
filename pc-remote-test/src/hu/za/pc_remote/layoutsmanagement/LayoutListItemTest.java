package hu.za.pc_remote.layoutsmanagement;

import junit.framework.TestCase;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 12/4/11
 * Time: 8:19 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayoutListItemTest extends TestCase{
    @Test
    public void parseTest() throws JSONException {
        int id = 42;
        String name = "Test Name";

        JSONObject item = new JSONObject();
        item.put("id", id);
        item.put("name", name);

        LayoutListItem lli = LayoutListItem.parse(item);

        assertEquals("Name does not match", name, lli.getName());
        assertEquals("Id does not match", id, lli.getId());
    }
}
