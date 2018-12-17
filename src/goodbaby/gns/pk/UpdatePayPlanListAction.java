package goodbaby.gns.pk;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdatePayPlanListAction implements Action{
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		String tableName = "";
		String dpkid = "";//待排款id
		String SJFKJE_NPP = "";//修改后付款金额
		String dtid = "";
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
		}
		sql = "select * from " + tableName + "_dt1 where mainid=" + mainID;
		rs.execute(sql);
		while (rs.next()) {
			dtid = Util.null2String(rs.getString("id"));
			dpkid = Util.null2String(rs.getString("dpkid"));
			SJFKJE_NPP = Util.null2String(rs.getString("SJFKJE_NPP"));
			updatePayList(dpkid,SJFKJE_NPP,tableName,dtid,requestid);
			
		}
		String summoney = "";
		sql = "select sum(SJFKJE_NPP) as summoney from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		if(rs.next()) {
			summoney = Util.null2String(rs.getString("summoney"));
		}
		sql = "update "+tableName+" set SJFKZJE='"+summoney+"' where requestid="+requestid;
		rs.executeSql(sql);
		
		return SUCCESS;
	}
	
	public void updatePayList(String dpkid,String SJFKJE_NPP,String tablename,String dtid,String requestid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String pkids = "";//排款id
		String syje = SJFKJE_NPP;
		String dfje = "";
		String flag = "";
		String dpkidarr[] = dpkid.split(",");
		String billid = "";
		String updatemoney = SJFKJE_NPP;
		sql = "update uf_flow_payinternal set pklcmxid='"+dtid+"',sfpzpk='1' where pklcid='"+requestid+"' and pkzjbid in("+dpkid+")";
		rs.executeSql(sql);
		
		for(String id : dpkidarr) {
			if("".equals(id)) {
				continue;
			}
			if(Util.getFloatValue(syje, 0)<=0) {
				break;
			}
			sql = "select dfje,id from uf_flow_payinternal where pkzjbid='"+id+"' and pklcid='"+requestid+"' order by kprq asc";
			rs.executeSql(sql);
			if(rs.next()) {
				dfje = Util.null2String(rs.getString("dfje"));
				billid = Util.null2String(rs.getString("id"));
			}
			if(Util.getFloatValue(syje, 0)<Util.getFloatValue(dfje, 0)) {
				continue;
			}else {
				pkids = pkids+flag+id;
				flag = ",";
				syje = sub(syje,dfje);
				sql = "update uf_flow_payinternal set sfpzpk='0' where id="+billid;
				rs.executeSql(sql);
			}
		}
		updatemoney = sub(updatemoney, syje);
		sql="update "+tablename+"_dt1 set SJFKJE_NPP='"+updatemoney+"',pkids='"+pkids+"' where id="+dtid;
		rs.executeSql(sql);
		
		sql="update uf_flow_payinternal set sfje='0' where pklcmxid='"+dtid+"' and sfpzpk='1'";
		rs.executeSql(sql);
		
		sql="update uf_payinternal set sfpk='1' where id in(select pkzjbid from uf_flow_payinternal where pklcmxid='"+dtid+"' and sfpzpk='1')";
		rs.executeSql(sql);
		return;
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
