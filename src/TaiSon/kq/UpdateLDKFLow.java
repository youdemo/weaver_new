package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.TimeUtil;
import weaver.general.Util;

public class UpdateLDKFLow {
		public void createFlow(String ryid) {
			if("".equals(ryid)) {
				return;
			}
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			String endDate =TimeUtil.dateAdd(sf.format(new Date()).substring(0, 7)+"-01", -1);
			String startDate = endDate.substring(0, 7)+"-01";
			KqUtil kq = new KqUtil();
			RecordSet rsff = new RecordSet();
			RecordSet rs_dt = new RecordSet();
			RecordSet rs_dt2 = new RecordSet();
			String sql_dt = "";
			String sql = "select id from hrmresource where (id in(select distinct xm from uf_tsmygskqry) or id in(select distinct xm from uf_jtzbkqry)) and id="+ryid;
			rsff.executeSql(sql);
			if(rsff.next()) {
				ryid = Util.null2String(rsff.getString("id"));
				sql_dt = "SELECT  TO_CHAR(TO_DATE('"+startDate+"', 'YYYY-MM-DD') + ROWNUM - 1, 'YYYY-MM-DD') DAY_ID   FROM DUAL " + 
						"CONNECT BY ROWNUM <= TO_DATE('"+endDate+"', 'YYYY-MM-DD') - TO_DATE('"+startDate+"', 'YYYY-MM-DD') + 1";
				rs_dt.execute(sql_dt);
				while(rs_dt.next()) {
					String cldate = Util.null2String(rs_dt.getString("DAY_ID"));
					rs_dt2.execute("{Call ks_calEmpDayAtten("+ryid+",'"+cldate+"')}");
					
				}
			}			
			kq.insertLdkData(startDate, endDate);
			int count =0;
			sql = "select count(1) as count from uf_kq_ldk_mid where ryid='"+ryid+"'";
			rsff.executeSql(sql);
			if(rsff.next()) {
				count = rsff.getInt("count");
			}
			if(count >0) {
				CreateLDKFlowAction clfa = new CreateLDKFlowAction();
				try {
					clfa.createFlow(ryid);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
}
