package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.service.invoker.AbstractInvoker;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class CreateDCPRequestService {
	/**
	 * 
	 * @param dataInfo {"Owner":"V0009118","PjtType":"技术开发","DCPID":"DCPID123","Date":"2018-11-29","PjtName":"testproject","DCPName":"dcp名称","Stage":"第一阶段","PjtID":"test123","attachName":"测试附件.doc"}
	 * @return
	 */
	public String createRequest(String dataInfo) {
		BaseBean log = new BaseBean();
		log.writeLog("CreateDCPRequestService  dataInfo:" + dataInfo);
		if ("".equals(dataInfo)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "参数不能为空");
			retMap.put("OA_ID", "0");
			log.writeLog("CreateDCPRequestService result:" + getJsonStr(retMap));		
            return getJsonStr(retMap); 
		}
		MessageContext ctx=AbstractInvoker.getContext();
		CreateDCPRequestServiceImpl crs = new CreateDCPRequestServiceImpl();
		String result;
		try {
			result = crs.CreateDCPRequest(dataInfo,ctx);
		} catch (Exception e) {
			log.writeLog(e);
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json解析异常");
			retMap.put("OA_ID", "0");
			log.writeLog("CreateDCPRequestService result:" + getJsonStr(retMap));		
            return getJsonStr(retMap);
		}
		log.writeLog("CreateDCPRequestService result:"+result);
		
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
