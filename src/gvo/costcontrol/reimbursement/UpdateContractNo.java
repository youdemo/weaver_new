package gvo.costcontrol.reimbursement;

import gvo.costcontrol.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.reimbursement.VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.reimbursement.VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub.Response;
import gvo.wsclient.costcontrol.reimbursement.VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub.VXG064_SAPPR_MM_0_UPDATECONTRACTService;

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

public class UpdateContractNo implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String tableName = "";// 表名
		String fpyzh = "";//发票发票预制号
		String htbh = "";//合同编号
		String gxhtbhzt = "";
		String sql="";
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			fpyzh =Util.null2String(rs.getString("fpyzh"));
			htbh =Util.null2String(rs.getString("htbhnew"));
			gxhtbhzt =Util.null2String(rs.getString("gxhtbhzt"));
		}
		if("S".equals(gxhtbhzt)){
			return SUCCESS;
		}
		JSONArray array = new JSONArray();
		JSONObject head = new JSONObject();
		try {
			head.put("I_BELNR",fpyzh);
			head.put("I_LEGAL_CONTRACT",htbh);
		} catch (JSONException e) {
			log.writeLog("head异常");
			log.writeLog(e);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		PurXmlUtil tran = new PurXmlUtil();
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		Head head1 = new Head("SAP.MM_VXG-064_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		String json = tran.javaToXml(head.toString(), "", requestid, "",head1);
		log.writeLog("json:"+json);
		Response result = null;
		try {
			result = getServiceResult(json);
		} catch (Exception e) {
			log.writeLog(e);
			log.writeLog("接口调用失败");
		}
		if(result != null){
			String sign = result.getSIGN();
			String message = result.getMessage();
			String E_MESSAGE = "";// 消息文本
			E_MESSAGE = saxXmlUtil.getResult("E_MESSAGE", message);
			sql="update "+tableName+" set gxhtbhzt='"+sign+"',gxhtbhxx='"+E_MESSAGE+"'  where requestid="+requestid;
			log.writeLog("sql:"+sql);
			rs.executeSql(sql);
		}
		return SUCCESS;
	}
	public Response getServiceResult(String json) throws Exception{
		VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub hf = new VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub();
		VXG064_SAPPR_MM_0_UPDATECONTRACTService hfs=new VXG064_SAPPR_MM_0_UPDATECONTRACTService_pttBindingQSServiceStub.VXG064_SAPPR_MM_0_UPDATECONTRACTService();
		hfs.setData(json);
		return hf.VXG064_SAPPR_MM_0_UPDATECONTRACTService(hfs);
	}
	

}
