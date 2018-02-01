package hsproject.dao;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class AlterTableFieild {
	/**
	 * 增加字段方法
	 * @param tableName
	 * @param type 0 单行文本,1 浏览框 2 check框 3 选择框 4 附件
	 * @param detailtype 0 文本, 1 整数, 2浮点数
	 * @param lenght 
	 */
	public boolean addTableField(String tableName,String fieldname,String type ,String detailtype,String lenght,String floatlength){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String sql="";
		if("1".equals(type)||"2".equals(type)||"3".equals(type)||"4".equals(type)){
			sql="alter table "+tableName+" add "+fieldname+" varchar(200)";
		}else if("0".equals(type)){
			if("0".equals(detailtype) && Util.getIntValue(lenght,0)>0){
				sql="alter table "+tableName+" add "+fieldname+" varchar("+lenght+")";
			}else if("1".equals(detailtype)){
				sql="alter table "+tableName+" add "+fieldname+" int";
			}else if("2".equals(detailtype)){
				sql="alter table "+tableName+" add "+fieldname+" decimal(12,"+floatlength+")";
			}
		}
		if(!"".equals(sql)){
			log.writeLog("addTableField:"+sql);
			if(!rs.executeSql(sql)){
				log.writeLog("addTableField fail:");
				return false;
			}
			return true;
		}
		return false;
	}
}
