package weixin.importflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fr.third.v2.org.apache.poi.ss.usermodel.Cell;
import com.fr.third.v2.org.apache.poi.ss.usermodel.Sheet;
 
import weaver.general.BaseBean;

public class ExcelImport {  
	public Map<String,String> readAndExceSheet(Sheet sheet,String createrid,String wfid) {
		Map<String,String> map = new HashMap<String,String>();
		BaseBean log = new BaseBean();
		 SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		log.writeLog("Import flow 进入导入流程Excel"); 
		try {
			JSONObject json = new JSONObject();
			JSONObject head = new JSONObject();
			JSONObject details = new JSONObject();
			Cell cell = null;
			String ISS = "";
			Date ISSDate = sheet.getRow(3).getCell(8).getDateCellValue();//Issue Date:
			if(ISSDate != null) {
				ISS = dateFormate.format(ISSDate);
			}
			String QUO = sheet.getRow(6).getCell(8).getStringCellValue();//Quote #:
			cell = sheet.getRow(7).getCell(8);
			cell.setCellType(Cell.CELL_TYPE_STRING);
			String QUO2 =cell.getStringCellValue();//Quote #:2
			String EXC = String.valueOf(sheet.getRow(10).getCell(8).getNumericCellValue());//Exchange Rate:
			String EndUser = sheet.getRow(14).getCell(2).getStringCellValue();//End User:
			String CustomerName = sheet.getRow(14).getCell(4).getStringCellValue();//客户名称Customer Name :
			String Payment = sheet.getRow(14).getCell(7).getStringCellValue();//付款周期Payment Term:
			String PartNo = sheet.getRow(15).getCell(4).getStringCellValue();//料号Part NO:
			String Shipping = sheet.getRow(15).getCell(7).getStringCellValue();//出货条件Shipping Term:
			
			String OEM = sheet.getRow(16).getCell(2).getStringCellValue();//OEM:
			String ProgramName = sheet.getRow(16).getCell(4).getStringCellValue();//项目名称Program Name:
			String Currency = sheet.getRow(16).getCell(7).getStringCellValue();//币别Currency:
			String Description = sheet.getRow(17).getCell(4).getStringCellValue();//项目描述Program Description:
			String Address = sheet.getRow(17).getCell(7).getStringCellValue();//出货地址Shipping Address:
			String Price_per =String.valueOf(sheet.getRow(21).getCell(3).getNumericCellValue());//Price per PCS:
			String Priceper = String.valueOf(sheet.getRow(21).getCell(5).getNumericCellValue());//Price per PCS:
			String Price = String.valueOf(sheet.getRow(21).getCell(7).getNumericCellValue());//Price per PCS:
			String Cost_per = String.valueOf(sheet.getRow(22).getCell(3).getNumericCellValue());//Cost per PCS:
			String CostPCS = String.valueOf(sheet.getRow(22).getCell(5).getNumericCellValue());//Cost per PCS:
			String Costper = String.valueOf(sheet.getRow(22).getCell(7).getNumericCellValue());//Cost per PCS:	
			String Quoting = String.valueOf(sheet.getRow(24).getCell(3).getNumericCellValue());//Quoting Price / PCS:
			String PanelSize = String.valueOf(sheet.getRow(27).getCell(4).getNumericCellValue());//Panel Size  (SF):
			String Numberup = String.valueOf(sheet.getRow(28).getCell(4).getNumericCellValue());//Number up (Per Panel):
			String Layer = String.valueOf(sheet.getRow(28).getCell(6).getNumericCellValue());//Layer:
			String Flex = sheet.getRow(29).getCell(2).getStringCellValue();//Flex
			String PerPanel1 = String.valueOf(sheet.getRow(30).getCell(4).getNumericCellValue());//Per Pane
			String PerPanel2 = String.valueOf(sheet.getRow(31).getCell(4).getNumericCellValue());//Per Panel
			String PerPanel3 = String.valueOf(sheet.getRow(32).getCell(4).getNumericCellValue());//Per Pane2
			String PerPanel4 = String.valueOf(sheet.getRow(33).getCell(4).getNumericCellValue());//Per Pane3
			String PerPanel5 = String.valueOf(sheet.getRow(34).getCell(4).getNumericCellValue());//Per Pane4
			String PerPanel6 = String.valueOf(sheet.getRow(35).getCell(4).getNumericCellValue());//Per Pane5
			String PerPanel7 = String.valueOf(sheet.getRow(36).getCell(4).getNumericCellValue());//Per Pane6
			String Assy = sheet.getRow(38).getCell(2).getStringCellValue();//Assy
			String AssyPer1 = String.valueOf(sheet.getRow(39).getCell(6).getNumericCellValue());//1AssyPer PCS
			String AssyPer2 = String.valueOf(sheet.getRow(40).getCell(6).getNumericCellValue());//2AssyPer PCS
			String AssyPer3 = String.valueOf(sheet.getRow(41).getCell(6).getNumericCellValue());//3AssyPer PCS
			String AssyPer4 = String.valueOf(sheet.getRow(42).getCell(6).getNumericCellValue());//4AssyPer PCS
			String AssyPer5 = String.valueOf(sheet.getRow(43).getCell(6).getNumericCellValue());//5AssyPer PCS
			String AssyPer6 = String.valueOf(sheet.getRow(44).getCell(6).getNumericCellValue());//6AssyPer PCS
			String AssyPer7 = String.valueOf(sheet.getRow(45).getCell(6).getNumericCellValue());//7AssyPer PCS
			String AssyPer8 = String.valueOf(sheet.getRow(46).getCell(6).getNumericCellValue());//8AssyPer PCS
			String FlexLabor = String.valueOf(sheet.getRow(52).getCell(4).getNumericCellValue());//Flex Labor - Hour per Panel:
			String AssyLabor3 = String.valueOf(sheet.getRow(53).getCell(6).getNumericCellValue());//3Assy Labor - Minute per PCS:
			String Monthly = String.valueOf(sheet.getRow(58).getCell(4).getNumericCellValue());;//Monthly QTY (PCS per Month)
			String LifeTime = String.valueOf(sheet.getRow(59).getCell(4).getNumericCellValue());;//Life Time (Month)：
			String Ramp = "";
			Date RampDate = sheet.getRow(60).getCell(4).getDateCellValue();//Ramp up Time:
			if(RampDate != null) {
				Ramp = dateFormate.format(RampDate);
			}
			String CustomerPays = sheet.getRow(64).getCell(4).getStringCellValue();//Customer Pays (Y / N)
			String Amortized = sheet.getRow(65).getCell(4).getStringCellValue();//Amortized to Shipping Qty (Y / N)
			head.put("ISS", ISS);
			head.put("QUO", QUO);
			head.put("QUO2", QUO2);
			head.put("EXC", EXC);
			head.put("EndUser1", EndUser);
			head.put("CustomerName", CustomerName);
			head.put("Payment", Payment);
			head.put("PartNo", PartNo);
			head.put("Shipping", Shipping);
			head.put("OEM", OEM);
			head.put("ProgramName", ProgramName);
			head.put("Currency1", Currency);
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
			head.put("Ramp1", Ramp);
			head.put("CustomerPays", CustomerPays);
			head.put("Amortized", Amortized);
			head.put("Sheet1", "0.07625");
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
