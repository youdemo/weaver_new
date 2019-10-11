package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateSignTableData extends BaseCronJob{
	public void execute(){
		writeLog("更新考情中间表");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate =sf.format(new Date());
		String startDate = TimeUtil.dateAdd(endDate.substring(0, 7)+"-01", -1).substring(0, 7)+"-01";
		endDate = TimeUtil.dateAdd(endDate, -1);
		updateStatus(startDate,endDate);
		writeLog("更新考情中间表结束");
	}
	
	public void updateStatus(String startDate,String endDate) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		RecordSet rs_dt2 = new RecordSet();
		String sql_dt = "";
		String ryid = "";
		String sql = "select id from hrmresource where (id in(select distinct xm from uf_tsmygskqry) or id in(select distinct xm from uf_jtzbkqry))";
		rs.executeSql(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("id"));
			sql_dt = "SELECT  TO_CHAR(TO_DATE('"+startDate+"', 'YYYY-MM-DD') + ROWNUM - 1, 'YYYY-MM-DD') DAY_ID   FROM DUAL " + 
					"CONNECT BY ROWNUM <= TO_DATE('"+endDate+"', 'YYYY-MM-DD') - TO_DATE('"+startDate+"', 'YYYY-MM-DD') + 1";
			rs_dt.execute(sql_dt);
			while(rs_dt.next()) {
				String cldate = Util.null2String(rs_dt.getString("DAY_ID"));
				rs_dt2.execute("{Call ks_calEmpDayAtten("+ryid+",'"+cldate+"')}");
				
			}
		}						
	}
	
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
