package zj.reimbursement;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class DoTravelAllowance implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		String requestId = info.getRequestid();
		String workflowId = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainId = "";
		String ccrq = "";//出差日期
		String ccsj = "";//出差时间
		String jsrq = "";//结束日期
		String jssj = "";//结束时间
		String xm = "";
		String seclevel = "0";
		int days=0;
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowId
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestId;
		rs.executeSql(sql);
		if (rs.next()) {
			mainId = Util.null2String(rs.getString("id"));
			ccrq = Util.null2String(rs.getString("ccrq"));
			ccsj = Util.null2String(rs.getString("ccsj"));
			jsrq = Util.null2String(rs.getString("jsrq"));
			jssj = Util.null2String(rs.getString("jssj"));
			xm = Util.null2String(rs.getString("xm"));
		}
		sql="select seclevel from hrmresource where id="+xm;
		rs.executeSql(sql);
		if(rs.next()){
			seclevel = Util.null2String(rs.getString("seclevel"));
		}
		
		int money=0;
		if(Util.getFloatValue(seclevel,0)<Util.getFloatValue("40",0)){
		
		String startDay=ccrq+" "+ccsj;
		String endDay=jsrq+" "+jssj;
		if(endDay.compareTo(startDay)<=0){
			money =0;
		}else{
			//log.writeLog("startDay:"+startDay+"endDay"+endDay);
			
			sql="select DATEDIFF(DAY,'"+ccrq+"','"+jsrq+"') as days";
			//log.writeLog("aa"+sql);
			rs.executeSql(sql);
			if(rs.next()){
				days = rs.getInt("days");
			}
			if(days == 0){
				money=money+getDayMoney(ccsj,jssj);
			}else if(days == 1){
				money=money+getDayMoney(ccsj,"24:00");
				money=money+getDayMoney("00:00",jssj);
			}else if(days > 1){
				money=money+getDayMoney(ccsj,"24:00");
				money=money+(days-1)*65;
				money=money+getDayMoney("00:00",jssj);
			}
			//log.writeLog("aa money"+money);
			int count=0;
			sql = "select count(1) as count from "+tableName+"_dt1 where mainid="+mainId+" and fykm='0'";
			rs.executeSql(sql);
			if(rs.next()){
				count = rs.getInt("count");
			}
			if(count > 0){
				money = money-(count*25);
			}
		}
		
		}
		//log.writeLog("aa money"+money);
		sql="update "+tableName+" set ccbt="+money+" where requestid="+requestId;
		//log.writeLog("aa"+sql);
		rs.executeSql(sql);
		sql="update "+tableName+"_dt1 set lb=fykm ,cbwb=sfcb where mainid="+mainId;
		rs.executeSql(sql);
		return SUCCESS;
	}

	public int getDayMoney(String ccsj,String jssj){
		int money=0;
		if(ccsj.compareTo("08:00")<=0){
			if(jssj.compareTo("08:00")>=0){
				money=money+15;
				if(jssj.compareTo("13:00")>=0){
					money=money+25;
					if(jssj.compareTo("18:00")>=0){
						money=money+25;
					}
				}
			}
		}else if(ccsj.compareTo("13:00")<=0){
			if(jssj.compareTo("13:00")>=0){
				money=money+25;
				if(jssj.compareTo("18:00")>=0){
					money=money+25;
				}
			}
		}else if(ccsj.compareTo("18:00")<=0){
			if(jssj.compareTo("18:00")>=0){
				money=money+25;
			}
		}
		return money;
	}
	
	
}
