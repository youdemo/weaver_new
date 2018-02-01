package cn.com.weaver.form.webservices;

import java.net.URL;

public class Demo {
	public static void main(String[] args) {
		try{
			String address = "http://10.118.22.101:8080//services/ModifyFormData";
			
			URL url = new URL(address);
			
			ModifyFormDataLocator locator = new ModifyFormDataLocator();
			
			ModifyFormDataPortType porty = locator.getModifyFormDataHttpPort(url);
			
			AnyType2AnyTypeMapEntry[] maps = new AnyType2AnyTypeMapEntry[2];
			
			AnyType2AnyTypeMapEntry detail = new AnyType2AnyTypeMapEntry();
			
			detail.setKey("xgnr");
			detail.setValue("444");
			maps[0] = detail;
			
			detail = new AnyType2AnyTypeMapEntry();
			detail.setKey("sqrq");
			detail.setValue("2017-07-12");
			maps[1] = detail;
			//参数1 ： requestid 参数2：修改的字段数组
			//返回为json格式字符串
			//例如：{"status":"1","msg":"执行成功！"}   
			//status = 1 表示成功。反之失败；msg为提示消息
			String result = porty.modeifyData("950", maps);
			
			System.err.println(result);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
