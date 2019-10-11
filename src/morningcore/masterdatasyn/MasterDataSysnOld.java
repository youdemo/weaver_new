package morningcore.masterdatasyn;

import morningcore.sap.BringMainAndDetailByMain;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.util.HashMap;
import java.util.Map;

public class MasterDataSysnOld extends BaseCronJob{
	public void execute(){
		getData();
	}

	public void getData(){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String sql = "";
		String workflowId = "1";
		String formid = "";
		String modeid = "";
		sql = "select id from workflow_bill where tablename='uf_km'";
		rs.execute(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql = "select id from modeinfo where formid=" + formid;
		rs.execute(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}
		Map<String,String> oaDatas = new HashMap<String,String>();
		oaDatas.put("IV_FLAG_ACCOUNT","X");
//        oaDatas.put("IV_FLAG_MATERIAL","X");
//        oaDatas.put("IV_FLAG_VENDOR","X");
//        oaDatas.put("IV_FLAG_PURCHASING_GROUP","X");
//        oaDatas.put("IV_FLAG_PAYMENT_TERMS","X");
//        oaDatas.put("IV_FLAG_DEPARTMENT","X");
//        oaDatas.put("IV_FLAG_EMPLOYEE","X");
//        oaDatas.put("IV_FLAG_COST_CENTER","X");
//        oaDatas.put("IV_FLAG_PROFIT_CENTER","X");
//        oaDatas.put("IV_FLAG_INTERNAL_ORDER","X");
//        oaDatas.put("IV_FLAG_DOCUMENT_TYPE","X");
//        oaDatas.put("IV_FLAG_POSTING_KEY","X");
//        oaDatas.put("IV_FLAG_SPECIAL_LEDGER","X");
//        oaDatas.put("IV_FLAG_REASON_CODE","X");
//        oaDatas.put("IV_FLAG_TCURT","X");
//        oaDatas.put("IV_FLAG_TCURR","X");
//        oaDatas.put("IV_FLAG_TAX","X");
//        oaDatas.put("IV_FLAG_CUSTTYPE","X");
//        oaDatas.put("IV_FLAG_CUST","X");
		BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("1");
		String result = bmb.getReturn(oaDatas,workflowId,"",null,"");
		log.writeLog("result=" + result);
//		try{
//			JSONObject json = new JSONObject(result);
//			JSONArray jsonArr = json.getJSONObject("table").getJSONArray("main");
//			JSONObject jsonx = (JSONObject)jsonArr.get(0);
//			jsonArr = json.getJSONObject("table").getJSONArray("Detail");
//			log.writeLog("jsonArr=" + jsonArr.toString());
//			for(int i = 0;i < jsonArr.length();i++){
//				jsonx = (JSONObject)jsonArr.get(i);
//				JSONObject jo = jsonx.getJSONObject("dt");
//				log.writeLog("jo=" + jo.toString());
//				String ACCOUNT = jo.getString("ACCOUNT");
//				String ACCOUNT_NAME = jo.getString("ACCOUNT_NAME");
//				String COMPANY_CODE = jo.getString("COMPANY_CODE");
//
//				InsertUtil iu = new InsertUtil();
//				Map<String,String> mapStr = new HashMap<String,String>();
//				mapStr.put("ACCOUNT",ACCOUNT);
//				mapStr.put("ACCOUNT_NAME",ACCOUNT_NAME);
//				mapStr.put("COMPANY_CODE",COMPANY_CODE);
//
//				mapStr.put("modedatacreater","1");//？？机要秘书
//				mapStr.put("modedatacreatertype","0");
//				mapStr.put("formmodeid",modeid);
//				log.writeLog("mapStr=" + mapStr);
//				iu.insert(mapStr,"uf_km");
//				String billid = "";
//				sql = "select id from uf_km where ACCOUNT='" + ACCOUNT + "' and COMPANY_CODE='" + COMPANY_CODE + "'";
//				rs.execute(sql);
//				if(rs.next()){
//					billid = Util.null2String(rs.getString("id"));
//				}
//				if(!"".equals(billid)){
//					ModeRightInfo ModeRightInfo = new ModeRightInfo();
//					ModeRightInfo.editModeDataShare(1,Integer.valueOf(modeid),Integer.valueOf(billid));
//				}
//
//			}
//		}catch(Exception e){
//			log.writeLog("error=" + e);
//		}

	}
}
