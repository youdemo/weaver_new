package gvo.hr.sysn.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import gvo.hr.sysn.tmc.org.HrmDepartmentBean;
import gvo.hr.sysn.tmc.org.HrmJobTitleBean;
import gvo.hr.sysn.tmc.org.HrmOrgAction;
import gvo.hr.sysn.tmc.org.HrmSubCompanyBean;
import gvo.hr.sysn.tmc.org.ReturnInfo;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.job.JobActivitiesComInfo;
import weaver.hrm.job.JobGroupsComInfo;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.job.JobTitlesTempletComInfo;

public class SysnHrOrgWebserviceImpl20190509 {

	/**
	 * 同步公司组织信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */
	public String sysSubcompanyInfo(String jsonString, JSONArray resultArr, String num) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray ja = json.getJSONArray("depts");
		HrmOrgAction hoa = new HrmOrgAction();
		String sign = "";
		// 先同步公司
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String orgType = jo.getString("orgType");
			if (!"10".equals(orgType)) {
				continue;
			}
			HrmSubCompanyBean hsb = new HrmSubCompanyBean();
			hsb.setSubCompanyCode(jo.getString("deptid"));
			hsb.setSubCompanyName(jo.getString("descr"));
			hsb.setSubCompanyDesc(jo.getString("descr"));
			hsb.setIdOrCode(1);
			
