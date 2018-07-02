//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package feilida.cpt;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.cpt.capital.CapitalComInfo;
import weaver.cpt.capital.CptShare;
import weaver.cpt.maintenance.CapitalAssortmentComInfo;
//import weaver.cpt.util.CptWfUtil;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.Cell;
import weaver.soa.workflow.request.DetailTable;
import weaver.soa.workflow.request.Property;
import weaver.soa.workflow.request.RequestInfo;
import weaver.soa.workflow.request.Row;
import weaver.system.code.CodeBuild;

public class CptApplyAction implements Action, Runnable {
    private static BaseBean baseBean = new BaseBean();
    private static Object lock = new Object();
   // private CptWfUtil cptWfUtil = new CptWfUtil();
    private RequestInfo request = null;
    private JSONObject wfObject = null;

    public CptApplyAction() {
    }

    public String execute(RequestInfo var1) {
        this.request = var1;

        try {
        //    this.wfObject = this.cptWfUtil.getCptwfInfo(var1.getWorkflowid());
            if(this.wfObject.getInt("zczltype") != this.wfObject.getInt("sltype") || "".equals(this.wfObject.getString("zczlname")) || "".equals(this.wfObject.getString("slname"))) {
                var1.getRequestManager().setMessageid("20088");
                var1.getRequestManager().setMessagecontent("后台流程配置不正确请检查后台流程配置");
                return "1";
            }

            if("1".equals(this.wfObject.getString("isasync"))) {
                (new Thread(this)).start();
            } else {
                this.doAction(var1);
            }
        } catch (Exception var3) {
            var3.printStackTrace();
            baseBean.writeLog(var3.getMessage());
        }

        return "1";
    }

    public void run() {
        this.doAction(this.request);
    }

