package morningcore.attendSysn;

import morningcore.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static morningcore.util.AttendUtils.getMonthFullDay;
import static morningcore.util.GetModeidUtil.getModeId;

public class InsertTotalData extends BaseCronJob {
    BaseBean log = new BaseBean();
    /**
     * 同步考勤汇总表
     * 写到中间表 uf_attendlist
     * 0 0 2 1 * ?
     */
    public void execute() {
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        Calendar cale = null;
        cale = Calendar.getInstance();
        int year = cale.get(Calendar.YEAR);
        int month = cale.get(Calendar.MONTH) + 1;
        List<String> list = getMonthFullDay(year, month);
        log.writeLog("list:" + list);
        for (int i = 0; i < list.size(); i++) {
            String qdrq = list.get(i);
            String sql = "select * from hrmresource where status<>5";
            rs.executeSql(sql);
            while (rs.next()) {
                String id = Util.null2String(rs.getString("id"));
                String lastname = Util.null2String(rs.getString("lastname"));
                String code = Util.null2String(rs.getString("workcode"));
                String loginid = Util.null2String(rs.getString("loginid"));
                String managerid = Util.null2String(rs.getString("managerid"));
                String departmentid = Util.null2String(rs.getString("departmentid"));
                String managercode = "";
                String supdepid = "";
                String depid = "";
                Map<String,String> map = new HashMap<String,String>();
                String sql_dt = "select * from hrmresource where id =" + managerid;
                res.executeSql(sql_dt);
                if (res.next()) {
                    managercode = Util.null2String(rs.getString("workcode"));
                }
                sql_dt = "select * from hrmdepartment where id=" + departmentid;
                res.executeSql(sql_dt);
                if (res.next()) {
                    supdepid = Util.null2String(rs.getString("supdepid"));
                }
                if ("".equals(supdepid)) {
                    map.put("supdept", departmentid);
                    map.put("dept", "");
                } else {
                    map.put("supdept", supdepid);
                    map.put("dept", departmentid);
                }
                String gzr = "";
                map.put("name", id);
                map.put("code", code);
                map.put("loginid", loginid);

                map.put("managerid", managerid);
                map.put("managercode", managercode);
                map.put("kqrq", qdrq);
                String sql_gzr = "select dbo.sh_what_holiday('" + qdrq + "') as sfgzr";
                res.executeSql(sql_gzr);
                if (res.next()) {
                    gzr = Util.null2String(res.getString("sfgzr"));
                }
                // 1:公休日2:工作日 3 休息日
                if ("1".equals(gzr) || "3".equals(gzr)) {
                    map.put("sfgzr", "1");
                } else {
                    map.put("sfgzr", "0");
                }
                String modeid = getModeId("uf_attendlist");
                String billid = "";
                SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
                SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
                String nowDate = dateFormate.format(new Date());
                map.put("kqny", qdrq.substring(0, 7));
                map.put("kqyf", qdrq.substring(5, 7));
                map.put("kqnf", qdrq.substring(0, 4));
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreater", id);//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                log.writeLog("map:" + map);
                InsertUtil tu = new InsertUtil();
                boolean result = tu.insert(map, "uf_attendlist");
                log.writeLog("result:" + result);
                sql_dt = "select id from uf_attendlist where name='" + id + "' and kqrq = '" + qdrq + "'";
                res.executeSql(sql_dt);
                if (rs.next()) {
                    billid = Util.null2String(res.getString("id"));
                }
                if (!"".equals(billid)) {
                    ModeRightInfo ModeRightInfo = new ModeRightInfo();
                    ModeRightInfo.editModeDataShare(Integer.valueOf(id), Integer.valueOf(modeid),
                            Integer.valueOf(billid));

                }
            }
        }
    }

}
