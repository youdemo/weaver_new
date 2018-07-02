package gvo.util.item;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class XStreamUtils {
	public static Data parseData(String xmlStr){
		 XStream xstream = new XStream(new DomDriver());   
	     xstream.registerConverter(new MethodConverter());
	     xstream.alias("DATA", Data.class);
	     xstream.alias("HEAD", Head.class); 
	     xstream.alias("LIST", RespList.class);   
	     xstream.alias("ITEM", ItemList.class);  
	     xstream.alias("item", Item.class); 
	     Data data=(Data)xstream.fromXML(xmlStr);
	     return data;
	}
//	public static Data2 parseData2(String  xmlStr){
//		 XStream xstream = new XStream(new DomDriver());   
//	     xstream.registerConverter(new MethodConverter());
//	     xstream.alias("DATA", Data2.class);
//	     xstream.alias("HEAD", Head.class); 
//	     xstream.alias("LIST", List2.class);   
//	     xstream.alias("ITEM", Item2.class); 
//	     Data2 data=(Data2)xstream.fromXML(xmlStr);
//	     return data;
//	}
//	 public static void main(String[] args) {
//	        try {
//	        	String jsonPara = "<DATA>" +
//                "<HEAD>" +
//                "<BIZTRANSACTIONID>SAP.MM_HNYG-021_20180110105347137</BIZTRANSACTIONID>" +
//                "<RESULT></RESULT>" +
//                "<ERRORCODE></ERRORCODE>" +
//                "<ERRORINFO></ERRORINFO>" +
//                "<COMMENTS></COMMENTS>" +
//                "<SUCCESSCOUNT>1</SUCCESSCOUNT>" +
//                "</HEAD>" +
//                "<LIST>" +
//                "<ITEM>" +
//                "<item>" +
//                "<OAID>1</OAID>" +
//                "<INFNR></INFNR>" +
//                "<STATUS>E</STATUS>" +
//                "<MESSAGE>输入应按格式___.___.__~,__;字段 EINE-NETPR 中格式出错;参见下一消息;请检查计量单位和转换因子;</MESSAGE>" +
//                "</item>" +
//                "<item>" +
//                "<OAID>3</OAID>" +
//                "<INFNR></INFNR>" +
//                "<STATUS>E</STATUS>" +
//                "<MESSAGE>供应商1000011未由采购组织1000建立;</MESSAGE>" +
//                "</item>" +
//                "<item >" +
//                "<OAID>4</OAID><INFNR></INFNR><STATUS>E</STATUS><MESSAGE>供应商1000026未由采购组织1000建立;</MESSAGE>" +
//                "</item>" +
//                "</ITEM>" +
//                "</LIST>" +
//                "</DATA>";
//	        	String json = "<DATA><HEAD><BIZTRANSACTIONID>SAP.MM_HNYG-002_20180110121554177</BIZTRANSACTIONID><RESULT>S</RESULT><ERRORCODE/><ERRORINFO/><COMMENTS/><SUCCESSCOUNT>1</SUCCESSCOUNT></HEAD><LIST><ITEM><E_BANFN>3500005041</E_BANFN><E_MSGTX></E_MSGTX></ITEM></LIST></DATA>";
//            Data data=(Data)XStreamUtils.parseData(jsonPara);
//            System.out.println(data);
//            List<Item> itemList=data.getLIST().getITEM();
//            for(Item item:itemList){
//            	System.out.println(item.getMESSAGE());
//            }
//            
//            Data2 data2=(Data2)XStreamUtils.parseData2(json);
//            Item2 item2=data2.getLIST().getITEM();
//            System.out.println(item2.getE_BANFN());
//           
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

	
}
