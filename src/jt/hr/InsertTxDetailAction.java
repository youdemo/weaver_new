package jt.hr;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action; 
import weaver.soa.workflow.request.RequestInfo;

public class InsertTxDetailAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		InsertUtil iu = new InsertUtil();
		String tableName = "";
		String mainid = "";
		String gh = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			mainid = Util.null2String(rs.getString("id"));		
			gh = Util.null2String(rs.getString("gh"));	
		}
		sql = "delete from "+tableName+"_dt1 where mainid="+mainid;
		rs.executeSql(sql);
		RecordSetDataSource rsd  = new RecordSetDataSource("HRsynchronize");
		sql = "select * from jt_txjl where code='"+gh+"' order by txrq desc";
		rsd.execute(sql);
		while(rsd.next()) {
			Map<String, String> map = new HashMap<String, String>();
			map.put("jxmc",Util.null2String(rsd.getString("jxfs")));
			String pyrq = Util.null2String(rsd.getString("pydate"));
			if(pyrq.length()>=10) {
				pyrq = pyrq.substring(0, 10);
			}
			String txrq = Util.null2String(rsd.getString("txrq"));
			if(txrq.length()>=10) {
				txrq = txrq.substring(0, 10);
			}
			map.put("pyrq",pyrq);
			map.put("txrq",txrq);
			map.put("zwmc",Util.null2String(rsd.getString("zw")));
			map.put("zjmc",Util.null2String(rsd.getString("zj")));
			map.put("jbgz",Util.null2String(rsd.getString("jbgz")));
			map.put("zgjg",Util.null2String(rsd.getString("zgjg")));
			map.put("gwjt",Util.null2String(rsd.getString("gwjt")));
			map.put("gdjbf",Util.null2String(rsd.getString("gdjbf")));
			map.put("qtjt",Util.null2String(rsd.getString("qtjt")));
			map.put("shjt",Util.null2String(rsd.getString("shjt")));
			map.put("zj",Util.null2String(rsd.getString("hj")));
			map.put("txyy", Util.null2String(rsd.getString("lb")));
			map.put("mainid",mainid);
			iu.insertold(map, tableName+"_dt1");
		}
		
		
		return SUCCESS;
	}

}
