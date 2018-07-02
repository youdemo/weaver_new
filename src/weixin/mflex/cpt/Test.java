package weixin.mflex.cpt;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;

public class Test {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		String aaa="'123;'  　";
		System.out.println(new String(aaa.getBytes(),"WE8MSWIN1252"));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time = sdf.format(new Date());
		System.out.println(time);
		System.out.println(time.substring(0, 4).replaceAll("^(0+)", ""));
		System.out.println(time.substring(4, 6).replaceAll("^(0+)", ""));
		System.out.println(time.substring(6, 8).replaceAll("^(0+)", ""));
		String aa="123,123";
		System.out.println((","+aa+",").indexOf(","+"1253"+","));
//		JSONArray array = new JSONArray();
//		JSONObject head = new JSONObject();
//		
//		try {
//			head.put("I_BUDAT","");//凭证中的过帐日期   oa 日期
//			head.put("I_BUKRS","");//公司代码   oa 公司代码
//			head.put("I_WAERS","");//货币码   oa 货币码
//			head.put("I_SGTXT","");//项目文本   oa 借款事由
//			head.put("I_WRBTR","");//凭证货币金额   oa 借款金额
//			head.put("I_LIFNR","");//供应商或债权人的帐号   oa 工号
//			head.put("I_BLDAT","");//凭证中的凭证日期   oa 日期
//			head.put("I_ZLSCH","E");//付款方式   oa 	付款方式
//			
//			head.put("I_DZUMSK","");//目标特别总帐标志   oa 借款类型
//			head.put("I_ZFBDT","");// 	期望付款日期
//			head.put("I_EBELN", "");
//			head.put("I_EBELP", "");
//			head.put("I_ZUONR", "");
//		} catch (Exception e) {
//			
//		}
//		array.put(head);
//		System.out.println(array.toString());
		JSONArray array = new JSONArray();
		JSONObject head = new JSONObject();
		JSONArray array1 = new JSONArray();
		JSONArray array2 = new JSONArray();
		
		
			JSONObject jo = new JSONObject();
			try {
				jo.put("BSCHL", "40");
				jo.put("HKONT", "1002010321");
				jo.put("LIFNR", "");
				jo.put("KUNNR", "");
				jo.put("UMSKZ", "");
				jo.put("WRBTR", "");
				jo.put("EBELN", "");
				jo.put("EBELP", "");
				jo.put("KOSTL", "");
				jo.put("AUFNR", "");
				jo.put("ZUONR", "");
				jo.put("SGTXT", "");
				jo.put("ZZPERSON", "");
				jo.put("RSTGR", "205");
				jo.put("ZFBDT", "");
				array1.put(jo);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
			JSONObject jo1 = new JSONObject();
			try {
				jo1.put("BSCHL", "39");
				jo1.put("HKONT", "");
				jo1.put("LIFNR", "");
				jo1.put("KUNNR", "");
				jo1.put("UMSKZ", "");
				jo1.put("WRBTR", "");
				jo1.put("EBELN", "");
				jo1.put("EBELP", "");
				jo1.put("KOSTL", "");
				jo1.put("AUFNR", "");
				jo1.put("ZUONR", "");
				jo1.put("SGTXT", "");
				jo1.put("ZZPERSON", "");
				jo1.put("RSTGR", "");
				jo1.put("ZFBDT", "");
				array2.put(jo);
			} catch (JSONException e) {
			}
		
		try {
			head.put("ZFLAG","");
			head.put("BUKRS","");
			head.put("BLART","SA");
			head.put("BLDAT","");
			head.put("BUDAT","");
			head.put("MONAT","");
			head.put("WAERS","");
			head.put("BKTXT","");
			head.put("XBLNR_ALT","");
			head.put("CHILD_LIST_TB1", array1);
			head.put("CHILD_LIST_TB2", array2);
		} catch (JSONException e) {;
		}
		array.put(head);
		System.out.println(array.toString());
	}
	public static String ToSBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == ' ') {
            c[i] = '\u3000';
          } else if (c[i] < '\177') {
            c[i] = (char) (c[i] + 65248);


         }
        }
        return new String(c);
}
}
