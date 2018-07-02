package feilida.finance;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class FinanceExpenseFI05 implements Action{
	BaseBean log = new BaseBean();
  
  	public String execute(RequestInfo info){
    this.log.writeLog("FinanceExpenseFI05——————");
    
    String workflowID = info.getWorkflowid();
    String requestid = info.getRequestid();
    
    RecordSet rs = new RecordSet();
    RecordSet rs_dt = new RecordSet();
    
    String tableName = "";
    String tableNamedt = "";
    String tableNamedt2 = "";
    String mainID = "";
    
    String sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + 
    
      workflowID + ")";
    
    rs.execute(sql);
    if (rs.next()) {
      tableName = Util.null2String(rs.getString("tablename"));
    }
    if (!"".equals(tableName))
    {
      tableNamedt = tableName + "_dt1";
      tableNamedt2 = tableName + "_dt2";
      

      sql = "select * from " + tableName + " where requestid=" + requestid;
      rs.execute(sql);
      if (rs.next()) {
        mainID = Util.null2String(rs.getString("ID"));
      }
      sql = " delete from " + tableNamedt2 + " where mainid = " + mainID + " and XZ = 0";
      rs.execute(sql);
      this.log.writeLog("明细删除:" + sql);
    }
    else
    {
      return "-1";
    }
    return "1";
  }
}
