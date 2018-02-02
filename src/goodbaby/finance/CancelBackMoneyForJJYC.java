package goodbaby.finance;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CancelBackMoneyForJJYC implements Action{
	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="delete from  uf_borrow_back_infp where rqid='"+requestid+"'";
		rs.executeSql(sql);
		
		return SUCCESS;
	}
}
