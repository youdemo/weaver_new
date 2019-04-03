package txrz.finance;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class FreezeProjectMoney implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid(); 
		RecordSet rs = new RecordSet();
		String tableName = "";
		String xzxm = "";
		String je = "";
		String zje = "";
		String djzje = "";
		String syje = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id,xzxm,je from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			xzxm = Util.null2String(rs.getString("xzxm"));
			je = Util.null2String(rs.getString("je"));
		}
		if("".equals(je)) {
			je = "0";
		}
		if("".equals(xzxm)) {
			return SUCCESS;
		}
		sql = "select zje,djzje,syje from uf_xmk where id="+xzxm;
		rs.executeSql(sql);
		if(rs.next()) {
			zje = Util.null2String(rs.getString("zje"));
			djzje = Util.null2String(rs.getString("djzje"));
			syje = Util.null2String(rs.getString("syje"));
		}
		if("".equals(syje)) {
			syje = "0";
		}
		if(Util.getFloatValue(syje)<Util.getFloatValue(je)) {
			info.getRequestManager().setMessageid(System.currentTimeMillis()+"");            
			info.getRequestManager().setMessagecontent("项目剩余金额不足，请联系管理员");
			return SUCCESS;
		}else {
			sql = "update uf_xmk set djzje = isnull(djzje,0)+"+je+",syje=isnull(syje,0)-"+je+" where id="+xzxm;
			rs.executeSql(sql);			
		}
		return SUCCESS;
	}

}
