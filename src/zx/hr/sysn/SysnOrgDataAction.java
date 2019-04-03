package zx.hr.sysn;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.interfaces.schedule.BaseCronJob;
import zx.sap.BringMainAndDetailByMain;
import zx.tmc.org.HrmDepartmentBean;
import zx.tmc.org.HrmJobTitleBean;
import zx.tmc.org.HrmOrgAction;
import zx.tmc.org.HrmResourceBean;
import zx.tmc.org.HrmSubCompanyBean;
import zx.tmc.org.ReturnInfo;

public class SysnOrgDataAction extends BaseCronJob{
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("组织结构集成定时开始");
		sysOrg();
		sysJobtitle();
		sysPerson();
		log.writeLog("组织结构集成定时结束");
	}
	public String sysOrg() {
		SimpleDateFormat aa = new SimpleDateFormat("yyyyMMdd");
		String result = getORGSapData(aa.format(new Date()), "X", "0000", "2359", "1");
		writeLog("sysOrg result:" + result);
		String MSG_TYPE = "";
		String MSG_TEXT = "";
		try {
			JSONObject json = new org.json.JSONObject(result);
			JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
			JSONObject jsonx = (JSONObject) jsonArr.get(0);
			MSG_TYPE = jsonx.getString("MSG_TYPE");
			MSG_TEXT = jsonx.getString("MSG_TEXT");
			writeLog("sysOrg MSG_TYPE:" + MSG_TYPE + " MSG_TEXT:" + MSG_TEXT);
			if ("S".equals(MSG_TYPE)) {
				sysSubcompany(result);
				sysDepatment(result);
			}
		} catch (Exception e) {
			writeLog("sysOrg exception:");
			writeLog(e);
		}
		return result;
	}
	
	public String sysPerson() {
		SimpleDateFormat aa = new SimpleDateFormat("yyyyMMdd");
		String result = getORGSapData(aa.format(new Date()), "X", "0000", "2359", "3");
		writeLog("sysPerson result:" + result);
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		String MSG_TYPE = "";
		String MSG_TEXT = "";
		HrmOrgAction hoa = new HrmOrgAction();
		try {
			JSONObject json = new org.json.JSONObject(result);
			JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
			JSONObject jsonx = (JSONObject) jsonArr.get(0);
			MSG_TYPE = jsonx.getString("MSG_TYPE");
			MSG_TEXT = jsonx.getString("MSG_TEXT");
			writeLog("sysPerson MSG_TYPE:" + MSG_TYPE + " MSG_TEXT:" + MSG_TEXT);
			if ("S".equals(MSG_TYPE)) {
				 jsonArr = json.getJSONObject("table").getJSONArray("Detail");
				for (int index = 0; index < jsonArr.length(); index++) {
				    jsonx = (JSONObject) jsonArr.get(index);
					JSONObject jo = jsonx.getJSONObject("dt");
					String BUKRS = jo.getString("BUKRS");// 公司 
					String ORGEH1 = jo.getString("ORGEH1");// Center ID
					String ORGEH2 = jo.getString("ORGEH2");// Division ID
					String ORGEH3 = jo.getString("ORGEH3");// Department ID
					String ORGEH4 = jo.getString("ORGEH4");// Section ID
					String KOSTL = jo.getString("KOSTL");//成本中心 
					String KTEXT = jo.getString("KTEXT");//成本中心描述
					String USRID = jo.getString("USRID");//SMEC工号 
					String PERNR = jo.getString("PERNR");// SAP工号 
					String ENAME = jo.getString("ENAME");//姓名 
					String SEX = jo.getString("SEX");// 性别 
					String GBDAT = jo.getString("GBDAT");//出生日期 
					String PLANS = jo.getString("PLANS");//职位编码
					String PARTF = jo.getString("PARTF");//兼职标识
					String PLANS_U = jo.getString("PLANS_U");//上级职位编码
					String STATV_T = jo.getString("STATV_T");//在职状态 
					String DAT01 = jo.getString("DAT01");//入职日期 
					String DATLE = jo.getString("DATLE");//离职日期
					String PTEXT = jo.getString("PTEXT");//雇用类别 
					String EMAIL = jo.getString("EMAIL");//公司邮箱 
					String PHONE = jo.getString("PHONE");//公务手机
					String JOBCL_T = jo.getString("JOBCL_T");//职务类别
					String ANSVH = jo.getString("ANSVH");//职级
					String telph = jo.getString("TELPH");//电话
					String departmentcode = "";
					String level1 = getDepartmentid(ORGEH1);
					String level2 = getDepartmentid(ORGEH2);
					String level3 = getDepartmentid(ORGEH3);
					String level4 = getDepartmentid(ORGEH4);
					if(!"00000000".equals(ORGEH4)) {
						departmentcode = ORGEH4;
					}else if(!"00000000".equals(ORGEH3)) {
						departmentcode = ORGEH3;
					}else if(!"00000000".equals(ORGEH2)) {
						departmentcode = ORGEH2;
					}else if(!"00000000".equals(ORGEH1)) {
						departmentcode = ORGEH1;
					}
					ryid = "";
					if(!"X".equals(PARTF)) {
						sql = "select id from hrmresource where workcode='"+USRID+"' and (isnull(belongto,0)<=0 or belongto ='')";
						rs.executeSql(sql);
						if(rs.next()) {
							ryid = Util.null2String(rs.getString("id"));
						}
					}
					HrmResourceBean hrb = new HrmResourceBean();
					hrb.setWorkcode(USRID);
					hrb.setLoginid(USRID);
					hrb.setLastname(ENAME);
					hrb.setPassword("123456");
					// 所属分部   部门所对应的分部   省略
					// 所属部门  0 是通过id获取  1是通过code获取   
					hrb.setDeptIdOrCode(1);
					hrb.setDepartmentCode(departmentcode);
					// 所属岗位  0 是通过id获取  1是通过code获取     
					hrb.setJobIdOrCode(0);
					hrb.setJobtitle(getJobtitle(PLANS));
					// 上级领导  0 是通过id获取  1是通过code获取      2是通过岗位获取
					hrb.setManagerIdOrCode(0);
					hrb.setManagerid(getManagerid(PLANS_U));
					hrb.setSeclevel(Util.getIntValue(getSeclevel(departmentcode,PLANS), 10));
					if("男".equals(SEX)) {
						hrb.setSex("0");
					}else if("女".equals(SEX)) {
						hrb.setSex("1");
					}
					if("X".equals(PARTF)) {
						hrb.setBelongIdOrCode(1);
						hrb.setBelongtoCode(USRID);
					}
					if(!"0000-00-00".equals(GBDAT)) {
						hrb.setBirthday(GBDAT);
					}
					if("在职".equals(STATV_T)) {
						hrb.setStatus("1");
					}else {
						hrb.setStatus("5");
					}
					if(!"0000-00-00".equals(DAT01)) {
						hrb.setDatefield1(DAT01);;
					}
					if(!"0000-00-00".equals(DATLE)) {
						hrb.setDatefield2(DATLE);
					}
					hrb.setEmail(EMAIL);
					hrb.setMobile(PHONE);
					hrb.setTelephone(telph);
					hrb.setJoblevel(ANSVH);
					hrb.addCusMap("field0", BUKRS);
					hrb.addCusMap("field1", KOSTL);
					hrb.addCusMap("field2", KTEXT);
					hrb.addCusMap("field3", PERNR);
					hrb.addCusMap("field4", PTEXT);
					hrb.addCusMap("field5", JOBCL_T);
					hrb.addCusMap("field6", level1);
					hrb.addCusMap("field7", level2);
					hrb.addCusMap("field8", level3);
					hrb.addCusMap("field9", level4);
					
					
					
					ReturnInfo rf = hoa.operResource(hrb);
					if(rf.isTure()){
						if(!"".equals(ryid)) {
							sql="update hrmresource set status='5' where belongto='"+ryid+"'";
							rs.executeSql(sql);
						}
					}else{
						writeLog("人员同步失败 ：USRID+"+USRID+" result:"+rf.getRemark());
					}
									
			}
		  }
		} catch (Exception e) {
			writeLog("sysOrg exception:");
			writeLog(e);
		}
		return result;
	}
	
	public String sysJobtitle() {
		writeLog("同步岗位开始");
		SimpleDateFormat aa = new SimpleDateFormat("yyyyMMdd");
		String result = getORGSapData(aa.format(new Date()), "X", "0000", "2359", "2");
		writeLog("sysJobtitle result:" + result);
		String MSG_TYPE = "";
		String MSG_TEXT = "";
		HrmOrgAction hoa = new HrmOrgAction();
		try {
			JSONObject json = new org.json.JSONObject(result);
			JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
			JSONObject jsonx = (JSONObject) jsonArr.get(0);
			MSG_TYPE = jsonx.getString("MSG_TYPE");
			MSG_TEXT = jsonx.getString("MSG_TEXT");
			writeLog("sysJobtitle MSG_TYPE:" + MSG_TYPE + " MSG_TEXT:" + MSG_TEXT);
			if ("S".equals(MSG_TYPE)) {
				 jsonArr = json.getJSONObject("table").getJSONArray("Detail");
				for (int index = 0; index < jsonArr.length(); index++) {
				    jsonx = (JSONObject) jsonArr.get(index);
					JSONObject jo = jsonx.getJSONObject("dt");
					HrmJobTitleBean hjt = new HrmJobTitleBean();
					String PLANS = jo.getString("PLANS");// 职位编码
					String PLSTX1 = jo.getString("PLSTX1");// 职位名称
					String STELL = jo.getString("STELL");// 职务编码
					String PLSTX2 = jo.getString("PLSTX2");// 职务名称
					String OBJID = jo.getString("OBJID");// 所属组织
					String PERNR = jo.getString("PERNR");// 员工编号
					String ENAME = jo.getString("ENAME");// 员工姓名
					String DFLAG = jo.getString("DFLAG");// 删除标识
					if("".equals(PLSTX2)) {
						PLSTX2 = PLSTX1; 
					}
					hjt.setJobtitlecode(PLANS);
					hjt.setJobtitlename(PLSTX1);
					hjt.setJobtitlemark(PLSTX1);
					hjt.setJobtitleremark(PLSTX1);
					// 所属部门  0 是通过id获取  1是通过code获取
					hjt.setDeptIdOrCode(1);
					hjt.setJobdepartmentCode(OBJID);
					hjt.setSuperJobCode("");
					hjt.setJobactivityName(PLSTX2);
					hjt.setJobGroupName(PLSTX2);
					// 执行结果  可以直接打印result 查看直接结果   
					ReturnInfo rf = hoa.operJobtitle(hjt);
					if(!rf.isTure()){
						writeLog("岗位同步失败 ：PLANS：" + PLANS + " " + rf.getRemark());
					}					
			}
		  }
		} catch (Exception e) {
			writeLog("sysJobtitle exception:");
			writeLog(e);
		}
		writeLog("同步岗位结束");
		return result;
	}
	
	public void sysSubcompany(String jsonstr) throws Exception {
		writeLog("同步分部开始");
		JSONObject json = new JSONObject(jsonstr);
		HrmOrgAction hoa = new HrmOrgAction();
		JSONArray jsonArr = json.getJSONObject("table").getJSONArray("Detail");
		for (int index = 0; index < jsonArr.length(); index++) {
			JSONObject jsonx = (JSONObject) jsonArr.get(index);
			JSONObject jo = jsonx.getJSONObject("dt");
			String OBJID = jo.getString("OBJID");// 组织编码
			String STEXT = jo.getString("STEXT");// 组织名称
			String HIERA = jo.getString("HIERA");// 组织层级
			String DDTEXT = jo.getString("DDTEXT");// 层级描述
			String OBJID_U = jo.getString("OBJID_U");// 上级组织
			String PLANS = jo.getString("PLANS");// 领导职位
			String DFLAG = jo.getString("DFLAG");// 删除标识
			if ("00000000".equals(OBJID_U)) {
				OBJID_U = "";
			}
			if ("0".equals(HIERA)) {
				HrmSubCompanyBean hsb = new HrmSubCompanyBean();
				hsb.setSubCompanyCode(OBJID);
				hsb.setSubCompanyName(STEXT);
				hsb.setSubCompanyDesc(STEXT);
				hsb.setIdOrCode(1);
				hsb.setSuperCode(OBJID_U);
				hsb.setOrderBy(0);
				// 状态 0正常 1封装
				if ("X".equals(DFLAG)) {
					hsb.setStatus(1);
				} else {
					hsb.setStatus(0);
				}
				ReturnInfo result = hoa.operSubCompany(hsb);
				//
				if (!result.isTure()) {
					writeLog("公司同步失败 ：OBJID：" + OBJID + " " + result.getRemark());
				}
			} else {
				continue;
			}

		}
		try {
			SubCompanyComInfo SubCompanyComInfo = new SubCompanyComInfo();
			SubCompanyComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
			writeLog(e);
		}
		writeLog("同步分部结束");
	}
	
	public void sysDepatment(String jsonstr) throws Exception {
		writeLog("同步部门开始");
		JSONObject json = new JSONObject(jsonstr);
		HrmOrgAction hoa = new HrmOrgAction();
		JSONArray jsonArr = json.getJSONObject("table").getJSONArray("Detail");
		for (int index = 0; index < jsonArr.length(); index++) {
			JSONObject jsonx = (JSONObject) jsonArr.get(index);
			JSONObject jo = jsonx.getJSONObject("dt");
			String OBJID = jo.getString("OBJID");// 组织编码
			String STEXT = jo.getString("STEXT");// 组织名称
			String HIERA = jo.getString("HIERA");// 组织层级
			String DDTEXT = jo.getString("DDTEXT");// 层级描述
			String OBJID_U = jo.getString("OBJID_U");// 上级组织
			String PLANS = jo.getString("PLANS");// 领导职位
			String DFLAG = jo.getString("DFLAG");// 删除标识
			if ("00000000".equals(OBJID_U)) {
				OBJID_U = "";
			}
			if (!"0".equals(HIERA)) {
				HrmDepartmentBean hdb = new HrmDepartmentBean();
				hdb.setDepartmentcode(OBJID);
				hdb.setDepartmentname(STEXT);
				hdb.setDepartmentark(STEXT);
				// 分部的获取操作方式     0 是通过id获取  1是通过code获取
				hdb.setComIdOrCode(0);
				hdb.setSubcompanyid1(getSubcompanyid(OBJID_U,HIERA));
				// 上级的操作方式     0 是通过id获取  1是通过code获取
				hdb.setIdOrCode(1);
				if("1".equals(HIERA)) {
					hdb.setSuperCode("");
				}else {
					hdb.setSuperCode(OBJID_U);
				}
				
				//排序字段
				hdb.setOrderBy(0);
				if("X".equals(DFLAG)) {
					hdb.setStatus(1);
				}else {
					hdb.setStatus(0);
				}
				hdb.addCusMap("HIERA", HIERA);
				hdb.addCusMap("DDTEXT", DDTEXT);
				hdb.addCusMap("PLANS", PLANS);
				ReturnInfo result = hoa.operDept(hdb);
				if (!result.isTure()) {
					writeLog("部门同步失败 ：OBJID：" + OBJID + " " + result.getRemark());
				}
			} else {
				continue;
			}

		}
		try {
			DepartmentComInfo DepartmentComInfo= new DepartmentComInfo();
			DepartmentComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
			writeLog(e);
		}
		writeLog("同步部门结束");
	}
	/**
	 * 获取部门id
	 * @param code
	 * @return
	 */
	public String getDepartmentid(String code) {
		RecordSet rs = new RecordSet();
		String deptid="";
		if(!"".equals(code) && !"00000000".equals(code)) {			
			String sql = "select id from HrmDepartment where departmentcode = '" + code + "'";
			rs.executeSql(sql);
			if(rs.next()) {
				deptid  = Util.null2String(rs.getString("id"));
			}
		}
		return deptid;
	}
	
	public String getSubcompanyid(String OBJID_U,String HIERA) {
		RecordSet rs = new RecordSet();
		String sql = "";		
		String subid = "";
		if("".equals(OBJID_U)) {
			return "";
		}
		if("1".equals(HIERA)) {
			sql = "select id from hrmsubcompany where subcompanycode='"+OBJID_U+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				subid  = Util.null2String(rs.getString("id"));
			}
		}else {
			sql = "select subcompanyid1 from HrmDepartment where departmentcode = '"+OBJID_U+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				subid  = Util.null2String(rs.getString("subcompanyid1"));
			}
		}
		return subid;
		
	}
	public String getORGSapData(String IV_DATE, String IV_SELECT_ALL, String IV_TIME_LOW, String IV_TIME_HIGH,
			String workflowId) {
		Map<String, String> oaDatas = new HashMap<String, String>();
		oaDatas.put("IV_DATE", IV_DATE);
		oaDatas.put("IV_SELECT_ALL", IV_SELECT_ALL);
		oaDatas.put("IV_TIME_LOW", IV_TIME_LOW);
		oaDatas.put("IV_TIME_HIGH", IV_TIME_HIGH);
		BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("2");
		String result = bmb.getReturn(oaDatas, workflowId, "", null, "org");
		return result;
	}
	
	public String getJobtitle(String jobtitleCode) {
		RecordSet rs = new RecordSet();
		String jobid = "";
		String sql = "select id from HrmJobTitles where jobtitlecode='"+jobtitleCode+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			jobid = Util.null2String(rs.getString("id"));
		}
		return jobid;
	}
	
	public String getSeclevel(String departmentcode,String PLANS) {
		RecordSet rs = new RecordSet();
		String seclevel = "10";
		String hiera = "";
		String sql = "select a.hiera from HrmDepartmentDefined a,HrmDepartment b where a.deptid=b.id and a.plans='"+PLANS+"' and b.departmentcode='"+departmentcode+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			hiera = Util.null2String(rs.getString("hiera"));
		}
		if("1".equals(hiera)) {
			seclevel = "90";
		}else if("2".equals(hiera)) {
			seclevel = "70";
		}else if("3".equals(hiera)) {
			seclevel = "50";
		}else if("4".equals(hiera)) {
			seclevel = "30";
		}else {
			seclevel = "10";
		}
		
		if("30000000".equals(PLANS)) {
			seclevel = "100";
		}
		return seclevel;
	}
	
	public String getManagerid(String PLANS_U) {
		RecordSet rs = new RecordSet();
		String managerid = "";
		if("00000000".equals(PLANS_U)) {
			managerid = "";
		}
		String sql = "select b.id from HrmJobTitles a,hrmresource b where a.id=b.jobtitle and a.jobtitlecode='"+PLANS_U+"' and b.status<5 order by id desc";
		rs.executeSql(sql);
		if(rs.next()) {
			managerid = Util.null2String(rs.getString("id"));
		}
		return managerid;
	}
	
	private void writeLog(Object obj) {
		if (true) {
			new BaseBean().writeLog(this.getClass().getName(), obj);
		}
	}

}
