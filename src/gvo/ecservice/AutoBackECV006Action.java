package gvo.ecservice;

import gvo.ecservice.CreateRequestServiceECStub.AutoBackV0006;
import gvo.ecservice.CreateRequestServiceECStub.AutoBackV0006Response;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AutoBackECV006Action implements Action{

	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String ecrqid2="";//
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,ecrqid2 from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			ecrqid2 = Util.null2String(rs.getString("ecrqid2"));
			
		}
		
		if(!"".equals(ecrqid2)){
			String result ="";
		      try {
				result = doService(ecrqid2);
				if("success".equals(result)){
					deleteRequestInfo(ecrqid2);
				}
			} catch (Exception e) {
				log.writeLog("oa流程"+requestid+" 自动退回EC流程"+ecrqid2+"调用接口失败");
			}
		    log.writeLog("oa流程"+requestid+" 自动退回EC流程"+ecrqid2+"结果:"+result);
		}
		return SUCCESS;
	}
 public String doService(String ecrqid2) throws Exception{
		 
		 CreateRequestServiceECStub crsec= new CreateRequestServiceECStub();
		 AutoBackV0006 crs = new CreateRequestServiceECStub.AutoBackV0006();
		 crs.setRequestid(ecrqid2);
		 AutoBackV0006Response cr= crsec.AutoBackV0006(crs);
		 return cr.getOut();
	 }
 
 public void deleteRequestInfo(String requestid){
	 RecordSetDataSource rsd = new RecordSetDataSource("EC");
	 String sql="";
	 sql="delete workflow_currentoperator where requestid ="+requestid;
	 rsd.executeSql(sql);
	 sql="delete workflow_form where requestid ="+requestid;
	 rsd.executeSql(sql);
	 sql="delete workflow_formdetail where requestid ="+requestid;
	 rsd.executeSql(sql);
	 sql="delete workflow_requestLog where requestid ="+requestid;
	 rsd.executeSql(sql);
	 sql="delete workflow_requestViewLog where id ="+requestid;
	 rsd.executeSql(sql);
	 sql="delete workflow_requestbase where requestid ="+requestid;
	 rsd.executeSql(sql);


 }
}
