package gvo.ecservice;


import gvo.ecservice.CreateRequestServiceECStub.CreateBrrowService;
import gvo.ecservice.CreateRequestServiceECStub.CreateBrrowServiceResponse;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateECBrrowAction implements Action{

	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		// TODO Auto-generated method stub
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String txr="";//填写人
		String sqrq="";//填写日期
		String sqr="";//申请人
		String szgs="";//所在公司
		String szbm="";//所在部门
		String jkje="";//借款金额
		String  sfxyjk="";//是否需要借款
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,txr,sqrq,sqr,szgs,szbm,jkje,sfxyjk from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			txr = getcode(Util.null2String(rs.getString("txr")),"1");
			sqrq =  Util.null2String(rs.getString("sqrq"));
			sqr = getcode(Util.null2String(rs.getString("sqr")),"1");
			szgs = getcode(Util.null2String(rs.getString("szgs")),"3");
			szbm = getcode(Util.null2String(rs.getString("szbm")),"2");
			jkje  = Util.null2String(rs.getString("jkje"));
			sfxyjk = Util.null2String(rs.getString("sfxyjk"));
		}
		if("1".equals(sfxyjk)||"".equals(sfxyjk)){
			return SUCCESS;
		}
		 JSONObject json = new JSONObject();
	     JSONObject header = new JSONObject();
	 	 JSONObject details = new JSONObject();
	 	 try {
				json.put("DETAILS", details);
				 json.put("HEADER", header);
				 header.put("txr", txr);
				 header.put("sqrq", sqrq);
				 header.put("sqr", sqr);
				 header.put("szgs", szgs);
				 header.put("szbm", szbm);
				 header.put("jkje", jkje);
				 header.put("oarqid", requestid);
				 
	 	} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog("拼装json失败"); 
		}
	 	 try {
				String result=doService(txr,json.toString());
				log.writeLog("调用webservice结果 requestid="+requestid+" message:"+result);
				JSONObject json1 = new JSONObject(result);
				String ecrqid = json1.getString("OA_ID");
				sql="update "+ tableName +" set jkrqid='"+ecrqid+"' where requestid="+requestid;
				rs.executeSql(sql);
			 }catch (Exception e) {
				log.writeLog("调用webservice失败 requestid:"+requestid+" error:"+e.getMessage());
			  }
		return SUCCESS;
	}
	
	public String getcode(String id,String type){
		  RecordSet rs = new RecordSet();
		  String code="";
		  String sql="";
		  if("".equals(id)){
			  return "";
		  }
		  if("1".equals(type)){
		    sql="select workcode  as code from hrmresource where id="+id;
		  }else if("2".equals(type)){
			sql="select departmentcode as code from hrmdepartment where id="+id;	  
		  }else{
			  sql="select subcompanycode as code from hrmsubcompany where id="+id;	    
		  }
		  rs.executeSql(sql);
		  if(rs.next()){
			  code = Util.null2String(rs.getString("code"));
		  }
		  return code;
	  }
 public String doService(String workcode,String json) throws Exception{
		 
		 CreateRequestServiceECStub crsec= new CreateRequestServiceECStub();
		 CreateBrrowService crs = new CreateRequestServiceECStub.CreateBrrowService();
		 crs.setWorkcode(workcode);
		 crs.setDataInfo(json);
		 CreateBrrowServiceResponse cr= crsec.CreateBrrowService(crs);
		 return cr.getOut();
	 }
}
