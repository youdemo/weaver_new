package jsd.ape.re;

import jsd.ape.serviceimp.RewriteStatusStubImp;
import jsd.erp.util.GetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-7 上午9:29:27
 * 类说明
 */
public class ReReturn  implements Action {
	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String workflowid = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		int node = info.getRequestManager().getNodeid();
//		User user = new User();
		//{"DocNo":"123","IsSuccess":ES,"ReturnMsg":"test","VerifyDate":"2019-02-01"}
		GetUtil gu = new GetUtil();
		RewriteStatusStubImp st = new RewriteStatusStubImp();
		JSONObject json =new JSONObject();
		String tablename = info.getRequestManager().getBillTableName();
		String flag = info.getRequestManager().getSrc();//当前操作类型 submit:提交/reject:退回    
		String sql = "select DocNo from "+tablename +" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		try {
			if(rs.next()){
				json.put("DocNo", Util.null2String(rs.getString("DocNo")));//订单编号
			}
			sql = "select remark from workflow_requestlog where requestid='"+requestid+"' and workflowid='"+workflowid+"' and nodeid='"+node+"'";
			log.writeLog("sql-----"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				String remark = Util.null2String(rs.getString("remark"));
				json.put("ReturnMsg", gu.delHTMLTag(remark));//审批意见
			}
			if("submit".equals(flag)){//提交
				json.put("IsSuccess", "S");//是否审核成功 bool类型
			}else{//退回
				json.put("IsSuccess", "E");//是否审核成功 bool类型	
			}
			json.put("VerifyDate", gu.getNowdate());//201910101 审核日期 时分秒
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String res = st.returnUST(json.toString(), "APE-RE-001");
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