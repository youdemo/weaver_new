package jsd.erp.util;
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
	public String delHTMLTag(String htmlStr) {
		String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
		String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
		String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
		Pattern p_script = Pattern.compile(regEx_script,
				Pattern.CASE_INSENSITIVE);
		Matcher m_script = p_script.matcher(htmlStr);
		htmlStr = m_script.replaceAll(""); // 过滤script标签
		Pattern p_style = Pattern
				.compile(regEx_style, Pattern.CASE_INSENSITIVE);
		Matcher m_style = p_style.matcher(htmlStr);
		htmlStr = m_style.replaceAll(""); // 过滤style标签
		Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
		Matcher m_html = p_html.matcher(htmlStr);
		htmlStr = m_html.replaceAll(""); // 过滤html标签
		return htmlStr.trim(); // 返回文本字符串
	}
	//获取流程
	public String getWorkflowid(String flag){
		String workflowid= Util.null2o(weaver.file.Prop.getPropValue("workflowids", flag));
		return workflowid;
	}
	
	
	public static void main(String[] args) {
		GetUtil a= new GetUtil();
		System.out.println(a.getNowdate());
	}
	

}
