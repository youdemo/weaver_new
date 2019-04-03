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
 * EC与资金V0-046差旅费用报销申请流程
 * 
 * @author daisy
 * @version 1.0 2017-11-22
 **/

public class TravelReimAction implements Action {
	BaseBean log = new BaseBean();// 定义写入日志的对象

	public String execute(RequestInfo info) {
		log.writeLog("进入差旅费用报销TravelReimAction——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();

		RecordSet rs = new RecordSet();
		TransformUtil tran = new TransformUtil();
		String sql = "";
		String tableName = "";
		String tableNamedt = "";// 明细表
		String mainID = "";// 主表id,关联明细表
		String SERIAL_NO_ERP = "";// 报销单号
		String REQ_DATE = "";// 申请日期
		String CORP_CODE = "";// 公司代码
		String CUR = "";// 结算币别
		String ZZBS = "";// 传递空值
		String RMK = "";// 报销单号
		String ABS = "";// 费用报销说明
		String WISH_PAY_DAY = "";// 申请日期
		String ZZKM = "";// 传递空值
		String JZDM = "21";// 固定值21
		String SYSTEM_TYPE = "0";// 固定值0
		String sfpz = "0";// 固定值0
		String E_STATUS = "";
		String status = "";
		String ssgs = "";
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		if (!"".equals(tableName)) {

			tableNamedt = tableName + "_dt3";

			// 查询主表

			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("ID"));
				SERIAL_NO_ERP = Util.null2String(rs.getString("requestid"));
				REQ_DATE = Util.null2String(rs.getString("reqdate"));
				CORP_CODE = Util.null2String(rs.getString("corpcode"));
				CUR = Util.null2String(rs.getString("currencydesc"));
				RMK = Util.null2String(rs.getString("flowno"));
				ABS = Util.null2String(rs.getString("remark1"));
				if (ABS.length() >= 50) {
					ABS = ABS.substring(0, 50);
				}
				WISH_PAY_DAY = Util.null2String(rs.getString("reqdate"));
				E_STATUS = Util.null2String(rs.getString("E_STATUS"));
				status = Util.null2String(rs.getString("status"));
				ssgs = Util.null2String(rs.getString("ssgs"));
			}
			if(!"S".equals(E_STATUS)) {
				return SUCCESS;
			}
			if("S".equals(status)) {
				return SUCCESS;
			}
			if("82".equals(ssgs) || "83".equals(ssgs) || "84".equals(ssgs)) {
			try {
				JSONObject head = new JSONObject();
				JSONArray jsonArray = new JSONArray();

				// 查询明细表

				sql = "select * from " + tableNamedt + " where mainid=" + mainID;
				rs.execute(sql);
				while (rs.next()) {
					String PAYER_ACC_NO = Util.null2String(rs.getString("fkyhzh2"));// 付款银行账号
					String ITEM_CODE = Util.null2String(rs.getString("kmbm"));// 资金计划科目编码
					String AMT = Util.null2String(rs.getString("payamt"));// 支付金额
					String GYSDM = Util.null2String(rs.getString("hrmcode"));// 支付信息员工编号
					String PAYEE_NAME = Util.null2String(rs.getString("hrmname"));// 员工户名
					String PAYEE_BANK = Util.null2String(rs.getString("hrmbankname"));// 开户行
					String PAYEE_ACC_NO = Util.null2String(rs.getString("hrmbankaccount"));// 银行账号
					String PAYEE_CODE = Util.null2String(rs.getString("hrmbankcode"));// 联行号
					String FKYYDM = Util.null2String(rs.getString("cashcode2"));// 现金流量代码
					String PURPOSE = Util.null2String(rs.getString("usage"));// 银行付款用途
					String VOUCHER_TYPE = Util.null2String(rs.getString("paytype"));// 付款方式

					VOUCHER_TYPE = tran.getPaytype(VOUCHER_TYPE);
					String ISFORINDIVIDUAL = Util.null2String(rs.getString("gsbz"));// 对公对私标志
					String URGENCY_FLAG = Util.null2String(rs.getString("jjbz"));// 加急标志

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
				}
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
				e.printStackTrace();
			}
			}

		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}

}
