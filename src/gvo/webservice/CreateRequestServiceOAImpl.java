package gvo.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CreateRequestServiceOAImpl {
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
