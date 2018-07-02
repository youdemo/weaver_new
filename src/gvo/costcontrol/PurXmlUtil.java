package gvo.costcontrol;

import gvo.util.xml.Data;
import gvo.util.xml.Head;
import gvo.util.xml.ItemEsb;
import gvo.util.xml.ListEsb;
import gvo.util.xml.XmlUtil;

import java.util.ArrayList;
import java.util.List;

public class PurXmlUtil {
	public String javaToXml(String dataInfo, String workcode, String requestid, String xmlPay,Head head) {
		// 创建需要转换的对象
		Data data = new Data();
		Head headnew = head;
		//Head head = new Head("SAP.FI_NNYG-057_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
		List<Head> heads = new ArrayList<Head>();
		ItemEsb itemNew = new ItemEsb();

		// 差旅需要人员工号，采购不需要工号
		if (!"".equals(dataInfo)) {
			itemNew.setDATAINFO(dataInfo);
		}
		if (!"".equals(workcode)) {
			itemNew.setWorkcode(workcode);
		}

		if (!"".equals(xmlPay)) {
			itemNew.setxmlPay(xmlPay);
		}
		List<ItemEsb> itemNews = new ArrayList<ItemEsb>();
		itemNews.add(itemNew);
		ListEsb listnew = new ListEsb();
		listnew.setITEM(itemNews);
		
		List<ListEsb> listnews = new ArrayList<ListEsb>();
		listnews.add(listnew);
		headnew.setComments(requestid);
		heads.add(headnew);
		data.setHeads(heads);
		data.setLIST(listnews);
		// 将对象转换成string类型的xml
		String str = XmlUtil.convertToXml(data);
		// 输出
		// System.out.println(str);
		return str;
	}
}
