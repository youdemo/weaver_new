package gvo.capital;

import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.shaw.gvo.util.GetMessageBankUtil;
import weaver.interfaces.shaw.gvo.bank.BankAccServiceUtil;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.interfaces.shaw.gvo.util.TransformUtil;

/**
 * 银行账号创建与资金系统对接回写
 * 
 * @author adore
 * @version 2.0  2017-10-28
 * 
 **/
public class BankAccAction implements Action {
    BaseBean log = new BaseBean();//定义写入日志的对象

    public String execute(RequestInfo info) {
        log.writeLog("进银行账户审批BankAccAction——————");
        String workflowID = info.getWorkflowid();//获取工作流程Workflowid的值
        String requestid = info.getRequestid();//获取requestid的值

        RecordSet rs = new RecordSet();
        TransformUtil tran = new TransformUtil(); 
        String sql = "";
        String tableName = "";
        String CORP_CODE = "";//公司代码
        String BANKACC = "";//银行账号
        String ACC_NAME = "";//账户名称（公司名称）
        String BANK_NAME = "";//联行号
        String ACC_CATEGORY = "";//账户性质
        String IS_ONLINE = "";//账户是否上线
        String ACC_STATE = "";//申请类型
        String REGISTER_DATE = "";//申请日期
        String OP_RESSION = "";//具体事由
        String CUR = "";//币别
        String ACC_TYPE1 = "";//账户类别
        String BANK_TYPE = "";//银行类别
        String ACC_TYPE = "";//账户收支属性
        sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

        rs.execute(sql);
        if (rs.next()) {
            tableName = Util.null2String(rs.getString("tablename"));
        }

        if (!"".equals(tableName)) {

            sql = "select * from " + tableName + " where requestid=" + requestid;
            rs.execute(sql);
            if (rs.next()) {
            	CORP_CODE = Util.null2String(rs.getString("gsdm"));
                BANKACC = Util.null2String(rs.getString("yhzh"));
                ACC_NAME = Util.null2String(rs.getString("zhmc"));
                BANK_NAME = Util.null2String(rs.getString("lhh"));
                BANK_TYPE = Util.null2String(rs.getString("yhlb"));
                ACC_CATEGORY = Util.null2String(rs.getString("zhxz"));
                ACC_CATEGORY = tran.getAccountcategory(ACC_CATEGORY);
                ACC_TYPE1 = Util.null2String(rs.getString("zhlb"));
                ACC_TYPE = Util.null2String(rs.getString("zhsvsx"));
                CUR = Util.null2String(rs.getString("bb"));
                IS_ONLINE = Util.null2String(rs.getString("sfsx"));
                ACC_STATE = Util.null2String(rs.getString("sqlx"));
                ACC_STATE = tran.getAccounttype(ACC_STATE);
                REGISTER_DATE = Util.null2String(rs.getString("sqrq"));
                OP_RESSION = Util.null2String(rs.getString("jtsy"));
            }


            try {
                JSONObject head = new JSONObject();
                JSONArray jsonarray = new JSONArray();
                JSONObject jsonObjSon = new JSONObject();
                jsonObjSon.put("corp_code", CORP_CODE);
                jsonObjSon.put("bankacc", BANKACC);
                jsonObjSon.put("acc_name", ACC_NAME);
                jsonObjSon.put("bank_name", BANK_NAME);
                jsonObjSon.put("bank_type", BANK_TYPE);
                jsonObjSon.put("acc_category", ACC_CATEGORY);
                jsonObjSon.put("acc_type1", ACC_TYPE1);
                jsonObjSon.put("acc_type", ACC_TYPE);
                jsonObjSon.put("cur", CUR);
                jsonObjSon.put("is_online", IS_ONLINE);
                jsonObjSon.put("acc_state", ACC_STATE);
                jsonObjSon.put("register_date", REGISTER_DATE);
                jsonObjSon.put("op_ression", OP_RESSION);

                jsonarray.put(jsonObjSon);
                head.put("bean", jsonarray);

                BankAccServiceUtil basu = new BankAccServiceUtil();
                
                String result = "";
                result = basu.bankAccServiceMethod(head.toString());
                log.writeLog("开始————————"+head.toString());
                GetMessageBankUtil gmu = new GetMessageBankUtil();
                String status = gmu.getStatus(result);
                String message = "";
                message = gmu.getMessage(result);
                String sql_update = "update " + tableName + " set status='" + status + "',message='" + message + "' where requestid=" + requestid;
                rs.execute(sql_update);
                log.writeLog("错误信息————————" + sql_update);
                
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
