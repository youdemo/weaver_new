package gvo.webservice;

import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Test {

	/**
	 * @param args
	 * @throws Exception 
	 * @throws Exception 
	 */
	//加班
//	public static void main(String[] args) throws Exception {
//		JSONObject jo = new JSONObject();
//		jo.put("name", "丁吉");
//		jo.put("work_code", "009118");
//		jo.put("overtime_transfer", "0");//0 转支付 1 转调休
//		jo.put("overtime_way", "0");//0 正常加班 1公出加班 2 出差加班
//		jo.put("cday", "2018-06-05");
//		jo.put("P_begindate", "2018-06-05");
//		jo.put("P_begintime", "19:00");
//		jo.put("P_enddate", "2018-06-05");
//		jo.put("P_endtime", "21:00");
//		jo.put("P_hours", "2");
//		jo.put("month_hours", "2");
//		jo.put("limit_hours", "40");
//		jo.put("reason", "加班事由");
//		jo.put("shift", "白班8：00-15:00");
//		jo.put("overtime_type", "平时加班转调休");
//		System.out.println(jo.toString());
//
//	}
	//请假
//	public static void main(String[] args) throws Exception {
//		JSONObject jo = new JSONObject();
//		jo.put("name", "丁吉");
//		jo.put("work_code", "009118");
//		jo.put("absence_type", "16");
//		jo.put("begindate", "2018-06-06");
//		jo.put("begintime", "08:30");
//		jo.put("enddate", "2018-06-06");
//		jo.put("endtime", "17:30");
//		jo.put("hours", "8");
//		jo.put("agent", "3448");
//		jo.put("used_hours", "8");
//		jo.put("remain_hours", "20");
//		jo.put("on_hours", "1");
//		jo.put("place", "昆山");
//		jo.put("reason", "16");
//		System.out.println(jo.toString());
//
//	}
	//补卡申请
//	public static void main(String[] args) throws Exception {
//		JSONObject jo = new JSONObject();
//		jo.put("name", "丁吉");
//		jo.put("work_code", "009118");
//		jo.put("Nocard_type", "1");
//		jo.put("Nocard_Reason", "0");
//		jo.put("Break_date", "2018-06-06");
//		jo.put("repaircard_time_s", "08:00");
//		jo.put("repaircard_time_e", "18:00");
//		jo.put("card_place", "2");
//		jo.put("Witness", "3448");
//		jo.put("detail", "啊啊事由");
//		System.out.println(jo.toString());
//
//	}
//销假流程	
	public static void main(String[] args) throws Exception {
		String vvv=URLDecoder.decode("%E4%B8%8B%E8%BD%BD.jpeg");
		System.out.println(vvv);
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String aa="2017-06-07";
		System.out.println(aa.substring(0, 4));
		if(aa.substring(0, 4).equals(now.substring(0, 4))){
			System.out.println(now);
		}else{
			System.out.println(aa);
		}
		JSONObject jo = new JSONObject();
		jo.put("PjtID", "test123");
		jo.put("DCPID", "DCPID123");
		jo.put("DCPName", "dcp名称");
		System.out.println(jo.toString());

	}
//	public static void main(String[] args) throws Exception  {
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM");
//		System.out.println(sf.format(sf.parse("2018-06-01")));
//
//	}
}
