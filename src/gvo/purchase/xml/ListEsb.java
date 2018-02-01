package gvo.purchase.xml;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "LIST")
@XmlType(propOrder = {"ITEM"})
public class ListEsb implements Serializable {
    /**
     *
     * **/
    private List<ItemEsb> ITEM;

    public ListEsb() {

    }

    public List<ItemEsb> getITEM() {
        return ITEM;
    }

    public void setITEM(List<ItemEsb> ITEM) {
        this.ITEM = ITEM;
    }
}
