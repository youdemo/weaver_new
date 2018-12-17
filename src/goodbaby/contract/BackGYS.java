package goodbaby.contract;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class BackGYS implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "select * from "+tablename+" where requestid ='"+requestid+"'";
		rs.executeSql(sql);
		while(rs.next()){
			String fkq = Util.null2String(rs.getString("FKQ"));
			String jgkid = Util.null2String(rs.getString("TYWLJGK"));
			String gysbm = Util.null2String(rs.getString("GYSBH"));
			String HTSXRQ = Util.null2String(rs.getString("HTSXRQ"));
			String HTJZRQ = Util.null2String(rs.getString("HTJZRQ"));
			String sl = Util.null2String(rs.getString("CGSL"));
			String HTZJXX = Util.null2String(rs.getString("HTZJXX"));
			String str = "update uf_inquiryForm set YJJG= '"+HTZJXX+"',SXZT = '1',JGSXRQ ='"+HTSXRQ+"',JGJZRQ = '"+HTJZRQ+"',SL ='"+sl+"' where id = '"+jgkid+"' ";
			res.executeSql(str);
			str = "update uf_suppmessForm set FKQ = '"+fkq+"' where GYSBM = '"+gysbm+"' ";
			res.executeSql(str);
		}
		
		return SUCCESS;
	}
	

}
