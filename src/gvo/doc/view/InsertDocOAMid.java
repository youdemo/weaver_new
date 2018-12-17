package gvo.doc.view;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertDocOAMid implements Action{

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String billid = "";
		String tableName = "";
		String modeId = getModeId("uf_doc_esb_mid");
		String sql = "insert into uf_doc_esb_mid(rqid,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid) values('"+requestid+"',to_char(sysdate,'yyyy-mm-dd'),'1','0','"+modeId+"')";
		rs.executeSql(sql);
		sql="select id from uf_doc_esb_mid where rqid='"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			billid = Util.null2String(rs.getString("id"));
		}
		if (!"".equals(billid)) {
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(
					Integer.valueOf("1"),
					Util.getIntValue(modeId),
					Integer.valueOf(billid));
		}
		
		return SUCCESS;
	}
	
	public String getModeId(String tableName){
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"+tableName+"'";
		rs.executeSql(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql="select id from modeinfo where  formid="+formid;
		rs.executeSql(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
	
}
