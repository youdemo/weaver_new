package gvo.hr.sysn.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class SysnHrMatrixInfoService {
	/**
	 * HR矩阵
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRMatrixInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign = "S";
		log.writeLog("SysnHrOrgWebService sysnHRMatrixInfo jsonStr:"+jsonStr);
		SysnHrMatrixInfoServiceImpl shwi = new SysnHrMatrixInfoServiceImpl();
		try {
			sign = shwi.SysMatrixInfo(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService SysMatrixInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return getResultString("matrixInf",resultJo.toString(),sign);			
	}
	private String getResultString(String interfaceName,String responseString,String sign) {
		String result="<HPS_RESPONSE><SIGN>"+sign+"</SIGN><SYSTEM_ID>OA</SYSTEM_ID><INTERFACE_NAME>"+
			interfaceName+"</INTERFACE_NAME><RETURN>"+responseString+"</RETURN></HPS_RESPONSE>";
		return result;
	}
}
