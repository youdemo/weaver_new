package goodbaby.gb.stock.action;

import goodbaby.gb.stock.util.Stock;
import goodbaby.gb.stock.util.StockManager;
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
		BaseBean log = new BaseBean();
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
			}
		}
		return SUCCESS;
	}

}
