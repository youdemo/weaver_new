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
 * @version 创建时间：2019-6-7 下午10:38:07
 * 加工件ECN申请 回传
 */
public class EcnApplyReturn   implements Action {
	
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
				head.put("FEcnNO",Util.null2String(rs.getString("FEcnNO")));// ECN编号
				head.put("FBomType",Util.null2String(rs.getString("FBomType")));// 	BOM类型
				head.put("FItemID",Util.null2String(rs.getString("FItemID")));// 设备编号
				head.put("FItemDesc",Util.null2String(rs.getString("FItemDesc")));// 设备描述
				head.put("F_ChangeType",Util.null2String(rs.getString("F_ChangeType")));// 变更类型
				head.put("FEcnDesc",Util.null2String(rs.getString("FEcnDesc")));//变更描述
				head.put("FRelationProductID",Util.null2String(rs.getString("FRelationProductID")));// 相关制造单号
				head.put("FApplyDate",Util.null2String(rs.getString("FApplyDate")));// 申请日期
				head.put("Fremark",Util.null2String(rs.getString("Fremark")));// 备注
				head.put("FCreateDateTime",Util.null2String(rs.getString("FCreateDateTime")));//创建时间
//				head.put("Fsatus",Util.null2String(rs.getString("Fsatus")));// 状态	/
				if(flag.equals("submit")){
					head.put("FStatus", "3");// 状态			
				}else{
					head.put("FStatus", Util.null2String(rs.getString("FStatus")));// 状态
				}
				
				head.put("FApplyID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FApplyID"))+""));// 申请人
				head.put("FCreatorID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FCreatorID"))+""));// 创建人			
							
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
		log.writeLog("JSOn----JST-ECN-001--"+json.toString());
		String result = "";
		try {
			result = ts.serviceImp(json.toString(), "JST-ECN-001");
			log.writeLog("result----JST-ECN-001--"+result);
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
			node.put("FChangeType", Util.null2String(rs_detail.getString("FChangeType")));//变更类型
			node.put("FBomNo", Util.null2String(rs_detail.getString("FBomNo")));//BOM编码
			node.put("FModularId", Util.null2String(rs_detail.getString("FModularId")));//模块号
			node.put("FCharacterCode", Util.null2String(rs_detail.getString("FCharacterCode")));//特征码
			node.put("FPartNo", Util.null2String(rs_detail.getString("FPartNo")));//物料编码
			node.put("F_MSPPartType", Util.null2String(rs_detail.getString("F_MSPPartType")));//零件类型
			node.put("FPartNOOldVer", Util.null2String(rs_detail.getString("FPartNOOldVer")));//物料原版本
			node.put("FPartNONewVer", Util.null2String(rs_detail.getString("FPartNONewVer")));//物料新版本新
			node.put("FOldScaleQty", Util.null2String(rs_detail.getString("FOldScaleQty")));//原单套用量
			node.put("Fmaterial", Util.null2String(rs_detail.getString("Fmaterial")));//材质
			node.put("FSurfaceTreat", Util.null2String(rs_detail.getString("FSurfaceTreat")));//表面处理		
			node.put("FECNReason", Util.null2String(rs_detail.getString("FECNReason")));//变更原因			
			node.put("FPurchaseTreatment", Util.null2String(rs_detail.getString("FPurchaseTreatment")));//采购处理			
			node.put("FQCTreatment", Util.null2String(rs_detail.getString("FQCTreatment")));//IQC处理			
			node.put("FInvTreatment", Util.null2String(rs_detail.getString("FInvTreatment")));//入库物料处理			
			node.put("FProductUseTreatment", Util.null2String(rs_detail.getString("FProductUseTreatment")));//生产领用未使用处理					
			node.put("FProductTreatment", Util.null2String(rs_detail.getString("FProductTreatment")));//在制品物料处理		
			node.put("FShippingTreament", Util.null2String(rs_detail.getString("FShippingTreament")));//出货物料处理				
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}
}
