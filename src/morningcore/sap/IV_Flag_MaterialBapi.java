package morningcore.sap;

import com.sap.mw.jco.IFunctionTemplate;
import com.sap.mw.jco.JCO;
import com.sap.mw.jco.JCO.Table;
import com.weaver.integration.datesource.SAPInterationOutUtil;
import com.weaver.integration.log.LogInfo;

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

public class IV_Flag_MaterialBapi extends BaseBean{
    public void getData(){
        RecordSet rs = new RecordSet();
        InsertUtil iu = new InsertUtil();
        SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = dateFormate.format(new Date());
        String time = sf.format(new Date());
        String modeid = getModeId("uf_wl");
        JCO.Repository mRepository;
        JCO.Function jcoFunction = null;
        try{
        	SAPInterationOutUtil sapUtil = new SAPInterationOutUtil();
    		JCO.Client myConnection = (JCO.Client) sapUtil.getConnection("1", new LogInfo());
    		myConnection.connect();
    		mRepository = new JCO.Repository("Repository",myConnection);
            IFunctionTemplate ft = mRepository.getFunctionTemplate("Z_FI_RFC_FW_MDM");
            jcoFunction =ft.getFunction();

            //String BTWB = Util.null2String(rs.getString("BTWB"));
            JCO.ParameterList paraList = jcoFunction.getImportParameterList();
            paraList.setValue("X","IV_FLAG_MATERIAL");
            writeLog("paraList--> " + paraList.toString());
            myConnection.execute(jcoFunction);
            Table outtab = jcoFunction.getExportParameterList().getTable("ET_MATERIAL");
            //RecordSet rs2 = new RecordSet();
            writeLog("outtab--> " + outtab.getNumRows());
            String MATERIAL = "";
            String PLANT = "";
            String MATERIAL_DESC = "";
            String MATERIAL_TYPE = "";
            String UOM_BASE = "";
            String PRICE = "";
            for(int j = 0;j < outtab.getNumRows();j++){
                outtab.setRow(j);
                MATERIAL = Util.null2String((String)outtab.getValue("MATERIAL"));
                PLANT = Util.null2String((String)outtab.getValue("PLANT"));
                MATERIAL_DESC = Util.null2String((String)outtab.getValue("MATERIAL_DESC"));
                MATERIAL_TYPE = Util.null2String((String)outtab.getValue("MATERIAL_TYPE"));
                UOM_BASE = Util.null2String((String)outtab.getValue("UOM_BASE"));
                PRICE = Util.null2String((String)outtab.getValue("PRICE"));
                writeLog("MATERIAL--> " + MATERIAL + ",PLANT--> " + PLANT
                        + ",MATERIAL_DESC--> " + MATERIAL_DESC+ ",MATERIAL_TYPE--> " + MATERIAL_TYPE
                        + ",UOM_BASE--> " + UOM_BASE+ ",PRICE--> " + PRICE);
                int no = 0;
                String sql = "select count(1) as no from uf_wl where MATERIAL = '" + MATERIAL +
                        "' and PLANT = '" + PLANT + "'";
                rs.execute(sql);
                if(rs.next()){
                    no = rs.getInt("no");
                }
                boolean result = true;
                String billid = "";
                Map<String,String> map = new HashMap<String,String>();
                map.put("MATERIAL", MATERIAL);
                map.put("PLANT", PLANT);
                map.put("MATERIAL_DESC", MATERIAL_DESC);
                map.put("MATERIAL_TYPE", MATERIAL_TYPE);
                map.put("UOM_BASE", UOM_BASE);
                map.put("PRICE", PRICE);
                map.put("modedatacreatedate", nowDate);
                map.put("modedatacreatetime", time);
                map.put("modedatacreater", "1");//
                map.put("modedatacreatertype", "0");
                map.put("formmodeid", modeid);
                writeLog("map:" + map);
                if(no>0){
                    sql = "update uf_wl set MATERIAL_DESC = '" + MATERIAL_DESC + "',MATERIAL_TYPE = '"
                            + MATERIAL_TYPE + "',UOM_BASE = '" + UOM_BASE + "',PRICE = '" + PRICE +
                            "' where  MATERIAL = '" + MATERIAL + "' and PLANT = '" + PLANT + "'";
                    rs.execute(sql);
                    writeLog("sql-->" + sql);
                }else{
                    result = iu.insert(map, "uf_wl");
                    sql = "select id from uf_wl where  MATERIAL = '" + MATERIAL + "' and PLANT = '" + PLANT + "'";
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
