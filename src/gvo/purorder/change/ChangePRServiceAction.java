package gvo.purorder.change;

import gvo.purorder.change.SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub.Response;
import gvo.util.xml.SaxXmlUtil;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ChangePRServiceAction implements Action {
	/**
	 * ec与sap采购订单修改
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-17
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购订单修改流程 ChangePRServiceAction————————————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String tableNamedt = "";
		String mainID = "";// 主表ID
		String sql = "";
		String ORDERNO = "";// 采购订单编号
		RecordSet rs = new RecordSet();
		sql = "select tablename from workflow_bill where id in (select formid from workflow_base where id =" + workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		if (!"".equals(tableName)) {
			tableNamedt = tableName + "_dt1";
			// 查询主表
			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			JSONObject head = new JSONObject();
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("ID"));
				ORDERNO = Util.null2String(rs.getString("orderno"));
			}
			try {
				head.put("REQUESTID", requestid);
				head.put("ORDERNO", ORDERNO);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONArray jsonArray = new JSONArray();
			// 查询明细表
			sql = "select * from " + tableNamedt + " where mainid=" + mainID;
			rs.execute(sql);
			while (rs.next()) {
				String PROJECT = Util.null2String(rs.getString("project"));// 项目
				String MATERIALNO = Util.null2String(rs.getString("materialNo"));// 物料编号
				String NEWNUM = Util.null2String(rs.getString("newnum"));// 修改后数量
				String NEWPRICE = Util.null2String(rs.getString("newprice"));// 修改后单价
				String NEWTAXCODE = Util.null2String(rs.getString("newtaxcode"));// 修改税码
				String NEWOUTDATE = Util.null2String(rs.getString("newoutdate"));// 修改后交期
				String LOEKZ1 = Util.null2String(rs.getString("loekz1"));// 修改后删除标示
				JSONObject jsonObjSon = new JSONObject();
				
				try {
					jsonObjSon.put("PROJECT", PROJECT);
					jsonObjSon.put("MATERIALNO", MATERIALNO);
					jsonObjSon.put("NEWNUM", NEWNUM);
					jsonObjSon.put("NEWPRICE", NEWPRICE);
					jsonObjSon.put("NEWTAXCODE", NEWTAXCODE);
					jsonObjSon.put("NEWOUTDATE", NEWOUTDATE);
					jsonObjSon.put("LOEKZ1", LOEKZ1);
					jsonArray.put(jsonObjSon);
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("CHILD_ChangePRService_SAP_1_LIST", jsonArray);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			JSONArray jsonArr = new JSONArray();
			jsonArr.put(head);
			OrderXmlUtil tran = new OrderXmlUtil();
			String json = tran.javaToXml(jsonArr.toString(), "", requestid, "");
			log.writeLog("打印json——————————" + json);
			ChangePRService order = new ChangePRService();
			String sign = "";
			String message = "";
			try {
				Response result = order.getResultMethod(json);
				sign = result.getSIGN();
				message = result.getMessage(); 
				log.writeLog("sign = " + sign);
				log.writeLog("message = " + message);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "E_MSGTX";
			Map<String, Object> result = saxXmlUtil.getXmlMap(message);
			Object emeeage = result.get(para);//消息文本
			String sql_update = "update " + tableName + " set estatus2='" + sign + "',emeeage='" + emeeage + "' where requestid=" + requestid;
			rs.execute(sql_update);
			log.writeLog("更新语句————————" + sql_update);
		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
}
