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
 * @version 创建时间：2019-6-13 上午6:51:50
 * 快递回传   未完成
 */
public class ExpressReturn  implements Action {
	
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
				
				head.put("Orderid",Util.null2String(rs.getString("Orderid")));// []		ERP单号	
				head.put("Mailno",Util.null2String(rs.getString("Mailno")));// 	[]		顺丰单号				
				head.put("PayMethod",Util.null2String(rs.getString("PayMethod")));// []		付款方式	
				head.put("ExpressType",Util.null2String(rs.getString("ExpressType")));// []		快递类型	
				head.put("Remark",Util.null2String(rs.getString("Remark")));// []		备注信息	
				head.put("JProvince",Util.null2String(rs.getString("JProvince")));// []		寄件省	
				head.put("JCity",Util.null2String(rs.getString("JCity")));// []		寄件市
				head.put("JCounty",Util.null2String(rs.getString("JCounty")));// 	[]		寄件市/区/县	
				head.put("JCompany",Util.null2String(rs.getString("JCompany")));//[]		寄件公司
				head.put("JContact",Util.null2String(rs.getString("JContact")));// 	[]		寄件人姓名-------------------		
				head.put("JTel",Util.null2String(rs.getString("JTel")));// []		寄件人电话	
				head.put("JMobile",Util.null2String(rs.getString("JMobile")));// []		寄件人手机	
				head.put("JAddress",Util.null2String(rs.getString("JAddress")));// []		寄件人乡镇街道	
				head.put("DProvince",Util.null2String(rs.getString("DProvince")));// []		收件省	
				head.put("DCity",Util.null2String(rs.getString("DCity")));// []		收件市	
				head.put("DCounty",Util.null2String(rs.getString("DCounty")));// []		收件市/区/县	
				head.put("DCompany",Util.null2String(rs.getString("DCompany")));// 	[]		收件公司			
				head.put("DContact",Util.null2String(rs.getString("DContact")));// []		收件人姓名-------------------------------
				head.put("DTel",Util.null2String(rs.getString("DTel")));//[]		收件人电话	
				head.put("DMobile",Util.null2String(rs.getString("DMobile")));// []		收件人手机
				head.put("DAddress",Util.null2String(rs.getString("DAddress")));// 	[]		收件人地址	
				head.put("CargoLength",Util.null2String(rs.getString("CargoLength")));//[]		包裹长度	
				head.put("CargoWidth",Util.null2String(rs.getString("CargoWidth")));// []		包裹宽	
				head.put("CargoHeight",Util.null2String(rs.getString("CargoHeight")));// 	[]		包裹高				
				head.put("CargoTotalWeight",Util.null2String(rs.getString("CargoTotalWeight")));//[]		包裹总重	
				head.put("Volume",Util.null2String(rs.getString("Volume")));// []		包裹体积	
				head.put("Name",Util.null2String(rs.getString("Name")));// []		货物	
				head.put("Count",Util.null2String(rs.getString("Count")));//[]		货物数量	
				head.put("Unit",Util.null2String(rs.getString("Unit")));// []		货物单位	
//				if(flag.equals("submit")){
//					head.put("F_Status", "3");// 状态			
//				}else{
//					head.put("F_Status", Util.null2String(rs.getString("F_Status")));// 状态
//				}
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
			node.put("FEmpNoName", Util.null2String(rs_detail.getString("FEmpNoName")));//[]		申请人	----------------
			node.put("FDeptName", Util.null2String(rs_detail.getString("FDeptName")));//[]		申请人部门-----------	
			node.put("FCreateDateTime", Util.null2String(rs_detail.getString("FCreateDateTime")));//[]		创建时间	
			node.put("FStatus", Util.null2String(rs_detail.getString("FStatus")));//[]		状态	
			node.put("FRequestXML", Util.null2String(rs_detail.getString("FRequestXML")));//[]		下单请求xml
			node.put("FResponseXML", Util.null2String(rs_detail.getString("FResponseXML")));//[]		下单响应xml	
			node.put("FSalesID", Util.null2String(rs_detail.getString("FSalesID")));//[]		制造单号	
			node.put("FArriveTime", Util.null2String(rs_detail.getString("FArriveTime")));//[]		预计送达日期
			node.put("FOrderConfirmReqXML", Util.null2String(rs_detail.getString("FOrderConfirmReqXML")));//[]		订单取消请求xml	
			node.put("FOrderConfirmResXML", Util.null2String(rs_detail.getString("FOrderConfirmResXML")));//[]		订单取消响应xml
			node.put("IsDocall", Util.null2String(rs_detail.getString("IsDocall")));//[]		是否通知快递且1小时寄件 	
			node.put("Sendstarttime", Util.null2String(rs_detail.getString("Sendstarttime")));//[]		上门取件开始时间	
			detail1.put(node);
		}
		DT.put("DT1", detail1);
		return DT;
	}

}
