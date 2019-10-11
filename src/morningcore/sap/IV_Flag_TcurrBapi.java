package morningcore.sap;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Table;
import morningcore.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static morningcore.util.GetModeidUtil.getModeId;

public class IV_Flag_TcurrBapi extends BaseBean{
    public JCO.Client sapconnection = new SAPConn().getConnection();
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_hl");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
            mRepository = new JCO.Repository("sap",sapconnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction = new JCO.Function(ft);

            //String BTWB = Util.null2String(rs.getString("BTWB"));
            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_TCURR");
            writeLog("paraList--> " + paraList.toString());
            sapconnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_TCURR");
            //RecordSet rs2 = new RecordSet();
//            writeLog("outtab--> " + outtab.toString());
            String KURST = "";//汇率类型
            String FCURR = "";//从货币
            String TCURR = "";//最终货币
            String GDATU = "";//汇率有效起始日期
            String UKURS = "";//汇率
            String FFACT = "";//"来自"货币单位的比率
            String TFACT = "";//"到" 货币单位汇率
            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                KURST = Util.null2String((String)outtab.getValue("KURST"));
                FCURR = Util.null2String((String)outtab.getValue("FCURR"));
                TCURR = Util.null2String((String)outtab.getValue("TCURR"));
                GDATU = Util.null2String((String)outtab.getValue("GDATU"));
                UKURS = Util.null2String((String)outtab.getValue("UKURS"));
                FFACT = Util.null2String((String)outtab.getValue("FFACT"));
                TFACT = Util.null2String((String)outtab.getValue("TFACT"));
                writeLog("KURST--> " + KURST + ",FCURR--> " + FCURR
                        + ",TCURR--> " + TCURR+ ",GDATU--> " + GDATU
                        + ",UKURS--> " + UKURS + ",FFACT--> "
                        + FFACT + ",TFACT--> " + TFACT );
                int no = 0;
                String sql = "select count(1) as no from uf_hl where UKURS = '" + UKURS + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("KURST", KURST);
                map.put("FCURR", FCURR);
                map.put("TCURR", TCURR);
                map.put("GDATU", GDATU);
                map.put("UKURS", UKURS);
                map.put("FFACT", FFACT);
                map.put("TFACT", TFACT);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_hl set KURST = '" + KURST + "',FCURR = '" + FCURR + "',TCURR = '" + TCURR +
                            "',GDATU = '" + GDATU +  "',FFACT = '" + FFACT + "',TFACT = '" + TFACT +
                            "' where UKURS = '" + UKURS + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_hl");
                    sql = "select id from uf_hl where UKURS = '" + UKURS + "'";
                    rs.execute(sql);
                    if (rs.next()) {
                        billid = Util.null2String(rs.getString("id"));
                    }
                    if (!"".equals(billid)) {
                        ModeRightInfo ModeRightInfo = new ModeRightInfo();
                        ModeRightInfo.editModeDataShare(1, Integer.valueOf(modeid),
                                Integer.valueOf(billid));

                    }
                }
            }
           }catch(Exception e){
            e.printStackTrace();
            writeLog("e--> " + e);
        }finally{
        }
    }
}
