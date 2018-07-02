package gvo.purchase.information;

import gvo.purchase.information.SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub.Response;
import gvo.util.item.Data;
import gvo.util.item.Item;
import gvo.util.item.XStreamUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.List;

public class InfoDelAction extends BaseBean implements Action{
	/**
	 * oa与sap采购信息记录删除
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-23
	 * 
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购信息记录删除 InfoDelAction——————");
		String workflowID = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "";
		String requestid = info.getRequestid();// 流程请求ID
		String tableName = info.getRequestManager().getBillTableName();// 表单名称
		String mainID = "";//主表id
		String tableNamedt4 = "";//明细表4
		String WFTYPE  = "";//流程类型
		if(!"".equals(tableName)){
			tableNamedt4 = tableName + "_dt4";
			sql = "select * from " + tableName + " where requestid = " + requestid ;
			rs.execute(sql);
			if(rs.next()){
				mainID = Util.null2String(rs.getString("ID"));
				WFTYPE = Util.null2String(rs.getString("wftype"));
			}
			JSONArray Array1 = new JSONArray();
			JSONObject head = new JSONObject();
			
			JSONArray Array = new JSONArray();
			//查询明细表4
			sql = " select * from " + tableNamedt4 + " where mainid = " + mainID;
			res.execute(sql);
			while (res.next()) {
				JSONObject json1 = new JSONObject();
				String MATERIALNO = Util.null2String(res.getString("materialNo"));// 物料编号
				String RECORDNO = Util.null2String(res.getString("recordno"));// 信息记录编号
				String COMINFORECORD = Util.null2String(res.getString("cominforecord"));// 完整信息记录
				String GLANT = Util.null2String(res.getString("glant"));// 工厂
				String PURORGRECORD = Util.null2String(res.getString("purorgrecord"));// 采购组织数据
				String OAID = Util.null2String(res.getString("oaid"));// OAID
				String SUPPLIERNO = Util.null2String(res.getString("supplierNo"));// 供应商编号
				String PURORG = Util.null2String(res.getString("purorg"));// 采购组织
				String RECORDTYPE = Util.null2String(res.getString("recordtype"));// 信息记录类型
				try {
					json1.put("MATERIALNO", MATERIALNO);
					json1.put("RECORDNO", RECORDNO);
					json1.put("COMINFORECORD", COMINFORECORD);
					json1.put("GLANT", GLANT);
					json1.put("PURORGRECORD", PURORGRECORD);
					json1.put("OAID", OAID);
					json1.put("SUPPLIERNO", SUPPLIERNO);
					json1.put("PURORG", PURORG);
					json1.put("RECORDTYPE", RECORDTYPE);
					Array1.put(json1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("WFTYPE", WFTYPE);
				head.put("CHILD_POEINE_SAP_004_LIST", Array1);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Array.put(head);
			InfoXmlUtil inf = new InfoXmlUtil();
			String json = inf.javaToXml(Array.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			UpdatePurchasingInfo cs = new UpdatePurchasingInfo();
			String sign = "";
			String mess = "";
			try {
				Response result = cs.getResultMethod(json);
				sign = result.getSIGN();
				mess = result.getMessage(); 
				log.writeLog("sign = " + sign);
				log.writeLog("MESS = " + mess);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Data data=(Data)XStreamUtils.parseData(mess);
			List<Item> itemList=data.getLIST().getITEM();
			for(Item item:itemList){
				String recordno = item.getINFNR();//信息记录编号
	        	String sapmessage = item.getMESSAGE();//消息文本
	        	String status = item.getSTATUS();//状态
	        	String oaid = item.getOAID();//OAID
	        	String sql_update = "update " + tableNamedt4 + " set sapmessage = '" + sapmessage + "',sapstatus = '" + status + "',oaid = '" + oaid + "',recordno = '" + recordno + "' where mainid = " + mainID + " and oaid = " + oaid;
				rs.execute(sql_update);
	        	log.writeLog("更新语句————————" + sql_update);
			}     
		}else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
}
