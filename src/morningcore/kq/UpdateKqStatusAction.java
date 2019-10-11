package morningcore.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateKqStatusAction extends BaseCronJob{
	
	public void execute(){
		BaseBean log = new BaseBean();
		log.writeLog("开始更新考勤数据");
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String kqDate = TimeUtil.dateAdd(nowDate, -1);
		updatekqDate(kqDate);
		log.writeLog("更新考勤数据结束");
	}
	
	public void updatekqDate(String kqDate) {
		RecordSet rs_dt = new RecordSet();
		RecordSet rs = new RecordSet();
		String ryid = "";
		String sql  = "	select id from hrmresource where status<5 and isnull(belongto,-1)<=0 and id not in(select resourceid from HrmRoleMembers where roleid=1041)";
		rs.execute(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
			rs_dt.execute("exec calEmpDayAtten "+ryid+",'"+kqDate+"',1");			
		}
	}
	

}
