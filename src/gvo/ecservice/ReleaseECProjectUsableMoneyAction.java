package gvo.ecservice;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 释放扣除的预算
 * @author tangjianyong
 * @version 1.0
 *
 */
public class ReleaseECProjectUsableMoneyAction implements Action {

	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("EC");
		String tableName = "";
		String mainID = "";
		String tjxmmc = "";
		String bcfke = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id,tjxmmc,bcfke2 from " + tableName + " where requestid="
				+ requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			tjxmmc = Util.null2String(rs.getString("tjxmmc"));
			bcfke = Util.null2String(rs.getString("bcfke2"))
					.replaceAll(",", "");
		}
		if ("".equals(bcfke)) {
			bcfke = "0";
		}

		sql = "update uf_project1 set sylxje=nvl(sylxje,0)+'" + bcfke
				+ "',htfkz=nvl(htfkz,0)-'" + bcfke + "'  where id='" + tjxmmc
				+ "'";
		rsd.executeSql(sql);
		
		return SUCCESS;
	}
}
