package gvo.material;

import gvo.material.SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class MaterialAdmitInfo extends BaseBean implements Action{
	/**
	 * oa与sap材料承认信息更新接口
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-23
	 **/
	BaseBean log = new BaseBean();
	public Response getResultMethod(String json) throws Exception{
		SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub crs = new SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub();
		SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.SAPPR_MM_0_MaterialAdmitInfo cres = new SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.SAPPR_MM_0_MaterialAdmitInfo();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_MaterialAdmitInfo(cres);
		return result;
		
	}
	public String execute(RequestInfo info) {
		log.writeLog("进入材料承认信息创建 MaterialAdmitInfo——————");
		String workflowID = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String sql = "";
		String requestid = info.getRequestid();// 流程请求ID
		String tableName = info.getRequestManager().getBillTableName();// 表单名称
		String COMPANYCODE = "";//公司代码
		String ISSURE = "";//供应商状态
		String SUPPLIER = "";//供应商
		String MATERIALCODE = "";//物料编码
		if(!"".equals(tableName)){
			sql = "select * from " + tableName + " where requestid = " + requestid ;
			rs.execute(sql);
			if(rs.next()){
				COMPANYCODE = Util.null2String(rs.getString("companyCode"));
				SUPPLIER = Util.null2String(rs.getString("suppliers"));
				MATERIALCODE = Util.null2String(rs.getString("sapMaterial"));
				ISSURE = Util.null2String(rs.getString("isSure"));
			}
			JSONObject head = new JSONObject();
			JSONArray Array = new JSONArray();
			try {
				head.put("companyCode", COMPANYCODE);
				head.put("isSure", ISSURE);
				head.put("suppliers", SUPPLIER);
				head.put("sapMaterial", MATERIALCODE);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Array.put(head);
			MaterialXmlUtil tran = new MaterialXmlUtil();
			String json = tran.javaToXml(Array.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			MaterialAdmitInfo mater = new MaterialAdmitInfo();
			String status = "";
			try {
			 	Response result = mater.getResultMethod(json);
			 	status = result.getSIGN();
                log.writeLog("返回状态————————" + status);
			} catch (Exception e) {
				e.printStackTrace();
			}
			String sql_update = "update " + tableName + " set status = '" + status +"' where requestid =" + requestid;
			rs.execute(sql_update);
			log.writeLog("更新语句————————" + sql_update);
		}else {
			return "-1";
		}
		return SUCCESS;
	}
}
