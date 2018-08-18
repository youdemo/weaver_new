package gvo.costcontrol.yld;


import gvo.costcontrol.emreim.PurXmlUtilOld;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.costcontrol.yldchange.SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub;
import gvo.wsclient.costcontrol.yldchange.SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.Response;

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

public class YldChangeAction implements Action {
	/**
	 * oa与sap预留单审批修改流程
	 * 
	 * @author tangjianyong
	 * @version 1.0 2018-07-28
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入预留单审批修改流程 YldChangeAction——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String tableNamedt = "";
		String sql = "";
		String mainID = "";// 主表id,关联明细表
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String RSNUM = "";// 预留编号
		String WEMPF = "";// 领料人
		String UMLGO = "";// 接收库存地点
		JSONArray jsonArray = new JSONArray();
		JSONObject head = new JSONObject();
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		if (!"".equals(tableName)) {
			tableNamedt = tableName + "_dt1";

			// 查询主表
			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("ID"));
				RSNUM = Util.null2String(rs.getString("RSNUM"));
				WEMPF = Util.null2String(rs.getString("WEMPF"));
				UMLGO = Util.null2String(rs.getString("UMLGO"));
			}
			// 查询明细表1
			sql = " select * from " + tableNamedt + " where mainid = " + mainID;
			res.execute(sql);
			while (res.next()) {
				String RSPOS = Util.null2String(res.getString("RSPOS"));// 项目
				String LGORT = Util.null2String(res.getString("LGORT"));// 库存地点
				String CHARG = Util.null2String(res.getString("CHARG"));// 批次
				String MEASUREMENTUNIT = Util.null2String(res.getString("measurementUnit"));// 计量单位
				String REQ_DATE = Util.null2String(res.getString("req_date"));// 组件的需求日期
				String MATERIALDESCRIPTION = Util.null2String(res.getString("materialDescription"));// 物料描述
				String BDMNG = Util.null2String(res.getString("BDMNG"));// 申请数量
				String MOVEMENT = Util.null2String(res.getString("movement"));// 允许预留的货物移动
				String DELETE_IND = Util.null2String(res.getString("delete_ind"));// 删除标志
				try {
					JSONObject jsonObjSon = new JSONObject();
					jsonObjSon.put("WEMPF", WEMPF);
					jsonObjSon.put("UMLGO", UMLGO);
					jsonObjSon.put("RSPOS", RSPOS);
					jsonObjSon.put("LGORT", LGORT);
					jsonObjSon.put("CHARG", CHARG);
					jsonObjSon.put("MEASUREMENTUNIT", MEASUREMENTUNIT);
					jsonObjSon.put("REQ_DATE", REQ_DATE);
					jsonObjSon.put("MATERIALDESCRIPTION", MATERIALDESCRIPTION);
					jsonObjSon.put("BDMNG", BDMNG);
					jsonObjSon.put("MOVEMENT", MOVEMENT);
					jsonObjSon.put("DELETE_IND", DELETE_IND);
					jsonArray.put(jsonObjSon);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("ChangeReservationList_dt1", jsonArray);
				head.put("REQUESTID", requestid);
				head.put("RSNUM", RSNUM);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONArray jsonArr = new JSONArray();
			jsonArr.put(head);
			PurXmlUtilOld px = new PurXmlUtilOld();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdf.format(new Date());
			Head head1 = new Head("SAP.MM_HNYG-009_" + time, "1", "OA", "1",
					"userSAP", "P@ss0rd", "", "");
			String json = px.javaToXml(jsonArr.toString(), "", requestid, "", head1);
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
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String EMESS = "";// 
			EMESS = saxXmlUtil.getResult("EMESS", message);
			String sql_update = "update " + tableName + " set message= '" + sign + "',emess='"+EMESS+"' where requestid=" + requestid;
			rs.execute(sql_update);
			log.writeLog("更新语句————————" + sql_update);
		}else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub crs = new SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub();
		SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_ChangeReservationListService cres = new SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_ChangeReservationListService();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_ChangeReservationListService(cres);
		return result;
	}
}
