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
 * @version 创建时间：2019-6-7 下午10:38:38
 *  加工件分发 回传
 */
public class WorkpieceDistributeReturn   implements Action {
	
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
				head.put("FPOID",Util.null2String(rs.getString("FPOID")));// 采购单号		
				head.put("FOrderDate",Util.null2String(rs.getString("FOrderDate")));//单据日期		
				head.put("FCreateDateTime",Util.null2String(rs.getString("FCreateDateTime")));// 订购日期			
				head.put("FPOType",Util.null2String(rs.getString("FPOType")));// 采购类型					
				head.put("FPoReqType",Util.null2String(rs.getString("FPoReqType")));// 需求类型				
				head.put("Fremark",Util.null2String(rs.getString("Fremark")));// 备注				
//				head.put("Fstatus",Util.null2String(rs.getString("Fstatus")));// 状态	
				if(flag.equals("submit")){
					head.put("FStatus", "3");// 状态			
				}else{
					head.put("FStatus", Util.null2String(rs.getString("FStatus")));// 状态
				}
				head.put("FCreatorID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FCreatorID"))+""));// 填单人
				head.put("FBuyerID",gu.getFieldVal("hrmresource", "workcode", "id",Util.null2String(rs.getString("FBuyerID"))+""));// 采购员
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
		log.writeLog("JSOn----JST-ENP-001--"+json.toString());
		String result = "";
		try {
			result = ts.serviceImp(json.toString(), "JST-ENP-001");
			log.writeLog("result----JST-ENP-001--"+result);
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
			JSONObject  node = new JSONObject();	
			node.put("FSupplierID", Util.null2String(rs_detail.getString("FSupplierID")));//厂商编号			
			node.put("FSupplierName", Util.null2String(rs_detail.getString("FSupplierName")));//厂商名称		
			node.put("FSupplierPrice", Util.null2String(rs_detail.getString("FSupplierPrice")));//供应商价格		
			node.put("FQuoterPrice", Util.null2String(rs_detail.getString("FQuoterPrice")));//核价员价格		
			node.put("Fprice", Util.null2String(rs_detail.getString("Fprice")));//杰士德核价		
			node.put("Fmoney", Util.null2String(rs_detail.getString("Fmoney")));//核价总金额			
			node.put("FIsPrice", Util.null2String(rs_detail.getString("FIsPrice")));//通知供应商状态			
			node.put("FSalesID", Util.null2String(rs_detail.getString("FSalesID")));//制造单号		
			node.put("FProjectDesc", Util.null2String(rs_detail.getString("FProjectDesc")));//项目名称		
			node.put("FPartNO", Util.null2String(rs_detail.getString("FPartNO")));//零件号			
			node.put("Fver", Util.null2String(rs_detail.getString("Fver")));//版本号					
			node.put("FModularId", Util.null2String(rs_detail.getString("FModularId")));//模块号			
			node.put("FReqQty", Util.null2String(rs_detail.getString("FReqQty")));//需求用量			
			node.put("Fqty", Util.null2String(rs_detail.getString("Fqty")));//数量				
			node.put("FDeliveryDate", Util.null2String(rs_detail.getString("FDeliveryDate")));//交期		
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}
}
