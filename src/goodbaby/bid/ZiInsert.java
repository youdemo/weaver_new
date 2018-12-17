package goodbaby.bid;

import java.util.HashMap;
import java.util.Map;

import goodbaby.util.GetUtil;
import goodbaby.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class ZiInsert implements Action {
	GetUtil gu = new GetUtil();

	@Override
	public String execute(RequestInfo info) {
		RecordSet res = new RecordSet();
		RecordSet rs = new RecordSet();
		String requestid = info.getRequestid();
		InsertUtil iu = new InsertUtil();
		String tablename = info.getRequestManager().getBillTableName();
		String rid = "";
		String zbj = "";
		String xtkh = "";
		String aid = "";
		String ZBWJBH = "";
		String wlbm = "";
		String mainid = "";
		String kbsj = "";
		String pp = "";
		String xh = "";
		String gg = "";
		String zbj1 = "";
		String zongbj = "";
		String shuilv = "";
		String jhq = "";
		String dw = "";// /CONVERT(varchar(16),GETDATE(),120)
		String sql = " select a.jhq,a.jzbqrq+' '+a.jzbqsj as jzsj ,a.pp,a.xh,a.gg,a.dw,a.wlbm,a.zbj1,a.ZBWJBH,a.zhurid, a.id as aid,a.xtkh,a.zbj,a.shuilv from formtable_main_250 a  where  a.requestid ='"
				+ requestid + "'";
		// String sql =
		// " select a.zhurid,a.xtkh,a.zbj from formtable_main_250 a  where  a.requestid ='"+requestid+"'";
		rs.executeSql(sql);
		if (rs.next()) {
			xtkh = Util.null2String(rs.getString("xtkh"));
			rid = Util.null2String(rs.getString("zhurid"));
			zbj = Util.null2String(rs.getString("zbj"));
			zbj1 = Util.null2String(rs.getString("zbj1"));
			shuilv = Util.null2String(rs.getString("shuilv"));
			kbsj = Util.null2String(rs.getString("jzsj"));
			jhq = Util.null2String(rs.getString("jhq"));
			if (zbj.length() < 1) {
				zbj = "0.00";
			}
			if (zbj1.length() < 1) {
				zbj1 = "0.00";
			}
			pp = Util.null2String(rs.getString("pp"));
			xh = Util.null2String(rs.getString("xh"));
			gg = Util.null2String(rs.getString("gg"));
			dw = Util.null2String(rs.getString("dw"));
			aid = Util.null2String(rs.getString("aid"));
			ZBWJBH = Util.null2String(rs.getString("ZBWJBH"));
			wlbm = Util.null2String(rs.getString("wlbm"));
			// String str =
			// "update formtable_main_200_dt2 set zbjg = '"+zbj+"' where id  = (select a.id from formtable_main_200_dt2 a where a.gysmc='"+xtkh+"' and a.mainid = (select id from formtable_main_200 where requestid ='"+rid+"'))";
			// res.executeSql(str);
		}
		// sql =
		// "select a.TBRQ +' '+ a.TBSJ as ykbsj  from formtable_main_250_dt3 a where a.id =(select max(id) from formtable_main_250_dt3 where mainid = '"+aid+"')";
		// rs.executeSql(sql);
		// if(rs.next()){
		// kbsj = Util.null2String(rs.getString("ykbsj"));
		// }
		sql = "insert into  uf_gysbjb(ZBWJBH,zhurid,zirid,zbj,zkzbj,WLBM,kbsj,pp,xh,gg,dw,crsj,xtkh,shuilv,jhq) values ('"
				+ ZBWJBH + "','" + rid + "','" + requestid + "','" + zbj + "','" + zbj1 + "','" + wlbm + "','"
				+ kbsj + "','" + pp + "','" + xh + "','" + gg + "','" + dw + "',CONVERT(varchar(16),GETDATE(),120),'"
				+ xtkh + "','" + shuilv + "','" + jhq + "')";
		rs.executeSql(sql);
		sql = "select max(id) as mid from  uf_gysbjb ";
		rs.executeSql(sql);
		if (rs.next()) {
			mainid = Util.null2String(rs.getString("mid"));
		}
		sql = "select b.* from formtable_main_250_dt1 b where b.mainid = (select id from formtable_main_250 where requestid = '"
				+ requestid + "') ";
		rs.executeSql(sql);
		while (rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("BM_1", Util.null2String(rs.getString("BM_1")));
			map.put("PP_1", Util.null2String(rs.getString("PP_1")));
			map.put("XH_1", Util.null2String(rs.getString("XH_1")));
			map.put("GG_1", Util.null2String(rs.getString("GG_1")));
			map.put("SHUL_1", Util.null2String(rs.getString("SHUL_1")));
			map.put("DW_1", Util.null2String(rs.getString("DW_1")));
			map.put("WLMC_1", Util.null2String(rs.getString("WLMC_1")));
			map.put("BJJE_1", Util.null2String(rs.getString("BJJE_1")));
			map.put("bjje", Util.null2String(rs.getString("bjje")));
			map.put("mainid", mainid);
			map.put("zrid", requestid);
			iu.insert(map, "uf_gysbjb_dt1");
		}
		// /180717
		sql = "select b.* from formtable_main_250_dt3 b where b.mainid = (select id from formtable_main_250 where requestid = '"
				+ requestid + "') ";
		rs.executeSql(sql);
		while (rs.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("LC", Util.null2String(rs.getString("LC")));
			map.put("TBRQ", Util.null2String(rs.getString("TBRQ")));
			map.put("TBSJ", Util.null2String(rs.getString("TBSJ")));// uf_gysbjb_dt3
			map.put("zrid3", requestid);
			iu.insert(map, "uf_gysbjb_dt3");
		}

		// /

		return SUCCESS;
	}

}