package jsd.erp.methodbody;

import jsd.erp.util.GetUtil;
import jsd.serviceImp.TriggerStubImp;
import jsd.status.SetInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-7 下午10:03:16
 * 庶务类领料 回传
 */
public class GeneralConsumptionReturn  implements Action {
	
	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		String flag = info.getRequestManager().getSrc();//当前操作类型 submit:提交/reject:退回        
		RecordSet rs = new RecordSet();
		User user = info.getRequestManager().getUser();//获取当前操作用户对象 
		String sql = "select* from " + tablename + " where requestid = '"
				+ requestid + "'";
		rs.executeSql(sql);
		GetUtil gu = new GetUtil();
		TriggerStubImp ts = new TriggerStubImp();
		JSONObject json = new JSONObject();
		JSONObject head = new JSONObject();
		JSONObject detail = new JSONObject();
		if (rs.next()) {
			try {
				head.put("FMaterialReqNO",Util.null2String(rs.getString("FMaterialReqNO")));// 领料单号
				head.put("Ftype",Util.null2String(rs.getString("Ftype")));// 	领用类型
				head.put("FCreateDateTime",Util.null2String(rs.getString("FCreateDateTime")));// 单据日期
//				head.put("Fstatus",Util.null2String(rs.getString("Fstatus")));// 状态
				
				if(flag.equals("submit")){
					head.put("Fstatus", "3");// 状态			
				}else{
					head.put("Fstatus", Util.null2String(rs.getString("Fstatus")));// 状态
				}
				
				head.put("FDeptID",gu.getFieldVal("hrmdepartment", "departmentcode", "id",Util.null2String(rs.getString("FDeptID"))+""));// 所属部门
				head.put("FDemandEmpID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FDemandEmpID"))+""));// 领料人
				head.put("FCreatorID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FCreatorID"))+""));// 填单人
				head.put("Fremark",Util.null2String(rs.getString("Fremark")));// 备注
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		try {
			detail = mingxiInfo(tablename, requestid);
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		try {
			if(flag.equals("reject")){//退回
				json.put("ACTION", "Return");// Return:OA系统退单,Approve:OA系统审核完成
			}else if(flag.equals("submit")){
				json.put("ACTION", "Approve");// Return:OA系统退单,Approve:OA系统审核完成
			}
			json.put("CREATERCODE", gu.getFieldVal("hrmresource", "workcode", "id",user.getUID()+""));//user.getUID();
			json.put("HEADER", head);
			json.put("DETAILS", detail);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		log.writeLog("JSOn----JST-GEA-001--"+json.toString());
		String result = "";
		try {
			result = ts.serviceImp(json.toString(), "JST-GEA-001");
			log.writeLog("result----JST-GEA-001--"+result);
		} catch (Exception e) {
			log.writeLog("yicheng-------"+e.getMessage());
			SetInfo si = new SetInfo();
			si.setReturnInfo(requestid, "E", e.getMessage());
		}
		try {
			JSONObject  JsonResult = new JSONObject(result);
			String return_type = JsonResult.getString("return_type");//E  S
			String return_message = JsonResult.getString("return_message");
			SetInfo si = new SetInfo();
			si.setReturnInfo(requestid, return_type, return_message);
			if(return_type.equals("E")){
				info.getRequestManager().setMessagecontent(return_message);    
				info.getRequestManager().setMessageid("错误信息提示"); 
				return SUCCESS ;//
			}
		} catch (JSONException e) {
			log.writeLog("异常----"+e.getMessage());
			e.printStackTrace();
		}		
		return SUCCESS;
	}

	public JSONObject mingxiInfo(String tablename, String requestid) throws JSONException {
		RecordSet rs_detail = new RecordSet();
		JSONObject DT = new JSONObject();
		JSONArray detail1 = new JSONArray();
		String sql = "select * from "+tablename+"_dt1 where mainid=(select id from "
				+ tablename + " where requestid='" + requestid + "')";//
		rs_detail.executeSql(sql);
		while (rs_detail.next()) {
			JSONObject node = new JSONObject();	
			node.put("FMaterialId", Util.null2String(rs_detail.getString("FMaterialId")));//物料编码
			node.put("FMaterialDesc", Util.null2String(rs_detail.getString("FMaterialDesc")));//物料名称
			node.put("FMaterialBrand", Util.null2String(rs_detail.getString("FMaterialBrand")));//品牌
			node.put("FMaterialSpec", Util.null2String(rs_detail.getString("FMaterialSpec")));//规格
			node.put("Funit", Util.null2String(rs_detail.getString("Funit")));//单位
			node.put("FNowInvQty", Util.null2String(rs_detail.getString("FNowInvQty")));//现有库存
			node.put("Fqty", Util.null2String(rs_detail.getString("Fqty")));//数量
			node.put("FUsedQty", Util.null2String(rs_detail.getString("FUsedQty")));//已发数量
			node.put("Fremark", Util.null2String(rs_detail.getString("Fremark")));//备注
			node.put("FMainMaterialID", Util.null2String(rs_detail.getString("FMainMaterialID")));//被替代料
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}
}
