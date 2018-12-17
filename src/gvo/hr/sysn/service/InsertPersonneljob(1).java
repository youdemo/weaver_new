package gvo.hr.sysn.service;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;
import weaver.general.Util;

public class InsertPersonneljob {
	public String  InsertInfo(String jsonstr) throws JSONException{
		BaseBean log = new BaseBean();
		JSONObject json = new JSONObject(jsonstr);
		JSONArray  jsa = json.getJSONArray("employeeJobs");
		InsertUtil  insert = new InsertUtil();
		for(int i=0;i<jsa.length();i++){
			Map<String,String> map = new HashMap<String,String>();
			JSONObject json1 = jsa.getJSONObject(i);
			try{
				map.put("emplid", Util.null2String(json1.get("emplid")));
				map.put("emplRcd", Util.null2String(json1.get("emplRcd")));
				map.put("hr_status", Util.null2String(json1.get("hr_status")));
				map.put("socialDt", Util.null2String(json1.get("socialDt")));
				map.put("hireDt", Util.null2String(json1.get("hireDt")));
				map.put("terDt", Util.null2String(json1.get("terDt")));
				map.put("enterGpDt", Util.null2String(json1.get("enterGpDt")));
				map.put("positionNbr", Util.null2String(json1.get("positionNbr")));
				map.put("deptId", Util.null2String(json1.get("deptId")));
				map.put("locationId", Util.null2String(json1.get("locationId")));
				map.put("idenCategory", Util.null2String(json1.get("idenCategory")));
				map.put("jobIndiactor", Util.null2String(json1.get("jobIndiactor")));
				map.put("action", Util.null2String(json1.get("action")));
				map.put("action_rsn", Util.null2String(json1.get("action_rsn")));
				map.put("actionFlag", Util.null2String(json1.get("actionFlag")));
				map.put("batchNumber", Util.null2String(json1.get("batchNumber")));
				map.put("repordsTold", Util.null2String(json1.get("repordsTold")));
				map.put("repordsToPosn", Util.null2String(json1.get("repordsToPosn")));
				map.put("sfcl", "0");//是否处理   0 未处理  1处理
				///以下字段excle 没有
	//			map.put("jobcode", Util.null2String(json1.get("jobcode")));//
	//			map.put("channel", Util.null2String(json1.get("channel")));
	//			map.put("sequence", Util.null2String(json1.get("sequence")));
	//			map.put("grade", Util.null2String(json1.get("grade")));
	//			map.put("lastupdttm", Util.null2String(json1.get("lastupdttm")));
				insert.insert(map, "uf_personneljob");	
			}catch (Exception e) {
				log.writeLog("json 解析错误"+json1.toString());
			}
		}
		return "";
		
	}
	

}
