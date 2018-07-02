package gvo.ecservice;

import gvo.ecservice.CreateRequestServiceECStub.CreateECV0006Service;
import gvo.ecservice.CreateRequestServiceECStub.CreateECV0006ServiceResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateECV0006Action implements Action{

	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		//log.writeLog("开始测试CreateECV0006Action");
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String txr="";//填写人
		String sqrq="";//填写日期
		String sqr="";//申请人
		String szgs="";//所在公司
		String fycdbm="";//费用承担部门
		String fyys="";//费用预算
		//明细2
		String fyssqj="";//费用所属期间
		String fycd="";//费用承担
		String yskm="";//预算科目
		String ydkyys="";//月度可用预算
		String ndkyys="";//年度可用预算
		String jtsy="";//具体事由
		String yssqje="";//预算申请金额
		String oamxid="";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,txr,sqrq,sqr,szgs,fycdbm,fyys from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			txr = getcode(Util.null2String(rs.getString("txr")),"1");
			sqrq =  Util.null2String(rs.getString("sqrq"));
			sqr = getcode(Util.null2String(rs.getString("sqr")),"1");
			szgs = getcode(Util.null2String(rs.getString("szgs")),"3");
			fycdbm = getcode(Util.null2String(rs.getString("fycdbm")),"2");
			fyys  = Util.null2String(rs.getString("fyys"));
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
			 header.put("fycdbm", fycdbm);
			 header.put("fyys", fyys);
			 header.put("oarqid", requestid);
			 JSONArray dt11 = new JSONArray();
			 sql="select * from "+tableName+"_dt2 where mainid="+mainID;
			 rs.executeSql(sql);
			 while(rs.next()){
				 JSONObject node = new JSONObject();
				  fyssqj=Util.null2String(rs.getString("fyssqj"));
				  fycd=Util.null2String(rs.getString("fycd"));
				  yskm=Util.null2String(rs.getString("yskm"));
				  ydkyys=Util.null2String(rs.getString("ydkyys"));
				  ndkyys=Util.null2String(rs.getString("ndkyys"));
				  jtsy=Util.null2String(rs.getString("jtsy"));
				  yssqje=Util.null2String(rs.getString("yssqje"));
				  oamxid =Util.null2String(rs.getString("id"));
				  node.put("fyssqj", fyssqj);
				  node.put("fycd", fycd);
				  node.put("yskm", yskm);
				  node.put("ydkyys", ydkyys);
				  node.put("ndkyys", ndkyys);
				  node.put("jtsy", jtsy);
				  node.put("yssqje", yssqje);
				  node.put("oamxid", oamxid);
				  dt11.put(node);
				 
			 }
			 details.put("DT1", dt11);
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
			sql="update "+ tableName +" set ecrqid2='"+ecrqid+"' where requestid="+requestid;
			rs.executeSql(sql);
		 }catch (Exception e) {
			log.writeLog("调用webservice失败 requestid:"+requestid+" error:"+e.getMessage());
		  }
		return SUCCESS;
	}
	
	
	 public String doService(String workcode,String json) throws Exception{
		 
		 CreateRequestServiceECStub crsec= new CreateRequestServiceECStub();
		 CreateECV0006Service crs = new CreateRequestServiceECStub.CreateECV0006Service();
		 crs.setWorkcode(workcode);
		 crs.setDataInfo(json);
		 CreateECV0006ServiceResponse cr= crsec.CreateECV0006Service(crs);
		 return cr.getOut();
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
	
}
