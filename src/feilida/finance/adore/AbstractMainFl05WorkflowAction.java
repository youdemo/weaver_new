package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * 表头摘要
 * 支付+月份+费用
 */
public class AbstractMainFl05WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入Fl-05供应商一般付款申请表单（非银企直联）AbstractMainFl05WorkflowAction——————");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();

        String sql = "";
        String tableName = "";
        String applyDate = "";
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
            log.writeLog("sql_dt=" + sql);
            if (rs.next()) {
                applyDate = Util.null2String(rs.getString("QKRQ"));
                applyDate = applyDate.substring(5, 7);
                fl_money = Util.null2String(rs.getString("HZJE"));
            }
            log.writeLog("applyDate=" + applyDate);

            abstract_main = "支付" + applyDate + "费用";
            if (abstract_main.length() > 50) {
                abstract_main = abstract_main.substring(0, 50);
            }
            sql = " update " + tableName + " set PZZY='" + abstract_main + "' where requestid= " + requestid;
            rs.executeSql(sql);
            log.writeLog("sql_update=" + sql);
        } else {
            log.writeLog("流程信息表读取错误");
            return "-1";
        }
        return SUCCESS;
    }
}
