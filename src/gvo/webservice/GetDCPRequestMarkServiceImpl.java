package gvo.webservice;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sun.util.logging.resources.logging;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class GetDCPRequestMarkServiceImpl {
	public String getRequestMark(String DCPID) {
		Map<String, String> retMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String result = "";
		String sql = "";
		String tablename = "";
		String requestid = "";
		JSONArray arr = new JSONArray();
		sql = "select bm from uf_dcp_workflow_map where bs='DCPYS'";
		rs.executeSql(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("bm"));
		}
		if ("".equals(tablename)) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "流程表单无法匹配");
			return getJsonStr(retMap, arr);
		}
		sql = "select a.requestid from " + tablename
				+ " a,workflow_requestbase b where a.requestid=b.requestid and a.DCPID='" + DCPID + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			requestid = Util.null2String(rs.getString("requestid"));
		}
		if ("".equals(requestid)) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "传入的DCPID匹配不到有效的流程");
			return getJsonStr(retMap, arr);
		}
		
		try {
			arr=getRemarkJson(requestid,tablename);
		} catch (Exception e) {
			log.writeLog("GetDCPRequestMarkServiceImpl");
			log.writeLog(e);
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "凭借审批意见出错");
			return getJsonStr(retMap, arr);
		}
		retMap.put("MSG_TYPE", "S");
		retMap.put("MSG_CONTENT", "");
		return getJsonStr(retMap, arr);
	}

	/**
	 * 获取返回值json串
	 * 
	 * @param map 返回结果map
	 * @param arr 意见json数组
	 * @return
	 */
	private String getJsonStr(Map<String, String> map, JSONArray arr) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		try {
			while (it.hasNext()) {
				String key = it.next();
				String value = map.get(key);
				json.put(key, value);
			}
			json.put("JSONSTR", arr);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return json.toString();
	}

	private JSONArray getRemarkJson(String requestId, String tableName) throws Exception {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		JSONArray arr = new JSONArray();
		String sql = "";
		String sql_dt = "";
		String owner = "";//流程发起人
		String requestname = "";//流程名称
		String pjtid = "";//项目ID
		String PjtName = "";//项目名称
		String PjtType = "";//项目类型
		String dcpid = "";//DCPID
		String dcpname = "";//DCP名称
		String stage = "";//项目所处阶段
		String operator = "";//审批人
		String remark = "";//审批意见
		String time = "";//审批时间
		String logtype = "";//审批类型
		String annexdocids = "";//审批附件
		String oaaddress = "";
		if ("".equals(requestId)) {
			return arr;
		}
		sql = "select oaaddress from uf_doc_oaadress where rownum=1";
		rs.execute(sql);
		if(rs.next()) {
			oaaddress = Util.null2String(rs.getString("oaaddress"));
		}
		JSONArray array = new JSONArray();
		String sql_remark = "select (select workcode from hrmresource where id=c.sqr) as owner,d.requestname,c.pjtid,c.PjtName,(select selectname from workflow_billfield t, workflow_bill t1,workflow_selectitem t2 where t.billid=t1.id and t2.fieldid=t.id  and t1.tablename='formtable_main_1052' and t.fieldname='PjtType' and t2.selectvalue=c.PjtType) as PjtType,c.dcpid,c.dcpname,c.stage,(select workcode from hrmresource where id=a.operator) as operator,a.remark,a.operatedate||' '||a.operatetime as time,a.logtype,a.annexdocids "
				+ " from workflow_requestlog a, workflow_nodebase b ," + tableName
				+ " c ,workflow_requestbase d where a.nodeid=b.id  and a.requestid=c.requestid and a.requestid=d.requestid and b.isstart=0 and b.isend = 0 and  a.requestid="
				+ requestId;
		// log.writeLog("testsql:"+sql_remark);
		rs.executeSql(sql_remark);
		while (rs.next()) {
			JSONObject jo = new JSONObject();
			owner = Util.null2String(rs.getString("owner"));
			requestname = Util.null2String(rs.getString("requestname"));
			pjtid = Util.null2String(rs.getString("pjtid"));
			PjtName = Util.null2String(rs.getString("PjtName"));
			PjtType = Util.null2String(rs.getString("PjtType"));
			dcpid = Util.null2String(rs.getString("dcpid"));
			dcpname = Util.null2String(rs.getString("dcpname"));
			stage = Util.null2String(rs.getString("stage"));
			operator = Util.null2String(rs.getString("operator"));						
			remark = removeHtmlTag(Util.null2String(rs.getString("remark")));
			time = Util.null2String(rs.getString("time"));			
			logtype = getState(Util.null2String(rs.getString("logtype")), 7);
			annexdocids = Util.null2String(rs.getString("annexdocids"));
			
			jo.put("Owner", owner);
			jo.put("WFName", requestname);
			jo.put("PjtID", pjtid);
			jo.put("PjtName", PjtName);
			jo.put("PjtType", PjtType);
			jo.put("DCPID", dcpid);
			jo.put("DCPName", dcpname);
			jo.put("Stage", stage);
			jo.put("Reviewer", operator);
			jo.put("Idea", remark);
			jo.put("AppTime", time);
			jo.put("LOGTYPE", logtype);
			JSONArray attArr = new JSONArray();
			String[] docIdArr = annexdocids.split(",");
			for(String docid : docIdArr) {
				if("".equals(docid)) {
					continue;
				}
				JSONObject job = new JSONObject();
				sql_dt = " select imagefilename,'"+oaaddress+"/gvo/dcp/downloaddoc.jsp?fileid='||imagefileid as fileurl from docimagefile where docid='"+docid+"' order by id desc ";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()) {
					job.put("attachName", Util.null2String(rs_dt.getString("imagefilename")));
					job.put("attachUrl", Util.null2String(rs_dt.getString("fileurl")));
					attArr.put(job);
				}
				
			}
			jo.put("attach", attArr);

			array.put(jo);
		}

		// log.writeLog("test:"+array.toString());
		return array;
	}

	/**
	 * 获取流程操作类型
	 * 
	 * @param state
	 * @param saplan
	 * @return
	 */
	private String getState(String state, int saplan) {
		String statename = "";
		if (saplan == 8) {
			if ("0".equals(state)) {
				statename = "Approval";
			} else if ("2".equals(state)) {
				statename = "Submit";
			} else if ("3".equals(state)) {
				statename = "Return";
			} else if ("4".equals(state)) {
				statename = "Reopen";
			} else if ("5".equals(state)) {
				statename = "Delete";
			} else if ("6".equals(state)) {
				statename = "Activation";
			} else if ("7".equals(state)) {
				statename = "Retransmission";
			} else if ("9".equals(state)) {
				statename = "Comment";
			} else if ("a".equals(state)) {
				statename = "Opinion inquiry";
			} else if ("b".equals(state)) {
				statename = "Opinion inquiry reply";
			} else if ("e".equals(state)) {
				statename = "filing";
			} else if ("h".equals(state)) {
				statename = "Transfer";
			} else if ("i".equals(state)) {
				statename = "intervene";
			} else if ("j".equals(state)) {
				statename = "Transfer feedback";
			} else if ("t".equals(state)) {
				statename = "CC";
			} else if ("s".equals(state)) {
				statename = "Supervise";
			} else if ("i".equals(state)) {
				statename = "Process intervention";
			} else if ("1".equals(state)) {
				statename = "Save";
			} else if ("new".equals(state)) {
				statename = "Create";
			} else {
				statename = "";
			}
		} else {
			if ("0".equals(state)) {
				statename = "批准";
			} else if ("2".equals(state)) {
				statename = "提交";
			} else if ("3".equals(state)) {
				statename = "退回";
			} else if ("4".equals(state)) {
				statename = "重新打开";
			} else if ("5".equals(state)) {
				statename = "删除";
			} else if ("6".equals(state)) {
				statename = "激活";
			} else if ("7".equals(state)) {
				statename = "转发";
			} else if ("9".equals(state)) {
				statename = "批注";
			} else if ("a".equals(state)) {
				statename = "意见征询";
			} else if ("b".equals(state)) {
				statename = "意见征询回复";
			} else if ("e".equals(state)) {
				statename = "归档";
			} else if ("h".equals(state)) {
				statename = "转办";
			} else if ("i".equals(state)) {
				statename = "干预";
			} else if ("j".equals(state)) {
				statename = "转办反馈";
			} else if ("t".equals(state)) {
				statename = "抄送";
			} else if ("s".equals(state)) {
				statename = "督办";
			} else if ("i".equals(state)) {
				statename = "流程干预";
			} else if ("1".equals(state)) {
				statename = "保存";
			} else if ("new".equals(state)) {
				statename = "创建";
			} else {
				statename = "";
			}
		}
		return statename;
	}

	private String removeHtmlTag(String content) {
		Pattern p = Pattern.compile("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>");
		Matcher m = p.matcher(content);
		if (m.find()) {
			content = content.replaceAll("<([a-zA-Z]+)[^<>]*>(.*?)</\\1>", "$2");
			content = removeHtmlTag(content);
		}
		return content;
	}
}
