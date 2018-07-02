package gvo.ecservice;

import gvo.ecservice.CreateRequestServiceECStub.AutoSubmitV0006;
import gvo.ecservice.CreateRequestServiceECStub.AutoSubmitV0006Response;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class AutoSubmitECV006Action implements Action{

	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("EC");
		String tableName = "";
		String mainID = "";
		String ecrqid2="";//
		String requestname="";
		String requestmark="";
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
		sql="select requestname,requestmark from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			requestname = Util.null2String(rs.getString("requestname"));
			requestmark = Util.null2String(rs.getString("requestmark"));
		}
		if(!"".equals(ecrqid2)){
			String sql_ec="update workflow_requestbase set requestname='"+requestname+"',requestmark='"+requestmark+"' where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			sql_ec="update formtable_main_2 set sfyxtj='0' where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			String result ="";
		      try {
				result = doService(ecrqid2);
			} catch (Exception e) {
				sql_ec="update formtable_main_2 set sfyxtj='1' where requestid="+ecrqid2;
				rsd.executeSql(sql_ec);
				log.writeLog("oa流程"+requestid+" 自动提交EC流程"+ecrqid2+"调用接口失败");
			}
		    log.writeLog("oa流程"+requestid+" 自动提交EC流程"+ecrqid2+"结果:"+result);
		}
		return SUCCESS;
	}
 public String doService(String ecrqid2) throws Exception{
		 
		 CreateRequestServiceECStub crsec= new CreateRequestServiceECStub();
		 AutoSubmitV0006 crs = new CreateRequestServiceECStub.AutoSubmitV0006();
		 crs.setRequestid(ecrqid2);
		 AutoSubmitV0006Response cr= crsec.AutoSubmitV0006(crs);
		 return cr.getOut();
	 }
}
