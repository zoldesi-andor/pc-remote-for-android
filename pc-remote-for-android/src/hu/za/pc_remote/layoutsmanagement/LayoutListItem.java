package hu.za.pc_remote.layoutsmanagement;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import static hu.za.pc_remote.common.ListItemJSONConstants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/18/11
 * Time: 9:02 AM
 * To change this template use File | Settings | File Templates.
 */
public class LayoutListItem implements Serializable{
    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static LayoutListItem parse(JSONObject o) throws JSONException {
        LayoutListItem result = new LayoutListItem();
        result.id = o.getInt(ID);
        result.name = o.getString(NAME);
        return result;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        boolean  result = false;
        if(o instanceof LayoutListItem){
            LayoutListItem other = (LayoutListItem) o;
            result = this.id == other.id && this.name.equals(other.name);
        }
        return result;
    }
}
