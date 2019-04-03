package gb.stock.action;

import gb.stock.util.Stock;
import gb.stock.util.StockManager;
import goodbaby.pz.GetGNSTableName;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 领用申请
 * 
 * @author baoja
 */
public class CollarStockAction implements Action {

	@Override
	public String execute(RequestInfo request) {
		String requestid = request.getRequestid();
		String workflowid = request.getWorkflowid();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql_dt = "";
		BaseBean log = new BaseBean();
		writeLog("CollarStockAction 开始");
		String sql = "";
		String tableName = "";
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		if (!"".equals(tableName)) {
			StockManager sm = new StockManager();
			sql = "select fm.requestId,fd.id,fd.WLMC_2,fd.SLSL_1,convert(varchar(10),getdate(),121) as fdate,"
					+ " convert(varchar(5),getdate(),24) as ftime,fm.CK  "
					+ " from  "
					+ tableName
					+ " fm join "
					+ tableName
					+ "_dt1 fd on fm.id = fd.mainid "
					+ " where fm.requestId in(select requestId from workflow_requestbase where currentnodetype=3)"
					+ " and requestid = " + requestid;
			log.writeLog("sql = " + sql);
			rs.executeSql(sql);
			while (rs.next()) {
				String tempRequestid = Util.null2String(rs
						.getString("requestId"));
				String dtID = Util.null2String(rs.getString("id"));
				String tempWlmc = Util.null2String(rs.getString("WLMC_2"));
				double tempSl = rs.getDouble("SLSL_1");
				String fdate = Util.null2String(rs.getString("fdate"));
				String ftime = Util.null2String(rs.getString("ftime"));
				String ck = Util.null2String(rs.getString("CK"));
				writeLog("CollarStockAction tempWlmc:"+tempWlmc);
				double je=getLyje(tempWlmc,tempSl,ck);
				Stock sk = new Stock();
				sk.setRequestID(tempRequestid);
				sk.setDtID(dtID);
				sk.setMaterielID(tempWlmc);
				sk.setcNum(tempSl);
				sk.setfNum(0);
				sk.setcDate(fdate);
				sk.setcTime(ftime);
				sk.setCkid(ck);
				sk.setType(1);

				sm.putStock(sk);
				
				sql_dt = "update "+tableName+"_dt1 set rmbje='"+je+"' where id="+dtID;;
				rs_dt.executeSql(sql_dt);
			}
		}
		return SUCCESS;
	}
	
	public double getLyje(String wlid,double sl,String ck) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql_dt = "";
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_rk = gg.getTableName("RKD");//入库单
		String tablename_dd = gg.getTableName("CGDD");//订单
		double nowNum = sl;
		double je = 0;
		String sql = "select * from uf_Stock where rkly = 0 and rksl > isnull(ylysl,0) and wlmc = '"+wlid+"'"
		+ " and CKID = '"+ck+"'"
		+ " order by rkrq,rksj,id ";

		writeLog("CollarStockAction sql:"+sql);
		rs.executeSql(sql);
		while(rs.next()) {
			double allNum = rs.getDouble("rksl");
			double useNum = rs.getDouble("ylysl");
			double unitPrice = rs.getDouble("wsdj");
			String lch = Util.null2String(rs.getString("lch"));
			String hl = "";
			sql_dt = "select c.HL as hl from "+tablename_rk+" a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c" + 
					" where a.id=b.mainid  and b.cgsqd = c. requestid and a.requestid in("+lch+")"  ;
			rs_dt.executeSql(sql_dt);
			writeLog("CollarStockAction sql_dt:"+sql_dt);
			if(rs_dt.next()) {
				hl = Util.null2String(rs_dt.getString("hl"));
			}
			if("".equals(hl)) {
				hl = "0";
			}
			if(useNum < 0) useNum = 0;
			// 剩余数量
			double otherNum = oper(allNum, useNum, 1);
			if(otherNum >= nowNum){
				je = oper(je, Double.valueOf(mult(nowNum,unitPrice,hl)), 0);
				break;
			}else {
				je = oper(je, Double.valueOf(mult(otherNum,unitPrice,hl)), 0);
				nowNum = oper(nowNum, otherNum, 1);
			}

			writeLog("CollarStockAction je:"+je);
			
		}
		return je;
	}
	private double oper(double a,double b,int type){
		int res = 0;
		if(type == 0){
			res = ((int)(a*100) + (int)(b*100));
		}else{
			res = ((int)(a*100) - (int)(b*100));
		}
		return res/100.0;
	}
	public String mult(double a,double b,String c){
    	RecordSet rs = new RecordSet();
    	String result="";
    	String sql="select cast(cast('"+a+"' as numeric(18,2))*cast('"+b+"' as numeric(18,2))*cast('"+c+"' as numeric(18,4)) as numeric(18,2)) as je  ";
		rs.executeSql(sql);
		if(rs.next()){
			result = Util.null2String(rs.getString("je"));
		}
		return result;
		
    }
	 private void writeLog(Object obj) {
	        if (true) {
	            new BaseBean().writeLog(this.getClass().getName(), obj);
	        }
	    }

}
