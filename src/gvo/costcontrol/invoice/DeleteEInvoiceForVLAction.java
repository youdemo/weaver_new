package gvo.costcontrol.invoice;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class DeleteEInvoiceForVLAction  implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id from "+tableName+" where requestid="+requestid;
		
		rs.executeSql(sql);
		if(rs.next()){
			mainID= Util.null2String(rs.getString("id"));
		}
		sql="delete from uf_e_invoice where xglc='"+requestid+"'";
		rs.executeSql(sql);

		for(int i=4;i<=7; i++){ 
			sql="update "+tableName+"_dt"+i+" set dzfp=null where mainid="+mainID;
			rs.executeSql(sql);
		}
		return SUCCESS;
	}
}
