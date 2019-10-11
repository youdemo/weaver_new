package morningcore.jx;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateJxMxidAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String mainid = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			mainid = Util.null2String(rs.getString("id"));			
		}
		sql = "update "+tableName+"_dt1 set mxid=convert(varchar(20),id)+'_1'+'"+requestid+"' where mainid="+mainid;
		rs.execute(sql);
		sql = "update "+tableName+"_dt2 set mxid=convert(varchar(20),id)+'_2'+'"+requestid+"' where mainid="+mainid;
		rs.execute(sql);
		return SUCCESS;
	}

}
