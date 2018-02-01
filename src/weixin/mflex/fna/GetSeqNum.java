package weixin.mflex.fna;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class GetSeqNum {
	
	public String getNum(String str,String tableName,int index,String nf){
		if(tableName == null) return "";
		tableName = tableName.toUpperCase();
		InsertUtil iu = new InsertUtil();
		RecordSet rs = new RecordSet();
		String maxid="1";
		
		int count_cc = 0;
		String sql = "select count(id) as count_cc from wx_SysNo where strsys ='"+str+"' and " 
			 +" datesys='"+nf+"' and tableName='"+tableName+"'"; 
		rs.executeSql(sql);
		if(rs.next()){
			count_cc = rs.getInt("count_cc");
		}
		sql="select isnull(max(id),0)+1 as maxid from wx_SysNo ";
		rs.executeSql(sql);
		if(rs.next()){
			maxid = Util.null2String(rs.getString("maxid"));
		}
		int num  = 1;
		
		if(count_cc > 0){
			sql = "select nextseq from wx_SysNo where strsys ='"+str+"' and " 
					 +" datesys='"+nf+"' and tableName='"+tableName+"'";  
			rs.executeSql(sql);
			if(rs.next()){
				num = rs.getInt("nextseq");
			}
			
			sql = "update wx_SysNo set nextseq=nextseq+1 where strsys ='"+str+"' and " 
					 +" datesys='"+nf+"' and tableName='"+tableName+"'";
			rs.executeSql(sql);
			
		}else{
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("id", maxid);
			mapStr.put("strsys", str);
			mapStr.put("datesys", nf);
			mapStr.put("nextseq", "2");
			mapStr.put("tablename", tableName);
			
			iu.insert(mapStr, "wx_SysNo");
		}		
		String tmp = str + nf + getStrNum(num,index);
		
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
