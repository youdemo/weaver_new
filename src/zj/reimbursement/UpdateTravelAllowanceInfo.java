package zj.reimbursement;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateTravelAllowanceInfo implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestId = info.getRequestid();
		String workflowId = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainId = "";
		String zbid = "";
		String mxid = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowId
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestId;
		rs.executeSql(sql);
		if (rs.next()) {
			mainId = Util.null2String(rs.getString("id"));
			zbid = Util.null2String(rs.getString("zbid"));
			mxid = Util.null2String(rs.getString("mxid"));
		}
		sql="update formtable_main_30_dt1 set sfntj='0' where id="+mxid;
		rs.executeSql(sql);
		
		return SUCCESS;
	}

}
