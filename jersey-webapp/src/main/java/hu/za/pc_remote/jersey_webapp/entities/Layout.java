package hu.za.pc_remote.jersey_webapp.entities;

import hu.za.pc_remote.jersey_webapp.XmlValidationException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.transform.Result;
import javax.xml.transform.dom.DOMResult;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static hu.za.pc_remote.common.LayoutJSONConstants.*;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/17/11
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class Layout {
    @XmlElement
    public Integer id;
    @XmlElement
    public String name;
    @XmlTransient
    public RC rc;

    @XmlTransient
    public boolean isError;

    @XmlElement(name = "text")
    public String getXML() throws XmlValidationException {
        StringWriter writer = new StringWriter();
        try {
            JAXBContext context = JAXBContext.newInstance(RC.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            m.marshal(rc, writer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmlValidationException("The RC xml is not well formed!");
        }
        return writer.toString();
    }

    public void setXML(String data) throws XmlValidationException {
        StringReader reader = new StringReader(data);
        RC result = null;
        try {
            JAXBContext context = JAXBContext.newInstance(RC.class);
            Unmarshaller um = context.createUnmarshaller();
            result = (RC) um.unmarshal(reader);
        } catch (Exception e) {
            e.printStackTrace();
            throw new XmlValidationException("The RC xml is not well formed!");
        }
        rc = result;
    }
}
