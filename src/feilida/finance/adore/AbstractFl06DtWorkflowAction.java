package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * 明细行摘要
 * 字段组合出来，月份+预付+供应商简称、需要自动出来，不能超过50个字符
 */
public class AbstractFl06DtWorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入Fl-06供应商预付款申请表单AbstractFl06DtWorkflowAction——————");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();

        String sql = "";
        String tableName = "";
        String tableNamedt = "";
        String main_id = "";
        String abstract_main = "";
        String applyDate = "";

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
            log.writeLog("sql_dt=" + sql);
            if (rs.next()) {
                main_id = Util.null2String(rs.getString("id"));
                applyDate = Util.null2String(rs.getString("QKRQ"));
                applyDate = applyDate.substring(5, 7);

            }
            //log.writeLog("applyDate=" + applyDate);
            //查询明细表信息
            sql = "select * from " + tableNamedt + " where mainid=" + main_id;
            rs.execute(sql);
            while (rs.next()) {
                String lastname = Util.null2String(rs.getString("KHGYSJC"));
                String abstract_dt = applyDate + "预付" + lastname;

                if (abstract_dt.length() > 50) {
                    abstract_dt = abstract_main.substring(0, 50);
                }

                sql = " update " + tableNamedt + " set WB='" + abstract_dt + "' where mainid=" + main_id;
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
