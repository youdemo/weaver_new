package weixin.mflex.hr;

import java.sql.CallableStatement;
import java.sql.SQLException;
import java.sql.Types;

import COM.rsa.jsafe.cs;

import weaver.general.StaticObj;
import weaver.interfaces.datasource.DataSource;

public class Test {
 public static void main(String[] args){
	 String date="2017-02-10";
	 System.out.println(date.substring(2,4));
	    
 }
 public static String getStrNum(String  requestid,int len){
		String buff = requestid;
		int max = len - buff.length();
		for(int index = 0; index < max;index++){
			buff = "0" + buff;
		}
		return buff;
	}
}
