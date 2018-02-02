package goodbaby.finance;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertBorrowInfo implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String sqr = "";
		String jklx = "";
		String sqjedx = "";
		String tableName = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			sqr = Util.null2String(rs.getString("sqr"));
			jklx = Util.null2String(rs.getString("jklx"));
			sqjedx = Util.null2String(rs.getString("sqjedx")).replaceAll(",", "");
		}
		if("".equals(sqjedx)){
			sqjedx = "0";
		}
		sql="insert into uf_borrow_back_infp(rqid,sqr,rq,type,je,jklx,status) " +
				"values('"+requestid+"','"+sqr+"','"+nowDate+"','0','"+sqjedx+"','"+jklx+"','1')";
		rs.execute(sql);
		return SUCCESS;
	}

}
