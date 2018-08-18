package gvo.costcontrol.yld;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateItemNoForcgsqAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String workflowID = info.getWorkflowid();
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
		sql="select *from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID= Util.null2String(rs.getString("id")); 
			
		}
		sql="update "+tableName+"_dt1 a set (RSPOS)=(  select RSPOS from(select rownum  as RSPOS ,id from (select id from "+tableName+"_dt1 where mainid="+mainID+" order by id asc))  b where a.id=b.id) where a.mainid="+mainID+"";
		log.writeLog("UpdateItemNoForcgsqAction sql"+sql);
		rs.executeSql(sql);
		sql="update "+tableName+" set lcid='"+requestid+"' where requestid="+requestid;
		rs.executeSql(sql);
		return SUCCESS;
	}

}
