package goodbaby.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 
 * @author 张瑞坤
 *0626
 */
public class GetUtil {
	

	public String getNowDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
		
	}	
	public String getFieldVal(String tableName,String fieldName,String wherekey,String workcode){
		String sql = "select * from "+ tableName + " where  "+wherekey+" = '" + workcode + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	
	public static void main(String[] args) {
		GetUtil  g=new GetUtil();
		//String bb=g.getOAFormat("00000000");
		System.out.println(g.getNowDate());
	}

}
