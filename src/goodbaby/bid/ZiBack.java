package goodbaby.bid;

import java.util.HashMap;
import java.util.Map;

import goodbaby.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 
 * @author 张瑞坤 子流程回写数据 0626
 */
public class ZiBack implements Action {

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
		String zbj1 = "";
		String shuilv = "";
		String sql = " select a.zhurid,a.hl,a.currency,a.xtkh,a.zbj,a.zbj1,a.shuilv from formtable_main_250 a  where  a.requestid ='"
				+ requestid + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			xtkh = Util.null2String(rs.getString("xtkh"));
			rid = Util.null2String(rs.getString("zhurid"));
			zbj = Util.null2String(rs.getString("zbj"));
			zbj1 = Util.null2String(rs.getString("zbj1"));
			shuilv = Util.null2String(rs.getString("shuilv"));
			String hl = Util.null2String(rs.getString("hl"));
			String currency = Util.null2String(rs.getString("currency"));
			if (zbj.length() < 1) {
				zbj = "0.00";
			}
			if (zbj1.length() < 1) {
				zbj1 = "0.00";
			}
			StringBuffer buff = new StringBuffer();
			buff.append("update formtable_main_200_dt2 set ");
			int aa = 0;
			if(shuilv.length()>0){
				buff.append(" shuilv='");
				buff.append(shuilv+"' ");
				aa++;
			}
			if(zbj.length()>0){
				if(aa==0){
					buff.append(" zbjg = '");
				}else{
					buff.append(" , zbjg = '");
				}
				
				buff.append(zbj+"' ");
				aa++;
			}
			if(zbj1.length()>0){
				if(aa==0){
					buff.append(" zkbj = '");
				}else{
					buff.append(" , zkbj = '");
				}
				
				buff.append(zbj1+"' ");
				aa++;
			}
			if(currency.length()>0){
				if(aa==0){
					buff.append(" currency = '");
				}else{
					buff.append(" , currency = '");
				}
				
				buff.append(currency+"' ");
				aa++;
			}
			if(hl.length()>0){
				if(aa==0){
					buff.append(" hl = '");
				}else{
					buff.append(" , hl = '");
				}
				
				buff.append(hl+"' ");
				aa++;
			}
			buff.append(" where id  = (select a.id from formtable_main_200_dt2 a where a.gysmc='"+xtkh+"' ");
			buff.append(" and a.mainid = (select id from formtable_main_200 where requestid ='"+rid+"' ");
			res.executeSql(buff.toString()); 
			// aid = Util.null2String(rs.getString("aid"));

		}


		return SUCCESS;
	}

}
