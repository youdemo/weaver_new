package gvo.hr.business;

import gvo.hr.utils.PurXmlUtil;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.hr.EmployApprovalService;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployApproval implements Action {
    @Override
    public String execute(RequestInfo info) {
        String requestid = info.getRequestid();
        String workflowid = info.getWorkflowid();
        String sql = "";
        String nownodeid = "";//当前节点id
        String nownodename = "";//当前节点名称
        String backtime = "";//退回时间
        String backdate = "";//退回日期
        String logtype = "";//签字类型
        String operator = "";//操作者id
        String name = "";//操作者姓名
        String remark = "";//签字意见
        RecordSet rs = new RecordSet();
        ResourceComInfo hrm = null;
        try {
            hrm = new ResourceComInfo();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sql = "select * from workflow_nownode where requestid = " + requestid;
        rs.execute(sql);
        if(rs.next()){
            nownodeid = Util.null2String(rs.getString("nownodeid"));
        }
        sql = "select * from workflow_nodebase where id = " + nownodeid;
        rs.execute(sql);
        if(rs.next()){
            nownodename = Util.null2String(rs.getString("nodename"));
        }
        sql = "select * from workflow_requestlog where requestid = " + requestid + " and nodeid = " + nownodeid + " order by operatedate,operatetime";
        rs.execute(sql);
        while(rs.next()) {
            operator = Util.null2String(rs.getString("operator"));
            name = hrm.getResourcename(operator);
            logtype = Util.null2String(rs.getString("logtype"));
            backtime = Util.null2String(rs.getString("operatedate"));
            backdate = Util.null2String(rs.getString("operatetime"));
            remark = Util.null2String(rs.getString("remark"));
            remark = delHTMLTag(remark);
        }
        if("3".equals(logtype)){
            JSONObject jsonobj = new JSONObject();
            try {
                jsonobj.put("NGnode",name + nownodename + backdate + " " + backtime + "退回");//退回节点
                jsonobj.put("RequestID",requestid);//请求id
                jsonobj.put("operator",name);//操作者
                jsonobj.put("NGreason",remark);//签字意见
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
            Head head = new Head("IFC.VXG-104_" + time, "1", "OA", "1", "", "", "", "");
            String json = tran.javaToXml(jsonobj.toString(), "", requestid, "",head);
            EmployApprovalService hr = new EmployApprovalService();
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
                log.writeLog("sendZD result:sign "+sign+" retmsg "+retmsg);
            }
        }
        return SUCCESS;
    }
    public String delHTMLTag(String htmlStr) {
        String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
        String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
        Pattern p_script = Pattern.compile(regEx_script,
                Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签
        Pattern p_style = Pattern
                .compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签
        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签
        return htmlStr.trim(); // 返回文本字符串
    }
}
