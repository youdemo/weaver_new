package gvo.costcontrol;

import java.text.SimpleDateFormat;
import java.util.Date;

import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub.HNYG057_SAPFI_FI_0_CreateIntorder;
import gvo.wsclient.costcontrol.HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub.Response;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PurchaseAddForGDZC implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		BaseBean log = new BaseBean();
		String tableName = "";
		String mainId = "";
		String gsdm = "";//公司代码
		String gcdm = "";//工厂代码
		String cbzx="";//成本中心
		String zcmc="";//资产名称
		String nbddh="";
		String dtId="";
		String datainfo="";
		String sql_dt="";
		String flag = "S";
		log.writeLog("start PurchaseAddForGDZC");
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			gsdm = Util.null2String(rs.getString("gsdm"));
			gcdm = Util.null2String(rs.getString("gcdm"));
			cbzx = Util.null2String(rs.getString("cbzx"));
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			zcmc = Util.null2String(rs.getString("zcmc"));
			dtId = Util.null2String(rs.getString("id"));
			nbddh = Util.null2String(rs.getString("nbddh"));
			if(!"".equals(nbddh)){
				continue;
			}
			datainfo="{\"I_BUKRS\":\""+gsdm+"\",\"I_CO_AREA\":\"1000\",\"I_CURRENCY\":\"CNY\",\"I_ORDER_NAME\":\""+zcmc+"\",\"I_ORDER_TYPE\":\"Z003\",\"I_PLANT\":\""+gcdm+"\",\"I_RESPCCTR\":\""+cbzx+"\"}";
			PurXmlUtil tran = new PurXmlUtil();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdf.format(new Date());
			Head head = new Head("SAP.FI_NNYG-057_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
			String json = tran.javaToXml(datainfo, "", requestid, "",head);
			log.writeLog("json:"+json);
			Response result;
			String E_ORDER="";
			String E_MSG = "";
			try {
				result = doEsb(json);
				String sign = result.getSIGN();
				if(!"S".equals(sign)){
					flag = "E";
				}
				String message = result.getMessage();
				log.writeLog("sign:"+sign+" message:"+message);
				E_ORDER = saxXmlUtil.getResult("E_ORDER", message);
				E_MSG = saxXmlUtil.getResult("E_MSG", message);
				sql_dt="update "+tableName+"_dt1 set nbddh='"+E_ORDER+"',flag='"+sign+"',message='"+E_MSG+"' where id="+dtId;
				rs_dt.executeSql(sql_dt);
			} catch (Exception e) {
				log.writeLog("调用接口失败");
				log.writeLog(e);
			}
		}
		sql="update "+tableName+" set flag='"+flag+"' where requestid="+requestid;
		rs.executeSql(sql);
		return SUCCESS;
	}

	
	public static Response doEsb(String json) throws Exception{
		HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub hny = new HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub();
		HNYG057_SAPFI_FI_0_CreateIntorder hsf = new HNYG057_SAPFI_FI_0_CreateIntorder_pttBindingQSServiceStub.HNYG057_SAPFI_FI_0_CreateIntorder();
		hsf.setData(json);
		Response result= hny.HNYG057_SAPFI_FI_0_CreateIntorder(hsf);
		return result;
	}
	
	
}
