package jsd.ape.qu;

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
 * @version 创建时间：2019-5-6 下午5:18:53
 *  艾派    报价单
 */
public class QuOrder {
	public String triggerProcess6(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "191";//流程id------------------测试机156
		GetUtil gu = new GetUtil();
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
//			results = new JSONArray();
		} catch (JSONException e) {
			log.writeLog("jsonstrqq----"+jsar.toString());
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
						
						log.writeLog("异常1----"+e.getMessage());
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
						String workcode = Util.null2String(rs.getString("workcode"));
						String sub = Util.null2String(rs.getString("subcompanyid1"));
						head.put("CreatedBy", CreatedBy);	
						head.put("sqrbm", deptid);	
						head.put("gsmc", sub);	
						head.put("sqrgh", workcode);	
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("CreatedBy")+"创建人不存在OA中\",\"requestid\":\"0\"}");
						} catch (JSONException e) {
							e.printStackTrace();
							log.writeLog("异常3----"+e.getMessage());
						}
//						results.put(jsn1);
						return jsn1.toString();
					}
				}
//				if(!head.isNull("SaleDept")){
//					String SaleDept = head.getString("SaleDept");
//					if(SaleDept.length()>0){
//						String str = "select id from hrmdepartment where departmentcode = '"+SaleDept+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							SaleDept =Util.null2String(rs.getString("id"));
//							head.put("SaleDept", SaleDept);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("SaleDept")+"部门不存在OA中\",\"requestid\":\"0\"}");
//							}catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常2----"+e.getMessage());
//							}
////							results.put(jsn1);
//							return jsn1.toString();
//						}
//					}
//					
//				}
				
				if(!head.isNull("Seller")){
					String Seller = head.getString("Seller");
					if(Seller.length()>0){
						String str = "select id from hrmresource where workcode = '"+Seller+"'" ;
						rs.executeSql(str);
						if(rs.next()){
							Seller =Util.null2String(rs.getString("id"));
							head.put("Seller", Seller);	
							jsar.getJSONObject(i).put("HEADER", head);
						}else{
							JSONObject jsn1 = null;
							try {
								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("SupplierContacts")+"创建人不存在OA中\",\"requestid\":\"0\"}");
							} catch (JSONException e) {
								e.printStackTrace();
								log.writeLog("异常4----"+e.getMessage());
							}
//							results.put(jsn1);
							return jsn1.toString();
						}
					}
					
				}
				
			} catch (JSONException e) {
				log.writeLog("异常5----"+e.getMessage());
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
					log.writeLog("明细错误--------");
					continue;
				}
				for (int m = 0; m < arr.length(); m++) {
					JSONObject dt = arr.getJSONObject(m);
					if(!dt.isNull("OrderQtyTU")){
						String OrderQtyTU = dt.getString("OrderQtyTU");
						dt.put("OrderQtyTU", gu.getFour(OrderQtyTU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("OrderQtyTBU")){
						String OrderQtyTBU = dt.getString("OrderQtyTBU");
						dt.put("MaxPrice", gu.getFour(OrderQtyTBU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ReferencePrice")){
						String ReferencePrice = dt.getString("ReferencePrice");
						dt.put("ReferencePrice", gu.getFour(ReferencePrice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("FinallyPriceFC")){
						String FinallyPriceFC = dt.getString("FinallyPriceFC");
						dt.put("FinallyPriceFC", gu.getFour(FinallyPriceFC));
//						arrnew.put(dt);		
					}					
					if(!dt.isNull("TBUToPBURate")){
						String TBUToPBURate = dt.getString("TBUToPBURate");
						dt.put("TBUToPBURate", gu.getFour(TBUToPBURate));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("DiscountRate")){
						String DiscountRate = dt.getString("DiscountRate");
						dt.put("DiscountRate", gu.getFour(DiscountRate));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("NetMoneyFC")){
						String NetMoneyFC = dt.getString("NetMoneyFC");
						dt.put("NetMoneyFC", gu.getFour(NetMoneyFC));
//						arrnew.put(dt);		
					}					
					if(!dt.isNull("TotalMoneyFC")){
						String TotalMoneyFC = dt.getString("TotalMoneyFC");
						dt.put("TotalMoneyFC", gu.getFour(TotalMoneyFC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TaxMoneyFC")){
						String TaxMoneyFC = dt.getString("TaxMoneyFC");
						dt.put("TaxMoneyFC", gu.getFour(TaxMoneyFC));
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
				log.writeLog("jsn----"+jsn);
				return jsn.toString();
//				results.put(jsn);
			} catch (JSONException e) {
				log.writeLog("异常json----"+job.toString());
				log.writeLog("异常6----"+e.getMessage());
				JSONObject jsn1 = null;
				try {
					jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
				} catch (JSONException ee) {
					ee.printStackTrace();
					log.writeLog("异常7----"+ee.getMessage());
				}
//				results.put(jsn);
				e.printStackTrace();
				return jsn1.toString();
			}
		}
		
		return "";//jsn.toString();
		
	}
}

