package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * �������
 * �ֶ���ϳ������·�+Ա������+�����ܳ���50���ַ�
 */
public class ReasonFl17WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("����Fl-17Ա����������ReasonFl17WorkflowAction������������");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();

        String sql = "";
        String tableName = "";
        String applyDate = "";
        String supp_name = "";
        String abstract_main = "";
        String fl_money = "";

        sql = " select tablename from Workflow_bill where id in (" + "select formid from workflow_base where id=" + workflowID + ")";
        //log.writeLog("sql---------" + sql);
        rs.executeSql(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {
            sql = " select * from " + tableName + " where requestid = " + requestid;
            rs.executeSql(sql);
            if (rs.next()) {
                applyDate = Util.null2String(rs.getString("QKRQ"));
                applyDate = applyDate.substring(5, 7);
                supp_name = Util.null2String(rs.getString("YGGYSMC"));
                fl_money = Util.null2String(rs.getString("JEXX"));

            }
            abstract_main = applyDate + supp_name + "���";
            if (abstract_main.length() > 50) {
                abstract_main = abstract_main.substring(0, 50);
            }
            sql = " update " + tableName + " set PZZY='" + abstract_main + "' where requestid= " + requestid;
            rs.executeSql(sql);
            log.writeLog("sql_update=" + sql);
        } else {
            log.writeLog("������Ϣ���ȡ����");
            return "-1";
        }
        return SUCCESS;
    }
}
