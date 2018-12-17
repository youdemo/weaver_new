package gvo.hr.sysn.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.Util;

public class Test {

	public static void main(String[] args) throws Exception {
		Map<String, String> aa= new HashMap<String, String>();
		String aaString = Util.null2String(aa.get("aaa"));
		System.out.println(aaString);
		JSONObject json= new JSONObject();
		json.put("aaa", "123");
		String aaa=json.toString();
		JSONObject ccc= new JSONObject("{\"aaa\":}") ;
		
		BigDecimal dou=BigDecimal.valueOf(ccc.getDouble("aaa")) ;
		System.out.println(dou.stripTrailingZeros().toPlainString());
		
		
	}

}
