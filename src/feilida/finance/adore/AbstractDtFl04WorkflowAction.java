package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * ��ϸ��ƾ֤ժҪ
 * �ֶ���ϳ������·�+��+��Ӧ�̼��+���ã���Ҫ�Զ����������ܳ���50���ַ�
 */
public class AbstractDtFl04WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("����Fl04-��Ӧ��һ�㸶�����뵥������ֱ����AbstractDtFl05WorkflowAction������������");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();

        String sql = "";
        String tableName = "";
        String tableNamedt = "";
        String main_id = "";
        String abstract_main = "";

        sql = " select tablename from Workflow_bill where id in (" + "select formid from workflow_base where id=" + workflowID + ")";
        //log.writeLog("sql---------" + sql);
        rs.executeSql(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {
            tableNamedt = tableName + "_dt3";

            sql = " select * from " + tableName + " where requestid = " + requestid;
            rs.executeSql(sql);
            log.writeLog("sql_dt=" + sql);
            if (rs.next()) {
                main_id = Util.null2String(rs.getString("id"));


            }
            //log.writeLog("applyDate=" + applyDate);
            //��ѯ��ϸ����Ϣ
            sql = "select * from " + tableNamedt + " where mainid=" + main_id;
            rs.execute(sql);
            while (rs.next()) {
                String applyDate = Util.null2String(rs.getString("DCRQ"));
                applyDate = applyDate.substring(5, 7);
                String lastname = Util.null2String(rs.getString("GYSJC"));
                //String fl_money = Util.null2String(rs.getString("JYYBJE"));
                String abstract_dt = applyDate + "��" + lastname + "����";
                String dtid = Util.null2String(rs.getString("id"));

                if (abstract_dt.length() > 50) {
                    abstract_dt = abstract_main.substring(0, 50);
                }

                sql = " update " + tableNamedt + " set WB='" + abstract_dt + "' where id=" + dtid;
                res.executeSql(sql);
                log.writeLog("sql_update=" + sql);
            }
        } else {
            log.writeLog("������Ϣ���ȡ����");
            return "-1";
        }
        return SUCCESS;
    }
}
