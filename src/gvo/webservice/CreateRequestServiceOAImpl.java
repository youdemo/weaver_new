package gvo.webservice;

import org.apache.axis.encoding.Base64;
import org.codehaus.xfire.MessageContext;
import org.codehaus.xfire.attachments.Attachment;
import org.json.JSONArray;
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

import javax.activation.DataHandler;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CreateRequestServiceOAImpl {
    BaseBean log = new BaseBean();

    public String doserviceSupplier(String workcode, String jsonStr) {
        RecordSet rs = new RecordSet();
        String sqr = "";
        String sqrbm = "";
//		String workflowId = "9342";//正式机:8122
        String workflowId = "8122";
        String sql = "select id,departmentid from hrmresource where workcode='"
                + workcode + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            sqr = Util.null2o(rs.getString("id"));
            sqrbm = Util.null2o(rs.getString("departmentid"));
        } else {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配！");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        String json = "";
        try {
            json = getjsonSupplier(sqr, sqrbm, jsonStr);
        } catch (Exception e) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json格式不正确");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        AutoRequestService ars = new AutoRequestService();
        String result = ars.createRequest(workflowId, json, sqr, "0");
        return result;

    }

    public String doserviceMaterial(String workcode, String jsonStr) {
        RecordSet rs = new RecordSet();
        String sqr = "";
        String sqrbm = "";
//		String workflowId = "9341";//正式机:8161
        String workflowId = "8161";
        String sql = "select id,departmentid from hrmresource where workcode='"
                + workcode + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            sqr = Util.null2o(rs.getString("id"));
            sqrbm = Util.null2o(rs.getString("departmentid"));
        } else {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配！");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        String json = "";
        try {
            json = getjsonMaterial(sqr, sqrbm, jsonStr);
        } catch (Exception e) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json格式不正确");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        AutoRequestService ars = new AutoRequestService();
        String result = ars.createRequest(workflowId, json, sqr, "0");
        return result;

    }

    public String doserviceHR015(String workcode, String jsonStr) {
        RecordSet rs = new RecordSet();
        String sqr = "";
        String sqrbm = "";
        String workflowId = "8126";
        String sql = "select id,departmentid from hrmresource where workcode='"
                + workcode + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            sqr = Util.null2o(rs.getString("id"));
            sqrbm = Util.null2o(rs.getString("departmentid"));
        } else {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配！");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        String json = "";
        try {
            json = getjsonHR015(sqr, sqrbm, jsonStr);
        } catch (Exception e) {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json格式不正确");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        AutoRequestService ars = new AutoRequestService();
        String result = ars.createRequest(workflowId, json, sqr, "0");
        return result;

    }

    public String getjsonMaterial(String sqr, String sqrbm, String jsonStr)
            throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sf.format(new Date());
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        String BUKRS = "";//公司代码
        String BUTXT = "";//公司名称
        String LIFNR = "";//供应商
        String NAME1 = "";//供应商名称
        String MATNR = "";//物料编码
        String MAKTX = "";//物料名称
        String ZRECOGNIZE = "";//是否承认

        json.put("HEADER", header);
        json.put("DETAILS", details);
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        BUKRS = head.getString("BUKRS");
        BUTXT = head.getString("BUTXT");
        LIFNR = head.getString("LIFNR");
        NAME1 = head.getString("NAME1");
        MATNR = head.getString("MATNR");
        MAKTX = head.getString("MAKTX");
        ZRECOGNIZE = head.getString("ZRECOGNIZE");

        header.put("applicant", sqr);
        header.put("department", sqrbm);
        header.put("appDate", now);
        header.put("companyCode", BUKRS);
        header.put("companyName", BUTXT);
        header.put("suppliers", LIFNR);
        header.put("supNames", NAME1);
        header.put("sapMaterial", MATNR);
        header.put("materName", MAKTX);
        header.put("isSure", ZRECOGNIZE);

        return json.toString();

    }

    public String getjsonHR015(String sqr, String sqrbm, String jsonStr)
            throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sf.format(new Date());
        RecordSet rs = new RecordSet();
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        String reqman = "";//经办人员工号
        String mnyBearPsn = "";//费用承担人工号
        String reqdate = "";//申请日期
        String businessTripRange = "";//出差范围
        String mnyBearDept = "";//费用承担部门编号
        String reqsub = "";//所属分部编号
        String eeramt = "";//申请金额

        String txr = sqr;
        String sqrid = "";
        String szbm = "";
        String szgs = "";
        String fycdbm = "";
        String ecrqid = "";
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        reqman = head.getString("reqman");
        mnyBearPsn = head.getString("mnyBearPsn");
        reqdate = head.getString("reqdate");
        businessTripRange = head.getString("businessTripRange");
        mnyBearDept = head.getString("mnyBearDept");
        reqsub = head.getString("reqsub");
        eeramt = head.getString("eeramt");
        ecrqid = head.getString("ecrqid");
        String sql = "select id,departmentid from hrmresource where workcode='"
                + mnyBearPsn + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            sqrid = Util.null2o(rs.getString("id"));
            szbm = Util.null2o(rs.getString("departmentid"));
        }
        sql = "select id from hrmsubcompany where subcompanycode=" + reqsub;
        rs.executeSql(sql);
        if (rs.next()) {
            szgs = Util.null2o(rs.getString("id"));
        }
        sql = "select id from hrmdepartment where departmentcode='" + mnyBearDept + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            fycdbm = Util.null2o(rs.getString("id"));
        }
        json.put("HEADER", header);
        json.put("DETAILS", details);
        header.put("txr", txr);
        header.put("sqrq", reqdate);
        header.put("sqr", sqrid);
        header.put("gh", mnyBearPsn);
        header.put("szbm", szbm);
        header.put("fycdbm", fycdbm);
        header.put("szgs", szgs);
        header.put("fyys", eeramt);
        header.put("fjlb", businessTripRange);
        header.put("ecrqid", ecrqid);

        return json.toString();

    }

    public String getjsonSupplier(String sqr, String sqrbm, String jsonStr)
            throws Exception {
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sf.format(new Date());
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        String BUKRS = "";// 公司代码
        String BUTXT = "";// 公司名称
        String LIFNR = "";// 供应商
        String NAME1 = "";// 供应商名称
        String MATNR = "";// 物料编码
        String MAKTX = "";// 公司代码
        String ZQUALIFIED = "";//供应商状态

        json.put("HEADER", header);
        json.put("DETAILS", details);
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        BUKRS = head.getString("BUKRS");
        BUTXT = head.getString("BUTXT");
        LIFNR = head.getString("LIFNR");
        NAME1 = head.getString("NAME1");
        MATNR = head.getString("MATNR");
        MAKTX = head.getString("MAKTX");
        ZQUALIFIED = head.getString("ZQUALIFIED");

        header.put("applicant", sqr);
        header.put("dept", sqrbm);
        header.put("appDate", now);
        header.put("companyCode", BUKRS);
        header.put("companyName", BUTXT);
        header.put("supplier", LIFNR);
        header.put("supplierName", NAME1);
        header.put("materialCode", MATNR);
        header.put("materialName", MAKTX);
        header.put("suplStatus", ZQUALIFIED);

        return json.toString();

    }

    public String doserviceEmploy(String jsonStr, MessageContext ctx) throws JSONException {
        RecordSet rs = new RecordSet();
        String sqr = "";
        String sqrbm = "";
        String workcode = "";
        String workflowId = getWorkFlowID("6");
        String docCategory = "";
        Map<String, String> retMap = new HashMap<String, String>();
        String json = "";
        try {
            json = getjsonEmploy(jsonStr);
        } catch (Exception e) {
            log.writeLog("Exception info---------------->" + e.getMessage());

            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json格式不正确");
            retMap.put("OA_ID", "0");
            return getJsonStr(retMap);
        }
        JSONObject jObject = new JSONObject(jsonStr);
        JSONObject head = new JSONObject();
        String tablename = "formtable_main_1102";

        head = jObject.getJSONObject("HEADER");

        String sql = "";
        if ("".equals(workflowId)) {
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "请在流程类型配置表中添加对应流程");
            retMap.put("OA_ID", "0");
            return getJsonStr(retMap);
        }
        sql = " select DOCCATEGORY from WORKFLOW_BASE where ID=" + workflowId;
        rs.executeSql(sql);
        if (rs.next()) {
            String docCategories = Util.null2String(rs.getString("DOCCATEGORY"));
            String dcg[] = docCategories.split(",");
            docCategory = dcg[dcg.length - 1];
        }
        AutoRequestService ars = new AutoRequestService();
        String result = ars.createRequest(workflowId, json, "6340", "0");
        log.writeLog("result---------------->" + result);
        String docids = "";
        String flag = "";

        JSONObject resultobj = new JSONObject(result);
        log.writeLog("MSG_TYPE---------------->" + resultobj.getString("MSG_TYPE"));
        if ("S".equals(resultobj.getString("MSG_TYPE"))) {
            if (ctx.getInMessage().getAttachments() == null) {
                log.writeLog("ERROR---------------->" + ctx.getInMessage().getAttachments());
                return result;
            }

//            ctx.getInMessage().getXMLStreamReader().getName();
            Iterator it = ctx.getInMessage().getAttachments().getParts();
//            log.writeLog(it);
            log.writeLog("attachmentsize111:"+ctx.getInMessage().getAttachments().size());
            int i = 0;
            while (it.hasNext()) {
                Attachment attachment = (Attachment) it.next();
 //               DataHandler dh = attachment.getDataHandler();
                //String attachName = URLDecoder.dee(Util.null2String(attachment.getId()));
//                    if("".equals(attachName)) {code(Util.null2String(dh.getName()));
//                String docname = "";
//                log.writeLog("attachmentid11:"+attachment.getId());
//                if("".equals(attachName)){
//                    attachName = URLDecoder.decod
//                        log.writeLog("文档名获取异常");
//                    }
////                    log.writeLog("docname获取异常！");
//                }
                try {
                    String docid = createDoc("6340", docCategory, attachment, "");

                    if (!"".equals(docid)) {
                        docids = docids + flag + docid;
                        flag = ",";
                    }
                } catch (Exception e) {
                    log.writeLog(e);
                    log.writeLog("文档创建失败");
                }
                i=i+1;
            }
            log.writeLog("docids---->" + docids);
            sql = "update " + tablename + " set fj='" + docids + "' where requestid=" + resultobj.getString("OA_ID");
            rs.executeSql(sql);
        }
        return result;

    }

    public String getjsonEmploy(String jsonStr)
            throws Exception {
        log.writeLog("jsonStr-------->" + jsonStr);
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        String responsible = "";//HR负责人
        int datetime = 0;//系统时间
        String username = "";//应聘者姓名
        String architecture = "";//录用部门名称
        String corporation = "";//拟录用公司名称
        String city = "";//主要城市
        String position_level = "";//职等

        String position_code = "";//职位代码
        String channel = "";//发展通道
        String link2 = "";
        String link3 = "";
        String link4 = "";
        String link1 = "";
        String attachment = "";
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        responsible = head.getString("responsible_user");
        datetime = head.getInt("auto_datetime");
        corporation = head.getString("corporation");
        Calendar c = Calendar.getInstance();
        long millions = new Long(datetime).longValue() * 1000;
        c.setTimeInMillis(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = sdf.format(c.getTime());
        username = head.getString("username");
        city = head.getString("city_id");
        position_level = head.getString("position_level");
        channel = head.getString("development_channel");
        architecture = head.getString("architecture");
        position_code = head.getString("position_code");
//        attachment = head.getString("attachment");
        RecordSet rs = new RecordSet();
        String sql = "";

        //获取部门，公司，职位名称，通道
        sql = "select * from Hrmdepartment where departmentcode='"
                + architecture + "'";
        rs.executeSql(sql);
        if (rs.next()) {
//            position_code = Util.null2o(rs.getString("id"));
            architecture = Util.null2o(rs.getString("id"));
        }
        sql = "select * from HrmSubCompany where subcompanycode='"
                + corporation + "'";
        rs.executeSql(sql);
        if (rs.next()) {
            corporation = Util.null2o(rs.getString("id"));
        }

//        log.writeLog("position_level-------->" + position_level);
//        link1 = head.getString("link1");
        link1 = URLDecoder.decode(head.getString("link1"));
        link2 = URLDecoder.decode(head.getString("link2"));
        link3 = URLDecoder.decode(head.getString("link3"));
        link4 = URLDecoder.decode(head.getString("link4"));
        header.put("Person_charge", responsible);
        header.put("apply_date", dateString);
        header.put("name", username);
        header.put("emp_dept", architecture);
        header.put("corporation", corporation);
        header.put("City", city);
        header.put("position_level", position_level);
        header.put("position_code", position_code);
        header.put("development_channel", channel);
        header.put("link1", "<a target=blank href=" + link1 + ">面试官评价</a>");
        header.put("link2", "<a target=blank href=" + link2 + ">测评</a>");
        header.put("link3", "<a target=blank href=" + link3 + ">简历</a>");
        header.put("link4", "<a target=blank href=" + link4 + ">应聘登记表</a>");

        json.put("HEADER", header);
        JSONObject dts = new JSONObject();
        JSONArray dt11 = new JSONArray();
        dt11.put(dts);
        details.put("DT1", dt11);
        json.put("DETAILS", details);
        log.writeLog("json-------->" + json.toString());
        return json.toString();

    }

    public String doserviceDocLimit(String jsonStr) throws JSONException {
        RecordSet rs = new RecordSet();
        String sqr = "";
        String sqrbm = "";
//		String workflowId = "9341";//正式机:8161
        String workflowId = getWorkFlowID("4");
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        String workcode = head.getString("UserCode");
        String sql = "select id,departmentid from hrmresource where workcode='"
                + workcode + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            sqr = Util.null2o(rs.getString("id"));
            sqrbm = Util.null2o(rs.getString("departmentid"));
        } else {
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "人员编号无法匹配！");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        String json = "";
        try {
            json = getjsonDocLimit(sqr, sqrbm, jsonStr);
        } catch (Exception e) {
            log.writeLog("Exception info---------------->" + e.getMessage());
            Map<String, String> retMap = new HashMap<String, String>();
            retMap.put("MSG_TYPE", "E");
            retMap.put("MSG_CONTENT", "json格式不正确");
            retMap.put("OA_ID", "0");

            return getJsonStr(retMap);
        }
        AutoRequestService ars = new AutoRequestService();
        String result = ars.createRequest(workflowId, json, sqr, "0");
        return result;

    }

    public String getjsonDocLimit(String sqr, String sqrbm, String jsonStr)
            throws Exception {
        /**
         * UserCode	字符	申请人工号	工号
         * Path	字符	文件目录	路径
         * Code	字符	唯一编号	流程审批完成后需回传此编号
         * DataInfo	Json字符串	文件或文件夹数据信息	集合形式
         * Type	字符	类型为文件或文件夹	类型为文件或文件夹
         */
        /**
         * UserCode	文本	工号	根据工号带出姓名
         * wdml	文本	文档目录	路径
         * Code	文本	DMC编号	流程审批完成后需回传此编号
         * mxb1	明细表	明细表1	明细表
         */
        /**
         * Name	字符	名称	文件或文件夹名称	wjmc	字符	文件名称	文件或文件夹名称
         * ID	字符	唯一ID	文件或文件夹ID（不用展示到OA）	ID	字符	唯一ID	文件或文件夹ID（不用展示到OA）
         * CreateUser	字符	创建用户		CreateUser	字符	创建用户
         * CreateDate	Date	创建时间		CreateDate	Date	创建时间
         * SecurityLevel	字符	密级		SecurityLevel	字符	密级
         * CreateCode	字符	创建用户工号		CreateCode	字符	创建用户工号
         */

        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String now = sf.format(new Date());
        JSONObject json = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        String UserCode = "";//申请人工号
        String Path = "";//文件目录
        String Code = "";//唯一编号
        String Type = "";//类型为文件或文件夹

        String Name = "";
        String ID = "";
        String CreateUser = "";
        String CreateDate = "";
        String SecurityLevel = "";
        String CreateCode = "";
        String apply = "";
        JSONObject jo = new JSONObject(jsonStr);
        JSONObject head = jo.getJSONObject("HEADER");
        UserCode = head.getString("UserCode");
        Path = head.getString("Path");
        Code = head.getString("Code");

        RecordSet rs = new RecordSet();
        String sql = "select id,departmentid from hrmresource where workcode='"
                + UserCode + "' and status in(0,1,2,3)";
        rs.executeSql(sql);
        if (rs.next()) {
            apply = Util.null2o(rs.getString("id"));
        }

        header.put("UserCode", UserCode);
        header.put("wdml", Path);
        header.put("sqr", apply);
        header.put("Code", Code);
        header.put("sqrq", now);
        //dw  xqrszdw
        JSONObject dts = jo.getJSONObject("DETALIS");
        JSONArray dt1 = dts.getJSONArray("DT1");
        JSONArray dt11 = new JSONArray();
        for (int i = 0; i < dt1.length(); i++) {
            JSONObject arr = dt1.getJSONObject(i);
            JSONObject node = new JSONObject();
            Name = arr.getString("Name");
            ID = arr.getString("ID");
            Type = arr.getString("Type");
            CreateUser = arr.getString("CreateUser");
//			sql = "select id from hrmresource where workcode='"
//					+ CreateCode + "' and status in(0,1,2,3)";
//			rs.executeSql(sql);
//			if (rs.next()) {
//				CreateUser = Util.null2o(rs.getString("id"));
//			}
            CreateDate = arr.getString("CreateDate");
            SecurityLevel = arr.getString("SecurityLevel");
            CreateCode = arr.getString("CreateCode");

            node.put("wdmc", Name);
            node.put("wyid", ID);
            node.put("cjyh", CreateUser);
            node.put("cjsj", CreateDate);
            node.put("mj", SecurityLevel);
            node.put("cjyhid", CreateCode);
            node.put("Type", Type);

            dt11.put(node);
        }
        details.put("DT1", dt11);

        json.put("HEADER", header);
        json.put("DETAILS", details);
        log.writeLog("json-------->" + json.toString());
        return json.toString();

    }

    public String createDoc(String createrid, String seccategory, Attachment attachment, String attachName) throws Exception {
        BaseBean log = new BaseBean();
        String docids = "";
        String flag = "";
        DataHandler dh = attachment.getDataHandler();
        String docname = "";
//        if ("".equals(attachName)) {
//            docname = Util.null2String(attachment.getId());
//            if("".equals(docname)) {
//            	log.writeLog("文档名获取异常");
//            	return "";
//            }
////            log.writeLog("文档名获取异常");
//            return "";
//        }
        InputStream fi = dh.getInputStream();
		ZipInputStream zis = new ZipInputStream(fi);
       
		ZipEntry entry = null ;
		while ( ( entry = zis.getNextEntry() ) != null )
        {
			docname = entry.getName();
			log.writeLog("docname:"+docname);
			String uploadBuffer = "";
			String docid = "";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
		    byte[] buffer = new byte[1024];
		    int count = 0;
		    while ((count = zis.read(buffer)) >= 0) {
		         baos.write(buffer, 0, count);
		    }
		    uploadBuffer = org.apache.axis.encoding.Base64.encode(baos.toByteArray());
		    docid = getDocId(docname, uploadBuffer, createrid, seccategory);
		    if(!"".equals(docid)) {
		    	docids = docids+flag+docid;
		    	flag = ",";
		    }
        }
       
        return docids;
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

    public String getWorkFlowID(String id) {
        RecordSet rs = new RecordSet();
        String res = "";
        String sql = " select workflowID from uf_cflcpzb where flowtype='" + id + "' and status='0' ";
        rs.execute(sql);
        if (rs.next()) {
            res = rs.getString("workflowID");
        }
        return res;
    }
}
