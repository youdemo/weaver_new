package morningcore.jx;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class TransUtil {
	public String getFlowHref(String ryid,String khzq) {
		StringBuffer result = new StringBuffer();
		RecordSet rs = new RecordSet();
		String sql = "";
		String flag = "";
		if(!"".equals(ryid) && !"".equals(khzq)) {
			String kh[] = khzq.split("_");
			String year = kh[0];
			String zq = kh[1];
			sql = "select a.requestid,a.bdbh from formtable_main_51 a,workflow_requestbase b where a.requestId=b.requestid and b.currentnodetype=3  and khzqnf='"+year+"' and khzq='"+zq+"' and fqr='"+ryid+"'";
			rs.executeSql(sql);
			while(rs.next()) {
				String requestid = Util.null2String(rs.getString("requestid"));
				String bdbh = Util.null2String(rs.getString("bdbh"));				
				result.append(flag);
				result.append("<a href=\"/workflow/request/ViewRequest.jsp?requestid="+requestid+"\" target=\"_blank\">"+bdbh+"</a>");
				flag = "<br/>";
			}
		}
		return result.toString();
	}
}
