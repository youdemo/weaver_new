package gvo.purchase.xml;

import java.util.ArrayList;
import java.util.List;

public class TestXmlUtil {
    public String javaToXml(String dataInfo) {
        // 创建需要转换的对象
        Data data = new Data();
        Head head = new Head("?", "1", "EC", "1", "", "", "", "?");
        List<Head> heads = new ArrayList<Head>();
        ItemEsb itemEsb = new ItemEsb();
        itemEsb.setDataInfo(dataInfo);
        List<ItemEsb> itemEsbs = new ArrayList<ItemEsb>();
        itemEsbs.add(itemEsb);
        ListEsb listEsb = new ListEsb();
        List<ListEsb> listEsbs = new ArrayList<ListEsb>();
        listEsbs.add(listEsb);
        listEsb.setITEM(itemEsbs);
        heads.add(head);
        data.setHeads(heads);
        data.setLIST(listEsbs);
        //System.out.println("---将对象转换成string类型的xml Start---");
        // 将对象转换成string类型的xml
        String str = XmlUtil.convertToXml(data);
        // 输出
        //System.out.println(str);
        return str;
    }
}
