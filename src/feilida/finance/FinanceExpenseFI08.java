package feilida.finance;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * Updated by adore on 2017/01/11.
 * 凭证摘要
 * 1.如果冲销区有勾选项：
 * 生成规则（一）：主表报销类型"D01-普通类报销，D03-业务招待费，D04-资讯费用-软件开发费和维护费”，
 * “OA预付款申请编号”+过账日期“月份”+“供应商名称”+费用项目名称”
 * 生成规则（二）：主表报销类型" D02-资产类报销”，“OA预付款申请编号”+过账日期“月份”+“供应商名称”+资产描述”
 * <p>
 * 2.如果冲销区无勾选项：
 * 生成规则（一）：主表报销类型"D01-普通类报销，D03-业务招待费，D04-资讯费用-软件开发费和维护费”，
 * 过账日期“月份”+“供应商名称”+费用项目名称”
 * 生成规则（二）：主表报销类型" D02-资产类报销”，过账日期“月份”+“供应商名称”+资产描述”
 */

public class FinanceExpenseFI08 implements Action {

    BaseBean log = new BaseBean();//定义写入日志的对象

    public String execute(RequestInfo info) {
        log.writeLog("FinanceExpenseFI08——————");

        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();//获取工作流程ID与requestID值

        RecordSet rs = new RecordSet();
        RecordSet rs_dt = new RecordSet();

        String tableName = "";
        String tableNamedt = "";
        String tableNamedt3 = "";
        String mainID = "";
        String expType = "";//报销类型
        String applyDate = "";//过账日期

        String sql = " Select tablename From Workflow_bill Where id in ("
                + " Select formid From workflow_base Where id= "
                + workflowID + ")";

        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {
            String PZZY = "";//凭证摘要
            tableNamedt = tableName + "_dt1";
            tableNamedt3 = tableName + "_dt3";

            // 查询主表
            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
                mainID = Util.null2String(rs.getString("ID"));
                expType = Util.null2String(rs.getString("BXLX"));
                //applyDate = Util.null2String(rs.getString("GZRQ"));
                applyDate = Util.null2String(rs.getString("BXRQ"));
                if (!"".equals(applyDate)) {
                    applyDate = applyDate.substring(5, 7);
                }
            }

            //冲销是否有勾选
            int isExist = 0;
            sql = " select count(1) as isExist from " + tableNamedt3 + " where mainid = " + mainID + " and XZ = 1 ";
            rs.execute(sql);
            log.writeLog("是否冲销:" + sql);
            if (rs.next()) {
                isExist = rs.getInt("isExist");
            }

            if (isExist > 0) {
                //查询明细表3
                String applyNum = "";//OA预付款申请编号
                sql = "select * from " + tableNamedt3 + " where mainid=" + mainID;
                rs_dt.execute(sql);
                log.writeLog("OA预付款申请编号:" + sql);
                while (rs_dt.next()) {
                    applyNum = Util.null2String(rs_dt.getString("YFKBH"));
                }

                //查询明细表1
                sql = "select * from " + tableNamedt + " where mainid=" + mainID;
                rs_dt.execute(sql);
                log.writeLog("查询费用科目:" + sql);
                while (rs_dt.next()) {
                    String zcms = Util.null2String(rs_dt.getString("ZCMS"));
                    String pro_id = Util.null2String(rs_dt.getString("FYXMMC"));
                    String supllierName = Util.null2String(rs_dt.getString("GYSMC"));
                    String dtid = Util.null2String(rs_dt.getString("id"));
                    String sql_name = " select fyxmmc from uf_fyxmdzb where id=" + pro_id;
                    String pro_name = "";
                    rs.executeSql(sql_name);
                    if (rs.next()) {
                        pro_name = Util.null2String(rs.getString("fyxmmc"));

                    }
                    if ("1".equals(expType)) {
                        PZZY = applyNum + applyDate + supllierName + zcms;
                    } else {
                        PZZY = applyNum + applyDate + supllierName + pro_name;
                    }
                    String sql_update_dt1 = " update " + tableNamedt + " set PZZY='" + PZZY + "' where id= " + dtid;
                    rs.executeSql(sql_update_dt1);
                }

            } else {
                //查询明细表1
                sql = "select * from " + tableNamedt + " where mainid=" + mainID;
                rs_dt.execute(sql);
                log.writeLog("查询费用科目:" + sql);
                while (rs_dt.next()) {
                    String zcms = Util.null2String(rs_dt.getString("ZCMS"));
                    String pro_id = Util.null2String(rs_dt.getString("FYXMMC"));
                    String supllierName = Util.null2String(rs_dt.getString("GYSMC"));
                    String dtid = Util.null2String(rs_dt.getString("id"));
                    String sql_name = " select fyxmmc from uf_fyxmdzb where id=" + pro_id;
                    String pro_name = "";
                    rs.executeSql(sql_name);
                    if (rs.next()) {
                        pro_name = Util.null2String(rs.getString("fyxmmc"));

                    }
                    if ("1".equals(expType)) {
                        PZZY = applyDate + supllierName + zcms;
                    } else {
                        PZZY = applyDate + supllierName + pro_name;
                    }
                    String sql_update_dt1 = " update " + tableNamedt + " set PZZY='" + PZZY + "' where id= " + dtid;
                    rs.executeSql(sql_update_dt1);
                    log.writeLog("sql_update=" + sql_update_dt1);
                }
                
            }
            
            sql = " delete from " + tableNamedt3 + " where mainid = " + mainID + " and XZ = 0";
            rs.execute(sql);
            log.writeLog("明细删除:" + sql);
            
        } else {

            return "-1";
        }
        return SUCCESS;
    }

}

