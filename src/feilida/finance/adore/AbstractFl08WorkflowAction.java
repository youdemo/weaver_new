package feilida.finance.adore;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 2016/11/12.
 * 凭证摘要
 * 过账日期“月份”+“供应商名称”+费用项目名称（明细表循环取值，用”/”隔开）”
 */
public class AbstractFl08WorkflowAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入Fl08-报销申请单AbstractFl08WorkflowAction——————");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();

        String sql = "";
        String tableName = "";
        String tableNamedt = "";
        String main_id = "";
        String applyDate = "";
        String abstract_dt = "";
        String abstract_main = "";
        String supp_name = "";

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
                applyDate = Util.null2String(rs.getString("GZRQ"));
                applyDate = applyDate.substring(5, 7);
                supp_name = Util.null2String(rs.getString("GYSMC"));
            }
            //log.writeLog("applyDate=" + applyDate);
            //查询明细表信息
            sql = "select * from " + tableNamedt + " where mainid=" + main_id;
            rs.execute(sql);
            String flag = "";
            while (rs.next()) {
                String pro_id = Util.null2String(rs.getString("FYXMMC"));
                String sql_name = " select fyxmmc from uf_fyxmdzb where id="+pro_id;
                res.executeSql(sql_name);
                if(res.next()){
                    String pro_name = Util.null2String(res.getString("fyxmmc"));
                    abstract_dt += flag + pro_name;
                    flag = "/";
                }
            }
            log.writeLog("abstract_dt=" + abstract_dt);

            abstract_main = applyDate + supp_name + abstract_dt;
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
