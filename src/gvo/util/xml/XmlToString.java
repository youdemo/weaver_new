package gvo.util.xml;


/**
 * @author Shaw java 截取xml格式的字符串的值
 */
public class XmlToString {
	/**
	 * @param source
	 * @param priStr
	 * @param suxStr
	 * @return
	 * @name 中文名称
	 * @description 截取字符串
	 * @time 创建时间:2017年11月09日18:08:57
	 * @author Shaw
	 * @history 修订历史（历次修订内容、修订人、修订时间等）
	 */
	public String getTotalMidValue(String source, String priStr, String suxStr) {
		if (source == null)
			return null;
		int iFirst = source.indexOf(priStr);
		int iLast = source.lastIndexOf(suxStr);
		if (iFirst < 0 || iLast < 0)
			return null;
		int beginIndex = iFirst + priStr.length();
		return source.substring(beginIndex, iLast);
	}

	// public static void main(String[] args) {
	// String para =
	// "<sap:SIGN>S</sap:SIGN><sap:Message><![CDATA[<DATA><HEAD/><LIST><ITEM><OA_ID>4814</OA_ID><MSG_CONTENT>流程创建成功</MSG_CONTENT></ITEM></LIST></DATA>]]></sap:Message>\n";
	// String para2 =
	// "{message=<DATA><HEAD><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID><RESULT>0</RESULT><ERRORCODE/><ERRORINFO/><COMMENTS/><SUCCESSCOUNT>2</SUCCESSCOUNT></HEAD></HEAD><LIST><ITEM><E_BANFN>3500004931</E_BANFN><E_MSGTX></E_MSGTX/></ITEM></LIST></DATA>, sign=S}";
	// XmlToString xts = new XmlToString();
	// String sign = xts.getTotalMidValue(para, "<sap:SIGN>", "</sap:SIGN>");
	// System.out.println("sign=" + sign);
	// String oa_id = xts.getTotalMidValue(para, "<OA_ID>", "</OA_ID>");
	// System.out.println("oa_id=" + oa_id);
	// String msg = xts.getTotalMidValue(para, "<MSG_CONTENT>",
	// "</MSG_CONTENT>");
	// System.out.println("msg=" + msg);
	//
	// String E_BANFN = xts.getTotalMidValue(para2, "<E_BANFN>", "</E_BANFN>");
	// System.out.println("E_BANFN=" + E_BANFN);
	// }
}