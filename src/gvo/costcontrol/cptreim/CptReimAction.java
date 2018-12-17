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
		String bcbxe = "";//明细1 本次报销金额原币
		String fphm = "";//明细1 发票号码
		
		String yflx = "";//预付类型
		String bccxyfkje = "";//本次冲销预付款金额
		String yfdh = "";//预付单号
		String sap_bz = "";
		String bhsje = "";//本次报销未税金额（CNY）
		String se = "";//本次报销未税金额（CNY）
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
			sap_bz = Util.null2String(rs.getString("sap_bz"));
		}
		if("S".equals(sap_bz)){
			return SUCCESS;
		}
		try {
			sqr = new ResourceComInfo().getLastname(sqr);
			txr = new ResourceComInfo().getLastname(txr);
		} catch (Exception e) {
			log.writeLog(e);
			sqr = "";
			txr = "";
		}
//		sql="select bzdm from formtable_main_33 where id="+bz;
//		rs.executeSql(sql);
//		if(rs.next()){
//			bz = Util.null2String(rs.getString("bzdm"));
//		}
		if("0".equals(sfpz)){
			sfpz = "Y";
		}else{
			sfpz = "X";
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			fykmbm = Util.null2String(rs.getString("fykmbm"));
			zzch = Util.null2String(rs.getString("zzch"));
			bcbxe = Util.null2String(rs.getString("bcbxe"));
			bhsje = Util.null2String(rs.getString("bhsje"));
			se = Util.null2String(rs.getString("se"));
			fphm = Util.null2String(rs.getString("fphm"));
			JSONObject jo = new JSONObject();
			try {
				jo.put("I_BUZEI", "");
				jo.put("I_BSCHL", "");
				jo.put("I_HKONT",fykmbm);
				jo.put("I_KOSTL", "");
				jo.put("I_AUFNR", "");
				jo.put("I_ZUONR",bxdh);
				jo.put("I_SGTXT", syjs);
				jo.put("I_PERSON", "");
				jo.put("I_ANLN1", zzch);
				jo.put("I_ANLN2", "0000");
				if(!"CNY".equals(bz)){
					jo.put("I_WRBTR", bcbxe);
					jo.put("I_TAXAMOUNT", "0");
				}else{
					jo.put("I_WRBTR", bhsje);
					jo.put("I_TAXAMOUNT", se);
				}
				array1.put(jo);
			} catch (JSONException e) {
				log.writeLog("array1异常");
				log.writeLog(e);
			}
		}
		sql="select * from "+tableName+"_dt4 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			yflx = Util.null2String(rs.getString("yflx"));
			bccxyfkje = Util.null2String(rs.getString("bccxyfkje"));
			yfdh = Util.null2String(rs.getString("yfdh"));
			JSONObject jo = new JSONObject();
			try {
				jo.put("BSCHL", "");
				jo.put("UMSKZ", yflx);
				jo.put("LIFNR","");
				jo.put("WRBTR", bccxyfkje);
				jo.put("ZUONR", yfdh);
				jo.put("SGTXT", syjs);
				array2.put(jo);
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
			head.put("I_PERSON","");//sqr
			head.put("I_UMSKZ",zzbs);//zzbs
			head.put("IT_ITEM", array1);
			head.put("IT_ITEM2", array2);
		} catch (JSONException e) {
			log.writeLog("head异常");
			log.writeLog(e);
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		PurXmlUtil tran = new PurXmlUtil();
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		Head head1 = new Head("SAP.FI_NNYG-060_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		String json = tran.javaToXml(head.toString(), "", requestid, "",head1);
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
			//log.writeLog("CptReimAction ddd:");
			String sign = result.getSIGN();
			//log.writeLog("CptReimAction dddaa sign"+sign);
			String message = result.getMessage();
			//log.writeLog("CptReimAction dddaa message"+message);
			E_MEG = saxXmlUtil.getResult("E_MSG", message); 
			//log.writeLog("CptReimAction ddd33"+E_MEG);
			E_BELNR = saxXmlUtil.getResult("E_BELNR", message);//凭证编号
			//log.writeLog("CptReimAction dddddd"+E_BELNR);
			sql="update "+tableName+" set sap_bz='"+sign+"',sap_xxms='"+E_MEG+"',sap_pzh='"+E_BELNR+"' where requestid="+requestid;
			//log.writeLog("CptReimActionfff:"+sql);
			rs.executeSql(sql);
		}else{
			log.writeLog("接口调用失败 result=null");
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
