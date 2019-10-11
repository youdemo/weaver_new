package jsd.ape.re;

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
 * @version 创建时间：2019-5-6 下午5:19:19
 *  艾派    退货处理单
 */
public class ReOrder {
	public String triggerProcess8(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
//		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
//		JSONObject jsn = null;
		String workflowid = "193";//流程id------------------测试机163
		GetUtil gu = new GetUtil();
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
//			results = new JSONArray();
		} catch (JSONException e) {
			log.writeLog("jsonstr1111----"+jsar.toString());
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
						String workcode = Util.null2String(rs.getString("workcode"));
						String sub = Util.null2String(rs.getString("subcompanyid1"));
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
				if(!head.isNull("TotalSum")){
					String TotalSum = head.getString("TotalSum");
					head.put("TotalSum", gu.getFour(TotalSum));	
					jsar.getJSONObject(i).put("HEADER", head);
				}
				if(!head.isNull("TotalQty")){
					String TotalQty = head.getString("TotalQty");;
					head.put("TotalQty", gu.getFour(TotalQty));	
					jsar.getJSONObject(i).put("HEADER", head);
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
					continue;
				}
				for (int m = 0; m < arr.length(); m++) {
					JSONObject dt = arr.getJSONObject(m);
					if(!dt.isNull("ApplyQtyPU")){
						String ApplyQtyPU = dt.getString("ApplyQtyPU");
						dt.put("ApplyQtyPU", gu.getFour(ApplyQtyPU));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("RtnQtyPU")){
						String RtnQtyPU = dt.getString("RtnQtyPU");
						dt.put("RtnQtyPU", gu.getFour(RtnQtyPU));
//						arrnew.put(dt);		
					}
					
					if(!dt.isNull("ApplyValuationQty")){
						String ApplyValuationQty = dt.getString("ApplyValuationQty");
						dt.put("ApplyValuationQty", gu.getFour(ApplyValuationQty));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("VerificationValuationQty")){
						String VerificationValuationQty = dt.getString("VerificationValuationQty");
						dt.put("VerificationValuationQty", gu.getFour(VerificationValuationQty));
//						arrnew.put(dt);		
					}	
					if(!dt.isNull("OrderPrice")){
						String OrderPrice = dt.getString("OrderPrice");
						dt.put("OrderPrice", gu.getFour(OrderPrice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("DiscountRate")){
						String DiscountRate = dt.getString("DiscountRate");
						dt.put("DiscountRate", gu.getFour(DiscountRate));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ApplyPrice")){
						String ApplyPrice = dt.getString("ApplyPrice");
						dt.put("ApplyPrice", gu.getFour(ApplyPrice));
//						arrnew.put(dt);		
					}					
					if(!dt.isNull("RtnPice")){
						String RtnPice = dt.getString("RtnPice");
						dt.put("RtnPice", gu.getFour(RtnPice));
//						arrnew.put(dt);		
					}
					if(!dt.isNull("ApplyMoneyTC")){
						String ApplyMoneyTC = dt.getString("ApplyMoneyTC");
						dt.put("ApplyMoneyTC", gu.getFour(ApplyMoneyTC));
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