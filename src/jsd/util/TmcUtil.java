package jsd.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * 工具类 整合系统公共内容
 * @author Tony
 * @version 1.0 2017-12-19
 */
public class TmcUtil {
	BaseBean log = new BaseBean();
	/**
	 * 插入OA本地系统数据 
	 * @param table  插入的表名
	 * @param mapStr 需要插入的内容 ，以map中key和value存储，如有转换之轻易 ## 开头
	 * @return
	 */
	public boolean insert(String table,Map<String,String> mapStr){
		RecordSet rs = new RecordSet();
		String sql = getInsertSql(table, mapStr);
		if(Util.null2String(sql).length() < 5) {
			log.writeLog("异常: 拼接sql异常！ table ->" + table + " ; mapStr->" + mapStr);
			return false;
		}
		log.writeLog("拼接sql！" + sql);
		return rs.executeSql(sql);
	}
	
	/**
	 * 需要插入 以后系统的方式
	 * @param table  插入的表名
	 * @param mapStr 需要插入的内容 ，以map中key和value存储，如有转换之轻易 ## 开头
	 * @param rsds 异构系统的数据源连接
	 * @return
	 */
	public boolean insert(String table,Map<String,String> mapStr,RecordSetDataSource rsds){
		String sql = getInsertSql(table, mapStr);
		if(Util.null2String(sql).length() < 5) {
			log.writeLog("异常: 拼接sql异常！ table ->" + table + " ; mapStr->" + mapStr);
			return false;
		}
		return rsds.executeSql(sql);
	}
	
	/**
	 * oa系统需要更新内容
	 * @param table 更新表
	 * @param mapStr 更新的内容存储
	 * @param whereMap 更新的条件存储
	 * @return
	 */
	public boolean updateGen(String table,Map<String,String> mapStr,Map<String,String> whereMap){
		RecordSet rs = new RecordSet();
		String sql = getUpdateSql(table,mapStr,whereMap);
		if(Util.null2String(sql).length() < 5) {
			log.writeLog("异常: 拼接sql异常！ table ->" + table + " ; mapStr->" 
					+ mapStr + " ; whereMap->" + whereMap);
			return false;
		}
		log.writeLog("拼接sql！" + sql);
		return rs.executeSql(sql);
	}
	
	/**
	 * 以后系统需要更新的内容
	 * @param table 更新表
	 * @param mapStr 更新的内容存储
	 * @param whereMap 更新的条件存储
	 * @param rsds 异构系统的数据源连接
	 * @return
	 */
	public boolean updateGen(String table,Map<String,String> mapStr,
				Map<String,String> whereMap,RecordSetDataSource rsds){
		RecordSet rs = new RecordSet();
		String sql = getUpdateSql(table,mapStr,whereMap);
		if(Util.null2String(sql).length() < 5) {
			log.writeLog("异常: 拼接sql异常！ table ->" + table + " ; mapStr->" 
					+ mapStr + " ; whereMap->" + whereMap);
			return false;
		}
		return rs.executeSql(sql);
	}
	
