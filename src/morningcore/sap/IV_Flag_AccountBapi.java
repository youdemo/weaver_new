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

public class IV_Flag_AccountBapi extends BaseBean{
    public JCO.Client sapconnection = new SAPConn().getConnection();
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_km");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
            mRepository = new JCO.Repository("sap",sapconnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction = new JCO.Function(ft);

            //String BTWB = Util.null2String(rs.getString("BTWB"));
            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_ACCOUNT");
            writeLog("paraList--> " + paraList.toString());
            sapconnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_ACCOUNT");
            //RecordSet rs2 = new RecordSet();
//            writeLog("outtab--> " + outtab.toString());
            String ACCOUNT = "";
            String ACCOUNT_NAME = "";
            String COMPANY_CODE = "";

            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                ACCOUNT = Util.null2String((String)outtab.getValue("ACCOUNT").toString());
                ACCOUNT_NAME = Util.null2String((String)outtab.getValue("ACCOUNT_NAME").toString());
                COMPANY_CODE = Util.null2String((String)outtab.getValue("COMPANY_CODE").toString());
                writeLog("ACCOUNT--> " + ACCOUNT + ",ACCOUNT_NAME--> " + ACCOUNT_NAME + ",COMPANY_CODE--> " + COMPANY_CODE);
                int no = 0;
                String sql = "select count(1) as no from uf_km where ACCOUNT = '" + ACCOUNT +
                        "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("ACCOUNT", ACCOUNT);
                map.put("ACCOUNT_NAME", ACCOUNT_NAME);
                map.put("COMPANY_CODE", COMPANY_CODE);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_km set ACCOUNT_NAME = '" + ACCOUNT_NAME + "' where  ACCOUNT = '" +
                            ACCOUNT + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_km");
//                    writeLog("result-->" + result);
                    sql = "select id from uf_km where  ACCOUNT = '" +
                            ACCOUNT + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
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
