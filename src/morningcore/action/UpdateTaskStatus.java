package morningcore.action;

import morningcore.util.GetModeidUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UpdateTaskStatus extends BaseCronJob{
    /**
     * 同步考勤汇总表
     * 写到中间表 uf_attendlist
     * 0 0 2 1 * ?
     */
    public void execute(){
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        BaseBean log = new BaseBean();
        GetModeidUtil du = new GetModeidUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String maintablename = du.getBillTableName("ZRW");//主任务主表
        String subbilltable = du.getBillTableName("DBZRW");//子任务主表
        String sql = "";
        String zrwmxid = "";
        sql = "select * from " + subbilltable + " where jssj<'" + nowDate + "' and rwzt!=4 and rwzt!=3";
        rs.execute(sql);
        log.writeLog("sql3=" + sql);
        while(rs.next()){
            zrwmxid = Util.null2String(rs.getString("mxid"));
            String sql_dt = "update " + maintablename + "_dt1 set rwzt=2 where mxid=" + zrwmxid;
            res.execute(sql_dt);
            log.writeLog("sql_dt=" + sql_dt);
            sql_dt = "update " + subbilltable + " set rwzt=2 where mxid=" + zrwmxid;
            res.execute(sql_dt);
            log.writeLog("sql_dt2=" + sql_dt);
        }
        sql = "update " + maintablename + " set rwzt=2 where jssj<'" + nowDate + "' and rwzt!=4 and rwzt!=3";
        rs.execute(sql);
        log.writeLog("sql1=" + sql);
    }
}
