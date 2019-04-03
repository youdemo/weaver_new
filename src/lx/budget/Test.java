package lx.budget;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {

	public static void main(String[] args) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String now = sf.format(new Date());
		System.out.println(now);

	}

}
