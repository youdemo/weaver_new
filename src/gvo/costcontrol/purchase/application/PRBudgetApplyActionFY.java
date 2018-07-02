package gvo.costcontrol.purchase.application;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 费用类采购申请插入中间表数据
 * @author tangjianyong 2018-06-13
 *
 */
public class PRBudgetApplyActionFY implements Action {

	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		String zjbbj = "";//中间表标识
		String gsdm = "";
		log.writeLog("PRBudgetApplyActionFY------");

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
			mainID = Util.null2String(rs.getString("ID"));
			zjbbj = Util.null2String(rs.getString("zjbbj"));
			gsdm = Util.null2String(rs.getString("gsdm"));
		}
		sql="update "+tableName+"_dt1 a set (xm)=(  select xm from(select rownum*10  as xm ,id from (select id from "+tableName+"_dt1 where mainid="+mainID+" order by id asc))  b where a.id=b.id) where a.mainid="+mainID+"";
		rs.executeSql(sql);
		sql = "select * from " + tableName + "_dt1 where mainid=" + mainID;
		rs.execute(sql);
		while (rs.next()) {
			String xm = Util.null2String(rs.getString("xm"));
			String fycd = Util.null2String(rs.getString("fycd"));
			String yskm = Util.null2String(rs.getString("yskm"));
			String fyrq = Util.null2String(rs.getString("fyqj"));
			String je = Util.null2String(rs.getString("je"));

			String sql_dt = " insert into uf_pr_budget (lcid,mxhid,cdbm,yskm,qj,je,type,gsdm)"
					+ " values('"+requestid+ "','"+ xm+ "','"+ fycd+ "','"
					+ yskm + "','" + fyrq + "','" + je + "','"+zjbbj+"','"+gsdm+"') ";
			rs_dt.execute(sql_dt);
		}

		return SUCCESS;
	}
}
