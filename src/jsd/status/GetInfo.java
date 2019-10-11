package jsd.status;

import jsd.util.GetUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2018-11-8 下午12:39:36
 * 类说明
 */
public class GetInfo {
	/**
	 * 获取日志信息
	 * @param workflowid 流程id
	 * @param requestid 流程请求id
	 * @return
	 */
	public String getInfos(String workflowid,String requestid){
		GetUtil gu = new GetUtil();
		RecordSet rs = new RecordSet(); 
		RecordSet res = new RecordSet(); 
		BaseBean log = new BaseBean();
		RecordSet res1 = new RecordSet(); 
		RecordSet res2 = new RecordSet(); 
		JSONArray jsarr = new JSONArray(); 
		String sql = "select a.id,a.nodename from  workflow_nodebase a join workflow_flownode b on a.id =b.nodeid where b.workflowid='"+workflowid+"' order by a.id asc" ;
		rs.executeSql(sql);
		while(rs.next()){
			String nodename = Util.null2String(rs.getString("nodename"));
			String nodeid = Util.null2String(rs.getString("id"));
			String str  = "select isremark,receivedate+' '+receivetime as jssj,operatedate+' '+operatetime as clsj,userid from workflow_currentoperator where requestid ='"+requestid+"' and nodeid = '"+nodeid+"'";
			res.executeSql(str);
			while(res.next()){
				JSONObject json = new JSONObject();
				String jssj = Util.null2String(res.getString("jssj"));
				String userid = Util.null2String(res.getString("userid"));
				String isremark = Util.null2String(res.getString("isremark"));
				//
				String clsj = Util.null2String(res.getString("clsj"));//20190118
				//
				try {
					json.put("nodename", nodename);
					json.put("arriveTim", jssj);
					json.put("approvalDep", gu.getDepCode(userid));
					json.put("approvalDepName", gu.getDepName(userid));
					json.put("approvalPeo", gu.getFieldVal("hrmresource", "workcode", "id", userid));
					json.put("approvalPeoName", gu.getFieldVal("hrmresource", "lastname", "id", userid));
				}catch(JSONException e){
					e.printStackTrace();
				}
				if(isremark.equals("0")){
					try {
						json.put("approvalTim", "");
						json.put("approvalSta", "未操作");
						//json.put("remark","");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					jsarr.put(json);
					
				}else if(isremark.equals("4")){
					try {
						json.put("approvalTim", "");
						json.put("approvalSta", "归档");
						//json.put("remark","");
					} catch (JSONException e) {
						e.printStackTrace();
					}
					jsarr.put(json);
					
				}else{
//					String strs = "select operatedate+' '+operatetime as clsj, logtype,remark from workflow_requestlog where requestid='"+requestid+"'  and nodeid = '"+nodeid+"' and operator='"+userid+"'";
//					res1.executeSql(strs);
//					while(res1.next()){
//						String clsj = Util.null2String(res1.getString("clsj"));
//						String logtype = Util.null2String(res1.getString("logtype"));//签字类型
//						String remark = Util.null2String(res1.getString("remark"));
//						try {
//							json.put("approvalTim", clsj);
//							if(clsj.length()>13){
//								String ss = "select dbo.Fun_GetTimes('"+jssj+"','"+clsj+"') as jg ";
//								res2.executeSql(ss);
//								if(res2.next()) json.put("intervalTime", res2.getString("jg"));
//								
//							}
//							json.put("approvalSta", gu.getLogtypee(logtype));
//							//json.put("remark",gu.delHTMLTag(remark));
//						} catch (JSONException e) {
//							e.printStackTrace();
//						}
//						jsarr.put(json);
//					}
					if(clsj.length()>3){
						String strs = "select count(*) as con from workflow_requestlog where requestid='"+requestid+"'  and" +
								" nodeid = '"+nodeid+"' and operator='"+userid+"' and operatedate+' '+operatetime = '"+clsj+"'";
						int a = 0;
						res1.executeSql(strs);
						if(res1.next()){
							a = res1.getInt("con");
						}
						if(a<1){
							try {
								json.put("approvalTim", clsj);
								json.put("approvalSta", "已查看");
								//json.put("remark","");
							} catch (JSONException e) {
								e.printStackTrace();
							}
							jsarr.put(json);
						}else{
							strs = "select operatedate+' '+operatetime as clsj, logtype,remark from workflow_requestlog where requestid='"+requestid+"'  and" +
									" nodeid = '"+nodeid+"' and operator='"+userid+"' and operatedate+' '+operatetime = '"+clsj+"'";
							res1.executeSql(strs);
							while(res1.next()){
								String clsj1 = Util.null2String(res1.getString("clsj"));
								String logtype = Util.null2String(res1.getString("logtype"));//签字类型
//								String remark = Util.null2String(res1.getString("remark"));
								try {
									json.put("approvalTim", clsj1);
									if(clsj1.length()>13){
										String ss = "select dbo.Fun_GetTimes('"+jssj+"','"+clsj1+"') as jg ";
										res2.executeSql(ss);
										if(res2.next()) json.put("intervalTime", res2.getString("jg"));
									}
									json.put("approvalSta", gu.getLogtypee(logtype));
									//json.put("remark",gu.delHTMLTag(remark));
								} catch (JSONException e) {
									e.printStackTrace();
								}
								jsarr.put(json);
							}
						}
					}else{
						try {
							json.put("approvalTim", "");
							json.put("approvalSta", "未查看");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						jsarr.put(json);
					}
				}
				
			}
		}
		log.writeLog("jsarr.toString----"+jsarr.toString());
		return jsarr.toString();
		
	}

}
