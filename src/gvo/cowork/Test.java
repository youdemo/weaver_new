package gvo.cowork;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
		String nowTime = timeFormate.format(new Date());
		System.out.println(nowTime.substring(0,5));

	}

}
