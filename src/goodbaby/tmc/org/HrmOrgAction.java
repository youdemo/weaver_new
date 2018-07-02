package goodbaby.tmc.org;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import goodbaby.tmc.util.TmcDBUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.job.JobTitlesComInfo;
import weaver.hrm.job.JobTitlesTempletComInfo;
import weaver.hrm.resource.AllManagers;
import weaver.hrm.resource.ResourceComInfo;

public class HrmOrgAction {

	/*
	 *   分部操作
	 *   参数   hcb 分部对象参数
	 *   原则：  oracle 或 Sqlserver都可以执行的SQL
	 */
	public ReturnInfo operSubCompany(HrmSubCompanyBean hcb){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		String subCompanyCode = hcb.getSubCompanyCode();
		RecordSet rs = new RecordSet();
		rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+subCompanyCode+"'");
		int id = 0;
		if(rs.next()) { 
			id = rs.getInt("id");
		}
		
		// 查询公司直接上级  
		int idOrCode = hcb.getIdOrCode();
		String superID = "";
		if(idOrCode == 0 ){
			superID = Util.null2String(hcb.getSuperID());
		}else if(idOrCode == 1){
			rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+hcb.getSuperCode()+"'");
			if(rs.next()) { 
				superID = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(superID)||String.valueOf(id).equals(superID))  superID = "0";
		
