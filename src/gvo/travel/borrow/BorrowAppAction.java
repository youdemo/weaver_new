package gvo.travel.borrow;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;
import gvo.travel.TravelXmlUtil;
import gvo.util.xml.SaxXmlUtil;
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

	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程BorrowAppAction————————");
		String tableName = "";
		String sql = "";
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String mainID = "";
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
		if (!"".equals(tableName)) {
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			txr = getcode(Util.null2String(rs.getString("txr")),"1");
			sqrq =  Util.null2String(rs.getString("sqrq"));
			sqr = getcode(Util.null2String(rs.getString("sqr")),"1");
			szgs = getcode(Util.null2String(rs.getString("szgs")),"3");
			szbm = getcode(Util.null2String(rs.getString("szbm")),"2");
			jkje  = Util.null2String(rs.getString("jkje"));
			sfxyjk = Util.null2String(rs.getString("sfxyjk"));
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
		JSONObject details = new JSONObject();
		try {
			jsonobj.put("HEADER", head);
			jsonobj.put("DETAILS", details);
		} catch (JSONException e1) {
			log.writeLog("错误日志----" + e1.getMessage());
			e1.printStackTrace();
		}
		TravelXmlUtil tran = new TravelXmlUtil();
		String json = tran.javaToXml(jsonobj.toString(), txr, requestid, "");
//		log.writeLog("requestid=" + requestid);
		log.writeLog("打印json————————" + json);
		BorrowWebService borrow = new BorrowWebService();
		String sign = "";
		String MESSAGE = "";
		try {
			Response result = borrow.getResultMethod(json);
			sign = result.getSIGN();
		 	MESSAGE = result.getMessage();
//			log.writeLog("sign=" + sign);
//			log.writeLog("message=" + MESSAGE);
		} catch (Exception e) {
			log.writeLog("错误日志----" + e.getMessage());
			e.printStackTrace();
		}
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String para = "MSG_CONTENT";
		String para1 = "OA_ID";
		String message = saxXmlUtil.getResult(para, MESSAGE);
		String oa_id = saxXmlUtil.getResult(para1, MESSAGE);
		String sql_update = "update " + tableName + " set sign='" + sign + "',message='" + message + "',jkrqid='" + oa_id + "' where requestid=" + requestid;
		res.execute(sql_update);
//		log.writeLog("更新语句————————" + sql_update);
		} else {
//			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
	public String getcode(String id,String type){
		  RecordSet rs = new RecordSet();
		  String code="";
		  String sql="";
		  if("".equals(id)){
			  return "";
		  }
		  if("1".equals(type)){
		    sql="select workcode  as code from hrmresource where id="+id;
		  }else if("2".equals(type)){
			sql="select departmentcode as code from hrmdepartment where id="+id;	  
		  }else{
			  sql="select subcompanycode as code from hrmsubcompany where id="+id;	    
		  }
		  rs.executeSql(sql);
		  if(rs.next()){
			  code = Util.null2String(rs.getString("code"));
		  }
		  return code;
	  }
}
