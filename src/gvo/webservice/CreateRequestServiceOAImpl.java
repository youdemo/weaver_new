package gvo.webservice;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.*;

public class CreateRequestServiceOAImpl {
	BaseBean log = new BaseBean();
	public String doserviceSupplier(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
//		String workflowId = "9342";//正式机:8122
		String workflowId = "8122";
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ workcode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqr = Util.null2o(rs.getString("id"));
			sqrbm = Util.null2o(rs.getString("departmentid"));
		} else {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配！");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		String json = "";
		try {
			json = getjsonSupplier(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "0");
		return result;

	}

	public String doserviceMaterial(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
//		String workflowId = "9341";//正式机:8161
		String workflowId = "8161";
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ workcode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqr = Util.null2o(rs.getString("id"));
			sqrbm = Util.null2o(rs.getString("departmentid"));
		} else {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配！");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		String json = "";
		try {
			json = getjsonMaterial(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "0");
		return result;

	}
	public String doserviceHR015(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
		String workflowId = "8126";
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ workcode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqr = Util.null2o(rs.getString("id"));
			sqrbm = Util.null2o(rs.getString("departmentid"));
		} else {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配！");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		String json = "";
		try {
			json = getjsonHR015(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "0");
		return result;

	}

	public String getjsonMaterial(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		String BUKRS="";//公司代码
		String BUTXT="";//公司名称
		String LIFNR="";//供应商
		String NAME1="";//供应商名称
		String MATNR="";//物料编码
		String MAKTX="";//物料名称
		String ZRECOGNIZE = "";//是否承认

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		BUKRS = head.getString("BUKRS");
 		BUTXT = head.getString("BUTXT");
 		LIFNR = head.getString("LIFNR");
 		NAME1 = head.getString("NAME1");
 		MATNR = head.getString("MATNR");
 		MAKTX = head.getString("MAKTX");
 		ZRECOGNIZE = head.getString("ZRECOGNIZE");

		header.put("applicant", sqr);
		header.put("department", sqrbm);
		header.put("appDate", now);
		header.put("companyCode", BUKRS);
 		header.put("companyName", BUTXT);
 		header.put("suppliers", LIFNR);
 		header.put("supNames", NAME1);
 		header.put("sapMaterial", MATNR);
 		header.put("materName", MAKTX);
 		header.put("isSure", ZRECOGNIZE);

		return json.toString();

	}
	public String getjsonHR015(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		RecordSet rs = new RecordSet();
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		String reqman = "";//经办人员工号
		String mnyBearPsn = "";//费用承担人工号
		String reqdate = "";//申请日期
		String businessTripRange = "";//出差范围
		String mnyBearDept = "";//费用承担部门编号
		String reqsub = "";//所属分部编号
		String eeramt = "";//申请金额
        
        String txr=sqr;
        String sqrid="";
        String szbm="";
        String szgs="";
        String fycdbm="";
        String ecrqid = "";
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        reqman = head.getString("reqman");
        mnyBearPsn = head.getString("mnyBearPsn");
        reqdate = head.getString("reqdate");
        businessTripRange = head.getString("businessTripRange");
        mnyBearDept = head.getString("mnyBearDept");
        reqsub = head.getString("reqsub");
        eeramt = head.getString("eeramt");
        ecrqid = head.getString("ecrqid");
		 String sql = "select id,departmentid from hrmresource where workcode='"
				+ mnyBearPsn + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqrid = Util.null2o(rs.getString("id"));
			szbm = Util.null2o(rs.getString("departmentid"));
		}
		sql="select id from hrmsubcompany where subcompanycode="+reqsub;
		rs.executeSql(sql);
		if (rs.next()) {
			szgs = Util.null2o(rs.getString("id"));
		}
		sql="select id from hrmdepartment where departmentcode='"+mnyBearDept+"'";
		rs.executeSql(sql);
		if (rs.next()) {
			fycdbm = Util.null2o(rs.getString("id"));
		}
		json.put("HEADER", header);
		json.put("DETAILS", details);
		header.put("txr", txr);
		header.put("sqrq", reqdate);
		header.put("sqr", sqrid);
		header.put("gh", mnyBearPsn);
 		header.put("szbm", szbm);
 		header.put("fycdbm", fycdbm);
 		header.put("szgs", szgs);
 		header.put("fyys", eeramt);
 		header.put("fjlb", businessTripRange);
 		header.put("ecrqid", ecrqid);

		return json.toString();

	}

	public String getjsonSupplier(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String MATNR = "";// 物料编码
		String MAKTX = "";// 公司代码
		String ZQUALIFIED = "";//供应商状态

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		MATNR = head.getString("MATNR");
		MAKTX = head.getString("MAKTX");
		ZQUALIFIED = head.getString("ZQUALIFIED");

		header.put("applicant", sqr);
		header.put("dept", sqrbm);
		header.put("appDate", now);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("materialCode", MATNR);
		header.put("materialName", MAKTX);
		header.put("suplStatus", ZQUALIFIED);

		return json.toString();

	}

	public String doserviceEmploy(String jsonStr) throws JSONException {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
		String workcode = "";
//		String workflowId = "9341";//正式机:8161
		String workflowId = getWorkFlowID("3");
		String json = "";
		try {
			json = getjsonEmploy(jsonStr);
		} catch (Exception e) {
			log.writeLog("Exception info---------------->" + e.getMessage());
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		workcode = head.getString("workcode");
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ workcode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqr = Util.null2o(rs.getString("id"));
		} else {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配！");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "0");
		return result;

	}
	public String getjsonEmploy(String jsonStr)
			throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		String responsible="";//HR负责人
		String workcode = "";//申请人工号
		int datetime = 0;//系统时间
		String username = "";//应聘者姓名
		String architecture = "";//录用部门名称
		String corporation = "";//拟录用公司名称
		String city = "";//主要城市
		String position_level = "";//职等

		String position_code = "";//职位代码
		String position_name = "";//职位名称
		String channel = "";//发展通道
		String superior = "";//直接上级ID
		String tutor = "";//建议指导人ID
		String url = "";//面试官评价

		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		responsible = head.getString("responsible_user");
		datetime = head.getInt("auto_datetime");
		Calendar c=Calendar.getInstance();
		long millions=new Long(datetime).longValue()*1000;
		c.setTimeInMillis(millions);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = sdf.format(c.getTime());
		username = head.getString("username");
		city = head.getString("city_id");
		position_level = head.getString("position_level");
		position_code = head.getString("position_code");
		superior = head.getString("my_superior");
		tutor = head.getString("my_tutor");
		RecordSet rs = new RecordSet();
		String sql = "select id from hrmresource where email='"
				+ responsible + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			responsible = Util.null2o(rs.getString("id"));
		}
		sql = "select id from hrmresource where workcode='"
				+ superior + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			superior = Util.null2o(rs.getString("id"));
		}
		sql = "select id from hrmresource where workcode='"
				+ tutor + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			tutor = Util.null2o(rs.getString("id"));
		}
		//获取部门，公司，职位名称，通道
		sql = "select * from hrmjobtitles where jobtitlecode='"
				+ position_code + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			position_code = Util.null2o(rs.getString("id"));
			architecture = Util.null2o(rs.getString("jobdepartmentid"));
			position_name = Util.null2o(rs.getString("jobtitlename"));
		}
		sql = "select * from uf_hr_jobtitlecode where POSITIONNBR='"
				+ position_code + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			channel = Util.null2o(rs.getString("channel"));
		}
		sql = "select * from hrmdepartment where id='"
				+ architecture + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			corporation = Util.null2o(rs.getString("subcompanyid1"));
		}
		url = head.getString("interview_evaluation_url");
		header.put("apply_name", responsible);
		header.put("apply_date", dateString);
		header.put("name", username);
		header.put("emp_dept", architecture);
		header.put("company", corporation);
		header.put("work_place", city);
		header.put("grade", position_level);
		header.put("postion", position_code);
		header.put("position_name", position_name);
		header.put("develop", channel);
		header.put("superior", superior);
		header.put("zdr", tutor);
		header.put("msgpj", url);

		json.put("HEADER", header);
		json.put("DETAILS", details);
		log.writeLog("json-------->"+json.toString());
		return json.toString();

	}

	public String doserviceDocLimit(String jsonStr) throws JSONException {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
//		String workflowId = "9341";//正式机:8161
		String workflowId = getWorkFlowID("4");
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		String workcode = head.getString("UserCode");
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ workcode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			sqr = Util.null2o(rs.getString("id"));
			sqrbm = Util.null2o(rs.getString("departmentid"));
		} else {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配！");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		String json = "";
		try {
			json = getjsonDocLimit(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			log.writeLog("Exception info---------------->" + e.getMessage());
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "0");
		return result;

	}
	public String getjsonDocLimit(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		/**
		 * UserCode	字符	申请人工号	工号
		 * Path	字符	文件目录	路径
		 * Code	字符	唯一编号	流程审批完成后需回传此编号
		 * DataInfo	Json字符串	文件或文件夹数据信息	集合形式
		 * Type	字符	类型为文件或文件夹	类型为文件或文件夹
		 */
		/**
		 * UserCode	文本	工号	根据工号带出姓名
		 * wdml	文本	文档目录	路径
		 * Code	文本	DMC编号	流程审批完成后需回传此编号
		 * mxb1	明细表	明细表1	明细表
		 */
		/**
		 * Name	字符	名称	文件或文件夹名称	wjmc	字符	文件名称	文件或文件夹名称
		 * ID	字符	唯一ID	文件或文件夹ID（不用展示到OA）	ID	字符	唯一ID	文件或文件夹ID（不用展示到OA）
		 * CreateUser	字符	创建用户		CreateUser	字符	创建用户
		 * CreateDate	Date	创建时间		CreateDate	Date	创建时间
		 * SecurityLevel	字符	密级		SecurityLevel	字符	密级
		 * CreateCode	字符	创建用户工号		CreateCode	字符	创建用户工号
		 */

		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		String UserCode = "";//申请人工号
		String Path = "";//文件目录
		String Code = "";//唯一编号
		String Type = "";//类型为文件或文件夹

		String Name = "";
		String ID = "";
		String CreateUser = "";
		String CreateDate = "";
		String SecurityLevel = "";
		String CreateCode = "";
		String apply = "";
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		UserCode = head.getString("UserCode");
		Path = head.getString("Path");
		Code = head.getString("Code");

		RecordSet rs = new RecordSet();
		String sql = "select id,departmentid from hrmresource where workcode='"
				+ UserCode + "' and status in(0,1,2,3)";
		rs.executeSql(sql);
		if (rs.next()) {
			apply = Util.null2o(rs.getString("id"));
		}

		header.put("UserCode", UserCode);
		header.put("wdml", Path);
		header.put("sqr", apply);
		header.put("Code", Code);
//		header.put("Type", Type);
		header.put("sqrq", now);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			Name = arr.getString("Name");
			ID = arr.getString("ID");
			Type = arr.getString("Type");
			CreateUser = arr.getString("CreateUser");
//			sql = "select id from hrmresource where workcode='"
//					+ CreateCode + "' and status in(0,1,2,3)";
//			rs.executeSql(sql);
//			if (rs.next()) {
//				CreateUser = Util.null2o(rs.getString("id"));
//			}
			CreateDate = arr.getString("CreateDate");
			SecurityLevel = arr.getString("SecurityLevel");
			CreateCode = arr.getString("CreateCode");

			node.put("wdmc", Name);
			node.put("wyid", ID);
			node.put("cjyh", CreateUser);
			node.put("cjsj", CreateDate);
			node.put("mj", SecurityLevel);
			node.put("cjyhid", CreateCode);
			node.put("Type", Type);

			dt11.put(node);
		}
		details.put("DT1", dt11);

		json.put("HEADER", header);
		json.put("DETAILS", details);
		log.writeLog("json-------->"+json.toString());
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
	public String getWorkFlowID(String id){
		RecordSet rs = new RecordSet();
		String res = "";
		String sql = " select workflowID from uf_cflcpzb where flowtype='"+id+"' and status='0' ";
		rs.execute(sql);
		if(rs.next()){
			res = rs.getString("workflowID");
		}
		return res;
	}
}
