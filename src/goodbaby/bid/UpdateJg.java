package goodbaby.bid;

import goodbaby.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 
 * @author 张瑞坤
 * 
 */
public class UpdateJg implements Action {

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		GetUtil gu = new GetUtil();
		String requestid = Util.null2String(info.getRequestid());
		RecordSet rs = new RecordSet();
		String xtkh = "";
		String zrid = "";
		String jgkid = "";
		RecordSet res = new RecordSet();
		String sql = "select b.hl,b.currency,a.jgkxx,a.ZKZJ,b.gysmc2,b.gysmc,b.shuilv,a.SL from formtable_main_200 a "
				+ " join  formtable_main_200_dt2 b  on a.id = b.mainid where b.cgzxsfty = 0 and "
				+ " a.requestid ='" + requestid + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			String jgkxx = Util.null2String(rs.getString("jgkxx"));
			jgkid = jgkxx;
			String gys = Util.null2String(rs.getString("gysmc2"));
			String jg = Util.null2String(rs.getString("ZKZJ"));
			xtkh = Util.null2String(rs.getString("gysmc"));
			String shuilv = Util.null2String(rs.getString("shuilv"));
			String hl = Util.null2String(rs.getString("hl"));
			String sl = Util.null2String(rs.getString("SL"));
			String currency = Util.null2String(rs.getString("currency"));
			String gybm = gu.getFieldVal("uf_suppmessForm", "GYSBM", "id", gys);
			String str = "update uf_inquiryForm set GYSMC='" + gys
					+ "',GYSBH='" + gybm + "',YJJG='" + jg + "',rate='"
					+ shuilv + "',GYS='" + xtkh + "',hl1='" + hl + "',bz1='"
					+ currency + "',SL='" + sl + "'  where id ='" + jgkxx + "'";
			log.writeLog("str---" + str);
			res.executeSql(str);
		}

		sql = "select a.requestid from formtable_main_250 a join formtable_main_250_dt1 b on   a.id =b.mainid where a.zhurid"
				+ "='" + requestid + "' and a.xtkh = '" + xtkh + "'";

		rs.executeSql(sql);
		if (rs.next()) {
			zrid = Util.null2String(rs.getString("requestid"));
		}

		// join uf_gysbjb_dt1 on a.id =b.mainid
		sql = " select b.BM_1,b.WLMC_1,b.BJJE_1,b.bjje from uf_gysbjb_dt1 b where b.mainid =(select max(a.id) as mid from uf_gysbjb a  where a.zhurid='"
				+ requestid
				+ "' and"
				+ " a.zirid='"
				+ zrid
				+ "' and a.xtkh = '" + xtkh + "') and b.zrid='" + zrid + "'";
		rs.executeSql(sql);
		while (rs.next()) {
			String BM_1 = Util.null2String(rs.getString("BM_1"));
			String WLMC_1 = Util.null2String(rs.getString("WLMC_1"));
			String BJJE_1 = Util.null2String(rs.getString("BJJE_1"));
			String bjje = Util.null2String(rs.getString("bjje"));
			String str = "update uf_inquiryForm_dt1 set DJ_1='" + BJJE_1
					+ "',ze_1='" + bjje + "' where mainid ='" + jgkid
					+ "' and WLMC_1='" + WLMC_1 + "' and BM_1='" + BM_1 + "'  ";
			log.writeLog("str2---" + str);
			res.executeSql(str);
		}

		return SUCCESS;
	}

}
