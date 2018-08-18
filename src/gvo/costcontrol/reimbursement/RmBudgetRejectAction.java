package gvo.costcontrol.reimbursement;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 现金购报销退回删除中间表数据
 * @author tangjianyong 2018-08-09
 *
 */
public class RmBudgetRejectAction implements Action{
	
	public String execute(RequestInfo info){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();	
		String requestid = info.getRequestid();
		log.writeLog("RmBudgetRejectAction------");
		
		String sql = " delete from uf_pr_budget where lcid = "+requestid+" ";
		rs.execute(sql);	
		 
		return SUCCESS; 
	}
}
