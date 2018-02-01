package gvo.travel.autosubmit;

import gvo.travel.TravelXmlUtil;
import gvo.util.xml.SaxXmlUtil;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AutoSubmitAction implements Action {
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
		String mainID = "";
		String ecrqid2 = "";
		
		RecordSet rs = new RecordSet();
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,ecrqid2 from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			ecrqid2 = Util.null2String(rs.getString("ecrqid2"));
			
		}
		TravelXmlUtil tran = new TravelXmlUtil();
		String json = tran.javaToXml("", "", requestid, "");
		log.writeLog("requestid=" + requestid);
		log.writeLog("开始————————json" + json);
		AutoSubmitService auto = new AutoSubmitService();
		String sign = "";
		String MESSAGE = "";
		log.writeLog("json——————————" + json);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = auto.getResultMethod(json);
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
		String sql_update = "update " + tableName + " set sign='" + sign + "',message='" + message + "' where requestid=" + requestid;
		rs.execute(sql_update);
		log.writeLog("错误信息2————————" + sql_update);

		return SUCCESS;
	}

}
