package goodbaby.gns.fk;

import goodbaby.pz.GetGNSTableName;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 付款申请冲销预付款
 * @author tangj
 *
 */
public class UpdateMoneyForYfk implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 流程类型唯一id
		String requestid = info.getRequestid();// 具体单据的唯一id
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainid = "";
		String dtid = "";
		String bcfk = "";
		String yfklc = "";
		String sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			mainid = Util.null2String(rs.getString("id"));
		}
		sql = "select * from " + tableName + "_dt1 where mainid=" + mainid;
		rs.executeSql(sql);
		while(rs.next()) {
			dtid = Util.null2String(rs.getString("id"));
			bcfk = Util.null2String(rs.getString("bcfk"));
			yfklc = Util.null2String(rs.getString("yfklc"));
			if("".equals(yfklc)) {
				continue;
			}
			if("".equals(bcfk)) {
				bcfk = "0";
			}
			updateMoney(dtid,bcfk,yfklc,tableName);
		}
		return SUCCESS;
	}
	/**
	 * 扣减金额
	 * @param dtid 明细id
	 * @param bcfk 本次支付金额
	 * @param yfklc 预付款流程id
	 * @param tablename 流程表名
	 */
	public void updateMoney(String dtid,String bcfk,String yfklc,String tablename) {
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_yfk = gg.getTableName("YFK");//预付款
		String yfklcs[] = yfklc.split(",");
		String sql = "";
		String bcfkje = bcfk;
		for(String yfid:yfklcs) {
			if("".equals(yfid)) {
				continue;
			}
			String syje = "";
			sql = "select isnull(bcjeyb,0)-isnull(ycxje,0) as syje from " + tablename_yfk + " where requestid=" + yfid;
			rs.executeSql(sql);
			if(rs.next()) {
				syje = Util.null2String(rs.getString("syje"));
			}
			if(Util.getFloatValue(syje, 0)<=0) {
				continue;
			}
			if(Util.getFloatValue(bcfkje, 0)<=0) {
				break;
			}
			if(Util.getFloatValue(syje, 0)<Util.getFloatValue(bcfkje, 0)) {
				sql = "update " + tablename_yfk + " set ycxje=isnull(ycxje,0)+" + syje + " where requestid=" + yfid;
				rs.executeSql(sql);
				sql = "update " + tablename + "_dt1 set bcfk=bcfk-" + syje + " where id=" + dtid;
				rs.executeSql(sql);
				bcfkje = sub(bcfkje, syje);
			}else {
				sql = "update " + tablename_yfk + " set ycxje=isnull(ycxje,0)+" + bcfkje + " where requestid=" + yfid;
				//log.writeLog(sql);
				rs.executeSql(sql);
				sql = "update " + tablename + "_dt1 set bcfk=0 where id=" + dtid;
				rs.executeSql(sql);
				bcfkje = "0";
			}
		}
		sql = "update " + tablename + "_dt1 set yfkcxje=fpje-bcfk,rmb=cast(bcfk*hl as numeric(18,2)),sj=cast(bcfk*sl as numeric(18,2)) where id=" + dtid;
		rs.executeSql(sql);
	}
	
	public String sub(String a,String b){
    	RecordSet rs = new RecordSet();
    	String result="";
    	String sql="select cast(cast('"+a+"' as numeric(18,2))-cast('"+b+"' as numeric(18,2))as numeric(18,2))as je  ";
		rs.executeSql(sql);
		if(rs.next()){
			result = Util.null2String(rs.getString("je"));
		}
		return result;
		
    }

}
