package gvo.costcontrol.personborrow;


import java.text.SimpleDateFormat;
import java.util.Date;

import gvo.costcontrol.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.personborrow.HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.personborrow.HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub.HNYG059_SAPFI_FI_0_AdvanceService;
import gvo.wsclient.costcontrol.personborrow.HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub.Response;

import javax.swing.text.TabExpander;

import org.apache.axis2.AxisFault;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.Signal;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PersonBorrowAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("进入多成本中心分摊流程MultCenterReimAction————————");
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String sql = "";
		RecordSet rs = new RecordSet();
		String tableName = "";// 表名
		String reqdate = "";//申请日期
		String gsdm = "";//公司代码
		String currtype = "";//币别
		String remark = "";//事由
		String eeramt ="";//借款金额
		String jkry = "";//借款人
		String loantype = "";//借款类型
		String qwfkrq = "";//期望付款日期
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		JSONArray array = new JSONArray();
		JSONObject head = new JSONObject();
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			reqdate = Util.null2String(rs.getString("reqdate"));
			gsdm = Util.null2String(rs.getString("gsdm"));
			currtype = Util.null2String(rs.getString("currtype"));
			remark = Util.null2String(rs.getString("remark"));
			eeramt = Util.null2String(rs.getString("eeramt"));
			jkry = Util.null2String(rs.getString("jkry"));
			loantype = Util.null2String(rs.getString("loantype"));
			qwfkrq = Util.null2String(rs.getString("qwfkrq"));
		}
		try {
			head.put("I_BUDAT",reqdate);//凭证中的过帐日期   oa 日期
			head.put("I_BUKRS",gsdm);//公司代码   oa 公司代码
			head.put("I_WAERS",getSelectValue(tableName,"currtype",currtype));//货币码   oa 货币码
			head.put("I_SGTXT",remark);//项目文本   oa 借款事由
			head.put("I_WRBTR",eeramt);//凭证货币金额   oa 借款金额
			head.put("I_LIFNR",new ResourceComInfo().getWorkcode(jkry));//供应商或债权人的帐号   oa 工号
			head.put("I_BLDAT",reqdate);//凭证中的凭证日期   oa 日期
			head.put("I_ZLSCH","E");//付款方式   oa 	付款方式
			if("0".equals(loantype)){
				loantype="H";
			}else if("1".equals(loantype)){
				loantype="M";
			}
			head.put("I_DZUMSK",loantype);//目标特别总帐标志   oa 借款类型
			head.put("I_ZFBDT",qwfkrq);// 	期望付款日期
			head.put("I_EBELN", "");
			head.put("I_EBELP", "");
			head.put("I_ZUONR", "");
		} catch (Exception e) {
			log.writeLog(e);
			log.writeLog("拼装json异常");
		}
		array.put(head);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		PurXmlUtil tran = new PurXmlUtil();
		Head head1 = new Head("SAP.FI_NNYG-059_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		String json = tran.javaToXml(array.toString(), "", requestid, "",head1);
		log.writeLog("json:"+json);
		Response result=null;
		try {
			result = getServiceResult(json);
		} catch (Exception e) {
			log.writeLog("调用接口失败");
			log.writeLog(e);
		}
		String E_CJAHR = "";
		String E_MSG = "";
		String E_BELNR = "";
		if(result != null){
			String sign = result.getSIGN();
			String message = result.getMessage();
			E_CJAHR = saxXmlUtil.getResult("E_CJAHR", message);//会计年度
			E_MSG = saxXmlUtil.getResult("E_MSG", message);
			E_BELNR = saxXmlUtil.getResult("E_BELNR", message);//凭证编号
			sql="update "+tableName+" set ztbz='"+sign+"',xxms='"+E_MSG+"',cwpzh='"+E_BELNR+"',nd='"+E_CJAHR+"' where requestid="+requestid;
			log.writeLog("sql:"+sql);
			rs.executeSql(sql);
		}
		
		
		return SUCCESS;
	}
	
	public Response getServiceResult(String json) throws Exception{
		HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub hf = new HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub();
		HNYG059_SAPFI_FI_0_AdvanceService hfs=new HNYG059_SAPFI_FI_0_AdvanceService_pttBindingQSServiceStub.HNYG059_SAPFI_FI_0_AdvanceService();
		hfs.setData(json);
		return hf.HNYG059_SAPFI_FI_0_AdvanceService(hfs);
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
