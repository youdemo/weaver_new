package gvo.util.xml;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.util.*;
public class XmlUtils {
    public static Map<String, Object> Dom2Map(Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return map;
        Element root = doc.getRootElement();
        for (Iterator<?> iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List<?> list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), Dom2Map(e));
            } else
                map.put(e.getName(), e.getText());
        }
        return map;
    }


    @SuppressWarnings("unchecked")
	public static Map Dom2Map(Element e) {
        Map<String, Object> map = new HashMap();
        List list = (List) e.elements();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                Element iter = (Element) list.get(i);
                List<Object> mapList = new ArrayList();

                if (iter.elements().size() > 0) {
                    Map m = Dom2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), m);
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = new ArrayList();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if (obj.getClass().getName().equals("java.util.ArrayList")) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else
                        map.put(iter.getName(), iter.getText());
                }
            }
        } else
            map.put(e.getName(), e.getText());
        return map;
    }
    public static void main(String[] args)  throws DocumentException {
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
        SAXReader reader = new SAXReader();
        StringReader read = new StringReader(jsonPara);
        InputSource source = new InputSource(read);
        Document document;
			document = reader.read(source);
			Map<String,Object> retMap = XmlUtils.Dom2Map(document);
//			System.out.println(retMap);
			Map<String,Object> LIST = (Map<String, Object>) retMap.get("LIST");
//			System.out.println(LIST);
			Map<String,Object> ITEMList = (Map<String, Object>) LIST.get("ITEM");
			System.out.println(ITEMList);
		    List<Map<String,Object>> ItemList = (List<Map<String, Object>>) ITEMList.get("item");
		    System.out.println(ItemList);
		    for(Map<String,Object> map :ItemList){
	        	Item item = new Item();
	        	item.setINFNR((String) map.get("INFNR"));
	        	item.setMESSAGE( (String) map.get("MESSAGE"));
	        	item.setOAID((String) map.get("OAID"));
	        	item.setSTATUS((String) map.get("STATUS"));
	        	System.out.println(item.getINFNR());
	        	System.out.println(item.getMESSAGE());
	        	System.out.println(item.getOAID());
	        	System.out.println(item.getSTATUS());
	        }
			
    }
}
    
