package morningcore.util;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CreateFKWorkFLow{
    /**
     * @param billid  建模数据id
     * @param creater 创建人
     */
    public String CreateMainFK(String billid,String creater){
        RecordSet rs = new RecordSet();
        RecordSet res = new RecordSet();
        BaseBean log = new BaseBean();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        GetModeidUtil du = new GetModeidUtil();
        String wftablename = du.getWfTableName("ZRW");
        String wfid = du.getWfid("ZRW");
        String billtablename = du.getBillTableName("ZRW");
        //主表
        String sqr = "";
        String rwfqrgh = "";
        String erjm = "";
        String sjbm = "";
        String ssbm = "";
        String sqrq = "";
        String sqsm = "";
        String lcfj = "";
        String manager = "";
        String zrwmxid = "";
        String rwlx = "";
        String zrwfzr = "";
        String kssj = "";
        String jssj = "";
        String rwzt = "";
        String zrwwcl = "";
        String zrwsyts = "";
        String zjlqrsfwc = "";
        String rwmc = "";
        String fklcid = "";
//        String zrwms = "";
//        String zrwcz = "";
//        String wcrq = "";
//        String zbbm = "";
//        String cbbm = "";
//        String rwfk = "";
//        String zrwfzrpj = "";
//        String mxid = "";
//        String rwmc = "";
        //明细表
        String zrwms = "";
        String zrwcz = "";
        String wcrq = "";
        String zbbm = "";
        String zrwfzr1 = "";
        String rwzt1 = "";
        String cbbm = "";
        String zrwwcl1 = "";
        String rwfk = "";
        String zrwfzrpj = "";
        String mxid = "";
        String sql = "select * from " + billtablename + " where id = " + billid;
        rs.executeSql(sql);
        log.writeLog("sql1=" + sql);
        if(rs.next()){
            sqr = Util.null2String(rs.getString("sqr"));
            erjm = Util.null2String(rs.getString("erjm"));
            sjbm = Util.null2String(rs.getString("sjbm"));
            ssbm = Util.null2String(rs.getString("ssbm"));
            sqrq = Util.null2String(rs.getString("sqrq"));
            sqsm = Util.null2String(rs.getString("sqsm"));
            lcfj = Util.null2String(rs.getString("lcfj"));
            manager = Util.null2String(rs.getString("manager"));
            rwfqrgh = Util.null2String(rs.getString("rwfqrgh"));

            rwlx = Util.null2String(rs.getString("rwlx"));
            zrwfzr = Util.null2String(rs.getString("zrwfzr"));
            kssj = Util.null2String(rs.getString("kssj"));
            jssj = Util.null2String(rs.getString("jssj"));
            rwzt = Util.null2String(rs.getString("rwzt"));
            zrwwcl = Util.null2String(rs.getString("zrwwcl"));
            zrwsyts = Util.null2String(rs.getString("zrwsyts"));
            zjlqrsfwc = Util.null2String(rs.getString("zjlqrsfwc"));
            rwmc = Util.null2String(rs.getString("rwmc"));
            fklcid = Util.null2String(rs.getString("fklcid"));
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
//        JSONObject node = new JSONObject();
        JSONArray dt1 = new JSONArray();
        String result = "";
        String rqid = "-1";
        try{
            header.put("sqr",sqr);
            header.put("erjm",erjm);
            header.put("sjbm",sjbm);
            header.put("ssbm",ssbm);
            header.put("sqrq",nowDate);
            header.put("sqsm",sqsm);
            header.put("lcfj",lcfj);
            header.put("manager",manager);
            header.put("rwfqrgh",rwfqrgh);
            header.put("rwlx",rwlx);
            header.put("zrwfzr",zrwfzr);
            header.put("kssj",kssj);
            header.put("jssj",jssj);
            header.put("rwzt",rwzt);
            header.put("zrwwcl",zrwwcl);
            header.put("zrwsyts",zrwsyts);
            header.put("zrwid",billid);

        }catch(JSONException e){
            e.printStackTrace();
        }
        sql = "select * from " + billtablename + "_dt1 where mainid = " + billid;
        rs.executeSql(sql);
        log.writeLog("sql2=" + sql);
        while(rs.next()){
            JSONObject node = new JSONObject();
            zrwmxid = Util.null2String(rs.getString("id"));
            zrwms = Util.null2String(rs.getString("zrwms"));
            zrwcz = Util.null2String(rs.getString("zrwcz"));
            wcrq = Util.null2String(rs.getString("wcrq"));
            zbbm = Util.null2String(rs.getString("zbbm"));
            zrwfzr1 = Util.null2String(rs.getString("zrwfzr"));
            rwzt1 = Util.null2String(rs.getString("rwzt"));
            cbbm = Util.null2String(rs.getString("cbbm"));
            zrwwcl1 = Util.null2String(rs.getString("zrwwcl"));
            rwfk = Util.null2String(rs.getString("rwfk"));
            zrwfzrpj = Util.null2String(rs.getString("zrwfzrpj"));
            mxid = Util.null2String(rs.getString("mxid"));
            try{
                node.put("zrwms",zrwms);
                node.put("zrwcz",zrwcz);
                node.put("wcrq",wcrq);
                node.put("zbbm",zbbm);
                node.put("zrwfzr",zrwfzr1);
                node.put("rwzt",rwzt1);
                node.put("cbbm",cbbm);
                node.put("zrwwcl",zrwwcl1);
                node.put("rwfk",rwfk);
                node.put("zrwfzrpj",zrwfzrpj);
                node.put("zrwmxid",mxid);
                dt1.put(node);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        try{
            details.put("DT1",dt1);
            jsonObject.put("HEADER",header);
            jsonObject.put("DETAILS",details);
        }catch(JSONException e){
            e.printStackTrace();
        }
        log.writeLog("json=" + jsonObject.toString());
        AutoRequestService au = new AutoRequestService();
        result = au.createRequest(wfid,jsonObject.toString(),creater,"1");
        JSONObject jo = null;
        try{
            jo = new JSONObject(result);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String OA_ID = null;
        try{
            OA_ID = jo.getString("OA_ID");
        }catch(JSONException e){
            e.printStackTrace();
        }
        if(Util.getIntValue(OA_ID,0) > 0){
            rqid = OA_ID;
            if(!"".equals(fklcid)){
                fklcid = fklcid + "," + rqid;
            }else{
                fklcid = rqid;
            }
            sql = "update " + billtablename + " set fklcid='" + fklcid + "',spzt=0 where id=" + billid;
            rs.executeSql(sql);
        }else{
            rqid = "-1";
        }
        return rqid;
    }
    /**
     * @param billid  建模数据id
     * @param creater 创建人
     */
    public String CreateSubFK(String billid,String creater){
        RecordSet rs = new RecordSet();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        GetModeidUtil du = new GetModeidUtil();
        String wfid = du.getWfid("DBZRW");
        String billtablename = du.getBillTableName("DBZRW");
        //主表
        String rwlx = "";
        String zrwfzr = "";
        String kssj = "";
        String jssj = "";
        String rwzt = "";
        String fklcid = "";
        String dbzrwms = "";
        String wcrq = "";
        String cbbm = "";
        //明细表
        String zrwms = "";
        String zrwqz = "";
        String dbzrwfzr = "";
        String wcl1 = "";
        String wcl = "";
        String fksm = "";
        String fksj = "";
        String bz = "";
        String sql = "select * from " + billtablename + " where id = " + billid;
        rs.executeSql(sql);
        if(rs.next()){
//            id	rwlx	fqrq	kssj	jssj	zrwfzr	zrwms	dbzrwms	zrwqz	wcrq	cbbm	dbzrwfzr
// rwzt	zrwmxid	fklcid	spzt	MODEUUID	wcl
            rwlx = Util.null2String(rs.getString("rwlx"));
            kssj = Util.null2String(rs.getString("kssj"));
            jssj = Util.null2String(rs.getString("jssj"));
            zrwfzr = Util.null2String(rs.getString("zrwfzr"));
            zrwms = Util.null2String(rs.getString("zrwms"));
            dbzrwms = Util.null2String(rs.getString("dbzrwms"));
            zrwqz = Util.null2String(rs.getString("zrwqz"));
            wcrq = Util.null2String(rs.getString("wcrq"));
            cbbm = Util.null2String(rs.getString("cbbm"));
            dbzrwfzr = Util.null2String(rs.getString("dbzrwfzr"));
            rwzt = Util.null2String(rs.getString("rwzt"));
            fklcid = Util.null2String(rs.getString("fklcid"));
            wcl = Util.null2String(rs.getString("wcl"));
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject header = new JSONObject();
        JSONObject details = new JSONObject();
        JSONObject node = new JSONObject();
        JSONArray dt1 = new JSONArray();
        String result = "";
        String rqid = "-1";
        try{
            header.put("rwlx",rwlx);
            header.put("fqrq",nowDate);
            header.put("kssj",kssj);
            header.put("jssj",jssj);
            header.put("zrwfzr",zrwfzr);
            header.put("zrwms",zrwms);
            header.put("dbzrwms",dbzrwms);
            header.put("zrwqz",zrwqz);
            header.put("wcrq",wcrq);
            header.put("cbbm",cbbm);
            header.put("dbzrwfzr",dbzrwfzr);
            header.put("rwzt",rwzt);
            header.put("wcl",wcl);
            header.put("dbzrwid",billid);
        }catch(JSONException e){
            e.printStackTrace();
        }
        sql = "select * from " + billtablename + "_dt1 where mainid = " + billid;
        rs.executeSql(sql);
        while(rs.next()){
//            id	mainid	fksm	fksj	wcl	bz
            fksm = Util.null2String(rs.getString("fksm"));
            fksj = Util.null2String(rs.getString("fksj"));
            wcl1 = Util.null2String(rs.getString("wcl"));
            bz = Util.null2String(rs.getString("bz"));
            try{
                node.put("fksm",fksm);
                node.put("fksj",fksj);
                node.put("wcl",wcl1);
                node.put("bz",bz);
                dt1.put(node);
                details.put("DT1",dt1);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
        try{
            jsonObject.put("HEADER",header);
            jsonObject.put("DETAILS",details);
        }catch(JSONException e){
            e.printStackTrace();
        }
        AutoRequestService au = new AutoRequestService();
        result = au.createRequest(wfid,jsonObject.toString(),creater,"1");
        JSONObject jo = null;
        try{
            jo = new JSONObject(result);
        }catch(JSONException e){
            e.printStackTrace();
        }
        String OA_ID = null;
        try{
            OA_ID = jo.getString("OA_ID");
        }catch(JSONException e){
            e.printStackTrace();
        }
        if(Util.getIntValue(OA_ID,0) > 0){
            rqid = OA_ID;
            if(!"".equals(fklcid)){
                fklcid = fklcid + "," + rqid;
            }else{
                fklcid = rqid;
            }
            sql = "update " + billtablename + " set fklcid='" + fklcid + "',spzt=0 where id=" + billid;
            rs.executeSql(sql);
        }else{
            rqid = "-1";
        }
        return rqid;
    }

    private void writeLog(Object obj){
        if(true){
            new BaseBean().writeLog(this.getClass().getName(),obj);
        }
    }
}
