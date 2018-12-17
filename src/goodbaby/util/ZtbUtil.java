package goodbaby.util;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class ZtbUtil {
	/**
	 * 
	 * @param name
	 * @param id   明细id
	 * @return
	 * @throws Exception
	 */
	public String getListColor(String name,String id) throws Exception{
		RecordSet rs = new RecordSet();
		String str = name;
		String 	BJJE_1 = "";
		String mid = "";
		String sql="select *  from  where id="+id;
		String avg ="";
		rs.executeSql(sql);
		if(rs.next()){
			BJJE_1 = Util.null2String(rs.getString("BJJE_1"));
			mid  = Util.null2String(rs.getString("mid"));
		}
		sql = "select isnull(CONVERT(DECIMAL(10,2),AVG(BJJE_1)),0) as avgs from (select MAX(id) as aid ,GYSBM,BJJE_1  from ("
		+"select TOP 5 h.BJJE_1 ,h.id ,h.GYSBM  from formtable_main_223_dt1 h where   GYSBM is not null and BJJE_1 is not null " +
		" group by GYSBM,h.id ,h.BJJE_1 ) a  group by GYSBM,id ,BJJE_1) b ";
		rs.executeSql(sql);
		if(rs.next()){
			avg = rs.getString("avgs");
		}
		Float BJJE = Float.parseFloat(BJJE_1);
		Float BJJE_vag = Float.parseFloat(avg);
		Float BJJE1 = (float) (BJJE_vag*0.1+BJJE_vag);
		if(BJJE_vag <= BJJE && BJJE < BJJE1){
			str = "<span style='color:yellow;'>"+name+"</span>";
		}else if(BJJE>=BJJE1){
			str = "<span style='color:orange;'>"+name+"</span>";
		}
	    return str;
	}
	
	
	
	
}
