package gvo.util.item;

import java.io.Serializable;

public class Head implements Serializable {
    /**
     *
     */
	private String RESULT;
	private String BIZTRANSACTIONID;
	private String ERRORCODE;
	private String ERRORINFO;
	private String COMMENTS;
	private String SUCCESSCOUNT;

    public Head() {
    	
    }

    public Head(String biztransactionid,String result, String errorcode, String errorinfo, String comments, String successcount) {
        super();
        this.BIZTRANSACTIONID = biztransactionid;
        this.RESULT = result;
        this.ERRORCODE = errorcode;
        this.ERRORINFO = errorinfo;
        this.COMMENTS = comments;
        this.SUCCESSCOUNT = successcount;
    }

	public String getRESULT() {
		return RESULT;
	}

	public void setRESULT(String result) {
		RESULT = result;
	}

	public String getBIZTRANSACTIONID() {
		return BIZTRANSACTIONID;
	}

	public void setBIZTRANSACTIONID(String biztransactionid) {
		BIZTRANSACTIONID = biztransactionid;
	}

	public String getERRORCODE() {
		return ERRORCODE;
	}

	public void setERRORCODE(String errorcode) {
		ERRORCODE = errorcode;
	}

	public String getERRORINFO() {
		return ERRORINFO;
	}

	public void setERRORINFO(String errorinfo) {
		ERRORINFO = errorinfo;
	}

	public String getCOMMENTS() {
		return COMMENTS;
	}

	public void setCOMMENTS(String comments) {
		COMMENTS = comments;
	}

	public String getSUCCESSCOUNT() {
		return SUCCESSCOUNT;
	}

	public void setSUCCESSCOUNT(String successcount) {
		SUCCESSCOUNT = successcount;
	}

}
