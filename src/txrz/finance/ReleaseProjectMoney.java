package txrz.finance;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ReleaseProjectMoney implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String xzxm = "";
		String je = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id,xzxm,je from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			xzxm = Util.null2String(rs.getString("xzxm"));
			je = Util.null2String(rs.getString("je"));
		}
		if("".equals(je)) {
			je = "0";
		}
		if("".equals(xzxm)) {
			return SUCCESS;
		}
		
		sql = "update uf_xmk set djzje = isnull(djzje,0)-"+je+",syje=isnull(syje,0)+"+je+" where id="+xzxm;
		rs.executeSql(sql);		 	
		
		return SUCCESS;
	}

}
