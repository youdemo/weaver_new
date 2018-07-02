package gvo.costcontrol.personrepay;


import java.text.SimpleDateFormat;
import java.util.Date;

import gvo.costcontrol.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.personrepay.VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub.Response;
import gvo.wsclient.costcontrol.personrepay.VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.personrepay.VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub.VXG061_SAPFI_FI_0_FundDocmentPostService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PersonReplaymentAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("进入个人还款PersonReplaymentAction————————");
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String sql = "";
		RecordSet rs = new RecordSet();
		String tableName = "";// 表名
		String mainID = "";// 主表ID
		String sfpz = "";//是否抛转
		String gzrq = "";//过账日期
		String currtype = "";//结算币别
		String gsdm = "";//公司代码
		String remark = "";//还款说明 明细1
		String flowno = "";//单据编号
		String gh = "";
		String repayamt = "";//明细1还款金额
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		JSONArray array = new JSONArray();
		JSONObject head = new JSONObject();
		JSONArray array1 = new JSONArray();
		JSONArray array2 = new JSONArray();
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			sfpz = Util.null2String(rs.getString("sfpz"));
			gzrq = Util.null2String(rs.getString("gzrq"));
			currtype = getSelectValue(tableName, "currtype",
					Util.null2String(rs.getString("currtype")));
			gsdm = Util.null2String(rs.getString("gsdm"));
			flowno = Util.null2String(rs.getString("flowno"));
			gh = Util.null2String(rs.getString("gh"));
		}
		if("0".equals(sfpz)){
			sfpz = "X";
		}else{
			sfpz = "N";
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			repayamt = Util.null2String(rs.getString("repayamt"));
			remark = Util.null2String(rs.getString("remark"));
			JSONObject jo = new JSONObject();
			try {
				jo.put("BSCHL", "40");
				jo.put("HKONT", "1002010321");
				jo.put("LIFNR", "");
				jo.put("KUNNR", "");
				jo.put("UMSKZ", "");
				jo.put("WRBTR", repayamt);
				jo.put("EBELN", "");
				jo.put("EBELP", "");
				jo.put("KOSTL", "");
				jo.put("AUFNR", "");
				jo.put("ZUONR", flowno);
				jo.put("SGTXT", remark);
				jo.put("ZZPERSON", "");
				jo.put("RSTGR", "205");
				jo.put("ZFBDT", "");
				array1.put(jo);
			} catch (JSONException e) {
				log.writeLog("array1异常");
				log.writeLog(e);
			}
		}
		sql="select * from "+tableName+"_dt2 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			String jklx =  Util.null2String(rs.getString("jklx"));
			if("0".equals(jklx)){
				jklx = "H";
			}else{
				jklx = "M";
			}
			remark = Util.null2String(rs.getString("remark"));
			JSONObject jo = new JSONObject();
			try {
				jo.put("BSCHL", "39");
				jo.put("HKONT", "");
				jo.put("LIFNR", gh);
				jo.put("KUNNR", "");
				jo.put("UMSKZ", jklx);
				jo.put("WRBTR", repayamt);
				jo.put("EBELN", "");
				jo.put("EBELP", "");
				jo.put("KOSTL", "");
				jo.put("AUFNR", "");
				jo.put("ZUONR", flowno);
				jo.put("SGTXT", remark);
				jo.put("ZZPERSON", "");
				jo.put("RSTGR", "");
				jo.put("ZFBDT", "");
				array2.put(jo);
			} catch (JSONException e) {
				log.writeLog("array2异常");
				log.writeLog(e);
			}
		}
		try {
			head.put("ZFLAG",sfpz);
			head.put("BUKRS",gsdm);
			head.put("BLART","SA");
			head.put("BLDAT",gzrq);
			head.put("BUDAT",gzrq);
			head.put("MONAT",remark);
			head.put("WAERS",currtype);
			head.put("BKTXT",remark);
			head.put("XBLNR_ALT",flowno);
			head.put("T_ITEMS", array1);
			head.put("T_ITEMH", array2);
		} catch (JSONException e) {
			log.writeLog("head异常");
			log.writeLog(e);
		}
		array.put(head);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		PurXmlUtil tran = new PurXmlUtil();
		Head head1 = new Head("SAP.FI_VXG-061_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		String json = tran.javaToXml(array.toString(), "", requestid, "",head1);
		log.writeLog("json:"+json);
		Response result = null;
		try {
			result = getServiceResult(json);
		} catch (Exception e) {
			log.writeLog(e);
			log.writeLog("接口调用失败");
		}
		String E_MESS = "";
		String E_BELNR = "";
		if(result != null){
			String sign = result.getSIGN();
			String message = result.getMessage();
			E_MESS = saxXmlUtil.getResult("E_MESS", message); 
			E_BELNR = saxXmlUtil.getResult("E_BELNR", message);//凭证编号
			sql="update "+tableName+" set saphxzt='"+sign+"',saphxcwxx='"+E_MESS+"',saphxpzh='"+E_BELNR+"' where requestid="+requestid;
			log.writeLog("sql:"+sql);
			rs.executeSql(sql);
		}
		return SUCCESS;
	}
	
	public Response getServiceResult(String json) throws Exception{
		VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub hf = new VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub();
		VXG061_SAPFI_FI_0_FundDocmentPostService hfs=new VXG061_SAPFI_FI_0_FundDocmentPostService_pttBindingQSServiceStub.VXG061_SAPFI_FI_0_FundDocmentPostService();
		hfs.setData(json);
		return hf.VXG061_SAPFI_FI_0_FundDocmentPostService(hfs);
	}
	public String getSelectValue(String mainTable, String filedname,
			String selectvalue) {
		RecordSet rs = new RecordSet();
		String value = "";
		String sql = "select c.selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"
				+ mainTable
				+ "' and a.fieldname='"
				+ filedname
				+ "' and c.selectvalue='"
				+ selectvalue
				+ "'"
				+ " and a.detailtable is null";
		rs.executeSql(sql);
		if (rs.next()) {
			value = Util.null2String(rs.getString("selectname"));
		}
		return value;
	}
}
