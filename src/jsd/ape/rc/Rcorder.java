package jsd.ape.rc;

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
 * @version 创建时间：2019-5-6 下午5:19:57
 *  艾派    请款单
 */
public class Rcorder {
	public String triggerProcess7(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
//		JSONObject jsn = null;
		GetUtil gu = new GetUtil();
		String workflowid = "187";//流程id------------------测试机164
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
						head.put("sqrgh", workcode);	
						head.put("sqrbm", deptid);	
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
				if(!head.isNull("PaymentMoney")){
					String PaymentMoney = head.getString("PaymentMoney");
					head.put("PaymentMoney", gu.getFour(PaymentMoney));	
					jsar.getJSONObject(i).put("HEADER", head);
				}
				if(!head.isNull("PaymentAmount")){
					String PaymentAmount = head.getString("PaymentAmount");
					head.put("PaymentAmount", gu.getFour(PaymentAmount));	
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
				log.writeLog("异常6----"+e.getMessage());
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
					log.writeLog("明细异常0----"+arr.length() );
					continue;
				}
				System.out.println(arr.length());
				for (int m = 0; m < arr.length(); m++) {
					JSONObject dt = arr.getJSONObject(m);
					if(!dt.isNull("MatchingQty")){
						String MatchingQty = dt.getString("MatchingQty");
						dt.put("MatchingQty", gu.getFour(MatchingQty));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("AccountsQty")){
						String AccountsQty = dt.getString("AccountsQty");
						dt.put("AccountsQty", gu.getFour(AccountsQty));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ContainPriceTax")){
						String ContainPriceTax = dt.getString("ContainPriceTax");
						dt.put("ContainPriceTax", gu.getFour(ContainPriceTax));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("NonTaxAmount")){
						String NonTaxAmount = dt.getString("NonTaxAmount");
						dt.put("NonTaxAmount", gu.getFour(NonTaxAmount));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TaxAmount")){
						String TaxAmount = dt.getString("TaxAmount");
						dt.put("TaxAmount", gu.getFour(TaxAmount));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TotalMnyTC")){
						String TotalMnyTC = dt.getString("TotalMnyTC");
						dt.put("TotalMnyTC", gu.getFour(TotalMnyTC));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TotalSum")){
						String TotalSum = dt.getString("TotalSum");
						dt.put("TotalSum", gu.getFour(TotalSum));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("TotalBalance")){
						String TotalBalance = dt.getString("TotalBalance");
						dt.put("TotalBalance", gu.getFour(TotalBalance));
//						arrnew.put(dt);		
					}
					log.writeLog("dt------"+dt.toString());
					arrnew.put(dt);	
				}	
				log.writeLog("arrnew----"+arrnew.toString());
				detail = details.put("DT1", arrnew);	
				jsar.getJSONObject(i).put("DETAILS", detail);
				
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		///	
			try {
				job = jsar.getJSONObject(i);
				log.writeLog("job--"+job.toString());
				result = as.createRequest(workflowid, job.toString(), createrid, "1");
				JSONObject jsn = new JSONObject(result);
				log.writeLog("result----"+result);
				return jsn.toString();
			} catch (JSONException e) {
				log.writeLog("异常json----"+job.toString());
				log.writeLog("异常7----"+e.getMessage());
				JSONObject jsn1 = null;
				try {
					jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
				} catch (JSONException ee) {
					ee.printStackTrace();
					log.writeLog("异常8----"+ee.getMessage());
				}
				e.printStackTrace();
				return jsn1.toString();
			}
		}
		return "";
		
	}
}

