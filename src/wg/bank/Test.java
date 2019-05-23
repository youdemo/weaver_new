package wg.bank;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import weaver.general.BaseBean;

public class Test {
	public static void main(String args[]) throws Exception {
		String result = "<?xml version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK><INFO><DATTYP>2</DATTYP><ERRMSG></ERRMSG><FUNNAM>DCPAYREQ</FUNNAM><LGNNAM>学科园科兴学532</LGNNAM><RETCOD>0</RETCOD></INFO><NTQPAYRQZ><ERRCOD>SUC0000</ERRCOD><OPRALS>终极审批</OPRALS><OPRSQN>001</OPRSQN><REQNBR>0030221519</REQNBR><REQSTS>AUT</REQSTS><SQRNBR>0000000000</SQRNBR><YURREF>201905130004</YURREF></NTQPAYRQZ></CMBSDKPGK>";
		doxml(result);
	}
	
	public static void doxml(String result) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		if(doc.getElementsByTagName("RETCOD").getLength()>0) {
			resultMap.put("RETCOD", doc.getElementsByTagName("RETCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRCOD").getLength()>0) {
			resultMap.put("ERRCOD", doc.getElementsByTagName("ERRCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("OPRALS").getLength()>0) {
			resultMap.put("OPRALS", doc.getElementsByTagName("OPRALS").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRTXT").getLength()>0) {
			resultMap.put("ERRTXT", doc.getElementsByTagName("ERRTXT").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRMSG").getLength()>0) {
			resultMap.put("ERRMSG", doc.getElementsByTagName("ERRMSG").item(0).getTextContent());
		}
		
	}
	public String test() {
		String result = "";
		String status = "";
		String errcode = "";
		String errtxt = "";
		String zxjg = "";
		String parm = "<?xml   version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK>\r\n" + 
				"                           <INFO>\r\n" + 
				"                              <FUNNAM>DCPAYREQ</FUNNAM>\r\n" + 
				"                              <DATTYP>2</DATTYP>\r\n" + 
				"                              <LGNNAM>学科园科兴学532</LGNNAM>\r\n" + 
				"                           </INFO>\r\n" + 
				"                           <SDKPAYRQX>\r\n" + 
				"                              <BUSCOD>N02030</BUSCOD>\r\n" + //业务类别 N02030:支付 N02040:集团支付
				"                              <BUSMOD>00001</BUSMOD>\r\n" + 
				"                           </SDKPAYRQX>\r\n" + 
				"                           <DCPAYREQX>\r\n" + 
				"                              <YURREF>201905130004</YURREF>\r\n" + //业务号
				"                              <DBTACC>755915711310210</DBTACC>\r\n" + //付款账号
				"                              <DBTBBK>75</DBTBBK>\r\n" + //地区
				"                              <BNKFLG>N</BNKFLG>\r\n" + //Y：招行；N：非招行；
				"                              <STLCHN>N</STLCHN>\r\n" + //结算方式
				"                              <TRSAMT>33.35</TRSAMT>\r\n" + //金额
				"                              <CCYNBR>10</CCYNBR>\r\n" + //人民币
				"                              <NUSAGE>测试002</NUSAGE>\r\n" + //对应对账单中的摘要
				"                              <CRTACC>7777880230001175</CRTACC>\r\n" + //收方账号
                //"                              <BRDNBR>102100099996</BRDNBR>\r\n" + //收方行号
                "                              <CRTBNK>中国工商银行总行清算中心</CRTBNK>\r\n" + //收方开户行名称
				"                              <CRTNAM>中国工商银行总行清算中心</CRTNAM>\r\n" + //帐户名称
				"                              <CRTPVC>北京</CRTPVC>\r\n" + //收方省份
				"                              <CRTCTY>北京</CRTCTY>\r\n" + //收方城市
				//"                              <CRTDTR>石龙区</CRTDTR>\r\n" + 
				"                              <RCVCHK>1</RCVCHK>\r\n" + 
				"                              <BUSNAR>测试支付</BUSNAR>\r\n" + 
				"                           </DCPAYREQX>  \r\n" + 
				"                        </CMBSDKPGK>";
		
		try {
			result = postConnection("http://192.168.7.36:8080", parm);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			BaseBean log = new BaseBean();
			log.writeLog("result aaa:"+result);
			
		
		return result;
	}
}
