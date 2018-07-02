package goodbaby.hr.rz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

import goodbaby.tmc.org.HrmJobTitleBean;
import goodbaby.tmc.org.HrmOrgAction;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

public class InsertHrmResourceService {
	
	
	public String InsertHrmResource(String lastname,String departmentid,String jobtitlename,
			String seclevel,String workcode ,String joblevel ,String workcode_manager) {
		
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "";
		String departmentname = "";//部门姓名
		String jobtitleid = "";//岗位id
		String seclevel_new = "";//安全级别
		String subcompanyid1 = "";//分布
		String manager_id = "";//上级的编号；
		log.writeLog("InsertHrmResource start lastname:" + lastname+" departmentid:"+departmentid+" jobtitlename:"+jobtitlename+" seclevel:"+seclevel+" workcode:"+workcode+
				" joblevel:"+joblevel+" workcode_manager:"+workcode_manager);
		
		//检验参数
		if ("".equals(lastname)||"".equals(departmentid)||"".equals(jobtitlename )
				||"".equals(workcode)||"".equals(workcode_manager)) {
	        Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "输入的参数有空值");
			log.writeLog("info:" + getJsonStr(retMap));
			return getJsonStr(retMap);
		}
		
		//员工的id
		int currentid = 0;
        int nextid = 0;
        sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
        rs.executeSql(sql);
        if (rs.next()) {
          currentid = rs.getInt("currentid");
        }
        sql = "select max(id)+1 as nextid from hrmresource";
        rs.executeSql(sql);
        if (rs.next()) {
          nextid = rs.getInt("nextid");
        }
        if (currentid != nextid) {
          rs.executeSql("update SequenceIndex set currentid=" + nextid + " where indexdesc='resourceid'");
        }
        
        //部门
        sql  = "select departmentname from hrmdepartment where id = "+departmentid;
        rs.executeSql(sql);
        if(rs.next()){
        	departmentname = Util.null2String(rs.getString("departmentname"));
        }
        if("".equals(departmentname)) {
        	Map<String, String> retMap = new HashMap<String, String>();
 			retMap.put("MSG_TYPE", "E");
 			retMap.put("MSG_CONTENT", "部门不存在");
 			log.writeLog("info:" + getJsonStr(retMap));
 			return getJsonStr(retMap);
        }
        
        //工号
        sql = "select id from hrmresource where workcode = '"+workcode+"' ";
        rs.executeSql(sql);
        if(rs.next()) {
        	Map<String, String> retMap = new HashMap<String, String>();
 			retMap.put("MSG_TYPE", "E");
 			retMap.put("MSG_CONTENT", "该编号人员已经存在");
 			log.writeLog("info:" + getJsonStr(retMap));
 			return getJsonStr(retMap);
        }
        
        //根据部门获得分部信息
        sql = "select subcompanyid1 from hrmdepartment where id = "+departmentid;
        rs.executeSql(sql);
        if(rs.next()) {
        	subcompanyid1 = Util.null2String(rs.getString("subcompanyid1"));
        }

        //岗位
        sql = "select id from HrmJobTitles where jobtitlename = '"+ jobtitlename+"' ";
        rs.executeSql(sql);
        if(rs.next()) {
        	jobtitleid = Util.null2String(rs.getString("id"));
        }
        if("".equals(jobtitleid)) {
        	HrmOrgAction  hoa = new HrmOrgAction();
        	HrmJobTitleBean hjt = new HrmJobTitleBean();
        	hjt.setJobtitlecode("");
    		hjt.setJobtitlename(jobtitlename);
    		hjt.setJobtitlemark(jobtitlename);
    		hjt.setJobtitleremark(jobtitlename);
    		hjt.setDeptIdOrCode(0);
    		hjt.setJobdepartmentid(departmentid);
    		hjt.setJobdepartmentCode("0");
    		hjt.setSuperJobCode("");
    		hjt.setJobactivityName(jobtitlename);
    		hjt.setJobGroupName(jobtitlename);
    		hoa.operJobtitle(hjt);
    		rs.executeSql(sql);
	        if(rs.next()) {
	        	jobtitleid = Util.null2String(rs.getString("id"));
	        }
        }
        
