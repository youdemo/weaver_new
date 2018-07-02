package gvo.ecservice;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateECV0006BudgetAction implements Action{

	public String execute(RequestInfo info) {
		// TODO Auto-generated method stub
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("EC");
		String tableName = "";
		String mainID = "";
		String sftzys="";//�Ƿ����
		String ecrqid2="";
		String fyys="";//����Ԥ��
		String oamxid="";//
		String bgce="";//������
		String yssqje="";//Ԥ��������
		String Ecmainid="";//
		String sql_ec="";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,sftzys,ecrqid2,fyys from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			sftzys = Util.null2String(rs.getString("sftzys"));
			ecrqid2 = Util.null2String(rs.getString("ecrqid2"));
			fyys = Util.null2String(rs.getString("fyys"));
		}
		if("0".equals(sftzys)&&!"".equals(ecrqid2)){
			sql_ec="select * from formtable_main_2 where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			if(rsd.next()){
				Ecmainid = Util.null2String(rsd.getString("id"));
			}
			sql_ec="update formtable_main_2 set eeramt='"+fyys+"',cneeramt='"+fyys+"' where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			sql="select * from "+tableName+"_dt2 where mainid="+mainID;
			rs.executeSql(sql);
			while(rs.next()){
				oamxid = Util.null2String(rs.getString("id"));
				bgce = Util.null2String(rs.getString("bhce"));
				yssqje = Util.null2String(rs.getString("yssqje"));
				if(!"".equals(Ecmainid)){
					String dtid="";
					sql_ec="update formtable_main_2_dt1 set eeramt='"+yssqje+"' where mainid="+Ecmainid+" and oamxid='"+oamxid+"'";
					rsd.executeSql(sql_ec);
					sql_ec="select * from formtable_main_2_dt1 where mainid="+Ecmainid+" and oamxid='"+oamxid+"'";
					rsd.executeSql(sql_ec);
					if(rsd.next()){
						dtid = Util.null2String(rsd.getString("id"));
					}
					if(!"".equals(dtid)){
						sql_ec="update fnaexpenseinfo set amount='"+yssqje+"' where sourceRequestid='"+ecrqid2+"' and sourceRequestidDtlId='"+dtid+"'";
					    rsd.executeSql(sql_ec);
					}
					
				}
			}
			
		}
		return SUCCESS;
	}

}
