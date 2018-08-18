package gvo.costcontrol.purchase.orderedit;

import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.purchase.orderstatus.SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.purchase.orderstatus.SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.Response;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ChangePoStatusRejectAction implements Action {
	/**
	 * 采购订单修改更新审批状态不通过
	 * 
	 * @author tangjianyong
	 * @version 1.0 2018-06-26
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购订单状态更新ChangePoStatusRejectAction————————————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String sql = "";
		String ORDERNO = "";// 采购订单号
		String FRGCO = "10";// 审批代码
		String ZRESET = "X";// 取消审批
		RecordSet rs = new RecordSet();
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		// 查询主表
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		JSONObject head = new JSONObject();
		if (rs.next()) {
			ORDERNO = Util.null2String(rs.getString("orderno"));
		}
		try {
			head.put("REQUESTID", requestid);
			head.put("ORDERNO", ORDERNO);
			head.put("FRGCO", FRGCO);
			head.put("ZRESET", ZRESET);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		JSONArray jsonArray = new JSONArray();

		jsonArray.put(head);
		PurXmlUtilOld px = new PurXmlUtilOld();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		Head head1 = new Head("SAP.MM_HNYG-007_" + time, "1", "OA", "1",
				"userSAP", "P@ss0rd", "", "");
		String json = px.javaToXml(jsonArray.toString(), "", requestid, "",
				head1);
		log.writeLog("打印json——————————" + json);
		String sign = "";
		String message = "";
		Response result = null;
		try {
			result = getResultMethod(json);
			sign = result.getSIGN();
			message = result.getMessage();
			log.writeLog("sign = " + sign);
			log.writeLog("message = " + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String E_MSGTX = "";// 消息文本
		E_MSGTX = saxXmlUtil.getResult("E_MSGTX", message);
		String sql_update = "update " + tableName + " set estatus1='" + sign
				+ "',emeeage='" + E_MSGTX + "' where requestid=" + requestid;
		rs.execute(sql_update);
		log.writeLog("更新语句————————" + sql_update);

		return SUCCESS;
	}

	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub crs = new SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub();
		SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.SAPPR_MM_0_ChangePRStateService cres = new SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.SAPPR_MM_0_ChangePRStateService();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_ChangePRStateService(cres);
		return result;
	}
}
