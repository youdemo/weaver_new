package gvo.purchase.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ITEM")
@XmlType(propOrder = {"dataInfo"})
public class ItemEsb implements Serializable {
    /**
     *
     * **/
    //private List<DataInfo> dataInfo;
    private String dataInfo;

    public ItemEsb() {

    }

    public String getDataInfo() {
        return dataInfo;
    }

    public void setDataInfo(String dataInfo) {
        this.dataInfo = dataInfo;
    }
}
