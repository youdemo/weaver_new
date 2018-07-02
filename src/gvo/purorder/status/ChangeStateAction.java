package gvo.purorder.status;

import gvo.purorder.status.SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.Response;
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

public class ChangeStateAction implements Action {
	/**
	 * ec与sap采购订单状态更新
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-15
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购订单状态更新ChangeStateAction————————————");
		String workflowID = info.getWorkflowid();
		String REQUESTID = info.getRequestid();
		String tableName = info.getRequestManager().getBillTableName();
		String sql = "";
		String ORDERNO = "";// 采购订单号
		String FRGCO = "20";//审批代码
		String ZRESET = "X";//取消审批
		RecordSet rs = new RecordSet();
		if (!"".equals(tableName)) {
			// 查询主表
			sql = "select * from " + tableName + " where requestid="
					+ REQUESTID;
			rs.execute(sql);
			JSONObject head = new JSONObject();
			if (rs.next()) {
				ORDERNO = Util.null2String(rs.getString("orderno"));
			}
			try {
				head.put("REQUESTID", REQUESTID);
				head.put("ORDERNO", ORDERNO);
				head.put("FRGCO", FRGCO);
				head.put("ZRESET", ZRESET);
			} catch (JSONException e) {
				e.printStackTrace();
			}

			JSONArray jsonArray = new JSONArray();

			jsonArray.put(head);
			StatusXmlUtil tran = new StatusXmlUtil();
			String json = tran.javaToXml(jsonArray.toString(), "", REQUESTID, "");
			log.writeLog("打印json——————————" + json);
			ChangePRStateService state = new ChangePRStateService();
			String sign = "";
			String message = "";
			try {
				Response result = state.getResultMethod(json);
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
			Object emeeage = result.get(para);
			String sql_update = "update " + tableName + " set estatus1='" + sign + "'where requestid="
					+ REQUESTID;
			rs.execute(sql_update);
			log.writeLog("更新语句————————" + sql_update);
		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}

		return SUCCESS;
	}
}
