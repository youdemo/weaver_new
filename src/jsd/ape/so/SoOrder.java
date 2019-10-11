package jsd.ape.so;

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
 * @version 创建时间：2019-5-6 下午5:19:04
 *  艾派    销售订单
 */
public class SoOrder {
	public String triggerProcess9(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
//		JSONObject jsn = null;
		GetUtil gu = new GetUtil();
		String workflowid = "192";//流程id------------------测试161
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
//			results = new JSONArray();
		} catch (JSONException e) {
			log.writeLog("jsonstrqqq----"+jsar.toString());
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
//				if(!head.isNull("SaleDepartment")){
//					String SaleDepartment = head.getString("SaleDepartment");
//					if(SaleDepartment.length()>0){
//						String str = "select id from hrmdepartment where departmentcode = '"+SaleDepartment+"'" ;
//						rs.executeSql(str);
//						if(rs.next()){
//							SaleDepartment =Util.null2String(rs.getString("id"));
//							head.put("SaleDepartment", SaleDepartment);	
//							jsar.getJSONObject(i).put("HEADER", head);
//						}else{
//							JSONObject jsn1 = null;
//							try {
//								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("SaleDepartment")+"部门不存在OA中\",\"requestid\":\"0\"}");
//							}catch (JSONException e) {
//								e.printStackTrace();
//								log.writeLog("异常2----"+e.getMessage());
//							}
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
								jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+ head.getString("Seller")+"业务员不存在OA中\",\"requestid\":\"0\"}");
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
					if(!dt.isNull("OrderByQtyTU")){
						String OrderByQtyTU = dt.getString("OrderByQtyTU");
						dt.put("OrderByQtyTU", gu.getFour(OrderByQtyTU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ValuationQty")){
						String ValuationQty = dt.getString("ValuationQty");
						dt.put("ValuationQty", gu.getFour(ValuationQty));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("FinallyPriceTC")){
						String FinallyPriceTC = dt.getString("FinallyPriceTC");
						dt.put("FinallyPriceTC", gu.getFour(FinallyPriceTC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ReferencePrice")){
						String ReferencePrice = dt.getString("ReferencePrice");
						dt.put("ReferencePrice", gu.getFour(ReferencePrice));
//						arrnew.put(dt);		
					}	
					if(!dt.isNull("StandardPrice")){
						String StandardPrice = dt.getString("StandardPrice");
						dt.put("StandardPrice", gu.getFour(StandardPrice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TBUToPBURate")){
						String TBUToPBURate = dt.getString("TBUToPBURate");
						dt.put("TBUToPBURate", gu.getFour(TBUToPBURate));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TotalMoneyTC")){
						String TotalMoneyTC = dt.getString("TotalMoneyTC");
						dt.put("TotalMoneyTC", gu.getFour(TotalMoneyTC));
//						arrnew.put(dt);		
					}					
					if(!dt.isNull("NetMoneyTC")){
						String NetMoneyTC = dt.getString("NetMoneyTC");
						dt.put("NetMoneyTC", gu.getFour(NetMoneyTC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TaxMoneyTC")){
						String TaxMoneyTC = dt.getString("TaxMoneyTC");
						dt.put("TaxMoneyTC", gu.getFour(TaxMoneyTC));
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
				return jsn1.toString();
			}
		}
		return "";
		
	}
}

