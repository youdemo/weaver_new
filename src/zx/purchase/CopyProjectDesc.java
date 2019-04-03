package zx.purchase;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 统管类 明细2 采购醒目描述选择显示值赋值到手写
 * @author tangj
 *
 */
public class CopyProjectDesc implements Action{

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String sfwtgl = "";//是否为统管类
		String mainid = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			sfwtgl = Util.null2String(rs.getString("sfwtgl"));
			mainid = Util.null2String(rs.getString("id"));
		}
		if("0".equals(sfwtgl)) {
			sql="update "+tableName+"_dt2  set cgxmms=a.cgxmms from uf_cgxmms a where a.id="+tableName+"_dt2.cgxmms1 and "+tableName+"_dt2.mainid="+mainid;
			rs.executeSql(sql);
		}
		return SUCCESS;
	}

}
