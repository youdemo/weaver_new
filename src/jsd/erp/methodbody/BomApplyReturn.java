package jsd.erp.methodbody;

import jsd.erp.util.GetUtil;
import jsd.serviceImp.TriggerStubImp;//回传接口---------
import jsd.status.SetInfo;/////回传日志
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
 * @version 创建时间：2019-5-14 下午2:38:38
 *  加工件BOM申请    回传
 */
public class BomApplyReturn implements Action {
	
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
				head.put("F_BOMId",Util.null2String(rs.getString("F_BOMId")));// 单据编号
				head.put("F_BomType",Util.null2String(rs.getString("F_BomType")));// 	BOM类型			
				head.put("F_ItemId",Util.null2String(rs.getString("F_ItemId")));// 设备编号
				head.put("F_CharacterCode",Util.null2String(rs.getString("F_CharacterCode")));// 特征码
				head.put("F_ModularId",Util.null2String(rs.getString("F_ModularId")));// 模块号
				head.put("F_VersionId",Util.null2String(rs.getString("F_VersionId")));// 版本号
				head.put("F_Remark",Util.null2String(rs.getString("F_Remark")));// 备注
//				head.put("F_Status",Util.null2String(rs.getString("F_Status")));// 文控状态
				
				if(flag.equals("submit")){
					head.put("F_Status", "3");// 状态			
				}else{
					head.put("F_Status", Util.null2String(rs.getString("F_Status")));// 状态
				}
				head.put("FTempNeedReq",Util.null2String(rs.getString("FTempNeedReq")));// 是否下需求
				head.put("F_EnginerApproveStatus",Util.null2String(rs.getString("F_EnginerApproveStatus")));// 工程状态
				head.put("F_CreatorDateTime",Util.null2String(rs.getString("F_CreatorDateTime")));// 创建时间
				head.put("F_CreatorID",Util.null2String(rs.getString("F_CreatorID")));// 创建人工号
				head.put("F_IsSubmit",Util.null2String(rs.getString("F_IsSubmit")));// 是否提交
			//F_CreatorName???????????????????????????/
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
		log.writeLog("JSOn----JST-ENB-001--"+json.toString());
		String result = "";
		try {
			result = ts.serviceImp(json.toString(), "JST-ENB-001");
			log.writeLog("result----JST-ENB-001--"+result);
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
			node.put("F_PartNO", Util.null2String(rs_detail.getString("F_PartNO")));//零件号
			node.put("F_VerNO", Util.null2String(rs_detail.getString("F_VerNO")));//版本号
			node.put("F_Material", Util.null2String(rs_detail.getString("F_Material")));//材质
			node.put("F_SurfaceTreat", Util.null2String(rs_detail.getString("F_SurfaceTreat")));//表面处理工艺
			node.put("F_SingleAmount", Util.null2String(rs_detail.getString("F_SingleAmount")));//单套用量
			node.put("F_Unit", Util.null2String(rs_detail.getString("F_Unit")));//单位
			node.put("F_MSPPartType", Util.null2String(rs_detail.getString("F_MSPPartType")));//零件类型
			node.put("F_Remark", Util.null2String(rs_detail.getString("F_Remark")));//备注
			node.put("F_RelationECN", Util.null2String(rs_detail.getString("F_RelationECN")));//相关联ECN
			node.put("FIsPause", Util.null2String(rs_detail.getString("FIsPause")));//状态
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}

}
