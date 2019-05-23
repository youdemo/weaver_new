package morningcore.action;

import morningcore.util.GetModeidUtil;
import weaver.conn.RecordSet;
import weaver.formmode.customjavacode.AbstractModeExpandJavaCode;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;


/**
 * 说明
 * 修改时
 * 类名要与文件名保持一致
 * class文件存放位置与路径保持一致。
 * 请把编译后的class文件，放在对应的目录中才能生效
 * 注意 同一路径下java名不能相同。
 *
 * @author Daisy
 */
public class UpdateFeedbackInfo extends AbstractModeExpandJavaCode{
    /**
     * 执行模块扩展动作
     *
     * @param param param包含(但不限于)以下数据
     *              user 当前用户
     */
    public void doModeExpand(Map<String,Object> param) throws Exception{
        User user = (User)param.get("user");
        int billid = -1;//数据id
        int modeid = -1;//模块id
        RequestInfo requestInfo = (RequestInfo)param.get("RequestInfo");
        if(requestInfo != null){
            billid = Util.getIntValue(requestInfo.getRequestid());
            modeid = Util.getIntValue(requestInfo.getWorkflowid());
            BaseBean log = new BaseBean();
            log.writeLog("modeid=" + modeid + ",billid=" + billid);
            if(billid > 0 && modeid > 0){
                RecordSet rs = new RecordSet();
                GetModeidUtil du = new GetModeidUtil();
                String maintablename = du.getBillTableName("ZRW");//主任务主表
                String subbilltable = du.getBillTableName("DBZRW");//子任务主表
                String mainid = "";//子任务库主表id
                String mxid = "";//子任务库中对应流程明细id，主任务明细表中对应流程明细id
                String zrwid = "";//主任务主表id
                String fksm = "";//反馈说明
                String wcl = "";//完成率
                String zrwqz = "";//子任务权重
                String sql = "select * from " + subbilltable + " where id=" + billid;
                rs.execute(sql);
//                log.writeLog("sql1=" + sql);
                if(rs.next()){
                    mainid = Util.null2String(rs.getString("id"));
                    mxid = Util.null2String(rs.getString("zrwmxid"));
                    wcl = Util.null2String(rs.getString("wcl"));
                    zrwqz = Util.null2String(rs.getString("zrwqz"));
                }
                sql = "select * from " + maintablename + "_dt1 where mxid = " + mxid;
                rs.execute(sql);
//                log.writeLog("sql2=" + sql);
                if(rs.next()){
                    zrwid = Util.null2String(rs.getString("mainid"));
                }
                int no = 0;
                sql = "select count(1) as no from " + subbilltable + "_dt1 where mainid=" + mainid;
                rs.execute(sql);
//                log.writeLog("sql3=" + sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                if(no > 0){
                    sql = "update " + subbilltable + " set rwzt=1 where id=" + billid;
                    rs.execute(sql);
//                    log.writeLog("sql4=" + sql);
                    sql = "update " + maintablename + " set rwzt=1 where id=" + zrwid;
                    rs.execute(sql);
//                    log.writeLog("sql5=" + sql);
                    sql = "update " + maintablename + "_dt1 set rwzt=1 where mxid=" + mxid;
                    rs.execute(sql);
//                    log.writeLog("sql6=" + sql);
                }
                sql = "select * from " + subbilltable + "_dt1 where id=(select max(id) from " + subbilltable + "_dt1 where mainid=" + billid + ")";
                rs.execute(sql);
//                log.writeLog("sql7=" + sql);
                if(rs.next()){
                    fksm = Util.null2String(rs.getString("fksm"));
//                    wcl = Util.null2String(rs.getString("wcl"));
                }
                sql = "update " + maintablename + "_dt1 set rwfk = '" + fksm + "' where mxid = " + mxid;
                rs.execute(sql);
                sql = "update " + maintablename + " set zrwwcl = (select sum(zrwwcl*zrwcz) from uf_zrwk_dt1 where mainid=" + zrwid + ") where id = " + zrwid;
                rs.execute(sql);
//                log.writeLog("sql8=" + sql);
            }
        }
    }

}