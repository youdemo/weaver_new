package jsd.util;

import java.awt.AWTException;
import java.awt.Robot;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;


/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-4-23 上午9:41:52
 * 类说明
 */
public class test implements Action{
	


	@Override
	public String execute(RequestInfo arg0) {
		Robot r=null;
		try {
			r = new   Robot();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        System.out.println( "延时前:"+new Date().toString()  ); 
        r.delay(   4000   );   
        System.out.println(   "延时后:"+new Date().toString()   );   
		return SUCCESS;
	} 
	
	public static void main(String[] args) {
		test t = new test();
	//	t.execute(null);
		double a = 1;
		int c = (int) a;
		System.out.println(c);
		t.readProperty1();
	}
	 private  void readProperty1() {
	        Properties properties = new Properties();
	        InputStream inputStream = Object.class.getResourceAsStream("D:\\WEAVER\\cology\\WEB-INF\\prop\\test.properties");
	        try {
	            properties.load(inputStream);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        System.out.println(properties.get("status"));
	    }
	     
	 private static void readProperty3() throws UnsupportedEncodingException {
	        ResourceBundle resourceBundle = ResourceBundle.getBundle("D:\\WEAVER\\cology\\WEB-INF\\prop\\test");
	        //遍历取值
	        Enumeration enumeration = resourceBundle.getKeys();
	        while (enumeration.hasMoreElements()) {
	            String value = resourceBundle.getString((String) enumeration.nextElement());
	                System.out.println(new String(value.getBytes("iso-8859-1"), "gbk"));
	        }

	 }

}
