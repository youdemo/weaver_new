package morningcore.action;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static morningcore.util.AttendUtils.getPerDaysByStartAndEndDate;
import static morningcore.util.GetModeidUtil.getModeId;

public class LeaveCancelApplication implements Action{
    @Override
    public String execute(RequestInfo requestInfo){
        String workflowid = requestInfo.getWorkflowid();
        String requestid = requestInfo.getRequestid();
        String tablename = "";
        String stime = "";
        String etime = "";
        String fqr = "";
        String sqqsrq = "";
        String sqqssj = "";
        String sqjsrq = "";
        String sqjssj = "";
        String qjlcid = "";//请假流程id
        String sql = "select tablename from workflow_bill where id=" + "(select formid from workflow_base where id=" + workflowid + ")";
        RecordSet rs = new RecordSet();
        rs.executeSql(sql);
        if(rs.next()){
            tablename = Util.null2String(rs.getString("tablename"));
        }
        sql = "select * from " + tablename + " where requestid = " + requestid;
        rs.executeSql(sql);
        if(rs.next()){
            fqr = Util.null2String(rs.getString("fqr"));
            sqqsrq = Util.null2String(rs.getString("sqqsrq"));
            sqqssj = Util.null2String(rs.getString("sqqssj"));
            sqjsrq = Util.null2String(rs.getString("sqjsrq"));
            sqjssj = Util.null2String(rs.getString("sqjssj"));
            qjlcid = Util.null2String(rs.getString("yxjlc"));
        }
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String modeid = getModeId("uf_outdoor");
        List<String> list = getPerDaysByStartAndEndDate(sqqsrq,sqjsrq,"yyyy-MM-dd");
        for(int i = 0;i < list.size();i++){
            String date = list.get(i);
            if(date.equals(sqjsrq) && date.equals(sqqsrq)){
                stime = sqqssj;
                etime = sqjssj;
            }else if(date.equals(sqqsrq)){
                stime = sqqssj;
                etime = "17:00";
            }else if(date.equals(sqjsrq)){
                stime = "09:00";
                etime = sqjssj;
            }else{
                stime = "09:00";
                etime = "17:00";
            }
            sql = "update uf_outdoor set xjkssj='" + stime + "', xjjssj ='" + etime + "',xjlc ='" + requestid + "'  where fqr =" + fqr + " and qjrq = '" + date + "' and rqid = '" + qjlcid + "'";
            rs.executeSql(sql);
        }
        return SUCCESS;
    }
}