			if(jo.getString("parDeptId").equals("100000")){
				hsb.setSuperCode("0");
			}else{
				hsb.setSuperCode(jo.getString("parDeptId"));
			}
			//hsb.setSuperCode(jo.getString("parDeptId"));
			hsb.setOrderBy(0);
			String status = jo.getString("status");
			if ("I".equals(status)) {
				hsb.setStatus(1);
			} else {
				hsb.setStatus(0);
			}
			ReturnInfo result = hoa.operSubCompany(hsb);
			//
			JSONObject resutlJo = new JSONObject();
			if (!result.isTure()) {
				resutlJo = getResultJo(jo.getString("deptid"), "", jo.getString("lastupdttm"), "E", result.getRemark());
				sign = "E";
			} else {
				resutlJo = getResultJo(jo.getString("deptid"), "", jo.getString("lastupdttm"), "S", "OK");
			}
			if ("2".equals(num)) {
				if (resutlJo != null) {
					resultArr.put(resutlJo);
				}
			}
		}
		try {
			SubCompanyComInfo SubCompanyComInfo = new SubCompanyComInfo();
			SubCompanyComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 同步部门组织信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */
	public String sysDepartmentInfo(String jsonString, JSONArray resultArr, String num) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray ja = json.getJSONArray("depts");
		HrmOrgAction hoa = new HrmOrgAction();
		String sign = "";
		// 先同步公司
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			String orgType = jo.getString("orgType");
			if ("10".equals(orgType)) {
				continue;
			}

			HrmDepartmentBean hdb = new HrmDepartmentBean();
			hdb.setDepartmentcode(jo.getString("deptid"));
			hdb.setDepartmentname(jo.getString("descr"));
			hdb.setDepartmentark(jo.getString("descr"));
			String subcompanyid = issubcompany(jo.getString("parDeptId"));
			if (!"".equals(subcompanyid)) {
				hdb.setComIdOrCode(0);
				hdb.setSubcompanyid1(subcompanyid);
				hdb.setIdOrCode(0);
				hdb.setSuperID("0");
			} else {

				hdb.setIdOrCode(1);
				hdb.setSuperCode(jo.getString("parDeptId"));

				subcompanyid = getSubcompanyid(jo.getString("parDeptId"));
				hdb.setComIdOrCode(0);
				hdb.setSubcompanyid1(subcompanyid);
			}

			// 排序字段
			hdb.setOrderBy(0);
			// 状态 0正常 1封装
			String status = jo.getString("status");
			if ("I".equals(status)) {
				hdb.setStatus(1);
			} else {
				hdb.setStatus(0);
			}
			hdb.addCusMap("zzlx", orgType);
			ReturnInfo result = hoa.operDept(hdb);
			JSONObject resutlJo = new JSONObject();
			if (!result.isTure()) {
				resutlJo = getResultJo(jo.getString("deptid"), "", jo.getString("lastupdttm"), "E", result.getRemark());
				sign = "E";
			} else {
				resutlJo = getResultJo(jo.getString("deptid"), "", jo.getString("lastupdttm"), "S", "OK");
			}
			if ("2".equals(num)) {
				if (resutlJo != null) {
					resultArr.put(resutlJo);
				}
			}

		}
		try {
			DepartmentComInfo DepartmentComInfo= new DepartmentComInfo();
			DepartmentComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 同步职务信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */

	public String sysJobActivityInfo(String jsonString, JSONArray resultArr) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray ja = json.getJSONArray("jobs");
		RecordSet rs = new RecordSet();
		HrmOrgAction hoa = new HrmOrgAction();
		String jobcode = "";
		String descr = "";
		String descrShort = "";
		String status = "";
		String sign = "";
		String sql = "";
		int count = 0;
		for (int i = 0; i < ja.length(); i++) {
			JSONObject jo = ja.getJSONObject(i);
			jobcode = Util.null2String(jo.getString("jobcode"));
			descr = Util.null2String(jo.getString("descr"));
			descrShort = Util.null2String(jo.getString("descrShort"));
			status = Util.null2String(jo.getString("status"));
			if ("".equals(jobcode)) {
				continue;
			}
			count = 0;
			sql = "select count(id) as count from uf_hr_activitycode where zwid = '" + jobcode + "'";
			rs.executeSql(sql);
			if (rs.next()) {
				count = rs.getInt("count");
			}
			if (count > 0) {
				sql = "update uf_hr_activitycode set zwms = '" + descr + "' , zwjc = '" + descrShort
						+ "',sxzt = '" + status + "' where zwid = '" + jobcode + "'";
				rs.executeSql(sql);
			} else {
				sql = "insert into uf_hr_activitycode(zwid,zwms,zwjc,sxzt) values ('" + jobcode + "','" + descr + "','"
						+ descrShort + "','" + status + "')";
				rs.executeSql(sql);
			}

			ReturnInfo ri = hoa.operJobActivities(descr, descrShort);
			JSONObject resutlJo = new JSONObject();
			if (!ri.isTure()) {
				sign = "E";
				resutlJo = getResultJo(jo.getString("jobcode"), "", jo.getString("lastupdttm"), "E", ri.getRemark());
			} else {
				resutlJo = getResultJo(jo.getString("jobcode"), "", jo.getString("lastupdttm"), "S", "OK");
			}

			if (resutlJo != null) {
				resultArr.put(resutlJo);
			}

		}
		try {
			JobGroupsComInfo jobGroupsComInfo = new JobGroupsComInfo();
			jobGroupsComInfo.removeCompanyCache();
			JobActivitiesComInfo jobActivitiesComInfo = new JobActivitiesComInfo();
			jobActivitiesComInfo.removeJobActivitiesCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 同步岗位信息
	 * 
	 * @param jsonString
	 * @throws JSONException
	 */
	public String sysJobTitleInfo(String jsonString, JSONArray resultArr) throws Exception {
		RecordSet rs = new RecordSet();
		JSONObject json = null;
		JSONArray jsa = null;
		String zwms = "";// 职务描述
		String zwjc = "";// 职务简称
		String sign = "";
		json = new JSONObject(jsonString);
		jsa = json.getJSONArray("positions");
		int count = 0;
		for (int i = 0; i < jsa.length(); i++) {
			HrmOrgAction hoa = new HrmOrgAction();
			JSONObject json1 = jsa.getJSONObject(i);
			String positionNbr = json1.getString("positionNbr");// 职位ID
			String descr = json1.getString("descr");// 职位描述
			String descrShort = json1.getString("descrShort");// 职位简称
			String status = json1.getString("status");// 生效状态
			String deptId = json1.getString("deptId");// 部门ID
			String jobcode = json1.getString("jobcode");// 职务ID
			String locationId = json1.getString("locationId");// 地点ID
			String channel = json1.getString("channel");// 通道
			String sequence = json1.getString("sequence");// 序列
			String actionFlag = json1.getString("actionFlag");// 数据操作标识
			String vacancies = json1.getString("vacancies");// 编制人数
			String graderank = json1.getString("graderank");// 职等区间
			String reportPosn = json1.getString("reportPosn");// 直接上级职位
			String damagePosn = json1.getString("damagePosn");// 危害岗位
			JSONObject resutlJo = new JSONObject();
			String sql = "select id,zwms,zwjc from uf_hr_activitycode where zwid ='" + jobcode + "' ";
			rs.executeSql(sql);
			if (rs.next()) {
				zwms = Util.null2String(rs.getString("zwms"));
				zwjc = Util.null2String(rs.getString("zwjc"));
			}
			///
			if(zwms.equals("")){
				resutlJo = getResultJo(positionNbr, "", json1.getString("lastupdttm"), "E", "职务不存在");
				sign = "E";
				resultArr.put(resutlJo);
				continue;
			}
			//////
			count = 0;
			sql = "select count(id) as count from uf_hr_jobtitlecode where positionNbr = '" + positionNbr + "'";
			rs.executeSql(sql);
			if (rs.next()) {
				count = rs.getInt("count");
			}
			if (count > 0) {
				sql = "update uf_hr_jobtitlecode set jobcode = '" + jobcode + "',locationId = '" + locationId
						+ "',channel='" + channel + "',sequence='" + sequence + "',descr='" + descr + "',descrShort='" + descrShort + "',status='" + status + "',deptId='" + deptId + "'" + 
						",vacancies='" + vacancies + "',graderank='" + graderank + "',reportPosn='"+reportPosn+"',damagePosn='"+damagePosn+"' where positionNbr = '" + positionNbr+ "'";
				rs.executeSql(sql);
			} else {
				sql = "insert into uf_hr_jobtitlecode(positionNbr,jobcode,locationId,channel,sequence,descr,descrShort,status,deptId,vacancies,graderank,reportPosn,damagePosn) values ('"
						+ positionNbr + "','" + jobcode + "','" + locationId + "','" + channel + "','" + sequence+ "','" + descr+ "','" + descrShort+ "','" + status+ "','" + deptId+ "','" + vacancies+ "','" + graderank+ "','"+reportPosn+"','"+damagePosn+"')";
				rs.executeSql(sql);
			}
			HrmJobTitleBean hjt = new HrmJobTitleBean();
			hjt.setDeptIdOrCode(1);
			hjt.setJobtitlecode(positionNbr);
			hjt.setJobtitlename(descr);
			hjt.setJobtitlemark(descr);
			hjt.setJobtitleremark(descrShort);
			// 所属部门 0 是通过id获取 1是通过code获取
			hjt.setDeptIdOrCode(1);
			hjt.setJobdepartmentCode(deptId);
			hjt.setSuperJobCode("");
			// 职位 直接通过字段去查询，没有就添加，有就直接获取
			hjt.setJobactivityName(zwms);
			hjt.setJobGroupName(zwms);
			ReturnInfo result = hoa.operJobtitle(hjt);
			
			if (!result.isTure()) {
				resutlJo = getResultJo(positionNbr, "", json1.getString("lastupdttm"), "E", result.getRemark());
				sign = "E";
			} else {
				resutlJo = getResultJo(positionNbr, "", json1.getString("lastupdttm"), "S", "OK");
			}

			if (resutlJo != null) {
				resultArr.put(resutlJo);
			}

		}
		try {
			JobTitlesTempletComInfo JobTitlesTempletComInfo = new JobTitlesTempletComInfo();
			JobTitlesTempletComInfo.removeJobTitlesTempletCache();
			
            JobTitlesComInfo JobTitlesComInfo= new JobTitlesComInfo();
            JobTitlesComInfo.removeJobTitlesCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;
	}

	/**
	 * 同步人员职务信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */
	public String sysPersonJobRelation(String jsonString, JSONArray resultArr) throws Exception {
		String sign = "";
		String pc = "";
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "select nvl(max(nvl(pc,0)),0)+1 as pc from uf_personneljob";
		rs.executeSql(sql);
		if(rs.next()) {
			pc = Util.null2String(rs.getString("pc"));
		}
		log.writeLog("sysPersonJobRelation pc:"+pc);
		insertPersonJobRelationInfo(jsonString,pc);
		doSysPersonJobRelation(resultArr, "1",pc);
		sign = doSysPersonJobRelation(resultArr, "2",pc);
		log.writeLog("sysPersonJobRelation done pc:"+pc);
		return sign;
	}
	
	public String sysPersonJobRelationALL(String jsonString, JSONArray resultArr) throws Exception {
		String sign = "S";
		String pc = "0";
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("sysPersonJobRelationALL pc:"+pc);
		insertPersonJobRelationInfo(jsonString,pc);
		//doSysPersonJobRelation(resultArr, "1",pc);
		//sign = doSysPersonJobRelation(resultArr, "2",pc);
		JSONObject resutlJo = new JSONObject();
		resutlJo = getResultJo("", "", "", "S",
				"插入全量数据成功");
		if (resutlJo != null) {
			resultArr.put(resutlJo);
		}
		log.writeLog("sysPersonJobRelationALL done pc:"+pc);
		return sign;
	}

	/**
	 * 执行同步人员职务信息
	 * 
	 * @throws Exception
	 */
	public String doSysPersonJobRelation(JSONArray resultArr, String num,String pc) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String sql_dt = "";
		String emplid = "";
		String ryid = "";
		String sign = "";
		String sign1 = "";
		sql = "select distinct emplid from uf_personneljob where pc='"+pc+"'";
		rs.executeSql(sql);
		while (rs.next()) {
			emplid = Util.null2String(rs.getString("emplid"));
			ryid = checkPersonIsexits(emplid);
			if ("".equals(ryid)) {
				if ("2".equals(num)) {
					sql_dt = "select * from uf_personneljob where emplid='" + emplid + "' and pc='"+pc+"'";
					rs_dt.executeSql(sql_dt);
					while (rs_dt.next()) {
						JSONObject resutlJo = new JSONObject();
						sign = "E";
						resutlJo = getResultJo(emplid, rs_dt.getString("emplRcd"), rs_dt.getString("lastupdttm"), "E",
								"员工基本信息不存在");
						if (resutlJo != null) {
							resultArr.put(resutlJo);
						}
					}
				}
				continue;
			}
			sign1 = updateInsertPersonJob(emplid, ryid, resultArr, num,pc);
			if("E".equals(sign1)) {
				sign = "E";
			}
		}
		return sign;
	}

	/**
	 * 更新人员的岗位信息
	 */
	public String updateInsertPersonJob(String emplid, String ryid, JSONArray resultArr, String num,String pc) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		HrmOrgAction hoa = new HrmOrgAction();
		ReturnInfo ri = new ReturnInfo();
		String emplRcd = "";// 员工记录
		String jobIndiactor = "";// P主岗S兼岗
		String actionFlag = "";// 数据操作标识 A新增U更新
		String positionNbr = "";// 岗位id
		String jobid = "";//
		String jkryid = "";// 兼岗人员id
		String hr_status = "";// 人员状态
		String repordsTold = "";// 直接上级工号
		String repordsToPosn = "";// 直接上级岗位
		String lastupdttm = "";// 最后更新日期
		String managerid = "";
		String idenCategory = "";//身份类别
		String id = "";
		String insertFlag = "0";
		String sign = "";
		String loginid = "";
		String seclevel = "";
		String yglb = "";//员工类型 0直接 1间接
		
		String sql_dt = "";
		String sql = "select loginid,seclevel from hrmresource where id="+ryid;
		rs.executeSql(sql);
		if(rs.next()) {
			loginid = Util.null2String(rs.getString("loginid"));
			seclevel = Util.null2String(rs.getString("seclevel"));
		}
		sql = "select field25 from cus_fielddata where scopeid=-1 and id="+ryid;
		rs.executeSql(sql);
		if(rs.next()) {
			yglb = Util.null2String(rs.getString("field25"));
		}
		sql = "select * from uf_personneljob where emplid='" + emplid + "' and pc='"+pc+"'";
		rs.executeSql(sql);
		while (rs.next()) {
			Map<String, String> jobMap = new HashMap<String, String>();
			Map<String, String> jobCusMap = new HashMap<String, String>();
			JSONObject resutlJo = new JSONObject();
			insertFlag = "0";
			jobid = "";
			jkryid = "";
			managerid = "";
			id = Util.null2String(rs.getString("id"));
			emplRcd = Util.null2String(rs.getString("emplRcd"));
			jobIndiactor = Util.null2String(rs.getString("jobIndiactor"));
			actionFlag = Util.null2String(rs.getString("actionFlag"));
			positionNbr = Util.null2String(rs.getString("positionNbr"));
			hr_status = Util.null2String(rs.getString("hr_status"));
			repordsTold = Util.null2String(rs.getString("repordsTold"));
			repordsToPosn = Util.null2String(rs.getString("repordsToPosn"));
			lastupdttm = Util.null2String(rs.getString("lastupdttm"));
			idenCategory = Util.null2String(rs.getString("idenCategory"));
			jobid = getJobtitleId(positionNbr);
			if ("".equals(jobid)) {
				if ("2".equals(num)) {
					resutlJo = getResultJo(emplid, emplRcd, lastupdttm, "E",
							"岗位不存在");
					sign = "E";
					if (resutlJo != null) {
						resultArr.put(resutlJo);
					}
				}
				log.writeLog("该岗位不存在 emplid:" + emplid + " positionNbr:" + positionNbr);
				continue;
			}
			if ("U".equals(actionFlag)) {
				if ("S".equals(jobIndiactor)) {// 兼岗更新
					jkryid = getJgryid(emplid, emplRcd);
					if ("".equals(jkryid)) {
						if ("2".equals(num)) {
							resutlJo = getResultJo(emplid, emplRcd, lastupdttm, "E",
									"该人员兼岗不存在");
							sign = "E";
							if (resutlJo != null) {
								resultArr.put(resutlJo);
							}
						}
						log.writeLog("该人员兼岗不存在 emplid:" + emplid + " positionNbr:" + positionNbr);
						continue;
					}

				} else {// 主岗更新
					jkryid = ryid;
				}

				jobMap.put("ryid", jkryid);

			} else if ("A".equals(actionFlag)) {
				if ("S".equals(jobIndiactor)) {// 兼岗新建
					jkryid = getJgryid(emplid, emplRcd);// 判断是否已经新建过
					if ("".equals(jkryid)) {
						insertFlag = "1";
						jobMap.put("belongto", ryid);
						jobMap.put("workcode", emplid+emplRcd);
					} else {
						jobMap.put("ryid", jkryid);
					}
				} else {
					jobMap.put("ryid", ryid);
				}
			}
			String ryids = Util.null2String(jobMap.get("ryid"));
			if("".equals(ryids)) {
				seclevel = "";
			}
			if(!"".equals(ryids)) {
				sql_dt = "select seclevel from hrmresource where id="+ryids;
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()) {
					seclevel = Util.null2String(rs_dt.getString("seclevel"));
				}
			}
			if ("A".equals(actionFlag) && "".equals(seclevel)) {
				if ("10".equals(idenCategory)) {
					if ("S".equals(jobIndiactor)) {
						jobMap.put("seclevel", "10");
					}else {

						jobMap.put("seclevel", "0");
					}
				}else {
					if ("S".equals(jobIndiactor)) {
						jobMap.put("seclevel", "10");
					}else {
						jobMap.put("seclevel", "11");
					}
				}
			}else {
				if("0".equals(yglb) && "20".equals(idenCategory)) {
					jobMap.put("seclevel", "11");
				}
			}
			
			
			//直接员工主岗loginid更新成工号 2018-11-13
			if("S".equals(jobIndiactor)) {
				String jkloginid = "";
				String jkid = getJgryid(emplid, emplRcd);
				if(!"".equals(jkid)) {
					sql_dt = "select loginid from hrmresource where id="+jkid;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						jkloginid = Util.null2String(rs_dt.getString("loginid"));
					}
					if("".equals(jkloginid)) {
						sql_dt = "select max(id)+1 as id from hrmresource where workcode like '" + emplid + "%' and id <"+jkid;
						rs_dt.executeSql(sql_dt);
						if(rs_dt.next()) {
							jkloginid = loginid+Util.null2String(rs_dt.getString("id"));
						}
						jobMap.put("loginid",jkloginid);
					}else {
						jobMap.put("loginid",jkloginid);
					}
					
					
				}else {
					sql_dt = "select max(id)+1 as id from hrmresource where workcode like '" + emplid + "%'";
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						jkloginid = loginid+Util.null2String(rs_dt.getString("id"));
					}
					jobMap.put("loginid",jkloginid);
				}
				
			}else {
				if("".equals(loginid) && "10".equals(idenCategory)) {
					jobMap.put("loginid",emplid);
				}else {
					jobMap.put("loginid",loginid);
				}
			}
			if ("A".equals(hr_status)) {
				jobMap.put("status", "1");
			} else {
				jobMap.put("status", "5");
				jobMap.put("loginid","");
			}
			jobMap.put("datefield1", Util.null2String(rs.getString("hireDt")));// 入职日期
			jobMap.put("datefield2", Util.null2String(rs.getString("terDt")));// 离职日期
			jobMap.put("datefield4", Util.null2String(rs.getString("enterGpDt")));// 集团入职日期
			jobMap.put("jobtitle", jobid);
			jobMap.put("deptId", Util.null2String(rs.getString("deptId")));// 部门code
			jobCusMap.put("field31", psTransMethod("locationId", Util.null2String(rs.getString("locationId"))));// 工作地点
			if ("10".equals(idenCategory)) {
				jobCusMap.put("field25", "0");
			} else if ("20".equals(idenCategory)) {
				jobCusMap.put("field25", "1");
			} else {
				jobCusMap.put("field25", "");
			}
			jobCusMap.put("field32", Util.null2String(rs.getString("channel")));
			jobCusMap.put("field33", Util.null2String(rs.getString("sequence")));
			//jobCusMap.put("field34", Util.null2String(rs.getString("grade")));
			//if (!"".equals(repordsTold) && !"".equals(repordsToPosn)) { 2018-11-29
			if (!"".equals(repordsTold)) {
				managerid = getZgryid(repordsTold, getJobtitleId(repordsToPosn));
				if ("".equals(managerid)) {
					managerid = getJgry(repordsTold, getJobtitleId(repordsToPosn));
				}
				if("".equals(managerid)) {
					managerid = getOtherRyid(repordsTold,getJobtitleId(repordsToPosn));
				}
			}
			jobMap.put("managerid", managerid);
			if ("U".equals(actionFlag)) {
				ri = hoa.updatePersonJobInfo(jobMap, jobCusMap);
			} else if ("A".equals(actionFlag)) {
				if ("1".equals(insertFlag)) {
					ri = hoa.insertPersonJobInfo(jobMap, jobCusMap);
				} else {
					ri = hoa.updatePersonJobInfo(jobMap, jobCusMap);
				}

			}
			sql_dt = "update uf_personneljob set sfcl = '1' where id=" + id;
			rs_dt.executeSql(sql_dt);
			if (!ri.isTure()) {
				sign = "E";
				resutlJo = getResultJo(emplid, emplRcd, lastupdttm, "E",
						ri.getRemark());
			} else {
				resutlJo = getResultJo(emplid, emplRcd, lastupdttm, "S", "OK");
			}

			if ("2".equals(num)) {
				if (resutlJo != null) {
					resultArr.put(resutlJo);
				}
			}

		}
		return sign;
	}

	/**
	 * 同步人员基本信息
	 * 
	 * @param jsonString
	 * @throws Exception
	 */
	public String SysHrmresourceInfo(String jsonString, JSONArray resultArr) throws Exception {
		JSONObject json = new JSONObject(jsonString);
		JSONArray jsa = json.getJSONArray("employees");
		String sex = "";
		String sign = "";
		String residentplace = "";// 工作性质
		HrmOrgAction hoa = new HrmOrgAction();
		for (int i = 0; i < jsa.length(); i++) {
			Map<String, String> comMap = new HashMap<String, String>();
			Map<String, String> cusMap = new HashMap<String, String>();
			Map<String, String> cusMap1 = new HashMap<String, String>();
			JSONObject jo = jsa.getJSONObject(i);
			comMap.put("workcode", Util.null2String(jo.getString("emplid")));// 员工工号
			comMap.put("lastname", Util.null2String(jo.getString("name")));// 姓名
			comMap.put("loginid", Util.null2String(jo.getString("loginName")));// 登陆帐号
			comMap.put("email", Util.null2String(jo.getString("email")));// 电子邮件
			sex = Util.null2String(jo.getString("sex"));// 性别
			if ("F".equals(sex)) {
				comMap.put("sex", "1");
			} else if ("M".equals(sex)) {
				comMap.put("sex", "0");
			} else {
				comMap.put("sex", "");
			}
			comMap.put("birthday", Util.null2String(jo.getString("birthDate")));// 生日
			comMap.put("telephone", Util.null2String(jo.getString("phone")));// 电话
			comMap.put("mobile", Util.null2String(jo.getString("cellPhone")));// 手机
			comMap.put("certificatenum", Util.null2String(jo.getString("Id")));// 身份证
			comMap.put("folk", psTransMethod("nation", Util.null2String(jo.getString("nation"))));// 民族
			comMap.put("tempresidentnumber", Util.null2String(jo.getString("accountNumber")));// 银行账户
			comMap.put("startdate", Util.null2String(jo.getString("contractStartDt")));// 合同开始日期
			comMap.put("enddate", Util.null2String(jo.getString("contractEndDt")));// 合同结束日期
			residentplace = Util.null2String(jo.getString("category"));// 工作性质
			if ("10".equals(residentplace)) {
				comMap.put("residentplace", "A");
			} else if ("30".equals(residentplace)) {
				comMap.put("residentplace", "B");
			} else {
				comMap.put("residentplace", "");
			}
			cusMap.put("field37", Util.null2String(jo.getString("contractType")));// 合同类型//??
			cusMap.put("field36", Util.null2String(jo.getString("socialArea")));// 社保缴纳地

			cusMap1.put("field30", Util.null2String(jo.getString("accountName")));// 账户名称
			cusMap1.put("field28", Util.null2String(jo.getString("accountBank")));// 开户行名称
			cusMap1.put("field29", Util.null2String(jo.getString("coupletNumber")));// 联行号
			cusMap.put("field40", Util.null2String(jo.getString("Party")));// 甲方主体
			
			ReturnInfo ri = hoa.insertUpdatePerson(comMap, cusMap, cusMap1);
			JSONObject resutlJo = new JSONObject();
			if (!ri.isTure()) {
				sign = "E";
				resutlJo = getResultJo(jo.getString("emplid"), "", jo.getString("lastupdttm"), "E", ri.getRemark());
			} else {
				resutlJo = getResultJo(jo.getString("emplid"), "", jo.getString("lastupdttm"), "S", "OK");
			}

			if (resutlJo != null) {
				resultArr.put(resutlJo);
			}

		}
		return sign;
	}

	/**
	 * 同步矩阵信息
	 * 
	 * @param jsonString
	 * @throws Exception 
	 */
	public String SysMatrixInfo(String jsonString, JSONArray resultArr) throws Exception {
		 JSONObject json = new JSONObject(jsonString);
		 JSONArray ja = json.getJSONArray("deptMatrix");
		 RecordSet rs = new RecordSet();
		 String sql = "";
		 String depcode = "";//部门编号
		 String depid = "";//部门id
		 String subid = "";//分部id
		 String zzlx = "";//组织类型
		 String zg = "";//主管
		 String bmjl = "";//部门经理
		 String zxzjl = "";//中心总经理
		 String fzc = "";//副总裁
		 String zc = "";//总裁
		 String sign = "S";
		 String lastupdttm = "";
		 for (int i = 0; i < ja.length(); i++) {
			 depid = "";
			 depcode = "";
			 zzlx = "";
			 zg = "";
			 bmjl = "";
			 zxzjl = "";
			 fzc = "";
			 zc = "";
			 JSONObject jo = ja.getJSONObject(i);
			 JSONObject resutlJo = new JSONObject();
			 depcode = Util.null2String(jo.getString("deptid"));
			 lastupdttm = Util.null2String(jo.getString("lastupdttm"));
			 sql = "select id,subcompanyid1 from hrmdepartment where departmentcode='"+depcode+"'";
			 rs.execute(sql);
			 if(rs.next()) {
				 depid = Util.null2String(rs.getString("id"));
				 subid = Util.null2String(rs.getString("subcompanyid1"));
			 }
			 if("".equals(depid)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "", lastupdttm, "E", "部门不存在");
				 resultArr.put(resutlJo);
				 continue;
			 }
			 if("".equals(subid)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "部门所属公司不存在");
				 resultArr.put(resutlJo);
				 continue;
			 }
			 sql="select zzlx from hrmdepartmentdefined where deptid="+depid;
			 rs.executeSql(sql);
			 if(rs.next()) {
				 zzlx = Util.null2String(rs.getString("zzlx"));
			 }
			 if("".equals(zzlx)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "组织类型为空");
				 resultArr.put(resutlJo);
				 continue;
			 }
			 zg = getRyids(Util.null2String(jo.getString("magId4")));
			 bmjl = getRyids(Util.null2String(jo.getString("magId3")));
			 zxzjl = getRyids(Util.null2String(jo.getString("magId2")));
			 fzc = getRyids(Util.null2String(jo.getString("leadId2")));
			 zc = getRyids(Util.null2String(jo.getString("leadId1")));
			 if("40".equals(zzlx)) {//组
				
			 }else if("30".equals(zzlx)){//部
				 zg = bmjl;
			 }else if("20".equals(zzlx)) {//中心
				 bmjl = zxzjl;
				 zg = zxzjl;
			 }
			 String result = insertOrUpdateMatrix(subid,depid,zg,bmjl,zxzjl,fzc,zc,"0");
			 if("-1".equals(result)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "统一矩阵表不存在");
				 resultArr.put(resutlJo);
				 continue;
			 }else if("-2".equals(result)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "统一更新sql失败");
				 resultArr.put(resutlJo);
				 continue;
			 }
			 result = insertOrUpdateMatrix(subid,depid,zg,bmjl,zxzjl,fzc,zc,"1");
			 if("-1".equals(result)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "矩阵表不存在");
				 resultArr.put(resutlJo);
				 continue;
			 }else if("-2".equals(result)) {
				 sign = "E";
				 resutlJo = getResultJo(depcode, "",lastupdttm, "E", "更新sql失败");
				 resultArr.put(resutlJo);
				 continue;
			 }else {
				 resutlJo = getResultJo(depcode, "",lastupdttm, "S", "成功");
				 resultArr.put(resutlJo);
			 }
		 }
		 return sign;
	}

	public String insertOrUpdateMatrix(String subid,String depid,String zg,String bmjl,String zxzjl,String fzc,String zc,String isall) {
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "";
		String matrixTable = "";
		String Dataorder = "0";
		String mtid = "";//矩阵数据id
		if("0".equals(isall)) {
			sql = "select jzbm from uf_compMatrix_map where isall='0'";
			rs.executeSql(sql);
			if(rs.next()) {
				matrixTable = Util.null2String(rs.getString("jzbm"));
			}
		}else {
			sql = "select jzbm from uf_compMatrix_map where gs='"+subid+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				matrixTable = Util.null2String(rs.getString("jzbm"));
			}
		}
		if("".equals(matrixTable)) {
			return "-1";
		}
		sql = "select nvl(max(dataorder),0)+1 as Dataorder from "+matrixTable;
		rs.executeSql(sql);
		if(rs.next()) {
			Dataorder = Util.null2String(rs.getString("Dataorder"));
		}
		sql="select uuid from "+matrixTable+" where fb='"+subid+"' and bm='"+depid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			mtid = Util.null2String(rs.getString("uuid"));
		}
		if("".equals(mtid)) {
			sql = "insert into "+matrixTable+"(uuid,Dataorder,fb,bm,zg,bmjl,zxzjl,fzc,zc)"
					+ " values(sys_guid(),"+Dataorder+",'"+subid+"','"+depid+"','"+zg+"','"+bmjl+"'," +
							"'"+zxzjl+"','"+fzc+"','"+zc+"')";
		   boolean result=rs.executeSql(sql);
		   if(!result) {
			   log.writeLog("insertOrUpdateMatrix sql:"+sql);
			   return "-2";
		   }
		}else {
			sql="update "+matrixTable+" set zg='"+zg+"',bmjl='"+bmjl+"',zxzjl='"+zxzjl+"',fzc='"+fzc+"',zc='"+zc+"' where uuid='"+mtid+"'";
			boolean result=rs.executeSql(sql);
			if(!result) {
				log.writeLog("insertOrUpdateMatrix sql:"+sql);
				return "-2";
			}
		}
		return "1";
	}
	/**
	 * 过去下一个兼岗工号
	 * 
	 * @param emplid 主岗工号
	 */
	public String getJGWorkCode(String emplid) {
		RecordSet rs = new RecordSet();
		String workcode = "";
		String sql = "select nvl(max(nvl(substr(workcode,9),0)),0)+1  as jkworkcode from hrmresource where workcode like '"
				+ emplid + "%'";
		rs.executeSql(sql);
		if (rs.next()) {
			workcode = emplid + Util.null2String(rs.getString("jkworkcode"));
		} else {
			workcode = emplid + "1";
		}
		return workcode;
	}
	/**
	 * 根据工号获取人员id
	 * @param workcode
	 * @return
	 */
	public String getRyid(String workcode) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		if("".equals(workcode)) {
			return "";
		}
		sql = "select id from hrmresource where workcode = '" + workcode + "'  and nvl(belongto,0)<=0  order by id desc";
		rs.executeSql(sql);
		if (rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}

		return ryid;
	}
	/**
	 * 获取多人力ids
	 * @param workcodes
	 * @return
	 */
	public String getRyids(String workcodes) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		String ryids = "";
		String flag = "";	
		if("".equals(workcodes)) {
			return "";
		}
		String wc[] = workcodes.split(",");
		String newWorckcodes = "";
		for(String workcode :wc) {
			if(!"".equals(workcode)) {
				newWorckcodes = newWorckcodes + flag +"'"+workcode+"'";
				flag = ",";
			}
		}
		flag = "";
		sql = "select id from hrmresource where workcode in("+newWorckcodes+") and nvl(belongto,0)<=0 ";
		rs.executeSql(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
			if(!"".equals(ryid)) {
				ryids = ryids+flag+ryid;
				flag = ",";
			}
		}
		return ryids;
	}
	/**
	 * 获取兼岗人员id 有效
	 * 
	 * @param emplid
	 * @param jobid
	 */
	public String getJgryid(String workcode, String emplRcd) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		if("".equals(workcode)||"".equals(emplRcd)) {
			return "";
		}
		sql = "select id from hrmresource where workcode = '" + workcode+emplRcd+ "' and nvl(belongto,0)>0  order by id desc";
		rs.executeSql(sql);
		if (rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}

		return ryid;
	}

	/**
	 * 获取主岗人员id
	 * 
	 * @param emplid
	 * @param jobid
	 */
	public String getZgryid(String workcode, String jobid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		if("".equals(workcode)||"".equals(jobid)) {
			return "";
		}
		sql = "select id from hrmresource where workcode = '" + workcode + "' and jobtitle='" + jobid
				+ "' and nvl(belongto,0)<=0  order by id desc";
		rs.executeSql(sql);
		if (rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}

		return ryid;
	}
	
	/**
	 * 获取兼岗上级人员id
	 * 
	 * @param emplid
	 * @param jobid
	 */
	public String getJgry(String workcode, String jobid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		if("".equals(workcode)||"".equals(jobid)) {
			return "";
		}
		sql = "select id from hrmresource where workcode like '" + workcode + "%' and jobtitle='" + jobid
				+ "' and nvl(belongto,0)>0  order by id desc";
		rs.executeSql(sql);
		if (rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}

		return ryid;
	}
	/**
	 * 获取上级人员
	 * 
	 * @param emplid
	 * @param jobid
	 */
	public String getOtherRyid(String workcode, String jobid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String ryid = "";
		int count = 0;
		if("".equals(workcode) && "".equals(jobid)) {
			return "";
		}
		sql = "select count(1) as count from hrmresource where jobtitle = '"+jobid+"' and status<5";
		rs.executeSql(sql);
		if (rs.next()) {
			count = rs.getInt("count");
		}
		if(count == 1) {
			sql = "select id from hrmresource where jobtitle = '"+jobid+"' and status<5";
			rs.executeSql(sql);
			if (rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}else {
			sql = "select id from hrmresource where workcode = '" + workcode + "' and nvl(belongto,0)<=0  order by id desc";
			rs.executeSql(sql);
			if (rs.next()) {
				ryid = Util.null2String(rs.getString("id"));
			}
		}

		return ryid;
	}
	/**
	 * ps中间表转换
	 * 
	 * @param fieldname
	 * @param keyValue
	 * @return
	 */
	public String psTransMethod(String fieldname, String keyValue) {
		RecordSetDataSource rsd = new RecordSetDataSource("Hrms");
		String desc = "";// 描述
		String sql = "select descr100 from SYSADM.PS_HPS_TRAN_INCRMT where fieldname=upper('" + fieldname
				+ "') and ecextvalue='" + keyValue + "'";
		rsd.executeSql(sql);
		if (rsd.next()) {
			desc = Util.null2String(rsd.getString("descr100"));
		}
		return desc;

	}

	/**
	 * 获取岗位id
	 * 
	 * @param jobcode
	 * @return
	 */
	public String getJobtitleId(String jobcode) {
		RecordSet rs = new RecordSet();
		String jobid = "";
		if("".equals(jobcode)) {
			return "";
		}
		String sql = "select id from hrmjobtitles where jobtitlecode='" + jobcode + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			jobid = Util.null2String(rs.getString("id"));
		}
		return jobid;
	}

	public String checkPersonIsexits(String workcode) {
		RecordSet rs = new RecordSet();
		String ryid = "";
		if ("".equals(workcode)) {
			return ryid;
		}
		String sql = "select id from hrmresource where workcode='" + workcode + "' and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if (rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
		}
		return ryid;
	}

	public String issubcompany(String supercode) {
		RecordSet rs = new RecordSet();
		String subcompanyid = "";
		String sql = "";
		if ("".equals(supercode)) {
			return subcompanyid;
		}
		sql = "select id from hrmsubcompany where subcompanycode='" + supercode + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			subcompanyid = Util.null2String(rs.getString("id"));
		}
		return subcompanyid;
	}

	public String getSubcompanyid(String supdpcode) {
		RecordSet rs = new RecordSet();
		String subcompanyid = "";
		String supid = "";
		String sql = "";
		boolean flag = true;
		if ("".equals(supdpcode)) {
			return subcompanyid;
		}
		sql = "select subcompanyid1,supdepid from hrmdepartment where departmentcode='" + supdpcode + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			subcompanyid = Util.null2String(rs.getString("subcompanyid1"));
			supid = Util.null2String(rs.getString("supdepid"));
		}
		if (!"".equals(subcompanyid) && Util.getIntValue(subcompanyid, 0) > 0) {
			return subcompanyid;
		}
		if ("".equals(supid) || Util.getIntValue(supid, 0) <= 0) {
			return subcompanyid;
		}
		while (flag) {
			sql = "select subcompanyid1,supdepid from hrmdepartment where supdepid = '" + supid + "'";
			rs.executeSql(sql);
			if (rs.next()) {
				subcompanyid = Util.null2String(rs.getString("subcompanyid1"));
				supid = Util.null2String(rs.getString("supdepid"));
			}
			if (!"".equals(subcompanyid) && Util.getIntValue(subcompanyid, 0) > 0) {
				break;
			}
			if ("".equals(supid) || Util.getIntValue(supid, 0) <= 0) {
				break;
			}
		}
		return subcompanyid;

	}

	/**
	 * 人员职务信息表数据插入
	 * 
	 * @param jsonstr
	 * @return
	 * @throws JSONException
	 */
	public void insertPersonJobRelationInfo(String jsonstr,String pc) throws Exception {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
//		String sql = "delete from uf_personneljob";
//		rs.executeSql(sql);
		String sql = "";
		JSONObject json = new JSONObject(jsonstr);
		JSONArray jsa = json.getJSONArray("employeeJobs");
		InsertUtil insert = new InsertUtil();

		for (int i = 0; i < jsa.length(); i++) {
			Map<String, String> map = new HashMap<String, String>();
			JSONObject json1 = jsa.getJSONObject(i);
			try {
				map.put("emplid", Util.null2String(json1.get("emplid")));// 员工ID
				map.put("emplRcd", Util.null2String(json1.get("emplRcd")));// 员工记录
				map.put("hr_status", Util.null2String(json1.get("hr_status")));// hr状态
				map.put("socialDt", Util.null2String(json1.get("socialDt")));// 社会工龄起算日
				map.put("hireDt", Util.null2String(json1.get("hireDt")));// 入职日期
				map.put("terDt", Util.null2String(json1.get("terDt")));// 离职日期
				map.put("enterGpDt", Util.null2String(json1.get("enterGpDt")));// 集团入职日期
				map.put("positionNbr", Util.null2String(json1.get("positionNbr")));// 岗位ID
				map.put("deptId", Util.null2String(json1.get("deptId")));// 部门ID
				map.put("locationId", Util.null2String(json1.get("locationId")));// 地点ID
				map.put("idenCategory", Util.null2String(json1.get("idenCategory")));// 身份类别
				map.put("jobIndiactor", Util.null2String(json1.get("jobIndiactor")));// 职务标识
				map.put("action", Util.null2String(json1.get("action")));// 操作（员工状态）
				map.put("action_rsn", Util.null2String(json1.get("action_rsn")));// 操作原因
				map.put("actionFlag", Util.null2String(json1.get("actionFlag")));// 数据操作标识
				//map.put("batchNumber", Util.null2String(json1.get("batchNumber")));// 批次号------用不着
				map.put("repordsTold", Util.null2String(json1.get("repordsTold")));// 直接上级
				map.put("repordsToPosn", Util.null2String(json1.get("repordsToPosn")));// 直接上级职位id
				map.put("sfcl", "0");// 是否处理 0 未处理 1处理
				map.put("jobcode", Util.null2String(json1.get("jobcode")));// 职务ID
				map.put("channel", Util.null2String(json1.get("channel")));// 通道
				map.put("sequence", Util.null2String(json1.get("sequence")));// 序列
				//map.put("grade", Util.null2String(json1.get("grade")));// 职等
				map.put("lastupdttm", Util.null2String(json1.get("lastupdttm")));// 最后更新日期
				map.put("pc", pc);
				insert.insert(map, "uf_personneljob");
			} catch (Exception e) {
				log.writeLog("InsertPersonActiovityInfo 出错-------" + json1.toString());
				continue;
			}
		}

	}

	public JSONObject getResultJo(String key1, String key2, String lastupdttm, String flag, String comments) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("key1", key1);
			jo.put("key2", key2);
			jo.put("lastupdttm", lastupdttm);
			jo.put("flag", flag);
			jo.put("Comments", comments);
		} catch (Exception e) {
			jo = null;
		}
		return jo;
	}

}
