package hsproject.impl;

import hsproject.util.InsertUtil;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class editCommonField implements Action{

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String tableName = "";
		String fieldname = "";
		String description = "";
		String showname = "";
		String groupinfo = "";
		String isused = "";
		String ismust = "";
		String dsporder = "";
		String isedit = "";
		String isreadonly = "";
		String fieldid="";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			fieldname=Util.null2String(rs.getString("fieldname"));		
			description =Util.null2String(rs.getString("description"));	
			showname =Util.null2String(rs.getString("showname"));
			groupinfo =Util.null2String(rs.getString("groupinfo"));
			isused =Util.null2String(rs.getString("isused"));
			ismust =Util.null2String(rs.getString("ismust"));
			dsporder =Util.null2String(rs.getString("dsporder"));
			isedit =Util.null2String(rs.getString("isedit"));
			isreadonly =Util.null2String(rs.getString("isreadonly"));
			
		}
		sql="select * from uf_project_field where fieldname='"+fieldname+"' and iscommon='0'";
		rs.executeSql(sql);
		while(rs.next()){
			fieldid = Util.null2String(rs.getString("id"));
			Map<String, String> mapStr = new HashMap<String, String>();
			mapStr.put("description", description);
			mapStr.put("showname", showname);
			mapStr.put("groupinfo", groupinfo);
			mapStr.put("isused", isused);
			mapStr.put("ismust", ismust);
			mapStr.put("dsporder", dsporder);
			mapStr.put("isedit", isedit);
			mapStr.put("isreadonly", isreadonly);
			iu.updateGen(mapStr, "uf_project_field", "id", fieldid);
		}
		return SUCCESS;
	}
}
