package gvo.webservice;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.db2.jcc.am.s;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.MD5;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.ldap.LdapUtil;

public class AttendanceIntegrateImpl {
	/**
	 * 登陆检验
	 * @param loginid 登陆名
	 * @param password 密码
	 * @return
	 */
	public String logincheck(String loginid,String password){
		RecordSet rs =new RecordSet();
		String sql="";
		MD5 md5 = new MD5();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddhhmmss");
		String nowtime = sf.format(new Date());
		int count = 0;
		String workcode = "";
		String lastname = "";
		String ryid = "";
		Map<String, String> map = new HashMap<String, String>();
		if("".equals(Util.null2String(loginid))||"".equals(Util.null2String(password))){
			map.put("result", "E");
			map.put("workcode", "");
			map.put("name", "");
			map.put("checkno", "");
			return getJsonStr(map);
		}
		String passwordmd5 = md5.getMD5ofStr(password);
		sql="select count(1) as count from hrmresource where loginid='"+loginid+"' and status<4";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count <=0){
			map.put("result", "E");
			map.put("workcode", "");
			map.put("name", "");
			map.put("checkno", "");
			return getJsonStr(map);
		}
		String isADAccount = "";
		sql = "select isADAccount from HrmResource where loginid = '" + loginid + "'";
		rs.executeSql(sql);
	    if (rs.next()) {
	    	isADAccount = rs.getString("isADAccount");
	    }
	    String authentic = Prop.getPropValue(GCONST.getConfigFile(), "authentic");
	    if("ldap".equals(authentic)&&"1".equals(isADAccount)){
	    	LdapUtil lu =LdapUtil.getInstance();
	    	boolean ispass = false;
	    	String username = lu.authenticuser(loginid);
            if (username == null || "uac".equals(username)) {
            	ispass = false;
            }else{
            	ispass = lu.authentic(loginid,password);
            }
            if(!ispass){
            	map.put("result", "E");
    			map.put("workcode", "");
    			map.put("name", "");
    			map.put("checkno", "");
    			return getJsonStr(map);
            }
	    }else{
	    	count = 0;
	    	sql="select count(1) as count from hrmresource where loginid='"+loginid+"' and password='"+passwordmd5+"' and status<4";
			rs.executeSql(sql);
			if(rs.next()){
				count = rs.getInt("count");
			}
			if(count <=0){
				map.put("result", "E");
				map.put("workcode", "");
				map.put("name", "");
				map.put("checkno", "");
				return getJsonStr(map);
			}
	    }
		sql="select id,workcode,lastname from hrmresource where loginid='"+loginid+"'  and status<4";
		rs.executeSql(sql);
		if(rs.next()){
			ryid = Util.null2String(rs.getString("id"));
			workcode = Util.null2String(rs.getString("workcode"));
			lastname = Util.null2String(rs.getString("lastname"));
		}
		int num = (int)(Math.random()*(9999-1000+1))+1000;
		String checkno = loginid+nowtime+password+"check"+num;
		checkno = md5.getMD5ofStr(checkno);
		count = 0;
		sql="select count(1) as count from uf_kq_person_record where ryid='"+ryid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count >0){
			sql="update uf_kq_person_record set workcode='"+workcode+"',checkno='"+checkno+"',checktime='"+nowtime+"' where ryid='"+ryid+"'";
			rs.executeSql(sql);
		}else{
			sql="insert into uf_kq_person_record(ryid,workcode,checkno,checktime) values('"+ryid+"','"+workcode+"','"+checkno+"','"+nowtime+"')";
			rs.executeSql(sql);
		}
		
		map.put("result", "S");
		map.put("workcode", workcode);
		map.put("name", lastname);
		map.put("checkno", checkno);
		return getJsonStr(map);
		
	}
	
	public String getPersonListByName(String name) throws Exception{
		RecordSet rs = new RecordSet();
		String sql="";
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		if("".equals(name)){
			json.put("result", "S");
			json.put("items", ja);
			json.put("context", "姓名不能为空");
			return json.toString();
		}
		sql="select a.lastname,a.workcode,b.jobtitlename,c.departmentname from hrmresource a,hrmjobtitles b,hrmdepartment c where a.jobtitle=b.id and a.departmentid=c.id and a.status<4 and a.lastname ='"+name+"' and nvl(a.belongto,0)<=0";
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("workcode", Util.null2String(rs.getString("workcode")));
			jo.put("jobtitle", Util.null2String(rs.getString("jobtitlename")));
			jo.put("department", Util.null2String(rs.getString("departmentname")));
			jo.put("name", Util.null2String(rs.getString("lastname")));
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context", "");
		return json.toString();
	}
	
	public String  getLowerLevelPerson(String workcode) throws Exception{
		RecordSet rs = new RecordSet();
		String sql="";
		String managerid="";
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		if("".equals(workcode)){
			json.put("result", "S");
			json.put("items", ja);
			json.put("context", "工号不能为空");
			return json.toString();
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			managerid = Util.null2String(rs.getString("id"));
		}
		if("".equals(managerid)){
			json.put("result", "S");
			json.put("items", ja);
			json.put("context", "人员编号无法匹配");
			return json.toString();
		}
		sql="select workcode,lastname from hrmresource where managerid='"+managerid+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("workcode", Util.null2String(rs.getString("workcode")));
			jo.put("name", Util.null2String(rs.getString("lastname")));
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context", "");
		return json.toString();
	}
	
	public String getInProcessHours(String workcode,String holidayType,String date,String checkno){
		RecordSet rs = new RecordSet();
		String sql = "";
		String sqr = "";
		String workflowid = "";
		String tablename = "";
		String hours ="0";
		int count = 0;
		Map<String, String> retMap = new HashMap<String, String>();
		if("".equals(workcode)||"".equals(holidayType)||"".equals(date)||"".equals(checkno)){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "接口参数不能为空");
			return getJsonStr(retMap);
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			sqr = Util.null2String(rs.getString("id"));
		}
		if("".equals(sqr)){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "人员编号无法匹配");
			return getJsonStr(retMap);
		}
		sql="select count(1) as count from uf_kq_person_record where workcode='"+workcode+"' and checkno='"+checkno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count <=0){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "该用户登录校验不正确");
			return getJsonStr(retMap);
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String year ="";
		try {
			year=sf.format(sf.parse(date));
		} catch (ParseException e) {
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "日期参数格式异常");
			return getJsonStr(retMap);
		}
		sql="select wfid from uf_weixinkq_wfmt where code='HR-041'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("wfid"));
		}
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename"));
		}
		sql="select nvl(sum(nvl(b.hours,0)),0) as hours from "+tablename+" a,"+tablename+"_dt1 b,workflow_requestbase c where a.id=b.mainid and a.requestid=c.requestid and c.currentnodetype<3 and c.workflowid='"+workflowid+"' and to_char(to_date(b.begindate,'yyyy-mm-dd'),'yyyy')='"+year+"' and b.name='"+getPersonID(workcode)+"' and b.absence_type='"+holidayType+"'";
		rs.executeSql(sql);
		if(rs.next()){
			hours=Util.null2String(rs.getString("hours"));
		}
		retMap.put("result", "S");
		retMap.put("time", hours);
		retMap.put("context", "");
		return getJsonStr(retMap);
	}
	
	/**
	 * 创建流程
	 * @param workflowType
	 * @param workcode
	 * @param dataInfo
	 * @param checkno
	 * @return
	 */
	public String createRequestInfo(String workflowType,String workcode,String dataInfo,String checkno){
		RecordSet rs = new RecordSet();
		String result = "";
		String creater = "";
		String sql = "";
		String workflowid = "";
		String tablename ="";
		int count = 0;
		Map<String, String> retMap = new HashMap<String, String>();
		if("".equals(workflowType)||"".equals(workcode)||"".equals(dataInfo)||"".equals(checkno)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "接口参数不能为空");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		sql="select wfid from uf_weixinkq_wfmt where code='"+workflowType+"'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("wfid"));
		}
		if("".equals(workflowid)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "流程类型无法匹配");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename"));
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			creater = Util.null2String(rs.getString("id"));
		}
		if("".equals(creater)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");	
			return getJsonStr(retMap);
		}
		sql="select count(1) as count from uf_kq_person_record where workcode='"+workcode+"' and checkno='"+checkno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count <=0){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "该用户登录校验不正确");
			retMap.put("OA_ID", "0");	
			return getJsonStr(retMap);
		}
		String jsonstr = "";
		if("HR-017".equals(workflowType)){//未打卡流程
			try {
				jsonstr=getHR017Json(creater,dataInfo);
			} catch (Exception e) {
				retMap.put("MSG_TYPE", "E");
				retMap.put("MSG_CONTENT", "JSON格式转换异常");
				retMap.put("OA_ID", "0");	
				return getJsonStr(retMap);
			}
		}else if("HR-012".equals(workflowType)){//加班
			
			try {
				int countnum=0;
				String residentplace = "";
				JSONObject jo = new JSONObject(dataInfo);
				String P_begindate=jo.getString("P_begindate");
				String P_begintime=jo.getString("P_begintime");
				String P_enddate=jo.getString("P_enddate");
				String P_endtime=jo.getString("P_endtime");
				String overtime_type=jo.getString("overtime_type");
				sql="select  count(1) as count from "+tablename+" a ,"+tablename+"_dt1 b where a.id=b.mainid and b.name="+creater+" and (b.a_begindate is  null or b.a_begintime is null or b.a_enddate is null or b.a_endtime is null)"+
						" and '"+P_begindate+P_begintime+"'<b.p_enddate||b.p_endtime and '"+P_enddate+P_endtime+"'>b.p_begindate||b.p_begintime ";
				rs.executeSql(sql);
				if(rs.next()){
					countnum = rs.getInt("count");
				}
				if(countnum<=0){
					sql="select  count(1) as count from "+tablename+" a ,"+tablename+"_dt1 b where a.id=b.mainid and b.name="+creater+" and (b.a_begindate is not null and b.a_begintime is not null and b.a_enddate is not null and b.a_endtime is not null)"+
							" and '"+P_begindate+P_begintime+"'<b.a_enddate||b.a_endtime and '"+P_enddate+P_endtime+"'>b.a_begindate||b.a_begintime ";
					rs.executeSql(sql);
					if(rs.next()){
						countnum = rs.getInt("count");
					}
				}
				if(countnum>0){
					retMap.put("MSG_TYPE", "E");
					retMap.put("MSG_CONTENT", "OA流程中存在重复的时间段加班，请检查");
					retMap.put("OA_ID", "0");	
					return getJsonStr(retMap);
				}
				sql="select residentplace from hrmresource where id="+creater;
				rs.executeSql(sql);
				if(rs.next()){
					residentplace = Util.null2String(rs.getString("residentplace"));
				}
				if("B".equals(residentplace)){
					if(!"法定节假日转支付".equals(overtime_type)){
						retMap.put("MSG_TYPE", "E");
						retMap.put("MSG_CONTENT", "B类员工无法申请该类型加班，请检查");
						retMap.put("OA_ID", "0");	
						return getJsonStr(retMap);
					}
				}
				jsonstr=getHR012Json(creater,dataInfo);
			} catch (Exception e) {
				retMap.put("MSG_TYPE", "E");
				retMap.put("MSG_CONTENT", "JSON格式转换异常");
				retMap.put("OA_ID", "0");	
				return getJsonStr(retMap);
			}
		}else if("HR-010".equals(workflowType)){//请假
			try {
				
				jsonstr=getHR010Json(creater,dataInfo);
			} catch (Exception e) {
				retMap.put("MSG_TYPE", "E");
				retMap.put("MSG_CONTENT", "JSON格式转换异常");
				retMap.put("OA_ID", "0");	
				return getJsonStr(retMap);
			}
		}else if("HR-023".equals(workflowType)){//销假
			try {
				jsonstr=getHR023Json(creater,dataInfo);
			} catch (Exception e) {
				retMap.put("MSG_TYPE", "E");
				retMap.put("MSG_CONTENT", "JSON格式转换异常");
				retMap.put("OA_ID", "0");	
				return getJsonStr(retMap);
			}
		}
		if(!"".equals(jsonstr)){
			AutoRequestService ars = new AutoRequestService();
			result = ars.createRequest(workflowid, jsonstr, creater, "1");
			
		}
		return result;
	}
	
	/**
	 * 
	 * @param creater
	 * @param datainfo {"detail":"事由","card_place":"应打卡地点","Witness":"证明人","Nocard_type":"未打卡类型","name":"姓名","Break_date":"违反考勤日期","repaircard_time_e":"下班卡时间","work_code":"工号","repaircard_time_s":"上班卡时间","Nocard_Reason":"未打卡原因"}
	 * @throws Exception 

	 */
	public String getHR017Json(String creater,String datainfo) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		ResourceComInfo rci = new ResourceComInfo();
		String nowdate = sf.format(new Date());
		String name = "";//姓名
		String work_code = "";//工号
		String Nocard_type = "";//未打卡类型
		String Nocard_Reason = "";//未打卡原因
		String Break_date = "";//违反考勤日期
		String repaircard_time_s = "";//上班卡时间
		String repaircard_time_e = "";//下班卡时间
		String card_place = "";//应打卡地点
		String Witness = "";//证明人
		String detail = "";//事由
		JSONObject jo = new JSONObject(datainfo);
		name = jo.getString("name");
		work_code = jo.getString("work_code");
		Nocard_type = jo.getString("Nocard_type");
		Nocard_Reason = jo.getString("Nocard_Reason");
		Break_date = jo.getString("Break_date");
		repaircard_time_s = jo.getString("repaircard_time_s");
		repaircard_time_e = jo.getString("repaircard_time_e");
		card_place = jo.getString("card_place");
		Witness = jo.getString("Witness");
		detail = jo.getString("detail");
		
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		json.put("HEADER", header);
		json.put("DETAILS", details);
			
		header.put("apply_name", creater);
		header.put("Witness", getPersonID(Witness));
		header.put("Witness_code", Witness);
		header.put("apply_code", work_code);
		header.put("apply_company", rci.getSubCompanyID(creater));
		header.put("apply_depart", rci.getDepartmentID(creater));
		header.put("apply_date", nowdate);	
			
		JSONArray dt11 = new JSONArray();
		JSONObject node = new JSONObject();
		node.put("work_code", work_code);
		node.put("name", creater);
		node.put("Break_date", Break_date);
		node.put("Nocard_type", Nocard_type);
		node.put("Nocard_Reason",Nocard_Reason);
		if("0".equals(Nocard_type)){
			node.put("repaircard_time", repaircard_time_s);
		}else if("1".equals(Nocard_type)){
			node.put("repaircard_time", repaircard_time_e);
		}
	
		node.put("card_place", card_place);		
		node.put("detail", detail);
		
		dt11.put(node);
		details.put("DT1", dt11);
		return json.toString();
	}
	
	/**
	 * 
	 * @param creater
	 * @param datainfo {"overtime_type":"加班类型","shift":"班次","month_hours":"当月加班时数","overtime_way":"加班方式","reason":"加班事由","P_hours":"加班时数","P_begindate":"开始日期","P_begintime":"开始时间","limit_hours":"加班受限时数","cday":"加班归属日期","name":"姓名","P_endtime":"结束时间","P_enddate":"结束日期","overtime_transfer":"加班转换","work_code":"工号"}
	   加班转换 0 转支付 1 转调休 加班方式 0 正常加班 1公出加班 2 出差加班
	 * @throws Exception 
	 */
	public String getHR012Json(String creater,String datainfo) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		ResourceComInfo rci = new ResourceComInfo();
		String nowdate = sf.format(new Date());
		String name = "";//姓名
		String work_code = "";//工号
		String overtime_transfer = "";//加班转换
		String overtime_way = "";//加班方式
		String cday = "";//加班归属日期
		String P_begindate = "";//开始日期
		String P_begintime = "";//开始时间
		String P_enddate = "";//结束日期
		String P_endtime = "";//结束时间
		String P_hours = "";//加班时数
		String month_hours = "";//当月加班时数
		String limit_hours = "";//加班受限时数
		String reason = "";//加班事由
		String shift = "";//班次
		String overtime_type = "";//加班类型
		JSONObject jo = new JSONObject(datainfo);
		name = jo.getString("name");
		work_code = jo.getString("work_code");
		overtime_transfer = jo.getString("overtime_transfer");
		overtime_way = jo.getString("overtime_way");
		cday = jo.getString("cday");
		P_begindate = jo.getString("P_begindate");
		P_begintime = jo.getString("P_begintime");
		P_enddate = jo.getString("P_enddate");
		P_endtime = jo.getString("P_endtime");
		P_hours = jo.getString("P_hours");
		month_hours = jo.getString("month_hours");
		limit_hours = jo.getString("limit_hours");
		shift = jo.getString("shift");
		overtime_type = jo.getString("overtime_type");
		reason = jo.getString("reason");
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		json.put("HEADER", header);
		json.put("DETAILS", details);
		
		header.put("writer", creater);
		header.put("apply_name", creater);
		header.put("apply_data", nowdate);
		
		header.put("apply_code", work_code);
		header.put("apply_company", rci.getSubCompanyID(creater));
		header.put("apply_depart", rci.getDepartmentID(creater));
		header.put("apply_postion", rci.getJobTitle(creater));
		header.put("reason", reason);

		JSONArray dt11 = new JSONArray();
		JSONObject node = new JSONObject();
		node.put("work_code", work_code);
		node.put("name", creater);
		node.put("department", rci.getDepartmentID(creater));
		node.put("cday", cday);
		node.put("overtime_transfer", overtime_transfer);
		node.put("shift", shift);
		node.put("overtime_type", overtime_type);		
		node.put("P_begindate", P_begindate);
		node.put("P_enddate", P_enddate);
		node.put("P_begintime", P_begintime);
		node.put("P_endtime", P_endtime);
		node.put("P_hours", P_hours);
		
		node.put("month_hours", month_hours);
		node.put("limit_hours", limit_hours);
		node.put("overtime_way", overtime_way);
		dt11.put(node);
		details.put("DT1", dt11);
		return json.toString();
	}

	/**
	 * 
	 * @param creater
	 * @param datainfo {"endtime":"请假结束时间","remain_hours":"假期余额","reason":"请假事由","begintime":"请假开始时间","hours":"请假时数","absence_type":"请假类别","agent":"代理人工号","used_hours":"已用时数","name":"姓名","work_code":"工号","begindate":"请假开始日期","enddate":"请假结束日期","place":"出差地点","on_hours":"在途时数"}
	 * @throws Exception 
	 */
	public String getHR010Json(String creater,String datainfo) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		ResourceComInfo rci = new ResourceComInfo();
		String nowdate = sf.format(new Date());
		String name = "";//姓名
		String work_code = "";//工号
		String absence_type = "";//请假类别
		String begindate = "";//请假开始日期
		String begintime = "";//请假开始时间
		String enddate = "";//请假结束日期
		String endtime = "";//请假结束时间
		String hours = "";//请假时数
		String agent = "";//代理人工号
		String used_hours = "";//已用时数
		String remain_hours = "";//假期余额
		String on_hours = "";//在途时数
		String place = "";//出差地点
		String reason = "";//请假事由
		JSONObject jo = new JSONObject(datainfo);
		name = jo.getString("name");
		work_code = jo.getString("work_code");
		absence_type = jo.getString("absence_type");
		begindate = jo.getString("begindate");
		begintime = jo.getString("begintime");
		enddate = jo.getString("enddate");
		endtime = jo.getString("endtime");
		hours = jo.getString("hours");
		agent = jo.getString("agent");
		used_hours = jo.getString("used_hours");
		remain_hours = jo.getString("remain_hours");
		on_hours = jo.getString("on_hours");
		place = jo.getString("place");
		reason = jo.getString("reason");
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		json.put("HEADER", header);
		json.put("DETAILS", details);
		
		header.put("writer", creater);
		header.put("apply_name", creater);
		header.put("apply_company", rci.getSubCompanyID(creater));
		header.put("apply_code", work_code);
		header.put("apply_depart", rci.getDepartmentID(creater));
		header.put("apply_postion", rci.getJobTitle(creater));
		header.put("reason", reason);
		header.put("agent", getPersonID(agent));
		header.put("apply_date", nowdate);	
			
		JSONArray dt11 = new JSONArray();
		JSONObject node = new JSONObject();
		node.put("work_code", work_code);
		node.put("name", creater);
		node.put("begindate", begindate);
		node.put("begintime", begintime);
		node.put("enddate", enddate);
		node.put("endtime", endtime);
		node.put("hours", hours);		
		node.put("used_hours", used_hours);
		node.put("remain_hours", remain_hours);
		node.put("on_hours", on_hours);
		node.put("place", place);
		node.put("absence_type", absence_type);
		
		dt11.put(node);
		details.put("DT1", dt11);
		return json.toString();
	}
	
	/**
	 * 
	 * @param creater
	 * @param datainfo
	 * @throws Exception 
	 */
	public String getHR023Json(String creater,String datainfo) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		ResourceComInfo rci = new ResourceComInfo();
		String nowdate = sf.format(new Date());
		String name = "";//姓名
		String work_code = "";//工号
		String begindate = "";//开始日期
		String enddate = "";//结束日期
		String reason = "";//原因
		JSONObject jo = new JSONObject(datainfo);
		name = jo.getString("name");
		work_code = jo.getString("work_code");
		begindate = jo.getString("begindate");
		enddate = jo.getString("enddate");
		reason = jo.getString("reason");
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		json.put("HEADER", header);
		json.put("DETAILS", details);
		
		header.put("apply_name", creater);
		header.put("apply_date", nowdate);	
		header.put("apply_code", work_code);
		header.put("apply_company", rci.getSubCompanyID(creater));
		header.put("apply_depart", rci.getDepartmentID(creater));
		header.put("reason", reason);
		JSONArray dt11 = new JSONArray();
		JSONObject node = new JSONObject();
		node.put("work_code", work_code);
		node.put("name", creater);
		node.put("begindate", begindate);
		node.put("enddate", enddate);
		node.put("department", rci.getDepartmentID(creater));	
		
		dt11.put(node);
		details.put("DT1", dt11);
		return json.toString();
	}
	
	public String getOldRequestInfo(String workflowType,String workcode,int num,String checkno) throws Exception{
		RecordSet rs = new RecordSet();
		String result = "";
		String creater = "";
		String sql = "";
		String workflowid = "";
		String tablename ="";
		int count = 0;
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		if("".equals(workflowType)||"".equals(workcode)||"".equals(checkno)){
			json.put("result", "E");
			json.put("items", ja);
			json.put("context","接口参数不能为空");
			return json.toString();
		}
		sql="select wfid from uf_weixinkq_wfmt where code='"+workflowType+"'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("wfid"));
		}
		if("".equals(workflowid)){
			json.put("result", "E");
			json.put("items", ja);
			json.put("context","流程类型无法匹配");
			return json.toString();
		}
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename"));
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			creater = Util.null2String(rs.getString("id"));
		}
		if("".equals(creater)){
			json.put("result", "E");
			json.put("items", ja);
			json.put("context","人员编号无法匹配");
			return json.toString();
		}
		sql="select count(1) as count from uf_kq_person_record where workcode='"+workcode+"' and checkno='"+checkno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count <=0){
			json.put("result", "E");
			json.put("items", ja);
			json.put("context","该用户登录校验不正确");
			return json.toString();
		}
		String jsonstr = "";
		if("HR-037".equals(workflowType)){
			jsonstr=getOldRequestHR037(tablename, workflowid, creater, num);
		}else if("HR-040".equals(workflowType)){
			jsonstr=getOldRequestHR040(tablename, workflowid, creater, num);
		}else if("HR-041".equals(workflowType)){
			jsonstr=getOldRequestHR041(tablename, workflowid, creater, num);
		}else if("HR-042".equals(workflowType)){
			jsonstr=getOldRequestHR042(tablename, workflowid, creater, num);
		}
		return jsonstr;
	}
	/**
	 * 
	 * @param tablename 表名
	 * @param workflowid 流程id
	 * @param creater 创建人
	 * @param num 次数
	 * @return {"result":"S","items":[{"card_place":"昆山","status":"审批中","Nocard_type":"补下班卡","Break_date":"2018-06-06","repaircard_time":"18:00","Nocard_Reason":"忘打卡"},{"card_place":"昆山","status":"审批中","Nocard_type":"补上班卡","Break_date":"2018-06-06","repaircard_time":"08:00","Nocard_Reason":"忘打卡"}],"context":""}
	 * @throws Exception {"card_place":"地点","status":"审批中","Nocard_type":"未打卡类型","Break_date":"违反考勤日期","repaircard_time":"补卡时间","Nocard_Reason":"未打卡原因"}
	 */
	public String getOldRequestHR037(String tablename,String workflowid,String creater,int num) throws Exception{
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		String sql="";
		int startnum=0;
		int endnum=0;
		if(num==0){
			startnum =0;
			endnum=10;
		}else{
			startnum =10*(num-1);
			endnum=10*num;
		}
		 sql="select * from (select a.*,rownum as num from "+
			 " (select a.requestid,a.currentnodetype,c.Nocard_type,c.Nocard_Reason,c.Break_date,c.repaircard_time,c.card_place "+
			 " from workflow_requestbase a,"+tablename+" b,"+tablename+"_dt1 c "+
			 " where a.requestid=b.requestid and b.id=c.mainid "+
			 " and a.workflowid='"+workflowid+"' "+
			 " and c.name='"+creater+"' "+
			 " order by requestid desc) a) where num>"+startnum+" and num<="+endnum; 
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("Nocard_type", getSelectValueDetail(tablename, tablename+"_dt1", "Nocard_type", Util.null2String(rs.getString("Nocard_type"))));
			jo.put("Nocard_Reason",getSelectValueDetail(tablename, tablename+"_dt1", "Nocard_Reason", Util.null2String(rs.getString("Nocard_Reason"))));
			jo.put("Break_date", Util.null2String(rs.getString("Break_date")));
			jo.put("repaircard_time", Util.null2String(rs.getString("repaircard_time")));
			jo.put("card_place", getSelectValueDetail(tablename, tablename+"_dt1", "card_place", Util.null2String(rs.getString("card_place"))));
			
			if(rs.getInt("currentnodetype")>=3){
				jo.put("status", "归档");
			}else{
				jo.put("status", "审批中");
			}
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context","");
		
		return json.toString();		
		
	}
	
	/**
	 * 
	 * @param tablename 表名
	 * @param workflowid 流程id
	 * @param creater 创建人
	 * @param num 次数
	 * @return {"result":"S","items":[{"endtime":"13:00","apply_data":"2018-06-06","status":"审批中","cday":"2018-06-06","overtime_way":"正常加班","begintime":"11:00","hours":"2","overtime_type":"平时加班转调休","enddate":"2018-06-06","begindate":"2018-06-06"},{"endtime":"18:40","apply_data":"2018-06-05","status":"审批中","cday":"2018-06-05","overtime_way":"正常加班","begintime":"18:00","hours":"2","overtime_type":"法定节假日转支付","enddate":"2018-06-05","begindate":"2018-06-05"}],"context":""}
	 * @throws Exception {"endtime":"结束时间","apply_data":"申请日期","status":"审批中","cday":"归属日期","overtime_way":"加班方式","begintime":"开始时间","hours":"时长","overtime_type":"加班日期","enddate":"结束日期","begindate":"开始日期"}
	 */
	public String getOldRequestHR040(String tablename,String workflowid,String creater,int num) throws Exception{
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		String sql="";
		int startnum=0;
		int endnum=0;
		if(num==0){
			startnum =0;
			endnum=10;
		}else{
			startnum =10*(num-1);
			endnum=10*num;
		}
		 sql="select * from (select a.*,rownum as num from (select * from ( "+
				" select a.requestid,b.apply_data,c.overtime_way,c.cday,c.overtime_type,c.P_hours as hours ,c.p_begindate as begindate,c.p_begintime as begintime,c.p_enddate as enddate,c.p_endtime as endtime,a.currentnodetype "+ 
				" from  workflow_requestbase a,"+tablename+" b,"+tablename+"_dt1 c "+  
				" where a.requestid=b.requestid "+ 
				"  and b.id=c.mainid "+ 
				"  and a.workflowid='"+workflowid+"' "+ 
				"  and c.name='"+creater+"' "+ 
				"  and (c.a_begindate is  null or c.a_begintime is null or c.a_enddate is null or c.a_endtime is null) "+ 
				" union all "+ 
				" select a.requestid,b.apply_data,c.overtime_way,c.cday,c.overtime_type,c.a_hours as hours ,c.a_begindate as begindate,c.a_begintime as begintime,c.a_enddate as enddate,c.a_endtime as endtime,a.currentnodetype "+ 
				" from  workflow_requestbase a,"+tablename+" b,"+tablename+"_dt1 c  "+ 
				" where a.requestid=b.requestid "+ 
				"  and b.id=c.mainid "+ 
				"  and a.workflowid='"+workflowid+"' "+ 
				"  and c.name='"+creater+"' "+ 
				"  and (c.a_begindate is not null and c.a_begintime is not null and c.a_enddate is not null and c.a_endtime is not null) "+ 
				" )  order by requestid desc) a) where num>"+startnum+" and num<="+endnum; 
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("apply_data", Util.null2String(rs.getString("apply_data")));
			jo.put("overtime_way", getSelectValueDetail(tablename, tablename+"_dt1", "overtime_way", Util.null2String(rs.getString("overtime_way"))));
			jo.put("cday", Util.null2String(rs.getString("cday")));
			jo.put("overtime_type", Util.null2String(rs.getString("overtime_type")));
			jo.put("hours", Util.null2String(rs.getString("hours")));
			jo.put("begindate", Util.null2String(rs.getString("begindate")));
			jo.put("begintime", Util.null2String(rs.getString("begintime")));
			jo.put("enddate", Util.null2String(rs.getString("enddate")));
			jo.put("endtime", Util.null2String(rs.getString("endtime")));
			if(rs.getInt("currentnodetype")>=3){
				jo.put("status", "归档");
			}else{
				jo.put("status", "审批中");
			}
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context","");
		return json.toString();
	}
	/**
	 * 
	 * @param tablename 表名
	 * @param workflowid 流程id
	 * @param creater 创建人
	 * @param num 次数
	 * @return {"result":"S","items":[{"endtime":"17:30","apply_data":"","status":"审批中","hours":"8","begintime":"08:30","absence_type":"公假","enddate":"2019-06-06","begindate":"2019-06-06"},{"endtime":"17:30","apply_data":"","status":"审批中","hours":"8","begintime":"08:30","absence_type":"公假","enddate":"2019-06-06","begindate":"2019-06-06"}],"context":""}
	 * @throws Exception {"endtime":"请假结束时间","apply_data":"申请日期","status":"审批中","hours":"请假时长","begintime":"开始时间","absence_type":"请假类别","enddate":"结束日期","begindate":"开始日期"}
	 */
	public String getOldRequestHR041(String tablename,String workflowid,String creater,int num) throws Exception{
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		String sql="";
		int startnum=0;
		int endnum=0;
		if(num==0){
			startnum =0;
			endnum=10;
		}else{
			startnum =10*(num-1);
			endnum=10*num;
		}
		 sql="select * from (select a.*,rownum as num from (select a.requestid,a.currentnodetype,b.apply_date,(select absence_name  from uf_absence_type where absence_code=c.absence_type) as absence_type,c.begindate,c.begintime,c.enddate,c.endtime,c.hours "+
			" from workflow_requestbase a,"+tablename+" b,"+tablename+"_dt1 c "+
			" where a.requestid=b.requestid and b.id=c.mainid "+
			" and a.workflowid='"+workflowid+"' "+
			" and c.name='"+creater+"' "+
			" order by requestid desc) a) where num>"+startnum+" and num<="+endnum; 
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("apply_data", Util.null2String(rs.getString("apply_data")));
			jo.put("absence_type",Util.null2String(rs.getString("absence_type")));
			jo.put("begindate", Util.null2String(rs.getString("begindate")));
			jo.put("begintime", Util.null2String(rs.getString("begintime")));
			jo.put("enddate", Util.null2String(rs.getString("enddate")));
			jo.put("endtime", Util.null2String(rs.getString("endtime")));
			jo.put("hours", Util.null2String(rs.getString("hours")));
			if(rs.getInt("currentnodetype")>=3){
				jo.put("status", "归档");
			}else{
				jo.put("status", "审批中");
			}
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context","");
		return json.toString();
	}
	/**
	 * 
	 * @param tablename 表名
	 * @param workflowid 流程id
	 * @param creater 创建人
	 * @param num 次数
	 * @return {"result":"S","items":[{"status":"审批中","apply_date":"2018-06-05","enddate":"2018-06-06","begindate":"2018-06-06"}],"context":""}
	 * @throws Exception {"status":"审批中","apply_date":"申请日期","enddate":"销假结束日期","begindate":"销假开始日期"}
	 */
	public String getOldRequestHR042(String tablename,String workflowid,String creater,int num) throws Exception{
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		String sql="";
		int startnum=0;
		int endnum=0;
		if(num==0){
			startnum =0;
			endnum=10;
		}else{
			startnum =10*(num-1);
			endnum=10*num;
		}
		 sql="select * from (select a.*,rownum as num from "+
			 " (select a.requestid,a.currentnodetype,b.apply_date,c.begindate,c.enddate "+
			 " from workflow_requestbase a,"+tablename+" b,"+tablename+"_dt1 c "+
			 " where a.requestid=b.requestid and b.id=c.mainid "+
			 " and a.workflowid='"+workflowid+"' "+
			 " and c.name='"+creater+"' "+
			 " order by requestid desc) a) where num>"+startnum+" and num<="+endnum; 
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject jo = new JSONObject();
			jo.put("apply_date", Util.null2String(rs.getString("apply_date")));
			jo.put("begindate", Util.null2String(rs.getString("begindate")));
			jo.put("enddate", Util.null2String(rs.getString("enddate")));
						
			if(rs.getInt("currentnodetype")>=3){
				jo.put("status", "归档");
			}else{
				jo.put("status", "审批中");
			}
			ja.put(jo);
		}
		json.put("result", "S");
		json.put("items", ja);
		json.put("context","");
		
		return json.toString();		
		
	}
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
	
	public String getSelectValueDetail(String mainTable,String detailTable,String filedname,String selectvalue){
		RecordSet rs = new RecordSet();
		String value = "";
		String sql="select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"+mainTable+"' and a.fieldname='"+filedname+"' and c.selectvalue='"+selectvalue+"'"+
				" and a.detailtable ='"+detailTable+"'";
		rs.executeSql(sql);
		if(rs.next()){
			value = Util.null2String(rs.getString("selectname"));
		}
		return value;
	}
	public String getSelectValue(String mainTable,String filedname,String selectvalue){
		RecordSet rs = new RecordSet();
		String value = "";
		String sql="select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"+mainTable+"' and a.fieldname='"+filedname+"' and c.selectvalue='"+selectvalue+"'"+
				" and a.detailtable is null";
		rs.executeSql(sql);
		if(rs.next()){
			value = Util.null2String(rs.getString("selectname"));
		}
		return value;
	}
	public String getPersonID(String workcode){
		RecordSet rs = new RecordSet();
		String personid = "";
		String sql = "select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			personid = Util.null2String(rs.getString("id"));
		}
		return personid;
	}
	
	public String getOvertimeHours(String date,String workcode,String checkno ){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("kronos");
		String sql = "";
		String sqr = "";
		String workflowid = "";
		String tablename = "";
		String hours ="0";
		int count = 0;
		Map<String, String> retMap = new HashMap<String, String>();
		if("".equals(date)||"".equals(workcode)||"".equals(checkno)){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "接口参数不能为空");
			return getJsonStr(retMap);
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			sqr = Util.null2String(rs.getString("id"));
		}
		if("".equals(sqr)){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "人员编号无法匹配");
			return getJsonStr(retMap);
		}
		sql="select count(1) as count from uf_kq_person_record where workcode='"+workcode+"' and checkno='"+checkno+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count <=0){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "该用户登录校验不正确");
			return getJsonStr(retMap);
		}
		
		sql="select wfid from uf_weixinkq_wfmt where code='HR-040'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("wfid"));
		}
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename"));
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
		String month = "";
		try {
			month=sf.format(sf.parse(date));
		} catch (ParseException e) {
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "日期参数格式异常");
			return getJsonStr(retMap);
		}
		String startdate = "";
		String enddate="";
		sql="select to_char(trunc(to_date('"+date+"','yyyy-mm-dd'),'month'),'yyyy-mm-dd') as startdate,to_char(add_months(trunc(to_date('"+date+"','yyyy-mm-dd'),'month'),1) -1,'yyyy-mm-dd') as enddate from dual";
		rs.executeSql(sql);
		if(rs.next()){
			startdate = Util.null2String(rs.getString("startdate"));
			enddate = Util.null2String(rs.getString("enddate"));
		}
		if("".equals(startdate)||"".equals(enddate)){
			retMap.put("result", "E");
			retMap.put("time", "0");
			retMap.put("context", "日期参数格式异常");
			return getJsonStr(retMap);
		}
		sql="select nvl(sum(nvl(case when b.a_hours is null then b.p_hours else b.a_hours end,0)),0) as hour  from "+tablename+" a ,"+tablename+"_dt1 b ,workflow_requestbase c "+
			" where a.id=b.mainid and a.requestid=c.requestid and c.currentnodetype<3 and b.name="+sqr+" and c.workflowid='"+workflowid+"'"+
			" and to_char(to_date(b.cday,'yyyy-mm-dd'),'yyyy-mm')='"+month+"'";
		rs.executeSql(sql);
		if(rs.next()){
			hours = Util.null2String(rs.getString("hour"));
		}
		
		sql="select "+hours+"+Cux_getOTHours.Fn_getothours('"+workcode+"','"+enddate+"') as hour from dual";
		rsd.executeSql(sql);
		if(rsd.next()){
			hours = Util.null2String(rsd.getString("hour"));
		}
		if("".equals(hours)){
			hours = "0";
		}
		retMap.put("result", "S");
		retMap.put("time",hours);
		retMap.put("context", "");
		return getJsonStr(retMap);
	}
}