	/**
	 * 在OA系统中，如果存在就更新；如果不存在插入
	 * @param table
	 * @param mapStr
	 * @param whereMap
	 * @return
	 */
	public boolean saveOrUpdate(String table,Map<String,String> mapStr,Map<String,String> whereMap){
		// 判断传入值是否正常
		if(table == null || table.length() < 1) return false;
		if(mapStr == null || mapStr.isEmpty()) return false;
		if(whereMap == null || whereMap.isEmpty()) return false;
		
		RecordSet rs = new RecordSet();
		String whereSql = getWhereSql(whereMap);
		if(whereSql == null || whereSql.length() < 1) return false;
		
		// 判断值是否存在
		String sql = " select count(1) as mct from " + table + whereSql;
		rs.executeSql(sql);
		int isExit = 0;
		if(rs.next()){
			isExit = rs.getInt("mct");
		}
		if(isExit > 0){
			// 存在更新
			return updateGen(table,mapStr,whereMap);
		}else{
			// 不存在 插入
			return insert(table,mapStr);
		}
	}
	
	
	/**
	 * 把map中的key val 拼接sql
	 * @param table  插入的表名
	 * @param mapStr 需要插入的内容 ，以map中key和value存储，如有转换之轻易 ## 开头
	 * @return
	 */
	private String getInsertSql(String table,Map<String,String> mapStr){
		if(mapStr == null || mapStr.isEmpty()) return "";
		if(table == null || "".equals(table)) return "";
		String sql_0 = "insert into "+table+"(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			if(tmp_1_str.contains("'")||tmp_1_str.contains("&")){
				if(!tmp_1_str.contains("########")){
					String tmp_1_strt = tmp_1_str.replace("'", "'||chr(39)||'").replace("&", "'||chr(38)||'");
					tmp_1_str=tmp_1_strt;
				}
			}
			if(tmp_1_str.length() > 0){
				sql_1.append(tmp_1);sql_1.append(",");
				
				if(tmp_1_str.contains("########")){
					sql_3.append(tmp_1_str.replace("########", ""));sql_3.append(",");
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
		return sql;
	}
	
	/**
	 * 获取更新拼接的语句
	 * @param table 更新表
	 * @param mapStr 更新的内容存储
	 * @param whereMap 更新的条件存储
	 * @return
	 */
	private String getUpdateSql(String table,Map<String,String> mapStr,Map<String,String> whereMap){
		if(table == null || table.length() < 1) return "";
		if(mapStr == null || mapStr.isEmpty()) return "";
		if(whereMap == null || whereMap.isEmpty()) return "";
		
		String whereSql = getWhereSql(whereMap);
		if(whereSql == null || whereSql.length() < 1) return "";
		
		StringBuffer buff = new StringBuffer();
		buff.append("update ");
		buff.append(table);
		buff.append(" set ");
		String flag = "";
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();	
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			if(tmp_1_str.contains("'")||tmp_1_str.contains("&")){
				if(!tmp_1_str.contains("########")){
					String tmp_1_strt = tmp_1_str.replace("'", "'||chr(39)||'").replace("&", "'||chr(38)||'");
					tmp_1_str=tmp_1_strt;
				}
			}		
			if(tmp_1_str.length() > 0){
				buff.append(flag);buff.append(tmp_1);buff.append("=");
						
				if(tmp_1_str.contains("########")){
					buff.append(tmp_1_str.replace("########", ""));
				}else{
					buff.append("'");buff.append(tmp_1_str);buff.append("'");
				}
				flag = ",";
			}else{
				buff.append(flag);buff.append(tmp_1);buff.append("=null");
				flag = ",";
			}
		}
		String sql = buff.toString() + whereSql;
	//	log.writeLog("## sql = " + sql);
		return sql;
	}
	
	/**
	 * 获取更新拼接的语句  null 更新过滤 
	 * @param table 更新表
	 * @param mapStr 更新的内容存储
	 * @param whereMap 更新的条件存储
	 * @return
	 */
	private String getUpdateSqlNotNull(String table,Map<String,String> mapStr,Map<String,String> whereMap){
		if(table == null || table.length() < 1) return "";
		if(mapStr == null || mapStr.isEmpty()) return "";
		if(whereMap == null || whereMap.isEmpty()) return "";
		
		String whereSql = getWhereSql(whereMap);
		if(whereSql == null || whereSql.length() < 1) return "";
		
		StringBuffer buff = new StringBuffer();
		buff.append("update ");
		buff.append(table);
		buff.append(" set ");
		String flag = "";
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();	
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			if(tmp_1_str.contains("'")||tmp_1_str.contains("&")){
				if(!tmp_1_str.contains("########")){
					String tmp_1_strt = tmp_1_str.replace("'", "'||chr(39)||'").replace("&", "'||chr(38)||'");
					tmp_1_str=tmp_1_strt;
				}
			}		
			if(tmp_1_str.length() > 0){
				buff.append(flag);buff.append(tmp_1);buff.append("=");
						
				if(tmp_1_str.contains("########")){
					buff.append(tmp_1_str.replace("########", ""));
				}else{
					buff.append("'");buff.append(tmp_1_str);buff.append("'");
				}
				flag = ",";
			}
		}
		String sql = buff.toString() + whereSql;
	//	log.writeLog("## sql = " + sql);
		return sql;
	}
	
	private String getWhereSql(Map<String,String> whereMap){
		if(whereMap == null || whereMap.isEmpty()) return "";
		StringBuffer whereBuff = new StringBuffer();
		whereBuff.append(" where 1 = 1 ");
		Iterator<String> it = whereMap.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();	
			String tmp_1_str = whereMap.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
					
			if(tmp_1_str.length() > 0){
				whereBuff.append(" and ");
				whereBuff.append(tmp_1);
				whereBuff.append(" = ");
						
				if(tmp_1_str.contains("########")){
					whereBuff.append(tmp_1_str.replace("########", ""));
				}else{
					whereBuff.append("'");
					whereBuff.append(tmp_1_str);
					whereBuff.append("'");
				}
			}
		}
		return whereBuff.toString();
	}
	
	
	public static void main(String[] args) {
		TmcUtil tu = new TmcUtil();
		String table = "";
		Map<String,String> mapStr = new HashMap<String,String>();
		Map<String,String> whereMap = new HashMap<String,String>();
		System.out.println(tu.getUpdateSql(table, mapStr, whereMap));
	}
	
}

