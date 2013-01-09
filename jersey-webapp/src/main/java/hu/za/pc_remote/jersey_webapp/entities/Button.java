package hu.za.pc_remote.jersey_webapp.entities;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/23/11
 * Time: 9:42 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "button")
public class Button {
    @XmlAttribute(required = true)
    public String key;
    @XmlAttribute(required = true)
    public String text;
}
