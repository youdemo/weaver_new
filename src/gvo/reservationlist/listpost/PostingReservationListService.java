package gvo.reservationlist.listpost;

import gvo.reservationlist.listpost.SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.Response;
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


public class PostingReservationListService extends BaseBean implements Action {
	/**
	 * ec与sap预留单审批过账流程
	 * 
	 * @author 张瑞坤
	 * @version 1.0 2017-11-16
	 **/
	BaseBean log = new BaseBean();

	public Response doService(String json) throws Exception {
		SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub sps = new SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub();
		SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_PostingReservationListService sr = new SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_PostingReservationListService();
		sr.setData(json);
		Response result = sps.SAPPR_MM_0_PostingReservationListService(sr);
		return result;
	}

	public String execute(RequestInfo info) {
		log.writeLog("进入预留单过账流程 PostingReservationListService——————");
		RecordSet rs = new RecordSet();
		String workflowid = info.getWorkflowid();// 流程ID
		String tablename = info.getRequestManager().getBillTableName();// 表单名称
		String requestid = info.getRequestid();
		String mainid = "";
		String RSNUM = "";// 预留编号
		String WEMPFNAME = "";// 领料人姓名	
		String UMLGO = "";// 接收库存地点
		String RSDAT = "";// 申请日期	
		String BUDAT = "";// 过账日期	
		String BWART = "";// 移动类型	
		JSONObject json = new JSONObject();
		JSONArray jsArray = new JSONArray();
		JSONArray jsArray1 = new JSONArray();
		
		if(!"".equals(tablename)){
			String sql = "select * from " + tablename + " where requestid='" + requestid + "'";
			rs.executeSql(sql);
			if (rs.next()) {
				mainid = Util.null2String(rs.getString("id"));
				RSDAT = Util.null2String(rs.getString("RSDAT"));
				BUDAT = Util.null2String(rs.getString("BUDAT"));
				BWART = Util.null2String(rs.getString("BWART"));
				RSNUM = Util.null2String(rs.getString("RSNUM"));
				UMLGO = Util.null2String(rs.getString("UMLGO"));
				WEMPFNAME = Util.null2String(rs.getString("wempfName"));
			}
			String tableNamedt = tablename + "_dt1";
			sql = "select * from " + tableNamedt + " where  mainid='" + mainid + "'";
			rs.executeSql(sql);
			while (rs.next()) {
				JSONObject json1 = new JSONObject();
				String RSPOS = Util.null2String(rs.getString("RSPOS"));//项目
				String MATNR = Util.null2String(rs.getString("MATNR"));//物料编号	
				String netAmount = Util.null2String(rs.getString("netAmount"));//实发数量
				String ACTUALCHARG = Util.null2String(rs.getString("actualCharg"));//实发批次	
				String LGORT = Util.null2String(rs.getString("LGORT"));//库存地点
				
				try {
					json1.put("REQUESTID", requestid);
					json1.put("RSPOS", RSPOS);
					json1.put("MATNR", MATNR);
					json1.put("NETAMOUNT", netAmount);
					json1.put("ACTUALCHARG", ACTUALCHARG);
					json1.put("LGORT", LGORT);
					json1.put("RSNUM", RSNUM);
					json1.put("UMLGO", UMLGO);
					json1.put("WEMPFNAME", WEMPFNAME);
					jsArray.put(json1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				json.put("REQUESTID", requestid);
				json.put("RSDAT", RSDAT);
				json.put("BUDAT", BUDAT);
				json.put("BWART", BWART);
				json.put("PostingReservationListService_dt1", jsArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsArray1.put(json);
			PostXmlUtil tran = new PostXmlUtil();
			String json2 = tran.javaToXml(jsArray1.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json2);
			String SIGN = "";
			String MESSAGE = "";
			try {
				Response result = doService(json2);
				SIGN = result.getSIGN();
				MESSAGE = result.getMessage();
				log.writeLog("返回结果————————" + SIGN);
				log.writeLog("返回结果————————" + MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String message = SIGN;
			String para1 = "EMESS";
			String para2 = "MJAHR";
			String para3 = "MBLNR";
			Map<String, Object> result = saxXmlUtil.getXmlMap(MESSAGE);
			Object E_MESS = result.get(para1);
			Object E_GJAHR = result.get(para2);
			Object E_MBLNR = result.get(para3);
			String sql_update = "update " + tablename + " set message='" + message
					+ "',emess ='" + E_MESS + "',MJAHR ='" + E_GJAHR + "',MBLNR ='"
					+ E_MBLNR + "'  where requestid=" + requestid;
			rs.execute(sql_update);
			}else{
				return "-1";
			}
			return SUCCESS;
		}
}
