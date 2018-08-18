package gvo.costcontrol.yld;

import gvo.costcontrol.emreim.PurXmlUtilOld;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.yldpost.SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.yldpost.SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


public class PostingYLDAction extends BaseBean implements Action {
	/**
	 * oa与sap预留单审批过账流程
	 * 
	 * @author tangjianyong
	 * @version 1.0 2018-07-28
	 **/
	BaseBean log = new BaseBean();

	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub sps = new SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub();
		SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_PostingReservationListService sr = new SAPPR_MM_0_PostingReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_PostingReservationListService();
		sr.setData(json);
		Response result = sps.SAPPR_MM_0_PostingReservationListService(sr);
		return result;
	}

	public String execute(RequestInfo info) {
		log.writeLog("进入预留单过账流程 PostingYLDAction——————");
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
			PurXmlUtilOld px = new PurXmlUtilOld();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdf.format(new Date());
			Head head1 = new Head("SAP.MM_HNYG-010_" + time, "1", "OA", "1",
					"userSAP", "P@ss0rd", "", "");
			String jsonResult = px.javaToXml(jsArray1.toString(), "", requestid, "", head1);
			log.writeLog("查看json格式————————" + json);
			String sign = "";
			String message = "";
			Response result = null;
			try {
				result = getResultMethod(jsonResult);
			} catch (Exception e) {
				log.writeLog("错误日志----" + e.getMessage());
				e.printStackTrace();
			}
			sign = result.getSIGN();
			message = result.getMessage();
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String EMESS = "";// 
			String MJAHR = "";// 
			String MBLNR = "";// 
			EMESS = saxXmlUtil.getResult("EMESS", message);
			MJAHR = saxXmlUtil.getResult("MJAHR", message);
			MBLNR = saxXmlUtil.getResult("MBLNR", message);
			
			String sql_update = "update " + tablename + " set gzxxlx='" + sign
					+ "',gzxxlxms ='" + EMESS + "',MJAHR ='" + MJAHR + "',MBLNR ='"
					+ MBLNR + "'  where requestid=" + requestid;
			rs.execute(sql_update);
			log.writeLog("sql_update:"+sql_update);
			}else{
				return "-1";
			}
			return SUCCESS;
		}
}
