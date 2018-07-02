package gvo.purchase.updatestatus;

import gvo.purchase.updatestatus.SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub.Response;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class UpdateSignAction implements Action {
    /**
     * oa与sap采购申请审批状态更新
     *
     * @author daisy
     * @version 1.0 2017-11-15
     **/
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入采购订单审批状态更新 UpdateSignAction——————");
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();
        RecordSet rs = new RecordSet();
        String tableName = "";
        String PRNO = "";// 采购申请单号
        String STATUS = "4";//审批标识
        String sql = "";
        sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }
        if (!"".equals(tableName)) {
            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
                PRNO = Util.null2String(rs.getString("prNo"));
            }

            JSONObject jsonObjSon = new JSONObject();
            try {
                jsonObjSon.put("PRNO", PRNO);
                jsonObjSon.put("STATUS", STATUS);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            StateXmlUtil state = new StateXmlUtil();
            String json = state.javaToXml(jsonObjSon.toString(), "", requestid, "");
            log.writeLog("打印json————————" + json);
            UpdatePRApproval pur = new UpdatePRApproval();
            String SIGN = "";
            String MESSAGE = "";
            try {
                Response result = pur.getResultMethod(json);
                SIGN = result.getSIGN();
                MESSAGE = result.getMessage();
                log.writeLog("sign = " + SIGN);
                log.writeLog("MESSAGE = " + MESSAGE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
            String para = "E_MSGTX";
            Map<String, Object> result = saxXmlUtil.getXmlMap(MESSAGE);
            Object message = result.get(para);//消息文本
            log.writeLog("消息文本————————" + message);
            String sql_update = "update " + tableName + " set e_falg_s='" + SIGN + "',e_mes_s='" + message + "' where requestid=" + requestid;
            rs.execute(sql_update);
            log.writeLog("更新语句————————" + sql_update);
        }
        return SUCCESS;
    }
}
