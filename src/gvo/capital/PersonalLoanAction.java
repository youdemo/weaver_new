package gvo.capital;

import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.shaw.gvo.pay.HnPayServiceUtil;
import weaver.interfaces.shaw.gvo.util.GetMessageUtil;
import weaver.interfaces.shaw.gvo.util.TransformUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 个人借款申请与资金系统对接回写
 * 
 * @author adore
 * @version 2.0  2017-10-28
 * 
 **/
public class PersonalLoanAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入个人借款流程申请 PersonalLoanAction——————");
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();
        TransformUtil tran = new TransformUtil();
        String sql = "";
        String tableName = "";
        String tableNamedt = "";//明细表
        String mainID = "";//主表id,关联明细表
        String SERIAL_NO_ERP = "";//借款单编号
        String REQ_DATE = "";//申请日期
        String CORP_CODE = "";//公司代码
        String CUR = "";//结算币别
        String ZZBS = "";//借款类型
        String RMK = "";//借款单编号
        String ABS = "";//借款申请事由
        String WISH_PAY_DAY = "";//申请日期
        String ZZKM = "";//传递空值
        String JZDM = "29";//固定值29
        String SYSTEM_TYPE = "0";//固定值0

        sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {

            tableNamedt = tableName + "_dt2";

            /**查询主表**/

            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
                mainID = Util.null2String(rs.getString("ID"));
                SERIAL_NO_ERP = Util.null2String(rs.getString("requestid"));
                REQ_DATE = Util.null2String(rs.getString("reqdate"));
                CORP_CODE = Util.null2String(rs.getString("corpcode"));
                CUR = Util.null2String(rs.getString("balancetype"));
                ZZBS = Util.null2String(rs.getString("loantype"));
                ZZBS = tran.getLoantype(ZZBS);
                RMK = Util.null2String(rs.getString("flowno"));
                ABS = Util.null2String(rs.getString("remark"));
                WISH_PAY_DAY = Util.null2String(rs.getString("reqdate"));
            }
            
            try {
                JSONObject head = new JSONObject();
                JSONArray jsonArray = new JSONArray();

                /**查询明细表**/

                sql = "select * from " + tableNamedt + " where mainid=" + mainID;
                rs.execute(sql);
                while (rs.next()) {
                    String AMT = Util.null2String(rs.getString("payamt"));//支付金额
                    String PAYER_ACC_NO = Util.null2String(rs.getString("fkyhzh2"));//付款银行账号
                    String ITEM_CODE = Util.null2String(rs.getString("kmbm"));//资金计划科目编码
                    String GYSDM = Util.null2String(rs.getString("hrmcode"));//支付信息员工编号
                    String PAYEE_NAME = Util.null2String(rs.getString("hrmname"));//员工户名
                    String PAYEE_BANK = Util.null2String(rs.getString("hrmbankname"));//开户行
                    String PAYEE_ACC_NO = Util.null2String(rs.getString("hrmbankaccount"));//银行账号
                    String PAYEE_CODE = Util.null2String(rs.getString("hrmbankcode"));//联行号
                    String FKYYDM = Util.null2String(rs.getString("cashcode2"));//现金流量代码
                    String PURPOSE = Util.null2String(rs.getString("usage"));//银行付款用途
                    String VOUCHER_TYPE = Util.null2String(rs.getString("paytype"));//付款方式
                     
                    VOUCHER_TYPE = tran.getPaytype(VOUCHER_TYPE);
                    String ISFORINDIVIDUAL = Util.null2String(rs.getString("gsbz"));//对公对私标志
                    String URGENCY_FLAG = Util.null2String(rs.getString("jjbz"));//加急标志
                    
                    JSONObject jsonObjSon = new JSONObject();

                    jsonObjSon.put("serial_no_erp", SERIAL_NO_ERP);
                    jsonObjSon.put("req_date", REQ_DATE);
                    jsonObjSon.put("corp_code", CORP_CODE);
                    jsonObjSon.put("payer_acc_no", PAYER_ACC_NO);
                    jsonObjSon.put("cur", CUR);
                    jsonObjSon.put("item_code", ITEM_CODE);
                    jsonObjSon.put("zzbs", ZZBS);
                    jsonObjSon.put("rmk", RMK);
                    jsonObjSon.put("abs", ABS);
                    jsonObjSon.put("voucher_type", VOUCHER_TYPE);
                    jsonObjSon.put("wish_pay_day", WISH_PAY_DAY);
                    jsonObjSon.put("zzkm", ZZKM);
                    jsonObjSon.put("jzdm", JZDM);
                    jsonObjSon.put("system_type", SYSTEM_TYPE);

                    jsonObjSon.put("fkyydm", FKYYDM);
                    jsonObjSon.put("purpose", PURPOSE);
                    jsonObjSon.put("amt", AMT);
                    jsonObjSon.put("gysdm", GYSDM);
                    jsonObjSon.put("payee_name", PAYEE_NAME);
                    jsonObjSon.put("payee_bank", PAYEE_BANK);
                    jsonObjSon.put("payee_acc_no", PAYEE_ACC_NO);
                    jsonObjSon.put("payee_code", PAYEE_CODE);
                    jsonObjSon.put("isforindividual", ISFORINDIVIDUAL);
                    jsonObjSon.put("urgency_flag", URGENCY_FLAG);


                    jsonArray.put(jsonObjSon);
                }
                head.put("bean", jsonArray);

                HnPayServiceUtil hpsu = new HnPayServiceUtil();
                String result = "";
                result = hpsu.hnPayService(head.toString());
                log.writeLog("开始————————" + head.toString());
                GetMessageUtil gmu = new GetMessageUtil();
                String status = gmu.getStatus(result);
                String message = gmu.getMessage(result);
                String sql_update = "update " + tableName + " set status='" + status + "',message='" + message + "' where requestid=" + requestid;
                rs.execute(sql_update);
                log.writeLog("错误信息————————" + sql_update);
                if ("F".equals(status)) {
                    //调用异常 返回错误信息
                    info.getRequestManager().setMessageid(System.currentTimeMillis() + "");
                    info.getRequestManager().setMessagecontent(message);
                    return SUCCESS;
                }
               
            } catch (Exception e) {
                log.writeLog("json拼接错误");
            }

        } else {
            log.writeLog("流程表信息获取失败!");
            return "-1";
        }
        return SUCCESS;
    }

}
