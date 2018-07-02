package gvo.ecreimburse;

import gvo.ecpay.BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub.Response;
import gvo.ecpay.ECPayXmlUtil;
import gvo.ecpay.HnPayWebService;
import gvo.util.pay.TransformUtil;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

/**
 * EC与资金V0-025合同/订单付款申请非立项黑牛
 * 
 * @author daisy
 * @version 1.0 2017-11-22
 **/

public class ContractOrderPaymentAction implements Action {
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入合同/订单付款申请 ContractOrderPaymentAction——————");
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
		String RMK = "";// 合同编号
		String ABS = "";// 付款事由
		String AMT = "";// 本次付款金额
		String GYSDM = "";// 供应商编码
		String PAYEE_NAME = "";// 户名
		String PAYEE_BANK = "";// 开户行
		String PAYEE_ACC_NO = "";// 收款账号
		String PAYEE_CODE = "";// 联行号
		String FKYYDM = "";// 现金流量代码
		String PURPOSE = "";// 银行付款用途
		String URGENCY_FLAG = "";// 加急标志
		String ISFORINDIVIDUAL = "";// 对公对私标志
		String VOUCHER_TYPE = "";// 付款方式
		String WISH_PAY_DAY = "";// 申请日期
		String ZZKM = "";// 传递空值
		String JZDM = "21";// 固定值21
		String SYSTEM_TYPE = "0";// 固定值0
		String PAYTYPE = "";// 结算方式
		String sfpz = "";// 是否生成凭证
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		if (!"".equals(tableName)) {

			// 查询主表

			sql = "select * from " + tableName + " where requestid="
					+ requestid;
			rs.execute(sql);
			if (rs.next()) {
				SERIAL_NO_ERP = Util.null2String(rs.getString("requestid"));
				REQ_DATE = Util.null2String(rs.getString("sqrq"));
				CORP_CODE = Util.null2String(rs.getString("corpcode"));
				PAYER_ACC_NO = Util.null2String(rs.getString("fkyhzh2"));
				CUR = Util.null2String(rs.getString("htjebz"));
				ITEM_CODE = Util.null2String(rs.getString("kmbm"));

				RMK = Util.null2String(rs.getString("htbh"));
				ABS = Util.null2String(rs.getString("fksy"));
				if (ABS.length() >= 50) {
					ABS = ABS.substring(0, 50);
				}
				VOUCHER_TYPE = Util.null2String(rs.getString("fkfs"));
				PAYTYPE = Util.null2String(rs.getString("jsfs"));
				ZZBS = Util.null2String(rs.getString("zzbs"));
				TransformUtil tran = new TransformUtil();
				if (PAYTYPE.equals("0")) {
					ZZBS = Util.null2String(rs.getString("tszzbs"));
				} else {
					ZZBS = "";
				}
				VOUCHER_TYPE = tran.getPaytype(VOUCHER_TYPE);

				WISH_PAY_DAY = Util.null2String(rs.getString("sqrq"));
				AMT = Util.null2String(rs.getString("htjfbcje"));
				GYSDM = Util.null2String(rs.getString("gysbm"));
				PAYEE_NAME = Util.null2String(rs.getString("hm"));
				PAYEE_BANK = Util.null2String(rs.getString("khh"));
				PAYEE_ACC_NO = Util.null2String(rs.getString("skzh"));
				PAYEE_CODE = Util.null2String(rs.getString("bankcode"));
				FKYYDM = Util.null2String(rs.getString("cashcode2"));
				PURPOSE = Util.null2String(rs.getString("usage"));
				URGENCY_FLAG = Util.null2String(rs.getString("jjbz"));
				ISFORINDIVIDUAL = Util.null2String(rs.getString("gsbz"));
				sfpz = Util.null2String(rs.getString("sfpz"));
			}
			String sqlpara = "";
			sqlpara = " select bzdm from uf_currency where id= " + CUR;
			res.executeSql(sqlpara);
			if (res.next()) {
				CUR = Util.null2String(res.getString("bzdm"));
			}
			try {
				JSONObject head = new JSONObject();
				JSONArray jsonArray = new JSONArray();
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
				head.put("bean", jsonArray);

				ECPayXmlUtil chan = new ECPayXmlUtil();
				String json = chan.javaToXml("", "", requestid, head.toString());
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
				if ("F".equals(sign)) {
					// 调用异常 返回错误信息
					info.getRequestManager().setMessageid(System.currentTimeMillis() + "");
					info.getRequestManager().setMessagecontent(mess.toString());
					return SUCCESS;
				}
			} catch (Exception e) {
				log.writeLog(e.getMessage());
			}

		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}

}
