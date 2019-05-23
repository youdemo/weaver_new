package wasu.action.finance;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 包干内费用报销，增加付款属性
 * @Author: adore
 * @DateTime: 2018-08-27 16:15:58
 * @Version: v1.0
 */
public class CertificateWorkflowAction implements Action {
    private StaticObj staticobj = null;

    public String execute(RequestInfo info) {
        String requestid = Util.null2String(info.getRequestid());
        writeLog("_-----------------进入包干内action-------------------:" + requestid);
        staticobj = StaticObj.getInstance();
        String errorMsg = "";

        //--------------------------主表字段start----------------------------
        //申请人sqr
        String f_sqr = Util.null2String(Prop.getPropValue("FinancialDocuments", "sqr"));
        // String f_sqr = "sqr";
        //申请部门
        String f_sqbm = Util.null2String(Prop.getPropValue("FinancialDocuments", "sqbm"));
        // String f_sqbm = "yjbm";
        //凭证号(新增)
        String f_pzh = Util.null2String(Prop.getPropValue("FinancialDocuments", "pzh"));
        // String f_pzh = "debitremark";
        //事由
        String f_sy = Util.null2String(Prop.getPropValue("FinancialDocuments", "sy"));
        // String f_sy = "sy";
        //拓展经理(新增)
        String f_tzjl = Util.null2String(Prop.getPropValue("FinancialDocuments", "tzjl"));
        // String f_tzjl = "tzjl";
        //报销金额
        String f_bxje = Util.null2String(Prop.getPropValue("FinancialDocuments", "bxje"));
        // String f_bxje = "fkje";
        //附件数
        String f_fjs = Util.null2String(Prop.getPropValue("FinancialDocuments", "fjs"));
        //OA编号
        String f_oabh = Util.null2String(Prop.getPropValue("FinancialDocuments", "oabh"));
        // String f_oabh = "lcbh";
        //入账日期 （新增）
        String f_rzrq = Util.null2String(Prop.getPropValue("FinancialDocuments", "rzrq"));
        // String f_rzrq = "rzrq";
        //是否需要导入（新增）
        String f_sfxydr = Util.null2String(Prop.getPropValue("FinancialDocuments", "sfxydr"));
        // String f_sfxydr = "sfxydr";
        //导入标志（新增）
        String f_drbz = Util.null2String(Prop.getPropValue("FinancialDocuments", "drbz"));
        // String f_drbz = "drbz";
        //报销方式
        String f_bxfs = Util.null2String(Prop.getPropValue("FinancialDocuments", "bxfs"));
        // String f_bxfs = "bxfs";
        //所属分部
        String f_ssfb = Util.null2String(Prop.getPropValue("FinancialDocuments", "ssfb"));
        // String f_ssfb = "ssgs";
        //是否需要选择拓展经理(新增)
        String f_sfxyxztzjl = Util.null2String(Prop.getPropValue("FinancialDocuments", "sfxyxztzjl"));
        // String f_sfxyxztzjl = "sfxyxztzjl";
        //申请日期
        String f_sqrq = Util.null2String(Prop.getPropValue("FinancialDocuments", "sqrq"));
        // String f_sqrq = "sqrq";
        //--------------------------明细表字段start--------------------------
        //科目
        String f_mx_km = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_km"));
        // String f_mx_km = "subject";
        //发生日期
        String f_mx_fsrq = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_fsrq"));
        // String f_mx_fsrq = "budgetperiod";
        //附件数
        String f_mx_fjs = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_fjs"));
        // String f_mx_fjs = "fjs";
        //说明
        String f_mx_sm = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_sm"));
        // String f_mx_sm = "sm";
        //申报金额
        String f_mx_sbje = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_sbje"));
        // String f_mx_sbje = "applyamount";
        //核算项目
//		  String f_mx_hsxm = Util.null2String(Prop.getPropValue("FinancialDocuments", "mx_hsxm"));
        String f_table = Util.null2String(Prop.getPropValue("FinancialDocuments", "f_table"));
        String f_tabledt = Util.null2String(Prop.getPropValue("FinancialDocuments", "f_tabledt"));
        RecordSet rs = new RecordSet();
        RecordSet rs_str = new RecordSet();
        RecordSet rs_tmp = new RecordSet();
        RecordSet rsc = new RecordSet();
        RecordSet up = new RecordSet();
        
        // 区分账套   默认一个
        RecordSetDataSource rsds = new RecordSetDataSource("jd_data");

        String sql_str = "";
        String sql = "";
        // String tableName = "";//主表名称
        // String requestid = Util.null2String(info.getRequestid());
        String tableName = Util.null2String(info.getRequestManager().getBillTableName());//表名
        String detailtablename = "";//明细表名称

        detailtablename = tableName + "_dt1";
        String m_sqr = "";//申请人
        String m_sqbm = "";//申请部门
        String m_sy = "";//事由
        String m_tzjl = "";//拓展经理
        String JD_depCode = "";//金蝶部门编码
        String depName = "";//部门名称
        String m_ssfb = "";//所属分部
        String m_sqrq = "";//申请日期

        String mainID = "";//主表ID
        String falg = "";//科目是否有归属地标志

        String need_hsxm = "";//核算项目段
        String need_fls = "";//分录数
        String need_kjnd = "";//会计年度
        String need_kjqj = "";//会计期间
        String need_pzh = "";//凭证号
        String need_bxje = "";//借方和贷方金额
        String need_bz = "";//备注
        String need_m_fjs = "";//主表附件数
        String need_rzrq = "";//入账日期
        String need_oabh = "";//OA编号
        String need_xh = "";//序号
        String need_dflx = "";//贷方类型

        String need_resName = "";//申请人姓名

        String sfxydr_tmp = "";//是否需要导入
        String bxfs_tem = "";//报销方式临时变量
        String m_sfxyxztzjl = "";//是否需要选择拓展经理
        String syleng = "";//主表事由字节长度
        String dfzy = "";//金蝶需要传入的贷方摘要

        String need_kmdm = "";//科目代码
        String need_kmID = "";//金蝶科目ID
        String need_sbje = "";//申报金额
        String need_zy = "";//摘要
        String need_fjs = "";//附件数
        String need_fsrq = "";//发生日期
        String need_zdr = "";//制单人
        // add by shaw 2018年08月07日13:47:14 start
        String payee = ""; // 领款人
        // add by shaw 2018年08月07日13:47:32 end
        String paymentType = ""; // 付款属性 add by shaw 2018-08-08 17:01:26
        String receiverName = ""; // 收款方户名 add by shaw 2018-08-08 17:08:53
        if (!"".equals(tableName)) {
            sql = "select " + f_sqr + "," + f_sqbm + "," + f_sy + "," + f_tzjl + "," + f_bxje + "," + f_rzrq + "," +
                    "" + f_oabh + "," + f_sfxydr + "," + f_bxfs + "," + f_ssfb + "," + f_sfxyxztzjl + "," + f_sqrq + "," +
                    " id,lengthb(" + f_sy + ") as syleng,payee,PaymentType,skdwhm,field_1954858588 as newtzjl from " + tableName + " " +
                    " where requestid = '" + info.getRequestid() + "'";
            writeLog("主表SQL：" + sql);
            rs.executeSql(sql);
            if (rs.next()) {
                m_sqr = Util.null2String(rs.getString(f_sqr));    //申请人
                m_sqbm = Util.null2String(rs.getString(f_sqbm));    //申请部门
//				 need_pzh = Util.null2String(rs.getString(f_pzh));	//凭证号
                m_sy = Util.null2String(rs.getString(f_sy));    //事由
                m_tzjl = Util.null2String(rs.getString("newtzjl"));    //拓展经理
                need_bxje = Util.null2String(rs.getString(f_bxje));    //报销金额
//				 need_m_fjs  = Util.null2String(rs.getString(f_fjs));	//主表附件数
                need_rzrq = Util.null2String(rs.getString(f_rzrq));    //入账日期
                need_oabh = Util.null2String(rs.getString(f_oabh));    //OA编号
                sfxydr_tmp = Util.null2String(rs.getString(f_sfxydr));    //是否需要导入
                bxfs_tem = Util.null2String(rs.getString(f_bxfs));    //报销方式
                syleng = Util.null2String(rs.getString("syleng"));//事由字节长度
                m_ssfb = Util.null2String(rs.getString(f_ssfb));//所属分部
                
                // 账套的区分  根据公司来  公司ID
                if("623".equals(m_ssfb)){
                	rsds = new RecordSetDataSource("jd_data_zj");
                }
                
//				 m_sfxyxztzjl = Util.null2String(rs.getString(f_sfxyxztzjl));	//是否需要选择拓展经理field_1442494596
                m_sfxyxztzjl = Util.null2String(rs.getString(f_sfxyxztzjl));    //是否需要选择拓展经理
                m_sqrq = Util.null2String(rs.getString(f_sqrq));//申请日期
                mainID = Util.null2String(rs.getString("ID"));//主表ID
                writeLog("主表ID:---------------" + mainID);
                payee = Util.null2String(rs.getString("payee")); // 领款人 add by shaw 2018年08月07日13:49:49
                paymentType = Util.null2String(rs.getString("PaymentType")); // 付款属性 add by shaw 2018年08月08日17:02:15
                receiverName = Util.null2String(rs.getString("skdwhm")); // add by shaw 2018-08-08 17:09:29
            }
        }

        //如果流程不是包干内费用报销（适用于全国营销中心），则判断是否需要导入金蝶     ---2013-08-01其他3个流程上线，防止流程误导入金蝶出错
        boolean isIn = false;
        int num_1 = m_sqrq.compareTo("2013-08-02");
        writeLog("m_sqrq:------申请日期*******" + m_sqrq);
        writeLog("num_1:------字符串比较-------" + num_1);
        if (info.getWorkflowid().equals("1122") || info.getWorkflowid().equals("1104") || info.getWorkflowid().equals("1121")) {
            isIn = true;
        }

        //如果申请日期小于2013-08-02日，则不插入金蝶数据库
        if ((num_1 < 0 && isIn) || "".equals(m_sqrq)) {
            // new BaseBean().writeLog("getWorkflowid:------getWorkflowid-------" + info.getWorkflowid());
            return SUCCESS;
        } else {
            //获取改年度下， 改月度下， 最大的凭证号
            if (!"".equals(need_rzrq)) {
                int nowYear = 0;
                int nowMonth = 0;

                nowYear = Integer.valueOf(need_rzrq.substring(0, 4));
                writeLog("nowYear:-------1--------" + nowYear);
                nowMonth = Integer.valueOf(need_rzrq.substring(5, 7));
                writeLog("nowMonth:------1---------" + nowMonth);
                need_kjnd = String.valueOf(nowYear);
                need_kjqj = String.valueOf(nowMonth);
                writeLog("need_kjnd:-------2--------" + need_kjnd);
                writeLog("need_kjqj:------2---------" + need_kjqj);

                String str_tem = "";//临时变量

                String sql_1 = "select  fnumber from t_voucher where FYear = '" + need_kjnd + "' and FPeriod = '" + need_kjqj + "' order by FNumber desc";
                writeLog("获取凭证号语句:---------------" + sql_1);
                rsds.executeSql(sql_1);
                if (rsds.next()) {
                    str_tem = Util.null2String(rsds.getString("fnumber"));
                }
                writeLog("获取凭证号:---------------" + str_tem);
                if ("".equals(str_tem)) {
                    need_pzh = "1";
                } else {
                    need_pzh = String.valueOf(Integer.valueOf(str_tem) + 1);
                }
                writeLog("处理后的凭证号:---------------" + need_pzh);
            }


            //获取最大序号
            if (!"".equals(need_pzh)) {
                String str_tem = "";//临时变量

                String sql_1 = "select  top 1 FSerialNum from t_voucher order by FSerialNum desc";
                writeLog("获取最大序号语句:---------------" + sql_1);
                rsds.executeSql(sql_1);
                if (rsds.next()) {
                    str_tem = Util.null2String(rsds.getString("FSerialNum"));
                }
                writeLog("获取最大序号:---------------" + str_tem);
                need_xh = String.valueOf(Integer.valueOf(str_tem) + 1);
                writeLog("处理后的序号:---------------" + need_xh);
            }

            //获取该分部的制单人
            writeLog("所属分部:---------------" + m_ssfb);
            sql = "select maker from hrmsubcompany where id ='" + m_ssfb + "'";
            rs.executeSql(sql);
            if (rs.next()) {
                need_zdr = rs.getString("maker");   // 制单人 一定要根据账套的 人员编号
                writeLog("制单人:---------------" + need_zdr);
            }

            //如果付款属性为对公付款 ，则贷方类型为6187，否则为5520 edit by shaw 2018-08-08 17:04:09
            if ("1".equals(paymentType)) { 
                // need_dflx = "1092";
                need_dflx = "6187";
            } else {
                need_dflx = "5520";   
                // need_dflx = "3460";
                //need_dflx = "1527";
            }
			if("623".equals(m_ssfb)){
				if ("1".equals(paymentType)) {
	                need_dflx = "1681";
	            } else {
	                need_dflx = "1371"; 
	            }
			}

            //获取关联明细总条数
            if (!"".equals(detailtablename)) {
                int tem = 0;//分录数临时变量
                int str_fjs = 0;//附件数临时变量
                //字段需调整 expenseid
//			sql_str = "select count(*) as cc from "+detailtablename+" where expenseid = '"+mainID+"'";
                sql_str = "select " + f_mx_fjs + " from " + detailtablename + " where mainid = '" + mainID + "'";
                writeLog("分录数语句:---------------" + sql_str);
                rs.executeSql(sql_str);
                while (rs.next()) {
                    writeLog("语句执行成功---------------");
                    tem++;
                    str_fjs += Integer.valueOf(rs.getString(f_mx_fjs));
                    writeLog("语句执行成功---" + str_fjs + "------" + tem + "------");
                }
                need_m_fjs = String.valueOf(str_fjs);
                writeLog("附件数need_m_fjs:---------------" + need_m_fjs);
                need_fls = String.valueOf(tem);
                writeLog("分录数need_fls:---------------" + need_fls);
            }

            if ("1".equals(sfxydr_tmp)) {
                String workCode = "";
                if (!"".equals(m_sqr)) {
                    String sql_resName = "select * from hrmresource where id = '" + m_sqr + "'";
                    rs_str.executeSql(sql_resName);
                    if (rs_str.next()) {
                        need_resName = rs_str.getString("lastname");
                        workCode = Util.null2String(rs_str.getString("workcode"));
                        writeLog("申请人名称:---------------" + need_resName);
                    }
                }

                need_bz = m_sy;

                //根据部门ID获取对应的金蝶部门编码和部门名称
                if (!"".equals(m_sqbm)) {
//			    sql_str = "select * from hrmdepartment where id = '"+m_sqbm+"'";
//				rs.executeSql(sql_str);
                    rs.executeSql("Select HRMDEPARTMENT.DEPARTMENTNAME, HRMDEPARTMENTDEFINED.JDIDBH"
                            + " From HRMDEPARTMENT, HRMDEPARTMENTDEFINED "
                            + " Where HRMDEPARTMENT.ID = HRMDEPARTMENTDEFINED.DEPTID and HRMDEPARTMENT.ID='" + m_sqbm + "'");
                    if (rs.next()) {
//					JD_depCode = rs.getString("departmentcode");
//					depName = rs.getString("departmentname");
                        JD_depCode = rs.getString("JDIDBH"); 
                        depName = rs.getString("DEPARTMENTNAME");
                    }
                    writeLog("OA部门及编码:-----部门：" + depName + "-----所属编码：" + JD_depCode);
                }

                //如果OA部门编码为空，则流程出错
                if ("".equals(JD_depCode) || JD_depCode == null) {
                    writeLog("OA部门编码为空:---------------");
                    return "-1";
                }
                
                /**
                	 地区的获取开始
                **/
                //如果需要拓展经理，则判断拓展经理，如果不需要，即使选了，也不会判断拓展经理
                writeLog("是否需要拓展经理:---------------" + m_sfxyxztzjl);
                if ("0".equals(m_sfxyxztzjl)) {
                    //根据拓展经理字段判断核算科目属于哪种情况
                    if (!"".equals(m_tzjl) && !"0".equals(m_tzjl)) {
                        sql_str = "select * from uf_hrmmanager where resourceid = '" + m_tzjl + "' and isshow='0'";
                        writeLog("拓展经理ID语句:---------------" + sql_str);
                        rs.executeSql(sql_str); 
                        if (rs.next()) {
                            //拼接核算项目段  核算项目段= 部门---金蝶部门编码---部门名称+区域经理表的MEMO字段
                            String itemID = "";//ITEM表ID

                            String sql_1 = "select * from t_Item where FNumber = '" + rs.getString("code") + "'";
                            writeLog("地区ID语句:---------------" + sql_1);
                            rsds.executeSql(sql_1);
                            if (rsds.next()) {
                                itemID = rsds.getString("FitemID");
                            }
                            writeLog("核算地区ID:---------------" + itemID);
                            //再根据条件筛选出FDetailID
                            sql_1 = "select * from t_ItemDetailV where FItemClassID = 3003 and FItemID = '" + itemID + "' and FDetailID in (select FDetailID from t_ItemDetailV where FItemClassID = '2' and FItemID = (select FItemID from t_Department where FNumber = '" + JD_depCode + "'))";
                            writeLog("核算地区语句:---------------" + sql_1);
                            rsds.executeSql(sql_1);
                            if (rsds.next()) {
                                need_hsxm = rsds.getString("FDetailID");
                            }
                            writeLog("核算项目need_hsxm:---------------" + need_hsxm);
                        }
                    } else {
                        writeLog("核算项目不是情况3");
                    }
                }
		/**
                	 地区的获取结束
                **/
                
                
                //如果事由字节长度超过240，则输出固定值
                if (Integer.valueOf(syleng) > 240) {
                    dfzy = "摘要详情请参考OA相关流程";
                } else {
                    dfzy = need_resName + "-" + need_bz;
                }

                if (!"".equals(detailtablename)) {
                    int maxID = 0;
                    //插入主表
                    String sql_main = "insert into t_Voucher(FApproveID,FAttachments,FBrNo,FCashierID,FChecked,FCHECKERID,FCreditTotal,FDate,FDebitTotal," +
                            "FEntryCount,FExplanation,FReference,FFrameWorkID,FGroupID," +
                            "FInternalInd,FNumber,FOwnerGroupID,FPeriod,FPosted,FPosterID,FPreparerID," +
                            "FSerialNum,FTransDate,FTranType,FYear) " +
                            "values " +
                            "('-1','" + need_m_fjs + "','0','-1','0','-1','" + need_bxje + "','" + need_rzrq + "','" + need_bxje + "'," +
                            "'" + need_fls + "','" + dfzy + "','" + need_oabh + "-OA流程转入数据" + "','-1','1'," +
                            "'','" + need_pzh + "','0','" + need_kjqj + "','0','-1','" + need_zdr + "'," +
                            "'" + need_xh + "','" + need_rzrq + "','0','" + need_kjnd + "')";

                    rsds.executeSql(sql_main);
//					stmt = con.prepareStatement(sql_main,Statement.RETURN_GENERATED_KEYS);
                    writeLog("主表操作==================" + sql_main);

                    rsds.executeSql("select MAX(FVOUCHERID) from t_Voucher");
//					resultSet = stmt.getGeneratedKeys();
                    if (rsds.next()) {
                        maxID = rsds.getInt(1);
                        writeLog("返回ID==================" + maxID);
                    }

                    // 费用科目数组
                    List<String> subject = new ArrayList<String>();
                    String dt_sql = " select * from " + detailtablename + " where mainid = " + mainID;
                    rs.executeSql(dt_sql);
                    while (rs.next()) {
                        String tmpSubj = Util.null2String(rs.getString(f_mx_km));
                        subject.add(tmpSubj);
                    }
                    List<String> newSubj = removeDuplicate(subject);
                    //取明细表数据
                    int i = 0;
                    for (String subj : newSubj) {
                        String tmp_need_kmid = "";
                        sql_str = "select * from " + detailtablename + " where mainid = '" + mainID + "' and subject=" + subj;
                        writeLog("明细表:====" + sql_str);
                        rs.executeSql(sql_str);
                        writeLog("位置1");
                        double tmp_sbje = 0.00;
                        while (rs.next()) {
                            String subID = "";//科目ID
                            String tmp_need_kmdm = "";
                            // new BaseBean().writeLog("位置2");
                            need_sbje = rs.getString(f_mx_sbje);//申报金额
                            tmp_sbje += rs.getDouble(f_mx_sbje);
                            // new BaseBean().writeLog("申报金额" + need_sbje);
                            need_zy = rs.getString(f_mx_sm);//摘要
                            // new BaseBean().writeLog("摘要" + need_zy);
                            need_fjs = rs.getString(f_mx_fjs);//附件数
                            // new BaseBean().writeLog("附件数" + need_fjs);
                            need_fsrq = rs.getString(f_mx_fsrq);//发生日期
                            // new BaseBean().writeLog("发生日期" + need_fsrq);
//						countFjs = countFjs + Integer.valueOf(need_fjs);//附件数取和
//						new BaseBean().writeLog("位置3");
//						new BaseBean().writeLog("总和countFjs"+countFjs);
                            //取科目编码
                            String sql_budget = "select * from FnaBudgetfeeType where id = '" + rs.getString(f_mx_km) + "'";
                            writeLog("科目sql语句====" + sql_budget);
                            rsc.executeSql(sql_budget);
                            if (rsc.next()) {
//							String sql_sub = "select * from subjectDeatil where code = '"+rsc.getString("JDCODE")+"'";
                                String sql_sub = "select * from subjectDeatil where code = '" + rsc.getString("codeName") + "'";
                                writeLog("科目sql语句2====" + sql_sub);
                                rs_tmp.executeSql(sql_sub);
                                if (rs_tmp.next()) {
                                    subID = rs_tmp.getString("id");
                                    writeLog("科目ID:====" + subID);
                                }
                            }
                            String sql_dep = "select * from firstDepLeft where code = '" + JD_depCode + "'";
                            rsc.executeSql(sql_dep);
                            if (rsc.next()) {
                                String sql_gl = "select * from relevance where subjectID = '" + subID + "' and parentDepID = '" + rsc.getString("id") + "'";
                                rs_tmp.executeSql(sql_gl);
                                if (rs_tmp.next()) {
                                    tmp_need_kmdm = rs_tmp.getString("subjcetCode");
                                    falg = rs_tmp.getString("isFalg");
                                }
                            }
                            if ("1".equals(m_sfxyxztzjl) || "".equals(m_sfxyxztzjl)) { // add by shaw || "".equals(m_sfxyxztzjl) 2018-08-08 17:00:05
//						if("".equals(m_tzjl)||"0".equals(m_tzjl)){
                            	if("623".equals(m_ssfb))  {
                              	  String sql3 = "select fdetailid from t_ItemDetailV a where fitemid in(select fitemid from t_Item where fnumber in('"+JD_depCode+"') and fitemclassid=3007) and fitemclassid=3007 and(select fdetailcount from t_ItemDetail where fdetailid=a.fdetailid)=1";
                              	  rsds.executeSql(sql3);
                              	  if (rsds.next()) {
                                        need_hsxm = Util.null2String(rsds.getString("fdetailid"));
                                    }
                              	  writeLog("情况3===============" + sql3);
                                }else {
                                	if ("0".equals(falg)) {
                                        //科目没有归属地，特殊处理。情况2
                                        String sql_2 = "select * from t_ItemDetailV where FItemClassID = 3003 and FItemID = (select FItemID from t_Item where FNumber = '02.999') and FDetailID in (select FDetailID from t_ItemDetailV where FItemClassID = '2' and FItemID = (select FItemID from t_Department where FNumber = '" + JD_depCode + "'))";
                                        writeLog("情况2===============" + sql_2);
                                        rsds.executeSql(sql_2);
                                        if (rsds.next()) {
                                            need_hsxm = Util.null2String(rsds.getString("FDetailID"));
                                        }
                                    } else {
                                        //单纯的情况1：部门核算
                                        String sql_2 = "select * from t_ItemDetailV where FItemID = (select FItemID from t_Department where FNumber = '" + JD_depCode + "') and FDetailID in (select t1.FDetailID from (select FDetailID,COUNT(*) as cc from t_ItemDetailV  group by FDetailID) t1 where t1.cc <2)";
                                        writeLog("情况1===============" + sql_2);
                                        rsds.executeSql(sql_2);
                                        if (rsds.next()) {
                                            need_hsxm = Util.null2String(rsds.getString("FDetailID"));
                                        }
                                    }
                                }
                                
//							}
                              
                            }

                            rsds.executeSql("select * from t_Account where FNumber = '" + tmp_need_kmdm + "'");
                            writeLog("读取金蝶数据=====select * from t_Account where FNumber = " + tmp_need_kmdm);
                            if (rsds.next()) {
                                if ("".equals(need_kmID)) {
                                    need_kmID = Util.null2String(rsds.getString("FAccountID"));
                                }
                                tmp_need_kmid = Util.null2String(rsds.getString("FAccountID"));
                                writeLog("need_kmID =" + need_kmID);
                                writeLog("tmp_need_kmid=" + tmp_need_kmid);
                                if ("".equals(need_kmID) && need_kmID != null) {
                                    writeLog("科目代码===============" + tmp_need_kmdm);
                                    writeLog("关联关系未维护");
                                    return "-1";
                                }
                            }
                        }
                        String mx_sql = "insert into t_VoucherEntry(FAccountID,FAccountID2,FAmount,FAmountFor,FBrNo," +
                                "FCashFlowItem,FCurrencyID,FDC,FDetailID,FEntryID," +
                                "FExchangeRate,FExchangeRateType,FExplanation,FMeasureUnitID,FQuantity," +
                                "FResourceID,FSettleTypeID,FSideEntryID,FTaskID,FUnitPrice,FVoucherID) " +
                                " values " +
                                "('" + tmp_need_kmid + "','" + need_dflx + "','" + tmp_sbje + "','" + tmp_sbje + "','0'," +
                                "'0','1','1','" + need_hsxm + "','" + i + "'," +
                                "'1','1','" + depName + "-" + need_resName + "-" + need_fsrq + "-" + need_zy + "','0','" + need_fjs + "'," +
                                "'0','0','" + need_fls + "','0','0','" + maxID + "')";
                        writeLog("借方明细===============" + mx_sql);
                        rsds.executeSql(mx_sql);
                        i++;
                        // }
                    }

                    // add by shaw 2018-08-08 17:04:55 增加付款属性判断 start
                    String fdetailid = "";
                    // 个人报销
                    if ("0".equals(paymentType)) {
                        String sql_FDetailID = " select FDetailID from t_ItemDetailV where FItemClassID=3005 and FItemID " +
                                " in(select FItemID from t_Item where FNumber='" + workCode + "') ";
                        writeLog("sql_FDetailID=" + sql_FDetailID);
                        rsds.executeSql(sql_FDetailID);
                        if (rsds.next()) {
                            fdetailid = Util.null2String(rsds.getString("FDetailID"));
                        }

                        if ("".equals(fdetailid)) {
                            fdetailid = "0";
                        }
                        if("623".equals(m_ssfb))  {
                        	fdetailid = "28";
                        }
                    } else {
                        // 对公付款
                        fdetailid = "0";
                        dfzy += ";收款单位:" + receiverName;
                        writeLog("对公付款摘要：" + dfzy);
                    }

                    writeLog("fdetailid=" + fdetailid);
                    // add by shaw 2018-08-08 17:04:55 增加付款属性判断 end
                    String mx_last_sql = "insert into t_VoucherEntry(FAccountID,FAccountID2,FAmount,FAmountFor,FBrNo," +
                            "FCashFlowItem,FCurrencyID,FDC,FDetailID,FEntryID," +
                            "FExchangeRate,FExchangeRateType,FExplanation,FMeasureUnitID,FQuantity," +
                            "FResourceID,FSettleTypeID,FSideEntryID,FTaskID,FUnitPrice,FVoucherID) " +
                            " values " +
                            "('" + need_dflx + "','" + need_kmID + "','" + need_bxje + "','" + need_bxje + "','0'," +
                            "'0','1','0','" + fdetailid + "','" + i + "'," +
                            "'1','1','" + dfzy + "','0','" + need_m_fjs + "'," +
                            "'0','0','0','0','0','" + maxID + "')";
                    writeLog("贷方明细===============" + mx_last_sql);
                    rsds.executeSql(mx_last_sql);
                    String sql_up = "update " + tableName + " set " + f_drbz + " = '1' where requestid = '" + info.getRequestid() + "'";
                    writeLog("更新导入标志语句（成功）===============" + sql_up);
                    up.executeSql(sql_up);
                    sql_up = "update " + tableName + " set " + f_pzh + " = '" + need_pzh + "' where requestid = '" + info.getRequestid() + "'";
                    writeLog("插入凭证号语句===============" + sql_up);
                    up.executeSql(sql_up);
                } else {
                    errorMsg = "系统错误，关系表不存在！";
                    staticobj.putRecordToObj("budget_upd", "ErrorMsg", "OA ErrorMsg:[" + errorMsg + "]");
                    return "-1";
                }
            } else {
                writeLog("不需要导入的数据===============" + info.getRequestid());
            }
            return SUCCESS;
        }
    }

    /**
     * 开发技巧，一键开关日志，if(true)开启；if(false)关闭
     *
     * @param obj
     */
    public void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }

    /**
     * 去除list重复元素
     *
     * @param lists 参数
     * @return List
     */
    private static List<String> removeDuplicate(List<String> lists) {
        List<String> listTemp = new ArrayList<String>();
        for (String list : lists) {
            if (!listTemp.contains(list)) {
                listTemp.add(list);
            }
        }
        return listTemp;
    }
}