		if(id < 1){  // 不存在，需要新增
			// 把记录插入到分部表
	//		String sql = "insert into hrmsubcompany (subcompanyname,subcompanydesc,subcompanycode,"
	//				+"supsubcomid,companyid,showorder,canceled)" +
	//				"  values ('"+hcb.getSubCompanyName()+"','"+hcb.getSubCompanyDesc()+"','"
	//				+subCompanyCode+"',"+superID+",1,'"+hcb.getOrderBy()+"','"+hcb.getStatus()+"')";
			
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("subcompanycode", subCompanyCode);
			mapStr.put("subcompanyname", hcb.getSubCompanyName());
			mapStr.put("subcompanydesc", hcb.getSubCompanyDesc());
			mapStr.put("supsubcomid", superID);
			mapStr.put("companyid", "1");
			mapStr.put("showorder", "" + hcb.getOrderBy());
			mapStr.put("canceled", "" + hcb.getStatus());
			
			boolean isRun = tdu.insert(mapStr, "HrmSubCompany");
			
		//	boolean isRun = rs.executeSql(sql);
			if(!isRun){
				ri.setMessage(false,"1002", "分部信息 插入错误！");
				return ri;
			}	
	
			rs.executeSql("select id from hrmsubcompany where subcompanycode='"+subCompanyCode+"'");
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id < 1){
				ri.setMessage(false,"1003", "分部信息 插入后 无记录错误！");
				return ri;
			}
			// 菜单插入操作
			rs.executeSql(" insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  " +
					  "	distinct  userid,infoid,visible,viewindex," + id + ",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig where resourcetype=1  and resourceid=1");
			rs.executeSql("insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  " +
					  "	distinct  userid,infoid,visible,viewindex," + id + ",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig where resourcetype=1  and resourceid=1");
			
		}else{ // 已经存在，需要更新
			//更新分部信息
		//	String sql = "update HrmSubCompany set subcompanyname='"
		//			+hcb.getSubCompanyName()+"',subcompanydesc='"+hcb.getSubCompanyDesc()+"',supsubcomid='"
		//			+superID+"',showorder="+hcb.getOrderBy()+",canceled="+hcb.getStatus()
		//			+" where id='" + id + "'";
			
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("id", ""+id);
			// departmentcode,departmentname,departmentmark,supdepid,subcompanyid1
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("subcompanyname", hcb.getSubCompanyName());
			mapStr.put("subcompanydesc", hcb.getSubCompanyDesc());
			mapStr.put("supsubcomid", superID);
			mapStr.put("showorder", "" + hcb.getOrderBy());
			mapStr.put("canceled", "" + hcb.getStatus());
			
			boolean isRun = tdu.update("HrmSubCompany", mapStr, whereMap);

			if(!isRun){
				ri.setMessage(false,"1001", "分部信息 更新错误！");
				return ri;
			}	
		}
		// 自定义信息处理
		String tableName = "HrmSubcompanyDefined";
		Map<String,String> updateMap = hcb.getCusMap();
		Map<String,String> mainMap = new HashMap<String,String>();
		mainMap.put("subcomid", ""+id);
		String message = updateCustom(tableName,mainMap,updateMap);	
		ri.setRemark(message);
		
		// 更新分部缓存
		try {
			SubCompanyComInfo SubCompanyComInfo = new SubCompanyComInfo();
			SubCompanyComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog(e.getMessage());
		}
		return ri;
	}
	
	/*
	 *   部门操作
	 *   参数   hdb 部门对象参数
	 *   原则：  oracle 或 Sqlserver都可以执行的SQL
	 */
	public ReturnInfo operDept(HrmDepartmentBean hdb){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		String departmentcode = hdb.getDepartmentcode();
		RecordSet rs = new RecordSet();
		String sql = "";
		sql = "select id from HrmDepartment where departmentcode='"+departmentcode+"' ";
		rs.executeSql(sql);
		int id = 0;
		if(rs.next()){
			id = rs.getInt("id");
		}
		
		// 获取直属分部
		int comIdOrCode = hdb.getComIdOrCode();
		String subComID = "";
		if(comIdOrCode == 0){
			subComID = Util.null2String(hdb.getSubcompanyid1());
		}else if(comIdOrCode == 1){
			sql = "select id from hrmsubcompany where subcompanycode='"+hdb.getSubcompanyCode()+"'";
			rs.executeSql(sql);
			if(rs.next()){
				subComID = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(subComID))  subComID = "0";
				
		// 查询公司直接部门  
		int idOrCode = hdb.getIdOrCode();
		String superID = "";
		if(idOrCode == 0 ){
			superID = Util.null2String(hdb.getSuperID());
		}else{
			sql = "select id from HrmDepartment where departmentcode = '"+hdb.getSuperCode()+"'";
			rs.executeSql(sql);
			if(rs.next()) { 
				superID = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(superID) || String.valueOf(id).equals(superID))  superID = "0";

		if(id < 1){
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("departmentcode", departmentcode);
			mapStr.put("departmentname", hdb.getDepartmentname());
			mapStr.put("departmentmark", hdb.getDepartmentark());
			mapStr.put("supdepid", superID);
			mapStr.put("subcompanyid1", subComID);
			mapStr.put("canceled", ""+hdb.getStatus());
			mapStr.put("showorder", ""+hdb.getOrderBy());
			
			boolean isRun = tdu.insert(mapStr,"HrmDepartment");
			if(!isRun){
				ri.setMessage(false,"1101", "部门信息 插入操作错误！");
				return ri;
			}
			
			rs.executeSql("select id from HrmDepartment where departmentcode='"+departmentcode+"' ");
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id < 1){
				ri.setMessage(false,"1103", "部门信息 插入后 无记录错误！");
				return ri;
			}
			
		}else{
			// 更新部门信息
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("id", ""+id);
			// departmentcode,departmentname,departmentmark,supdepid,subcompanyid1
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("departmentname", hdb.getDepartmentname());
			mapStr.put("departmentmark", hdb.getDepartmentark());
			mapStr.put("supdepid", superID);
			mapStr.put("subcompanyid1", subComID);
			mapStr.put("canceled", ""+hdb.getStatus());
			mapStr.put("showorder", ""+hdb.getOrderBy());
			
			boolean isRun = tdu.update("HrmDepartment", mapStr, whereMap);
			if(!isRun){
				ri.setMessage(false,"1102", "部门信息 更新错误！");
				return ri;
			}	
		}
		// 自定义信息处理
		String tableName = "HrmDepartmentDefined";
		Map<String,String> updateMap = hdb.getCusMap();
		Map<String,String> mainMap = new HashMap<String,String>();
		mainMap.put("deptid", ""+id);
		String message = updateCustom(tableName,mainMap,updateMap);	
		ri.setRemark(message);		
		
		try {
			DepartmentComInfo DepartmentComInfo= new DepartmentComInfo();
			DepartmentComInfo.removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog(e.getMessage());
		}
		
		return ri;
	}
	
	/*
	 *   岗位操作
	 *   参数   hjt 岗位对象参数
	 *   原则：  oracle 或 Sqlserver都可以执行的SQL
	 */
	public ReturnInfo operJobtitle(HrmJobTitleBean hjt){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		String jobtitlecode = hjt.getJobtitlecode();
		RecordSet rs = new RecordSet();
		String sql = "";
		sql = "select id from HrmJobTitles where jobtitlecode='"+jobtitlecode+"' ";
		rs.executeSql(sql);
		int id = 0;
		if(rs.next()){
			id = rs.getInt("id");
		}
		
		// 获取岗位所属部门
		int deptIdOrCode = hjt.getDeptIdOrCode();
		String deptId = "";
		if(deptIdOrCode == 0){
			deptId = Util.null2String(hjt.getJobdepartmentid());
		}else if(deptIdOrCode == 1){
			sql = "select id from HrmDepartment where departmentcode='"+hjt.getJobdepartmentCode()+"'";
			rs.executeSql(sql);
			if(rs.next()){
				deptId = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(deptId) || String.valueOf(id).equals(deptId))  deptId = "0";
		
		// 所属职位的模板的ID   HrmJobGroups
		String jobGroups = hjt.getJobGroupName();
		sql = "select count(*) as ct from HrmJobGroups where jobgroupname='"+jobGroups+"'";
		rs.executeSql(sql);
		int flag_1 = 0;
		if(rs.next()){
			flag_1 = rs.getInt("ct");
		}
		if(flag_1 < 1){
			sql = "insert into HrmJobGroups(jobgroupname,jobgroupremark) values('"+jobGroups+"','"+jobGroups+"')";
			rs.executeSql(sql);
		}
		sql = "select id from HrmJobGroups where jobgroupname='"+jobGroups+"'";
		rs.executeSql(sql);
		String groupID = "";
		if(rs.next()){
			groupID = Util.null2String(rs.getString("id"));
		}
		if("".equals(groupID))  groupID = "11";    // 11为待定
		
		// 所属职位的ID   HrmJobActivities
		String jobAct = hjt.getJobactivityName();
		sql = "select count(*) as ct from HrmJobActivities where jobactivityname='"+jobAct+"'";
		rs.executeSql(sql);
		flag_1 = 0;
		if(rs.next()){
			flag_1 = rs.getInt("ct");
		}
		if(flag_1 < 1){
			sql = "insert into HrmJobActivities(jobactivityname,jobactivitymark,jobgroupid) values('"
					+jobAct+"','"+jobAct+"',"+groupID+")";
			rs.executeSql(sql);
		}
		sql = "select id from HrmJobActivities where jobactivityname='"+jobAct+"'";
		rs.executeSql(sql);
		String jobActID = "";
		if(rs.next()){
			jobActID = Util.null2String(rs.getString("id"));
		}
		if("".equals(jobActID))  jobActID = "14";   // 14 为待定
		
		// select jobactivityid,jobtitlename,jobtitlemark,jobtitlecode,jobdepartmentid,outkey from HrmJobTitles
		if(id < 1){
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("jobtitlecode", jobtitlecode);
			mapStr.put("jobtitlename", hjt.getJobtitlename());
			mapStr.put("jobtitleremark", hjt.getJobtitleremark());
			mapStr.put("jobtitlemark", hjt.getJobtitlemark());
			mapStr.put("jobactivityid", jobActID);
			mapStr.put("jobdepartmentid", deptId);
			mapStr.put("outkey", hjt.getSuperJobCode());
			
			boolean isRun = tdu.insert(mapStr,"HrmJobTitles");
			if(!isRun){
				ri.setMessage(false,"1201", "岗位信息 插入操作错误！");
				return ri;
			}
			
			rs.executeSql("select id from HrmJobTitles where jobtitlecode='"+jobtitlecode+"' ");
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id < 1){
				ri.setMessage(false,"1203", "岗位信息 插入后 无记录错误！");
				return ri;
			}
			
		}else{
			// 更新部门信息
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("id", ""+id);
			Map<String,String> mapStr = new HashMap<String,String>();
	//		mapStr.put("jobtitlecode", jobtitlecode);
			mapStr.put("jobtitlename", hjt.getJobtitlename());
			mapStr.put("jobtitleremark", hjt.getJobtitleremark());
			mapStr.put("jobtitlemark", hjt.getJobtitlemark());
			mapStr.put("jobactivityid", jobActID);
			mapStr.put("jobdepartmentid", deptId);
			mapStr.put("outkey", hjt.getSuperJobCode());
			
			boolean isRun = tdu.update("HrmJobTitles", mapStr, whereMap);
			if(!isRun){
				ri.setMessage(false,"1202", "岗位信息 更新错误！");
				return ri;
			}	
		}	
		
		try {
			JobTitlesTempletComInfo JobTitlesTempletComInfo = new JobTitlesTempletComInfo();
			JobTitlesTempletComInfo.removeJobTitlesTempletCache();
			
            JobTitlesComInfo JobTitlesComInfo= new JobTitlesComInfo();
            JobTitlesComInfo.removeJobTitlesCache();
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog(e.getMessage());
		}
		
		return ri;
	}
	
	/*
	 *   人员操作
	 *   参数   hrb 人员对象参数
	 *   原则：  oracle 或 Sqlserver都可以执行的SQL
	 */
	public ReturnInfo operResource(HrmResourceBean hrb){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		String workcode = hrb.getWorkcode();
		RecordSet rs = new RecordSet();
		String sql = "";
		sql = "select id from hrmresource where workcode='"+workcode+"' ";
		rs.executeSql(sql);
		int id = 0;
		if(rs.next()){
			id = rs.getInt("id");
		}
		
		// 岗位ID
		int jobFlag = hrb.getJobIdOrCode();
		String jobTitleID = "";
		if(jobFlag == 0){
			jobTitleID = Util.null2String(hrb.getJobtitle());
		}else if(jobFlag == 1){
			sql = "select id from HrmJobTitles where jobtitlecode='"+hrb.getJobtitleCode()+"'";
			rs.executeSql(sql);
			if(rs.next()){
				jobTitleID  = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(jobTitleID)){
			ri.setMessage(false, "2200", "人员的岗位不存在!");
			return ri;
		}
		
		// 获取部门ID
		int deptFlag = hrb.getDeptIdOrCode();
		String deptID = "";
		if(deptFlag == 0){
			deptID = Util.null2String(hrb.getDepartmentid());
		}else if(deptFlag == 1){
			sql = "select id from HrmDepartment where departmentcode='"+hrb.getDepartmentCode()+"'";
			rs.executeSql(sql);
			if(rs.next()){
				deptID  = Util.null2String(rs.getString("id"));
			}
		}
		if("".equals(deptID)){
			ri.setMessage(false, "2201", "人员的部门不存在!");
			return ri;
		}
		
		String comID = "";
		sql = "select subcompanyid1 from HrmDepartment where id="+deptID;
		rs.executeSql(sql);
		if(rs.next()){
			comID  = Util.null2String(rs.getString("subcompanyid1"));
		}

		if("".equals(comID)){
			ri.setMessage(false, "2202", "分部的部门不存在!");
			return ri;
		}
		
		// 获取人员上级
		int managerFlag = hrb.getManagerIdOrCode();
		String managerID = "";
		if(managerFlag == 0){
			managerID = Util.null2String(hrb.getManagerid());
		}else if(managerFlag == 1){
			sql = "select id from hrmresource where workcode='"+hrb.getManagerCode()+"'";
			rs.executeSql(sql);
			if(rs.next()){
				managerID  = Util.null2String(rs.getString("id"));
			}
		}else if(managerFlag == 2){
			// 待通过岗位处理
			managerID = "@@";
		}
		if("".equals(managerID)){
			ri.setRemark(ri.getRemark()+";直接上级不存在！");
		}
	
		// 获取次账号所属主账号
		int belongFlag = hrb.getBelongIdOrCode();
		String belongID = "";
		if(belongFlag >= 0){
			if(belongFlag == 0){
				belongID = Util.null2String(hrb.getBelongto());
			}else if(belongFlag == 1){
				sql = "select id from hrmresource where workcode='"+hrb.getBelongtoCode()+"'";
				rs.executeSql(sql);
				if(rs.next()){
					belongID  = Util.null2String(rs.getString("id"));
				}
			}
			
			if(String.valueOf(id).equals(belongID)) belongID="";
			
			if("".equals(belongID)){
				ri.setRemark(ri.getRemark()+";次账号归属主账号不存在！");
			}
		}

		// 岗位信息判断上级
		if("@@".equals(managerID)){
			String tmp_1 = "";
			sql = "select outkey from HrmJobTitles where id="+jobTitleID;
			rs.executeSql(sql);
			if(rs.next()){
				tmp_1 = Util.null2String(rs.getString("outkey"));
			}
		//	log.writeLog("tmp_1 = " + tmp_1);
			if(!"".equals(tmp_1)){
				managerID = "";
				int tmp_managerID = 0;
				if(id > 0){
					sql = "select managerid from hrmresource where id="+id;
					rs.executeSql(sql);
					if(rs.next()){
						tmp_managerID = rs.getInt("managerid");
					}
				}
				int tmp_2_x = 0;
				// 跑2边 肯定都是有值的。
				while("".equals(managerID)){
					// 查询处理上级   如果已经存在看里面是否存在，如果存在就还是之前的；如果不存在,就需要
					sql = "select h.id,jt.outkey from hrmresource h join HrmJobTitles jt on h.jobtitle=jt.id "
					+"  where jt.jobtitlecode='"+tmp_1+"' and h.status in(0,1,2,3,4) order by h.id ";
					rs.executeSql(sql);
					while(rs.next()){
						int ss_xx = rs.getInt("id");
						if(tmp_2_x < 1) tmp_2_x = ss_xx;
						if(tmp_managerID == ss_xx){
							managerID = String.valueOf(tmp_managerID);
							break;
						}
						tmp_1 = Util.null2String(rs.getString("outkey"));
						
				//		log.writeLog("ss_xx = " + ss_xx + " ; tmp_2_x = " + tmp_2_x + " ; tmp_managerID = " + tmp_managerID);
					}
					
					if("".equals(managerID)){
						if(tmp_2_x > 0) managerID = String.valueOf(tmp_2_x);
						break;
					}
					if("".equals(tmp_1))  break;
				}
			}else{
				managerID = "";
			}
		}
		if(String.valueOf(id).equals(managerID))   managerID = "";
		
		if("".equals(managerID)){
			ri.setRemark(ri.getRemark()+";直接上级不存在！");
		}

		if(id < 1){
			Map<String,String> mapStr = new HashMap<String,String>();
			int ss_yy = hrb.getAccounttype();
			if(ss_yy == 0){
				mapStr.put("loginid", hrb.getLoginid());
			}
			mapStr.put("workcode", workcode);
			mapStr.put("loginid", hrb.getLoginid());
			mapStr.put("status", hrb.getStatus());
			mapStr.put("lastname", hrb.getLastname());
			mapStr.put("sex", hrb.getSexID());
			mapStr.put("birthday", hrb.getBirthday());
			mapStr.put("seclevel", ""+hrb.getSeclevel());
			mapStr.put("jobtitle", jobTitleID);
			mapStr.put("departmentid", deptID);
			mapStr.put("subcompanyid1", comID);
			
			mapStr.put("managerid", managerID);
			mapStr.put("nationality",hrb.getNationalityID());
			mapStr.put("systemlanguage",hrb.getSystemlanguage());
			mapStr.put("password",hrb.getPassword());
			mapStr.put("maritalstatus",hrb.getMaritalstatus());
			mapStr.put("telephone",hrb.getTelephone());
			mapStr.put("mobile",hrb.getMobile());
			mapStr.put("mobilecall",hrb.getMobilecall());
			mapStr.put("email",hrb.getEmail());
			mapStr.put("dsporder",""+hrb.getDsporder());
			mapStr.put("createrid",hrb.getCreaterid());
			mapStr.put("createdate",hrb.getCreatedate());
			mapStr.put("accounttype",""+hrb.getAccounttype());
			mapStr.put("belongto",belongID);
			mapStr.put("locationid",hrb.getLocationid());
			mapStr.put("workroom",hrb.getWorkroom());
			mapStr.put("homeaddress",hrb.getHomeaddress());
			mapStr.put("startdate",hrb.getStartdate());
			mapStr.put("enddate",hrb.getEnddate());
			mapStr.put("datefield1",hrb.getDatefield1());
			mapStr.put("datefield2",hrb.getDatefield2());
			mapStr.put("datefield3",hrb.getDatefield3());
			mapStr.put("datefield4",hrb.getDatefield4());
			mapStr.put("datefield5",hrb.getDatefield5());
			mapStr.put("numberfield1",hrb.getNumberfield1());
			mapStr.put("numberfield2",hrb.getNumberfield2());
			mapStr.put("numberfield3",hrb.getNumberfield3());
			mapStr.put("numberfield4",hrb.getNumberfield4());
			mapStr.put("numberfield5",hrb.getNumberfield5());
			mapStr.put("textfield1",hrb.getTextfield1());
			mapStr.put("textfield2",hrb.getTextfield2());
			mapStr.put("textfield3",hrb.getTextfield3());
			mapStr.put("textfield4",hrb.getTextfield4());
			mapStr.put("textfield5",hrb.getTextfield5());
			mapStr.put("tinyintfield1",hrb.getTinyintfield1());
			mapStr.put("tinyintfield2",hrb.getTinyintfield2());
			mapStr.put("tinyintfield3",hrb.getTinyintfield3());
			mapStr.put("tinyintfield4",hrb.getTinyintfield4());
			mapStr.put("tinyintfield5",hrb.getTinyintfield5());
			mapStr.put("jobactivitydesc",hrb.getJobactivitydesc());
			mapStr.put("certificatenum",hrb.getCertificatenum());
			mapStr.put("nativeplace",hrb.getNativeplace());
			mapStr.put("educationlevel",hrb.getEducationlevel());
			mapStr.put("regresidentplace",hrb.getRegresidentplace());
			mapStr.put("healthinfo",hrb.getHealthinfo());
			mapStr.put("policy",hrb.getPolicy());
			mapStr.put("degree",hrb.getDegree());
			mapStr.put("height",hrb.getHeight());
			mapStr.put("jobcall",hrb.getJobcall());
			mapStr.put("accumfundaccount",hrb.getAccumfundaccount());
			mapStr.put("birthplace",hrb.getBirthday());
			mapStr.put("folk",hrb.getFolk());
			mapStr.put("extphone",hrb.getExtphone());
			mapStr.put("fax",hrb.getFax());
			mapStr.put("weight",hrb.getWeight());
			mapStr.put("tempresidentnumber",hrb.getTempresidentnumber());
			mapStr.put("probationenddate",hrb.getProbationenddate());
			mapStr.put("bankid1",hrb.getBankid1());
			mapStr.put("accountid1",hrb.getAccountid1());
			
			int s = 0;
			sql = "select max(id) as maxid from hrmresource";
			rs.executeSql(sql);
			if(rs.next()){
				s = rs.getInt("maxid");
			}
			if(s < 2) s = 2;
			else s = s+1;
			
			mapStr.put("id",String.valueOf(s));
			
			boolean isRun = tdu.insert(mapStr,"hrmresource");
			if(!isRun){
				ri.setMessage(false,"2220", "人员信息 插入操作错误！");
				return ri;
			}
			
			rs.executeSql("select id from hrmresource where workcode='"+workcode+"' ");
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id < 1){
				ri.setMessage(false,"2221", "人员信息 插入后 无记录错误！");
				return ri;
			}
			
		}else{
			// 更新部门信息
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("id", ""+id);
			Map<String,String> mapStr = new HashMap<String,String>();
//			mapStr.put("workcode", workcode);
			int ss_yy = hrb.getAccounttype();
			if(ss_yy == 0){
				mapStr.put("loginid", hrb.getLoginid());
			}
			
			mapStr.put("status", hrb.getStatus());
			mapStr.put("lastname", hrb.getLastname());
			mapStr.put("sex", hrb.getSexID());
			mapStr.put("birthday", hrb.getBirthday());
			mapStr.put("seclevel", ""+hrb.getSeclevel());
			mapStr.put("jobtitle", jobTitleID);
			mapStr.put("departmentid", deptID);
			mapStr.put("subcompanyid1", comID);
			mapStr.put("managerid", managerID);
			mapStr.put("nationality",hrb.getNationalityID());
			mapStr.put("systemlanguage",hrb.getSystemlanguage());
			mapStr.put("password",hrb.getPassword());
			mapStr.put("maritalstatus",hrb.getMaritalstatus());
			mapStr.put("telephone",hrb.getTelephone());
			mapStr.put("mobile",hrb.getMobile());
			mapStr.put("mobilecall",hrb.getMobilecall());
			mapStr.put("email",hrb.getEmail());
			mapStr.put("dsporder",""+hrb.getDsporder());
			mapStr.put("createrid",hrb.getCreaterid());
			//mapStr.put("createdate",hrb.getCreatedate());
			mapStr.put("accounttype",""+hrb.getAccounttype());
			mapStr.put("belongto",belongID);
			mapStr.put("locationid",hrb.getLocationid());
			mapStr.put("workroom",hrb.getWorkroom());
			mapStr.put("homeaddress",hrb.getHomeaddress());
			mapStr.put("startdate",hrb.getStartdate());
			mapStr.put("enddate",hrb.getEnddate());
			mapStr.put("datefield1",hrb.getDatefield1());
			mapStr.put("datefield2",hrb.getDatefield2());
			mapStr.put("datefield3",hrb.getDatefield3());
			mapStr.put("datefield4",hrb.getDatefield4());
			mapStr.put("datefield5",hrb.getDatefield5());
			mapStr.put("numberfield1",hrb.getNumberfield1());
			mapStr.put("numberfield2",hrb.getNumberfield2());
			mapStr.put("numberfield3",hrb.getNumberfield3());
			mapStr.put("numberfield4",hrb.getNumberfield4());
			mapStr.put("numberfield5",hrb.getNumberfield5());
			mapStr.put("textfield1",hrb.getTextfield1());
			mapStr.put("textfield2",hrb.getTextfield2());
			mapStr.put("textfield3",hrb.getTextfield3());
			mapStr.put("textfield4",hrb.getTextfield4());
			mapStr.put("textfield5",hrb.getTextfield5());
			mapStr.put("tinyintfield1",hrb.getTinyintfield1());
			mapStr.put("tinyintfield2",hrb.getTinyintfield2());
			mapStr.put("tinyintfield3",hrb.getTinyintfield3());
			mapStr.put("tinyintfield4",hrb.getTinyintfield4());
			mapStr.put("tinyintfield5",hrb.getTinyintfield5());
			mapStr.put("jobactivitydesc",hrb.getJobactivitydesc());
			mapStr.put("certificatenum",hrb.getCertificatenum());
			mapStr.put("nativeplace",hrb.getNativeplace());
			mapStr.put("educationlevel",hrb.getEducationlevel());
			mapStr.put("regresidentplace",hrb.getRegresidentplace());
			mapStr.put("healthinfo",hrb.getHealthinfo());
			mapStr.put("policy",hrb.getPolicy());
			mapStr.put("degree",hrb.getDegree());
			mapStr.put("height",hrb.getHeight());
			mapStr.put("jobcall",hrb.getJobcall());
			mapStr.put("accumfundaccount",hrb.getAccumfundaccount());
			mapStr.put("birthplace",hrb.getBirthday());
			mapStr.put("folk",hrb.getFolk());
			mapStr.put("extphone",hrb.getExtphone());
			mapStr.put("fax",hrb.getFax());
			mapStr.put("weight",hrb.getWeight());
			mapStr.put("tempresidentnumber",hrb.getTempresidentnumber());
			mapStr.put("probationenddate",hrb.getProbationenddate());
			mapStr.put("bankid1",hrb.getBankid1());
			mapStr.put("accountid1",hrb.getAccountid1());
			
			boolean isRun = tdu.update("hrmresource", mapStr, whereMap);
			if(!isRun){
				ri.setMessage(false,"2222", "人员信息 更新错误！");
				return ri;
			}	
		}	
		//更新managerstr
		AllManagers al = new AllManagers();
		String managerstr = al. getAllManagerstr(""+id);
		sql="update hrmresource set managerstr='"+managerstr+"' where id="+id;
		rs.executeSql(sql);
		int  currentid = 0;
		int nextid=0;
		// 处理系统最大需要问题
		sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
		rs.executeSql(sql);
		if(rs.next()){
			currentid = rs.getInt("currentid");
		}
		sql = "select max(id)+1 as nextid from hrmresource";
		rs.executeSql(sql);
		if(rs.next()){
			nextid = rs.getInt("nextid");
		}
		if(currentid != nextid){
			rs.executeSql("update SequenceIndex set currentid=" +nextid + " where indexdesc='resourceid'");
		}
		
		// 自定义信息处理
		String tableName = "cus_fielddata";
		Map<String,String> updateMap = hrb.getCusMap();
		Map<String,String> mainMap = new HashMap<String,String>();			
		mainMap.put("id", ""+id);
		mainMap.put("scope", "HrmCustomFieldByInfoType");
		mainMap.put("scopeid", "-1");
		String message = updateCustom(tableName,mainMap,updateMap);	
		ri.setRemark(message);		
				
		try {			
			ResourceComInfo ResourceComInfo= new ResourceComInfo();
			ResourceComInfo.addResourceInfoCache(""+id);
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog(e.getMessage());
		}
		
		return ri;
	}
	
	// 更新自定义表    tableName:自定义表      mainMap:判断字段    updateMap：需要更新的自定义字段
	private String updateCustom(String tableName,Map<String,String> mainMap,Map<String,String> updateMap){
		BaseBean log = new BaseBean();
		log.writeLog("updateCustom(Start) : " + tableName);
		// 无明细字段
		if(updateMap == null || mainMap.size() < 1)
			return "";
		if(mainMap == null || mainMap.size() < 1)
			return "主表判断字段为空";
		
		String message = "";
		
		// 判断字段拼接
		String where = " where 1=1 ";
		// 组合内容拼接
		String sum_key = "";
		String sum_val = "";
		String flme = "";
		Iterator<String> it = mainMap.keySet().iterator();
		while(it.hasNext()){
			String key = it.next();
			String value = mainMap.get(key);
			
			where = where + " and " + key + "='" + value + "'";
			sum_key = sum_key + flme + key;
			sum_val = sum_val + flme + "'" + value+"'";
			flme = ",";
		}
		
		RecordSet rs = new RecordSet();
		// 判断值是否存在，不存在话插入
		String sql = "select count(*) as ct from " + tableName + where;
		rs.executeSql(sql);
		int flag = 0;
		if(rs.next()){
			flag = rs.getInt("ct");
		}
		if(flag ==0 ){
			sql = "insert into " + tableName + "("+sum_key+") values("+sum_val+")";
			rs.executeSql(sql);
			log.writeLog("updateCustom(sql1) : " + sql);
		}
		
		// 所有字段更新
		Iterator<String> itx = updateMap.keySet().iterator();
		while(itx.hasNext()){
			String key = itx.next();
			String value = updateMap.get(key);
			
			sql = "update " + tableName + " set " + key + "='" + value + "' " + where;
			log.writeLog("updateCustom(sql2) : " + sql);
			boolean isRun = rs.executeSql(sql);
			if(!isRun){
				message = message + ";字段:"+key+",值为:"+value+" 信息更新失败";
			}
		}	
		
		return message;
	}
}
