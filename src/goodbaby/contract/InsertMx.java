package goodbaby.contract;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class InsertMx {
	public JSONArray wljgk(String wid) throws Exception{
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		JSONArray jsa = new JSONArray();
		String sql = "select * from uf_inquiryForm_dt1 where mainid = '"+wid+"' ";
		rs.executeSql(sql);
		while(rs.next()){
			JSONObject node = new JSONObject(); 
		   	String	WLMC_1 = Util.null2String(rs.getString("WLMC_1"));
//		   	String  BM_1 = Util.null2String(rs.getString("BM_1"));
		   	node.put("PP_1",Util.null2String(rs.getString("PP_1")));
		   	node.put("XH_1",Util.null2String(rs.getString("XH_1")));
		   	node.put("GG_1",Util.null2String(rs.getString("GG_1")));
		   	node.put("SL_1",Util.null2String(rs.getString("SL_1")));
		   	node.put("DW_1",Util.null2String(rs.getString("DW_1")));
		   	node.put("DJ_1", Util.null2String(rs.getString("DJ_1")));
		   	node.put("WLMC_1",WLMC_1);
		   	node.put("BM_1",Util.null2String(rs.getString("BM_1")));
		   	jsa.put(node);
		}
		return jsa;
		
	}
	

}
