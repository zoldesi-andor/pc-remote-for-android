package hu.za.pc_remote.layoutsmanagement;

import android.util.Log;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static hu.za.pc_remote.common.LayoutJSONConstants.NAME;
import static hu.za.pc_remote.common.LayoutJSONConstants.TEXT;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/18/11
 * Time: 8:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class NetworkManager {

    private HttpHost host;
    private static final String getLayoutURI = "/jersey-webapp/webresources/layouts?id=";
    private static final String listURI = "/jersey-webapp/webresources/layouts/list";

    public NetworkManager(String hostUrl) {
        host = new HttpHost(hostUrl, 8080);
    }

    public List<LayoutListItem> listLayouts() {

        List<LayoutListItem> result = new ArrayList<LayoutListItem>();
        HttpClient hc = new DefaultHttpClient();
        BasicHttpRequest request = new BasicHttpRequest("GET", listURI);
        InputStream is = null;
        try {
            HttpResponse response = hc.execute(host, request);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                List<LayoutListItem> layouts = FileManager.listFiles();
                String s = getStringFromResponse(response);
                JSONArray list = new JSONArray(s);
                for (int i = 0; i < list.length(); i++) {
                    JSONObject o = list.getJSONObject(i);
                    LayoutListItem item = LayoutListItem.parse(o);
                    if (!layouts.contains(item)) {
                        result.add(item);
                    }
                }
            } else {
                Log.e("listLayouts", "Unexpected response status code:" + status);
            }
        } catch (IOException e) {
            Log.e("listLayouts", "Failed to execute HTTP request", e);
        } catch (JSONException e) {
            Log.e("listLayouts", "Failed to create JSONArray", e);
        }

        return result;
    }

    public void saveLayout(int id) {
        HttpClient hc = new DefaultHttpClient();
        BasicHttpRequest request = new BasicHttpRequest("GET", getLayoutURI + id);
        try {
            HttpResponse response = hc.execute(host, request);
            int status = response.getStatusLine().getStatusCode();
            if (status == 200) {
                String s = getStringFromResponse(response);
                JSONObject jsonObject = new JSONObject(s);

                Object o = jsonObject.get(NAME);
                String name = o != JSONObject.NULL ? o.toString() : null;
                o = jsonObject.get(TEXT);
                String text = o != JSONObject.NULL ? o.toString() : null;

                FileManager.saveToFile(name, id, text);

            } else {
                Log.e("saveLayout", "Unexpected response status code:" + status);
            }
        } catch (IOException e) {
            Log.e("saveLayout", "Failed to execute HTTP request", e);
        } catch (JSONException e) {
            Log.e("saveLayout", "Failed to parse JSONObject", e);
        }
    }

    private String getStringFromResponse(HttpResponse response) {
        StringBuilder result = new StringBuilder();
        InputStream is = null;
        InputStreamReader reader = null;
        try {
            is = response.getEntity().getContent();

            String charset = getContentCharSet(response.getEntity());
            if (charset == null)
                charset = HTTP.DEFAULT_CONTENT_CHARSET;

            reader = new InputStreamReader(is, charset);

            char[] tmp = new char[1024];
            int l;
            while ((l = reader.read(tmp)) != -1) {
                result.append(tmp, 0, l);
            }


        } catch (IOException e) {
            Log.e("getStringFromResponse", "Failed to get String from request", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }
        return result.toString();
    }

    private String getContentCharSet(final HttpEntity entity) {

        if (entity == null) {
            throw new IllegalArgumentException("HTTP entity may not be null");
        }

        String charset = null;

        if (entity.getContentType() != null) {
            HeaderElement values[] = entity.getContentType().getElements();
            if (values.length > 0) {
                NameValuePair param = values[0].getParameterByName("charset");
                if (param != null) {
                    charset = param.getValue();
                }
            }
        }
        return charset;

    }
}
