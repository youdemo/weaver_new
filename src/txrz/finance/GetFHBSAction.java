package txrz.finance;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class GetFHBSAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String fylx = "";
		String fhbs = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select fylx from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			fylx = Util.null2String(rs.getString("fylx"));
		}
		sql="select fhbs from uf_fylxkmdz where ','+convert(varchar(2000),fylx)+',' like '%,"+fylx+",%'";
		rs.executeSql(sql);
		if(rs.next()) {
			fhbs = Util.null2String(rs.getString("fhbs"));
		}
		if(!"".equals(fhbs)) {
			sql = "update "+tableName+" set hqfhbs='"+fhbs+"' where requestid="+requestid;
			rs.executeSql(sql);
		}
		return SUCCESS;
	}

}
