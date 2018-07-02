package feilida;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class SortNumGen implements Action {
	
	BaseBean log = new BaseBean();//定义写入日志的对象
	public String execute(RequestInfo info) {
		log.writeLog("---------------SortNum-----------------");
		
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();//获取工作流程ID与requestID值
		
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		
		String tableName = "";
		String tableNamedt = "";
		String mainID = "";
		//主表
		String BH = "";//编号
		//明细表
		String ID = "";
		String workflowid = "";
		
		String sql  = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= "
				+ workflowID + ")";
		
		rs.execute(sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		
		if(!"".equals(tableName)){
			tableNamedt = tableName + "_dt1";
			
			// 查询主表
			sql = "select * from "+tableName + " where requestid="+requestid;
			rs.execute(sql);
			if(rs.next()){
				BH = Util.null2String(rs.getString("BH"));
				mainID = Util.null2String(rs.getString("ID"));
			}
			
			//查询明细表
			sql = "select * from " + tableNamedt + " where mainid="+mainID;
			rs_dt.execute(sql);
			log.writeLog("查询明细表:" +sql);
			while(rs_dt.next()){
				ID = Util.null2String(rs_dt.getString("id"));
				
				String sql_up = " update formtable_main_5_dt1 set SQBHC = (select sortnum from (select id,'"+BH+"'||'-'||sort  " +
								" as sortnum from (select id,rownum as sort from formtable_main_5_dt1 where mainid in (select id" +
								" from formtable_main_5 where requestid = '"+requestid+"' ) order by id)) where id = "+ID+")" +
								" where id = "+ID+" ";
				rs.execute(sql_up);
				log.writeLog("明细表序号更新:" +sql_up);

	       }
		}else{
			
			return "-1";
		}
		return SUCCESS;
	}

}
