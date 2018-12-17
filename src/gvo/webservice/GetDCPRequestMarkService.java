package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class GetDCPRequestMarkService {
	/**
	 * 
	 * @param dataInfo {"DCPID":"DCPID123","DCPName":"dcp名称","PjtID":"test123"}
	 * @return {"JSONSTR":[{"LOGTYPE":"批准","Owner":"V0009118","PjtType":"技术开发","Reviewer":"V0009118","DCPID":"DCPID123","AppTime":"2018-11-28 10:03:52","PjtName":"testproject","attach":[{"attachName":"测试提交.docx","attachUrl":"http://222.92.108.195:1515/gvo/dcp/downloaddoc.jsp?fileid=436619"}],"Stage":"第一阶段","DCPName":"dcp名称","PjtID":"test123","Idea":"测试提交","WFName":"IPD-108-A-DCP预审流程-VXG-丁吉-2018-11-27"},{"LOGTYPE":"批准","Owner":"V0009118","PjtType":"技术开发","Reviewer":"V0017940","DCPID":"DCPID123","AppTime":"2018-11-28 10:12:10","PjtName":"testproject","attach":[{"attachName":"测试提交.docx","attachUrl":"http://222.92.108.195:1515/gvo/dcp/downloaddoc.jsp?fileid=436621"},{"attachName":"测试提交 2.docx","attachUrl":"http://222.92.108.195:1515/gvo/dcp/downloaddoc.jsp?fileid=436622"}],"Stage":"第一阶段","DCPName":"dcp名称","PjtID":"test123","Idea":"测试提交2&lt;br/>&lt;br/>","WFName":"IPD-108-A-DCP预审流程-VXG-丁吉-2018-11-27"},{"LOGTYPE":"退回","Owner":"V0009118","PjtType":"技术开发","Reviewer":"V0032322","DCPID":"DCPID123","AppTime":"2018-11-28 10:00:57","PjtName":"testproject","attach":[],"Stage":"第一阶段","DCPName":"dcp名称","PjtID":"test123","Idea":"","WFName":"IPD-108-A-DCP预审流程-VXG-丁吉-2018-11-27"}],"MSG_CONTENT":"","MSG_TYPE":"S"}
	 */
	public String getRequestMark(String dataInfo) {
		BaseBean log = new BaseBean();
		JSONArray arr = new JSONArray();
		String DCPID = "";
		log.writeLog("GetDCPRequestMarkService  dataInfo:" + dataInfo);
		if ("".equals(dataInfo)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "输入参数不能为空");
			log.writeLog("CreateDCPRequestService result:" + getJsonStr(retMap,arr));		
            return getJsonStr(retMap,arr); 
		}	
		try {
			JSONObject jo = new JSONObject(dataInfo);
			DCPID =jo.getString("DCPID");
		} catch (JSONException e1) {
			DCPID = "";
		}
		if ("".equals(DCPID)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "DCPID不能为空");
			log.writeLog("CreateDCPRequestService result:" + getJsonStr(retMap,arr));		
            return getJsonStr(retMap,arr); 
		}
		GetDCPRequestMarkServiceImpl crs = new GetDCPRequestMarkServiceImpl();
		String result = "";
		try {
			result = crs.getRequestMark(DCPID);
		} catch (Exception e) {
			log.writeLog(e);
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "json解析异常");
			log.writeLog("CreateDCPRequestService result:" + getJsonStr(retMap,arr));		
            return getJsonStr(retMap,arr);
		}
		log.writeLog("CreateDCPRequestService result:"+result);
		
        return result;
	}
	
	/**
     * 获取返回值json串
     * @param map 返回结果map
     * @param arr 意见json数组
     * @return
     */
    private String getJsonStr(Map<String, String> map,JSONArray arr) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		try {
			while (it.hasNext()) {
				String key = it.next();
				String value = map.get(key);		
					json.put(key, value);
			}
			json.put("JSONSTR", arr);
	    } catch (JSONException e) {
	    	e.printStackTrace();		
	    }
		

		return json.toString();
	}
}
