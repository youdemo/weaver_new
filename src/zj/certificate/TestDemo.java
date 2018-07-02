package zj.certificate;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.general.Util;

public class TestDemo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String a1="144000.00";
		String a2="144000.01";
		System.out.println(Util.getFloatValue(a1)==Util.getFloatValue(a2));
		String cc="123";
		System.out.println(cc.split(",")[0]);
		String aa="1,11,11";
		System.out.println(aa.replaceAll(",", ""));
		float allMoney = 0.01f;
		System.out.println(Util.getFloatValue("", 0));
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		String now =sf.format(new Date());
		System.out.println(now);
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", "1");
		Map<String, String> mddStr = new HashMap<String, String>();
		mddStr=mapStr;
		mddStr.put("SET_OF_BOOKS_ID", "2");
		System.out.println(mapStr.get("SET_OF_BOOKS_ID"));
		System.out.println(mddStr.get("SET_OF_BOOKS_ID"));
		float a = 10.1f;
		float b = 2.2f;
		double alMoney=sub(a,b);
		System.out.println(alMoney);

	}
	public static double add(double v1,double v2){   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.add(b2).doubleValue();   
	}   
	
	public static double sub(double v1,double v2){   
		BigDecimal b1 = new BigDecimal(Double.toString(v1));   
		BigDecimal b2 = new BigDecimal(Double.toString(v2));   
		return b1.subtract(b2).doubleValue();   
	}   
}
