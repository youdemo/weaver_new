package goodbaby.order;

import goodbaby.pz.GetGNSTableName;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class CheckCanCreate {
	public String CheckCreate(String rqid) {
		RecordSet rs = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_rk = gg.getTableName("RKD");//入库单
		String tablename_dd = gg.getTableName("CGDD");//订单
		String tablename_ht = gg.getTableName("FKJHT");//非框架合同
		String contactReq = "";
		String contactDtId = "";
		String cgsl = "";
		String zshsl = "";
		String sfrk = "1";
		int count =0;
		String result = "true";
		String sql="select a.CGSL,contactReq,contactDtId,isnull((select sum(isnull(d.SJSHSL_1,0)) from  "+tablename_rk+" c join "+tablename_rk+"_dt1 d on c.id =d.mainid join workflow_requestbase e on c.requestid = e.requestid where d.wlbh_1=a.WLBM and d.cgsqd=a.requestid  ),0) as zshsl from "+tablename_dd+" a where requestid ='"+rqid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			cgsl = Util.null2String(rs.getString("CGSL"));
			contactReq = Util.null2String(rs.getString("contactReq"));
			contactDtId = Util.null2String(rs.getString("contactDtId"));
			zshsl = Util.null2String(rs.getString("zshsl"));
		}
		if(!"".equals(contactReq) && !"".equals(contactDtId)){
			sql = "select * from (select row_number() over(order by b.id asc) as num,htbh,fkbfb,fkje,sfrk,b.id from "+tablename_ht+" a,"+tablename_ht+"_dt1 b where a.id=b.mainid and a.requestid='"+contactReq+"') t where t.id="+contactDtId;
			rs.executeSql(sql);
			if(rs.next()){
				sfrk = Util.null2String(rs.getString("sfrk"));
			}
		}
		if(!"1".equals(sfrk)){
			sql = "select count(a.id) as count from "+tablename_rk+" a,"+tablename_rk+"_dt1 b where a.id=b.mainid and cgsqd='"+rqid+"'";
			rs.executeSql(sql);
			if(rs.next()){
				count = rs.getInt("count");
			}
		}
		if(!"1".equals(sfrk)) {
			if(count>0) {
				result = "false";
			}
		}else {
			if(Util.getDoubleValue(zshsl, 0)>=Util.getDoubleValue(cgsl, 0)) {
				result = "false";
			}
		}
		return result;
	}
}
