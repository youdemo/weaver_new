package wldq.contract;


import java.util.HashMap;
import java.util.Map;


import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import wldq.contract.template.CreateTemplate;


public class CreateContractTemple2 implements Action {

    @Override
    public String execute(RequestInfo info) {
        BaseBean log = new BaseBean();
        RecordSet rs = new RecordSet();
        String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
        String requestid = info.getRequestid();
        String creator = "";
        String tableName = "";
        String sql = "";
        String docCategory = "";
        sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select creater from workflow_requestbase where requestid='"+requestid+"'";
        rs.executeSql(sql);
        if(rs.next()) {
        	creator = Util.null2String(rs.getString("creater"));
        }
		sql = " select DOCCATEGORY from WORKFLOW_BASE where ID=" + workflowID;
        rs.executeSql(sql);
        if (rs.next()) {
            String docCategories = Util.null2String(rs.getString("DOCCATEGORY"));
            String dcg[] = docCategories.split(",");
            docCategory = dcg[dcg.length-1];
        }
        writeLog("docCategory:" + docCategory);
        

        //主表同步字段
        String mainID = "";
        String sqrq = "";//申请日期
        String htbh = ""; // 合同编号
        String htmc = "";//合同名称
        String htdx = ""; // 合同对象
        String htjk = ""; // 合同价款

        sql = "select * from " + tableName + " where requestid=" + requestid;
        writeLog("流程主数据：" + sql);
        rs.executeSql(sql);
        if (rs.next()) {
            mainID = Util.null2String(rs.getString("id"));//
            sqrq = Util.null2String(rs.getString("sqrq"));
            htbh = Util.null2String(rs.getString("htbh"));
            htmc = Util.null2String(rs.getString("htmc"));
            htdx = Util.null2String(rs.getString("htdx"));
            htjk = Util.null2String(rs.getString("htjk"));
            
        }
       

            Map<String, String> mapStr = new HashMap<String, String>();
            mapStr.put("sqrq", sqrq);
            mapStr.put("htbh", htbh);
            mapStr.put("htmc", htmc);
            mapStr.put("htdx", "");
            sql = "select name from CRM_CustomerInfo where id="+htdx;
            rs.executeSql(sql);
            if(rs.next()) {
            	mapStr.put("htdx", Util.null2String(rs.getString("name")));
            }
            
            mapStr.put("htjk", htjk);
            mapStr.put("docCreator", creator);
            writeLog("docCategory:123");
            CreateTemplate ccf = new CreateTemplate();
            writeLog("docCategory:124");
            String docid = ccf.CreateFile(mapStr, docCategory,tableName,mainID,requestid);
            if (!"-1".equals(docid)) {
                sql = "update " + tableName + " set pdffj='" + docid + "' where requestid=" + requestid;
                writeLog("更新流程合同文档字段：" + sql);
                rs.executeSql(sql);
            }
        
 
        return SUCCESS;
    }

    /**
     * 开发技巧，一键开关日志，if(true)开启；if(false)关闭
     *
     * @param obj
     */
    private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }

}
