package gvo.costcontrol.reimbursement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class Test {
	public void testaa(){
		RecordSet rs = new RecordSet();
		String cgddhs = Util.null2String(request.getParameter("cgddlcs"));
		String ysssqj = Util.null2String(request.getParameter("ysssqj"));
		String fycdbm = Util.null2String(request.getParameter("fycdbm"));
		String yskm = Util.null2String(request.getParameter("yskm"));
		if("".equals(cgddhs)||"".equals(ysssqj)||"".equals(fycdbm)||"".equals(yskm)){
			out.print("");
		}
		StringBuffer dataBuff = new StringBuffer();
		String tableName = "";
		String tableName2 = "";
		String podh = "";
		String yfxmbhs = "";//研发项目编号
		String costcenters = "";//成本中心
		String fykmbms = "";//费用科目编码
		String flag = "";
		String  sql="select tablename from workflow_bill where id in (select formid from workflow_base where id = (select distinct workflowid from workflow_requestbase where requestid in("+cgddhs+")))";
		rs.executeSql(sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		List<Map<String, String>> list = new ArrayList<Map<String, String>>();
		sql="select a.orderNo||'-'||b.project as dh,b.purrequest,b.reqproject,b.costcenter,b.fykmbm from "+tableName+" a,"+tableName+"_dt1 b where a.id=b.mainid and a.requestid in("+cgddhs+") and substr(b.fyqj,0,7)='"+ysssqj.substring(0,7)+"' and fykm='"+yskm+"' and fycdbm='"+fycdbm+"'";
		rs.executeSql(sql);
		while(rs.next()){
			podh = podh + flag +Util.null2String(rs.getString("dh"));
			flag=",";
			String costcenter = Util.null2String(rs.getString("costcenter"));
			String fykmbm = Util.null2String(rs.getString("fykmbm"));
			if(!"".equals(costcenter)){
				if("".equals(costcenters)){
					costcenters = costcenter; 
				}else{
					if((","+costcenters+",").indexOf(","+costcenter+",")<0){
						costcenters = costcenters+","+costcenter; 
					}
				}
			}
			
			if(!"".equals(fykmbm)){
				if("".equals(fykmbms)){
					fykmbms = fykmbm; 
				}else{
					if((","+fykmbms+",").indexOf(","+fykmbm+",")<0){
						fykmbms = fykmbms+","+fykmbm; 
					}
				}
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("purrequest", Util.null2String(rs.getString("purrequest")));
			map.put("reqproject", Util.null2String(rs.getString("reqproject")));
			list.add(map);
		}
		
		for(Map<String,String> map:list){
			String purrequest = map.get("purrequest");
			String reqproject = map.get("reqproject");
			String lcid = "";
			sql="select lcid from uf_pr_budget where cgsqdh='"+purrequest+"' and mxhid='"+reqproject+"' order by id asc";
			rs.executeSql(sql);
			if(rs.next()){
				lcid = Util.null2String(rs.getString("lcid"));
			}
			if(!"".equals(lcid)){
				sql="select tablename from workflow_bill where id in (select formid from workflow_base where id = (select distinct workflowid from workflow_requestbase where requestid in("+lcid+")))";
				rs.executeSql(sql);
				if(rs.next()){
					tableName2 = Util.null2String(rs.getString("tablename"));
				}
				sql="select distinct b.yfxmbh from "+tableName2+" a,"+tableName2+"_dt1 b where a.id=b.mainid and a.requestid="+lcid;
				rs.executeSql(sql);
				while(rs.next()){
					String yfxmbh = Util.null2String(rs.getString("yfxmbh"));
					if(!"".equals(yfxmbh)){
						if("".equals(yfxmbhs)){
							yfxmbhs = yfxmbh; 
						}else{
							if((","+yfxmbhs+",").indexOf(","+yfxmbh+",")<0){
								yfxmbhs = yfxmbhs+","+yfxmbh; 
							}
						}
					}
					
					
					
				}
			}
		}
		
	}
}
