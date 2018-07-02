package ego.peixun;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class insertPXRecord implements Action{
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String mainid = "";
		String Trainingcategory = "";//培训目录
		String Name = "";//名称
		String Dept = "";//部门
		String GH = "";//工号
		String billid = "";
		String sql_dt = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
			Trainingcategory = Util.null2String(rs.getString("Trainingcategory"));
		}
		sql="delete from uf_PXJLB where Trainingcategory='"+Trainingcategory+"'";
		rs.executeSql(sql);
		sql="insert into uf_PXJLB(requestid,Trainingcategory) values('"+Trainingcategory+"','"+requestid+"')";
		rs.executeSql(sql);
		sql="select id from uf_PXJLB where Trainingcategory='"+Trainingcategory+"'";
		rs.executeSql(sql);
		if(rs.next()){
			billid = Util.null2String(rs.getString("id"));
		}
		
		sql="select * from "+tableName+"_dt3 where mainid="+mainid;
		rs.executeSql(sql);
		while(rs.next()){
			Name = Util.null2String(rs.getString("name"));
			Dept = Util.null2String(rs.getString("Dept"));
			GH = Util.null2String(rs.getString("GH"));
			sql_dt="insert into uf_PXJLB_dt1(mainid,Name,Dept,GH) values('"+billid+"','"+Name+"','"+Dept+"','"+GH+"')";
			rs_dt.executeSql(sql_dt);
			
		}
		return SUCCESS;
	}

}
