package gvo.util.xml;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "HEAD")
@XmlType(propOrder = {"BIZTRANSACTIONID", "COUNT", "CONSUMER", "SRVLEVEL", "ACCOUNT", "PASSWORD", "USE", "COMMENTS"})
public class Head implements Serializable {
    /**
     *
     */
    private String BIZTRANSACTIONID;
    private String COUNT;
    private String CONSUMER;
    private String SRVLEVEL;
    private String ACCOUNT;
    private String PASSWORD;
    private String USE;
    private String COMMENTS;

    public Head() {
    }

    public Head(String biztransactionid,String count, String consumer, String srvlevel, String account, String password, String use, String comments) {
        super();
        this.BIZTRANSACTIONID = biztransactionid;
        this.COUNT = count;
        this.CONSUMER = consumer;
        this.SRVLEVEL = srvlevel;
        this.ACCOUNT = account;
        this.PASSWORD = password;
        this.USE = use;
        this.COMMENTS = comments;
    }

    public String getBiztransactionid() {
        return BIZTRANSACTIONID;
    }

    public void setBiztransactionid(String biztransactionid) {
        this.BIZTRANSACTIONID = biztransactionid;
    }

    public String getCount() {
        return COUNT;
    }

    public void setCount(String count) {
        this.COUNT = count;
    }

    public String getConsumer() {
        return CONSUMER;
    }

    public void setConsumer(String consumer) {
        this.CONSUMER = consumer;
    }

    public String getSrvlevel() {
        return SRVLEVEL;
    }

    public void setSrvlevel(String srvlevel) {
        this.SRVLEVEL = srvlevel;
    }

	public String getAccount() {
        return ACCOUNT;
    }

    public void setAccount(String account) {
        this.ACCOUNT = account;
    }

    public String getPassword() {
        return PASSWORD;
    }

    public void setPassword(String password) {
        this.PASSWORD = password;
    }

    public String getUse() {
        return USE;
    }

    public void setUse(String use) {
        this.USE = use;
    }

    public String getComments() {
        return COMMENTS;
    }

    public void setComments(String comments) {
        this.COMMENTS = comments;
    }
}
