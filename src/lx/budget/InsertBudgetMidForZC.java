package lx.budget;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 采购申请单（资产）
 * @author tangj
 *
 */
public class InsertBudgetMidForZC implements Action{

	@Override
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
		sql = "select * from "+tableName+"_dt1 where mainid = "+mainid;
		rs.executeSql(sql);
		while(rs.next()) {
			cbzxbm = Util.null2String(rs.getString("cbzxbm"));
			xmbm = Util.null2String(rs.getString("xmbm"));
			kmbm = Util.null2String(rs.getString("yszch"));
			sqyf = Util.null2String(rs.getString("ysqj"));
			bcsqje = Util.null2String(rs.getString("bcsqje"));
			if("".equals(bcsqje)) {
				bcsqje = "0";
			}
			sqyf = sqyf.substring(0, 7);
			UpdateInsertMid(cbzxbm,xmbm,kmbm,sqyf,bcsqje);
		}
		return SUCCESS;
	}
	
	public void UpdateInsertMid (String cbzxbm,String xmbm,String kmbm,String sqyf,String bcsqje) {
		RecordSet rs = new RecordSet();
		int count = 0;
		String sql = "select count(1) as count from uf_apl_budget_mid where cbzxbm='"+cbzxbm+"' and xmbm='"+xmbm+"' and kmbm='"+kmbm+"' and sqyf='"+sqyf+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			count = rs.getInt("count");
		}
		if(count == 0) {
			sql = "insert into uf_apl_budget_mid(cbzxbm,xmbm,kmbm,sqyf,sqje,yyje,djje) values('"+cbzxbm+"','"+xmbm+"','"+kmbm+"','"+sqyf+"','"+bcsqje+"','0','0')";
			rs.executeSql(sql);
		}else {
			sql= "update uf_apl_budget_mid set sqje=sqje+"+bcsqje+" where cbzxbm='"+cbzxbm+"' and xmbm='"+xmbm+"' and kmbm='"+kmbm+"' and sqyf='"+sqyf+"'";
			rs.executeSql(sql);				
		}
	}
}
