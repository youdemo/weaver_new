package jsd.ape.gys;

import jsd.ape.util.AutoRequestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-6 下午5:18:34
 * 艾派     供应商档案
 */
public class GysFile {
	public String triggerProcess2(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
//		JSONObject jsn = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "190";//流程id------------------159 正式190
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
//			results = new JSONArray();
		} catch (JSONException e) {
			log.writeLog("jsonstr----"+jsar.toString());
			e.printStackTrace();
		}
		for(int i = 0;i<jsar.length();i++){
			try {
				creatercode = jsar.getJSONObject(i).getString("CREATERCODE");
				String sql = "select id from hrmresource where workcode = '"+creatercode+"'" ;
				rs.executeSql(sql);
				if(rs.next()){
					createrid =Util.null2String(rs.getString("id"));
				}else{
					JSONObject jsn1 = null;
					try {
						jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"创建人不存在OA中\",\"requestid\":\"0\"}");
					} catch (JSONException e) {
						e.printStackTrace();
						
						log.writeLog("异常2----"+e.getMessage());
					}
					return jsn1.toString();
				}
			} catch (JSONException e1) {
				log.writeLog("异常3----"+e1.getMessage());
			}
			JSONObject job = null;
			try {
				
				JSONObject head = jsar.getJSONObject(i).getJSONObject("HEADER");
				if(!head.isNull("CreatedBy")){////////////字段待定-----------------
					String CreatedBy = head.getString("CreatedBy");
					String str = "select id,departmentid,subcompanyid1,workcode from hrmresource where workcode = '"+CreatedBy+"'" ;
					rs.executeSql(str);
					if(rs.next()){
						CreatedBy =Util.null2String(rs.getString("id"));
						String deptid = Util.null2String(rs.getString("departmentid"));
						String sub = Util.null2String(rs.getString("subcompanyid1"));
						String workcode = Util.null2String(rs.getString("workcode"));
						
						head.put("CreatedBy", CreatedBy);	
						head.put("cjrbm", deptid);	
						head.put("gsmc", sub);
						head.put("cjrgh", workcode);	
						
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("CreatedBy")+"创建人不存在OA中\",\"requestid\":\"0\"}");
						} catch (JSONException e) {
							e.printStackTrace();
							log.writeLog("异常4----"+e.getMessage());
						}
						return jsn1.toString();
					}
				}
				//改成文本接收  2019-06-25
//				if(!head.isNull("SupplierContacts")){
//					String SupplierContacts = head.getString("SupplierContacts");
//					if(SupplierContacts.length()>0){
//						String str = "select id from hrmresource where workcode = '"+SupplierContacts+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							SupplierContacts =Util.null2String(rs.getString("id"));
//							head.put("CreatedBy", SupplierContacts);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("SupplierContacts")+"创建人不存在OA中\",\"requestid\":\"0\"}");
//							} catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常5----"+e.getMessage());
//							}
//							return jsn1.toString();
//						}
//					}
//					
//				}
				
			} catch (JSONException e) {
				log.writeLog("异常1----"+e.getMessage());
				e.printStackTrace();
			}
			try {
				job = jsar.getJSONObject(i);
				result = as.createRequest(workflowid, job.toString(), createrid, "1");
				JSONObject jsn = new JSONObject(result);
				log.writeLog("result----"+result);
				return jsn.toString();
//				results.put(jsn);
			} catch (JSONException e) {
				log.writeLog("异常json----"+job.toString());
				log.writeLog("异常6----"+e.getMessage());
				JSONObject jsn1 = null;
				try {
					jsn1  = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
				} catch (JSONException ee) {
					ee.printStackTrace();
					log.writeLog("异常7----"+ee.getMessage());
				}
//				results.put(jsn);
				e.printStackTrace();
				return jsn1.toString();
			}
		}
		return "";
		
	}
}
