package morningcore.attendSysn;

import morningcore.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static morningcore.util.GetModeidUtil.getModeId;

public class AttendDataSynchron extends BaseCronJob{
    public void execute(){
        attendDataSysn("auto");
    }


    /**
     * 同步打卡记录
     * 写到中间表 uf_signdata
     * 更新考勤中间表
     */
    public static void attendDataSysn(String type){
        boolean result = true;
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        Date da = new Date();
        RecordSet res = new RecordSet();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateNowStr = sdf.format(da);
        String modeid = getModeId("uf_signdata");
        String billid = "";
        RecordSetDataSource rsd = new RecordSetDataSource("hr");// 链接数据源
        RecordSet rs = new RecordSet();
        String workcode = "";
        String sbsj = "";
        String sql = "";
        String sql_del = "";
        if("auto".equals(type)){
            if("04:00:00".equals(dateNowStr.substring(11,19))){
                sql = "select a.checktime,b.badgenumber,b.name from CHECKINOUT a,UserInfo b " +
                        "where a.userid=b.userid and convert(varchar,checktime)) " +
                        "<= CONVERT(varchar(100),GETDATE(), 23)+' '+'04:00:00' " +
                        "and convert(varchar,checktime)>=" +
                        "CONVERT(varchar(100), DATEADD(DAY,-1,GETDATE()) , 23)+' '+'11:00:00'";
            }else{
                sql = "select a.checktime,b.badgenumber,b.name from CHECKINOUT a,UserInfo b " +
                        "where a.userid=b.userid and convert(varchar,checktime)) " +
                        "< CONVERT(varchar(100),GETDATE(), 23)+' '+'11:00:00' " +
                        "and convert(varchar,checktime)>" +
                        "CONVERT(varchar(100), GETDATE() , 23)+' '+'04:00:00'";
            }
            rsd.executeSql(sql);
            while(rsd.next()){
                workcode = Util.null2String(rsd.getString("badgenumber"));
                sbsj = Util.null2String(rsd.getString("checktime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = null;//有异常要捕获
                try{
                    date = format.parse(sbsj);
                }catch(ParseException e){
                    e.printStackTrace();
                }
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dkshj = format.format(date);
                String dkrq = "";
                String dksj = "";
                String dkny = "";
                String dknf = "";
                String dkyf = "";
                if(!"".equals(dkshj)){
                    dkrq = dkshj.substring(0,10);
                    dksj = dkshj.substring(11,19);
                    dkny = dkshj.substring(0,7);
                    dknf = dkshj.substring(0,4);
                    dkyf = dkshj.substring(5,6);
                }
                long timestamp = System.currentTimeMillis();
                Map map = new HashMap();
                map.put("rygh",workcode);
                map.put("dkrq",dkrq);
                map.put("dksj",dksj);
                map.put("dkny",dkny);
                map.put("dknf",dknf);
                map.put("dkyf",dkyf);
                map.put("tbsj",timestamp);
                map.put("modedatacreatedate",nowDate);
                map.put("modedatacreater",1);//
                map.put("modedatacreatertype","0");
                map.put("formmodeid",modeid);
                InsertUtil tu = new InsertUtil();
                result = tu.insert(map,"uf_signdata");
                String sql_dt = "select id from uf_signdata where tbsj='" + timestamp + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    billid = Util.null2String(res.getString("id"));
                }
                String ryid = "";
                sql_dt = "select * from hrmresource where workcode = '" + workcode + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    ryid = Util.null2String(res.getString("id"));
                }
                if(!"".equals(billid)){
                    ModeRightInfo ModeRightInfo = new ModeRightInfo();
                    ModeRightInfo.editModeDataShare(Integer.valueOf(ryid),Integer.valueOf(modeid),Integer.valueOf(billid));

                }
            }
        }else{
            sql_del = "delete * from uf_signdata where tbsj " +
                    "<= CONVERT(varchar(100),GETDATE(), 23)+' '+'04:00:00' " +
                    "and tbsj>=CONVERT(varchar(100), DATEADD(DAY,-1,GETDATE()) , 23)+' '+'04:00:00'";
            rs.executeSql(sql_del);
            sql = "select a.checktime,b.badgenumber,b.name from CHECKINOUT a,UserInfo b " +
                    "where a.userid=b.userid and convert(varchar,checktime)) " +
                    "<= CONVERT(varchar(100),GETDATE(), 23)+' '+'04:00:00' " +
                    "and convert(varchar,checktime)>=" +
                    "CONVERT(varchar(100),DATEADD(DAY,-1,GETDATE()), 23)+' '+'04:00:00'";
            // select a.* from CHECKINOUT a,UserInfo b where a.userid=b.userid
            rsd.executeSql(sql);
            while(rsd.next()){
                workcode = Util.null2String(rsd.getString("badgenumber"));
                sbsj = Util.null2String(rsd.getString("checktime"));
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date = null;//有异常要捕获
                try{
                    date = format.parse(sbsj);
                }catch(ParseException e){
                    e.printStackTrace();
                }
                format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dkshj = format.format(date);
                String dkrq = "";
                String dksj = "";
                String dkny = "";
                String dknf = "";
                String dkyf = "";
                long timestamp = System.currentTimeMillis();
                if(!"".equals(dkshj)){
                    dkrq = dkshj.substring(0,10);
                    dksj = dkshj.substring(11,19);
                    dkny = dkshj.substring(0,7);
                    dknf = dkshj.substring(0,4);
                    dkyf = dkshj.substring(5,6);
                }
                Map map = new HashMap();
                map.put("rygh",workcode);
                map.put("dkrq",dkrq);
                map.put("dksj",dksj);
                map.put("dkny",dkny);
                map.put("dknf",dknf);
                map.put("dkyf",dkyf);
                map.put("tbsj",timestamp);
                map.put("modedatacreatedate",nowDate);
                map.put("modedatacreater",1);//
                map.put("modedatacreatertype","0");
                map.put("formmodeid",modeid);
                InsertUtil tu = new InsertUtil();
                result = tu.insert(map,"uf_signdata");
                String sql_dt = "select id from uf_signdata where tbsj='" + timestamp + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    billid = Util.null2String(res.getString("id"));
                }
                String ryid = "";
                sql_dt = "select * from hrmresource where workcode = '" + workcode + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    ryid = Util.null2String(res.getString("id"));
                }
                if(!"".equals(billid)){
                    ModeRightInfo ModeRightInfo = new ModeRightInfo();
                    ModeRightInfo.editModeDataShare(Integer.valueOf(ryid),Integer.valueOf(modeid),Integer.valueOf(billid));

                }
            }
        }
        String code = "";
        String sbk = "";
        String xbk = "";
        sql = "select * from uf_attendlist where kqrq='" + dateNowStr.substring(0,10) + "'";
        rs.executeSql(sql);
        while(rs.next()){
            code = Util.null2String(rs.getString("code"));
            int no = 0;
            String sql_dt = "select count(1) as no from uf_signdata where rygh='" + code + "' and dkrq ='" + dateNowStr.substring(0,10) + "'";
            res.executeSql(sql_dt);
            if(res.next()){
                no = res.getInt("no");
            }
            if(no == 1){
                sql_dt = "select * from uf_signdata where rygh='" + code + "' and dkrq ='" + dateNowStr.substring(0,10) + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    String dksj = Util.null2String(rs.getString("dksj"));
                    if(dksj.compareTo("14:00") > 0){
                        sbk = dksj;
                    }else{
                        xbk = dksj;
                    }
                }
            }else if(no > 1){
                sql_dt = "select max(dksj) as xbk,min(dksj) as sbk from uf_signdata where rygh='" + code + "' and dkrq ='" + dateNowStr.substring(0,10) + "'";
                res.executeSql(sql_dt);
                if(res.next()){
                    sbk = Util.null2String(res.getString("sbk"));
                    xbk = Util.null2String(res.getString("xbk"));
                }
            }
            String kssj = "";
            String jssj = "";
            sql_dt = "update uf_attendlist set sbsj='" + sbk + "',xbsj='" + xbk + "' where code='" + code + "' and kqrq='" + dateNowStr.substring(0,10) + "'";
            res.executeSql(sql_dt);
            sql_dt = "select max(qjjssj) as xbk,min(qjkssj) as sbk from uf_outdoor where fqrgh = '" + code + "' and qjrq = '" + dateNowStr.substring(0,10) + "'";
            res.executeSql(sql_dt);
            if(res.next()){
                kssj = Util.null2String(res.getString("sbk"));
                jssj = Util.null2String(res.getString("xbk"));
            }
            sql_dt = "update uf_attendlist set kssj='" + kssj + "',jssj='" + jssj + "' where code='" + code + "' and kqrq='" + dateNowStr.substring(0,10) + "'";
            res.executeSql(sql_dt);
        }
    }
}
