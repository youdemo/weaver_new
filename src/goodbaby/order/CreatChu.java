package goodbaby.order;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;

/**
 * 
 * @author 张瑞坤 供应商订单出货 180720
 */
public class CreatChu {
	public String creatChu(String rids, String sls) {
		BaseBean log = new BaseBean();
		String workflowid = "213";//c 213 z 291
		RecordSet rs = new RecordSet();
		String rid[] = rids.split(",");
		String sl[] = sls.split(",");
		//log.writeLog("rids---" + rids + "---sls---" + sls);
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		String date = format.format(new Date());
		String gys = "";
		String results = "";
		String xtkh = "";
		for (int i = 0; i < rid.length; i++) {
			JSONObject json = new JSONObject();
			JSONObject header = new JSONObject();
			JSONObject details = new JSONObject();
			JSONArray dt1 = new JSONArray();
			try {
				header.put("SHRQ", date);// SHDW 收货地址 CBZX 成本中心 SHCK 收货仓库 SHR
											// 收货人 xtkh 系统客户
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String sql = "select a.SHDW,a.CBZX,a.SHCK,a.SHR,a.xtkh, a.CGSL,a.DDBH,a.WLMC,a.DJ,a.GYSMC,a.WLBM,a.zjlbm  from "
					+ " formtable_main_240 a where requestid ='"
					+ rid[i]
					+ "' ";//z 234  c 240
			rs.executeSql(sql);
			if (rs.next()) {
				gys = Util.null2String(rs.getString("GYSMC"));
				xtkh = Util.null2String(rs.getString("xtkh"));
				JSONObject node = new JSONObject();
				try {
					header.put("SHDW", Util.null2String(rs.getString("SHDW")));
					header.put("CBZX", Util.null2String(rs.getString("CBZX")));
					header.put("SHCK", Util.null2String(rs.getString("SHCK")));
					header.put("SHR", Util.null2String(rs.getString("SHR")));
					header.put("xtgys", Util.null2String(rs.getString("xtkh")));
					header.put("zjlbm", Util.null2String(rs.getString("zjlbm")));//总经理部门
					node.put("CGSL_1", Util.null2String(rs.getString("CGSL")));
					node.put("WLMC_1", Util.null2String(rs.getString("WLMC")));
					node.put("DJ_1", Util.null2String(rs.getString("DJ")));
					node.put("WLBH_1", Util.null2String(rs.getString("WLBM")));
					node.put("gys", Util.null2String(rs.getString("GYSMC")));
					node.put("cgsqd", rid[i]);
					node.put("SJSHSL_1", sl[i]);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dt1.put(node);
			}
			try {
				details.put("DT1", dt1);
				json.put("HEADER", header);
				json.put("DETAILS", details);
				results = creat(gys,xtkh,workflowid, json.toString());
			} catch (JSONException e) {
				log.writeLog("json解析异常 ----" + json.toString());

			}

		}

		return results;
	}

	public String creat(String gys,String xtkh, String workflowid, String json) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String str = "select c.id from crm_customerinfo c where c.portalloginid = "
				+ "(select u.GYSBM from uf_suppmessForm u where u.id ='"
				+ gys
				+ "') ";
		//log.writeLog("str--------------"+str);
		AutoRequestService as = new AutoRequestService();
		log.writeLog("doServer start json:" + json);
		String result = as.createRequest(workflowid, json, xtkh, "1");
		log.writeLog("doServer end result:" + result);
		String requestnamenew = "";
		String requestname = "";
		String hrname = "";
		String crname = "";
		try {
			JSONObject json1 = new JSONObject(result);
			String oaid = json1.getString("OA_ID");
			if (Integer.valueOf(oaid) > 0) {
				String sql = "select requestname ,requestnamenew from workflow_requestbase where requestid = '"+oaid+"'";
				rs.executeSql(sql);
				if(rs.next()){
					requestname = rs.getString("requestname");
					requestnamenew = rs.getString("requestnamenew");
				}
				sql = "select lastname from hrmresource where id = '"+xtkh+"'";
				rs.executeSql(sql);
				if(rs.next()){
					hrname = Util.null2String(rs.getString("lastname"));
				}
				sql = "select name from crm_customerinfo where id = '"+xtkh+"'";
				rs.executeSql(sql);
				if(rs.next()){
					crname = Util.null2String(rs.getString("name"));
				}
				log.writeLog("hrname---"+hrname+"----crname---"+crname);
				requestname = requestname.replace(hrname, crname);
				requestnamenew = requestnamenew.replace(hrname, crname);				
				String stt = "update workflow_requestbase set requestname = '"+requestname+"',requestnamenew = '"+requestnamenew+"',creatertype='1' where requestid='"+oaid+"' ";
				rs.executeSql(stt);
				log.writeLog(stt);
				stt = "update workflow_requestlog set operatortype = '1' where requestid = '"+oaid+"' and nodeid ='2218'";//z 2218---c1412
				log.writeLog(stt);
				rs.executeSql(stt);
				return "出货中。。。";
			} else {
				return "出货失败---" + json1.getString("MSG_CONTENT");
			}
		} catch (Exception e) {
			log.writeLog("json1解析异常");
		}
		return result;

	}
}
