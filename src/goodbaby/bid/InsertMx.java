package goodbaby.bid;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class InsertMx {
	public JSONArray wljgk(String wid) throws Exception {
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		JSONArray jsa = new JSONArray();
		String sql = "select * from uf_inquiryForm_dt1 where mainid = '" + wid
				+ "' ";
		rs.executeSql(sql);
		while (rs.next()) {
			JSONObject node = new JSONObject();
			// String PP_1 = Util.null2String(rs.getString("PP_1"));
			// String XH_1 = Util.null2String(rs.getString("XH_1"));
			// String GG_1 = Util.null2String(rs.getString("GG_1"));
			// String SL_1 = Util.null2String(rs.getString("SL_1"));
			// String DW_1 = Util.null2String(rs.getString("DW_1"));
			String WLMC_1 = Util.null2String(rs.getString("WLMC_1"));
			// String BM_1 = Util.null2String(rs.getString("BM_1"));
			node.put("PP_1", Util.null2String(rs.getString("PP_1")));
			node.put("XH_1", Util.null2String(rs.getString("XH_1")));
			node.put("GG_1", Util.null2String(rs.getString("GG_1")));
			node.put("SL_1", Util.null2String(rs.getString("SL_1")));
			node.put("DW_1", Util.null2String(rs.getString("DW_1")));
			node.put("WLMC_1", WLMC_1);
			node.put("BM_1", Util.null2String(rs.getString("BM_1")));
			jsa.put(node);
		}
		return jsa;

	}

	public JSONArray gys(String jmgys1) throws Exception {
		RecordSet rs1 = new RecordSet();
		JSONArray jsa = new JSONArray();
		if (jmgys1.length() > 0) {
			String[] kh1 = jmgys1.split(",");
			String str = "";
			String xtkh = "";
			String xtkhname = "";
			String jmgysmc = "";
			String jmgysbm = "";
			for (int i = 0; i < kh1.length; i++) {
				JSONObject node = new JSONObject();
				str = "select id,name from crm_customerinfo where name =(select GYSMC from uf_suppmessForm where id ='"
						+ kh1[i]
						+ "') and PortalLoginid =(select GYSBM from uf_suppmessForm where id ='"
						+ kh1[i] + "')";
				rs1.executeSql(str);
				if (rs1.next()) {
					xtkh = Util.null2String(rs1.getString("id"));// 客户
					xtkhname = Util.null2String(rs1.getString("name"));// 客户
				}
				str = "select GYSMC,GYSBM from uf_suppmessForm where id ='"
						+ kh1[i] + "'";
				rs1.executeSql(str);
				if (rs1.next()) {
					jmgysmc = Util.null2String(rs1.getString("GYSMC"));// 客户
					jmgysbm = Util.null2String(rs1.getString("GYSBM"));// 客户
				}

				node.put("jmgys", kh1[i]);
				node.put("xtkh", xtkh);
				node.put("xtkhname", xtkhname);
				node.put("jmgysmc", jmgysmc);
				node.put("jmgysbm", jmgysbm);
				jsa.put(node);
			}
		}
		return jsa;

	}

}
