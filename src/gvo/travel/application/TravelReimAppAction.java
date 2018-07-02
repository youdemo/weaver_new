package gvo.travel.application;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;
import gvo.travel.TravelXmlUtil;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class TravelReimAppAction implements Action {
	/**
	 * oa与ec出差申请流程
	 * 
	 * @author adore
	 * @version 1.0 2017-11-07
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程TravelReimAppAction——————");
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "";
		String tableName = "";
		String tableNamedt = "";// 明细表
		String mainID = "";
		String txr = "";// 填写人
		String sqrq = "";// 填写日期
		String sqr = "";// 申请人
		String szgs = "";// 所在公司
		String fycdbm = "";// 费用承担部门
		String fyys = "";// 费用预算

		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		if (!"".equals(tableName)) {

			tableNamedt = tableName + "_dt2";
			// 查询主表
			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			JSONObject head = new JSONObject();
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("id"));
				txr = getcode(Util.null2String(rs.getString("txr")),"1");
				sqrq =  Util.null2String(rs.getString("sqrq"));
				sqr = getcode(Util.null2String(rs.getString("sqr")),"1");
				szgs = getcode(Util.null2String(rs.getString("szgs")),"3");
				fycdbm = getcode(Util.null2String(rs.getString("fycdbm")),"2");
				fyys  = Util.null2String(rs.getString("fyys"));
			}
			try {
				head.put("oarqid", requestid);
				head.put("txr", txr);
				head.put("sqrq", sqrq);
				head.put("sqr", sqr);
				head.put("szgs", szgs);
				head.put("fycdbm", fycdbm);
				head.put("fyys", fyys);

			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			JSONArray jsonArray = new JSONArray();
			JSONObject json1 = new JSONObject();
			JSONObject DT = new JSONObject();
			// 查询明细表
			sql = "select * from " + tableNamedt + " where mainid=" + mainID;
			res.execute(sql);
//			log.writeLog("错误信息————————" + sql);
			while (res.next()) {
				JSONObject jsonObjSon = new JSONObject();
				// 明细2
				String fyssqj = Util.null2String(res.getString("fyssqj"));// 费用所属期间
				String fycd = Util.null2String(res.getString("fycd"));// 费用承担
				String yskm = Util.null2String(res.getString("yskm"));// 预算科目
				String ydkyys = Util.null2String(res.getString("ydkyys"));// 月度可用预算
				String ndkyys = Util.null2String(res.getString("ndkyys"));// 年度可用预算
				String jtsy = Util.null2String(res.getString("jtsy"));// 具体事由
				String yssqje = Util.null2String(res.getString("yssqje"));// 预算申请金额
				String oamxid = Util.null2String(res.getString("id"));
//				log.writeLog("开始————————yssqje = " + yssqje);
				try {
					jsonObjSon.put("fyssqj", fyssqj);
					jsonObjSon.put("fycd", fycd);
					jsonObjSon.put("yskm", yskm);
					jsonObjSon.put("ydkyys", ydkyys);
					jsonObjSon.put("ndkyys", ndkyys);
					jsonObjSon.put("jtsy", jtsy);
					jsonObjSon.put("yssqje", yssqje);
					jsonObjSon.put("oamxid", oamxid);
					jsonArray.put(jsonObjSon);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				try {
					DT.put("DT1", jsonArray);
					json1.put("DETAILS", DT);
					json1.put("HEADER", head);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			TravelXmlUtil tran = new TravelXmlUtil();
			String json = tran.javaToXml(json1.toString(), txr, requestid, "");
			log.writeLog("打印json————————" + json);
			TravelReimWebService tral = new TravelReimWebService();
			String SIGN = "";
			String MESSAGE = "";
			try {
			 	Response result = tral.getResultMethod(json);
			 	SIGN = result.getSIGN();
			 	MESSAGE = result.getMessage();
			} catch (Exception e) {
				log.writeLog("错误日志----" + e.getMessage());
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "MSG_CONTENT";
			String para1 = "OA_ID";
			String message = saxXmlUtil.getResult(para, MESSAGE);
			String oa_id = saxXmlUtil.getResult(para1, MESSAGE);
			String sql_update = "update " + tableName + " set　ecrqid2='" + oa_id + "' where requestid=" + requestid;
			res.execute(sql_update);
//			log.writeLog("更新语句————————" + sql_update);
		} else {
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
