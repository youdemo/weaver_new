package gvo.costcontrol.purchase.applicationedit;
import gvo.purchase.revise.RevisePRService;
import gvo.purchase.revise.ReviseXmlUtil;
import gvo.purchase.revise.SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub.Response;
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

public class PurchaseUpdateWebServiceAction implements Action {
	/**
	 * 采购申请修改esb接口
	 * 
	 * @author tangjy
	 * @version 1.0 2018-06-20
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购申请修改流程 PurchaseUpdateWebServiceAction——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String PRNO = "";// 采购申请单号
		String mainID = "";// 主表id,关联明细表
		String sql = "";
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

			// 查询主表
			sql = "select * from " + tableName + " where requestid=" + requestid;
			rs.execute(sql);
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("id"));
				PRNO = Util.null2String(rs.getString("prno"));
			}
			JSONArray jsonArray = new JSONArray();
			JSONObject head = new JSONObject();
			JSONArray jsonArr = new JSONArray();
			// 查询明细表1
			sql = " select * from " + tableName + "_dt1 where mainid = " + mainID;
			rs.execute(sql);
			while (rs.next()) {
				JSONObject jsonObjSon = new JSONObject();
				String ITEMID = Util.null2String(rs.getString("itemid"));// 项目
				String XWLMS = Util.null2String(rs.getString("xwlms"));// 修改后物料描述
				String NEWPURNUM = Util.null2String(rs.getString("newpurnum"));// 采购申请数量
				String OUTDATE = Util.null2String(rs.getString("outdate"));// 项目交货日期
				String NEWPRICE = Util.null2String(rs.getString("newprice"));// 修改后预估单价
				try {
					jsonObjSon.put("ITEMID", ITEMID);
					jsonObjSon.put("XWLMS", XWLMS);
					jsonObjSon.put("NEWPURNUM", NEWPURNUM); 
					jsonObjSon.put("OUTDATE", OUTDATE);
					jsonObjSon.put("NEWPRICE", NEWPRICE);
					jsonArray.put(jsonObjSon);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("PRNO", PRNO);
				head.put("CHILD_RevisePR_SAP_1_LIST", jsonArray);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			jsonArr.put(head);
			ReviseXmlUtil rvs = new ReviseXmlUtil();
			String json = rvs.javaToXml(jsonArr.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			RevisePRService pur = new RevisePRService();
			String SIGN = "";
			String MESSAGE = "";
			try {
				Response result = pur.getResultMethod(json);
				SIGN = result.getSIGN();
				MESSAGE = result.getMessage(); 
				log.writeLog("sign = " + SIGN);
				log.writeLog("MESSAGE = " + MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "E_MSGTX";
			Map<String, Object> result = saxXmlUtil.getXmlMap(MESSAGE);
			Object message = result.get(para);//消息文本
			String sql_update = "update " + tableName + " set e_falg='" + SIGN + "',e_mes='" + message + "' where requestid=" + requestid;
			rs.execute(sql_update);
			log.writeLog("更新语句————————" + sql_update);
		
		return SUCCESS;
	}
}
