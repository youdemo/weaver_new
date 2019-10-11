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
 * @version 创建时间：2019-6-7 下午10:18:27
 * 上岗证等级 回传
 */
public class PostGradeReturn  implements Action {
	
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
				head.put("FApplyNO",Util.null2String(rs.getString("FApplyNO")));// 	单号			
				head.put("FAssessmentDate",Util.null2String(rs.getString("FAssessmentDate")));//单据日期		
//				head.put("Fstatus",Util.null2String(rs.getString("Fstatus")));// 状态	
				if(flag.equals("submit")){
					head.put("Fstatus", "3");// 状态			
				}else{
					head.put("Fstatus", Util.null2String(rs.getString("Fstatus")));// 状态
				}
				head.put("Fstation",Util.null2String(rs.getString("Fstation")));//等级		
				head.put("Fremark",Util.null2String(rs.getString("Fremark")));// 备注		
				head.put("FCreatorID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FCreatorID"))+""));// 填单人
				head.put("FDeptID",gu.getFieldVal("hrmdepartment", "departmentcode", "id",Util.null2String(rs.getString("FDeptID"))+""));// 所属部门
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
		log.writeLog("JSOn----JST-WLI-001--"+json.toString());
		String result = "";
		try {
			result = ts.serviceImp(json.toString(), "JST-WLI-001");
			log.writeLog("result----JST-WLI-001--"+result);
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
			node.put("FProjectPlace", Util.null2String(rs_detail.getString("FProjectPlace")));//项目地		
			node.put("FProjectDocID", Util.null2String(rs_detail.getString("FProjectDocID")));//项目大类		
			node.put("FProjectItem", Util.null2String(rs_detail.getString("FProjectItem")));//项目		
			node.put("Fphase", Util.null2String(rs_detail.getString("Fphase")));//阶段		
			node.put("FEmpID", Util.null2String(rs_detail.getString("FEmpID")));//工号		
//			姓名		FEmpName			
			node.put("Fscores", Util.null2String(rs_detail.getString("Fscores")));//基础得分		
			node.put("FAssessmentDate", Util.null2String(rs_detail.getString("FAssessmentDate")));//考核日期			
			node.put("FInvigilatorID", Util.null2String(rs_detail.getString("FInvigilatorID")));//监考官		
			node.put("Fgrade", Util.null2String(rs_detail.getString("Fgrade")));//能力状况		
			node.put("Fremark", Util.null2String(rs_detail.getString("Fremark")));//备注说明	
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}
}
