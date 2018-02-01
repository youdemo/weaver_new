package gvo.util.xml;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestXmlUtil {
	public String javaToXml(String dataInfo, String workcode, String requestid,
			String xmlPay) {
		// 创建需要转换的对象
		Data data = new Data();
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		String time = sdf.format(date);

		Head head = new Head("EC.FI_HNYG-001_" + time, "1", "EC", "1", "", "",
				"", "?");
		List<Head> heads = new ArrayList<Head>();
		ItemEsb itemEsb = new ItemEsb();

		// 差旅需要人员工号，采购不需要工号
		if (!"".equals(dataInfo)) {
			itemEsb.setDataInfo(dataInfo);
		}
		if (!"".equals(workcode)) {
			itemEsb.setWorkcode(workcode);
		}
		if (!"".equals(requestid)) {
			itemEsb.setRequestId(requestid);
		}
		if (!"".equals(xmlPay)) {
			itemEsb.setxmlPay(xmlPay);
		}
		List<ItemEsb> itemEsbs = new ArrayList<ItemEsb>();
		itemEsbs.add(itemEsb);
		ListEsb listEsb = new ListEsb();
		List<ListEsb> listEsbs = new ArrayList<ListEsb>();
		listEsbs.add(listEsb);
		listEsb.setITEM(itemEsbs);
		heads.add(head);
		data.setHeads(heads);
		data.setLIST(listEsbs);
		// 将对象转换成string类型的xml
		String str = XmlUtil.convertToXml(data);
		// 输出
		// System.out.println(str);
		return str;
	}

	 public static void main(String[] args) {
	 TestXmlUtil txu = new TestXmlUtil();
	 String json = txu.javaToXml("{\n" + "    \"DETAILS\":{\n" + "        \"DT1\":[\n"
	 + "            {\n"
	 + "                \"ndkyys\": \"94511\",\n"
	 + "                \"fycd\": \"101\",\n"
	 + "                \"fyssqj\": \"2017-10-12\",\n"
	 + "                \"oamxid\": \"4\",\n"
	 + "                \"ydkyys\": \"0\",\n"
	 + "                \"yssqje\": \"100\",\n"
	 + "                \"jtsy\": \"\",\n"
	 + "                \"yskm\": \"263\"\n" + "            }\n"
	 + "        ]\n" + "    },\n" + "    \"HEADER\":{\n"
	 + "        \"fycdbm\": \"700005\",\n"
	 + "        \"txr\": \"3448\",\n"
	 + "        \"szgs\": \"70000\",\n"
	 + "        \"sqr\": \"3448\",\n"
	 + "        \"sqrq\": \"2017-10-12\",\n"
	 + "        \"oarqid\": \"708328\",\n"
	 + "        \"fyys\": \"100\"\n" + "    }\n" + "}", "3448", null, null);
	 System.out.println(json);
	 }
}
