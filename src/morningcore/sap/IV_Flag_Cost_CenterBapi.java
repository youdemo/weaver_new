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

public class IV_Flag_Cost_CenterBapi extends BaseBean{
    public JCO.Client sapconnection = new SAPConn().getConnection();
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_cbzx");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
            mRepository = new JCO.Repository("sap",sapconnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction = new JCO.Function(ft);

            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_COST_CENTER");
            writeLog("paraList--> " + paraList.toString());
            sapconnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_COST_CENTER");
            //RecordSet rs2 = new RecordSet();
//            writeLog("outtab--> " + outtab.toString());
            String COST_CENTER = "";
            String COST_CENTER_NAME = "";
            String COMPANY_CODE = "";
            String PROFIT_CENTER = "";
            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                COST_CENTER = Util.null2String((String)outtab.getValue("COST_CENTER"));
                COST_CENTER_NAME = Util.null2String((String)outtab.getValue("COST_CENTER_NAME"));
                COMPANY_CODE = Util.null2String((String)outtab.getValue("COMPANY_CODE"));
                PROFIT_CENTER = Util.null2String((String)outtab.getValue("PROFIT_CENTER"));
                writeLog("COST_CENTER--> " + COST_CENTER + ",COST_CENTER_NAME--> " + COST_CENTER_NAME
                        + ",COMPANY_CODE--> " + COMPANY_CODE + ",PROFIT_CENTER--> " + PROFIT_CENTER);
                int no = 0;
                String sql = "select count(1) as no from uf_cbzx where COST_CENTER = '" + COST_CENTER +
                        "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("COST_CENTER", COST_CENTER);
                map.put("COST_CENTER_NAME", COST_CENTER_NAME);
                map.put("COMPANY_CODE", COMPANY_CODE);
                map.put("PROFIT_CENTER", PROFIT_CENTER);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_cbzx set COST_CENTER_NAME = '" + COST_CENTER_NAME + "',PROFIT_CENTER = '" + PROFIT_CENTER
                            + "' where  COST_CENTER = '" + COST_CENTER + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_cbzx");
                    sql = "select id from uf_cbzx where  COST_CENTER = '" + COST_CENTER + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
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
