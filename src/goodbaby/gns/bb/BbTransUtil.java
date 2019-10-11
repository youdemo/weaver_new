package goodbaby.gns.bb;

import goodbaby.pz.GetGNSTableName;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class BbTransUtil {
	/**
	  * 获取所有部门名称
	 * @param ryid
	 * @return
	 */
	public String getPersonAllDepartmentName(String ryid) {
		String allDepartmentName = "";
		String deptid = "";
		RecordSet rs = new RecordSet();
		if("".equals(ryid)) {
			return "";
		}
		String sql = "select departmentid from hrmresource where id="+ryid;
		rs.executeSql(sql);
		if(rs.next()) {
			deptid = Util.null2String(rs.getString("departmentid"));
		}
		allDepartmentName = getAllDepartmentName(deptid);
		return allDepartmentName;
	}
	/**
	 * 递归获取部门名称
	 * @param deptid
	 * @return
	 */
	public String getAllDepartmentName(String deptid) {
		RecordSet rs = new RecordSet();
		String supdepid = "";
		String departmentname = "";
		String sql = "select departmentname,supdepid from HrmDepartment where id="+deptid;
		rs.executeSql(sql);
		if(rs.next()) {
			departmentname = Util.null2String(rs.getString("departmentname"));
			supdepid = Util.null2String(rs.getString("supdepid"));
		}
		if(departmentname.indexOf("~`~`7")>=0) {
			departmentname = departmentname.substring(departmentname.indexOf("`~`7")+4, departmentname.indexOf("`~`8"));
		}
		if("".equals(supdepid)||"0".equals(supdepid)||"-1".equals(supdepid)) {
			return departmentname;
		}else {
			return getAllDepartmentName(supdepid)+"/"+departmentname;
		}
		
	}
	
	public String getAllThdh(String rkd) {
		RecordSet rs = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String lrktablename = gg.getTableName("THD");
		String thdhall = "";
		String flag = "";
		String thdh = "";
		String sql = "select THDH from "+lrktablename+" t,"+lrktablename+"_dt1 t1 where t.id=t1.mainid and t1.RKD='"+rkd+"'";
		rs.executeSql(sql);
		while(rs.next()) {
			thdh = Util.null2String(rs.getString("THDH"));
			thdhall = thdhall+flag+thdh;
			flag = "<br/>";
		}
		return thdhall;
	}
	
}
