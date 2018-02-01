package htkjjt.pm.peek.order;

import java.util.List;

public class ExternalOrderMBean {
	private String aufnr;//订单号
	private String ktext;//订单描述
	private String vaplz;//维护班组
	private String wawrk;//工厂
	private String kbtext;//维护班组描述
	private String ingpr;//计划组
	private String innam;//计划组描述
	private String sname;//合作伙伴文本
	private String parnr;//要求工号
	private String erdat;//建立时间
	private List< ExternalOrderDBean1> list1;
	private List< ExternalOrderDBean2> list2;
	//private ExternalOrderDBean2 ExternalOrderDBean2[] ;
	
	public String getAufnr() {
		return aufnr;
	}
	public List<ExternalOrderDBean1> getList1() {
		return list1;
	}
	public void setList1(List<ExternalOrderDBean1> list1) {
		this.list1 = list1;
	}
	public List<ExternalOrderDBean2> getList2() {
		return list2;
	}
	public void setList2(List<ExternalOrderDBean2> list2) {
		this.list2 = list2;
	}
	public void setAufnr(String aufnr) {
		this.aufnr = aufnr;
	}
	public String getKtext() {
		return ktext;
	}
	public void setKtext(String ktext) {
		this.ktext = ktext;
	}
	public String getVaplz() {
		return vaplz;
	}
	public void setVaplz(String vaplz) {
		this.vaplz = vaplz;
	}
	public String getWawrk() {
		return wawrk;
	}
	public void setWawrk(String wawrk) {
		this.wawrk = wawrk;
	}
	public String getKbtext() {
		return kbtext;
	}
	public void setKbtext(String kbtext) {
		this.kbtext = kbtext;
	}
	public String getIngpr() {
		return ingpr;
	}
	public void setIngpr(String ingpr) {
		this.ingpr = ingpr;
	}
	public String getInnam() {
		return innam;
	}
	public void setInnam(String innam) {
		this.innam = innam;
	}
	public String getSname() {
		return sname;
	}
	public void setSname(String sname) {
		this.sname = sname;
	}
	
	public String getParnr() {
		return parnr;
	}
	public void setParnr(String parnr) {
		this.parnr = parnr;
	}
	public String getErdat() {
		return erdat;
	}
	public void setErdat(String erdat) {
		this.erdat = erdat;
	}
	
	
	
	
	
	
	
	
	


}
