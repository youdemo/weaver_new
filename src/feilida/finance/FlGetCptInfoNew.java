package feilida.finance;

import feilida.util.WebApi;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by adore on 2016/12/30.
 */
public class FlGetCptInfoNew {
	
	 public StringBuffer getCptInfoNew(String OADATE, String AMOUNT, String KOSTL, String KSTAR, String GSBER, String CURRENCY, String EXECTYPE
	            , String OPTTYPE, int STAFFID, int COMPID, int DEPTID, String ANLKL, String OAKEY,String Project) throws Exception{
	    	WebApi wb = new WebApi();
	    	Map<String,String> mapStr = new HashMap<String, String>();
	    	OADATE = OADATE.replace("-", ".");
	    	mapStr.put("OADATE", OADATE);
	    	mapStr.put("KOSTL", KOSTL);
	    	mapStr.put("PROJECT", Project);
	    	mapStr.put("GSBER", GSBER);
	    	mapStr.put("KSTAR", KSTAR);
	    	mapStr.put("OAKey", OAKEY);
	    	StringBuffer buffer = new StringBuffer();
	    	String result  = wb.getPostConn("QueryBudgetCode", wb.getJsonStr(mapStr));
	    	buffer.append(result);
	    	return buffer;
	 }
	
    public StringBuffer getCptInfoNew(String OADATE, String AMOUNT, String KOSTL, String KSTAR, String GSBER, String CURRENCY, String EXECTYPE
            , String OPTTYPE, int STAFFID, int COMPID, int DEPTID, String ANLKL, String OAKEY) throws Exception{
    	WebApi wb = new WebApi();
    	Map<String,String> mapStr = new HashMap<String, String>();
    	mapStr.put("OADATE", OADATE);
    	mapStr.put("KOSTL", KOSTL);
    	mapStr.put("PROJECT", ANLKL);
    	mapStr.put("GSBER", GSBER);
    	mapStr.put("KSTAR", KSTAR);
    	mapStr.put("OAKey", OAKEY);
    	StringBuffer buffer = new StringBuffer();
    	String result  = wb.getPostConn("QueryBudgetCode", wb.getJsonStr(mapStr));
    	buffer.append(result);
    	return buffer;
    	//StringBuffer buffer = new StringBuffer();
    	// CptBudgetUtilNew cbu = new CptBudgetUtilNew();
       /*
    	try{
            ECologyServiceStub.BudgetVO list = cbu.getBudgetResponse(OADATE, AMOUNT, KOSTL, KSTAR, GSBER, CURRENCY, EXECTYPE, 
            			OPTTYPE, STAFFID, COMPID, DEPTID, ANLKL, OAKEY);

            buffer.append("{");
            buffer.append("AMOUNT0");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getAMOUNT0());
            buffer.append("'");
            buffer.append(",");
            buffer.append("AMOUNT1");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getAMOUNT1());
            buffer.append("'");
            buffer.append(",");
            buffer.append("AMOUNT2");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getAMOUNT2());
            buffer.append("'");
            buffer.append(",");
            buffer.append("AMOUNT3");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getAMOUNT3());
            buffer.append("'");
            buffer.append(",");
            buffer.append("KOSTL");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getKOSTL());
            buffer.append("'");
            buffer.append(",");
            buffer.append("MSGTYP");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getMSGTYP());
            buffer.append("'");
            buffer.append(",");
            buffer.append("MSGTXT");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getMSGTXT());
            buffer.append("'");
            buffer.append(",");
            buffer.append("GPKEY");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getGPKEY());
            buffer.append("'");
            buffer.append(",");
            buffer.append("EXTSTRING");
            buffer.append(":");
            buffer.append("'");
            buffer.append(list.getEXTSTRING()); 
            buffer.append("'");
            buffer.append("}");
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return buffer;
        }
        */
    	
    }

//    public static void main(String[] args) throws Exception{
//
//        FlGetCptInfoNew gci = new FlGetCptInfoNew();
//        String OADATE = "2017-12-21";
//        String AMOUNT = "10";
//        String KOSTL = "";
//        String KSTAR = "";
//        String GSBER = "";
//        String CURRENCY = "";
//        String EXECTYPE = "1";
//        String OPTTYPE = "";
//        int STAFFID = 1;
//        int COMPID = 950;
//        int DEPTID = 7817;
//        String ANLKL = "00210020";
//        String OAKEY = "";
//        //gci.getCptInfo();
//
//        try {
//            StringBuffer json = new StringBuffer();
//            json = gci.getCptInfoNew(OADATE, AMOUNT, KOSTL, KSTAR, GSBER, CURRENCY, EXECTYPE, OPTTYPE, STAFFID, COMPID, DEPTID, ANLKL, OAKEY);
//            System.out.println(json.toString());
//        }
//        catch(IOException e) {
//            System.out.println("Exception encountered: " + e);
//        }
//
//    }

}
