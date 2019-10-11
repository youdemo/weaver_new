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

public class IV_Flag_VendorBapi extends BaseBean{
    public JCO.Client sapconnection = new SAPConn().getConnection();
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_gys");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
            mRepository = new JCO.Repository("sap",sapconnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction = new JCO.Function(ft);

            //String BTWB = Util.null2String(rs.getString("BTWB"));
            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_VENDOR");
            writeLog("paraList--> " + paraList.toString());
            sapconnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_VENDOR");
            //RecordSet rs2 = new RecordSet();
//            writeLog("outtab--> " + outtab.toString());
            String VENDOR = "";
            String COMPANY_CODE = "";
            String VENDOR_NAME = "";
            String VENDOR_GROUP = "";
            String VENDOR_RECON_ACCOUNT = "";
            String PAYMENT_TERMS = "";
            String BANK_KEY = "";
            String BANK_NAME = "";
            String BANK_ACCOUNT = "";
            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                VENDOR = Util.null2String((String)outtab.getValue("VENDOR"));
                COMPANY_CODE = Util.null2String((String)outtab.getValue("COMPANY_CODE"));
                VENDOR_NAME = Util.null2String((String)outtab.getValue("VENDOR_NAME"));
                VENDOR_GROUP = Util.null2String((String)outtab.getValue("VENDOR_GROUP"));
                VENDOR_RECON_ACCOUNT = Util.null2String((String)outtab.getValue("VENDOR_RECON_ACCOUNT"));
                PAYMENT_TERMS = Util.null2String((String)outtab.getValue("PAYMENT_TERMS"));
                BANK_KEY = Util.null2String((String)outtab.getValue("BANK_KEY"));
                BANK_NAME = Util.null2String((String)outtab.getValue("BANK_NAME"));
                BANK_ACCOUNT = Util.null2String((String)outtab.getValue("BANK_ACCOUNT"));
                writeLog("VENDOR--> " + VENDOR + ",COMPANY_CODE--> " + COMPANY_CODE
                        + ",VENDOR_NAME--> " + VENDOR_NAME+ ",VENDOR_GROUP--> " + VENDOR_GROUP
                        + ",VENDOR_RECON_ACCOUNT--> " + VENDOR_RECON_ACCOUNT + ",PAYMENT_TERMS--> "
                        + PAYMENT_TERMS + ",BANK_KEY--> " + BANK_KEY + ",BANK_NAME--> " + BANK_NAME
                        + ",BANK_ACCOUNT--> " + BANK_ACCOUNT);
                int no = 0;
                String sql = "select count(1) as no from uf_gys where VENDOR = '" + VENDOR +
                        "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("VENDOR", VENDOR);
                map.put("COMPANY_CODE", COMPANY_CODE);
                map.put("VENDOR_NAME", VENDOR_NAME);
                map.put("VENDOR_GROUP", VENDOR_GROUP);
                map.put("VENDOR_RECON_ACCOUNT", VENDOR_RECON_ACCOUNT);
                map.put("PAYMENT_TERMS", PAYMENT_TERMS);
                map.put("BANK_KEY", BANK_KEY);
                map.put("BANK_NAME", BANK_NAME);
                map.put("BANK_ACCOUNT", BANK_ACCOUNT);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_gys set VENDOR_NAME = '" + VENDOR_NAME + "',VENDOR_GROUP = '"
                            + VENDOR_GROUP + "',VENDOR_RECON_ACCOUNT = '" + VENDOR_RECON_ACCOUNT +
                            "',PAYMENT_TERMS = '" + PAYMENT_TERMS +  "',BANK_KEY = '" + BANK_KEY +
                            "',BANK_NAME = '" + BANK_NAME +  "',BANK_ACCOUNT = '" + BANK_ACCOUNT +
                            "' where  VENDOR = '" + VENDOR + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_gys");
                    sql = "select id from uf_gys where  VENDOR = '" + VENDOR + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
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
