
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import java.io.BufferedReader;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 12/3/11
 * Time: 10:08 PM
 * To change this template use File | Settings | File Templates.
 */
public class JerseyTestApp {
    private static final String JdbcUrl = "jdbc:mysql://localhost:3306/pcremote";
    private static final String user = "PCRemoteUser";
    private static final String pass = "pcremote";

    private static final String baseUrl = "http://localhost:8080/jersey-webapp/webresources/layouts";
    private static final String listUrl = "/list";

    public static void main(String[] args) throws Exception {

        Connection con = getConnection();

        System.out.println("1) Checking listLayouts service");
        listLayoutsTest(con);
        System.out.println("2) Checking getLayout service");
        getLayoutTest(con);
        System.out.println("3) Checking insertLayout service");
        Integer id = insertLayoutTest(con);
        if (id != null) {
            System.out.println("4) Checking deleteLayout service");
            deleteLayoutTest(con, id);
        }
        System.out.println("5) Checking upDateLayout service");
        updateLayoutTest(con);


        con.close();
    }

    private static void listLayoutsTest(Connection con) {
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            p = con.prepareStatement("select id, name from layouts");
            rs = p.executeQuery();
            List<Object[]> result = new ArrayList<Object[]>();
            while (rs.next()) {
                result.add(new Object[]{rs.getInt("id"), rs.getString("name")});
            }
            JSONArray list = new JSONArray(getResult(baseUrl + listUrl));

            System.out.print("\t\tNumber of results");
            if (list.length() == result.size()) {
                System.out.println("\t\t\t\t\t OK");

                System.out.print("\t\tValue of result");
                boolean ok = true;
                for (int i = 0; i < result.size(); i++) {
                    int id1 = (Integer) result.get(i)[0];
                    int id2 = list.getJSONObject(i).getInt("id");
                    if (id1 != id2) {
                        ok = false;
                        break;
                    }
                    String name1 = (String) result.get(i)[1];
                    String name2 = list.getJSONObject(i).getString("name");
                    if (!name1.equals(name2)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {
                    System.out.println("\t\t\t\t\t\t OK");
                } else {
                    System.out.println("\t\t\t\t\t\t FAILED");
                }
            } else {
                System.out.println("\t\t\t\t\t FAILED");
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t FAILED with Exception");
            e.printStackTrace();
        } finally {
            if (p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }

    private static void getLayoutTest(Connection con) {
        PreparedStatement p = null;
        ResultSet rs = null;

        try {

            JSONArray list = new JSONArray(getResult(baseUrl + listUrl));

            if (list.length() == 0) {
                System.out.println("\t\t\t\t\t\t\t\t FAILED no data in DB");
                return;
            }

            int id = list.getJSONObject(0).getInt("id");

            JSONObject item = new JSONObject(getResult(baseUrl + "/?id=" + id));

            p = con.prepareStatement("select id, name, text from layouts where id = ?");
            p.setInt(1, id);
            rs = p.executeQuery();
            rs.next();

            System.out.print("\t\tid");
            if (rs.getInt("id") == item.getInt("id")) {
                System.out.println("\t\t\t\t\t\t\t\t\t OK");
            } else {
                System.out.println("\t\t\t\t\t\t\t\t\t FAILED");
            }

            System.out.print("\t\tname");
            if (rs.getString("name").equals(item.getString("name"))) {
                System.out.println("\t\t\t\t\t\t\t\t OK");
            } else {
                System.out.println("\t\t\t\t\t\t\t\t FAILED");
            }

            System.out.print("\t\tData");
            if (rs.getString("text").equals(item.getString("text"))) {
                System.out.println("\t\t\t\t\t\t\t\t OK");
            } else {
                System.out.println("\t\t\t\t\t\t\t\t FAILED");
            }

        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t FAILED with Exception");
            e.printStackTrace();
        } finally {
            if (p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }

    private static Integer insertLayoutTest(Connection con) {

        PreparedStatement p = null;
        ResultSet rs = null;

        try {

            JSONObject item = new JSONObject();
            String name = "Test" + new Random().nextInt();
            item.put("name", name);
            item.put("text", "Hahahah");

            int code = sendRequest(baseUrl, "PUT", item.toString());

            System.out.print("\t\tValidation");
            if (code != 204) {
                System.out.println("\t\t\t\t\t\t\t OK");
            } else {
                System.out.println("\t\t\t\t\t\t\t FAILED");
            }

            System.out.print("\t\tInsert");
            item.put("text", "<rc><touchpad/></rc>");
            code = sendRequest(baseUrl, "PUT", item.toString());

            if (code == 204) {
                p = con.prepareStatement("select id, name from layouts where name = ?");
                p.setString(1, name);
                rs = p.executeQuery();
                if (rs.next()) {
                    System.out.println("\t\t\t\t\t\t\t\t OK");
                    return rs.getInt("id");
                } else {
                    System.out.println("\t\t\t\t\t\t\t\t FAILED");
                }
            } else {
                System.out.println("\t\t\t\t\t\t\t\t FAILED");
            }

        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t FAILED with Exception");
            e.printStackTrace();
        } finally {
            if (p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }

        return null;
    }

    private static void updateLayoutTest(Connection con) {
        PreparedStatement p = null;
        ResultSet rs = null;

        try {

            JSONArray list = new JSONArray(getResult(baseUrl + listUrl));

            if (list.length() == 0) {
                System.out.println("\t\t\t\t\t\t\t\t FAILED no data in DB");
                return;
            }

            int id = list.getJSONObject(0).getInt("id");

            JSONObject item = new JSONObject(getResult(baseUrl + "/?id=" + id));

            String oldName = item.getString("name");
            String oldText = item.getString("text");

            item.put("name", "Test" + new Random().nextInt());
            item.put("text", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
                    "<rc>\n" +
                    "    <touchpad/>\n" +
                    "</rc>");

            int code = sendRequest(baseUrl + "/?id=" + id, "POST", item.toString());

            if (code == 204) {

                p = con.prepareStatement("select id, name, text from layouts where id = ?");
                p.setInt(1, id);
                rs = p.executeQuery();
                rs.next();

                System.out.print("\t\tname");
                if (rs.getString("name").equals(item.getString("name"))) {
                    System.out.println("\t\t\t\t\t\t\t\t OK");
                } else {
                    System.out.println("\t\t\t\t\t\t\t\t FAILED");
                }

                System.out.print("\t\tData");
                if (rs.getString("text").trim().equals(item.getString("text").trim())) {
                    System.out.println("\t\t\t\t\t\t\t\t OK");
                } else {
                    System.out.println("\t\t\t\t\t\t\t\t FAILED");
                }
            } else {
                System.out.println("\t\t\t\t\t\t\t\t FAILED");
            }

            item.put("name", oldName);
            item.put("text", oldText);
            sendRequest(baseUrl + "/?id=" + id, "POST", item.toString());
        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t FAILED with Exception");
            e.printStackTrace();
        } finally {
            if (p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }

    private static void deleteLayoutTest(Connection con, int id) {
        PreparedStatement p = null;
        ResultSet rs = null;

        try {

            System.out.print("\t\tcheck id");
            if (getResult(baseUrl + "/?id=" + id) != null) {
                System.out.println("\t\t\t\t\t\t\t OK");
            } else {
                System.out.println("\t\t\t\t\t\t\t FAILED");
            }

            int code = sendRequest(baseUrl + "/?id=" + id, "DELETE", null);

            System.out.print("\t\tcheck delete");
            if (code == 204) {
                p = con.prepareStatement("select count(*) as num from layouts where id = ?");
                p.setInt(1, id);
                rs = p.executeQuery();
                rs.next();
                if (rs.getInt("num") == 0) {
                    System.out.println("\t\t\t\t\t\t OK");
                } else {
                    System.out.println("\t\t\t\t\t\t FAILED");
                }
            } else {
                System.out.println("\t\t\t\t\t\t\t\t FAILED");
            }

        } catch (Exception e) {
            System.out.println("\t\t\t\t\t\t\t\t FAILED with Exception");
            e.printStackTrace();
        } finally {
            if (p != null)
                try {
                    p.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            if (rs != null)
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
        }
    }

    private static int sendRequest(String _url, String method, String content) throws Exception {
        URL url = new URL(_url);
        HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestProperty(
                "Content-Type", "application/json");
        httpCon.setRequestMethod(method);
        if (content != null) {
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(content);
            out.close();
        }
        return httpCon.getResponseCode();
    }

    private static String getResult(String url) throws Exception {
        StringBuilder sb = new StringBuilder();
        BufferedReader in = null;
        try {
            URL yahoo = new URL(url);
            URLConnection yc = yahoo.openConnection();
            in = new BufferedReader(
                    new InputStreamReader(
                            yc.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null)
                sb.append(inputLine);
            in.close();
        } finally {
            if (in != null) {
                in.close();
            }
        }
        return sb.toString();
    }

    private static Connection getConnection() throws Exception {
        Class.forName("com.mysql.jdbc.Driver").newInstance();
        return DriverManager.getConnection(JdbcUrl, user, pass);
    }
}
