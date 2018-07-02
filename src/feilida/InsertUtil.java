package feilida;

import java.util.Iterator;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

public class InsertUtil {

	BaseBean log  = new BaseBean();
	
	// 本系统的数据插入操作
	public boolean insert(Map<String,String> mapStr,String table){
		if(mapStr == null) return false;
		if(mapStr.isEmpty()) return false;

		RecordSet rs = new RecordSet();
		
		String sql_0 = "insert into "+table+"(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";
		
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();
		//	String tmp_1_str = Util.null2String(mapStr.get(tmp_1));		
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			
			if(tmp_1_str.length() > 0){
				sql_1.append(tmp_1);sql_1.append(",");
				
				if(tmp_1_str.contains("##")){
					sql_3.append(tmp_1_str.replace("##", ""));sql_3.append(",");
				}else{
					sql_3.append("'");sql_3.append(tmp_1_str);sql_3.append("',");
				}
			}
		}
		
		String now_sql_1 = sql_1.toString();
		if(now_sql_1.lastIndexOf(",")>0){
			now_sql_1 = now_sql_1.substring(0,now_sql_1.length()-1);
		}
		
		String now_sql_3 = sql_3.toString();
		if(now_sql_3.lastIndexOf(",")>0){
			now_sql_3 = now_sql_3.substring(0,now_sql_3.length()-1);
		}
		
		String sql = sql_0 + now_sql_1 + sql_2 + now_sql_3 + sql_4;
		
	//	return false;
		return rs.executeSql(sql);
	}
	
	// 本系统的数据插入操作    有父级
	public boolean update(Map<String,String> mapStr,String table,String uqField,String uqVal){
		if(mapStr == null || mapStr.isEmpty()) return false;
		if(uqField == null || "".equals(uqField)) return false;
		if(uqVal == null|| "".equals(uqVal)) return false;
			
		RecordSet rs = new RecordSet();
			
		StringBuffer buff = new StringBuffer();
		buff.append("update ");buff.append(table);buff.append(" set ");
			
			
		Iterator<String> it = mapStr.keySet().iterator();
		String flag = "";
		while(it.hasNext()){
			String tmp_1 = it.next();	
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
				
			if(tmp_1_str.length() > 0){
				buff.append(flag);buff.append(tmp_1);buff.append("=");
					
				if(tmp_1_str.contains("##")){
					buff.append(tmp_1_str.replace("##", ""));
				}else{
					buff.append("'");buff.append(tmp_1_str);buff.append("'");
				}
				flag = ",";
				}
				
			}
			
			buff.append(" where ");buff.append(uqField);buff.append("='");buff.append(uqVal);
			// 一定要保持无级联父类
			buff.append("' and superid is null");
			
			String sql = buff.toString();

			
	//		System.out.println("## sql = " + sql);
			 
		//	return false;
			return rs.executeSql(sql);
		}
	
	// 本系统的数据插入操作   
	public boolean updateGen(Map<String,String> mapStr,String table,String uqField,String uqVal,String uqVal2){
		if(mapStr == null || mapStr.isEmpty()) return false;
		if(uqField == null || "".equals(uqField)) return false;
		if(uqVal == null|| "".equals(uqVal)) return false;
		if(uqVal2 == null|| "".equals(uqVal2)) return false;

				
		RecordSet rs = new RecordSet();
				
		StringBuffer buff = new StringBuffer();
		buff.append("update ");buff.append(table);buff.append(" set ");
				
				
		Iterator<String> it = mapStr.keySet().iterator();
		String flag = "";
		while(it.hasNext()){
			String tmp_1 = it.next();	
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
					
				buff.append(flag);buff.append(tmp_1);buff.append("=");
						
				if(tmp_1_str.contains("##")){
					buff.append(tmp_1_str.replace("##", ""));
				}else{
					buff.append("'");buff.append(tmp_1_str);buff.append("'");
				}
				flag = ",";			
			}
				
			buff.append(" where ");buff.append(uqField);
			buff.append("='");buff.append(uqVal);
			buff.append("' ");buff.append(uqVal2);
			
			String sql = buff.toString();
			//log.writeLog("sql="+sql);
				
			return rs.executeSql(sql);
		}
	
	// 外系统的数据插入操作   RecordSetDataSource rsx
	public boolean insert(Map<String,String> mapStr,String table,RecordSetDataSource rsx){
		if(mapStr == null) return false;
		if(mapStr.isEmpty()) return false;
		
		String sql_0 = "insert into "+table+"(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";
		
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();
			String tmp_1_str = Util.null2String(mapStr.get(tmp_1));		
			if(tmp_1_str.length() > 0){
				sql_1.append(tmp_1);sql_1.append(",");
				
				if(tmp_1_str.contains("##")){
					sql_3.append(tmp_1_str.replace("##", ""));sql_3.append(",");
				}else{
					sql_3.append("'");sql_3.append(tmp_1_str);sql_3.append("',");
				}
			}
		}
		
		String now_sql_1 = sql_1.toString();
		if(now_sql_1.lastIndexOf(",")>0){
			now_sql_1 = now_sql_1.substring(0,now_sql_1.length()-1);
		}
		
		String now_sql_3 = sql_3.toString();
		if(now_sql_3.lastIndexOf(",")>0){
			now_sql_3 = now_sql_3.substring(0,now_sql_3.length()-1);
		}
		
		String sql = sql_0 + now_sql_1 + sql_2 + now_sql_3 + sql_4;
		
		return rsx.executeSql(sql);
	}
}
