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

public class IV_Flag_Internal_OrderBapi extends BaseBean{
    public JCO.Client sapconnection = new SAPConn().getConnection();
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_nbdd");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
            mRepository = new JCO.Repository("sap",sapconnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction = new JCO.Function(ft);

            //String BTWB = Util.null2String(rs.getString("BTWB"));
            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_INTERNAL_ORDER");
            writeLog("paraList--> " + paraList.toString());
            sapconnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_INTERNAL_ORDER");
            //RecordSet rs2 = new RecordSet();
//            writeLog("outtab--> " + outtab.toString());
            String INTERNAL_ORDER = "";
            String INTERNAL_ORDER_DESC = "";
            String INTERNAL_ORDER_TYPE = "";
            String COMPANY_CODE = "";
            String PROFIT_CENTER = "";
            String FLAG_STATISTICAL = "";
            String EXTERNAL_ORDER = "";
            String ORDER_TYPE = "";
            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                INTERNAL_ORDER = Util.null2String((String)outtab.getValue("INTERNAL_ORDER"));
                COMPANY_CODE = Util.null2String((String)outtab.getValue("COMPANY_CODE"));
                INTERNAL_ORDER_DESC = Util.null2String((String)outtab.getValue("INTERNAL_ORDER_DESC"));
                INTERNAL_ORDER_TYPE = Util.null2String((String)outtab.getValue("INTERNAL_ORDER_TYPE"));
                PROFIT_CENTER = Util.null2String((String)outtab.getValue("PROFIT_CENTER"));
                FLAG_STATISTICAL = Util.null2String((String)outtab.getValue("FLAG_STATISTICAL"));
                EXTERNAL_ORDER = Util.null2String((String)outtab.getValue("EXTERNAL_ORDER"));
                ORDER_TYPE = Util.null2String((String)outtab.getValue("ORDER_TYPE"));
                writeLog("INTERNAL_ORDER--> " + INTERNAL_ORDER + ",COMPANY_CODE--> " + COMPANY_CODE
                        + ",INTERNAL_ORDER_DESC--> " + INTERNAL_ORDER_DESC+ ",INTERNAL_ORDER_TYPE--> " + INTERNAL_ORDER_TYPE
                        + ",PROFIT_CENTER--> " + PROFIT_CENTER + ",FLAG_STATISTICAL--> "
                        + FLAG_STATISTICAL + ",EXTERNAL_ORDER--> " + EXTERNAL_ORDER + ",ORDER_TYPE--> " + ORDER_TYPE);
                int no = 0;
                String sql = "select count(1) as no from uf_nbdd where INTERNAL_ORDER = '" + INTERNAL_ORDER +
                        "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("INTERNAL_ORDER", INTERNAL_ORDER);
                map.put("COMPANY_CODE", COMPANY_CODE);
                map.put("INTERNAL_ORDER_DESC", INTERNAL_ORDER_DESC);
                map.put("INTERNAL_ORDER_TYPE", INTERNAL_ORDER_TYPE);
                map.put("PROFIT_CENTER", PROFIT_CENTER);
                map.put("FLAG_STATISTICAL", FLAG_STATISTICAL);
                map.put("EXTERNAL_ORDER", EXTERNAL_ORDER);
                map.put("ORDER_TYPE", ORDER_TYPE);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_nbdd set INTERNAL_ORDER_DESC = '" + INTERNAL_ORDER_DESC + "',INTERNAL_ORDER_TYPE = '"
                            + INTERNAL_ORDER_TYPE + "',PROFIT_CENTER = '" + PROFIT_CENTER +
                            "',FLAG_STATISTICAL = '" + FLAG_STATISTICAL +  "',EXTERNAL_ORDER = '" + EXTERNAL_ORDER +
                            "',ORDER_TYPE = '" + ORDER_TYPE + "' where  INTERNAL_ORDER = '" + INTERNAL_ORDER +
                            "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_nbdd");
                    sql = "select id from uf_nbdd where  INTERNAL_ORDER = '" + INTERNAL_ORDER + "' and COMPANY_CODE = '" + COMPANY_CODE + "'";
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
