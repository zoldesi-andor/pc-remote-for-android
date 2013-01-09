package hu.za.pc_remote.jersey_webapp;

import com.sun.jersey.spi.container.WebApplication;
import hu.za.pc_remote.common.QueryParamConstants;
import hu.za.pc_remote.jersey_webapp.entities.Layout;
import hu.za.pc_remote.jersey_webapp.entities.LayoutListItem;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.jws.WebResult;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Variant;
import java.util.ArrayList;
import java.util.List;

/**
 * Example resource class hosted at the URI path "/myresource"
 */
@Path("/layouts")
public class LayoutService {

    /**
     * Method processing HTTP GET requests, producing "text/plain" MIME media
     * type.
     *
     * @return String that will be send back as a response of type "text/plain".
     */
    @GET
    @Produces("application/json")
    @Path("/list")
    public JSONArray listLayouts() throws JSONException {
        JSONArray result = new JSONArray();
        List<LayoutListItem> items = DAO.getLayouts();
        for (LayoutListItem item : items) {
            result.put(item.toJSON());
        }
        return result;
    }

    @GET
    @Produces("application/json")
    public Layout getLayout(@QueryParam(QueryParamConstants.ID) int id) throws JSONException {
        Layout result = DAO.getLayout(id);
        return result;
    }

    @POST
    @Consumes("application/json")
    public void updateLayout(Layout body) throws JSONException {
        DAO.updateLayout(body);
    }

    @PUT
    @Consumes("application/json")
    public void putLayout(Layout body) throws JSONException {
        DAO.insertLayout(body);
    }

    @DELETE
    public void deleteLayout(@QueryParam(QueryParamConstants.ID) int id) throws JSONException {
        DAO.deleteLayout(id);
    }
}
