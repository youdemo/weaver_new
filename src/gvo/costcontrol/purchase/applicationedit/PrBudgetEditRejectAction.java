package gvo.costcontrol.purchase.applicationedit;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 采购申请修改退回删除中间表数据
 * @author tangjianyong 2018-06-13
 *
 */
public class PrBudgetEditRejectAction implements Action{
	
	public String execute(RequestInfo info){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();	
		String requestid = info.getRequestid();
		log.writeLog("PoBudgetRejectAction------");
		
		String sql = " delete from uf_pr_budget where lcid = "+requestid+" ";
		rs.execute(sql);	
		 
		return SUCCESS;
	}
}
