package zj.certificate;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateCerificateNo extends BaseCronJob{
	public void execute() {
		updateNo();
	}
	
	public void updateNo(){
		RecordSetDataSource rsd = new RecordSetDataSource("MPROD");
		RecordSetDataSource rsd_dt = new RecordSetDataSource("MPROD");
		String realpzh="";
		String pzh="";
		String sql_dt="";
		String sql="select * from ZJ_JOURNAL_SEQ where JOURNAL_DOC_SEQ is not null and journal_status is null";
		rsd.executeSql(sql);
		while(rsd.next()){
			pzh = Util.null2String(rsd.getString("JOURNAL_NAME"));
			realpzh = Util.null2String(rsd.getString("JOURNAL_DOC_SEQ"));
			updateRecordInfo(pzh.replaceAll("OA", ""),realpzh);
			sql_dt = "update ZJ_JOURNAL_SEQ set journal_status='1' where JOURNAL_NAME='"+pzh+"'";
			rsd_dt.executeSql(sql_dt);
		}
		
		
	}
	public void updateRecordInfo(String pzh,String realpzh){
		RecordSet rs = new RecordSet();
		String requestid="";
		String sql="select * from request_fk_record where pch='"+pzh+"' and (hxzt = '' or hxzt is null) ";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("requestid"));
			updateWorkflowInfo(requestid,realpzh);
		}
		sql="update request_fk_record set hxzt='1',hxxx='"+realpzh+"' where pch='"+pzh+"'";
		rs.executeSql(sql);
		
	}
	
	public void updateWorkflowInfo(String requestId,String realpzh){
		RecordSet rs = new RecordSet();
		String tablename="";
		String sql="select c.tablename  from workflow_requestbase a,workflow_base b,workflow_bill c where a.workflowid=b.id and b.formid = c.id  and a.requestid="+requestId;
		rs.executeSql(sql);
		if(rs.next()){
			tablename  = Util.null2String(rs.getString("tablename"));
		}
		sql="update "+tablename+" set pzhm='"+realpzh+"' where requestid="+requestId;
		rs.executeSql(sql);
	}
}
