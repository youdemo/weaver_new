package morningcore.contract.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class SysnoUtil {

	public String getMaxNum(String tableName){
		if(tableName == null) return "";
		tableName = tableName.toUpperCase();
		InsertUtil iu = new InsertUtil();
		RecordSet rs = new RecordSet();
		
		int count_cc = 0;
		String sql = "select count(id) as count_cc from uf_cx_sysno where strsys=null and " 
			 +" datesys=null and tableName='"+tableName+"'"; 
		rs.executeSql(sql);
		if(rs.next()){
			count_cc = rs.getInt("count_cc");
		}
		
		int num = 1;
		if(count_cc > 0){
			sql = "select nextseq from uf_cx_sysno where strsys=null and " 
					 +" datesys=null and tableName='"+tableName+"'";  
			rs.executeSql(sql);
			if(rs.next()){
				num = rs.getInt("nextseq");
			}
			
			sql = "update uf_cx_sysno set nextseq=nextseq+1 where strsys=null and " 
					 +" datesys=null and tableName='"+tableName+"'";
			rs.executeSql(sql);
			
		}else{
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("nextseq", "2");
			mapStr.put("tablename", tableName);
			
			iu.insert(mapStr, "uf_cx_sysno");
		}		
		
		return String.valueOf(num);
	}
	
	public String getNum(String str,String tableName,int index){
		if(tableName == null) return "";
		tableName = tableName.toUpperCase();
		InsertUtil iu = new InsertUtil();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		RecordSet rs = new RecordSet();
		
		int count_cc = 0;
		String sql = "select count(id) as count_cc from uf_cx_sysno where strsys='"+str+"' and " 
			 +" datesys='"+now+"' and tableName='"+tableName+"'"; 
		rs.executeSql(sql);
		if(rs.next()){
			count_cc = rs.getInt("count_cc");
		}
		
		int num  = 1;
		
		if(count_cc > 0){
			sql = "select nextseq from uf_cx_sysno where strsys='"+str+"' and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";  
			rs.executeSql(sql);
			if(rs.next()){
				num = rs.getInt("nextseq");
			}
			
			sql = "update uf_cx_sysno set nextseq=nextseq+1 where strsys='"+str+"' and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";
			rs.executeSql(sql);
			
		}else{
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("strsys", str);
			mapStr.put("datesys", now);
			mapStr.put("nextseq", "2");
			mapStr.put("tablename", tableName);
			
			iu.insert(mapStr, "uf_cx_sysno");
		}		
		String tmp = str + now + getStrNum(num,index);
		
		return tmp;
	}
	
	public String getStrNum(int num,int len){
		String buff = String.valueOf(num);
		int max = len - buff.length();
		for(int index = 0; index < max;index++){
			buff = "0" + buff;
		}
		return buff;
	}
	
	public String getTableMaxId(String tableName){
		RecordSet rs = new RecordSet();
		String nextNo = "1";
		String sql="select max(id)+1 as nextNo from "+tableName;
		rs.executeSql(sql);
		if(rs.next()){
			nextNo = Util.null2String(rs.getString("nextNo"));
		}
		if("".equals(nextNo)){
			nextNo = "1";
		}
		return nextNo;
	}

}
