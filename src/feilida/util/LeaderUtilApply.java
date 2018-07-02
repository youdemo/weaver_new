package feilida.util;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Created by adore on 16/9/9.
 * 获取申请人所有上级
 */
public class LeaderUtilApply implements Action {
    BaseBean log = new BaseBean();//定义写入日志的对象

    public String execute(RequestInfo info) {
        log.writeLog("进入申请人直接上级LeaderUtilApply——————");

        String workflowID = info.getWorkflowid();//获取工作流程Workflowid的值
        String requestid = info.getRequestid();//获取requestid的值
        String wfcreater = "";//获取流程创建人

        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        RecordSet rs1 = new RecordSet();

        String sql = "";
        String tableName = "";
        String applicant = "";//申请人
        String HRFJL = "";//25
        String HRJL = "";//30
        String HRZHUZ = "";//60
        String HRFUZ = "";//70
        String[] leadergroup = new String[100];

        sql = " select creater from workflow_requestbase where requestid= " + requestid;
        rs.execute(sql);
        if (rs.next()) {
            wfcreater = Util.null2String(rs.getString("creater"));
        }

        sql = " Select tablename From Workflow_bill Where id in (Select formid From workflow_base Where id= " + workflowID + ")";
        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {
            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
                //修改【sqr】
                applicant = Util.null2String(rs.getString("sqr"));
            }

            sql = " select ltrim(sys_connect_by_path(id,'/'),'/') as leadergroup from hrmresource where CONNECT_BY_ISLEAF=1  start with id= " + applicant + " connect by prior managerid=id ";
            log.writeLog("sql_leader=" + sql);
            rs.execute(sql);
            if (rs.next()) {
                leadergroup = Util.null2String(rs.getString("leadergroup")).split("/");
                for (int i = 1; i <= leadergroup.length; i++) {
                    String tmpLeader = leadergroup[i];
                    sql = " select seclevel from HrmResource where id=" + tmpLeader;
                    log.writeLog("sql_tmp_" + tmpLeader + "=" + sql);
                    res.execute(sql);
                    if (res.next()) {
                        String seclevel = Util.null2String(res.getString("seclevel"));
                        if ("25".equals(seclevel)) {
                            sql = " update " + tableName + " set HRFJL = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            log.writeLog("sql_app_" + tmpLeader + "=" + sql);
                        }
                        if ("30".equals(seclevel)) {
                            sql = " update " + tableName + " set HRJL = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            log.writeLog("sql_app_" + tmpLeader + "=" + sql);
                        }
                        if ("60".equals(seclevel)) {
                            sql = " update " + tableName + " set HRZHUZ = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            log.writeLog("sql_app_" + tmpLeader + "=" + sql);
                        }
                        if ("70".equals(seclevel)) {
                            sql = " update " + tableName + " set HRFUZ = " + tmpLeader + " where requestid = " + requestid;
                            rs1.execute(sql);
                            log.writeLog("sql_app_" + tmpLeader + "=" + sql);
                        }
                    }
                }
            }

        } else {
            log.writeLog("流程表单获取失败!");
            return "-1";
        }
        return SUCCESS;
    }
}
