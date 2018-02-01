package zj.certificate;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.system.SysRemindWorkflow;

public class InsertRequestFKRecord {
	public void insertInfo(String requestids) {
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		SysRemindWorkflow remindwf=new SysRemindWorkflow();
		String requestid[] = requestids.split(",");
		int nextSeq = 0;
		String sql = "select isnull(MAX(fkseq),0)+1 as next from request_fk_record";
		rs.executeSql(sql);
		if (rs.next()) {
			nextSeq = rs.getInt("next");
		}
		int count =0;
		for (int i = 0; i < requestid.length; i++) {
			String rqid = requestid[i];
			if ("".equals(rqid) || "0".equals(rqid)) {
				continue;
			}
			count =count+1;
			int seq = i + 1;
			sql = "insert into request_fk_record(requestid,seq,fkseq,fkrq,fksj,fklx) values('"
					+ rqid + "','" + seq + "','" + nextSeq + "','"+nowDate+"','"+nowTime+"','0')";
			rs.executeSql(sql);
			String tableName = "";
			String requestName = "";
			sql = "select c.tablename,a.requestname from workflow_requestbase a,workflow_base b,workflow_bill c where a.workflowid=b.id and b.formid = c.id  and a.requestid="
					+ rqid;
			rs.executeSql(sql);
			if (rs.next()) {
				tableName = Util.null2String(rs.getString("tablename"));
				requestName = Util.null2String(rs.getString("requestName"));
			}
			String sqr="";
			
			sql="select sqr  from v_fukuan_requestFlow where requestid='"+rqid+"' ";
			rs.executeSql(sql);
			if(rs.next()){
				sqr = Util.null2String(rs.getString("sqr"));
			}
			sql = "update " + tableName + " set yfk='1' where requestid="
					+ rqid;
			rs.executeSql(sql);
			 try {
				remindwf.make("流程"+requestName+",已付款", 0, 0, 0, 0, 1, sqr, "流程"+requestName+",已付款",Integer.valueOf(rqid));
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(count > 1){
			sql="update request_fk_record set fklx='1' where fkseq='"+nextSeq+"'";
			rs.executeSql(sql);
		}
	}
}
