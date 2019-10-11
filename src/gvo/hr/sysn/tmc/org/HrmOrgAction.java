package gvo.hr.sysn.tmc.org;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import gvo.hr.sysn.tmc.util.TmcDBUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
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
//		try {
//			SubCompanyComInfo SubCompanyComInfo = new SubCompanyComInfo();
//			SubCompanyComInfo.removeCompanyCache();
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.writeLog(e.getMessage());
//		}
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
		
//		try {
//			DepartmentComInfo DepartmentComInfo= new DepartmentComInfo();
//			DepartmentComInfo.removeCompanyCache();
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.writeLog(e.getMessage());
//		}
		
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
//		sql = "select count(*) as ct from HrmJobGroups where jobgroupname='"+jobGroups+"'";
//		rs.executeSql(sql);
//		int flag_1 = 0;
//		if(rs.next()){
//			flag_1 = rs.getInt("ct");
//		}
//		if(flag_1 < 1){
//			sql = "insert into HrmJobGroups(jobgroupname,jobgroupremark) values('"+jobGroups+"','"+jobGroups+"')";
//			rs.executeSql(sql);
//		}
//		sql = "select id from HrmJobGroups where jobgroupname='"+jobGroups+"'";
//		rs.executeSql(sql);
		String groupID = "";
//		if(rs.next()){
//			groupID = Util.null2String(rs.getString("id"));
//		}
		if("".equals(groupID))  groupID = "761";    // 11为待定
		
		// 所属职位的ID   HrmJobActivities
		String jobAct = hjt.getJobactivityName();
		sql = "select count(*) as ct from HrmJobActivities where jobactivityname='"+jobAct+"'";
		rs.executeSql(sql);
		 int flag_1 = 0;
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
		
