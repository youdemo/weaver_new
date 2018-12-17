package gvo.doc.view;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
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
            System.out.println("name="+root.getName()+"|text="+root.getText());
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
                Element element = (Element) nodes.get(i);//循环依次得到子元素
                hashMap.put(element.getName(), element.getText());
                parse(element);
            }
        }
        return hashMap;
    }

    public static void main(String[] args) {
        SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
        String jsonPara = "<DATA>" +
        		"<HEAD>" +
	        		"<BIZTRANSACTIONID>SAP.MM_HNYG-021_20180110105347137</BIZTRANSACTIONID>" +
	        		"<RESULT></RESULT>" +
	        		"<ERRORCODE></ERRORCODE>" +
	        		"<ERRORINFO></ERRORINFO>" +
	        		"<COMMENTS></COMMENTS>" +
	        		"<SUCCESSCOUNT>1</SUCCESSCOUNT>" +
        		"</HEAD>" +
        		"<LIST>" +
	        		"<ITEM>" +
		        		"<item>" +
		    				"<OAID>1</OAID>" +
		    				"<INFNR></INFNR>" +
		    				"<STATUS>E</STATUS>" +
		    				"<MESSAGE>输入应按格式___.___.__~,__;字段 EINE-NETPR 中格式出错;参见下一消息;请检查计量单位和转换因子;</MESSAGE>" +
						"</item>" +
						"<item>" +
							"<OAID>3</OAID>" +
							"<INFNR></INFNR>" +
							"<STATUS>E</STATUS>" +
							"<MESSAGE>供应商1000011未由采购组织1000建立;</MESSAGE>" +
						"</item>" +
						"<item >" +
							"<OAID>4</OAID><INFNR></INFNR><STATUS>E</STATUS><MESSAGE>供应商1000026未由采购组织1000建立;</MESSAGE>" +
						"</item>" +
					"</ITEM>" +
					"</LIST>" +
				"</DATA>";
        String json = "<DATA><HEAD><BIZTRANSACTIONID>SAP.MM_HNYG-002_20180110121554177</BIZTRANSACTIONID><RESULT>S</RESULT><ERRORCODE/><ERRORINFO/><COMMENTS/><SUCCESSCOUNT>1</SUCCESSCOUNT></HEAD><LIST><ITEM><E_BANFN>3500005041</E_BANFN><E_MSGTX></E_MSGTX></ITEM></LIST></DATA>";
        String para = "item";
        String data = saxXmlUtil.getResult(para, json);
        System.out.println("data =" + data);
    }
}


