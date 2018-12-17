package goodbaby.gb.stock.action;

import goodbaby.gb.stock.util.Stock;
import goodbaby.gb.stock.util.StockManager;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 入库
 * @author baoja
 *
 */
public class GetIntoStockAction implements Action{

	@Override
	public String execute(RequestInfo request){
		String requestid = request.getRequestid();
		String workflowid = request.getWorkflowid();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("GetIntoStockAction start ... ");
		String sql = "";
		String tableName = "";
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid + ")";
		
		rs.execute(sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		
		if(!"".equals(tableName)){
			StockManager sm = new StockManager();
			sql = "select fm.SHCK,fd.SJSHSL_1,fd.WLMC_1,fd.DJ_1,fd.gys,fd.id as dtID, "
				+" convert(varchar(10),getdate(),121) as fdate,"
				+" convert(varchar(5),getdate(),24) as ftime "
				+" from "+tableName+" fm "
				+" join "+tableName+"_dt1 fd on fm.id=fd.mainid"
				+" where requestid  = " + requestid;
			rs.executeSql(sql);
			while(rs.next()){
				String dtID = Util.null2String(rs.getString("dtID"));
				String ck = Util.null2String(rs.getString("SHCK"));
				double num = rs.getDouble("SJSHSL_1");
				String materID = Util.null2String(rs.getString("WLMC_1"));
				double unitPrice = rs.getDouble("DJ_1");
				String sp = Util.null2String(rs.getString("gys"));
				String fdate = Util.null2String(rs.getString("fdate"));
				String ftime = Util.null2String(rs.getString("ftime"));
				
				Stock sk = new Stock();
				sk.setRequestID(requestid);
				sk.setDtID(dtID);
				sk.setMaterielID(materID);
				sk.setUnitPrice(unitPrice);
				sk.setcNum(num);
				sk.setfNum(0);
				sk.setcDate(fdate);
				sk.setcTime(ftime);
				sk.setCkid(ck);
				sk.setGys(sp);
				sk.setType(0);
				
				sm.putStock(sk);
			}
		}
		return SUCCESS;
	}
}
