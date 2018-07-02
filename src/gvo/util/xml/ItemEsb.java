package gvo.util.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ITEM")
@XmlType(propOrder = {"workcode", "datainfo","requestid","xmlPay","DATAINFO"})
public class ItemEsb implements Serializable {
    /**
     *
     * **/
    //private List<DataInfo> dataInfo;
    private String workcode;
    private String datainfo;
    private String requestid;
    private String xmlPay;
    private String DATAINFO;
    public ItemEsb() {

    }

    public ItemEsb(String datainfo) {
        super();
        this.datainfo = datainfo;
    }

    public ItemEsb(String workcode, String datainfo,String requestid,String xmlPay) {
        super();
        this.workcode = workcode;
        this.datainfo = datainfo;
        this.requestid = requestid;
        this.xmlPay = xmlPay;
    }

    public String getWorkcode() {
        return workcode;
    }

    public void setWorkcode(String workcode) {
        this.workcode = workcode;
    }

    public String getDataInfo() {
        return datainfo;
    }

    public void setDataInfo(String datainfo) {
        this.datainfo = datainfo;
    }
    public String getRequestId() {
        return requestid;
    }

    public void setRequestId(String requestid) {
        this.requestid = requestid;
    }
    public String getxmlPay() {
        return xmlPay;
    }

    public void setxmlPay(String xmlPay) {
        this.xmlPay = xmlPay;
    }
    
    public String getDATAINFO() {
        return DATAINFO;
    }

    public void setDATAINFO(String DATAINFO) {
        this.DATAINFO = DATAINFO;
    }  
}
