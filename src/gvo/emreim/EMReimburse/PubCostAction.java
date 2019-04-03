package gvo.emreim.EMReimburse;

import gvo.emreim.EMReimWebService;
import gvo.emreim.EmXmlUtil;
import gvo.emreim.SAPHR_FI_0_EmCostReimbursementService_pttBindingQSServiceStub.Response;
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

public class PubCostAction extends BaseBean implements Action {
    BaseBean log = new BaseBean();
    public String execute(RequestInfo info) {
        log.writeLog("进入公共费用分摊报销流程NoConPubAction————————");
        String requestid = info.getRequestid();//请求ID
        String workflowID = info.getWorkflowid();//流程ID
        String sql = "";
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        String tableName = "";//表名
        String tableNamedt = "";//明细表名
        String mainID = "";//主表ID
        String CORPCODE = "";//公司代码
        String REQCODE = "";//报销人编码
        String CURRTYPE_DES = "";//币别
        String FYBXSM = "";//费用报销说明
        String FLOWNO = "";//单据编号
        String REQNAME = "";//报销人姓名
        String VOUCHERTYPE = "ZE";//凭证类型
        String VOUCHERSPUN = "X";//是否抛转
        String sapReturnStatus = "";

        sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = " + workflowID + ")";
        rs.execute(sql);
//        log.writeLog(sql);
        if(rs.next()){
            tableName = Util.null2String(rs.getString("tablename"));
        }
        if(!"".equals(tableName)){
            tableNamedt = tableName + "_dt1";
            JSONArray Array1 = new JSONArray();
            JSONArray Array = new JSONArray();
            JSONObject head = new JSONObject();
            //查询主表
            sql = "select * from " + tableName + " where requestid=" + requestid;
//            log.writeLog("tableNamedt1=" + tableNamedt);
            rs.execute(sql);
            if(rs.next()) {
            	REQCODE = getWorkcode(res,Util.null2String(rs.getString("bxr")));
                mainID = Util.null2String(rs.getString("id"));
                CORPCODE = Util.null2String(rs.getString("corpcode"));
                CURRTYPE_DES = Util.null2String(rs.getString("currtype_des"));
                FYBXSM = Util.null2String(rs.getString("fybxsm"));
                FLOWNO = Util.null2String(rs.getString("flowno"));
                REQNAME = Util.null2String(rs.getString("reqname"));
                log.writeLog("查看REQNAME————————" + REQNAME);
                sapReturnStatus = Util.null2String(rs.getString("sapReturnStatus"));
            }
            if("S".equals(sapReturnStatus)){
            	return SUCCESS;
            }
            //查询明细表1
            sql = "select * from " + tableNamedt + " where mainid= " + mainID;
            rs.execute(sql);
//            log.writeLog("开始————————" + sql);
            while(rs.next()){
            	JSONObject json1 = new JSONObject();
                String ACCOUNTCODE = "40";//记账代码
                String FYKMBM = Util.null2String(rs.getString("fykmbm"));//费用科目编码
                String CBZXBM = Util.null2String(rs.getString("cbzxbm"));//成本中心编码
                String EXPENSEAMT = Util.null2String(rs.getString("noTax"));//未税金额
//                log.writeLog("查看EXPENSEAMT————————" + EXPENSEAMT);
                String TAX = Util.null2String(rs.getString("tax"));//税额
//                log.writeLog("查看TAX————————" + TAX);
                try {
                    json1.put("ACCOUNTCODE", ACCOUNTCODE);
                    json1.put("FYKMBM", FYKMBM);
                    json1.put("CBZXBM", CBZXBM);
                    json1.put("EXPENSEAMT", EXPENSEAMT);
                    json1.put("TAX", TAX);
                    json1.put("FYBXSM", FYBXSM);
                    json1.put("REQNAME", REQNAME);
                    json1.put("LWFLOWNO", FYBXSM);
                    Array1.put(json1);
                }catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            try{
                head.put("CORPCODE",CORPCODE);
                head.put("REQCODE",REQCODE);
                head.put("CURRTYPE_DES",CURRTYPE_DES);
                head.put("FYBXSM",FYBXSM);
                head.put("FLOWNO",FLOWNO);
                head.put("REQNAME",REQNAME);
                head.put("VOUCHERTYPE",VOUCHERTYPE);
                head.put("VOUCHERSPUN",VOUCHERSPUN);
                head.put("CHILD_LIST_TB2", Array1);
            }catch (JSONException e){
                e.printStackTrace();
            }
            Array.put(head);
            EmXmlUtil em = new EmXmlUtil();
            String json = em.javaToXml(Array.toString(),"",requestid,"");
//            log.writeLog("查看json格式————————" + json);
            EMReimWebService ems = new EMReimWebService();
            String sign = "";
            String message = "";
			try {
			 	Response result = ems.getResultMethod(json);
	            sign = result.getSIGN();
	            message = result.getMessage();
	            log.writeLog("返回结果sign————————" + sign);
                log.writeLog("返回结果message————————" + message);
			} catch (Exception e) {
                log.writeLog("错误日志----" + e.getMessage());
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
            String para = "E_BELNR";//会计凭证编号
            String para1 = "E_MSGTX";//消息文本
            Map<String, Object> result = saxXmlUtil.getXmlMap(message);
            Object SAPRETURNNO = result.get(para);//SAP回写凭证号
            Object SAPRETURNEMSG = result.get(para1);//SAP回写错误消息
//            log.writeLog("回写凭证号————————" + SAPRETURNNO);
//            log.writeLog("取出错误消息————————" + SAPRETURNEMSG);
            String sql_update = "update " + tableName + " set sapReturnStatus='"+ sign + "',sapReturnEMsg='" + SAPRETURNEMSG + "',sapReturnNo='" + SAPRETURNNO + "' where requestid=" + requestid;
            res.execute(sql_update);
//            log.writeLog("错误信息————————" + sql_update);
        }else {
            return "-1";
        }
        return SUCCESS;
    }
    //查询数据库，返回报销人编号
  	private String getWorkcode(RecordSet res,String hrmid) {
  		String code = "";
  		String sql = "select workcode from hrmresource where id=" + hrmid;
  		res.execute(sql);
  		if(res.next()){
  			code = Util.null2String(res.getString("workcode"));
  		}
  		return code;
  	}
}
