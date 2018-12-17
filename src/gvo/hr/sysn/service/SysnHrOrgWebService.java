package gvo.hr.sysn.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class SysnHrOrgWebService {
	/**
	 * 部门公司同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRDepartInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRDepartInfo jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign1 = "";
		String sign2 = "";
		String sign = "S";
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		try {
			shwi.sysSubcompanyInfo(jsonStr, resultArr, "1");
			sign1 = shwi.sysSubcompanyInfo(jsonStr, resultArr, "2");
			shwi.sysDepartmentInfo(jsonStr, resultArr, "1");
			sign2 = shwi.sysDepartmentInfo(jsonStr, resultArr, "2");
			if("E".equals(sign1)||"E".equals(sign2)) {
				sign = "E";
			}
			
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRDepartInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getResultString("deptInf",resultJo.toString(),sign);
		
		
	}
	/**
	 * 职务同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRActicityInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRActicityInfo jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		String sign = "S";
		try {
			sign = shwi.sysJobActivityInfo(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRActicityInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getResultString("jobInf",resultJo.toString(),sign);
		
		
	}
	/**
	 * 岗位同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRJobTitleInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRJobTitleInfo jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign = "S";
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		try {
			sign = shwi.sysJobTitleInfo(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRJobTitleInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getResultString("posnInf",resultJo.toString(),sign);
		
		
	}
	
	/**
	 * 人员基本信息同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRPersonInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRPersonInfo jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign = "S";
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		try {
			sign = shwi.SysHrmresourceInfo(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRPersonInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return getResultString("perInf",resultJo.toString(),sign);
		
		
	}
	
	/**
	 * 人员职务同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRPersonJobInfo(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRPersonJobInfo jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign = "S";
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		try {
			sign = shwi.sysPersonJobRelation(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRPersonJobInfo json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return getResultString("jobDataInf",resultJo.toString(),sign);
		
		
	}
	
	/**
	 * 人员职务同步
	 * @param jsonStr
	 * @return
	 */
	public String sysnHRPersonJobInfoALL(String jsonStr) {
		BaseBean log = new BaseBean();
		log.writeLog("SysnHrOrgWebService sysnHRPersonJobInfoALL jsonStr:"+jsonStr);
		JSONObject resultJo = new JSONObject();
		JSONArray  resultArr = new JSONArray();
		String sign = "S";
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
		try {
			sign = shwi.sysPersonJobRelationALL(jsonStr, resultArr);
			if("E".equals(sign)) {
				sign = "E";
			}else {
				sign = "S";
			}
		} catch (Exception e) {
			sign = "E";
			log.writeLog(e);
			log.writeLog("SysnHrOrgWebService sysnHRPersonJobInfoALL json格式解析异常");
			resultArr.put(shwi.getResultJo("", "", "", "E", "json格式解析异常"));
		}
		
		try {
			resultJo.put("responses", resultArr);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return getResultString("jobDataInf",resultJo.toString(),sign);
		
		
	}
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
		SysnHrOrgWebserviceImpl shwi = new SysnHrOrgWebserviceImpl();
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
	/**
	 * 拼接返回值字符串
	 * @param interfaceName
	 * @param responseString
	 * @return
	 */
	private String getResultString(String interfaceName,String responseString,String sign) {
		String result="<HPS_RESPONSE><SIGN>"+sign+"</SIGN><SYSTEM_ID>OA</SYSTEM_ID><INTERFACE_NAME>"+
			interfaceName+"</INTERFACE_NAME><RETURN>"+responseString+"</RETURN></HPS_RESPONSE>";
		return result;
	}
}
