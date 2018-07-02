package gvo.costcontrol.util;

import java.util.Iterator;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

public class InsertUtil {

	BaseBean log  = new BaseBean();
	
	// ��ϵͳ����ݲ������
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

		log.writeLog("## sql = " + sql);
		
	//	return false;
		return rs.executeSql(sql);
	}
	
	// ��ϵͳ����ݲ������    �и���
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
			// һ��Ҫ�����޼�������
			buff.append("' and superid is null");
			
			String sql = buff.toString();

			log.writeLog("## sql = " + sql);
			
	//		System.out.println("## sql = " + sql);
			
		//	return false;
			return rs.executeSql(sql);
		}
	
	// ��ϵͳ����ݲ������   
	public boolean updateGen(Map<String,String> mapStr,String table,String uqField,String uqVal){
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
					 			
				buff.append(flag);buff.append(tmp_1);buff.append("=");					
				if(tmp_1_str.contains("##")){
					buff.append(tmp_1_str.replace("##", ""));
				}else{
					if("".equals(tmp_1_str)){
						buff.append("null");
					}else{
						buff.append("'");buff.append(tmp_1_str);buff.append("'");
					}
					
				} 
				flag = ",";
			
					
			}
				
			buff.append(" where ");buff.append(uqField);
			buff.append("='");buff.append(uqVal);
			buff.append("' ");
			
			String sql = buff.toString();
 
			log.writeLog("## sql = " + sql);
			boolean re=rs.executeSql(sql);
//			if("formtable_main_73_dt1".equals(table)){
//				
//				 sql ="update formtable_main_73_dt1 set yhje = (select price*ActualNumbers from formtable_main_73_dt1 where val_1='"+uqVal+"') ,Money =(select price*ActualNumbers from formtable_main_73_dt1 where val_1='"+uqVal+"') where val_1='"+uqVal+"'";
//				 rs.executeSql(sql);
//			}
				
			return re;
			
			 
		}
	
	// ��ϵͳ����ݲ������   RecordSetDataSource rsx
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

		log.writeLog("## sql = " + sql);
		
		return rsx.executeSql(sql);
	}
}
