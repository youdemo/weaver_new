package feilida.finance;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateZZKMIDAction implements Action{
     /**
      * 当报销类型为资产类报销的时候，报销流程提交后将科目ID更新到总账科目ID上
      */
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String workflow_id = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String sql = "Select tablename,id From Workflow_bill Where id=(";
		sql += "Select formid From workflow_base Where id=" + workflow_id + ")";
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid= " + requestid;
		rs.executeSql(sql);
		String LX="";
		String mainId="";
		if (rs.next()) {
			LX = Util.null2String(rs.getString("LX"));
			mainId = Util.null2String(rs.getString("id"));
		}
		if("1".equals(LX)){
			sql="update "+tableName+"_dt1 set ZZKM1=KMID where mainid="+mainId;
			rs.executeSql(sql);
		}
		
		return SUCCESS;
	}

}
