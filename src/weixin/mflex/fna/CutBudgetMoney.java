package weixin.mflex.fna;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CutBudgetMoney implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String bhszcb = "";
		String ysbm = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			bhszcb = Util.null2String(rs.getString("bhszcb"));
			ysbm = Util.null2String(rs.getString("ysbm"));
		}
		double usableamt =0.00;
		sql="select nvl(syje,0)-'"+bhszcb+"' as usableamt from uf_BudgetNumber where id='"+ysbm+"'";
		rs.executeSql(sql);
		if(rs.next()){
			usableamt = rs.getDouble("usableamt");
		}
		if(usableamt<0){
			info.getRequestManager().setMessageid(System.currentTimeMillis()+"");            
			info.getRequestManager().setMessagecontent("可用预算不够，请联系管理员");
			return SUCCESS;
		}
		sql="update uf_BudgetNumber set syje=syje-'"+bhszcb+"'  where id='"+ysbm+"'";
		rs.executeSql(sql);
		sql="update uf_BudgetNumber set yyje=nvl(budgetje,0)-nvl(syje,0) where id='"+ysbm+"'";
		rs.executeSql(sql);
		return SUCCESS;
	}

}
