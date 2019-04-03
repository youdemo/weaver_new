package lx.budget;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 付款申请单（行政物资、IT物资、外修） 冻结预算
 * @author tangj
 *
 */
public class FreezeBudgetForFKSQDXZWZ implements Action{
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String mainid = "";
		String cbzxbm = "";
		String xmbm = "";
		String kmbm = "";
		String sqyf = "";
		String bcsqje ="";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid= " + requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from "+tableName+"_dt4 where mainid = "+mainid;
		rs.executeSql(sql);
		while(rs.next()) {
			cbzxbm = Util.null2String(rs.getString("cbzxbm"));
			xmbm = Util.null2String(rs.getString("xmbm"));
			kmbm = Util.null2String(rs.getString("kmbm"));
			sqyf = Util.null2String(rs.getString("sqyf"));
			bcsqje = Util.null2String(rs.getString("bcfkjecny"));
			if("".equals(bcsqje)) {
				bcsqje = "0";
			}
			sqyf = sqyf.substring(0, 7);
			UpdateMid(cbzxbm,xmbm,kmbm,sqyf,bcsqje);
		}
		return SUCCESS;
	}
	
	public void UpdateMid (String cbzxbm,String xmbm,String kmbm,String sqyf,String bcsqje) {
		RecordSet rs = new RecordSet();
		String billid ="";
		String sql = "select id from uf_apl_budget_mid where cbzxbm='"+cbzxbm+"' and xmbm='"+xmbm+"' and kmbm='"+kmbm+"' and sqyf='"+sqyf+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			billid = Util.null2String(rs.getString("id"));
		}
		if(!"".equals(billid)) {
			sql= "update uf_apl_budget_mid set djje=djje+"+bcsqje+" where id="+billid;
			rs.executeSql(sql);				
		}
	}
}
