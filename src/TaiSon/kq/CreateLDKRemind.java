package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class CreateLDKRemind extends BaseCronJob{
	public void execute(){
		writeLog("开始创建漏打卡提醒流程");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String startDate = TimeUtil.dateAdd(now, -7);
		String endDate = TimeUtil.dateAdd(now, -1);
		createLDKRemind(startDate,endDate);
		writeLog("漏打卡提醒流结束");
	}
	
	public void createLDKRemind(String startDate,String endDate) {
		KqUtil kq = new KqUtil();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String ryid = "";
		kq.insertLdkData(startDate, endDate);
		String sql = "select distinct ryid from uf_kq_ldk_mid";
		String sql_dt = "";
		rs.executeSql(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("ryid"));
			String title = "漏打卡提醒";
			StringBuffer bzString = new StringBuffer();
			sql_dt = "select * from uf_kq_ldk_mid where ryid='"+ryid+"' order by id asc";
			rs_dt.executeSql(sql_dt);
			while(rs_dt.next()) {
				String rq = Util.null2String(rs_dt.getString("rq"));
				String type = Util.null2String(rs_dt.getString("type"));
				if(!"".equals(bzString.toString())) {
					bzString.append("<br/>");				
				}
				if("0".equals(type)) {
					bzString.append("您好，您于"+rq+"上班遗漏打卡一次");	
				}else {
					bzString.append("您好，您于"+rq+"下班遗漏打卡一次");	
				}
			}
			
			
			kq.createRemindWorkflow(title, 1, ryid, bzString.toString());
		}
	}
	
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
