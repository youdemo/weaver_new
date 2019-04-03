package lx.sap;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class GetBudgetDataInfo {
	/**
	 * 
	 * @param workflowId 中间表配置id 默认1
	 * @param ZC_ACCT 科目
	 * @param ZC_APPNUM 申请/付款编号
	 * @param ZC_ASSET 预算资产号
	 * @param ZC_CSTCTR 成本中心
	 * @param ZC_CTRLM 控制月份
	 * @param ZC_PROJ 项目
	 * @return
	 */
	public String getResult(String workflowId, String ZC_ACCT,String ZC_APPNUM,String ZC_ASSET,String ZC_CSTCTR,String ZC_CTRLM,String ZC_PROJ){
		Map<String, String> oaDatas = new HashMap<String, String>();
		oaDatas.put("/BIC/ZC_ACCT", ZC_ACCT);
		oaDatas.put("/BIC/ZC_APPNUM", ZC_APPNUM);
		oaDatas.put("/BIC/ZC_ASSET", ZC_ASSET);
		oaDatas.put("/BIC/ZC_CSTCTR", ZC_CSTCTR);
		oaDatas.put("/BIC/ZC_CTRLM", ZC_CTRLM);
		oaDatas.put("/BIC/ZC_PROJ", ZC_PROJ);
		BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("2");
		String result = bmb.getReturn(oaDatas,workflowId,"ZSUBJECT_HEADER",null);   
		return result;
   }
	/**
	 * 
	 * @param ZC_ACCT 科目
	 * @param ZC_APPNUM 申请/付款编号
	 * @param ZC_ASSET 预算资产号
	 * @param ZC_CSTCTR 成本中心
	 * @param ZC_CTRLM 控制月份
	 * @param ZC_PROJ 项目
	 * @return
	 */
	public String getDetialinfo(String ZC_ACCT,String ZC_APPNUM,String ZC_ASSET,String ZC_CSTCTR,String ZC_CTRLM,String ZC_PROJ){
		BaseBean log = new BaseBean();
		String budgetamount = "";
		log.writeLog("getDetialinfo ZC_ACCT:"+ZC_ACCT+" ZC_APPNUM:"+ZC_APPNUM+" ZC_ASSET:"+ZC_ASSET+" ZC_CSTCTR:"+ZC_CSTCTR+" ZC_CTRLM:"+ZC_CTRLM+" ZC_PROJ:"+ZC_PROJ);
		String result=getResult("1",ZC_ACCT,ZC_APPNUM,ZC_ASSET,ZC_CSTCTR,ZC_CTRLM,ZC_PROJ);
		log.writeLog("getDetialinfo result:"+result);	
		try {
			org.json.JSONObject json = new org.json.JSONObject(result);
			org.json.JSONArray jsonArr = json.getJSONObject("table").getJSONArray("Detail");	
			for (int index = 0; index < jsonArr.length(); index++) {
				JSONObject jsonx = (JSONObject) jsonArr.get(index);
				JSONObject jo = jsonx.getJSONObject("dt");
				String ZC_ACCT_OUT = jo.getString("/BIC/ZC_ACCT");// 科目
				String ZC_APPNUM_OUT = jo.getString("/BIC/ZC_APPNUM");// 申请/付款编号
				String ZC_ASSET_OUT = jo.getString("/BIC/ZC_ASSET");// 预算资产号
				String ZC_CSTCTR_OUT = jo.getString("/BIC/ZC_CSTCTR");// 成本中心
				String ZC_CTRLM_OUT = jo.getString("/BIC/ZC_CTRLM");// 控制月份
				String ZC_PROJ_OUT = jo.getString("/BIC/ZC_PROJ");// 项目
				String ZI_ADDAMT = jo.getString("/BIC/ZI_ADDAMT");// 增加金额
				String ZI_ADGAMT = jo.getString("/BIC/ZI_ADGAMT");// 调整金额
				String ZI_BGTAMT = jo.getString("/BIC/ZI_BGTAMT");// 预算金额
				String ZI_DEDAMT = jo.getString("/BIC/ZI_DEDAMT");// 扣减金额
				String ZI_FRZAMT = jo.getString("/BIC/ZI_FRZAMT");// 冻结金额
				String ZI_PAYAMT = jo.getString("/BIC/ZI_PAYAMT");// 实际付款金额
				String ZI_PAYFRZ = jo.getString("/BIC/ZI_PAYFRZ");// 付款冻结金额
				

			}
		} catch (JSONException e) {
			log.writeLog(e);
		}	
		return budgetamount;	
	}
}
