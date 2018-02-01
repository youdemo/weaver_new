package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class CreateRequestServiceEC extends BaseBean {
	
	public OutHeadInfo CreatePurchaseOrder(String head,String workcode, String dataInfo) {
		BaseBean log = new BaseBean();
		log.writeLog("CreatePurchaseOrderService workcode:" + workcode + " dataInfo:" + dataInfo);
		if ("".equals(workcode) || "".equals(dataInfo)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");
			log.writeLog("CreatePurchaseOrderService result:"
					+ getJsonStr(retMap));
			OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut(getJsonStr(retMap));
            return ohi;
			//return getJsonStr(retMap);
		}
		CreateRequestServiceECImpl crs = new CreateRequestServiceECImpl();
		String result=crs.doservice(workcode, dataInfo);
		log.writeLog("CreatePurchaseOrderService result:"+result);
		OutHeadInfo ohi = new OutHeadInfo();
        ohi.setOut(result);
        return ohi;
//        return result;
	}
	public OutHeadInfo CreateECV0006Service(String head,String workcode,String dataInfo) {
		BaseBean log = new BaseBean();
		
		log.writeLog("CreateECV0006Service workcode:" + workcode + " dataInfo:"
				+ dataInfo);
		if ("".equals(workcode) || "".equals(dataInfo)) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配");
            retMap.put("OA_ID", "0");
            log.writeLog("CreateECV0006Service result:" + getJsonStr(retMap));
            OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut(getJsonStr(retMap));
            return ohi;
            //return getJsonStr(retMap);
        }
        CreateRequestServiceECImpl crso = new CreateRequestServiceECImpl();
        String result = crso.doserviceECV0006(workcode, dataInfo);
        log.writeLog("CreateECV0006Service result:" + result);
        OutHeadInfo ohi = new OutHeadInfo();
        ohi.setHead(head);
        ohi.setOut(result);
        return ohi;
        //return result;
	}
	
	public OutHeadInfo CreateBrrowService(String head,String workcode, String dataInfo) {
		BaseBean log = new BaseBean();
		log.writeLog("CreateBrrowService workcode:" + workcode + " dataInfo:"
				+ dataInfo);
		if ("".equals(workcode) || "".equals(dataInfo)) {
			Map<String, String> retMap = new HashMap<String, String>();
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");
			log.writeLog("CreateBrrowService result:" + getJsonStr(retMap));
			OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut(getJsonStr(retMap));
            return ohi;
            //return getJsonStr(retMap);
		}
		CreateRequestServiceECImpl crso = new CreateRequestServiceECImpl();
		String result = crso.doserviceBrrow(workcode, dataInfo);
		log.writeLog("CreateBrrowService result:" + result);
		OutHeadInfo ohi = new OutHeadInfo();
        ohi.setHead(head);
        ohi.setOut(result);
        return ohi;
        //return result;
	}
	
	public OutHeadInfo AutoSubmitV0006(String head,String requestid){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String userid="";
		log.writeLog("AutoSubmitV0006 requestid:" + requestid );
		if("".equals(requestid)){
			OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut("自动提交失败流程requestid为空");
            return ohi;
		}
		String sql="select userid from workflow_currentoperator where  isremark=0  and requestid="+requestid;
		 rs.executeSql(sql);
		 if(rs.next()){
			 userid = Util.null2String(rs.getString("userid"));
		 }
		 if("".equals(userid)){
			OutHeadInfo ohi = new OutHeadInfo();
            ohi.setHead(head);
            ohi.setOut("当前操作者不存在");
            return ohi;
            //return "当前操作者不存在";
		 }
		 CreateRequestServiceECImpl crso = new CreateRequestServiceECImpl();
		 String result =crso.AutoSubmitV0006(requestid, userid);
		 OutHeadInfo ohi = new OutHeadInfo();
         ohi.setHead(head);
         ohi.setOut("自动提交requestid:"+requestid+" 结果:"+result);
         return ohi;
		 //return "自动提交requestid:"+requestid+" 结果:"+result;
	}
	
	public OutHeadInfo AutoBackV0006(String head,String requestid){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String userid="";
		log.writeLog("AutoSubmitV0006 requestid:" + requestid );
		if("".equals(requestid)){
			 OutHeadInfo ohi = new OutHeadInfo();
	         ohi.setHead(head);
	         ohi.setOut("自动提交失败流程requestid为空");
	         return ohi;
			//return "自动提交失败流程requestid为空";
		}
		String sql="select userid from workflow_currentoperator where  isremark=0  and requestid="+requestid;
		 rs.executeSql(sql);
		 if(rs.next()){
			 userid = Util.null2String(rs.getString("userid"));
		 }
		 if("".equals(userid)){
			 OutHeadInfo ohi = new OutHeadInfo();
	         ohi.setHead(head);
	         ohi.setOut("当前操作者不存在");
	         return ohi;
			 //return "当前操作者不存在";
		 }
		 CreateRequestServiceECImpl crso = new CreateRequestServiceECImpl();
		 String result =crso.AutoBackV0006(requestid, userid);
		 OutHeadInfo ohi = new OutHeadInfo();
         ohi.setHead(head);
         ohi.setOut(result);
         return ohi;
		 //return result;
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
