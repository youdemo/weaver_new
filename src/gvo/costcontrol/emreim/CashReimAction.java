package gvo.costcontrol.emreim;

import gvo.wsclient.costcontrol.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.Response;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 现金购报销流程
 * @author tangj
 *
 */
public class CashReimAction implements Action {
	
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("进入现金购费用报销流程CashReimAction————————");
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		String sql = "";
		RecordSet rs = new RecordSet();
		String tableName = "";// 表名
		String mainID = "";// 主表ID
		String CORPCODE = "";// 公司代码
		String REQCODE = "";// 报销人编码
		String CURRTYPE_DES = "";// 币别
		String FYBXSM = "";// 费用报销说明
		String FLOWNO = "";// 单据编号
		String REQNAME = "";// 经办人姓名
		String cbzxbm = "";
		String yfxmbh = "";//研发项目编号
		String VOUCHERTYPE = "KR";// 凭证类型
		String VOUCHERSPUN = "X";// 是否抛转

		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		JSONArray array1 = new JSONArray();
		JSONArray array = new JSONArray();

		JSONArray array2 = new JSONArray();
		JSONObject head = new JSONObject();
		// 查询主表
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			CORPCODE = Util.null2String(rs.getString("gsdm"));
			try {
				REQNAME = new ResourceComInfo().getLastname(Util.null2String(rs
						.getString("fycdr")));
			} catch (Exception e) {
			}
			CURRTYPE_DES = getSelectValue(tableName, "currtype",
					Util.null2String(rs.getString("currtype")));
			FYBXSM = Util.null2String(rs.getString("sy"));
			FLOWNO = Util.null2String(rs.getString("flowno"));
			cbzxbm = Util.null2String(rs.getString("cbzxbm"));
			yfxmbh = Util.null2String(rs.getString("yfxmbh"));
			VOUCHERSPUN = Util.null2String(rs.getString("sfpz"));

		}
		if("0".equals(VOUCHERSPUN)){
			VOUCHERSPUN = "X";
		}else{
			VOUCHERSPUN = "N";
		}
		sql="select * from " + tableName + "_dt3 where mainid=" + mainID;
		rs.executeSql(sql);
		if(rs.next()){
			REQCODE = Util.null2String(rs.getString("hrmcode"));
		}
		// 查询明细表1
		sql = "select * from " + tableName + "_dt1 where mainid= " + mainID;
		rs.execute(sql);
		while (rs.next()) {
			JSONObject json1 = new JSONObject();
			String ACCOUNTCODE = "40";// 记账代码
			String FYKMBM = Util.null2String(rs.getString("fykmbm"));// 费用科目编码
			String EXPENSEAMT = Util.null2String(rs.getString("expenseamt"));// 费用未税金额
			String TAX = Util.null2String(rs.getString("zpse"));// 税额
			String fphm = Util.null2String(rs.getString("fphm"));// 发票号码
			try {
				json1.put("ACCOUNTCODE", ACCOUNTCODE);
				json1.put("FYKMBM", FYKMBM);
				json1.put("CBZXBM", cbzxbm);
				json1.put("EXPENSEAMT", EXPENSEAMT);
				json1.put("TAX", TAX);
				json1.put("FYBXSM", FYBXSM);
				json1.put("REQNAME", REQNAME);
				json1.put("NBDDH", yfxmbh);
				json1.put("LWFLOWNO", fphm);
				array1.put(json1);
			} catch (JSONException e) {
				log.writeLog("array1异常");
				log.writeLog(e);
			}
		}
		// 查询明细表2
		sql = "select * from " + tableName + "_dt2 where mainid=" + mainID;
		rs.execute(sql);
		while (rs.next()) {
			JSONObject json2 = new JSONObject();
			String ACCOUNTCODE = "39";// 记账代码
			String GENERALACCOUNT = "";// 特殊总帐标识
			String PAYAMT = Util.null2String(rs.getString("payamt"));// 本次冲销金额
			String LWFLOWNO = Util.null2String(rs.getString("lwflowno"));// 借款单号
			String jklx =  Util.null2String(rs.getString("jklx"));
			if("0".equals(jklx)){
				GENERALACCOUNT = "H";
			}else{
				GENERALACCOUNT = "M";
			}
			try {
				json2.put("ACCOUNTCODE", ACCOUNTCODE);
				json2.put("REQCODE", REQCODE);
				json2.put("GENERALACCOUNT", GENERALACCOUNT);
				json2.put("PAYAMT", PAYAMT);
				json2.put("LWFLOWNO", LWFLOWNO);
				json2.put("FYBXSM", FYBXSM);
				array2.put(json2);
			} catch (JSONException e) {
				log.writeLog("array2异常");
				log.writeLog(e);
			}
		}
		try {
			head.put("CORPCODE", CORPCODE);
			head.put("REQCODE", REQCODE);
			head.put("CURRTYPE_DES", CURRTYPE_DES);
			head.put("FYBXSM", FYBXSM);
			head.put("FLOWNO", FLOWNO);
			head.put("REQNAME", REQNAME);
			head.put("VOUCHERTYPE", VOUCHERTYPE);
			head.put("VOUCHERSPUN", VOUCHERSPUN);
			head.put("CHILD_LIST_TB2", array1);
			head.put("CHILD_LIST_TB1", array2);
		} catch (JSONException e) {
			log.writeLog("head异常");
			log.writeLog(e);
		}
		array.put(head);
		PurXmlUtilOld px = new PurXmlUtilOld();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(new Date());
		Head head1 = new Head("SAP.FI_HNYG-011_" + time, "200", "OA", "1",
				"userSAP", "P@ss0rd", "", "");
		String json = px.javaToXml(array.toString(), "", requestid, "", head1);
		log.writeLog("查看json格式————————" + json);
		String sign = "";
		String message = "";
		Response result = null;
		try {
			result = getResultMethod(json);
		} catch (Exception e) {
			log.writeLog("错误日志----" + e.getMessage());
			e.printStackTrace();
		}
		sign = result.getSIGN();
		message = result.getMessage();
		// log.writeLog("返回结果sign————————" + sign);
		// log.writeLog("返回结果message————————" + message);

		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String E_BELNR = "";// 会计凭证编号
		String E_MSGTX = "";// 消息文本
		E_BELNR = saxXmlUtil.getResult("E_BELNR", message);
		E_MSGTX = saxXmlUtil.getResult("E_MSGTX", message);
		sql = "update " + tableName + " set saphxzt='" + sign + "',saphxcwxx='"
				+ E_MSGTX + "',saphxpzh='" + E_BELNR + "' where requestid="
				+ requestid;
		log.writeLog("更新语句————————" + sql);
		rs.execute(sql);
		// log.writeLog("更新语句————————" + sql_update);

		return SUCCESS;
	}

	public Response getResultMethod(String json) throws Exception {
		SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub sem = new SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub();
		SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.SAPHR_FI_0_EmCostReimbursementService sems = new SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.SAPHR_FI_0_EmCostReimbursementService();
		sems.setData(json);
		Response result = sem.SAPHR_FI_0_EmCostReimbursementService(sems);
		return result;
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
