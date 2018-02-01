package weixin.mflex.fna;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateEPARFormInfo  implements Action {

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String currency="";//币别
		String rate="";//汇率
		String totalcost="";//项目总成本（不含税）
		String totalcost2="";//项目总成本（含税）
		String hszbb="";//含税总成本（隐）
		String bhszcb="";//不含税总成本（隐）
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			currency = Util.null2String(rs.getString("currency"));
			rate = getJDEHL(currency,tableName);
			totalcost = Util.null2String(rs.getString("totalcost"));
			totalcost2 = Util.null2String(rs.getString("totalcost2"));
		}
		sql="select round("+totalcost2+"*"+rate+",2) as hszbb,round("+totalcost+"*"+rate+",2) as bhszcb from dual";
		rs.executeSql(sql);
		if(rs.next()){
			hszbb = Util.null2String(rs.getString("hszbb"));
			bhszcb = Util.null2String(rs.getString("bhszcb"));
		}
		sql="update "+tableName+" set rate='"+rate+"',hszbb='"+hszbb+"',bhszcb='"+bhszcb+"' where requestid="+requestid;
		rs.executeSql(sql);
		return SUCCESS;
	}
	
	public String getJDEHL(String curreny,String tablename){
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("JDEpy");
		String sql_rsd="";
		String result="";
		String value="";
		String sql="select selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"+tablename+"' and a.fieldname='currency' and c.selectvalue='"+curreny+"'";
		rs.executeSql(sql);
		if(rs.next()){
			value = Util.null2String(rs.getString("selectname"));
		}
		sql_rsd="select cxcrr from v_oa_hl where cxcrcd='"+value+"'";
		rsd.executeSql(sql_rsd);
		if(rsd.next()){
			result= Util.null2String(rsd.getString("cxcrr"));
		}
		return result;		
	}

}
