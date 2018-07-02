package gvo.costcontrol.purchase.applicationedit;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 采购申请修改操作中间表
 * @author tangj
 *
 */
public class PrBudgetEditAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		log.writeLog("PrBudgetEditAction------");
		String pqNo = "";//明细 采购申请单号1
		String itemid = "";//明细 请求项目
		String xcgje = "";//预算冻结金额
		String tableName = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			pqNo = Util.null2String(rs.getString("prno"));
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			itemid = Util.null2String(rs.getString("itemid"));
			xcgje = Util.null2String(rs.getString("xcgje"));
			if("".equals(xcgje)){
				xcgje = "0";
			}
			if(!"".equals(pqNo)&&!"".equals(itemid)){
				updatePrBudgetTable(requestid,pqNo,itemid,xcgje);
			}
		}
		
		
		
		return SUCCESS;
	}
	
	public void updatePrBudgetTable(String requestid,String cgsqdh,String mxhid,String ysdjje){
		RecordSet rs = new RecordSet();
		String amount="0";
		String cdbm = "";
		String yskm = "";
		String qj = "";
		String type = "";
		String gsdm = "";
		String sql="select 0-nvl(sum(nvl(je,0)),0) as amount from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid='"+mxhid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			amount = Util.null2String(rs.getString("amount"));
		}
	
		sql="select * from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid='"+mxhid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			cdbm = Util.null2String(rs.getString("cdbm"));
			yskm = Util.null2String(rs.getString("yskm"));
			qj = Util.null2String(rs.getString("qj"));
			type = Util.null2String(rs.getString("type"));
			gsdm = Util.null2String(rs.getString("gsdm"));
		}
		if(!"0".equals(amount)){
			sql="insert into uf_pr_budget (lcid,cgsqdh,mxhid,cdbm,yskm,qj,je,type,gsdm)"
					+ " values('"+requestid+ "','"+cgsqdh+"','"+ mxhid+ "','"+ cdbm+ "','"
					+ yskm + "','" + qj + "','" + amount + "','"+type+"','"+gsdm+"') ";
			rs.executeSql(sql);		
		}
		sql="insert into uf_pr_budget (lcid,cgsqdh,mxhid,cdbm,yskm,qj,je,type,gsdm)"
				+ " values('"+requestid+ "','"+cgsqdh+"','"+ mxhid+ "','"+ cdbm+ "','"
				+ yskm + "','" + qj + "','" + ysdjje + "','"+type+"','"+gsdm+"') ";
		rs.executeSql(sql);
		
	}
}
