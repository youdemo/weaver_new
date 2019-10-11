package jsd.erp.methodbody;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import jsd.procedure.AutoRequestService;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-5-14 上午10:09:19
 * 加工件需求-BOM需求
 */
public class ENBBomApply {
	public String triggerProcess(String jsonstr,String flag){
		BaseBean log = new BaseBean();
		JSONArray jsar = null;
		JSONArray results = null;
		String createrid = "";
		String creatercode = "";
		String result = "";
		String workflowid = "199";// "191";//流程id------------------
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
				if(!head.isNull("F_CreatorName")){
					String F_CreatorName = head.getString("F_CreatorName");
					String str = "select id,departmentid,workcode from hrmresource where workcode = '"+F_CreatorName+"'" ;
					rs.executeSql(str);
					if(rs.next()){
						F_CreatorName =Util.null2String(rs.getString("id"));
						String departmentid = Util.null2String(rs.getString("departmentid"));
						String workcode = Util.null2String(rs.getString("workcode"));
						head.put("cjrbm", departmentid);
						if(head.isNull("F_CreatorID")){
							head.put("F_CreatorID", workcode);
						}
						head.put("F_CreatorName", F_CreatorName);	
						jsar.getJSONObject(i).put("HEADER", head);
					}else{
						JSONObject jsn1 = null;
						try {
							jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\""+head.getString("F_CreatorName")+"创建人不存在OA中\",\"requestid\":\"0\"}");
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
				if(!head.isNull("TPM")){
					String TPMS = head.getString("TPM");
					String stt = "";
					if(TPMS.length()>0){
						String tmp[] = TPMS.split(",");
						for(int k =0;k<tmp.length;k++){
							String str = "select id,departmentid from hrmresource where workcode = '"+tmp[k]+"'" ;
							rs.executeSql(str);
							if(rs.next()){
								String tp = Util.null2String(rs.getString("id"));
								if(stt.length()>0){
									stt = stt+","+tp;
								}else{ 
									stt = stt+tp;
								}
							}else{
								JSONObject jsn1 = null;
								try {
									jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"TPM"+tmp[k]+"人不存在OA中\",\"requestid\":\"0\"}");
								} catch (JSONException e) {
									e.printStackTrace();
									log.writeLog("异常2----"+e.getMessage());
								}
								return results.put(jsn1).toString();
							}
						}
						head.put("TPM", stt);
						jsar.getJSONObject(i).put("HEADER", head);
					}
				}
				if(!head.isNull("PM")){
					String TPMS = head.getString("PM");
					String stt = "";
					if(TPMS.length()>0){
						String tmp[] = TPMS.split(",");
						for(int k =0;k<tmp.length;k++){
							String str = "select id,departmentid from hrmresource where workcode = '"+tmp[k]+"'" ;
							rs.executeSql(str);
							if(rs.next()){
								String tp = Util.null2String(rs.getString("id"));
								if(stt.length()>0){
									stt = stt+","+tp;
								}else{ 
									stt = stt+tp;
								}
							}else{
								JSONObject jsn1 = null;
								try {
									jsn1 = new JSONObject("{\"return_type\":\"E\",\"return_message\":\"PM"+tmp[k]+"人不存在OA中\",\"requestid\":\"0\"}");
								} catch (JSONException e) {
									e.printStackTrace();
									log.writeLog("异常2----"+e.getMessage());
								}
								return results.put(jsn1).toString();
							}
						}
						head.put("PM", stt);
						jsar.getJSONObject(i).put("HEADER", head);
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

