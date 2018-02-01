package feilida.finance;

import feilida.util.WebApi;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CancelFYBXAction implements Action {
    BaseBean log = new BaseBean();
	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String workflow_id = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String OADATE = "";
		String KOSTL = "";
		String KSTAR = "";
		String GSBER = "";
		String ANLKL = "";
		String CURRENCY = "";
		String AMOUNT = "";
		String EXECTYPE = "";
		String OPTTYPE = "2";
		String OAKey = "";
		String STAFFID = "";
		String COMPID = "";
		String DEPTID = "";
		String GPKEY = "";
		// String xingzhi = "";
		String sql = "Select tablename,id From Workflow_bill Where id=(";
		sql += "Select formid From workflow_base Where id=" + workflow_id + ")";
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		String mainId = "";
		sql = "select * from " + tableName + " where requestid= " + requestid;
		rs.executeSql(sql);
		log.writeLog("updateBudget"+sql);
		if (rs.next()) {
			mainId = Util.null2String(rs.getString("id"));
			OADATE = Util.null2String(rs.getString("BXRQ"));
			
			DEPTID = Util.null2String(rs.getString("BXRBM"));
			STAFFID = Util.null2String(rs.getString("BXRXM"));
			EXECTYPE = Util.null2String(rs.getString("LX"));
			
		}
		if("1".equals(EXECTYPE)){
			return SUCCESS;
		}
		sql="select subcompanyid1 from hrmresource where id="+STAFFID;
		rs.executeSql(sql);
		if(rs.next()){

			COMPID = Util.null2String(rs.getString("subcompanyid1"));
		}
		OAKey = requestid;
		List<Map<String,String>> listMap = new ArrayList<Map<String,String>>();
		sql = "select * from " + tableName + "_dt1 where mainid=" + mainId;
		rs.executeSql(sql);
	    int i=0;
		while (rs.next()) {
			Map<String,String> mapStr = new HashMap<String, String>();
			GSBER = Util.null2String(rs.getString("YWFW2"));
			if(!"1".equals(EXECTYPE)){
				KSTAR = Util.null2String(rs.getString("ZZKM1"));
				ANLKL = Util.null2String(rs.getString("ZZKM1"));
			}else{
				KSTAR = Util.null2String(rs.getString("KMID"));
				ANLKL = Util.null2String(rs.getString("KMID"));
			}
			AMOUNT = Util.null2String(rs.getString("JE2"));
			GPKEY = Util.null2String(rs.getString("BSM"));
			
			KOSTL = Util.null2String(rs.getString("CBZX"));
			if("".equals(AMOUNT)){
				AMOUNT = "0";
			}
			if("".equals(COMPID)){
				COMPID = "1";
			}
			if("".equals(DEPTID)){
				DEPTID = "1";
			}
			if("".equals(STAFFID)){
				STAFFID = "1";
			}
			log.writeLog("sss"+i+":"+" AMOUNT:"+AMOUNT+" KSTAR:"+KSTAR+" OADATE:"+OADATE+" KOSTL:"+KOSTL+" GSBER:"+GSBER+" OPTTYPE:"+OPTTYPE+" STAFFID:"+STAFFID+" COMPID:"+COMPID+" DEPTID:"+DEPTID+" EXECTYPE:"+EXECTYPE+" ANLKL:"+ANLKL+" OAKey:"+OAKey+" GPKEY:"+GPKEY);
			
			mapStr.put("OADATE", OADATE);
			mapStr.put("KOSTL", KOSTL);
			mapStr.put("KSTAR", KSTAR);
			mapStr.put("GSBER", GSBER);
			mapStr.put("ANLKL", ANLKL);
			mapStr.put("CURRENCY", CURRENCY);
			mapStr.put("AMOUNT", AMOUNT);
			mapStr.put("EXECTYPE", EXECTYPE);
			mapStr.put("OPTTYPE", OPTTYPE);
			mapStr.put("OAKey", OAKey);
			mapStr.put("STAFFID", STAFFID);
			mapStr.put("COMPID", COMPID);
			mapStr.put("DEPTID", DEPTID);
			listMap.add(mapStr);
			i++;
		}
		WebApi wa = new WebApi();
		String param = wa.getJsonStr("VOList", listMap);
		String result = wa.getPostConn("BudgetVOCode", param);
		log.writeLog("result = " + result);
		return SUCCESS;
	}

}
