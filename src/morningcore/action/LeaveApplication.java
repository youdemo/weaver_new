package morningcore.action;

import morningcore.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static morningcore.util.AttendUtils.getPerDaysByStartAndEndDate;
import static morningcore.util.GetModeidUtil.getModeId;

public class LeaveApplication implements Action{
    @Override
    public String execute(RequestInfo requestInfo){
        String workflowid = requestInfo.getWorkflowid();
        String requestid = requestInfo.getRequestid();
        String tablename = "";
        String sdate = "";
        String edate = "";
        String stime = "";
        String etime = "";
        String fqr = "";
        String fqrgh = "";
        String fqri = "";
        String sqqsrq = "";
        String erjm = "";
        String sjbm = "";
        String ssbm = "";
        String sqrq = "";
        String sqsm = "";
        String lcfj = "";
        String lcbt = "";
        String manager = "";
        String bdbh = "";
        String sqqssj = "";
        String sqjsrq = "";
        String sqjssj = "";
        String qjxs = "";
        String qjlx = "";
        String aqjb = "";
        String sqbm = "";
        String erjbmmc = "";
        String sjbmmc = "";
        String sjdyljxs = "";
        String sjdllxjs = "";
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
            fqrgh = Util.null2String(rs.getString("fqrgh"));
            fqri = Util.null2String(rs.getString("fqri"));
            sqqsrq = Util.null2String(rs.getString("sqqsrq"));
            erjm = Util.null2String(rs.getString("erjm"));
            sjbm = Util.null2String(rs.getString("sjbm"));
            ssbm = Util.null2String(rs.getString("ssbm"));
            sqrq = Util.null2String(rs.getString("sqrq"));
            sqsm = Util.null2String(rs.getString("sqsm"));
            lcfj = Util.null2String(rs.getString("lcfj"));
            lcbt = Util.null2String(rs.getString("lcbt"));
            manager = Util.null2String(rs.getString("manager"));
            bdbh = Util.null2String(rs.getString("bdbh"));
            sqqssj = Util.null2String(rs.getString("sqqssj"));
            sqjsrq = Util.null2String(rs.getString("sqjsrq"));
            sqjssj = Util.null2String(rs.getString("sqjssj"));
            qjxs = Util.null2String(rs.getString("qjxs"));
            qjlx = Util.null2String(rs.getString("qjlx"));
            aqjb = Util.null2String(rs.getString("aqjb"));
            sqbm = Util.null2String(rs.getString("sqbm"));
            erjbmmc = Util.null2String(rs.getString("erjbmmc"));
            sjbmmc = Util.null2String(rs.getString("sjbmmc"));
            sjdyljxs = Util.null2String(rs.getString("sjdyljxs"));
            sjdllxjs = Util.null2String(rs.getString("sjdllxjs"));
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
            Map map = new HashMap();
            map.put("sqny",date.substring(0,7));
            map.put("sqyf",date.substring(5,6));
            map.put("fqr",fqr);
            map.put("erjm",erjm);
            map.put("fqrgh",fqrgh);
            map.put("sjbm",sjbm);
            map.put("fqri",fqri);
            map.put("manager",manager);
            map.put("bdbh",bdbh);
            map.put("sqqsrq",sqqsrq);
            map.put("sqqssj",sqqssj);
            map.put("sqjsrq",sqjsrq);
            map.put("sqjssj",sqjssj);
            map.put("qjkssj",stime);
            map.put("qjjssj",etime);
            map.put("qjxs",qjxs);
            map.put("qjlx",qjlx);
            map.put("qjrq",date);
            map.put("sqnf",date.substring(0,4));
            map.put("rqid",requestid);
            map.put("modedatacreatedate",nowDate);
            map.put("modedatacreater",1);//
            map.put("modedatacreatertype","0");
            map.put("formmodeid",modeid);
            InsertUtil tu = new InsertUtil();
            tu.insert(map,"uf_outdoor");
            String billid = "";
            String sql_dt = "select id from uf_outdoor where rqid='" + requestid + "'";
            rs.executeSql(sql_dt);
            if(rs.next()){
                billid = Util.null2String(rs.getString("id"));
            }
            if(!"".equals(billid)){
                ModeRightInfo ModeRightInfo = new ModeRightInfo();
                ModeRightInfo.editModeDataShare(Integer.valueOf(fqr),Integer.valueOf(modeid),Integer.valueOf(billid));

            }
        }
        return SUCCESS;
    }
}
