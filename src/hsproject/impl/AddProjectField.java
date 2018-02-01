package hsproject.impl;

import hsproject.dao.AlterTableFieild;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 增加项目自定义字段
 * @author tangjianyong
 *
 */
public class AddProjectField implements Action {

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String alterTableName = "hs_project_fielddata";
		String fieldname = "";
		String fieldtype = "";
		String texttype = "";
		String textlength = "";
		String floatdigit = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			fieldtype = Util.null2String(rs.getString("fieldtype"));
			fieldname=Util.null2String(rs.getString("fieldname"));
			texttype = Util.null2String(rs.getString("texttype"));
			textlength = Util.null2String(rs.getString("textlength"));
			floatdigit = Util.null2String(rs.getString("floatdigit"));
		}
		AlterTableFieild atf = new AlterTableFieild();
		atf.addTableField(alterTableName, fieldname, fieldtype, texttype, textlength, String.valueOf(Util.getIntValue(floatdigit,-1)+1));
		
		
		return SUCCESS;
	}
	
	

}
