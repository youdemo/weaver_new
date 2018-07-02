package feilida;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class updateRqid implements Action {
	/**
	 * 将流程requestid赋值给表单字段
	 */
	 public String execute(RequestInfo info) {

	        String workflowID = info.getWorkflowid();//获取工作流程Workflowid的值
	        String requestid = info.getRequestid();//获取requestid的值

	        RecordSet rs = new RecordSet();
	       

	        String sql = "";
	        String tableName = "";
	       
	        sql = " Select tablename From Workflow_bill Where id in (Select formid From workflow_base Where id= " + workflowID + ")";
	        rs.execute(sql);
	        if (rs.next()) {
	            tableName = Util.null2String(rs.getString("tablename"));
	        }

	        if (!"".equals(tableName)) {
	            sql="update "+tableName+" set rqid="+requestid +" where requestid="+requestid;
	            rs.execute(sql);	         
	        } else {
	            return "-1";
	        }
	        return SUCCESS;
	    }
}
