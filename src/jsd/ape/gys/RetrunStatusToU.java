package jsd.ape.gys;

import org.json.JSONException;
import org.json.JSONObject;

import jsd.ape.serviceimp.RewriteStatusStubImp;
import jsd.ape.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
//import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-17 下午2:30:46
 * 类说明
 */
public class RetrunStatusToU implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String workflowid = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		int node = info.getRequestManager().getNodeid();
//		User user = new User();
		GetUtil gu = new GetUtil();
		RewriteStatusStubImp st = new RewriteStatusStubImp();
		JSONObject json =new JSONObject();
		String tablename = info.getRequestManager().getBillTableName();
		String flag = info.getRequestManager().getSrc();//当前操作类型 submit:提交/reject:退回   
		String remark = "";
		String sql = "select Code from "+tablename +" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		try {
			if(rs.next()){
				json.put("DocNo", Util.null2String(rs.getString("Code")));//订单编号
			}
			sql = "select remark from workflow_requestlog where requestid='"+requestid+"' and workflowid='"+workflowid+"' and nodeid='"+node+"'";
			log.writeLog("sql-----"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				remark = Util.null2String(rs.getString("remark"));
			}
			if("submit".equals(flag)){//提交
				json.put("Msg", gu.delHTMLTag(remark));//审批意见
				json.put("DealTime", gu.getNowDatet());//201910101 审核日期 时分秒
			}else{//退回
				json.put("IsSuccess", "E");//是否审核成功 bool类型	
				json.put("ReturnMsg", gu.delHTMLTag(remark));//审批意见
				json.put("VerifyDate", gu.getNowDatet());//201910101 审核日期 时分秒
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = "";
		if("submit".equals(flag)){//提交
			res = st.returnUStue(json.toString(), "APE-GYS-001");
		}else{//退回 结束
			res = st.returnUST(json.toString(), "APE-GYS-001");
		}
		try {
			JSONObject retJson = new JSONObject(res);
			String return_type = retJson.getString("return_type");
			String return_message = retJson.getString("return_message");		
			log.writeLog("return_type----------"+return_type+"-----return_message---"+return_message);
			if("E".equals(return_type)){
				info.getRequestManager().setMessagecontent(return_message);    
				info.getRequestManager().setMessageid("错误信息提示");
				return SUCCESS ;//
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			log.writeLog("retJson解析错误----"+res);
		}
		return SUCCESS;
	}

}
