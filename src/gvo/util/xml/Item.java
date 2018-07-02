package gvo.util.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
@XmlType(propOrder = {"OAID", "INFNR", "STATUS", "MESSAGE"})
public class Item implements Serializable {
    private String OAID;
	private String INFNR;
	private String STATUS;
	private String MESSAGE;

	/**
     *
     */

    public Item() {
    }

    public Item(String oaid,String infnr, String status, String message) {
        super();
        this.OAID = oaid;
        this.INFNR = infnr;
        this.STATUS = status;
        this.MESSAGE = message;
    }

	public String getOAID() {
		return OAID;
	}

	public void setOAID(String oaid) {
		OAID = oaid;
	}

	public String getINFNR() {
		return INFNR;
	}

	public void setINFNR(String infnr) {
		INFNR = infnr;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String status) {
		STATUS = status;
	}

	public String getMESSAGE() {
		return MESSAGE;
	}

	public void setMESSAGE(String message) {
		MESSAGE = message;
	}

    
}
