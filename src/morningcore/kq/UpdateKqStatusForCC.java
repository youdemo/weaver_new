package morningcore.kq;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 出差
 * @author tangj
 *
 */
public class UpdateKqStatusForCC implements Action{

	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String fqr = "";
		String sqqsrq = "";
		String sqjsrq = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select sqr,ksrq,jsrq from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			fqr = Util.null2String(rs.getString("sqr"));
			sqqsrq = Util.null2String(rs.getString("ksrq"));
			sqjsrq = Util.null2String(rs.getString("jsrq"));
		}
		if(!"".equals(fqr) && !"".equals(sqqsrq) && !"".equals(sqjsrq)) {
			sql = "select * from (select convert(varchar(10),dateadd(dd,number,'"+sqqsrq+"'),120) as day from master..spt_values where type='P' and dateadd(dd,number,'"+sqqsrq+"')<=dateadd(dd,0,'"+sqjsrq+"')) a";
			rs.execute(sql);
			while(rs.next()){
				String kqdate = Util.null2String(rs.getString("day"));
				rs_dt.execute("exec calEmpDayAtten "+fqr+",'"+kqdate+"',1");
			}
		}
		
		return SUCCESS;
	}
}
