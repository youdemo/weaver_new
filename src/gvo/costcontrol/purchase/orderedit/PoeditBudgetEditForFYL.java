package gvo.costcontrol.purchase.orderedit;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PoeditBudgetEditForFYL implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		log.writeLog("PoeditBudgetEditForFYL------");
		String purrequest = "";//明细 采购申请
		String reqproject = "";//明细 请求项目
		String orderflow = "";//采购订单流程
		//String xghje = "";//修改后金额
		//String brtwr = "";//原金额
		String xghsl = "";
		String xghdj = "";
		String sl = "";
		String hl = "";
		String tableName = "";
		String sql_dt = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("ID"));
			orderflow = Util.null2String(rs.getString("orderflow"));
		}
		sql="select * from "+tableName+"_dt2 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			purrequest = Util.null2String(rs.getString("purrequest"));
			reqproject = Util.null2String(rs.getString("requestproj")).replaceAll("^(0+)", "");
			//xghje = Util.null2String(rs.getString("xghje"));
			//brtwr = Util.null2String(rs.getString("brtwr"));
			xghsl = Util.null2String(rs.getString("xghsl"));
			xghdj = Util.null2String(rs.getString("xghdj"));
			sl = Util.null2String(rs.getString("sl"));
			hl = Util.null2String(rs.getString("hl"));
			if("".equals(xghsl)){
				xghsl = "0";
			}
			if("".equals(xghdj)){
				xghdj = "0";
			}
			if("".equals(sl)){
				sl = "0";
			}
			if("".equals(hl)){
				hl = "0";
			}
			String je = "";
			sql_dt="select round(nvl('"+xghsl+"',0)*nvl('"+xghdj+"',0)*nvl('"+hl+"',0)/(1+nvl('"+sl+"',0)),2) as je from dual";
			rs_dt.executeSql(sql_dt);
			if(rs_dt.next()){
				je = Util.null2String(rs_dt.getString("je"));
			}
			if(!"".equals(purrequest)&&!"".equals(reqproject)){
				Map<String, String> map =getPurchaseMiddleInfo(purrequest,reqproject);
				String cdbm = map.get("cdbm");
				String yskm = map.get("yskm");
				String mxid = map.get("mxid");
				String lcid = map.get("lcid");
				String fnaid="";
				sql_dt="select id from fnaexpenseinfo where sourcerequestid='"+lcid+"' and sourcerequestiddtlid='"+mxid+"'";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					fnaid = Util.null2String(rs_dt.getString("id"));				
				}
				if(!"".equals(fnaid)){
					sql_dt="update fnaexpenseinfo set amount='"+je+"' where id="+fnaid;
					rs_dt.executeSql(sql_dt);
				}
				
			}
		}
		return SUCCESS;
	}
	public Map<String, String> getPurchaseMiddleInfo(String cgsqdh,String mxhid){
		RecordSet rs = new RecordSet();
		String cdbm="";
		String yskm="";
		String qj = "";
		String type = "";
		String mxid = "";
		String lcid = "";
		Map<String,String> map = new HashMap<String, String>();
		String sql="select * from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid = '"+mxhid+"' order by id desc";
		rs.executeSql(sql);
		if(rs.next()){
			cdbm = Util.null2String(rs.getString("cdbm"));
			yskm = Util.null2String(rs.getString("yskm"));
			qj = Util.null2String(rs.getString("qj"));	
			type = Util.null2String(rs.getString("type"));	
			mxid = Util.null2String(rs.getString("mxid"));	
			lcid = Util.null2String(rs.getString("lcid"));	
		}
		map.put("cdbm",cdbm);
		map.put("yskm",yskm);
		map.put("qj", qj);
		map.put("type",type);
		map.put("mxid",mxid);
		map.put("lcid",lcid);
		return map;
	}
}
