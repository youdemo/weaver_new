package htkjjt.pm.peek.order;

import java.util.List;

public class InternalOrderMBean {
	
	private String aufnr;//订单号
	private String ktext;//订单描述
	private String erdat;//建立日期
	private String wawrk;//工厂
	private String vaplz;//维护班组
	private String gstrp;//基本开始日期
	private String gltrp;//基本结束日期
	private String kbtext;//维护班组描述
	private String eqfnr;//公司编码
	private String innam;//计划组描述
	private String sname;//合作伙伴文本
	private String parnr;//要求工号
	private List<InternalOrderDBean> list;
	
	public List<InternalOrderDBean> getList() {
		return list;
	}
	public void setList(List<InternalOrderDBean> list) {
		this.list = list;
	}
	public String getAufnr() {
		return aufnr;
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
	public String getErdat() {
		return erdat;
	}
	public void setErdat(String erdat) {
		this.erdat = erdat;
	}
	public String getWawrk() {
		return wawrk;
	}
	public void setWawrk(String wawrk) {
		this.wawrk = wawrk;
	}
	public String getVaplz() {
		return vaplz;
	}
	public void setVaplz(String vaplz) {
		this.vaplz = vaplz;
	}
	public String getGstrp() {
		return gstrp;
	}
	public void setGstrp(String gstrp) {
		this.gstrp = gstrp;
	}
	public String getGltrp() {
		return gltrp;
	}
	public void setGltrp(String gltrp) {
		this.gltrp = gltrp;
	}
	public String getKbtext() {
		return kbtext;
	}
	public void setKbtext(String kbtext) {
		this.kbtext = kbtext;
	}
	public String getEqfnr() {
		return eqfnr;
	}
	public void setEqfnr(String eqfnr) {
		this.eqfnr = eqfnr;
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
	

}