    private String doAction(RequestInfo var1) {
        Object var2 = lock;
        synchronized(lock) {
            baseBean.writeLog("tagtag run action :" + this.getClass() + ",requestid:" + var1.getRequestid());
            CapitalAssortmentComInfo var3 = new CapitalAssortmentComInfo();
            CptShare var4 = new CptShare();
            CapitalComInfo var5 = null;
            DepartmentComInfo var6 = null;
            ResourceComInfo var7 = null;

            try {
                var5 = new CapitalComInfo();
                var6 = new DepartmentComInfo();
                var7 = new ResourceComInfo();
                char var8 = Util.getSeparator();
                String var9 = var1.getRequestid();
                (new StringBuilder()).append("").append(var1.getRequestManager().getFormid()).toString();
                String var11 = TimeUtil.getCurrentDateString();
                String var12 = TimeUtil.getOnlyCurrentTimeString();
                String var13 = "";
                String var14 = "";
                String var15 = "";
                String var16 = "";
                String var17 = "";
                String var18 = "";
                String var19 = "";
                String var20 = "";
                String var21 = "";
                String var22 = "";
                String var23 = "";
                JSONArray var24 = new JSONArray();
                Property[] var25 = var1.getMainTableInfo().getProperty();

                for(int var26 = 0; var26 < var25.length; ++var26) {
                    String var27 = var25[var26].getName();
                    String var28 = Util.null2String(var25[var26].getValue());
                    if(this.wfObject.getInt("sqrtype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("sqrname"))) {
                        var13 = var28;
                        var15 = var7.getDepartmentID(var28);
                    } else if(this.wfObject.getInt("zctype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("zcname"))) {
                        var14 = var28;
                    } else if(this.wfObject.getInt("sltype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("slname"))) {
                        var19 = var28;
                    } else if(this.wfObject.getInt("rqtype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("rqname"))) {
                        var18 = var28;
                    } else if(this.wfObject.getInt("cfddtype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("cfddname"))) {
                        var20 = var28;
                    } else if(this.wfObject.getInt("bztype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("bzname"))) {
                        var21 = var28;
                    } else if(this.wfObject.getInt("zczltype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("zczlname"))) {
                        var16 = var28;
                    } else if(this.wfObject.getInt("ggxhtype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("ggxhname"))) {
                        var22 = var28;
                    } else if(this.wfObject.getInt("jgtype") == 0 && var27.equalsIgnoreCase(this.wfObject.getString("jgname"))) {
                        var23 = var28;
                    }
                }

                String var31;
                String var37;
                String var38;
                if(this.wfObject.getInt("zczltype") == 0 && this.wfObject.getInt("sltype") == 0) {
                    JSONObject var87 = new JSONObject();
                    var87.put("sqr", var13);
                    var87.put("sqbm", var15);
                    var87.put("zc", var14);
                    var87.put("sl", var19);
                    var87.put("rq", var18);
                    var87.put("cfdd", var20);
                    var87.put("bz", var21);
                    var87.put("zczl", var16);
                    var87.put("ggxh", var22);
                    var87.put("jg", var23);
                    var24.put(var87);
                } else if(this.wfObject.getInt("zczltype") == 1 && this.wfObject.getInt("sltype") == 1 || this.wfObject.getInt("zczltype") == 2 && this.wfObject.getInt("sltype") == 2 || this.wfObject.getInt("zczltype") == 3 && this.wfObject.getInt("sltype") == 3 || this.wfObject.getInt("zczltype") == 4 && this.wfObject.getInt("sltype") == 4) {
                    DetailTable[] var86 = var1.getDetailTableInfo().getDetailTable();
                    int var89 = this.wfObject.getInt("zczltype") - 1;
                    if(var86.length > var89) {
                        DetailTable var91 = var86[var89];
                        Row[] var29 = var91.getRow();
                        boolean var30 = true;
                        var31 = "";

                        for(int var32 = 0; var32 < var29.length; ++var32) {
                            Row var33 = var29[var32];
                            Cell[] var34 = var33.getCell();

                            for(int var35 = 0; var35 < var34.length; ++var35) {
                                Cell var36 = var34[var35];
                                var37 = var36.getName().toLowerCase();
                                var38 = Util.null2String(var36.getValue());
                                if(var37.equalsIgnoreCase(this.wfObject.getString("sqrname"))) {
                                    var13 = var38;
                                    var15 = var7.getDepartmentID(var38);
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("zcname"))) {
                                    var14 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("slname"))) {
                                    var19 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("rqname"))) {
                                    var18 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("cfddname"))) {
                                    var20 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("bzname"))) {
                                    var21 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("zczlname"))) {
                                    var16 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("ggxhname"))) {
                                    var22 = var38;
                                } else if(var37.equalsIgnoreCase(this.wfObject.getString("jgname"))) {
                                    var23 = var38;
                                }
                            }

                            JSONObject var98 = new JSONObject();
                            var98.put("sqr", var13);
                            var98.put("sqbm", var15);
                            var98.put("zc", var14);
                            var98.put("sl", var19);
                            var98.put("rq", var18);
                            var98.put("cfdd", var20);
                            var98.put("bz", var21);
                            var98.put("zczl", var16);
                            var98.put("ggxh", var22);
                            var98.put("jg", var23);
                            var24.put(var98);
                        }
                    }
                }

                RecordSet var88 = new RecordSet();
                RecordSet var90 = new RecordSet();
                RecordSet var92 = new RecordSet();
                RecordSet var93 = new RecordSet();
                CodeBuild var94 = new CodeBuild();
                var31 = "";
                String var95 = "0";
                String var96 = "1";
                String var97 = "";
                String var99 = "";
                String var100 = "";
                var37 = "";
                var38 = "";
                String var39 = "";
                String var40 = "";
                String var41 = "";
                String var42 = "";
                String var43 = "";
                String var44 = "";
                String var45 = "";
                String var46 = "";
                String var47 = "";
                String var48 = "";
                String var49 = "";
                String var50 = "";
                String var51 = "";
                String var52 = "";
                String var53 = "";
                ArrayList var54 = new ArrayList();

                for(int var55 = 0; var55 < var24.length(); ++var55) {
                    JSONObject var56 = (JSONObject)var24.get(var55);
                    char var57 = Util.getSeparator();
                    String var58 = var56.getString("zczl");
                    String var59 = var56.getString("zc");
                    String var60 = var56.getString("jg");
                    String var61 = var56.getString("sl");
                    String var62 = var56.getString("rq");
                    var15 = var56.getString("sqbm");
                    var13 = var56.getString("sqr");
                    String var63 = var56.getString("cfdd");
                    String var64 = var56.getString("ggxh");
                    var21 = var56.getString("bz");
                    var6.getSubcompanyid1(var15);
                    if(Util.getIntValue(var58, 0) > 0 && Util.getDoubleValue(var61, 0.0D) > 0.0D) {
                        var97 = var58;
                        double var65 = Util.getDoubleValue(var61);
                        BigDecimal var67 = new BigDecimal("" + Util.getDoubleValue(var60, 0.0D));
                        var49 = var62;
                        var47 = var67.multiply(new BigDecimal(var61)).toString();
                        var88.executeProc("CptCapital_SelectByID", var58);
                        if(var88.next()) {
                            var99 = var88.getString("mark");
                            var45 = var88.getString("sptcount");
                            var48 = var88.getString("capitalgroupid");
                            var51 = var88.getString("capitaltypeid");
                        }

                        String var68 = "2,3,4,5,6,7,8,9";

                        String var69;
                        for(var69 = var48; !var3.getSupAssortmentId(var69).equals("0"); var69 = var3.getSupAssortmentId(var69)) {
                            ;
                        }

                        if(var67.compareTo(new BigDecimal("2000")) == 1) {
                            var50 = "1";
                        } else {
                            var50 = "2";
                        }

                        var52 = var6.getSubcompanyid1(var15);
                        var53 = var15;
                        var88.executeProc("CptCapital_SelectByDataType", var58 + var8 + var15);
                        if(!var45.equals("1") && var88.next()) {
                            var99 = var88.getString("mark");
                        } else if(!var45.equals("1")) {
                            var99 = var94.getCurrentCapitalCode(var6.getSubcompanyid1(var15), var15, var48, var51, var62, var62, var58);
                        }

                        var44 = var62 + var8 + "";
                        var44 = var44 + var8 + var95;
                        var44 = var44 + var8 + var13;
                        var44 = var44 + var8 + var61;
                        var44 = var44 + var8 + var63;
                        var44 = var44 + var8 + var9;
                        var44 = var44 + var8 + "";
                        var44 = var44 + var8 + var47;
                        var44 = var44 + var8 + var96;
                        var44 = var44 + var8 + var21;
                        var44 = var44 + var8 + var99;
                        var44 = var44 + var8 + var58;
                        var44 = var44 + var8 + var37;
                        var44 = var44 + var8 + var38;
                        var44 = var44 + var8 + var39;
                        var44 = var44 + var8 + var40;
                        var44 = var44 + var8 + var41;
                        var44 = var44 + var8 + var13;
                        var44 = var44 + var8 + var11;
                        var44 = var44 + var8 + var12;
                        String var71;
                        String var72;
                        String var73;
                        String var74;
                        String var75;
                        String var76;
                        String var77;
                        String var78;
                        String var79;
                        String var80;
                        if(var45.equals("1")) {
                            for(int var102 = 0; var102 < (int)Util.getDoubleValue(var61, 0.0D); ++var102) {
                                var99 = var94.getCurrentCapitalCode(var6.getSubcompanyid1(var15), var15, var48, var51, var49, var62, var97);
                                var44 = var62 + var8 + "";
                                var44 = var44 + var8 + var95;
                                var44 = var44 + var8 + var13;
                                var44 = var44 + var8 + "1";
                                var44 = var44 + var8 + var63;
                                var44 = var44 + var8 + var9;
                                var44 = var44 + var8 + "";
                                var44 = var44 + var8 + var47;
                                var44 = var44 + var8 + var96;
                                var44 = var44 + var8 + var21;
                                var44 = var44 + var8 + var99;
                                var44 = var44 + var8 + var97;
                                var44 = var44 + var8 + var37;
                                var44 = var44 + var8 + var38;
                                var44 = var44 + var8 + var39;
                                var44 = var44 + var8 + var40;
                                var44 = var44 + var8 + var41;
                                var44 = var44 + var8 + var13;
                                var44 = var44 + var8 + var11;
                                var44 = var44 + var8 + var12;
                                var31 = var97 + var8 + "";
                                var31 = var31 + var8 + "" + var67;
                                var31 = var31 + var8 + var64;
                                var31 = var31 + var8 + var63;
                                var31 = var31 + var8 + "";
                                var31 = var31 + var8 + var62;
                                var31 = var31 + var8 + var49;
                                var88.executeProc("CptCapital_Duplicate", var31);
                                var88.next();
                                var46 = var88.getString(1);
                                var44 = var46 + var8 + var44;
                                var44 = var44 + var8 + "" + var67;
                                var44 = var44 + var8 + "";
                                var44 = var44 + var8 + var50;
                                var44 = var44 + var8 + var100;
                                var88.executeProc("CptUseLogInStock_Insert", var44);
                                var88.executeSql("update cptcapital set olddepartment = " + var15 + ",blongsubcompany=\'" + var52 + "\', blongdepartment=\'" + var53 + "\',contractno=\'" + "" + "\' where id = " + var46);
                                var71 = "select * from cptcapitalparts where cptid = " + var97;
                                var90.executeSql(var71);

                                while(var90.next()) {
                                    var71 = "insert into cptcapitalparts (cptid,partsname,partsspec,partssum,partsweight,partssize) select " + var46 + ",partsname,partsspec,partssum,partsweight,partssize from cptcapitalparts where id = " + var90.getString("id");
                                    var92.executeSql(var71);
                                }

                                var71 = "select * from cptcapitalequipment where cptid = " + var97;
                                var90.executeSql(var71);

                                while(var90.next()) {
                                    var71 = "insert into cptcapitalequipment (cptid,equipmentname,equipmentspec,equipmentsum,equipmentpower,equipmentvoltage) select " + var46 + ",equipmentname,equipmentspec,equipmentsum,equipmentpower,equipmentvoltage from cptcapitalequipment where id = " + var90.getString("id");
                                    var92.executeSql(var71);
                                }

                                var72 = "";
                                var73 = "";
                                var74 = "";
                                var75 = "";
                                var76 = "";
                                var77 = "";
                                var78 = "";
                                var79 = "";
                                var80 = "";
                                String var81 = "";
                                var88.executeSql("select * from CptAssortmentShare where assortmentid=" + var69);

                                while(var88.next()) {
                                    var73 = var88.getString("sharetype");
                                    var74 = var88.getString("seclevel");
                                    var75 = var88.getString("rolelevel");
                                    var76 = var88.getString("sharelevel");
                                    var77 = var88.getString("userid");
                                    var78 = var88.getString("departmentid");
                                    var79 = var88.getString("roleid");
                                    var80 = var88.getString("foralluser");
                                    var81 = var88.getString("subcompanyid");
                                    var72 = var46 + var8 + var73;
                                    var72 = var72 + var8 + var74;
                                    var72 = var72 + var8 + var75;
                                    var72 = var72 + var8 + var76;
                                    var72 = var72 + var8 + var77;
                                    var72 = var72 + var8 + var78;
                                    var72 = var72 + var8 + var79;
                                    var72 = var72 + var8 + var80;
                                    var72 = var72 + var8 + var81;
                                    var72 = var72 + var8 + var69;
                                    var93.executeProc("CptShareInfo_Insert_dft", var72);
                                }

                                var4.setCptShareByCpt(var46);
                                var54.add(var46);
                            }
                        } else {
                            var88.executeProc("CptCapital_SelectByDataType", var58 + var8 + var15);
                            if(var88.next()) {
                                var46 = var88.getString("id");
                                BigDecimal var101 = new BigDecimal(var88.getString("startprice"));
                                BigDecimal var103 = new BigDecimal(var88.getString("capitalnum"));
                                var67 = var67.multiply(new BigDecimal(var61));
                                var67 = var67.add(var101.multiply(var103));
                                var67 = var67.divide(var103.add(new BigDecimal(var61)), 2, 0);
                                var44 = var46 + var8 + var44;
                                var44 = var44 + var8 + "" + var67;
                                var44 = var44 + var8 + "";
                                var44 = var44 + var8 + var50;
                                var44 = var44 + var8 + var100;
                                var88.executeProc("CptUseLogInStock_Insert", var44);
                                var31 = var46 + var8 + "" + var67;
                                var31 = var31 + var8 + var64;
                                var31 = var31 + var8 + "";
                                var31 = var31 + var8 + var63;
                                var31 = var31 + var8 + "";
                                var31 = var31 + var8 + var62;
                                var88.executeProc("CptCapital_UpdatePrice", var31);
                            } else {
                                var31 = var58 + var8 + "";
                                var31 = var31 + var8 + "" + var67;
                                var31 = var31 + var8 + var64;
                                var31 = var31 + var8 + var63;
                                var31 = var31 + var8 + "";
                                var31 = var31 + var8 + var62;
                                var31 = var31 + var8 + var62;
                                var88.executeProc("CptCapital_Duplicate", var31);
                                var88.next();
                                var46 = var88.getString(1);
                                var44 = var46 + var8 + var44;
                                var44 = var44 + var8 + "" + var67;
                                var44 = var44 + var8 + "";
                                var44 = var44 + var8 + var50;
                                var44 = var44 + var8 + var100;
                                var88.executeProc("CptUseLogInStock_Insert", var44);
                                var88.executeSql("update cptcapital set olddepartment = " + var15 + ",blongsubcompany=\'" + var52 + "\', blongdepartment=\'" + var15 + "\',contractno=\'" + "" + "\' ,capitalnum=\'" + var65 + "\'  where id = " + var46);
                                String var70 = "select * from cptcapitalparts where cptid = " + var58;
                                var90.executeSql(var70);

                                while(var90.next()) {
                                    var70 = "insert into cptcapitalparts (cptid,partsname,partsspec,partssum,partsweight,partssize) select " + var46 + ",partsname,partsspec,partssum,partsweight,partssize from cptcapitalparts where id = " + var90.getString("id");
                                    var92.executeSql(var70);
                                }

                                var70 = "select * from cptcapitalequipment where cptid = " + var58;
                                var90.executeSql(var70);

                                while(var90.next()) {
                                    var70 = "insert into cptcapitalequipment (cptid,equipmentname,equipmentspec,equipmentsum,equipmentpower,equipmentvoltage) select " + var46 + ",equipmentname,equipmentspec,equipmentsum,equipmentpower,equipmentvoltage from cptcapitalequipment where id = " + var90.getString("id");
                                    var92.executeSql(var70);
                                }

                                var71 = "";
                                var72 = "";
                                var73 = "";
                                var74 = "";
                                var75 = "";
                                var76 = "";
                                var77 = "";
                                var78 = "";
                                var79 = "";
                                var80 = "";
                                var88.executeSql("select * from CptAssortmentShare where assortmentid=" + var69);

                                while(var88.next()) {
                                    var72 = var88.getString("sharetype");
                                    var73 = var88.getString("seclevel");
                                    var74 = var88.getString("rolelevel");
                                    var75 = var88.getString("sharelevel");
                                    var76 = var88.getString("userid");
                                    var77 = var88.getString("departmentid");
                                    var78 = var88.getString("roleid");
                                    var79 = var88.getString("foralluser");
                                    var80 = var88.getString("subcompanyid");
                                    var71 = var46 + var8 + var72;
                                    var71 = var71 + var8 + var73;
                                    var71 = var71 + var8 + var74;
                                    var71 = var71 + var8 + var75;
                                    var71 = var71 + var8 + var76;
                                    var71 = var71 + var8 + var77;
                                    var71 = var71 + var8 + var78;
                                    var71 = var71 + var8 + var79;
                                    var71 = var71 + var8 + var80;
                                    var71 = var71 + var8 + var69;
                                    var93.executeProc("CptShareInfo_Insert_dft", var71);
                                }

                                var4.setCptShareByCpt(var46);
                                var54.add(var46);
                            }
                        }
                    }
                }

                try {
                    var5.addCapitalCache(var54);
                } catch (Exception var83) {
                    var83.printStackTrace();
                }
            } catch (Exception var84) {
                var84.printStackTrace();
                baseBean.writeLog(var84.getMessage());
            }

            return "1";
        }
    }
}
