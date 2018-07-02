package gvo.reservationlist.listpost;

import gvo.util.xml.Data;
import gvo.util.xml.Head;
import gvo.util.xml.ItemEsb;
import gvo.util.xml.ListEsb;
import gvo.util.xml.XmlUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PostXmlUtil {
	public String javaToXml(String dataInfo, String workcode, String requestid,
			String xmlPay) {
		// 创建需要转换的对象
		Data data = new Data();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(date);

		Head head = new Head("SAP.MM_HNYG-010_" + time, "1", "EC", "1", "userSAP", "P@ss0rd",
				"", "");
		List<Head> heads = new ArrayList<Head>();
		ItemEsb itemEsb = new ItemEsb();

		// 差旅需要人员工号，采购不需要工号
		if (!"".equals(dataInfo)) {
			itemEsb.setDataInfo(dataInfo);
		}
		if (!"".equals(workcode)) {
			itemEsb.setWorkcode(workcode);
		}
//		if (!"".equals(requestid)) {
//			itemEsb.setRequestId(requestid);
//		}
		if (!"".equals(xmlPay)) {
			itemEsb.setxmlPay(xmlPay);
		}
		List<ItemEsb> itemEsbs = new ArrayList<ItemEsb>();
		itemEsbs.add(itemEsb);
		ListEsb listEsb = new ListEsb();
		List<ListEsb> listEsbs = new ArrayList<ListEsb>();
		listEsbs.add(listEsb);
		listEsb.setITEM(itemEsbs);
		head.setComments(requestid);
		heads.add(head);
		data.setHeads(heads);
		data.setLIST(listEsbs);
		// 将对象转换成string类型的xml
		String str = XmlUtil.convertToXml(data);
		// 输出
		// System.out.println(str);
		return str;
	}
}
