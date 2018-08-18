package ego.peixun;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		String aa="~`~`7&nbsp;智能烹饪技术`~`8&nbsp;Smart&nbsp;Cooking&nbsp;Technic`~`~";
		System.out.println(aa.substring(aa.indexOf("`~`7")+4, aa.indexOf("`~`8")));
		System.out.println(aa.substring(aa.indexOf("`~`8")+4, aa.indexOf("`~`~")));
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		String nowdate = sf.format(new Date());
		System.out.println(nowdate);
		
	}

}
