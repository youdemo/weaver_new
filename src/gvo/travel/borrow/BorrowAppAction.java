package gvo.travel.borrow;

import gvo.travel.TravelXmlUtil;
import gvo.util.xml.SaxXmlUtil;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class BorrowAppAction implements Action {
	/**
	 * oa与ec出差申请（含借款）流程对接
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-17
	 **/
	BaseBean log = new BaseBean();

	@Override
	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程BorrowAppAction————————");
		String tableName = "";
		String sql = "";
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String mainID = "";
		String workcode = "";// 工号
		String txr = "";// 填写人
		String sqrq = "";// 填写日期
		String sqr = "";// 申请人
		String szgs = "";// 所在公司
		String szbm = "";// 所在部门
		String jkje = "";// 借款金额
		String sfxyjk = "";// 是否需要借款
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id,txr,sqrq,sqr,szgs,szbm,jkje,sfxyjk from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			txr = Util.null2String(rs.getString("txr"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			sqr = Util.null2String(rs.getString("sqr"));
			szgs = Util.null2String(rs.getString("szgs"));
			szbm = Util.null2String(rs.getString("szbm"));
			jkje = Util.null2String(rs.getString("jkje"));
			sfxyjk = Util.null2String(rs.getString("sfxyjk"));
			mainID = Util.null2String(rs.getString("ID"));
			workcode = Util.null2String(rs.getString("gh"));

		}
		if ("1".equals(sfxyjk) || "".equals(sfxyjk)) {
			return SUCCESS;
		}
		JSONObject head = new JSONObject();
		try {
			head.put("txr", txr);
			head.put("sqrq", sqrq);
			head.put("sqr", sqr);
			head.put("szgs", szgs);
			head.put("szbm", szbm);
			head.put("jkje", jkje);
			head.put("oarqid", requestid);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		JSONObject jsonobj = new JSONObject();
		JSONArray Array = new JSONArray();
		JSONObject details = new JSONObject();
		try {
			jsonobj.put("HEADER", head);
			jsonobj.put("DETAILS", details);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		Array.put(jsonobj);
		TravelXmlUtil tran = new TravelXmlUtil();
		String json = tran.javaToXml(Array.toString(), workcode, "", "");
		log.writeLog("requestid=" + requestid);
		log.writeLog("开始————————json" + json);
		BorrowWebService borrow = new BorrowWebService();
		String sign = "";
		String MESSAGE = "";
		log.writeLog("json——————————" + json);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = borrow.getResultMethod(json);
			sign = map.get("sign");
			MESSAGE = map.get("MESSAGE");
			log.writeLog("sign=" + sign);
			log.writeLog("message=" + MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String para = "MSG_CONTENT";
		String para1 = "OA_ID";
		String message = saxXmlUtil.getResult(para, MESSAGE);
		String oa_id = saxXmlUtil.getResult(para1, MESSAGE);
		String sql_update = "update " + tableName + " set sign='" + sign
				+ "',message='" + message + "',jkrqid='" + oa_id
				+ "' where requestid=" + requestid;
		res.execute(sql_update);
		log.writeLog("错误信息2————————" + sql_update);

		return SUCCESS;
	}

}
