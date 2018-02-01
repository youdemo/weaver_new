package zj.certificate;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;

public class InsertRequestHBRecord {
	public void insertInfo(String requestids) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
        int maxid = 1;
        String sql = "";
		String requestid[] = requestids.split(",");
		for (int i = 0; i < requestid.length; i++) {
			String rqid = requestid[i];
			if ("".equals(rqid) || "0".equals(rqid)) {
				continue;
			}
			sql = "select isnull(MAX(id),0)+1 as next from request_hb_record";
			rs.executeSql(sql);
			if (rs.next()) {
				maxid = rs.getInt("next");
			}
			sql = "insert into request_hb_record(id,requestid,sfcl ) values('"
					+ maxid + "','" + rqid + "','0')";
			rs.executeSql(sql);
		}
	}
}
