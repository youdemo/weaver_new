package gvo.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;

public class CreateRequestServiceECImpl {
	public String doservice(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
		String workflowId = "143";
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
			json = getjson(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "1");
		return result;

	}

	public String doserviceBrrow(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
		String workflowId = "44";
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
			json = getjsonBrrow(sqr, sqrbm, jsonStr);
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

	public String getjsonBrrow(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		// String reqman="";
		RecordSet rs = new RecordSet();
		String reqman = "";// 经办人员
		String reqdate = "";// 申请日期
		String amtPsn = "";// 借款人
		String amtDept = "";// 所属部门
		String reqsub = "";// 所在公司
		String sfscflc = "";// 是否是触发流程
		String oarqid = "";// oarqid
		String eeramt = "";//申请金额
		String cneeramt = "";//申请金额大写
		// 明细
		String applyamt = "";// 申请金额

		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		reqman = getId(head.getString("txr"), "1");
		reqdate = head.getString("sqrq");
		amtPsn = getId(head.getString("sqr"), "1");
		amtDept = getId(head.getString("szbm"), "2");
		reqsub = getId(head.getString("szgs"), "3");
		eeramt=head.getString("jkje");
		if (!"".equals(reqsub)) {
			String sql = "select supsubcomid from hrmsubcompany where id="
					+ reqsub;
			rs.executeSql(sql);
			if (rs.next()) {
				reqsub = Util.null2String(rs.getString("supsubcomid"));
			}

		}
		sfscflc = "0";

		oarqid = head.getString("oarqid");
		header.put("reqman", reqman);
		header.put("reqdate", reqdate);
		header.put("amtPsn", amtPsn);
		header.put("amtDept", amtDept);
		header.put("reqsub", reqsub);
		header.put("sfscflc", sfscflc);
		header.put("oarqid", oarqid);
		header.put("eeramt", eeramt);
		header.put("cneeramt", eeramt);

		JSONArray dt11 = new JSONArray();

		JSONObject node = new JSONObject();
		applyamt = head.getString("jkje");

		node.put("applyamt", applyamt);

		dt11.put(node);

		details.put("DT1", dt11);
		return json.toString();
	}

	public String doserviceECV0006(String workcode, String jsonStr) {
		RecordSet rs = new RecordSet();
		String sqr = "";
		String sqrbm = "";
		String workflowId = "644";
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
			json = getjsonECV0006(sqr, sqrbm, jsonStr);
		} catch (Exception e) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json格式不正确");
			retMap.put("OA_ID", "0");

			return getJsonStr(retMap);
		}
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowId, json, sqr, "1");
		return result;

	}

	public String getjsonECV0006(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		// String reqman="";
		RecordSet rs = new RecordSet();
		String reqman = "";// 经办人员
		String mnyBearPsn = "";// 费用承担人
		String mnyBearDept = "";// 费用承担部门
		String reqdate = "";// 申请日期
		String reqsub = "";// 所在公司
		String eeramt = "";// 申请金额
		String cneeramt = "";// 申请金额
		String oarqid2 = "";// oarqid2
		// 明细
		String budgetmonth = "";// 费用所属期间
		String budgettype = "";// 预算类型
		String costcenterid = "";// 费用承担
		String budgetaccountid = "";// 预算科目
		String eeramt_dt = "";// 预算申请金额
		String jtsy = "";// 具体事由
		String ndkyje = "";// 年度可用金额
		String oamxid = "";//

		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		reqdate = head.getString("sqrq");
		mnyBearPsn = getId(head.getString("sqr"), "1");
		mnyBearDept = getId(head.getString("fycdbm"), "2");
		reqsub = getId(head.getString("szgs"), "3");
		if (!"".equals(reqsub)) {
			String sql = "select supsubcomid from hrmsubcompany where id="
					+ reqsub;
			rs.executeSql(sql);
			if (rs.next()) {
				reqsub = Util.null2String(rs.getString("supsubcomid"));
			}

		}
		eeramt = head.getString("fyys");
		cneeramt = head.getString("fyys");
		oarqid2 = head.getString("oarqid");
		header.put("reqman", sqr);
		header.put("reqdate", reqdate);
		header.put("mnyBearPsn", mnyBearPsn);
		header.put("mnyBearDept", mnyBearDept);
		header.put("reqsub", reqsub);
		header.put("eeramt", eeramt);
		header.put("cneeramt", cneeramt);
		header.put("oarqid2", oarqid2);
		JSONObject dts = jo.getJSONObject("DETAILS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			budgetmonth = arr.getString("fyssqj");
			budgettype = "3";
			costcenterid = arr.getString("fycd");
			budgetaccountid = arr.getString("yskm");
			eeramt_dt = arr.getString("yssqje");
			jtsy = arr.getString("jtsy");
			ndkyje = arr.getString("ndkyys");
			oamxid = arr.getString("oamxid");
			node.put("budgetmonth", budgetmonth);
			node.put("budgettype", budgettype);
			node.put("costcenterid", costcenterid);
			node.put("budgetaccountid", budgetaccountid);
			node.put("eeramt", eeramt_dt);
			node.put("jtsy", jtsy);
			node.put("ndkyje", ndkyje);
			node.put("oamxid", oamxid);
			dt11.put(node);
		}
		details.put("DT1", dt11);
		return json.toString();
	}

	public String getId(String code, String type) {
		RecordSet rs = new RecordSet();
		String id = "";
		String sql = "";
		if ("".equals(code)) {
			return "";
		}
		if ("1".equals(type)) {
			sql = "select id  from hrmresource where workcode='" + code + "'";
		} else if ("2".equals(type)) {
			sql = "select id  from hrmdepartment where departmentcode='" + code
					+ "'";
		} else {
			sql = "select id  from hrmsubcompany where subcompanycode='" + code
					+ "'";
		}
		rs.executeSql(sql);
		if (rs.next()) {
			id = Util.null2String(rs.getString("id"));
		}
		return id;
	}

	public String getjson(String sqr, String sqrbm, String jsonStr)
			throws Exception {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String EBELN = "";// 采购订单号
		String BEDAT = "";// 凭证日期
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String EKORG = "";// 采购组织
		String EKOTX = "";// 采购组织（描述）
		String EKGRP = "";// 采购组
		String EKNAM = "";// 采购组（描述）
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String BRTWR = "";// 订单总金额
		// 明细1
		String EBELP = "";// 项目
		String MATNR = "";// 物料
		String TXZ01 = "";// 短文本
		String MENGE = "";// 采购订单数量
		String MEINS = "";// 订单单位
		String EINDT = "";// 交货日期
		String NETPR = "";// 净价
		String KBETR = "";// 含税价
		String WAERS = "";// 货币
		String MWSKZ = "";// 税码
		String PEINH = "";// 每
		String BPRME = "";// 价格单位
		String NAME1_dt1 = "";// 工厂
		String LGOBE = "";// 库存地点
		String WGBEZ = "";// 物料组
		String AFNAM = "";// 申请者
		String INFNR = "";// 采购信息记录
		String BANFN = "";// 采购申请
		String BNFPO = "";// 请求项目
		String KNTTP = "";// 科目分配类别
		String PSTYP = "";// 项目类别
		String SAKTO = "";// 总账科目
		String KOSTL = "";// 成本中心
		String AUFNR = "";// 订单
		String ANLN1 = "";// 固定资产编号
		String ANLN2 = "";// 次级编号
		String TEXT1 = "";// 税码描述
		String TXT20 = "";// 总账科目描述
		String LTEXT = "";// 成本中心描述
		String KTEXT = "";// 订单描述
		String TXT50 = "";// 固定资产描述
		String LOEKZ = "";// 删除标识
		String BRTWR_dt = "";// 行项目金额
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		// System.out.println(head.toString());
		EBELN = head.getString("EBELN");
		BEDAT = head.getString("BEDAT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		EKORG = head.getString("EKORG");
		EKOTX = head.getString("EKOTX");
		EKGRP = head.getString("EKGRP");
		EKNAM = head.getString("EKNAM");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		BRTWR = head.getString("BRTWR");
		header.put("Applicant", sqr);
		header.put("dept", sqrbm);
		header.put("appDate", now);
		header.put("orderNo", EBELN);
		header.put("certifidate", BEDAT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("PurchaseOrg", EKORG);
		header.put("PurOrgDesc", EKOTX);
		header.put("PurchaseGro", EKGRP);
		header.put("PurGroDesc", EKNAM);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("brtwr", BRTWR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);
			node.put("costcenter", KOSTL);
			node.put("orders", AUFNR);
			node.put("assetNum", ANLN1);
			node.put("subNum", ANLN2);
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);
			node.put("KTEXT", KTEXT);
			node.put("TXT50", TXT50);
			node.put("loekz", LOEKZ);
			node.put("brtwr", BRTWR_dt);
			dt11.put(node);
		}
		details.put("DT1", dt11);

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

	public String AutoSubmitV0006(String requestid, String userid) {
		WorkflowServiceImpl ws = new WorkflowServiceImpl();
		WorkflowRequestInfo wri = new WorkflowRequestInfo();
		String result = ws.submitWorkflowRequest(wri,Integer.valueOf(requestid), Integer.valueOf(userid), "submit","自动提交");
		return result;
	}
	
	public String AutoBackV0006(String requestid,String userid){
		  WorkflowServiceImpl ws = new WorkflowServiceImpl();
		  WorkflowRequestInfo wri = new WorkflowRequestInfo();
		  String result=ws.submitWorkflowRequest(wri, Integer.valueOf(requestid), Integer.valueOf(userid), "reject", "自动退回");   
		  return result;
	  }
}
