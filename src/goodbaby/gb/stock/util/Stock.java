package goodbaby.gb.stock.util;

public class Stock {

	private String requestID;	// 流程号
	private String dtID;		// 明细ID
	private String materielID;	// 物料ID
	private String cDate;		// 入库/出库日期
	private String cTime;		// 入库/出库时间
	private double unitPrice;	// 入库/出库单价
	private double cNum;		// 入库/出库数量
	private double fNum;		// 已出库数量
	private int type;			// 入库:0/出库:1
	private String fID;			// 关联入库ID
	private String poid;		// 采购订单
	private String ckid;		// 仓库ID
	private String gys;			// 供应商
	
	public String getCkid() {
		return ckid;
	}
	public void setCkid(String ckid) {
		this.ckid = ckid;
	}
	public String getGys() {
		return gys;
	}
	public void setGys(String gys) {
		this.gys = gys;
	}
	public String getPoid() {
		return poid;
	}
	public void setPoid(String poid) {
		this.poid = poid;
	}
	public String getDtID() {
		return dtID;
	}
	public void setDtID(String dtID) {
		this.dtID = dtID;
	}
	public double getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(double unitPrice) {
		this.unitPrice = unitPrice;
	}
	public String getRequestID() {
		return requestID;
	}
	public void setRequestID(String requestID) {
		this.requestID = requestID;
	}
	public String getMaterielID() {
		return materielID;
	}
	public void setMaterielID(String materielID) {
		this.materielID = materielID;
	}
	public String getcDate() {
		return cDate;
	}
	public void setcDate(String cDate) {
		this.cDate = cDate;
	}
	public String getcTime() {
		return cTime;
	}
	public void setcTime(String cTime) {
		this.cTime = cTime;
	}
	
	public double getcNum() {
		return cNum;
	}
	public void setcNum(double cNum) {
		this.cNum = cNum;
	}
	public double getfNum() {
		return fNum;
	}
	public void setfNum(double fNum) {
		this.fNum = fNum;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getfID() {
		return fID;
	}
	public void setfID(String fID) {
		this.fID = fID;
	}
}
