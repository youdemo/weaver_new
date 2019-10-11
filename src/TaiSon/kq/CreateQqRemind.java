package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class CreateQqRemind extends BaseCronJob{
	public void execute(){
		writeLog("开始创建缺勤提醒流程");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String startDate = TimeUtil.dateAdd(now, -7);
		String endDate = TimeUtil.dateAdd(now, -1);
		createQqRemind(startDate,endDate);
		writeLog("结束缺勤提醒");
	}
	
	public void createQqRemind(String startDate,String endDate) {
		KqUtil kq = new KqUtil();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String ryid = "";
		kq.insertQQData(startDate, endDate);
		String sql = "select distinct ryid from uf_kq_qq_mid";
		String sql_dt = "";
		rs.executeSql(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("ryid"));
			String title = "缺勤提醒";
			StringBuffer bzString = new StringBuffer();
			bzString.append("温馨提示：请核实上周缺勤，尽快提交请假流程");									
			kq.createRemindWorkflow(title, 1, ryid, bzString.toString());
		}
	}
	
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
