package morningcore.kq;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateKqException implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String mainid = "";
		String ygxm = "";
		String rq = "";
		String cllx = "";//处理类型 0 上班未打卡 1 下班未打卡 2 全天未打开 3  迟到4 早退 5  未满8小时
		String bksbsj = "";//补卡上班时间
		String bk = "";//补卡下班时间\
		String gh = "";
		String sql_dt = "";
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
		}
		
		sql = "select * from "+tableName+"_dt1 where mainid="+mainid;
		//log.writeLog(this.getClass().getName(),"sql:"+sql);
		rs.execute(sql);
		while(rs.next()) {
			ygxm = Util.null2String(rs.getString("ygxm"));
			rq = Util.null2String(rs.getString("rq"));
			cllx = Util.null2String(rs.getString("cllx"));
			bksbsj = Util.null2String(rs.getString("bksbsj"));
			bk = Util.null2String(rs.getString("bk"));
			//log.writeLog(this.getClass().getName(),"cllx:"+cllx);
			sql_dt = " select workcode from hrmresource where id="+ygxm;
			rs_dt.execute(sql_dt);
			if(rs_dt.next()) {
				gh = Util.null2String(rs_dt.getString("workcode"));
			}
			if("0".equals(cllx)) {
				sql_dt = "insert into uf_signdata(rygh,dkrq,dksj,tbsj,eptype) values('"+gh+"','"+rq+"','"+bksbsj+"',CONVERT(varchar(100), GETDATE(), 121),'0')";
				rs_dt.execute(sql_dt);
				rs_dt.execute("exec calEmpDayAtten "+ygxm+",'"+rq+"',1");
			}else if("1".equals(cllx)) {
				sql_dt = "insert into uf_signdata(rygh,dkrq,dksj,tbsj,eptype) values('"+gh+"','"+rq+"','"+bk+"',CONVERT(varchar(100), GETDATE(), 121),'1')";
				rs_dt.execute(sql_dt);
				rs_dt.execute("exec calEmpDayAtten "+ygxm+",'"+rq+"',1");
			}else if("2".equals(cllx)) {
				sql_dt = "insert into uf_signdata(rygh,dkrq,dksj,tbsj,eptype) values('"+gh+"','"+rq+"','"+bksbsj+"',CONVERT(varchar(100), GETDATE(), 121),'0')";
				rs_dt.execute(sql_dt);
				sql_dt = "insert into uf_signdata(rygh,dkrq,dksj,tbsj,eptype) values('"+gh+"','"+rq+"','"+bk+"',CONVERT(varchar(100), GETDATE(), 121),'1')";
				rs_dt.execute(sql_dt);
				rs_dt.execute("exec calEmpDayAtten "+ygxm+",'"+rq+"',1");
			}else {
				sql_dt = "update uf_all_atten_info set is_deal='1' where emp_id="+ygxm+" and atten_day='"+rq+"'";
				rs_dt.execute(sql_dt);
			}
		}
		return SUCCESS;
	}

}
