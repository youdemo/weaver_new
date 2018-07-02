package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * ��������
 * ��������=��������ڡ��·ݡ�+���ͻ���ơ�+�ڲ��ʽ��������ϸ��ѭ��ȡֵ���á�/��������
 */
public class ReasonFl09WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("����Fl09-�ڲ��ʽ������ReasonFl09WorkflowAction������������");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();

        String sql = "";
        String tableName = "";
        String tableNamedt = "";
        String main_id = "";
        String applyDate = "";
        String abstract_dt = "";
        String abstract_main = "";
        String fl_money = "";

        sql = " select tablename from Workflow_bill where id in (" + "select formid from workflow_base where id=" + workflowID + ")";
        //log.writeLog("sql---------" + sql);
        rs.executeSql(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {
            tableNamedt = tableName + "_dt1";

            sql = " select * from " + tableName + " where requestid = " + requestid;
            rs.executeSql(sql);
            if (rs.next()) {
                main_id = Util.null2String(rs.getString("id"));
                applyDate = Util.null2String(rs.getString("QKRQ"));
                applyDate = applyDate.substring(5, 7);
                fl_money = Util.null2String(rs.getString("JEX"));
            }
            //log.writeLog("applyDate=" + applyDate);
            //��ѯ��ϸ����Ϣ
            sql = "select * from " + tableNamedt + " where mainid=" + main_id;
            rs.execute(sql);
            log.writeLog("sql_dt=" + sql);
            String flag = "";
            while (rs.next()) {
                String lastname = Util.null2String(rs.getString("GYSMC"));
                abstract_dt += flag + lastname;
                flag = "/";
            }
            //log.writeLog("abstract_dt=" + abstract_dt);

            abstract_main = applyDate + abstract_dt + "�ڲ��ʽ����";
            if (abstract_main.length() > 50) {
                abstract_main = abstract_main.substring(0, 50);
            }
            sql = " update " + tableName + " set DBLY='" + abstract_main + "' where requestid= " + requestid;
            rs.executeSql(sql);
            log.writeLog("sql_update=" + sql);
        } else {
            log.writeLog("������Ϣ���ȡ����");
            return "-1";
        }
        return SUCCESS;
    }
}
