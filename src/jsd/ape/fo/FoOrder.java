package jsd.ape.fo;

import jsd.ape.util.AutoRequestService;
import jsd.ape.util.GetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-6 下午5:19:32
 * 艾派    预测订单
 */
public class FoOrder {
	public String triggerProcess1(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "194";//流程id------------------测试机162 正式194
		GetUtil gu = new GetUtil();
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
//			results = new JSONArray();
		} catch (JSONException e) {
			log.writeLog("异常jsonstr----"+jsar.toString());
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
//					results.put(jsn1);
					return jsn1.toString();
				}
			} catch (JSONException e1) {
				log.writeLog("异常2----"+e1.getMessage());
			}
			JSONObject job = null;
			try {
				
				JSONObject head = jsar.getJSONObject(i).getJSONObject("HEADER");
				if(!head.isNull("CreatedBy")){////////////创建人字段待定-----------------
					String CreatedBy = head.getString("CreatedBy");
					String str = "select id,departmentid,subcompanyid1,workcode from hrmresource where workcode = '"+CreatedBy+"'" ;
					rs.executeSql(str);
					if(rs.next()){
						CreatedBy =Util.null2String(rs.getString("id"));
						String deptid = Util.null2String(rs.getString("departmentid"));
						String sub = Util.null2String(rs.getString("subcompanyid1"));
						String workcode = Util.null2String(rs.getString("workcode"));
						head.put("CreatedBy", CreatedBy);	
						head.put("sqrbm", deptid);	
						head.put("sqrbm", workcode);	
						head.put("gsmc", sub);		
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("CreatedBy")+"创建人不存在OA中\",\"requestid\":\"0\"}");
						} catch (JSONException e) {
							e.printStackTrace();
							log.writeLog("异常3----"+e.getMessage());
						}
						return jsn1.toString();
					}
				}
				
//				if(!head.isNull("Department")){
//					String Department = head.getString("Department");
//					if(Department.length()>0){
//						String str = "select id from hrmdepartment where departmentcode = '"+Department+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							Department =Util.null2String(rs.getString("id"));
//							head.put("Department", Department);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("Department")+"部门不存在OA中\",\"requestid\":\"0\"}");
//							}catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常2----"+e.getMessage());
//							}
//							return jsn1.toString();
//						}				
//					}
//					
//				}
				//改成文本接受2019-06-25
//				if(!head.isNull("OrderOperator")){
//					String OrderOperator = head.getString("OrderOperator");
//					if(OrderOperator.length()>0){
//						String str = "select id from hrmresource where workcode = '"+OrderOperator+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							OrderOperator =Util.null2String(rs.getString("id"));
//							head.put("OrderOperator", OrderOperator);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("OrderOperator")+"创建人不存在OA中\",\"requestid\":\"0\"}");
//							} catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常4----"+e.getMessage());
//							}
////							results.put(jsn1);
//							return jsn1.toString();
//						}
//					}
//					
//				}
			} catch (JSONException e) {
				log.writeLog("异常1----"+e.getMessage());
				e.printStackTrace();
			}
			
			//明细 20190520
			try {
				JSONObject detail = null;
				JSONObject details = jsar.getJSONObject(i).getJSONObject("DETAILS");
				JSONArray arr = null;
				JSONArray arrnew = null;
				try {
					arr = details.getJSONArray("DT1");
					arrnew = new JSONArray();
				} catch (Exception e) {
					continue;
				}
				for (int m = 0; m < arr.length(); m++) {
					JSONObject dt = arr.getJSONObject(m);
					if(!dt.isNull("Num")){
						String Num = dt.getString("Num");
						if(Num.length()<1){
							Num = "0";
						}
						dt.put("Num", gu.getFour(Num));
//						arrnew.put(dt);		
					}
					arrnew.put(dt);	
				}	
				detail = details.put("DT1", arrnew);	
				jsar.getJSONObject(i).put("DETAILS", detail);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		///
			try {
				job = jsar.getJSONObject(i);
				result = as.createRequest(workflowid, job.toString(), createrid, "1");
				JSONObject jsn = new JSONObject(result);
				log.writeLog("result----"+result);
				return jsn.toString();
//				results.put(jsn);
			} catch (JSONException e) {
				log.writeLog("异常json----"+job.toString());
				log.writeLog("异常5----"+e.getMessage());
				JSONObject jsn1 = null;
				try {
					jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
				} catch (JSONException ee) {
					ee.printStackTrace();
					log.writeLog("异常6----"+ee.getMessage());
				}
//				results.put(jsn);
				e.printStackTrace();
				return jsn1.toString();
			}
		}
		return "";
		
	}
}