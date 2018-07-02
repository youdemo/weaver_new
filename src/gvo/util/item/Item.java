package gvo.util.item;

public class Item {
	private String OAID;//OAID
	private String INFNR;//信息记录编号
	private String STATUS;//状态
	private String MESSAGE;//消息文本
	
	public Item(){
		
	}
	 public Item(String OAID,String INFNR, String STATUS, String MESSAGE) {
	        super();
	        this.OAID = OAID;
	        this.INFNR = INFNR;
	        this.STATUS = STATUS;
	        this.MESSAGE = MESSAGE;
	    }
	//......get set方法
	public String getOAID() {
		return OAID;
	}
	public void setOAID(String OAID) {
		this.OAID = OAID;
	}
	public String getINFNR() {
		return INFNR;
	}
	public void setINFNR(String INFNR) {
		this.INFNR = INFNR;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String STATUS) {
		this.STATUS = STATUS;
	}
	public String getMESSAGE() {
		return MESSAGE;
	}
	public void setMESSAGE(String MESSAGE) {
		this.MESSAGE = MESSAGE;
	}
	
}
