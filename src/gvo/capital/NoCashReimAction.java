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
 * 非现金报销付款申请黑牛V0-018与资金系统对接回写
 * 
 * @author daisy
 * @version 3.0  2017-10-31
 * 
 **/
public class NoCashReimAction implements Action {
    BaseBean log = new BaseBean();

    public String execute(RequestInfo info) {
        log.writeLog("进入非现金报销付款申请黑牛 NoCashReimAction——————");
        String workflowID = info.getWorkflowid();
        String requestid = info.getRequestid();

        RecordSet rs = new RecordSet();
        //RecordSet res = new RecordSet();
        String sql = "";
        String tableName = "";
        String SERIAL_NO_ERP = "";//流程编号
        String REQ_DATE = "";//申请日期
        String CORP_CODE = "1700";//公司代码
        String PAYER_ACC_NO = "";//付款银行账号
        String CUR = "";//结算币别
        String ITEM_CODE = "";//资金计划科目编码
        String ZZBS = "";//特殊总帐标识
        String RMK = "";//流程编号
        String ABS = "";//事由
        String AMT = "";//本次付款额（RMB)
        String GYSDM = "";//供应商编码
        String PAYEE_NAME = "";//供应商
        String PAYEE_BANK = "";//银行名称
        String PAYEE_ACC_NO = "";//银行账号
        String PAYEE_CODE = "";//联行号
        String FKYYDM = "";//现金流量代码
        String PURPOSE = "";//银行付款用途
        String URGENCY_FLAG = "";//加急标志
        String ISFORINDIVIDUAL ="";//对公对私标志
        String VOUCHER_TYPE = "";//付款方式
        String WISH_PAY_DAY = "";//申请日期
        String ZZKM = "";//总账科目编码
        String JZDM = "40";//记账代码
        String SYSTEM_TYPE = "1";//固定值1
        String PAYTYPE = "";//结算方式

        sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {

            /**查询主表**/

            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
                SERIAL_NO_ERP = Util.null2String(rs.getString("requestid"));
                RMK = Util.null2String(rs.getString("lcdh"));
                REQ_DATE = Util.null2String(rs.getString("txrq"));
                PAYER_ACC_NO = Util.null2String(rs.getString("fkyhzh2"));
                CUR = Util.null2String(rs.getString("curtype"));
                ITEM_CODE = Util.null2String(rs.getString("kmbm"));
                ZZBS = Util.null2String(rs.getString("zzbs"));
                ABS = Util.null2String(rs.getString("sy"));
                VOUCHER_TYPE = Util.null2String(rs.getString("fkfs"));
                PAYTYPE = Util.null2String(rs.getString("jsfs"));
                ZZBS = Util.null2String(rs.getString("zzbs"));
                TransformUtil tran = new TransformUtil();
                if(PAYTYPE.equals("0")){
                ZZBS = tran.getPrepaytype(ZZBS);
                }else{
                	ZZBS="";
                }
                VOUCHER_TYPE = tran.getPaytype(VOUCHER_TYPE);
                
                WISH_PAY_DAY = Util.null2String(rs.getString("txrq"));
                AMT = Util.null2String(rs.getString("fkze"));
                GYSDM = Util.null2String(rs.getString("gysbm"));
                PAYEE_NAME = Util.null2String(rs.getString("gys"));
                PAYEE_BANK = Util.null2String(rs.getString("yhmc"));
                PAYEE_ACC_NO = Util.null2String(rs.getString("yhzh"));
                PAYEE_CODE = Util.null2String(rs.getString("bankcode"));
                FKYYDM = Util.null2String(rs.getString("cashcode2"));
                PURPOSE = Util.null2String(rs.getString("usage"));
                URGENCY_FLAG = Util.null2String(rs.getString("jjbz"));
                ZZKM = Util.null2String(rs.getString("zzkm2"));
                ISFORINDIVIDUAL = Util.null2String(rs.getString("gsbz"));
            }
            
            try {
                JSONObject head = new JSONObject();
                JSONArray jsonArray = new JSONArray();
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
