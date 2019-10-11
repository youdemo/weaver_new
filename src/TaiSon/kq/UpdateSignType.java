package TaiSon.kq;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateSignType extends BaseCronJob{
	public void execute(){
		writeLog("更新打卡类型开始");
		updatetime();
		writeLog("更新打卡类型结束");
	}
	
	public void updatetime() {
		RecordSet rs = new RecordSet();
		String qfsj = "";
		String sql = "select qfsj from uf_signout_time";
		rs.executeSql(sql);
		if(rs.next()) {
			qfsj = Util.null2String(rs.getString("qfsj"));
		}
		if(!"".equals(qfsj)) {
			qfsj = qfsj+":00";
		}
		sql="update hrmschedulesign set signtype=2 where signtime>='"+qfsj+"' and signtype=1 and app_what_holiday(signdate)=2";
		writeLog("更新打卡类型sql"+sql);
		rs.executeSql(sql);
		
		
	}
	
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
