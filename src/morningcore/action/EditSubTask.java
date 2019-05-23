package morningcore.action;

import morningcore.util.GetModeidUtil;
import morningcore.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.HashMap;
import java.util.Map;

import static morningcore.util.GetModeidUtil.getModeId;

public class EditSubTask implements Action{
    @Override
    public String execute(RequestInfo requestInfo){
        String workflowid = requestInfo.getWorkflowid();
        String requestid = requestInfo.getRequestid();
        RecordSet rs = new RecordSet();
        BaseBean log = new BaseBean();
        RecordSet res = new RecordSet();
        GetModeidUtil du = new GetModeidUtil();
        String billtablename = du.getBillTableName("ZRW");
        String subbilltablename = du.getBillTableName("DBZRW");
        String modeid = getModeId(subbilltablename);
        String tablename = "";
        String sql = "select tablename from workflow_bill where id=" + "(select formid from workflow_base where id=" + workflowid + ")";
        rs.execute(sql);
        if(rs.next()){
            tablename = Util.null2String(rs.getString("tablename"));
        }
//  zrwms	子任务描述 wcrq 完成日期	zbbm	承办部门信息 	zrwfzr	子任务负责人
//  rwzt	任务状态 	cbbm	承办部门 	zrwcz	子任务权重	xgzt	修改状态 	mxid	明细id
        String mxid = "";
        String mainid = "";
        String dtid = "";
        String sql_dt = "";
        String zrwms = "";
        String wcrq = "";//
        String zbbm = "";//
        String zrwfzr = "";//
        String rwzt = "";//
        String cbbm = "";//
        String zrwcz = "";//
        String xgzt = "";//
        String rwlx = "";
        String fqrq = "";
        String kssj = "";
        String jssj = "";
        String sqsm = "";
        sql = "select * from " + tablename + " where requestid = " + requestid;
        rs.execute(sql);
        if(rs.next()){
            mainid = Util.null2String(rs.getString("id"));
            rwlx =  Util.null2String(rs.getString("rwlx"));
            fqrq =  Util.null2String(rs.getString("sqrq"));
            kssj =  Util.null2String(rs.getString("kssj"));
            jssj =  Util.null2String(rs.getString("jssj"));
            sqsm =  Util.null2String(rs.getString("sqsm"));
        }
        sql = "select * from " + tablename + "_dt1 where mainid=" + mainid;
        rs.execute(sql);
        while(rs.next()){
            dtid = Util.null2String(rs.getString("id"));
            mxid = Util.null2String(rs.getString("mxid"));
            zrwms = Util.null2String(rs.getString("zrwms"));
            wcrq = Util.null2String(rs.getString("wcrq"));
            zrwfzr = Util.null2String(rs.getString("zrwfzr"));
            rwzt = Util.null2String(rs.getString("rwzt"));
            cbbm = Util.null2String(rs.getString("cbbm"));
            zrwcz = Util.null2String(rs.getString("zrwcz"));
            xgzt = Util.null2String(rs.getString("xgzt"));
//  rwlx	任务类型 	fqrq	发起日期 	kssj	开始时间
// 	jssj	结束时间
// 	zrwfzr	主任务负责人
// 	zrwms	主任务描述
// 	dbzrwms	子任务描述
//  zrwqz 子任务权重
// 	wcrq	完成日期
// 	cbbm	承办部门
// 	dbzrwfzr	子任务负责人
// 	rwzt	任务状态
// 	zrwmxid	主任务明细id
// 	fklcid	触发反馈流程id
//  spzt 审批状态
// 	wcl	完成率
//  xgzt 修改状态
// 	zrwpy	主任务负责人评语
            int no = 0;
            sql_dt = "select count(1) as no from " + subbilltablename + " where zrwmxid=" + mxid;
            res.execute(sql_dt);
            if(res.next()){
                no = res.getInt("no");
            }
            if(no == 0){
                sql_dt = "update " + subbilltablename + " set zrwms='" + zrwms + "',wcrq='" + wcrq + "'," +
                        "cbbm='" + cbbm + "',dbzrwfzr='" + zrwfzr + "',rwzt='" + rwzt + "',zrwqz='" + zrwcz +
                        "',xgzt='" + xgzt + "',zrwmxid=" + dtid + " where zrwmxid=" + mxid;
                res.execute(sql_dt);
            }else{
                Map map = new HashMap();
                map.put("rwlx",rwlx);
                map.put("fqrq",fqrq);
                map.put("kssj",kssj);
                map.put("jssj",jssj);
                map.put("zrwfzr",zrwfzr);
                map.put("zrwms",sqsm);
                map.put("dbzrwms",zrwms);
                map.put("zrwqz",zrwcz);
                map.put("wcrq",wcrq);
                map.put("cbbm",cbbm);
                map.put("dbzrwfzr",zrwfzr);
                map.put("rwzt",rwzt);
                map.put("zrwmxid",dtid);
                map.put("xgzt",xgzt);
                InsertUtil tu = new InsertUtil();
                boolean result = tu.insert(map, subbilltablename);
                log.writeLog("result:" + result);
                if (!"".equals(dtid)) {
                    String billid = "";
                    sql_dt = "select id from " + subbilltablename + " where zrwmxid ="+dtid;
                    res.execute(sql_dt);
                    if(res.next()){
                        billid = Util.null2String(res.getString("id"));
                    }
                    ModeRightInfo ModeRightInfo = new ModeRightInfo();
                    ModeRightInfo.editModeDataShare(Integer.valueOf(1), Integer.valueOf(modeid),
                            Integer.valueOf(billid));

                }
            }

        }

        return SUCCESS;
    }
}
