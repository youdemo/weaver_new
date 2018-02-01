package zj.certificate;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class GetSeqNum {
	
	public String getNum(String str,String tableName,int index){
		if(tableName == null) return "";
		tableName = tableName.toUpperCase();
		InsertUtil iu = new InsertUtil();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		RecordSet rs = new RecordSet();
		String maxid="1";
		
		int count_cc = 0;
		String sql = "select count(id) as count_cc from zj_SysNo where strsys is null and " 
			 +" datesys='"+now+"' and tableName='"+tableName+"'"; 
		rs.executeSql(sql);
		if(rs.next()){
			count_cc = rs.getInt("count_cc");
		}
		sql="select isnull(max(id),0)+1 as maxid from zj_SysNo ";
		rs.executeSql(sql);
		if(rs.next()){
			maxid = Util.null2String(rs.getString("maxid"));
		}
		int num  = 1;
		
		if(count_cc > 0){
			sql = "select nextseq from zj_SysNo where strsys is null and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";  
			rs.executeSql(sql);
			if(rs.next()){
				num = rs.getInt("nextseq");
			}
			
			sql = "update zj_SysNo set nextseq=nextseq+1 where strsys is null and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";
			rs.executeSql(sql);
			
		}else{
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("id", maxid);
			mapStr.put("strsys", str);
			mapStr.put("datesys", now);
			mapStr.put("nextseq", "2");
			mapStr.put("tablename", tableName);
			
			iu.insert(mapStr, "zj_SysNo");
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
}
