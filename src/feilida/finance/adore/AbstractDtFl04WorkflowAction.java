package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * 明细表凭证摘要
 * 字段组合出来，月份+付+供应商简称+费用，需要自动出来，不能超过50个字符
 */
public class AbstractDtFl04WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入Fl04-供应商一般付款申请单（银企直联）AbstractDtFl05WorkflowAction——————");

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
            //查询明细表信息
            sql = "select * from " + tableNamedt + " where mainid=" + main_id;
            rs.execute(sql);
            while (rs.next()) {
                String applyDate = Util.null2String(rs.getString("DCRQ"));
                applyDate = applyDate.substring(5, 7);
                String lastname = Util.null2String(rs.getString("GYSJC"));
                //String fl_money = Util.null2String(rs.getString("JYYBJE"));
                String abstract_dt = applyDate + "付" + lastname + "费用";
                String dtid = Util.null2String(rs.getString("id"));

                if (abstract_dt.length() > 50) {
                    abstract_dt = abstract_main.substring(0, 50);
                }

                sql = " update " + tableNamedt + " set WB='" + abstract_dt + "' where id=" + dtid;
                res.executeSql(sql);
                log.writeLog("sql_update=" + sql);
            }
        } else {
            log.writeLog("流程信息表读取错误");
            return "-1";
        }
        return SUCCESS;
    }
}
