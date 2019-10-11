package jsd.ape.os;

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
 * @version 创建时间：2019-5-6 下午5:19:46
 *  艾派    外协采购订单
 */
public class OsOrder {
	public String triggerProcess3(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
//		JSONObject jsn = null;
		GetUtil gu = new GetUtil();
		String workflowid = "195";//流程id------------------测试机160
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
						head.put("sqrgh", workcode);	
						head.put("gsmc", sub);	
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
				if(!head.isNull("TaxRate")){
					String TaxRate = head.getString("TaxRate");
					head.put("TaxRate", gu.getFour(TaxRate));	
					jsar.getJSONObject(i).put("HEADER", head);
				}
//				if(!head.isNull("PurDept")){
//					String PurDept = head.getString("PurDept");
//					if(PurDept.length()>0){
//						String str = "select id from hrmdepartment where departmentcode = '"+PurDept+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							PurDept =Util.null2String(rs.getString("id"));
//							head.put("PurDept", PurDept);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("PurDept")+"部门不存在OA中\",\"requestid\":\"0\"}");
//							}catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常2----"+e.getMessage());
//							}
//							return jsn1.toString();
//						}
//					}
//					
//				}
				if(!head.isNull("PurOper")){
					String PurOper = head.getString("PurOper");
					if(PurOper.length()>0){
						String str = "select id from hrmresource where workcode = '"+PurOper+"'" ;
						rs.executeSql(str);
						if(rs.next()){
							PurOper =Util.null2String(rs.getString("id"));
							head.put("PurOper", PurOper);	
							jsar.getJSONObject(i).put("HEADER", head);
						}else{
							JSONObject jsn1 = null;
							try {
								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("PurOper")+"不存在OA中\",\"requestid\":\"0\"}");
							} catch (JSONException e) {
								e.printStackTrace();
								log.writeLog("异常5----"+e.getMessage());
							}
							return jsn1.toString();
						}
					}
					
				}
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
					if(!dt.isNull("ReqQtyTU")){
						String ReqQtyTU = dt.getString("ReqQtyTU");
						dt.put("ReqQtyTU", gu.getFour(ReqQtyTU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("SupplierConfirmQtyTU")){
						String SupplierConfirmQtyTU = dt.getString("SupplierConfirmQtyTU");
						dt.put("SupplierConfirmQtyTU", gu.getFour(SupplierConfirmQtyTU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("UntaxedSubtotal")){
						String UntaxedSubtotal = dt.getString("UntaxedSubtotal");
						dt.put("UntaxedSubtotal", gu.getFour(UntaxedSubtotal));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("UntaxedPrice")){
						String UntaxedPrice = dt.getString("UntaxedPrice");
						dt.put("UntaxedPrice", gu.getFour(UntaxedPrice));
//						arrnew.put(dt);		
					}
					
					if(!dt.isNull("TotalTaxTC")){
						String TotalTaxTC = dt.getString("TotalTaxTC");
						dt.put("TotalTaxTC", gu.getFour(TotalTaxTC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TotalMnyTC")){
						String TotalMnyTC = dt.getString("TotalMnyTC");
						dt.put("TotalMnyTC", gu.getFour(TotalMnyTC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("MaxPrice")){
						String MaxPrice = dt.getString("MaxPrice");
						dt.put("MaxPrice", gu.getFour(MaxPrice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("MinPrice")){
						String MinPrice = dt.getString("MinPrice");
						dt.put("MinPrice", gu.getFour(MinPrice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("RecentPurPrice")){
						String RecentPurPrice = dt.getString("RecentPurPrice");
						dt.put("RecentPurPrice", gu.getFour(RecentPurPrice));
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
				log.writeLog("异常6---"+e.getMessage());
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
