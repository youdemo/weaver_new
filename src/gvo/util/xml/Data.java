package gvo.util.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "DATA")
@XmlType(propOrder = {"HEAD", "LIST"})

public class Data implements Serializable {
    /**
     *
     */
    private List<Head> HEAD;
    private List<ListEsb> LIST;

    public Data() {
    }

    public List<Head> getHeads() {
        return HEAD;
    }

    public void setHeads(List<Head> HEAD) {

        this.HEAD = HEAD;
    }

    public List<ListEsb> getLIST() {
        return LIST;
    }

    public void setLIST(List<ListEsb> LIST) {
        this.LIST = LIST;
    }
}
