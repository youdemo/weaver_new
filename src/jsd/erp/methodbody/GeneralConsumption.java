package jsd.erp.methodbody;

import jsd.procedure.AutoRequestService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-6-7 下午9:12:14
 *  庶务类领料
 */
public class GeneralConsumption {
	public String triggerProcess(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "202";//"213";//流程id------------------
		RecordSet rs = new  RecordSet();
		AutoRequestService  as = new AutoRequestService();
		log.writeLog("jsonstr----"+jsonstr+"---flag--"+flag);
		try {
			jsar = new JSONArray(jsonstr);
			results = new JSONArray();
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
					results.put(jsn1);
					continue;
				}
			} catch (JSONException e1) {
				log.writeLog("异常2----"+e1.getMessage());
			}
			JSONObject job = null;
			JSONObject jsn = null;
			try {				
				JSONObject head = jsar.getJSONObject(i).getJSONObject("HEADER");
				if(!head.isNull("FDemandEmpID")){//领料人
					String FDemandEmpID = head.getString("FDemandEmpID");
					String str = "select id,departmentid from hrmresource where workcode = '"+FDemandEmpID+"'" ;
					rs.executeSql(str);
					if(rs.next()){
						FDemandEmpID = Util.null2String(rs.getString("id"));
						String departmentid = Util.null2String(rs.getString("departmentid"));
						head.put("FDemandEmpID", FDemandEmpID);
//						head.put("cjrbm", departmentid);
						
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("FDemandEmpID")+"领料人不存在OA中\",\"requestid\":\"0\"}");
						} catch (JSONException e) {
							e.printStackTrace();
							log.writeLog("异常2----"+e.getMessage());
						}
						results.put(jsn1);
						continue;
					}
				}	
				if(!head.isNull("Fname")){
					StringBuffer stb = new StringBuffer();
					String Fnames = head.getString("Fname");
					String Fname[] = Fnames.split(";");
					for(int j = 0;j<Fname.length;j++){
						String Fjurl[] = Fname[j].split("\\|");
						if(Fjurl.length>1){
							stb.append("<a href=http://10.66.52.60:8080");
							stb.append(Fjurl[0]+"   target=_blank >");
							stb.append(Fjurl[1]);
							stb.append("</a>;");
						}
						
					}
					head.put("Fname", stb.toString());
					jsar.getJSONObject(i).put("HEADER", head);
				}			

				if(!head.isNull("FCreatorID")){//填单人
					String FCreatorID = head.getString("FCreatorID");
					String str = "select id,departmentid from hrmresource where workcode = '"+FCreatorID+"'" ;
					rs.executeSql(str);
					if(rs.next()){
						FCreatorID = Util.null2String(rs.getString("id"));
						String departmentid = Util.null2String(rs.getString("departmentid"));
						head.put("FCreatorID", FCreatorID);
//						head.put("cjrbm", departmentid);
						
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("FCreatorID")+"填单人不存在OA中\",\"requestid\":\"0\"}");
						} catch (JSONException e) {
							e.printStackTrace();
							log.writeLog("异常2----"+e.getMessage());
						}
						results.put(jsn1);
						continue;
					}
				}	
			} catch (JSONException e) {
				log.writeLog("异常1----"+e.getMessage());
				e.printStackTrace();
			}
			try {
				job = jsar.getJSONObject(i);
				result = as.createRequest(workflowid, job.toString(), createrid, "1");
				jsn = new JSONObject(result);
				log.writeLog("result----"+result);
				results.put(jsn);
			} catch (JSONException e) {
				log.writeLog("异常json----"+job.toString());
				log.writeLog("异常2----"+e.getMessage());
				try {
					jsn = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"json异常\",\"requestid\":\"0\"}");
				} catch (JSONException ee) {
					ee.printStackTrace();
					log.writeLog("异常2----"+ee.getMessage());
				}
				results.put(jsn);
				e.printStackTrace();
				continue;
			}
			
		}		
		return results.toString();
		
	}
}