        //安全级别
        if("".equals(seclevel)) {
        	seclevel_new = "10";
        }
        if("无".equals(seclevel)) {
        	seclevel_new = "10";
        }
        if("一级".equals(seclevel)) {
        	seclevel_new = "20";
        }
        if("二级".equals(seclevel)) {
        	seclevel_new = "30";
        }
        if("三级".equals(seclevel)) {
        	seclevel_new = "40";
        }
        if("四级".equals(seclevel)) {
        	seclevel_new = "50";
        }
        if("五级".equals(seclevel)) {
        	seclevel_new = "60";
        }
        if("六级".equals(seclevel)) {
        	seclevel_new = "70";
        }
        if("七级".equals(seclevel)) {
        	seclevel_new = "80";
        }
        if("八级".equals(seclevel)) {
        	seclevel_new = "90";
        }
        
       
  
        //直接上级
        sql = "select id  from hrmresource where workcode = '"+workcode_manager+"'";
        rs.executeSql(sql);
        if(rs.next()) {
        	manager_id = Util.null2String(rs.getString("id"));;
        }else {
        	manager_id = Util.null2String(rs.getString("id"));
        	Map<String, String> retMap = new HashMap<String, String>();
        	retMap.put("MSG_TYPE", "E"); 
			retMap.put("MSG_CONTENT", "上级编号不存在");
			log.writeLog("info:" + getJsonStr(retMap));
			return getJsonStr(retMap);
        }
        
        //插入
        sql = "insert into hrmresource (id,lastname,departmentid,jobtitle,seclevel,workcode,joblevel,managerid,status,subcompanyid1) "
				+ "values("+nextid+",'"+lastname+"','"+departmentid+"','"+jobtitleid+"','"+seclevel_new+""
						+ "','"+workcode+"','"+Util.getIntValue(joblevel, 0)+"','"+manager_id+"',0,'"+subcompanyid1+"')";
		rs.executeSql(sql);
		
		sql  = "select id from hrmresource where id = " + nextid;
		rs.executeSql(sql);
		if(rs.next()) {
			String isId = Util.null2String(rs.getString("id"));
		}else {
			Map<String, String> retMap = new HashMap<String, String>();
        	retMap.put("MSG_TYPE", "E"); 
			retMap.put("MSG_CONTENT", "添加失败");
			log.writeLog("info:" + getJsonStr(retMap));
			return getJsonStr(retMap);
		}
		
		Map<String, String> fieldmap = new HashMap<String,String>();
	        
	     fieldmap.put("field1", seclevel);
	     fieldmap.put("scope", "HrmCustomFieldByInfoType");
	     fieldmap.put("scopeid", "1");
	     fieldmap.put("id", String.valueOf(nextid));
	     insert(fieldmap, "cus_fielddata");
	     try
	        {
	          ResourceComInfo ResourceComInfo = new ResourceComInfo();
	          ResourceComInfo.addResourceInfoCache(String.valueOf(nextid));
	        } catch (Exception e) {
	          e.printStackTrace();
	          log.writeLog(e.getMessage());
	        }
	        
		Map<String, String> retMap = new HashMap<String, String>();
    	retMap.put("MSG_TYPE", "S");
		retMap.put("MSG_CONTENT", ""+nextid);
		log.writeLog("info:" + getJsonStr(retMap));
		return getJsonStr(retMap);
        
	}
	
	
	//返回类型的封装
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return json.toString();
	}
	
	private boolean insert(Map<String,String> mapStr,String table){
		if(mapStr == null) return false;
		if(mapStr.isEmpty()) return false;

		RecordSet rs = new RecordSet();
		
		BaseBean log = new BaseBean();
		String sql_0 = "insert into "+table+"(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";
		
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			
			if(tmp_1_str.length() > 0){
				sql_1.append(tmp_1);sql_1.append(",");
				
				if(tmp_1_str.contains("##")){
					sql_3.append(tmp_1_str.replace("##", ""));sql_3.append(",");
				}else{
					sql_3.append("'");sql_3.append(tmp_1_str);sql_3.append("',");
				}
			}
		}
		
		String now_sql_1 = sql_1.toString();
		if(now_sql_1.lastIndexOf(",")>0){
			now_sql_1 = now_sql_1.substring(0,now_sql_1.length()-1);
		}
		
		String now_sql_3 = sql_3.toString();
		if(now_sql_3.lastIndexOf(",")>0){
			now_sql_3 = now_sql_3.substring(0,now_sql_3.length()-1);
		}
		
		String sql = sql_0 + now_sql_1 + sql_2 + now_sql_3 + sql_4;
		log.writeLog("insert(sql) = " + sql);
		return rs.executeSql(sql);
	}
}
