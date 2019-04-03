package gvo.webservice;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.axis.encoding.Base64;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.attachments.Attachment;
import org.json.JSONException;
import org.json.JSONObject;

import sun.misc.BASE64Decoder;
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

public class CreateDCPRequestServiceImpl {
	
	public String CreateDCPRequest(String jsonStr,MessageContext ctx) throws Exception{
		BaseBean log = new BaseBean();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		RecordSet rs = new RecordSet();
		String result = "";
		String creater = "";
		String sql = "";
		String workflowid = "";
		String tablename = "";
		String attachName = "";
		JSONObject jObject = new JSONObject(jsonStr);
		String workcode = jObject.getString("Owner");
		String DCPID = jObject.getString("DCPID");
	    attachName = jObject.getString("attachName");
		String requestid = "";
		String docCategory = "";
		Map<String, String> retMap = new HashMap<String, String>();
		if("".equals(workcode)) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");	
			return getJsonStr(retMap);
		}
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			creater = Util.null2String(rs.getString("id"));
		}
		if("".equals(creater)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");	
			return getJsonStr(retMap);
		}
		sql="select lclxid,bm from uf_dcp_workflow_map where bs='DCPYS'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("lclxid"));
			tablename = Util.null2String(rs.getString("bm"));
		}
		if("".equals(workflowid)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "流程类型无法匹配");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		sql = "select a.requestid from " + tablename
				+ " a,workflow_requestbase b where a.requestid=b.requestid and a.DCPID='" + DCPID + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			requestid = Util.null2String(rs.getString("requestid"));
		}
		if(!"".equals(requestid)) {
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "该DCPID已经创建流程");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		sql = " select DOCCATEGORY from WORKFLOW_BASE where ID=" + workflowid;
        rs.executeSql(sql);
        if (rs.next()) {
            String docCategories = Util.null2String(rs.getString("DOCCATEGORY"));
            String dcg[] = docCategories.split(",");
            docCategory = dcg[dcg.length-1];
        }
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		header.put("PjtID", jObject.getString("PjtID"));
		header.put("PjtName", jObject.getString("PjtName"));
		header.put("Stage", jObject.getString("Stage"));
		String PjtType = jObject.getString("PjtType");
		if("技术开发".equals(PjtType)) {
			header.put("PjtType", "0");
		}else if("产品开发".equals(PjtType)) {
			header.put("PjtType", "1");
		}
		
		header.put("DCPID", jObject.getString("DCPID"));
		header.put("DCPName", jObject.getString("DCPName"));
		header.put("sqr", creater);
		header.put("sqrq", now);
		header.put("lcsxrq", jObject.getString("Date"));
		AutoRequestService ars = new AutoRequestService();
		result = ars.createRequest(workflowid, json.toString(), creater, "1");
		String docids = "";
		String flag = "";
		JSONObject resultobj = new JSONObject(result);
		if("S".equals(resultobj.getString("MSG_TYPE"))) {
			if(ctx.getInMessage().getAttachments() == null) {
				return result;
			}
			Iterator it = ctx.getInMessage().getAttachments().getParts();
			//log.writeLog("attachmentsize111:"+ctx.getInMessage().getAttachments().size());
			while(it.hasNext()){
				Attachment attachment = (Attachment) it.next();
				DataHandler dh=attachment.getDataHandler();
				//log.writeLog("attachmentid11:"+attachment.getId());
				try {
					String docid = createDoc(creater,docCategory,attachment,attachName);
					if(!"".equals(docid)) {
						docids = docids+flag+docid;
						flag = ",";
					}
				}catch(Exception e) {
					log.writeLog(e);
					log.writeLog("文档创建失败");
				}
			}
			sql = "update "+tablename+" set xgfj='"+docids+"' where requestid="+resultobj.getString("OA_ID");
			rs.executeSql(sql);
		}
		return result;
	}
	public String createDoc(String createrid,String seccategory,Attachment attachment,String attachName) throws Exception {
		BaseBean log = new BaseBean();
		String docid = "";
		DataHandler dh=attachment.getDataHandler();
		//String docname = Util.null2String(dh.getName());
		if("".equals(attachName)) {
			//docname = Util.null2String(attachment.getId());
			//if("".equals(docname)) {
			//	log.writeLog("文档名获取异常");
			//	return "";
			//}
			log.writeLog("文档名获取异常");
			return "";
		}
		
		String uploadBuffer = "";
        InputStream fi = dh.getInputStream();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = fi.read(buffer)) >= 0) {
            baos.write(buffer, 0, count);
        }
        uploadBuffer = Base64.encode(baos.toByteArray());
        docid = getDocId(attachName,uploadBuffer,createrid,seccategory);
		return docid;
	}
	
	private String getDocId(String name, String value, String createrid, String seccategory) throws Exception {
        String docId = "";
        DocInfo di = new DocInfo();
        di.setMaincategory(0);
        di.setSubcategory(0);
        di.setSeccategory(Util.getIntValue(seccategory));
        di.setDocSubject(name.substring(0, name.lastIndexOf(".")));
        DocAttachment doca = new DocAttachment();
        doca.setFilename(name);
        byte[] buffer = new BASE64Decoder().decodeBuffer(value);
        String encode = Base64.encode(buffer);
        doca.setFilecontent(encode);
        DocAttachment[] docs = new DocAttachment[1];
        docs[0] = doca;
        di.setAttachments(docs);
        String departmentId = "-1";
        String sql = "select departmentid from hrmresource where id=" + createrid;
        RecordSet rs = new RecordSet();
        rs.executeSql(sql);
        User user = new User();
        if (rs.next()) {
            departmentId = Util.null2String(rs.getString("departmentid"));
        }
        user.setUid(Util.getIntValue(createrid));
        user.setUserDepartment(Util.getIntValue(departmentId));
        user.setLanguage(7);
        user.setLogintype("1");
        user.setLoginip("127.0.0.1");
        DocServiceImpl ds = new DocServiceImpl();
        try {
            docId = String.valueOf(ds.createDocByUser(di, user));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return docId;
    }
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
