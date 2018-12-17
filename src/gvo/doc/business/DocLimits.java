package gvo.doc.business;

import gvo.doc.utils.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.doc.DocLimitsService;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet; 
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DocLimits implements Action {
    @Override
    public String execute(RequestInfo info) {
        String requestid = info.getRequestid();
        String workflowid = info.getWorkflowid();
        RecordSet rs = new RecordSet();
        String sql = "";
        String tableName = "";// 表名
        String Code = "";
        String xqqx = "";
        String ksrq = "";
        String jsrq = "";
        String Member = "";
        String MemberType = "";
        String xqbm = "";
        String xqr = "";
        sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
                + workflowid + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        sql="select * from "+tableName+" where requestid="+requestid;
        rs.executeSql(sql);
        if(rs.next()){
            Code = Util.null2String(rs.getString("Code"));
            xqqx = Util.null2String(rs.getString("xqqx"));
            ksrq = Util.null2String(rs.getString("ksrq"));
            jsrq = Util.null2String(rs.getString("jsrq"));
//            Member = Util.null2String(rs.getString("Member"));
            MemberType = Util.null2String(rs.getString("MemberType"));
            xqbm = Util.null2String(rs.getString("xqbm"));
            xqr = Util.null2String(rs.getString("xqr"));
        }
        sql = "select selectname from workflow_selectitem where fieldid = '61620' and selectvalue = '" + xqqx + "'";
        rs.executeSql(sql);
        if(rs.next()){
            xqqx =  Util.null2String(rs.getString("selectname"));
        }
        sql = "select selectname from workflow_selectitem where fieldid = '62097' and selectvalue = '" + MemberType + "'";
        rs.executeSql(sql);
        if(rs.next()){
            MemberType =  Util.null2String(rs.getString("selectname"));
        }
        /**
        *Code
        Purview
        EffectiveDate
        ExpirationDate
        Member
        MemberType
        */
        JSONObject jsonobj = new JSONObject();
        try {
            jsonobj.put("Code",Code);
            jsonobj.put("Purview",xqqx);
            jsonobj.put("EffectiveDate",ksrq);
            jsonobj.put("ExpirationDate",jsrq);
            if("部门".equals(MemberType)){
                sql = "select departmentcode from hrmdepartment where id = '"
                        + xqbm + "'";
                rs.executeSql(sql);
                if (rs.next()) {
                    xqbm = Util.null2String(rs.getString("departmentcode"));
                }
                jsonobj.put("Member",xqbm);
            }else if("人员".equals(MemberType)){
                StringBuffer sb = new StringBuffer();
                sql = "select workcode from hrmresource where id in ("
                        + xqr + ")";
                rs.executeSql(sql);
                while (rs.next()) {
                    xqr = Util.null2String(rs.getString("workcode"));
                    sb.append(xqr);
                    sb.append(",");
                }
                String temp = sb.toString();
                if(temp.length() > 0){
                    xqr = temp.substring(0,(temp.length()-1));
                }
                jsonobj.put("Member",xqr);
            }
            jsonobj.put("MemberType",MemberType);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String time = sdf.format(new Date());
        BaseBean log = new BaseBean();
        PurXmlUtil tran = new PurXmlUtil();
        SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
        String sign = "";
        String retmsg = "";
        Head head = new Head("DMC.VXG-108_" + time, "1", "OA", "1", "", "", "", requestid);
        String json = tran.javaToXml(jsonobj.toString(), "", requestid, "",head);
        log.writeLog("json------> " + json);
        DocLimitsService hr = new DocLimitsService();
        String result = hr.MobileCodeWS(json);
        if("".equals(result)) {
            sign = "E";
            retmsg = "调用失败";
        }else {
            Map<String, Object> getxmlmap = SaxXmlUtil.getXmlMap(result);
            //Object SIGN = (String) getxmlmap.get("SIGN");
            sign = (String) getxmlmap.get("SIGN");
            String E_MESSAGE = (String) getxmlmap.get("Message");
            retmsg = saxXmlUtil.getResult("retMsg", E_MESSAGE);
            log.writeLog("result:sign "+sign+" retMsg "+retmsg);
        }
        sql = "update " + tableName + " set xxlx = '" + sign + "',jcxx = '" + retmsg + "' where requestid = " + requestid;
        rs.executeSql(sql);
        log.writeLog("sql------->" + sql);
        return SUCCESS;
    }
}
