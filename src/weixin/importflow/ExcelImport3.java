package weixin.importflow;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import jxl.Sheet;
import weaver.general.BaseBean;

public class ExcelImport3 {
	public Map<String,String> readAndExceSheet(Sheet sheet,String createrid,String wfid) {
		Map<String,String> map = new HashMap<String,String>();
		BaseBean log = new BaseBean();
		log.writeLog("Import flow 进入导入流程Excel");
		int rows = sheet.getRows();
		log.writeLog("rows:"+rows);
		try {
			JSONObject json = new JSONObject();
			JSONObject head = new JSONObject();
			JSONObject details = new JSONObject();
			String ISS = getStr(sheet.getRow(3)[8].getContents());//Issue Date:
			String QUO = getStr(sheet.getRow(6)[8].getContents());//Quote #:
			String QUO2 = getStr(sheet.getRow(7)[8].getContents());//Quote #:2
			String EXC = getStr(sheet.getRow(10)[8].getContents());//Exchange Rate:
			String EndUser = getStr(sheet.getRow(14)[2].getContents());//End User:
			String CustomerName = getStr(sheet.getRow(14)[4].getContents());//客户名称Customer Name :
			String Payment = getStr(sheet.getRow(14)[7].getContents());//付款周期Payment Term:
			String PartNo = getStr(sheet.getRow(15)[4].getContents());//料号Part NO:
			String Shipping = getStr(sheet.getRow(15)[7].getContents());//出货条件Shipping Term:
			
			String OEM = getStr(sheet.getRow(16)[2].getContents());//OEM:
			String ProgramName = getStr(sheet.getRow(16)[4].getContents());//项目名称Program Name:
			String Currency = getStr(sheet.getRow(16)[7].getContents());//币别Currency:
			String Description = getStr(sheet.getRow(17)[4].getContents());//项目描述Program Description:
			String Address = getStr(sheet.getRow(17)[7].getContents());//出货地址Shipping Address:
			
			String Price_per = getNumStr(sheet.getRow(21)[3].getContents());//Price per PCS:
			String Priceper = getNumStr(sheet.getRow(21)[5].getContents());//Price per PCS:
			String Price = getNumStr(sheet.getRow(21)[7].getContents());//Price per PCS:
			String Cost_per = getNumStr(sheet.getRow(22)[3].getContents());//Cost per PCS:
			String CostPCS = getNumStr(sheet.getRow(22)[5].getContents());//Cost per PCS:
			String Costper = getNumStr(sheet.getRow(22)[7].getContents());//Cost per PCS:
			
			String Quoting = getNumStr(sheet.getRow(24)[3].getContents());//Quoting Price / PCS:
			String PanelSize = getNumStr(sheet.getRow(27)[4].getContents());//Panel Size  (SF):
			String Numberup = getNumStr(sheet.getRow(28)[4].getContents());//Number up (Per Panel):
			String Layer = getNumStr(sheet.getRow(28)[6].getContents());//Layer:
			String Flex = getStr(sheet.getRow(29)[2].getContents());//Flex
			String PerPanel1 = getNumStr(sheet.getRow(30)[4].getContents());//Per Pane
			String PerPanel2 = getNumStr(sheet.getRow(31)[4].getContents());//Per Panel
			String PerPanel3 = getNumStr(sheet.getRow(32)[4].getContents());//Per Pane2
			String PerPanel4 = getNumStr(sheet.getRow(33)[4].getContents());//Per Pane3
			String PerPanel5 = getNumStr(sheet.getRow(34)[4].getContents());//Per Pane4
			String PerPanel6 = getNumStr(sheet.getRow(35)[4].getContents());//Per Pane5
			String PerPanel7 = getNumStr(sheet.getRow(36)[4].getContents());//Per Pane6
			String Assy = getStr(sheet.getRow(38)[2].getContents());//Assy
			String AssyPer1 = getNumStr(sheet.getRow(39)[6].getContents());//1AssyPer PCS
			String AssyPer2 = getNumStr(sheet.getRow(40)[6].getContents());//2AssyPer PCS
			String AssyPer3 = getNumStr(sheet.getRow(41)[6].getContents());//3AssyPer PCS
			String AssyPer4 = getNumStr(sheet.getRow(42)[6].getContents());//4AssyPer PCS
			String AssyPer5 = getNumStr(sheet.getRow(43)[6].getContents());//5AssyPer PCS
			String AssyPer6 = getNumStr(sheet.getRow(44)[6].getContents());//6AssyPer PCS
			String AssyPer7 = getNumStr(sheet.getRow(45)[6].getContents());//7AssyPer PCS
			String AssyPer8 = getNumStr(sheet.getRow(46)[6].getContents());//8AssyPer PCS
			
			String FlexLabor = getNumStr(sheet.getRow(52)[4].getContents());//Flex Labor - Hour per Panel:
			String AssyLabor3 = getNumStr(sheet.getRow(53)[6].getContents());//3Assy Labor - Minute per PCS:
			String Monthly = getNumStr(sheet.getRow(58)[4].getContents());//Monthly QTY (PCS per Month)
			String LifeTime = getNumStr(sheet.getRow(59)[4].getContents());//Life Time (Month)：
			String Ramp = getNumStr(sheet.getRow(60)[4].getContents());//Ramp up Time:
			String CustomerPays = getStr(sheet.getRow(64)[4].getContents());//Customer Pays (Y / N)
			String Amortized = getStr(sheet.getRow(65)[4].getContents());//Amortized to Shipping Qty (Y / N)
			head.put("ISS", ISS);
			head.put("QUO", QUO);
			head.put("QUO2", QUO2);
			head.put("EXC", EXC);
			head.put("EndUser", EndUser);
			head.put("CustomerName", CustomerName);
			head.put("Payment", Payment);
			head.put("PartNo", PartNo);
			head.put("Shipping", Shipping);
			head.put("OEM", OEM);
			head.put("ProgramName", ProgramName);
			head.put("Currency", Currency);
			head.put("Description", Description);
			head.put("Address", Address);
			head.put("Price_per", Price_per);
			head.put("Priceper", Priceper);
			head.put("Price", Price);
			head.put("Cost_per", Cost_per);
			head.put("CostPCS", CostPCS);
			head.put("Costper", Costper);
			head.put("Quoting", Quoting);
			head.put("PanelSize", PanelSize);
			head.put("Numberup", Numberup);
			head.put("Layer", Layer);
			head.put("Flex", Flex);
			head.put("PerPanel1", PerPanel1);
			head.put("PerPanel2", PerPanel2);
			head.put("PerPanel3", PerPanel3);
			head.put("PerPanel4", PerPanel4);
			head.put("PerPanel5", PerPanel5);
			head.put("PerPanel6", PerPanel6);
			head.put("PerPanel7", PerPanel7);
			head.put("Assy", Assy);
			head.put("AssyPer1", AssyPer1);
			head.put("AssyPer2", AssyPer2);
			head.put("AssyPer3", AssyPer3);
			head.put("AssyPer4", AssyPer4);
			head.put("AssyPer5", AssyPer5);
			head.put("AssyPer6", AssyPer6);
			head.put("AssyPer7", AssyPer7);
			head.put("AssyPer8", AssyPer8);
			head.put("FlexLabor", FlexLabor);
			head.put("AssyLabor3", AssyLabor3);
			head.put("Monthly", Monthly);
			head.put("LifeTime", LifeTime);
			head.put("Ramp", Ramp);
			head.put("CustomerPays", CustomerPays);
			head.put("Amortized", Amortized);
			json.put("HEADER", head);
			json.put("DETAILS", details);
			log.writeLog("Import flow json:"+json.toString());
			AutoRequestService ars = new AutoRequestService();
			String result = ars.createRequest(wfid, json.toString(), createrid, "0");
			log.writeLog("Import flow result:"+result);
			JSONObject jo = new JSONObject(result);
			
			map.put("MSG_TYPE", jo.getString("MSG_TYPE"));
			map.put("MSG_CONTENT", jo.getString("MSG_CONTENT"));
			map.put("OA_ID", jo.getString("OA_ID"));

		} catch (Exception e) {
			log.writeLog(e);
			map.put("MSG_TYPE", "E");
			map.put("MSG_CONTENT", "Excel解析异常");
			map.put("OA_ID", "");
			return map;
		}

		return map;
	}

	

	private String getStr(String str) {
		if (str == null)
			return "";
		return str.trim();
	}

	private String getNumStr(String str) {
		if (str == null || "".equals(str))
			return "0";
		return str.trim();
	}
	
	
}
