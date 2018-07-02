package gvo.reservationlist.listcreate;

import gvo.reservationlist.listcreate.SAPPR_MM_0_CreateReservationListService_pttBindingQSServiceStub.Response;
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


public class CreateReservation extends BaseBean implements Action {
	/**
	 * ec与sap预留单审批创建流程
	 * 
	 * @author 张瑞坤
	 * @version 1.0 2017-11-16
	 **/

	BaseBean log = new BaseBean();

	public Response doService(String json) throws Exception {
		SAPPR_MM_0_CreateReservationListService_pttBindingQSServiceStub sqs = new SAPPR_MM_0_CreateReservationListService_pttBindingQSServiceStub();
		SAPPR_MM_0_CreateReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_CreateReservationListService ss = new SAPPR_MM_0_CreateReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_CreateReservationListService();
		ss.setData(json);
		//log.writeLog(sqs.SAPPR_MM_0_CreateReservationListService(ss).getMessage());
		Response result =sqs.SAPPR_MM_0_CreateReservationListService(ss);
		return result;
	}

	public String execute(RequestInfo info) {
		log.writeLog("进入预留单创建流程 CreateReservation——————");
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String workflowid = info.getWorkflowid();// 流程ID
		String tablename = info.getRequestManager().getBillTableName();// 表单名称
		String requestid = info.getRequestid();//请求id
		String mainid = "";
		if (!"".equals(tablename)) {
			JSONObject json = new JSONObject();
			JSONArray jsArray = new JSONArray();
			String sql = "select * from " + tablename + " where requestid='" + requestid + "'";
			rs.executeSql(sql);
			log.writeLog("错误信息————————" + sql);
			if (rs.next()) {
				mainid = Util.null2String(rs.getString("id"));
				String WERKS = Util.null2String(rs.getString("WERKS"));//工厂
				String RSDAT = Util.null2String(rs.getString("RSDAT"));//申请日期
				String SQRXM = Util.null2String(rs.getString("sqrxm"));//申请人姓名
				String BWART = Util.null2String(rs.getString("BWART"));//移动类型
				String KOSTL = Util.null2String(rs.getString("KOSTL"));//成本中心
				String ANLN1 = Util.null2String(rs.getString("ANLN1"));//固定资产编号
				String AUFNR = Util.null2String(rs.getString("AUFNR"));//内部订单号
				String WEMPFNAME = Util.null2String(rs.getString("wempfName"));//领料人姓名
				String WERKS1 = Util.null2String(rs.getString("WERKS"));//工厂
				String UMLGO = Util.null2String(rs.getString("UMLGO"));//接收库存地点
				try {
					json.put("REQUESTID", requestid);
					json.put("WERKS", WERKS);
					json.put("RSDAT", RSDAT);
					json.put("SQRXM", SQRXM);
					json.put("BWART", BWART);
					json.put("KOSTL", KOSTL);
					json.put("ANLN1", ANLN1);
					json.put("AUFNR", AUFNR);
					json.put("WEMPFNAME", WEMPFNAME);
					json.put("WERKS1", WERKS1);
					json.put("UMLGO", UMLGO);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			String tableNamedt = tablename + "_dt1";
			sql = "select * from " + tableNamedt + " where  mainid='" + mainid + "'";
			res.executeSql(sql);
			while (res.next()) {
				JSONObject json1 = new JSONObject();
				String MATNR = Util.null2String(res.getString("MATNR"));//物料编号
				String LGORT = Util.null2String(res.getString("LGORT"));//库存地点	
				String CHARG = Util.null2String(res.getString("CHARG"));//批次
				String BDMNG = Util.null2String(res.getString("BDMNG"));//申请数量	
				String MEASUREMENTUNIT = Util.null2String(res.getString("measurementUnit"));//计量单位	
				String ACCOUNT = Util.null2String(res.getString("account"));//总帐科目编号	
				String MATERIALDESCRIPTION = Util.null2String(res.getString("materialDescription"));//物料描述	
				try {
					json1.put("MATNR", MATNR);
					json1.put("LGORT", LGORT);
					json1.put("CHARG", CHARG);
					json1.put("BDMNG", BDMNG);
					json1.put("MEASUREMENTUNIT", MEASUREMENTUNIT);
					json1.put("ACCOUNT", ACCOUNT);
					json1.put("MATERIALDESCRIPTION", MATERIALDESCRIPTION);
					jsArray.put(json1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				json.put("CreateReservationListService_dt1", jsArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONArray jsArray1 = new JSONArray();
			jsArray1.put(json);
			CreateXmlUtil tran = new CreateXmlUtil();
			String json2 = tran.javaToXml(jsArray1.toString(), "", requestid, "");
			log.writeLog("json2 = " + json2);
			String sign = "";
			String mess = "";
			try {
				Response result;
				result = doService(json2);
				sign = result.getSIGN();
				mess = result.getMessage();
				log.writeLog("sign = " + sign);
				log.writeLog("mess = " + mess);
			} catch (Exception e) {
				 e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "EMESS";//返回状态描述
			String para1 = "RSNUM";// 预留编号
			String message = sign;// 状态
			Map<String, Object> result = saxXmlUtil.getXmlMap(mess);
			Object EMESS = result.get(para);
			Object RSNUM = result.get(para1);
			String sql_update = "update " + tablename + " set message='" + message + "',RSNUM='" + RSNUM + "',emess = '" + EMESS + "' where requestid=" + requestid;
			rs.execute(sql_update);
			log.writeLog("sql_update = " + sql_update);
		}else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
}
