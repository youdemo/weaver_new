package morningcore.action;

import morningcore.util.GetModeidUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class MainTaskFeedback implements Action{
    @Override
    public String execute(RequestInfo requestInfo){
        String workflowid = requestInfo.getWorkflowid();
        String requestid = requestInfo.getRequestid();
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        GetModeidUtil du = new GetModeidUtil();
        String billtablename = du.getBillTableName("ZRW");
        String subbilltablename = du.getBillTableName("DBZRW");
        String tablename = "";
        String sql = "select tablename from workflow_bill where id=" +
                "(select formid from workflow_base where id=" + workflowid + ")";
        rs.executeSql(sql);
        if(rs.next()){
            tablename = Util.null2String(rs.getString("tablename"));
        }
        String zrwid = "";
        String zrwmxid = "";
        String mainid = "";
        String sql_dt = "";
        String s = "";//是
        String f = "";//否  勾选数据库存1
        String zjlpy = "";//总经理评语
        String zrwfzrpj = "";//主任务负责人评论
        sql = "select * from " + tablename + " where requestid = " + requestid;
        rs.executeSql(sql);
        if(rs.next()){
            mainid = Util.null2String(rs.getString("id"));
            zrwid = Util.null2String(rs.getString("zrwid"));
            s = Util.null2String(rs.getString("s"));
            f = Util.null2String(rs.getString("f"));
            zjlpy = Util.null2String(rs.getString("zjlpy"));
        }
        if("1".equals(s)){
            sql_dt = "update " + billtablename + " set zjlpy='" + zjlpy + "',spzt=2 where id=" + zrwid;
            res.executeSql(sql_dt);
        }
        if("1".equals(f)){
            sql_dt = "update " + billtablename + " set zjlpy='" + zjlpy + "',spzt=1 where id=" + zrwid;
            res.executeSql(sql_dt);
        }
        sql = "select * from " + tablename + "_dt1 where mainid=" + mainid;
        rs.executeSql(sql);
        while(rs.next()){
            zrwfzrpj = Util.null2String(rs.getString("zrwfzrpj"));
            zrwmxid = Util.null2String(rs.getString("zrwmxid"));
            sql_dt = "update " + billtablename + "_dt1 set zrwfzrpj='" + zrwfzrpj + "' where mxid=" + zrwmxid;
            res.executeSql(sql_dt);
            sql_dt = "update " + subbilltablename + " set zrwpy='" + zrwfzrpj + "' where zrwmxid=" + zrwmxid;
            res.executeSql(sql_dt);
        }

        return SUCCESS;
    }
}
