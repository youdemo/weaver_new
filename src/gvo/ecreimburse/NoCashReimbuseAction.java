package gvo.ecreimburse;

import gvo.ecpay.BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub.Response;
import gvo.ecpay.ECPayXmlUtil;
import gvo.ecpay.HnPayWebService;
import gvo.util.pay.TransformUtil;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class NoCashReimbuseAction implements Action {

	/**
	 * ec与资金对公支付流程
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-14
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入053对公支付流程NoCashReimbuseAction——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "";
		String tableName = "";
		String SERIAL_NO_ERP = "";// 流程编号
		String REQ_DATE = "";// 申请日期
		String CORP_CODE = "";// 公司代码
		String PAYER_ACC_NO = "";// 付款银行账号
		String CUR = "";// 结算币别
		String ITEM_CODE = "";// 资金计划科目编码
		String ZZBS = "";// 特殊总帐标识
		String RMK = "";// 流程编号
		String ABS = "";// 事由
		String AMT = "";// 本次付款额（RMB)
		String GYSDM = "";// 供应商编码
		String PAYEE_NAME = "";// 供应商
		String PAYEE_BANK = "";// 银行名称
		String PAYEE_ACC_NO = "";// 银行账号
		String PAYEE_CODE = "";// 联行号
		String FKYYDM = "";// 现金流量代码
		String PURPOSE = "";// 银行付款用途
		String URGENCY_FLAG = "";// 加急标志
		String ISFORINDIVIDUAL = "";// 对公对私标志
		String VOUCHER_TYPE = "";// 付款方式
		String WISH_PAY_DAY = "";// 申请日期
		String ZZKM = "";// 总账科目编码
		String JZDM = "40";// 记账代码
		String SYSTEM_TYPE = "0";// 固定值1
		String PAYTYPE = "";// 结算方式
		String sfpz = "";// 是否生成凭证
		String wllb = "";//物料类别
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		if (!"".equals(tableName)) {

			// 查询主表

			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			if (rs.next()) {
				SERIAL_NO_ERP = Util.null2String(rs.getString("requestid"));
				CORP_CODE = Util.null2String(rs.getString("gsdm"));
				RMK = Util.null2String(rs.getString("lcbh"));
				REQ_DATE = Util.null2String(rs.getString("sqrq"));
				PAYER_ACC_NO = Util.null2String(rs.getString("fkyhzh"));
				CUR = Util.null2String(rs.getString("currency"));
				ITEM_CODE = Util.null2String(rs.getString("kmbm"));
				ABS = Util.null2String(rs.getString("sy"));
				VOUCHER_TYPE = Util.null2String(rs.getString("fkfs"));
				PAYTYPE = Util.null2String(rs.getString("jsfs"));
				if (PAYTYPE.equals("0")) {
					ZZBS = Util.null2String(rs.getString("tszzbs"));
				} else {
					ZZBS = "";
				}
				TransformUtil tran = new TransformUtil();
				VOUCHER_TYPE = tran.getPaytype(VOUCHER_TYPE);

				WISH_PAY_DAY = Util.null2String(rs.getString("sqrq"));
//				JZDM = Util.null2String(rs.getString("jzdm"));
				AMT = Util.null2String(rs.getString("bcfke"));
				if("".equals(AMT)){
					AMT = "0";
				}
				GYSDM = Util.null2String(rs.getString("gysbm"));
				ABS = GYSDM + "/*@" + ABS;
				if (ABS.length() >= 50) {
					ABS = ABS.substring(0, 50);
				}
				PAYEE_NAME = Util.null2String(rs.getString("gys"));
				PAYEE_BANK = Util.null2String(rs.getString("yhmc"));
				PAYEE_ACC_NO = Util.null2String(rs.getString("yhzh"));
				PAYEE_CODE = Util.null2String(rs.getString("lhh"));
				FKYYDM = Util.null2String(rs.getString("cashcode"));
				PURPOSE = Util.null2String(rs.getString("usage"));
				URGENCY_FLAG = Util.null2String(rs.getString("jjbz"));
				ZZKM = Util.null2String(rs.getString("zzkm"));
				ISFORINDIVIDUAL = Util.null2String(rs.getString("gsbz"));
				sfpz = Util.null2String(rs.getString("sfpz"));
				wllb = Util.null2String(rs.getString("wllb"));
			}
			if(wllb.equals("4")){
				String sql_gz = "update " + tableName + " set status='S',message='工资请款流程，不调用资金接口' where requestid=" + requestid;
				rs.execute(sql_gz);
			}else if(CORP_CODE.equals("2400")){
				String sql_gsdm = "update " + tableName + " set status='S',message='公司代码=2400，不调用资金接口' where requestid=" + requestid;
				rs.execute(sql_gsdm);
			}else{
				String sqlpara = "";
				sqlpara = " select bzdm from uf_currency where id= " + CUR;
				rs.executeSql(sqlpara);
				if (rs.next()) {
					CUR = Util.null2String(rs.getString("bzdm"));
				}
				JSONArray jsonArray = new JSONArray();
				try {
		
					JSONObject jsonObjSon = new JSONObject();
		
					jsonObjSon.put("serial_no_erp", SERIAL_NO_ERP);
					jsonObjSon.put("req_date", REQ_DATE);
					jsonObjSon.put("corp_code", CORP_CODE);
					jsonObjSon.put("payer_acc_no", PAYER_ACC_NO);
					jsonObjSon.put("cur", CUR);
					jsonObjSon.put("item_code", ITEM_CODE);
					jsonObjSon.put("zzbs", ZZBS);
					jsonObjSon.put("rmk", RMK);
					jsonObjSon.put("abs", ABS);
					jsonObjSon.put("voucher_type", VOUCHER_TYPE);
					jsonObjSon.put("wish_pay_day", WISH_PAY_DAY);
					jsonObjSon.put("zzkm", ZZKM);
					jsonObjSon.put("jzdm", JZDM);
					jsonObjSon.put("system_type", SYSTEM_TYPE);
		
					jsonObjSon.put("fkyydm", FKYYDM);
					jsonObjSon.put("purpose", PURPOSE);
					jsonObjSon.put("amt", AMT);
					jsonObjSon.put("gysdm", GYSDM);
					jsonObjSon.put("payee_name", PAYEE_NAME);
					jsonObjSon.put("payee_bank", PAYEE_BANK);
					jsonObjSon.put("payee_acc_no", PAYEE_ACC_NO);
					jsonObjSon.put("payee_code", PAYEE_CODE);
					jsonObjSon.put("isforindividual", ISFORINDIVIDUAL);
					jsonObjSon.put("urgency_flag", URGENCY_FLAG);
					jsonObjSon.put("sfpz", sfpz);
					jsonArray.put(jsonObjSon);
		
				} catch (Exception e) {
					log.writeLog("json拼接错误");
				}
				JSONObject head = new JSONObject();
				try {
					head.put("bean", jsonArray);
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				ECPayXmlUtil tran = new ECPayXmlUtil();
				String json = tran.javaToXml("", "", requestid, head.toString());
				log.writeLog("打印json————————" + json);
				HnPayWebService pay = new HnPayWebService();
				String sign = "";
				String message = "";
				try {
					Response result = pay.getResultMethod(json);
					sign = result.getSIGN();
					message = result.getMessage();
				} catch (Exception e) {
					e.printStackTrace();
				}
				SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
				String para = "message";
				Map<String, Object> result = saxXmlUtil.getXmlMap(message);
				Object mess = result.get(para);//提示信息
				if(mess.toString().length() >=50 ){
					mess = mess.toString().substring(0,50);
				}
				log.writeLog("状态和消息------"+sign +"," +mess);
				String sql_update = "update " + tableName + " set status='" + sign + "',message='" + mess + "' where requestid=" + requestid;
				rs.execute(sql_update);
				log.writeLog("错误信息2————————" + sql_update);
				if ("F".equals(sign)) {
					// 调用异常 返回错误信息
					info.getRequestManager().setMessageid(System.currentTimeMillis() + "");
					info.getRequestManager().setMessagecontent(mess.toString());
					return SUCCESS;
				}
			}
		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}

		return SUCCESS;
	}

}
