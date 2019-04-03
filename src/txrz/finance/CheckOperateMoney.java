package txrz.finance;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 暂借单人员审批权限开发
 * @author tangj
 *
 */
public class CheckOperateMoney implements Action{

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		User usr = info.getRequestManager().getUser();
		String tableName = "";
		String modetable = "uf_zzdspjmbd";
		String checkmoney = "";
		String fhbs = "1";
		String je = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select je from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			je = Util.null2String(rs.getString("je"));
		}
		if("".equals(je)) {
			je = "0";
		}
		
		sql = "select spjexe from "+modetable+" where spr='"+usr.getUID()+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			checkmoney = Util.null2String(rs.getString("spjexe"));
		}
		if(!"".equals(checkmoney)) {
			if(Util.getFloatValue(je, 0)<=Util.getFloatValue(checkmoney, 0)) {
				fhbs = "0";
			}
		}
		
		sql = "update "+tableName+" set fhbs='"+fhbs+"' where requestid="+requestid;
		rs.executeSql(sql);
		
		return SUCCESS;
	}

}
