package gvo.costcontrol.cptreim;


import java.text.SimpleDateFormat;
import java.util.Date;

import gvo.costcontrol.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.cptreim.HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.cptreim.HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub.HNYG060_SAPFI_FI_0_POSTAP2Service;
import gvo.wsclient.costcontrol.cptreim.HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CptReimAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("进入固定资产CptReimAction————————");
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		String sql = "";
		RecordSet rs = new RecordSet();
		String tableName = "";// 表名
		String mainID = "";// 主表ID
		String sfpz = "";//是否抛转
		String gzrq = "";//过账日期
		String bz = "";//结算币别
		String gsdm = "";//公司代码
		String gysdm = "";//供应商代码
		String txrq = "";//填写日期
		String syjs = "";//事由简述
		String bxdh = "";//报销单号
		String sqr = "";//实际付款人
		String zzbs = "";//总账标识
		String txr = "";//填写人
		String fykmbm = "";//明细1费用科目编码
		String zzch = "";//明细1 主资产号
		String bcbxje1 = "";//明细1 本次报销金额
		String fphm = "";//明细1 发票号码
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
			sfpz = Util.null2String(rs.getString("sfscpz"));
			bz = Util.null2String(rs.getString("bz"));
			syjs = Util.null2String(rs.getString("syjs"));
			gsdm = Util.null2String(rs.getString("gsdm"));
			gysdm = Util.null2String(rs.getString("gysdm"));
			txrq = Util.null2String(rs.getString("txrq"));
			bxdh = Util.null2String(rs.getString("bxdh"));
			sqr = Util.null2String(rs.getString("sqr"));
			zzbs = Util.null2String(rs.getString("zzbs"));
			txr = Util.null2String(rs.getString("txr"));
		}
		try {
			sqr = new ResourceComInfo().getLastname(sqr);
			txr = new ResourceComInfo().getLastname(txr);
		} catch (Exception e) {
			log.writeLog(e);
			sqr = "";
			txr = "";
		}
		sql="select bzdm from formtable_main_33 where id="+bz;
		rs.executeSql(sql);
		if(rs.next()){
			bz = Util.null2String(rs.getString("bzdm"));
		}
		if("0".equals(sfpz)){
			sfpz = "X";
		}else{
			sfpz = "N";
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			fykmbm = Util.null2String(rs.getString("fykmbm"));
			zzch = Util.null2String(rs.getString("zzch"));
			bcbxje1 = Util.null2String(rs.getString("bcbxje1"));
			fphm = Util.null2String(rs.getString("fphm"));
			JSONObject jo = new JSONObject();
			try {
				jo.put("I_BUZEI", "");
				jo.put("I_BSCHL", "");
				jo.put("I_HKONT",fykmbm);
				jo.put("I_KOSTL", "");
				jo.put("I_AUFNR", "");
				jo.put("I_WRBTR", bcbxje1);
				jo.put("I_ZUONR",fphm);
				jo.put("I_SGTXT", syjs);
				jo.put("I_PERSON", txr);
				jo.put("I_ANLN1", zzch);
				jo.put("I_ANLN2", "0000");
				array1.put(jo);
			} catch (JSONException e) {
				log.writeLog("array1异常");
				log.writeLog(e);
			}
		}
		
		try {
			head.put("I_BUKRS",gsdm);
			head.put("I_LIFNR",gysdm);
			head.put("I_BUDAT",txrq);
			head.put("I_BLDAT",txrq);
			head.put("I_WAERS",bz);
			head.put("I_BKTXT",syjs);
			head.put("I_XNLNR",bxdh);
			head.put("I_TAX",sfpz);
			head.put("I_PERSON",sqr);
			head.put("I_UMSKZ",zzbs);
			head.put("T_ITEM", array1);
			head.put("T_ITEM2", array2);
		} catch (JSONException e) {
			log.writeLog("head异常");
			log.writeLog(e);
		}
		array.put(head);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		PurXmlUtil tran = new PurXmlUtil();
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		Head head1 = new Head("SAP.FI_NNYG-060_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		String json = tran.javaToXml(array.toString(), "", requestid, "",head1);
		log.writeLog("json:"+json);
		Response result = null;
		try {
			result = getServiceResult(json);
		} catch (Exception e) {
			log.writeLog(e);
			log.writeLog("接口调用失败");
		}
		String E_MEG = "";
		String E_BELNR = "";
		if(result != null){
			String sign = result.getSIGN();
			String message = result.getMessage();
			E_MEG = saxXmlUtil.getResult("E_MEG", message); 
			E_BELNR = saxXmlUtil.getResult("E_BELNR", message);//凭证编号
			sql="update "+tableName+" set sap_bz='"+sign+"',sap_xxms='"+E_MEG+"',sap_pzh='"+E_BELNR+"' where requestid="+requestid;
			log.writeLog("sql:"+sql);
			rs.executeSql(sql);
		}
		return SUCCESS;
	}
	
	public Response getServiceResult(String json) throws Exception{
		HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub hf = new HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub();
		HNYG060_SAPFI_FI_0_POSTAP2Service hfs=new HNYG060_SAPFI_FI_0_POSTAP2Service_pttBindingQSServiceStub.HNYG060_SAPFI_FI_0_POSTAP2Service();
		hfs.setData(json);
		return hf.HNYG060_SAPFI_FI_0_POSTAP2Service(hfs);
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
