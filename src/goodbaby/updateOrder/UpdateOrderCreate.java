package goodbaby.updateOrder;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2018-11-5 上午11:54:12
 * 类说明
 * 更新创建信息和流程标题
 */
public class UpdateOrderCreate {
	public String updateorde(){
		String workflowid = "";
		String sql = "select * from workflow_requestbase where workflowid='"+workflowid+"' ";
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String aa = "";
		rs.executeSql(sql);
		while(rs.next()){
			String requestid = Util.null2String(rs.getString("requestid"));
			String requestname = Util.null2String(rs.getString("requestname"));
			String requestnamenew = Util.null2String(rs.getString("requestnamenew"));
			String creater = Util.null2String(rs.getString("creater"));
			String str = "select lastname from hrmresource where id = '"+creater+"'";
			String hrname = "";
			String Cname = "";
			res.executeSql(str);
			if(res.next()){
				hrname = Util.null2String(res.getString("lastname"));
			}
			str = "select name from crm_customerinfo where id = id = '"+creater+"'";
			res.executeSql(str);
			if(res.next()){
				Cname = Util.null2String(res.getString("name"));
			}
			requestname.replaceAll(hrname,Cname);
			requestnamenew.replaceAll(Cname, Cname);
			str = "update workflow_requestbase set requestname = '"+requestname+"',requestnamenew = '"+requestnamenew+"',creatertype='1' where requestid='"+requestid+"' ";
			aa ="---------"+aa+str+"------";
			res.executeSql(str);
			str = "update workflow_requestlog set operatortype = '1' where requestid = '"+requestid+"' and nodeid ='2218'";//z 2218---c1412
			//log.writeLog(str);
			aa = aa+str+"-------";
			res.executeSql(str);
			
		}
		
		
		return aa;
		
	}
	
}
