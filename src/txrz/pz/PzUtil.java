package txrz.pz;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class PzUtil {
	public String getModeId(String tableName) {
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"
				+ tableName + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			formid = Util.null2String(rs.getString("id"));
		}
		sql = "select id from modeinfo where  formid=" + formid;
		rs.executeSql(sql);
		if (rs.next()) {
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
	/**
	 * 获取辅助核算
	 */
	public String getFZHS(String kmdm) {
		RecordSet rs = new RecordSet();
		String hsxm = "";
		String sql = "select hsxm from uf_kmxx where kmdm='"+kmdm+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			hsxm = Util.null2String(rs.getString("hsxm"));
		}
		return hsxm;
	}
}
