package jsd.ape.util;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 
 * @author 张瑞坤
 */
public class GetUtil {
	

	public String getNowDate(){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return format.format(new Date());
		
	}	
	public String getNowDatet(){
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
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
	public String getDepCode(String userid){
		String sql = "select d.departmentcode from hrmresource h join hrmdepartment d on d.id =h.departmentid where  h.id = '" + userid + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("departmentcode"));
	    	return name;
	    }
	    return "0";
	}
	public String getDepName(String userid){
		String sql = "select d.departmentname from hrmresource h join hrmdepartment d on d.id =h.departmentid where  h.id = '" + userid + "'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("departmentname"));
	    	return name;
	    }
	    return "0";
	}
	public String getLogtypee(String logtype){
		if(logtype.equals("0")){
			return "批准";
		}else if(logtype.equals("1")){
			return "保存";
		}else if(logtype.equals("2")){
			return "提交";
		}else if(logtype.equals("3")){
			return "退回";
		}else if(logtype.equals("4")){
			return "归档";
		}else if(logtype.equals("5")){//20190118
			return "删除";
		}else if(logtype.equals("6")){
			return "激活";
		}else if(logtype.equals("7")){
			return "转发";
		}else if(logtype.equals("9")){
			return "批注";
		}else if(logtype.equals("e")){
			return "强制归档";
		}else if(logtype.equals("t")){
			return "抄送";
		}else if(logtype.equals("s")){////20190118
			return "督办";
		}
		return " ";
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
	public String getSelectVal(String fieldid,String selectvalue){
		String sql = "select * from workflow_selectitem where  fieldid = '" + fieldid + "' and selectvalue='"+selectvalue+"'";
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
	    if(rs.next()){
	    	String name = Util.null2String(rs.getString("selectname"));
	    	return name;
	    }
	    return "";
	}
	/**
	 * 保留4位小数
	 * @param str
	 * @return
	 */
	public String getFour(String str){
		if(str.length()<1){
			str = "0.0000";
		}
		double std = Double.valueOf(str);
		DecimalFormat df = new DecimalFormat("#0.00");
		return String.valueOf(df.format(std));		
	}
	public String getInt(String str){
		if(str.length()<1){
			str = "0";
		}
		double std = Double.valueOf(str);
		DecimalFormat df = new DecimalFormat("#");
		return String.valueOf(df.format(std));
		
	}
	public static void main(String[] args) {
		GetUtil  g=new GetUtil();
////		//String bb=g.getOAFormat("00000000");
////		System.out.println(g.getNowDate());
//		String result = "{\"return_type\":\"E\",\"return_message\":\"XXXX\",\"return_json\":{\"aa\":\"bb\"}}";
//		try {
//			JSONObject  JsonResult = new JSONObject(result);
//			String return_type = JsonResult.getString("return_type");
//			String return_message = JsonResult.getString("return_message");
//			
//			System.out.println(return_type);
//			System.out.println(return_message);
//			JSONObject  return_json = (JSONObject) JsonResult.get("return_json");
//			System.out.println(return_json.getString("aa"));
//			
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}	
//		System.out.println(g.getFour("1.000000000"));
//		System.out.println(g.getFour("1.000000009"));
		System.out.println(g.getFour("11111.160000000"));
		System.out.println(g.getFour("0.140000000"));
//		System.out.println(g.getFour("1.000999999"));
//		System.out.println(g.getInt("1.999999999"));
//		
	}

}
