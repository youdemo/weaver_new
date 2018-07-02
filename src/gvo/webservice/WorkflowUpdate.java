package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class WorkflowUpdate extends BaseBean{
	
	/**
	 * 更新流程数据方法
	 * @param WrokflowID 流程ID
	 * @param strJson  格式说明  ：例子：{"b1":"2","a1":"1"}  a1,b1：资金系统字段名
	 * @return
	 * @throws JSONException 
	 */
	public OutHeadInfo updateWorkflowRequest(String head,String requestid,String strJson) throws JSONException{
		JSONObject jsonObject = new JSONObject(strJson);	//json值
		Map<String, String> map = new HashMap<String, String>();	//json转
		Map<String, String> retMap = new HashMap<String, String>();
		Iterator iterator = jsonObject.keys();
		while(iterator.hasNext()){
			String key = (String) iterator.next();
			String value = jsonObject.getString(key);
			map.put(key, value);
		}
		
		int status=updateInfo(requestid,map);
		switch(status){
		case 0:
			retMap.put("MSG_TYPE", "S");
			retMap.put("MSG_CONTENT", "Workflow info update success!");
			break;
		case 1:
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "Workflow Can't be found!");
			break;
		case 2:
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "Update Field Can't be found!");
			break;
		}
		OutHeadInfo ohi = new OutHeadInfo();
        ohi.setHead(head);
        ohi.setOut(getJsonStr(retMap));
        return ohi;
		//return getJsonStr(retMap);
	}
	
	/**
	 * 更新流程信息
	 * @param WrokflowID 流程ID
	 * @param Map  资金系统字段及值
	 * @return
	 */
	private int updateInfo(String requestid,Map<String, String> map){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rsx = new RecordSet();
		
		String WrokflowID="";	//流程ID
		String tableName="";	//流程表名
		StringBuffer tmp_field =new StringBuffer();	//更新数据用
		Map<String, String> retMap = new HashMap<String, String>();	//异常抛出用
		
		String sql=" select workflowid from workflow_requestbase where requestid='"+requestid+"'";
		rs.execute(sql);
		//log.writeLog("WorkflowID get:"+sql);
		if(rs.next()){
			WrokflowID = Util.null2String(rs.getString("workflowid"));
		}
		
		if("".equals(WrokflowID)){
			return 1;
		}
		
		sql  = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= "
				+ WrokflowID + ")";
		
		rs.execute(sql);
		log.writeLog("Tablename get:"+sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		
		Iterator<String> tmp = map.keySet().iterator();
		while (tmp.hasNext()) {
			String key = tmp.next();		
			String value = map.get(key);
			
			String oaField="";
			String field_map=" select Field2 from uf_WorkflowMapTab_dt1 where mainid in (" +
							 " select id from uf_WorkflowMapTab where substr(workflowid,instr(workflowid,'_',-1)+1)="+WrokflowID+") and Field1='"+key+"' ";			
			rsx.execute(field_map);
			log.writeLog("Field get:"+field_map);
			if(rsx.next()){
				 oaField = Util.null2String(rsx.getString("Field2"));
				 
				tmp_field.append(oaField);
				tmp_field.append("=");
				tmp_field.append("'");tmp_field.append(value);tmp_field.append("'");			
				tmp_field.append(",");
			}
		}
		if(tmp_field.length()>0){
			tmp_field.deleteCharAt(tmp_field.length()-1);
		}else{
			return 2;
		}
		
		//流程数据更新
		rs.execute(" update "+tableName+" set "+tmp_field+" where requestid ="+requestid);
		
		return 0;
	}	

	/**
	 * msg传出
	 */
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return json.toString();
	}
	
	

}
