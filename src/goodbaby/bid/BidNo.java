package goodbaby.bid;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 
 * @author 张瑞坤
 *611
 */
public class BidNo implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet();
		String wlbm = "";
		String sql = "select * from "+tablename+" where requestid ='"+requestid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			wlbm = Util.null2String(rs.getString("WLBM"));	
		}
		String ZBWJBH = "ZBWJ"+wlbm;//投标文件编码
		sql = "update "+tablename+" set ZBWJBH = '"+ZBWJBH+"' where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		return SUCCESS;
	}

}
