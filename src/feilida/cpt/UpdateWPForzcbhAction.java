package feilida.cpt;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateWPForzcbhAction implements Action{
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet rs_detail = new RecordSet();
		log.writeLog("开始反写资产编号");
		String tableName = "";
		String mainID = "";
		String xzbm = "";
		String shyr="";
		String shybm="";
		String sql_detail="";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		
		sql="select id,xzbm,shyr,shybm from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			xzbm = Util.null2String(rs.getString("xzbm"));
			shyr = Util.null2String(rs.getString("shyr"));
			shybm = Util.null2String(rs.getString("shybm"));
		}
		String qfid="";
		String id="";
		String CFDD="";
		String zcid="";
		if(!"68".equals(xzbm)){
			//从资产模块根据唯一标识更新明细表的资产编号
			sql="select * from "+tableName+"_dt1 where mainid="+mainID;
			rs.executeSql(sql);
			while(rs.next()){
				qfid=Util.null2String(rs.getString("qfid"));
				id=Util.null2String(rs.getString("id"));
				CFDD=Util.null2String(rs.getString("CFDD")); 
				zcid = Util.null2String(rs.getString("zcid")); 
				int count=0;
				//log.writeLog("qfid:"+qfid+"id:"+id+"CFDD:"+CFDD+"zcid:"+zcid);
				if(!"".equals(zcid)){
					sql_detail="select count(1) as count from cptcapital where id='"+zcid+"'";
					//log.writeLog("sql_detail"+sql_detail);
					rs_detail.executeSql(sql_detail);
					if(rs_detail.next()){
						count = rs_detail.getInt("count");
					}
				}
				//log.writeLog("qfid"+qfid+"count"+count);
				if(!"".equals(qfid)&& count<=0){
					sql_detail="update "+tableName+"_dt1 set ZCBH =(select mark from cptcapital where location='"+qfid+"'),zcid=(select id from cptcapital where location='"+qfid+"') where id="+id;
					//log.writeLog("sql_detail"+sql_detail);
					rs_detail.executeSql(sql_detail);
				    sql_detail="update cptcapital set resourceid='"+shyr+"' ,departmentid='"+shybm+"' ,stateid=2,location='"+CFDD+"' where location='"+qfid+"'";
				   // log.writeLog("sql_detail"+sql_detail);
				    rs_detail.executeSql(sql_detail);
				}
			}
			log.writeLog("开始资产编号反写结束");
		}
		return SUCCESS;
	}

}
