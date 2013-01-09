package hu.za.pc_remote.jersey_webapp.entities;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 11/5/11
 * Time: 3:41 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement(name = "rc")
public class RC {
        @XmlElements({
            @XmlElement(name = "table", type = Table.class),
            @XmlElement(name = "keyboard", type = Keyboard.class),
            @XmlElement(name = "touchpad", type = Touchpad.class)}
    )
    public Element element;
}
