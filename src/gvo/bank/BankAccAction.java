package gvo.bank;

import gvo.bank.BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.Response;
import gvo.util.pay.TransformUtil;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

/**
 * OA与ESB对接银行账号创建流程接口调整
 *
 * @author daisy
 * @version 1.0 2017-11-29
 **/
public class BankAccAction extends BaseBean implements Action{
    BaseBean log = new BaseBean();
    public Response getResultMethod(String json) throws Exception{
    	BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub basw = new BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub();
    	BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.BFSFI_WF_0_bankAccServiceWebService basws = new BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.BFSFI_WF_0_bankAccServiceWebService();
        basws.setData(json);
        Response result = basw.BFSFI_WF_0_bankAccServiceWebService(basws);
        return result;
    }
    public String execute(RequestInfo info) {
        log.writeLog("银行账号创建流程 BankAccAction——————");
        String requestid = info.getRequestid();//请求ID
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        TransformUtil tran = new TransformUtil();
        String tableName = info.getRequestManager().getBillTableName();//表名
        String CORP_CODE = "";//单位代码
        String BANKACC = "";//银行账号
        String ACC_NAME = "";//账户名称
        String BANK_NAME = "";//开户行名称
        String BANK_TYPE = "";//银行类别
        String ACC_CATEGORY = "";//账户性质
        String ACC_TYPE1 = "";//账户类别
        String ACC_TYPE = "";//账户收支属性
        String CUR = "";//币种
        String IS_ONLINE = "";//账户是否上线
        String ACC_STATE = "";//账户状态
        String REGISTER_DATE = "";//开户日期
        String OP_RESSION = "";//开户说明
        String sql = "";
        if(!"".equals(tableName)) {
            sql = "select * from " + tableName + " where requestid = " + requestid;
            rs.execute(sql);
            if(rs.next()){
                CORP_CODE = Util.null2String(rs.getString("gsdm"));
                BANKACC = Util.null2String(rs.getString("yhzh"));
                ACC_NAME = Util.null2String(rs.getString("zhmc"));
                BANK_NAME = Util.null2String(rs.getString("lhh"));
                BANK_TYPE = Util.null2String(rs.getString("yhlb"));
                ACC_CATEGORY = Util.null2String(rs.getString("zhxz"));
                ACC_TYPE1 = Util.null2String(rs.getString("zhlb"));
                ACC_TYPE = Util.null2String(rs.getString("zhsvsx"));
                CUR = Util.null2String(rs.getString("bb"));
                IS_ONLINE = Util.null2String(rs.getString("sfsx"));
                ACC_STATE = Util.null2String(rs.getString("sqlx"));
                ACC_STATE = tran.getAccounttype(ACC_STATE);
                REGISTER_DATE = Util.null2String(rs.getString("sqrq"));
                OP_RESSION = Util.null2String(rs.getString("jtsy"));

            }
            JSONObject head = new JSONObject();
            JSONArray jsonarray = new JSONArray();
            JSONObject jsonObjSon = new JSONObject();
            try {
                
                jsonObjSon.put("corp_code",CORP_CODE);
                jsonObjSon.put("bankacc",BANKACC);
                jsonObjSon.put("acc_name",ACC_NAME);
                jsonObjSon.put("bank_name",BANK_NAME);
                jsonObjSon.put("bank_type",BANK_TYPE);
                jsonObjSon.put("acc_category",ACC_CATEGORY);
                jsonObjSon.put("acc_type1",ACC_TYPE1);
                jsonObjSon.put("acc_type",ACC_TYPE);
                jsonObjSon.put("cur",CUR);
                jsonObjSon.put("is_online",IS_ONLINE);
                jsonObjSon.put("acc_state",ACC_STATE);
                jsonObjSon.put("register_date",REGISTER_DATE);
                jsonObjSon.put("op_ression",OP_RESSION);
                jsonarray.put(jsonObjSon);
                head.put("bean", jsonarray);
            }catch (JSONException e) {
               log.writeLog(e.getMessage());
            }
            BankXmlUtil bankXmlUtil = new BankXmlUtil();
            String json = bankXmlUtil.javaToXml("","",requestid,head.toString());
            log.writeLog("打印json————————" + json);
            String SIGN = "";//状态
            String MESSAGE = "";//提示信息
            try {
				Response result = getResultMethod(json);
				SIGN = result.getSIGN();
				MESSAGE = result.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "message";
			Map<String, Object> result = saxXmlUtil.getXmlMap(MESSAGE);
            Object mess = result.get(para);//提示信息
            if(mess.toString().length() >=50 ){
                mess = mess.toString().substring(0,50);
            }
            log.writeLog("状态和消息------"+SIGN +"," +mess);
            String sql_update = "update " + tableName + " set status = '" + SIGN + "', message = '" + mess + "' where requestid = " + requestid ;
            rs.execute(sql_update);
        }else{
            log.writeLog("流程表信息获取失败!");
            return "-1";
        }
        return SUCCESS;
    }
}
 
