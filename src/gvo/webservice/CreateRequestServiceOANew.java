package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class CreateRequestServiceOANew {
	public String CreatePurchaseOrder(String workcode, String dataInfo) {
		BaseBean log = new BaseBean();
		log.writeLog("CreatePurchaseOrder  workcode:" + workcode + " dataInfo:" + dataInfo);
		if ("".equals(workcode) || "".equals(dataInfo)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "参数不能为空");
			retMap.put("OA_ID", "0");
			log.writeLog("CreatePurchaseOrder result:" + getJsonStr(retMap));		
            return getJsonStr(retMap); 
		}
		CreateRequestServiceOANewImpl crs = new CreateRequestServiceOANewImpl();
		String result;
		try {
			result = crs.CreatePurchaseOrder(workcode, dataInfo);
		} catch (Exception e) {
			log.writeLog(e);
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json解析异常");
			retMap.put("OA_ID", "0");
			log.writeLog("CreatePurchaseOrder result:" + getJsonStr(retMap));		
            return getJsonStr(retMap);
		}
		log.writeLog("CreatePurchaseOrder result:"+result);
		
        return result;
	}
	
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}
}
