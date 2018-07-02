package gvo.costcontrol.purchase.orderedit;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PoeditBudgetEditForGDZC implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		log.writeLog("PoeditBudgetEditForGDZC------");
		String purrequest = "";//明细 采购申请
		String reqproject = "";//明细 请求项目
		String orderflow = "";//采购订单流程
		String xghje = "";//修改后金额
		String brtwr = "";//原金额
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
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			purrequest = Util.null2String(rs.getString("purrequest"));
			reqproject = Util.null2String(rs.getString("requestproj"));
			xghje = Util.null2String(rs.getString("xghje"));
			brtwr = Util.null2String(rs.getString("brtwr"));
			if("".equals(xghje)){
				xghje = "0";
			}
			if(brtwr.equals(xghje)){
				continue;
			}
			if(!"".equals(purrequest)&&!"".equals(reqproject)){
				Map<String, String> map =getPurchaseMiddleInfo(purrequest,reqproject);
				String cdbm = map.get("cdbm");
				String yskm = map.get("yskm");
				String fnaid="";
				sql_dt="select id from fnaexpenseinfo where subject='"+yskm+"' and organizationid='"+cdbm+"' and organizationtype='18004' and sourceRequestid='"+orderflow+"' and amount="+brtwr;
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()){
					fnaid = Util.null2String(rs_dt.getString("id"));				
				}
				if(!"".equals(fnaid)){
					sql_dt="update fnaexpenseinfo set amount='"+xghje+"' where id="+fnaid;
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
		Map<String,String> map = new HashMap<String, String>();
		String sql="select * from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid = '"+mxhid+"' order by id asc";
		rs.executeSql(sql);
		if(rs.next()){
			cdbm = Util.null2String(rs.getString("cdbm"));
			yskm = Util.null2String(rs.getString("yskm"));
			qj = Util.null2String(rs.getString("qj"));	
			type = Util.null2String(rs.getString("type"));	
		}
		map.put("cdbm",cdbm);
		map.put("yskm",yskm);
		map.put("qj", qj);
		map.put("type",type);
		return map;
	}
}