//		try {
//			JobTitlesTempletComInfo JobTitlesTempletComInfo = new JobTitlesTempletComInfo();
//			JobTitlesTempletComInfo.removeJobTitlesTempletCache();
//			
//            JobTitlesComInfo JobTitlesComInfo= new JobTitlesComInfo();
//            JobTitlesComInfo.removeJobTitlesCache();
//		} catch (Exception e) {
//			e.printStackTrace();
//			log.writeLog(e.getMessage());
//		}
		
		return ri;
	}
	
	/**
	 * 职务同步
	 * @param descr 职责描述
	 * @param descrShort  职责标识
	 * @return
	 */
	public ReturnInfo operJobActivities(String descr,String descrShort){
		RecordSet rs = new RecordSet();
		String sql = "";
		String groupID = "";
		ReturnInfo ri = new ReturnInfo();
//		sql = "select count(1) as count from HrmJobGroups where jobgroupname='"+descr+"'";
//		rs.executeSql(sql);
//		int count = 0;
//		if(rs.next()){
//			count = rs.getInt("count");
//		}
//		if(count < 1){
//			sql = "insert into HrmJobGroups(jobgroupname,jobgroupremark) values('"+descr+"','"+descrShort+"')";
//			rs.executeSql(sql);
//		}
//		sql = "select id from HrmJobGroups where jobgroupname='"+descr+"'";
//		rs.executeSql(sql);
//		if(rs.next()){
//			groupID = Util.null2String(rs.getString("id"));
//		}
		if("".equals(groupID))  groupID = "761";    // 11为待定
		// 所属职位的ID   HrmJobActivities
		int count = 0;
		sql = "select count(1) as count from HrmJobActivities where jobactivityname='"+descr+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count < 1){
			sql = "insert into HrmJobActivities(jobactivityname,jobactivitymark,jobgroupid) values('"
					+descr+"','"+descrShort+"',"+groupID+")";
			rs.executeSql(sql);
		}else{
			sql = "update  HrmJobActivities set jobactivityname = '"+descr+"',jobactivitymark='"+descrShort+"',jobgroupid="+groupID+" where jobactivityname='"+descr+"'";
			rs.executeSql(sql);
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
			//ri.setRemark(ri.getRemark()+";直接上级不存在！");
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
			
//			int s = 0;
//			sql = "select max(id) as maxid from hrmresource";
//			rs.executeSql(sql);
//			if(rs.next()){
//				s = rs.getInt("maxid");
//			}
//			if(s < 2) s = 2;
//			else s = s+1;
			int  currentid = 0;
			int nextid=0;
			// 处理系统最大需要问题
			sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
			rs.executeSql(sql);
			if(rs.next()){
				currentid = rs.getInt("currentid");
			}
			nextid = currentid+1;
			rs.executeSql("update SequenceIndex set currentid=" +nextid + " where indexdesc='resourceid'");
			
			mapStr.put("id",String.valueOf(currentid));
			
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
		String managerstr = al.getAllManagerstr(""+id);
		sql="update hrmresource set managerstr=',"+managerstr+",' where id="+id;
		rs.executeSql(sql);
//		int  currentid = 0;
//		int nextid=0;
//		// 处理系统最大需要问题
//		sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
//		rs.executeSql(sql);
//		if(rs.next()){
//			currentid = rs.getInt("currentid");
//		}
//		sql = "select max(id)+1 as nextid from hrmresource";
//		rs.executeSql(sql);
//		if(rs.next()){
//			nextid = rs.getInt("nextid");
//		}
//		if(currentid != nextid){
//			rs.executeSql("update SequenceIndex set currentid=" +nextid + " where indexdesc='resourceid'");
//		}
		
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
		StringBuffer buff = new StringBuffer();
		String flag1 = "";
		Iterator<String> itx = updateMap.keySet().iterator();
		while(itx.hasNext()){
			String key = itx.next();
			String value = updateMap.get(key);
			if(value == null) value = "";
			buff.append(flag1);buff.append(key);buff.append("=");
			buff.append("'");buff.append(value);buff.append("'");
			flag1 = ",";
			
			
		}	
		sql = "update " + tableName + " set " + buff.toString()+ where;
		log.writeLog("updateCustom(sql2) : " + sql);
		boolean isRun = rs.executeSql(sql);
		if(!isRun){
			message ="自定义信息更新失败";
		}
		return message;
	}
	/**
	 * 更新或新增人员基本信息
	 * @param comMap
	 * @param cusMap
	 * @param cusMap1
	 * @return
	 */
	public ReturnInfo insertUpdatePerson(Map<String, String> comMap,Map<String, String> cusMap,Map<String, String> cusMap1){
		ReturnInfo ri = new ReturnInfo();
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		HrmResourceBean hrb = new HrmResourceBean();
		String id = "";//人员id
		String sql = "";
		String workcode = comMap.get("workcode");
		if("".equals(workcode)) {
			ri.setMessage(false,"", "工号为空");
		}
		sql = "select id from hrmresource where workcode='"+workcode+"' ";
		rs.executeSql(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
		}
		if("".equals(id)) {
			Map<String,String> mapStr = new HashMap<String,String>();
			int  currentid = 0;
			int nextid=0;
			// 处理系统最大需要问题
			sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
			rs.executeSql(sql);
			if(rs.next()){
				currentid = rs.getInt("currentid");
			}
			nextid = currentid+1;
			rs.executeSql("update SequenceIndex set currentid=" +nextid + " where indexdesc='resourceid'");
			
			mapStr.put("id",String.valueOf(currentid));
			
			mapStr.put("workcode", comMap.get("workcode"));//员工工号
			mapStr.put("lastname", comMap.get("lastname"));//姓名
			mapStr.put("loginid", comMap.get("loginid"));//登陆帐号
			mapStr.put("email", comMap.get("email"));//电子邮件
			mapStr.put("sex", comMap.get("sex"));
			mapStr.put("birthday",comMap.get("birthday"));//生日
			mapStr.put("telephone",comMap.get("telephone"));//电话
			mapStr.put("mobile",comMap.get("mobile"));//手机
			mapStr.put("certificatenum", comMap.get("certificatenum"));//身份证
			mapStr.put("folk", comMap.get("folk"));//民族
			mapStr.put("tempresidentnumber", comMap.get("tempresidentnumber"));//银行账户
			mapStr.put("startdate", comMap.get("startdate"));//合同开始日期
			mapStr.put("enddate", comMap.get("enddate"));//合同结束日期
			mapStr.put("residentplace",comMap.get("residentplace"));
			mapStr.put("isadaccount", "1");
			
			mapStr.put("nationality",hrb.getNationalityID());
			mapStr.put("systemlanguage",hrb.getSystemlanguage());
			mapStr.put("maritalstatus",hrb.getMaritalstatus());
			mapStr.put("dsporder",""+currentid);
			mapStr.put("createrid",hrb.getCreaterid());
			mapStr.put("createdate",hrb.getCreatedate());
			mapStr.put("accounttype","0");
			mapStr.put("belongto","-1");
			mapStr.put("needdynapass","0");
			mapStr.put("passwordstate","1");
			//mapStr.put("seclevel",comMap.get("SecLevel"));
			
			
			boolean isRun = tdu.insert(mapStr,"hrmresource");
			if(!isRun){
				ri.setMessage(false,"2220", "人员信息 插入操作错误！");
				return ri;
			}
			rs.executeSql("select id from hrmresource where workcode='"+workcode+"' ");
			if(rs.next()){
				id = Util.null2String(rs.getString("id"));
			}
			if("".equals(id)){
				ri.setMessage(false,"2221", "人员信息 插入后 无记录错误！");
				return ri;
			}
			String tableName = "cus_fielddata";
			Map<String,String> mainMap = new HashMap<String,String>();			
			mainMap.put("id", ""+id);
			mainMap.put("scope", "HrmCustomFieldByInfoType");
			mainMap.put("scopeid", "-1");
			String message = updateCustom(tableName,mainMap,cusMap);	
			ri.setRemark(message);		
			mainMap = new HashMap<String,String>();			
			mainMap.put("id", ""+id);
			mainMap.put("scope", "HrmCustomFieldByInfoType");
			mainMap.put("scopeid", "1");
			message = updateCustom(tableName,mainMap,cusMap1);	
			ri.setRemark(message);			
			try {			
				ResourceComInfo ResourceComInfo= new ResourceComInfo();
				ResourceComInfo.addResourceInfoCache(""+id);
			} catch (Exception e) {
				e.printStackTrace();
				log.writeLog(e.getMessage());
			}
			log.writeLog("testHrmUserSetting");
			Map<String,String> mapset = new HashMap<String,String>();
			sql = " select * from HrmUserSetting where resourceId="+id;
			log.writeLog("sql:"+sql);
			rs.execute(sql);
	        if(rs.getCounts()==0) {
	        	mapset.put("resourceId", id);
	        	mapset.put("rtxOnload", "0");
	        	mapset.put("isCoworkHead", "1");
	        	mapset.put("belongtoshow", "1");
	        	tdu.insert(mapset,"HrmUserSetting");
	        }else {
	        	rs.execute(" update HrmUserSetting set belongtoshow=1 where resourceId="+id);
	        }			
		}else {
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("workcode", comMap.get("workcode"));//员工工号
			mapStr.put("lastname", comMap.get("lastname"));//姓名
			if(!"".equals(comMap.get("loginid"))) {
				mapStr.put("loginid", comMap.get("loginid"));//登陆帐号
			}			
			mapStr.put("email", comMap.get("email"));//电子邮件
			mapStr.put("sex", comMap.get("sex"));
			mapStr.put("birthday",comMap.get("birthday"));//生日
			mapStr.put("telephone",comMap.get("telephone"));//电话
			mapStr.put("mobile",comMap.get("mobile"));//手机
			mapStr.put("certificatenum", comMap.get("certificatenum"));//身份证
			mapStr.put("folk", comMap.get("folk"));//民族
			mapStr.put("tempresidentnumber", comMap.get("tempresidentnumber"));//银行账户
			mapStr.put("startdate", comMap.get("startdate"));//合同开始日期
			mapStr.put("enddate", comMap.get("enddate"));//合同结束日期
			mapStr.put("residentplace",comMap.get("residentplace"));
			Map<String,String> whereMap = new HashMap<String,String>();
			whereMap.put("id", id);
			boolean isRun = tdu.update("hrmresource", mapStr, whereMap);
			if(!isRun){
				ri.setMessage(false,"2222", "人员信息 更新错误！");
				return ri;
			}	
			String tableName = "cus_fielddata";
			Map<String,String> mainMap = new HashMap<String,String>();			
			mainMap.put("id", ""+id);
			mainMap.put("scope", "HrmCustomFieldByInfoType");
			mainMap.put("scopeid", "-1");
			String message = updateCustom(tableName,mainMap,cusMap);	
			ri.setRemark(message);		
			mainMap = new HashMap<String,String>();			
			mainMap.put("id", ""+id);
			mainMap.put("scope", "HrmCustomFieldByInfoType");
			mainMap.put("scopeid", "1");
			message = updateCustom(tableName,mainMap,cusMap1);	
			ri.setRemark(ri.getRemark()+message);			
			try {			
				ResourceComInfo ResourceComInfo= new ResourceComInfo();
				ResourceComInfo.addResourceInfoCache(""+id);
			} catch (Exception e) {
				e.printStackTrace();
				log.writeLog(e.getMessage());
			}
			sql="select * from hrmresource where belongto='"+id+"'";
			rs.executeSql(sql);
			while(rs.next()) {
				Map<String,String> mapStr1 = new HashMap<String,String>();
				//mapStr1.put("workcode", comMap.get("workcode"));//员工工号
				mapStr1.put("lastname", comMap.get("lastname"));//姓名
				//mapStr1.put("loginid", comMap.get("loginid"));//登陆帐号
				mapStr1.put("email", comMap.get("email"));//电子邮件
				mapStr1.put("sex", comMap.get("sex"));
				mapStr1.put("birthday",comMap.get("birthday"));//生日
				mapStr1.put("telephone",comMap.get("telephone"));//电话
				mapStr1.put("mobile",comMap.get("mobile"));//手机
				mapStr1.put("certificatenum", comMap.get("certificatenum"));//身份证
				mapStr1.put("folk", comMap.get("folk"));//民族
				mapStr1.put("tempresidentnumber", comMap.get("tempresidentnumber"));//银行账户
				mapStr1.put("startdate", comMap.get("startdate"));//合同开始日期
				mapStr1.put("enddate", comMap.get("enddate"));//合同结束日期
				mapStr1.put("residentplace",comMap.get("residentplace"));
				Map<String,String> whereMap1 = new HashMap<String,String>();
				whereMap1.put("id", rs.getString("id"));
				boolean isRun1 = tdu.update("hrmresource", mapStr1, whereMap1);
				if(!isRun1){
					ri.setMessage(false,"2222", "人员信息 更新错误！id:"+rs.getString("id"));
					return ri;
				}	
				tableName = "cus_fielddata";
				Map<String,String> mainMap1 = new HashMap<String,String>();			
				mainMap1.put("id", ""+rs.getString("id"));
				mainMap1.put("scope", "HrmCustomFieldByInfoType");
				mainMap1.put("scopeid", "-1");
				String message1 = updateCustom(tableName,mainMap1,cusMap);	
				ri.setRemark(ri.getRemark()+message1);		
				mainMap1 = new HashMap<String,String>();			
				mainMap1.put("id", ""+rs.getString("id"));
				mainMap1.put("scope", "HrmCustomFieldByInfoType");
				mainMap1.put("scopeid", "1");
				message1 = updateCustom(tableName,mainMap1,cusMap1);	
				ri.setRemark(ri.getRemark()+message1);			
				try {			
					ResourceComInfo ResourceComInfo= new ResourceComInfo();
					ResourceComInfo.addResourceInfoCache(""+rs.getString("id"));
				} catch (Exception e) {
					e.printStackTrace();
					log.writeLog(e.getMessage());
				}
			}
		}
		
		return ri;
	}
	
	public ReturnInfo updatePersonJobInfo(Map<String, String> jobMap,Map<String, String> jobCusMap){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		RecordSet rs = new RecordSet();
		String sql = "";
		String deptID = "";
		String comID = "";
		String ryid = jobMap.get("ryid");
		sql = "select id from HrmDepartment where departmentcode='"+jobMap.get("deptId")+"'";
		rs.executeSql(sql);
		if(rs.next()){
			deptID  = Util.null2String(rs.getString("id"));
		}
		if("".equals(deptID)){
			ri.setMessage(false, "2201", "人员的部门不存在!");
			return ri;
		}
		
		sql = "select subcompanyid1 from HrmDepartment where id="+deptID;
		rs.executeSql(sql);
		if(rs.next()){
			comID  = Util.null2String(rs.getString("subcompanyid1"));
		}
		if("".equals(comID)){
			ri.setMessage(false, "2202", "分部的部门不存在!");
			return ri;
		}
		Map<String,String> mapStr = new HashMap<String,String>();
		mapStr.put("status", jobMap.get("status"));
		mapStr.put("datefield1", jobMap.get("datefield1"));
		mapStr.put("datefield2", jobMap.get("datefield2"));
		mapStr.put("datefield4", jobMap.get("datefield4"));
		mapStr.put("jobtitle", jobMap.get("jobtitle"));
		mapStr.put("departmentid", deptID);
		mapStr.put("subcompanyid1", comID);
		if(!"".equals(jobMap.get("managerid"))) {
			mapStr.put("managerid", jobMap.get("managerid"));
		}
		mapStr.put("loginid", jobMap.get("loginid"));
		String seclevel = Util.null2String(jobMap.get("seclevel"));
		if(!"".equals(seclevel)) {
			mapStr.put("seclevel", seclevel);
		}
		Map<String,String> whereMap = new HashMap<String,String>();
		whereMap.put("id", ryid);
		boolean isRun = tdu.update("hrmresource", mapStr, whereMap);
		if(!isRun){
			ri.setMessage(false,"2222", "人员信息 更新错误！");
			return ri;
		}	
		AllManagers al = new AllManagers();
		String managerstr = al.getAllManagerstr(""+ryid);
		sql="update hrmresource set managerstr=',"+managerstr+",' where id="+ryid;
		rs.executeSql(sql);
		
		String tableName = "cus_fielddata";
		Map<String,String> updateMap = jobCusMap;
		Map<String,String> mainMap = new HashMap<String,String>();			
		mainMap.put("id", ryid);
		mainMap.put("scope", "HrmCustomFieldByInfoType");
		mainMap.put("scopeid", "-1");
		String message = updateCustom(tableName,mainMap,updateMap);	
		ri.setRemark(message);		
				
		try {			
			ResourceComInfo ResourceComInfo= new ResourceComInfo();
			ResourceComInfo.addResourceInfoCache(ryid);
		} catch (Exception e) {
			e.printStackTrace();
			log.writeLog(e.getMessage());
		}
		return ri;
	}
	/**
	 * 插入兼岗账号 复制主账号数据
	 * @param jobMap
	 * @param jobCusMap
	 * @return
	 */
	public ReturnInfo insertPersonJobInfo(Map<String, String> jobMap,Map<String, String> jobCusMap){
		TmcDBUtil tdu = new TmcDBUtil();
		BaseBean log = new BaseBean();
		ReturnInfo ri = new ReturnInfo();
		RecordSet rs = new RecordSet();
		String sql = "";
		String deptID = "";
		String comID = "";
		String id = "";
		int count = 0;
		String zgid = jobMap.get("belongto");
		sql = "select count(1) as count from hrmresource where id="+zgid;		
		rs.executeSql(sql);
		if(rs.next()) {
			count = rs.getInt("count");
		}
		if(count < 0) {
			ri.setMessage(false,"", "主岗人员不存在");
			return ri;
		}
		sql = "select id from HrmDepartment where departmentcode='"+jobMap.get("deptId")+"'";
		rs.executeSql(sql);
		if(rs.next()){
			deptID  = Util.null2String(rs.getString("id"));
		}
		if("".equals(deptID)){
			ri.setMessage(false, "2201", "人员的部门不存在!");
			return ri;
		}
		
		sql = "select subcompanyid1 from HrmDepartment where id="+deptID;
		rs.executeSql(sql);
		if(rs.next()){
			comID  = Util.null2String(rs.getString("subcompanyid1"));
		}
		if("".equals(comID)){
			ri.setMessage(false, "2202", "分部的部门不存在!");
			return ri;
		}
		HrmResourceBean hrb = new HrmResourceBean();
		Map<String,String> mapStr = new HashMap<String,String>();
		Map<String,String> updateMap=jobCusMap;
		Map<String,String> updateMap1= new HashMap<String, String>();
		sql="select * from hrmresource where id="+zgid;
		rs.executeSql(sql);
		if(rs.next()) {
			mapStr.put("workcode", jobMap.get("workcode"));
			//mapStr.put("loginid", hrb.getLoginid());
			mapStr.put("status", jobMap.get("status"));
			mapStr.put("lastname", Util.null2String(rs.getString("lastname")));
			mapStr.put("sex",Util.null2String(rs.getString("sex")));
			mapStr.put("birthday",Util.null2String(rs.getString("birthday")));
			//mapStr.put("seclevel", ""+hrb.getSeclevel());
			mapStr.put("jobtitle", jobMap.get("jobtitle"));
			mapStr.put("departmentid", deptID);
			mapStr.put("subcompanyid1", comID);
			if(!"".equals(jobMap.get("managerid"))) {
				mapStr.put("managerid", jobMap.get("managerid"));
			}
			mapStr.put("nationality",Util.null2String(rs.getString("nationality")));
			mapStr.put("systemlanguage",Util.null2String(rs.getString("systemlanguage")));
			//mapStr.put("password",hrb.getPassword());
			mapStr.put("maritalstatus",Util.null2String(rs.getString("maritalstatus")));
			mapStr.put("telephone",Util.null2String(rs.getString("telephone")));
			mapStr.put("mobile",Util.null2String(rs.getString("mobile")));
			mapStr.put("mobilecall",Util.null2String(rs.getString("mobilecall")));
			mapStr.put("email",Util.null2String(rs.getString("email")));
			mapStr.put("dsporder",Util.null2String(rs.getString("dsporder")));
			mapStr.put("createrid",hrb.getCreaterid());
			mapStr.put("createdate",hrb.getCreatedate());
			mapStr.put("accounttype","1");
			mapStr.put("belongto",jobMap.get("belongto"));
			mapStr.put("needdynapass",Util.null2String(rs.getString("needdynapass")));
			mapStr.put("passwordstate",Util.null2String(rs.getString("passwordstate")));
			mapStr.put("locationid",Util.null2String(rs.getString("locationid")));
			mapStr.put("workroom",Util.null2String(rs.getString("workroom")));
			mapStr.put("homeaddress",Util.null2String(rs.getString("homeaddress")));
			mapStr.put("startdate",Util.null2String(rs.getString("startdate")));
			mapStr.put("enddate",Util.null2String(rs.getString("enddate")));
			mapStr.put("datefield1",jobMap.get("datefield1"));
			mapStr.put("datefield2",jobMap.get("datefield2"));
			mapStr.put("datefield3",Util.null2String(rs.getString("datefield3")));
			mapStr.put("datefield4",jobMap.get("datefield4"));
			mapStr.put("datefield5",Util.null2String(rs.getString("datefield5")));
			mapStr.put("numberfield1",Util.null2String(rs.getString("numberfield1")));
			mapStr.put("numberfield2",Util.null2String(rs.getString("numberfield2")));
			mapStr.put("numberfield3",Util.null2String(rs.getString("numberfield3")));
			mapStr.put("numberfield4",Util.null2String(rs.getString("numberfield4")));
			mapStr.put("numberfield5",Util.null2String(rs.getString("numberfield5")));
			mapStr.put("textfield1",Util.null2String(rs.getString("textfield1")));
			mapStr.put("textfield2",Util.null2String(rs.getString("textfield2")));
			mapStr.put("textfield3",Util.null2String(rs.getString("textfield3")));
			mapStr.put("textfield4",Util.null2String(rs.getString("textfield4")));
			mapStr.put("textfield5",Util.null2String(rs.getString("textfield5")));
			mapStr.put("tinyintfield1",Util.null2String(rs.getString("tinyintfield1")));
			mapStr.put("tinyintfield2",Util.null2String(rs.getString("tinyintfield2")));
			mapStr.put("tinyintfield3",Util.null2String(rs.getString("tinyintfield3")));
			mapStr.put("tinyintfield4",Util.null2String(rs.getString("tinyintfield4")));
			mapStr.put("tinyintfield5",Util.null2String(rs.getString("tinyintfield5")));
			mapStr.put("jobactivitydesc",Util.null2String(rs.getString("jobactivitydesc")));
			mapStr.put("certificatenum",Util.null2String(rs.getString("certificatenum")));
			mapStr.put("nativeplace",Util.null2String(rs.getString("nativeplace")));
			mapStr.put("educationlevel",Util.null2String(rs.getString("educationlevel")));
			mapStr.put("regresidentplace",Util.null2String(rs.getString("regresidentplace")));
			mapStr.put("healthinfo",Util.null2String(rs.getString("healthinfo")));
			mapStr.put("policy",Util.null2String(rs.getString("policy")));
			mapStr.put("degree",Util.null2String(rs.getString("degree")));
			mapStr.put("height",Util.null2String(rs.getString("height")));
			mapStr.put("jobcall",Util.null2String(rs.getString("jobcall")));
			mapStr.put("accumfundaccount",Util.null2String(rs.getString("accumfundaccount")));
			mapStr.put("birthplace",Util.null2String(rs.getString("birthplace")));
			mapStr.put("folk",Util.null2String(rs.getString("folk")));
			mapStr.put("extphone",Util.null2String(rs.getString("extphone")));
			mapStr.put("fax",Util.null2String(rs.getString("fax")));
			mapStr.put("weight",Util.null2String(rs.getString("weight")));
			mapStr.put("tempresidentnumber",Util.null2String(rs.getString("tempresidentnumber")));
			mapStr.put("probationenddate",Util.null2String(rs.getString("probationenddate")));
			mapStr.put("bankid1",Util.null2String(rs.getString("bankid1")));
			mapStr.put("accountid1",Util.null2String(rs.getString("accountid1")));			
			String seclevel = Util.null2String(jobMap.get("seclevel"));
			if(!"".equals(seclevel)) {
				mapStr.put("seclevel", seclevel);
			}
		}
		sql="select * from cus_fielddata where  scope='HrmCustomFieldByInfoType' and scopeid='-1' and id="+zgid;
		rs.executeSql(sql);
		if(rs.next()) {
			updateMap.put("field13", Util.null2String(rs.getString("field13")));
			updateMap.put("field14", Util.null2String(rs.getString("field14")));
			updateMap.put("field15", Util.null2String(rs.getString("field15")));
			updateMap.put("field16", Util.null2String(rs.getString("field16")));
			updateMap.put("field17", Util.null2String(rs.getString("field17")));
			updateMap.put("field18", Util.null2String(rs.getString("field18")));
			updateMap.put("field19", Util.null2String(rs.getString("field19")));
			updateMap.put("field20", Util.null2String(rs.getString("field20")));
			updateMap.put("field21", Util.null2String(rs.getString("field21")));
			updateMap.put("field26", Util.null2String(rs.getString("field26")));
			updateMap.put("field27", Util.null2String(rs.getString("field27")));
			updateMap.put("field37", Util.null2String(rs.getString("field37")));
			updateMap.put("field36", Util.null2String(rs.getString("field36")));
		}
		
		sql="select * from cus_fielddata where  scope='HrmCustomFieldByInfoType' and scopeid='1' and id="+zgid;
		rs.executeSql(sql);
		if(rs.next()) {
			updateMap1.put("field12", Util.null2String(rs.getString("field12")));
			updateMap1.put("field22", Util.null2String(rs.getString("field22")));
			updateMap1.put("field23", Util.null2String(rs.getString("field23")));
			updateMap1.put("field24", Util.null2String(rs.getString("field24")));
			updateMap1.put("field28", Util.null2String(rs.getString("field28")));
			updateMap1.put("field29", Util.null2String(rs.getString("field29")));
			updateMap1.put("field30", Util.null2String(rs.getString("field30")));
		}
		int  currentid = 0;
		int nextid=0;
		// 处理系统最大需要问题
		sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
		rs.executeSql(sql);
		if(rs.next()){
			currentid = rs.getInt("currentid");
		}
		nextid = currentid+1;
		rs.executeSql("update SequenceIndex set currentid=" +nextid + " where indexdesc='resourceid'");
		
		mapStr.put("id",String.valueOf(currentid));
		boolean isRun = tdu.insert(mapStr,"hrmresource");
		if(!isRun){
			ri.setMessage(false,"2220", "人员信息 插入操作错误！");
			return ri;
		}
		
		rs.executeSql("select id from hrmresource where workcode='"+jobMap.get("workcode")+"' ");
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
		}
		if("".equals(id)){
			ri.setMessage(false,"2221", "人员信息 插入后 无记录错误！");
			return ri;
		}
		AllManagers al = new AllManagers();
		String managerstr = al.getAllManagerstr(""+id);
		sql="update hrmresource set managerstr=',"+managerstr+",' where id="+id;
		rs.executeSql(sql);
		
		String tableName = "cus_fielddata";
		Map<String,String> mainMap = new HashMap<String,String>();			
		mainMap.put("id", ""+id);
		mainMap.put("scope", "HrmCustomFieldByInfoType");
		mainMap.put("scopeid", "-1");
		String message = updateCustom(tableName,mainMap,updateMap);	
		ri.setRemark(message);		
		mainMap = new HashMap<String,String>();			
		mainMap.put("id", ""+id);
		mainMap.put("scope", "HrmCustomFieldByInfoType");
		mainMap.put("scopeid", "1");
		message = updateCustom(tableName,mainMap,updateMap1);	
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
}
