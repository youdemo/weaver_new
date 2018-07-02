package feilida.util;

import feilida.cpt.ECologyServiceStub;
import feilida.cpt.ECologyServiceStub.*;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.axis2.AxisFault;

/**
 * Created by adore on 2016/12/30.
 */
public class CptBudgetUtilNew {

   /**
    * 调用查询接口获取预算信息
    * @param OADATE
    * @param AMOUNT
    * @param KOSTL
    * @param KSTAR
    * @param GSBER
    * @param CURRENCY
    * @param EXECTYPE
    * @param OPTTYPE
    * @param STAFFID
    * @param COMPID
    * @param DEPTID
    * @param ANLKL
    * @param OAKEY
    * @return
    * @throws Exception
    */
    public BudgetVO getBudgetResponse(String OADATE, String AMOUNT, String KOSTL, String KSTAR, String GSBER, String CURRENCY, String EXECTYPE
            , String OPTTYPE, int STAFFID, int COMPID, int DEPTID, String ANLKL, String OAKEY) throws Exception{
        BudgetVO list = null;
        try {
            ECologyServiceStub ess = new ECologyServiceStub();
            BudgetVO bv = new ECologyServiceStub.BudgetVO();
            ArrayOfBudgetVO ab = new ECologyServiceStub.ArrayOfBudgetVO();
            BigDecimal amount = new BigDecimal(Double.valueOf(AMOUNT));
            bv.setAMOUNT(amount);
            bv.setKSTAR(KSTAR);
            bv.setOADATE(str2Calendar(OADATE));
            bv.setKOSTL(KOSTL);
            bv.setGSBER(GSBER);
            bv.setOPTTYPE(OPTTYPE);
            bv.setSTAFFID(STAFFID);
            bv.setCOMPID(COMPID);
            bv.setCURRENCY(CURRENCY);
            bv.setDEPTID(DEPTID);
            bv.setEXECTYPE(EXECTYPE);
            bv.setANLKL(ANLKL);
            bv.setOAKey(OAKEY);
            bv.setGPKEY("");
            bv.setAMOUNT0(new BigDecimal("0"));
            bv.setAMOUNT1(new BigDecimal("0"));
            bv.setAMOUNT2(new BigDecimal("0"));
            bv.setAMOUNT3(new BigDecimal("0"));
            QueryBudget qb = new ECologyServiceStub.QueryBudget();
            ab.addBudgetVO(bv);
            qb.setBuget(ab);
            QueryBudgetResponse qbr = ess.QueryBudget(qb);

            ArrayOfBudgetVO xxx= qbr.getQueryBudgetResult();
            BudgetVO[] aa=xxx.getBudgetVO();
            //BudgetVO aaa = aa[0];
            list = aa[0];

            //result = qbr.getQueryBudgetResult().getBudgetVO().toString();
            //System.out.println("xxx="+aaa.getANLKL());
            return list;
        } catch (AxisFault e) {
            e.printStackTrace();
            return list;
        }
    }

    public static Calendar str2Calendar(String str) {
        //str="2012-5-27";
        Calendar calendar = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sdf.parse(str);
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return calendar;
    }


    
   public BudgetVO updateBudget(ArrayOfBudgetVO bvo) throws Exception{
	   ECologyServiceStub ess = new ECologyServiceStub();
	   UpdateBudget ub= new ECologyServiceStub.UpdateBudget();
	   ub.setBuget(bvo);
	   UpdateBudgetResponse qbr = ess.UpdateBudget(ub);
       ArrayOfBudgetVO xxx= qbr.getUpdateBudgetResult();
       BudgetVO[] aa=xxx.getBudgetVO();
       BudgetVO list = null;
       list = aa[0];
       return list;
       
   }
   
//   public static void main(String[] args) throws Exception{
//
//       CptBudgetUtilNew gci = new CptBudgetUtilNew();
//       String OADATE = "2016-12-31";
//       String AMOUNT = "10";
//       String KOSTL = "111";
//       String KSTAR = "6601540100";
//       String GSBER = "";
//       String CURRENCY = "";
//       String EXECTYPE = "";
//       String OPTTYPE = "";
//       int STAFFID = 1;
//       int COMPID = 1;
//       int DEPTID = 7817;
//       String ANLKL = "00210010";
//       String OAKEY = "";
//       String AMOUNT0="0";
//
//
//       BudgetVO json = gci.getBudgetResponse(OADATE, AMOUNT, KOSTL, KSTAR, GSBER, CURRENCY, EXECTYPE, OPTTYPE, STAFFID, COMPID, DEPTID, ANLKL, OAKEY);
//       System.out.println("AMOUNT0="+json.getAMOUNT1());
//       System.out.println("AMOUNT0="+json.getMSGTYP());
//       System.out.println("AMOUNT0="+json.getGPKEY());
//       System.out.println("AMOUNT0="+json.getMSGTXT());
//       System.out.println("AMOUNT0="+json.getKOSTL());
//   }
// public static void main(String[] args) throws Exception{
//
//     CptBudgetUtilNew gci = new CptBudgetUtilNew();
//     String OADATE = "2016-12-31";
//     String AMOUNT = "10";
//     String KOSTL = "";
//     String KSTAR = "6601540100";
//     String GSBER = "";
//     String CURRENCY = "";
//     String EXECTYPE = "";
//     String OPTTYPE = "0";
//     int STAFFID = 1;
//     int COMPID = 1;
//     int DEPTID = 7817;
//     String ANLKL = "6601540100";
//     String OAKEY = "1";
//     String AMOUNT0="0";
//     ArrayOfBudgetVO ab = new ECologyServiceStub.ArrayOfBudgetVO();
//		BudgetVO[] bvo = new BudgetVO[1];
//     BudgetVO bv = new ECologyServiceStub.BudgetVO();
//		bv.setAMOUNT(new BigDecimal(AMOUNT));
//		bv.setKSTAR(KSTAR);
//		bv.setOADATE(CptBudgetUtilNew.str2Calendar(OADATE));
//		bv.setKOSTL(KOSTL);
//		bv.setGSBER(GSBER);
//		bv.setOPTTYPE(OPTTYPE);
//		bv.setSTAFFID(STAFFID);
//		bv.setCOMPID(COMPID);
//		bv.setCURRENCY(CURRENCY);
//		bv.setDEPTID(DEPTID);
//		bv.setEXECTYPE(EXECTYPE);
//		bv.setANLKL(ANLKL);
//		bv.setOAKey(OAKEY);
//		bv.setGPKEY("");
//		bv.setAMOUNT0(new BigDecimal("0"));
//		bv.setAMOUNT1(new BigDecimal("0"));
//		bv.setAMOUNT2(new BigDecimal("0"));
//		bv.setAMOUNT3(new BigDecimal("0"));
//		bvo[0]=bv;
//		ab.setBudgetVO(bvo);
//
//
//     BudgetVO json = gci.updateBudget(ab);
//     System.out.println("AMOUNT0="+json.getAMOUNT1());
//     System.out.println("AMOUNT0="+json.getMSGTYP());
//     System.out.println("AMOUNT0="+json.getGPKEY());
//     System.out.println("AMOUNT0="+json.getMSGTXT());
//     System.out.println("AMOUNT0="+json.getKOSTL());
// }
}
