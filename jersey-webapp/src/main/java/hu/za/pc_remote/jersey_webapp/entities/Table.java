package hu.za.pc_remote.jersey_webapp.entities;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: Andor
 * Date: 10/23/11
 * Time: 9:47 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class Table extends Element{
    public Row[] row;
}
