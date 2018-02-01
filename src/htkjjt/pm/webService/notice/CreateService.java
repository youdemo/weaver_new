package htkjjt.pm.webService.notice;


import java.util.ArrayList;
import java.util.List;

import htkjjt.pm.util.AutoRequestService;
import htkjjt.pm.util.ReturnInfo2;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;

public class CreateService {
	public void doServerForAfter(int seqno) {
		List<ReturnInfo2> InfoList=new ArrayList<ReturnInfo2>();
		BaseBean log = new BaseBean();
		log.writeLog("doServerForAfter start");
		RecordSet rs= new RecordSet();
		RecordSet rs_dt= new RecordSet();
		String tablename = "uf_nowday_remind";
        String creater="1";
        String workflowId = "343";
		String AUFNR = "";
		String WAWRK = "";
		String VAPLZ = "";
		String WERKS = "";
		String PAK_TEXT = "";
		String INGPR = "";
		String EQUNR = "";
		String EQFNR = "";
		String MSGRP = "";
		String TYPBZ = "";
		String EQKTX = "";
		String KTEXT = "";
		String INNAM = "";
		String GSTRP = "";
		String bzcy="";
		String flag = "";
		String personId = "";
		String sql_dt="";
		JSONObject json = null;
		String sql="select vaplz,wawrk,max(innam) as innam,max(INGPR) as INGPR,max(KTEXT) as KTEXT  from uf_nowday_remind  where seqno="+seqno+" and sfcfwc='1' group by vaplz,wawrk";
		rs.executeSql(sql);
		while(rs.next()){
			VAPLZ = Util.null2String(rs.getString("vaplz"));
			WAWRK = Util.null2String(rs.getString("wawrk"));
			INNAM = Util.null2String(rs.getString("innam"));
			KTEXT = Util.null2String(rs.getString("KTEXT"));
			INGPR = Util.null2String(rs.getString("INGPR"));
			bzcy = "";
			flag = "";
			if("1000".equals(WAWRK)){
				sql_dt="select b.id from uf_tswhbz a,hrmresource b where a.gh=b.workcode and b.status in (0,1,2,3) and a.whbz='"+VAPLZ+"'";
				rs_dt.execute(sql_dt);
				while(rs_dt.next()){
					personId = Util.null2String(rs_dt.getString("id"));
					bzcy =  bzcy+flag+personId;
					flag=",";
				}
			}else if("1010".equals(WAWRK)){
				sql_dt="select b.id from uf_xawhbz a,hrmresource b where a.gh=b.workcode and b.status in (0,1,2,3) and a.whbz='"+VAPLZ+"'";
				rs_dt.execute(sql_dt);
				while(rs_dt.next()){
					personId = Util.null2String(rs_dt.getString("id"));
					bzcy =  bzcy+flag+personId;
					flag=",";
				}
			}else if("1001".equals(WAWRK)){
				sql_dt="select b.id from uf_ledwhbz a,hrmresource b where a.gh=b.workcode and b.status in (0,1,2,3) and a.whbz='"+VAPLZ+"'";
				rs_dt.execute(sql_dt);
				while(rs_dt.next()){
					personId = Util.null2String(rs_dt.getString("id"));
					bzcy =  bzcy+flag+personId;
					flag=",";
				}
			}
			try{
			json = new JSONObject();
			JSONObject header = new JSONObject();
			JSONObject details = new JSONObject();
			header.put("lcfqr", creater);//流程发起人
			header.put("gc", WAWRK);//工厂
			header.put("bm", INGPR);//部门
			header.put("bmms", INNAM);//部门描述
			header.put("whbz", VAPLZ);//维护班组
			header.put("KTEXT", KTEXT);//维护班组名称
			header.put("bzcy", bzcy);
			header.put("seqno", seqno);
			JSONArray dt1 = new JSONArray();
			sql_dt = "select * from uf_nowday_remind where seqno="+seqno+" and" +
					" sfcfwc='1' and wawrk='"+WAWRK+"' and vaplz='"+VAPLZ+"'";
			rs_dt.executeSql(sql_dt);
			while(rs_dt.next()){
				JSONObject node = new JSONObject();
				node.put("ddbh", Util.null2String(rs_dt.getString("AUFNR")));//订单编号
				//node.put("WERKS", Util.null2String(rs_dt.getString("WERKS")));
				node.put("zqwb", Util.null2String(rs_dt.getString("PAK_TEXT")));//周期文本
				//node.put("INGPR", Util.null2String(rs_dt.getString("INGPR")));
				node.put("sbh", Util.null2String(rs_dt.getString("EQUNR")));//设备号
				node.put("gsbm", Util.null2String(rs_dt.getString("EQFNR")));//公司编码
				node.put("cfwz", Util.null2String(rs_dt.getString("MSGRP")));//存放位置
				node.put("sbxh", Util.null2String(rs_dt.getString("TYPBZ")));//型号
				node.put("sbms", Util.null2String(rs_dt.getString("EQKTX")));//设备描述
				//node.put("KTEXT", Util.null2String(rs_dt.getString("KTEXT")));
				node.put("jhrq", Util.null2String(rs_dt.getString("GSTRP")));//计划日期
				dt1.put(node);
			}
			details.put("DT1", dt1);
			json.put("HEADER", header);
			json.put("DETAILS", details);
			}catch(Exception e){
				log.writeLog("json解析异常 seqno="+seqno+" and" +
					" sfcfwc='1' and wawrk='"+WAWRK+"' and vaplz='"+VAPLZ+"'");
				continue;
			}
			AutoRequestService ars = new AutoRequestService();
			log.writeLog("doServerForAfter start json:"+json.toString());
			String result = ars.createRequest(workflowId, json.toString(), creater, "0");
			log.writeLog("doServerForAfter end result:"+result);
			try{
				JSONObject json1 = new JSONObject(result);
				String oaid = json1.getString("OA_ID");
				if(Integer.valueOf(oaid)>0){
					AutoSubmit(oaid,creater);
					sql_dt="update uf_nowday_remind set sfcfwc='0',rqid='"+oaid+"' where seqno="+seqno+" and" +
						" sfcfwc='1' and wawrk='"+WAWRK+"' and vaplz='"+VAPLZ+"'";
					rs_dt.executeSql(sql_dt);
				}
			}catch(Exception e){
				log.writeLog("json1解析异常");
			}
		}
		
		
		
	}
	public String AutoSubmit(String requestid, String userid) {
		WorkflowServiceImpl ws = new WorkflowServiceImpl();
		WorkflowRequestInfo wri = new WorkflowRequestInfo();
		String result = ws.submitWorkflowRequest(wri,
				Integer.valueOf(requestid), Integer.valueOf(userid), "submit",
				" ");
		return result;
	}
}
