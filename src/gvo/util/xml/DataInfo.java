package gvo.util.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ITEM")
@XmlType(propOrder = {"datainfo"})
public class DataInfo implements Serializable {
    /**
     *
     */
    private String datainfo;

    public DataInfo() {

    }

    public DataInfo(String datainfo) {
        super();
        this.datainfo = datainfo;
    }

    public String getDatainfo() {
        return datainfo;
    }

    public void setDatainfo(String datainfo) {
        this.datainfo = datainfo;
    }
}
