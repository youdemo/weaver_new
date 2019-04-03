package goodbaby.pz;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class GetGNSTableName {
	public  String getTableName(String bs) {
		RecordSet rs = new RecordSet();
		String tablename = "";
		String sql = "select tbname from uf_gns_flow_tbname where bs='"+bs+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			tablename = Util.null2String(rs.getString("tbname"));
		}
		return tablename;
	}
}
