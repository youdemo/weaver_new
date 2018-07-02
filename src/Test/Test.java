package Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Test {
	public static void main(String[] args) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String Item = "";
		String VBELN = "";// 销售订单号
		String AUART = "";// 销售凭证类型
		String AUGRU = "";// 订单原因
		String WAERK = "";// 凭证货币
		String BUKRS_VF = "";// 出票公司代码
		String COMNAME = "";// 公司名称
		String KUNNR = "";// 客户编号
		String KNAME = "";// 客户名称
		String ZXSYGH = "";// 销售员工号
		String ZXSYXM = "";// 销售员姓名
		String BSARK = "";// 订单类型
		String BSARKT = "";// 订单类型描述
		String ZTERM = "";// 付款条件
		String ZTERMT = "";// 付款条件描述
		String ZINCO = "";// 贸易条款
		String INCO ="";// 贸易条款
		String ZSUM = "";// 总价（原币）
		String ZRMB = "";// 总价（本币）
		String KBETR = "";// 税率
		// 明细1
		String POSNR = "";// 项目
		String MATNR = "";// 物料编号
		String ARKTX = "";// 物料描述
		String KWMENG = "";// 数量
		String VRKME = "";// 单位
		String ZJG = "";// 价格
		String ZZJ = "";// 总价格
		//String EDATU = "";// 期望交期
		String jsonStr ="{\"HEADER\":{'ZRMB':'1',\"PNAME\":\"007491\",\"VBELN\":\"1000000886\",\"AUART\":\"Z001\",\"AUGRU\":\"Z03\",\"WAERK\":\"CNY\",\"BUKRS_VF\":\"2300\",\"COMNAME\":\"维信诺（固安）显示科技有限公司\",\"KUNNR\":\"DSHHZ09988\",\"NAME1\":\"\",\"NAME2\":\"\",\"KNAME\":\"测试专用公司名称2\",\"ZXSYGH\":\"\",\"ZXSYXM\":\"\",\"BSARK\":\"10PT\",\"BSARKT\":\"普通销售订单\",\"INCO1\":\"UN\",\"INCO2\":\"AAAAAAAAAAAAAAAA\",\"ZTERM\":\"S007\",\"ZTERMT\":\"月结30天\",\"ZSUM\":4230.0,\"KBETR\":\"0.17\"},\"DETALIS\": {\"DT1\":[{\"VBELN\":\"1000000886\",\"POSNR\":\"000010\",\"MATNR\":\"G1145OA113GF-001\",\"ARKTX\":\"1.45OA模组成品\",\"KWMENG\":10.0,\"VRKME\":\"PCS\",\"KBETR\":0.0,\"KPEIN\":0,\"ZJG\":143.0,\"ZZJ\":1430.0,\"EDATU\":\"2018-04-16\",\"WAERK\":\"CNY\",\"BSTDK\":\"2018-04-16\"},{\"VBELN\":\"1000000886\",\"POSNR\":\"000020\",\"MATNR\":\"G1549HD145GG-001\",\"ARKTX\":\"5.5HD 模组成品\",\"KWMENG\":20.0,\"VRKME\":\"PCS\",\"KBETR\":0.0,\"KPEIN\":0,\"ZJG\":140.0,\"ZZJ\":2800.0,\"EDATU\":\"2018-04-16\",\"WAERK\":\"CNY\",\"BSTDK\":\"2018-04-16\"}]}}";
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(jsonStr);
		JSONObject head = jo.getJSONObject("HEADER");
		VBELN = head.getString("VBELN");
		AUART = head.getString("AUART");
		AUGRU = head.getString("AUGRU");
		WAERK = head.getString("WAERK");
		BUKRS_VF = head.getString("BUKRS_VF");
		COMNAME = head.getString("COMNAME");
		KUNNR = head.getString("KUNNR");
		KNAME = head.getString("KNAME");
		ZXSYGH = head.getString("ZXSYGH");
		ZXSYXM = head.getString("ZXSYXM");
		BSARK = head.getString("BSARK");
		BSARKT = head.getString("BSARKT");
		ZTERM = head.getString("ZTERM");
		ZTERMT = head.getString("ZTERMT");
		ZINCO = head.getString("INCO1");
		INCO = head.getString("INCO2");
		ZSUM = head.getString("ZSUM");
		ZRMB = head.getString("ZRMB");
		KBETR = head.getString("KBETR");
	//	header.put("PNAME", sqr);
		//header.put("dept", sqrbm);
		header.put("data", now);
		header.put("VBELN", VBELN);
		header.put("AUART", AUART);
		header.put("AUGRU", AUGRU);
		header.put("WAERK", WAERK);
		header.put("BUKRS_VF", BUKRS_VF);
		header.put("COMNAME", COMNAME);
		header.put("KUNNR", KUNNR);
		header.put("KNAME", KNAME);
		header.put("ZXSYGH", ZXSYGH);
		header.put("ZXSYXM", ZXSYXM);
		header.put("BSARK", BSARK);
		header.put("BSARKT", BSARKT);
		header.put("ZTERM", ZTERM);
		header.put("ZTERMT", ZTERMT);
		header.put("ZINCO", ZINCO);
		header.put("INCO", INCO);
		header.put("ZSUM", ZSUM);
		header.put("ZRMB", ZRMB);
		header.put("KBETR", KBETR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			//log.writeLog("arr" + arr);
			JSONObject node = new JSONObject();
			//log.writeLog("node" + node);
			// System.out.println(arr.toString());
			POSNR = arr.getString("POSNR");
			MATNR = arr.getString("MATNR");
			ARKTX = arr.getString("ARKTX");
			KWMENG = arr.getString("KWMENG");
			VRKME = arr.getString("VRKME");
			ZJG = arr.getString("ZJG");
			ZZJ = arr.getString("ZZJ");
			node.put("POSNR", POSNR);
			node.put("MATNR", MATNR);
			node.put("ARKTX", ARKTX);
			node.put("KWMENG", KWMENG);
			node.put("VRKME", VRKME);
			node.put("ZJG", ZJG);
			node.put("ZZJ", ZZJ);
			dt11.put(node);
		}
		details.put("DT1", dt11);
		System.out.println(json.toString());
	}
}
