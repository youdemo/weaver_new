package gvo.hr.sysn.tmc.org;

import java.util.HashMap;
import java.util.Map;

public class HrmDepartmentBean {
	
	// 部门编号
	public String departmentcode;
	// 部门简称
	public String departmentname;
	// 部门全称
	public String departmentark;
	// 上级关系      0通过ID  1是通过编码
	private int idOrCode;
	// 上级id   如果没有上级补充为0 
	private String superID;
	// 上级唯一编码   如果没有上级编码为空 
	private String superCode;
	// 所属公司  0通过ID  1通过编码
	public int comIdOrCode;
	// 所属公司ID
	public String subcompanyid1;
	// 所属公司编码
	public String subcompanyCode;
	// 状态   0正常  1封存
	public int status = 0;
	// 排序 
	public int orderBy = 0;
	// 自定义字段     存放字段名称和字段值
	private Map<String,String> cusMap;
	
	public Map<String,String> getCusMap(){
		return cusMap;
	}
	
	public void addCusMap(String key,String value){
		if(cusMap == null)
			cusMap = new HashMap<String,String>();
		cusMap.put(key, value);
	}
	
	public int getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(int orderBy) {
		this.orderBy = orderBy;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public void setCusMap(Map<String, String> cusMap) {
		this.cusMap = cusMap;
	}

	public String getSuperID() {
		return superID;
	}

	public void setSuperID(String superID) {
		this.superID = superID;
	}

	public int getComIdOrCode() {
		return comIdOrCode;
	}

	public void setComIdOrCode(int comIdOrCode) {
		this.comIdOrCode = comIdOrCode;
	}

	public String getSubcompanyCode() {
		return subcompanyCode;
	}

	public void setSubcompanyCode(String subcompanyCode) {
		this.subcompanyCode = subcompanyCode;
	}

	public int getIdOrCode() {
		return idOrCode;
	}

	public void setIdOrCode(int idOrCode) {
		this.idOrCode = idOrCode;
	}

	public String getSuperCode() {
		return superCode;
	}

	public void setSuperCode(String superCode) {
		this.superCode = superCode;
	}

	public String getDepartmentname() {
		return departmentname;
	}

	public void setDepartmentname(String departmentname) {
		this.departmentname = departmentname;
	}

	public String getDepartmentark() {
		return departmentark;
	}

	public void setDepartmentark(String departmentark) {
		this.departmentark = departmentark;
	}
	
	public String getDepartmentcode() {
		return departmentcode;
	}

	public void setDepartmentcode(String departmentcode) {
		this.departmentcode = departmentcode;
	}
	
	public String getSubcompanyid1() {
		return subcompanyid1;
	}

	public void setSubcompanyid1(String subcompanyid1) {
		this.subcompanyid1 = subcompanyid1;
	}

}
