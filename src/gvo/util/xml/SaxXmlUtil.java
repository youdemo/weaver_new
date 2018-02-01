package gvo.util.xml;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * 接口参数修改
 * 
 * @author adore
 * @time 2017年11月09日23:31:36
 * @history v1.0
 **/

public class SaxXmlUtil {
	static HashMap<String, Object> hashMap = new HashMap<String, Object>();
	static List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

	/**
	 * 根据元素名称获取对应map内容
	 **/
	public String getResult(String para, String jsonPara) {
		list = new ArrayList<Map<String, Object>>();
		list.add(getXmlMap(jsonPara));

		for (Map<String, Object> m : list) {
			para = m.get(para).toString();
		}
		return para;
	}

	/**
	 * xml转map
	 **/
	public static Map<String, Object> getXmlMap(String jsonPara) {
		Map map = new HashMap<String, Object>();
		StringReader read = new StringReader(jsonPara);
		InputSource source = new InputSource(read);
		SAXBuilder sb = new SAXBuilder();
		try {
			Document doc = (Document) sb.build(source);
			Element root = doc.getRootElement();
			hashMap.put(root.getName(), root.getText());
			map = parse(root);
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	public static HashMap<String, Object> parse(Element root) {
		List nodes = root.getChildren();
		int len = nodes.size();
		if (len == 0) {
			hashMap.put(root.getName(), root.getText());
		} else {
			for (int i = 0; i < len; i++) {
				Element element = (Element) nodes.get(i);// 循环依次得到子元素
				hashMap.put(element.getName(), element.getText());
				parse(element);
			}
		}
		return hashMap;
	}

	public static void main(String[] args) {
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String para = "E_MSGTX";
		// String jsonPara = "  <DATA>\n" +
		// "    <HEAD>\n" +
		// "      <BIZTRANSACTIONID>?</BIZTRANSACTIONID>\n" +
		// "      <COUNT>1</COUNT>\n" +
		// "      <CONSUMER>EC</CONSUMER>\n" +
		// "      <SRVLEVEL>1</SRVLEVEL>\n" +
		// "      <ACCOUNT></ACCOUNT>\n" +
		// "      <PASSWORD></PASSWORD>\n" +
		// "      <USE></USE>\n" +
		// "      <COMMENTS>?</COMMENTS>\n" +
		// " </HEAD>\n" +
		// "   <LIST>\n" +
		// "<ITEM>\n" +
		// "\n" +
		// "<workcode>3448</workcode>\n" +
		// "  <dataInfo>{\n" +
		// "    \"DETAILS\":{\n" +
		// "        \"DT1\":[\n" +
		// "            {\n" +
		// "                \"ndkyys\": \"94511\",\n" +
		// "                \"fycd\": \"101\",\n" +
		// "                \"fyssqj\": \"2017-10-12\",\n" +
		// "                \"oamxid\": \"4\",\n" +
		// "                \"ydkyys\": \"0\",\n" +
		// "                \"yssqje\": \"100\",\n" +
		// "                \"jtsy\": \"\",\n" +
		// "                \"yskm\": \"263\"\n" +
		// "            }\n" +
		// "        ]\n" +
		// "    },\n" +
		// "    \"HEADER\":{\n" +
		// "        \"fycdbm\": \"700005\",\n" +
		// "        \"txr\": \"3448\",\n" +
		// "        \"szgs\": \"70000\",\n" +
		// "        \"sqr\": \"3448\",\n" +
		// "        \"sqrq\": \"2017-10-12\",\n" +
		// "        \"oarqid\": \"708328\",\n" +
		// "        \"fyys\": \"100\"\n" +
		// "    }\n" +
		// "}</dataInfo>\n" +
		// "</ITEM>\n" +
		// "   </LIST>\n" +
		// "  </DATA>";

		String jsonPara = "<DATA>\n" + "<HEAD>\n" + "<HEAD>\n"
				+ "<BIZTRANSACTIONID>?</BIZTRANSACTIONID>\n"
				+ "<RESULT>0</RESULT>\n" + "<ERRORCODE/><ERRORINFO/>\n"
				+ "<COMMENTS/>\n" + "<SUCCESSCOUNT>2</SUCCESSCOUNT>\n"
				+ "</HEAD>\n" + "</HEAD>\n" + "<LIST>\n" + "<ITEM>\n"
				+ "<E_BANFN>3500004943</E_BANFN>\n"
				+ "<E_MSGTX>123456</E_MSGTX>\n" + "</ITEM>\n" + "</LIST>\n"
				+ "</DATA>";
		String result = saxXmlUtil.getResult(para, jsonPara);
		System.out.println("result=" + result);
	}
}
