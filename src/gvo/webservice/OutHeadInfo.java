package gvo.webservice;


import gvo.webservice.xml.Data;
import gvo.webservice.xml.Head;
import gvo.webservice.xml.SaxXmlUtil;
import gvo.webservice.xml.XmlUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class OutHeadInfo {
	
	private String out;
	private String head;
	
	public void setOut(String json){
		this.out = json;
	}
	
	public void setHead(String head){
//		Data data = new Data();
//		Date date = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//		String time = sdf.format(date);
//		Head head = new Head("EC.FI_HNYG-001_" + time, "1", "EC", "1", "", "","", "?");
//		List<Head> heads = new ArrayList<Head>();
//		heads.add(head);
//		data.setHeads(heads);
//		将对象转换成string类型的xml
//		String str = XmlUtil.convertToXml(data);
		this.head = head;
	}
	
	public String getOut(){
		return this.out; 
	}
	
	public String getHead(){
		//setHead();
		return this.head;
	}
}