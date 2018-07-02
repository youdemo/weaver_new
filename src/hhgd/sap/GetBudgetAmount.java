package hhgd.sap;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class GetBudgetAmount {
	BaseBean log = new BaseBean();
	public String getResult(String workflowId, String I_DEPNUM,String I_FANUM,String I_PPNUM){
		Map<String, String> oaDatas = new HashMap<String, String>();
		oaDatas.put("I_DEPNUM", I_DEPNUM);
		oaDatas.put("I_FANUM", I_FANUM);
		oaDatas.put("I_PPNUM", I_PPNUM);
		BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("1");
		String result = bmb.getReturn(oaDatas,workflowId,"",null);   
		return result;
   }
	
	public String getDetialinfo(String I_DEPNUM,String I_FANUM,String I_PPNUM){
		log.writeLog("GetBudgetAmount I_DEPNUM:"+I_DEPNUM+" I_FANUM:"+I_FANUM+" I_PPNUM:"+I_PPNUM);
		String result=getResult("115",I_DEPNUM,I_FANUM,I_PPNUM);
		log.writeLog("GetBudgetAmount result:"+result);	
		String E_YS_AMOUNT = "0";
		try {
			org.json.JSONObject json = new org.json.JSONObject(result);
			org.json.JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");	
			for(int index=0;index<jsonArr.length();index++){
				JSONObject jsonx = (JSONObject)jsonArr.get(index);				
				 E_YS_AMOUNT = jsonx.getString("E_YS_AMOUNT");
	
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}	
		if("".equals(E_YS_AMOUNT)){
			E_YS_AMOUNT = "0";
		}
		return E_YS_AMOUNT;	
	}
	
	
}
