package gvo.costcontrol.reimbursement;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateContractMoney implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String tableName = "";
		String hth = "";
		String zfje = "";
		String sql="";
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			hth =Util.null2String(rs.getString("hth"));
			zfje =Util.null2String(rs.getString("zfje"));
		}
		if(!"".equals(hth)){
			if("".equals(zfje)){
				zfje = "0";
			}
			sql="update uf_gvocontractinfo set ljyfk=nvl(ljyfk,0)+"+zfje+" where id="+hth;
			rs.executeSql(sql);
		}
		return SUCCESS;
	}

}
