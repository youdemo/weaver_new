package gvo.doc.pdf;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.general.BaseBean;

public class Html2PdfUtil {

	public static String toTime(long l) {
		return new SimpleDateFormat("yyyy MM dd HH mm ss").format(new Date(l));
	}

	public boolean htmltopdf(String[] args) {
		BaseBean log = new BaseBean();
		 StringBuilder cmd = new StringBuilder();
	        cmd.append(args[0]);
	        cmd.append(" ");
	        cmd.append(args[1]);
	        cmd.append(" ");
	        cmd.append(args[2]);


		// new BaseBean().writeLog("cmd: "+cmd);
		//long l1 = System.currentTimeMillis();
		try {
			// new BaseBean().writeLog("开始执行转换!");
			Runtime run = Runtime.getRuntime();
			Process p = run.exec(cmd.toString());// 启动另一个进程来执行命令
			// BufferedInputStream in = new BufferedInputStream(p.getInputStream());
			BufferedInputStream err = new BufferedInputStream(p.getErrorStream());
			// BufferedReader inBr = new BufferedReader(new InputStreamReader(in));
			BufferedReader errBr = new BufferedReader(new InputStreamReader(err));
			String lineStr = "";
			while ((lineStr = errBr.readLine()) != null)
				// System.out.println(lineStr);
				// 检查命令是否执行失败。
				// new BaseBean().writeLog("转换执行中!");
				try {
					if (p.waitFor() != 0) {
						// new BaseBean().writeLog("是否正常结束"+p.exitValue());
						if (p.exitValue() == 1)// p.exitValue()==0表示正常结束，1：非正常结束
							log.writeLog("命令执行失败!");
//                        return false;
					}
				} catch (InterruptedException e) {
					log.writeLog("转换出错");
					log.writeLog(e);
					return false;
				}
			// new BaseBean().writeLog("执行转换结束");
			// new BaseBean().writeLog("命令执行成功!");

		} catch (Exception e) {
			new BaseBean().writeLog("转换出错" + e);
			e.printStackTrace();
			return false;
		}
		//long l2 = System.currentTimeMillis();

		// new BaseBean().writeLog(toTime(l1));
		// new BaseBean().writeLog(toTime(l2));

		// new BaseBean().writeLog("exec time="+(l2-l1));
		return true;
	}

}
