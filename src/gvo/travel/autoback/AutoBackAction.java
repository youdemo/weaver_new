package gvo.travel.autoback;

import gvo.travel.TravelXmlUtil;
import gvo.util.xml.SaxXmlUtil;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AutoBackAction implements Action {
	BaseBean log = new BaseBean();

	/**
	 * oa与ec出差申请（含借款）流程对接
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-20
	 **/
	@Override
	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程AutoSubmitAction————————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String sql = "";
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		TravelXmlUtil tran = new TravelXmlUtil();
		String json = tran.javaToXml("", "", requestid, "");
		log.writeLog("requestid=" + requestid);
		log.writeLog("开始————————" + json);
		AutoBackService back = new AutoBackService();
		String sign = "";
		String MESSAGE = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = back.getResultMethod(json);
			sign = map.get("sign");
			MESSAGE = map.get("MESSAGE");
			log.writeLog("sign=" + sign);
			log.writeLog("message=" + MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String para = "MSG_CONTENT";
		String message = saxXmlUtil.getResult(para, MESSAGE);
		String sql_update = "update " + tableName + " set sign='" + sign
				+ "',message='" + message + "' where requestid=" + requestid;
		res.execute(sql_update);
		log.writeLog("错误信息2————————" + sql_update);

		return SUCCESS;
	}

}
