package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.fr.third.org.apache.poi.hssf.record.formula.functions.Time;

import weaver.general.TimeUtil;
import weaver.general.Util;


public class Test {
	public static void main(String args[]) {
		System.out.println("无奥术大师多 222\n  Asdasd 222".replaceAll("\n", "333"));
		String aa="";
		String beLate = "";
		if(!"".equals(aa)) {
			if(Util.getIntValue(aa, 0)<=2) {
				beLate = "0";
			}else {
				beLate = String.valueOf(Util.getIntValue(aa,0)-2);
			}
		}
		System.out.println(weaver.common.DateUtil.getDate(Calendar.getInstance().getTime()));
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate =sf.format(new Date());
		System.out.println(endDate.substring(0, 7)+"-01");
		String startDate = TimeUtil.dateAdd(endDate.substring(0, 7)+"-01", -1).substring(0, 7)+"-01";
		endDate = TimeUtil.dateAdd(endDate, -1);
		System.out.println(endDate);
		System.out.println(startDate);
	}
	
	// TODO Auto-generated method stub

	
 
}
