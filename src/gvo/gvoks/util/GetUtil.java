package gvo.gvoks.util;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-1-10 上午10:32:18
 * 类说明
 */
public class GetUtil {
	/**
	 * 
	 * @param tableName  表
	 * @param fieldName  目标字段
	 * @param filed1     条件字段
	 * @param filed2  条件字段值
	 * @return
	 */
	public String getFieldVal(String tableName,String fieldName,String filed1,String filed2){
		String sql = "select * from "+ tableName + " where "+filed1+" = '" + filed2 + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	
	public String getDepVal(String tableName,String fieldName,String filed1,String filed2){
		String sql = "select * from "+ tableName + " where "+filed1+" in " + filed2;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString(fieldName));
	    	return name;
	    }
	    return "";
	}
	public String getNowdate(){
		Date da = new Date();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sf.format(da);
		
	}
	
	public  double mul(String m1, String m2) {
		 BigDecimal p1 = new BigDecimal(m1);
		 BigDecimal p2 = new BigDecimal(m2);
		 return p1.multiply(p2).doubleValue();
	}
	/**
	 * 时间差  转为小时差
	 * @param m1
	 * @param m2
	 * @return
	 * @throws ParseException
	 */
	public  double subDouble(String m1, String m2) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1 = sf.parse(m1);
		Date date2 = sf.parse(m2);
		Double d1 = (double) date1.getTime();
		Double d2 = (double) date2.getTime();
		BigDecimal p1 = new BigDecimal(Double.valueOf(d1).toString());
		BigDecimal p2 = new BigDecimal(Double.valueOf(d2).toString());
		double time = p2.subtract(p1).doubleValue();
		double th = div(time, 3600000, 2);
		return th;
	}
	/**
	 * 算术减
	 * @param m1
	 * @param m2
	 * @return
	 */
	public  double subDouble1(String m1, String m2){
		BigDecimal p1 = new BigDecimal(m1);
		BigDecimal p2 = new BigDecimal(m2);
		return p2.subtract(p1).doubleValue();
	}
	/**
	 * 除法
	 * @param m1
	 * @param m2
	 * @param scale
	 * @return
	 */
	public static double div(double m1, double m2, int scale) {
		if (scale < 0) {
			throw new IllegalArgumentException("Parameter error");
		}
		BigDecimal p1 = new BigDecimal(Double.toString(m1));
		BigDecimal p2 = new BigDecimal(Double.toString(m2));
		return p1.divide(p2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	
	
	
	public static void main(String[] args) throws ParseException {
		GetUtil a= new GetUtil();
		System.out.println(a.subDouble("2018-01-01 11:11:11","2018-01-01 11:12:11"));
		System.out.println(5.0/Double.valueOf(5));
		System.out.println(13.5%5);
	}
	

}
