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

public class InsertSource {
	
	
	public String insert(String lastname,String departmentid,String jobtitlename,
			String seclevel,String workcode ,String joblevel ,String workcode_manager) {
		
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "";
		String departmentname = "";//部门姓名
		String jobtitleid = "";//岗位id
		String seclevel_new = "";//安全级别
		String subcompanyid1 = "";//分布
		String manager_id = "";//上级的编号；
		
		//检验参数
		if ("".equals(lastname)||"".equals(departmentid)||"".equals(jobtitlename )||"".equals(seclevel)
				||"".equals(workcode)||"".equals(joblevel)||"".equals(workcode_manager)) {
	        Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "必填项不能为空");
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
        	sql = "select id from hrmresource where workcode = '"+workcode+"' ";
            rs.executeSql(sql);
            if(rs.next()) {
            	Map<String, String> retMap = new HashMap<String, String>();
     			retMap.put("MSG_TYPE", "E");
     			retMap.put("MSG_CONTENT", "该编号人员已经存在");
     			log.writeLog("info:" + getJsonStr(retMap));
     			return getJsonStr(retMap);
            }
        }
        
        if("".equals(departmentname)) {
        	Map<String, String> retMap = new HashMap<String, String>();
 			retMap.put("MSG_TYPE", "E");
 			retMap.put("MSG_CONTENT", "部门不存在");
 			log.writeLog("info:" + getJsonStr(retMap));
 			return getJsonStr(retMap);
        }
        //根据部门获得分布信息
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
        int isSeclevel=Integer.parseInt(seclevel);
        switch (isSeclevel){
              case 1:
            	  seclevel_new = "20";
                  break;
               case 2:
            	   seclevel_new = "30";
            	   break;
               case 3:
            	   seclevel_new = "40";
            	   break;
               case 4:
            	   seclevel_new = "50";
            	   break;
               case 5:
            	   seclevel_new = "60";
            	   break;
               case 6:
            	   seclevel_new = "70";
            	   break;
               case 7:
            	   seclevel_new = "80";
            	   break;
               case 8:
            	   seclevel_new = "90";
            	   break;
               default:
                    break;
                }
  
        //直接上级
        sql = "select id  from hrmresource where workcode = '"+workcode_manager+"'";
        rs.executeSql(sql);
        if(!rs.next()) {
        	Map<String, String> retMap = new HashMap<String, String>();
        	retMap.put("MSG_TYPE", "E"); 
			retMap.put("MSG_CONTENT", "没有上级这个编号");
			log.writeLog("info:" + getJsonStr(retMap));
			return getJsonStr(retMap);
        }else {
        	manager_id = Util.null2String(rs.getString("id"));
        }
        
        
        //插入
        sql = "insert into hrmresource (id,lastname,departmentid,jobtitle,seclevel,workcode,joblevel,managerid,status,subcompanyid1) "
				+ "values("+nextid+",'"+lastname+"','"+departmentid+"','"+jobtitleid+"','"+seclevel_new+""
						+ "',"+workcode+",'"+joblevel+"','"+manager_id+"',0,'"+subcompanyid1+"')";
		rs.executeSql(sql);
		log.writeLog(sql+"最终测试插入情况");
		Map<String, String> retMap = new HashMap<String, String>();
    	retMap.put("MSG_TYPE", "S");
		retMap.put("MSG_CONTENT", "添加成功");
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
}
