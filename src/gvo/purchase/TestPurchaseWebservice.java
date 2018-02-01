package gvo.purchase;

public class TestPurchaseWebservice {
    public String purchaseWebserviceMethod(String json) throws Exception {
    	String jsonPara = "";
        String result = "";
        SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub scpr = new SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub();
        SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.SAPPR_MM_0_CreatePRService sappr = new SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.SAPPR_MM_0_CreatePRService();
        sappr.setData(jsonPara);
        result = scpr.SAPPR_MM_0_CreatePRService(sappr).getMessage();
		return result;
    }
    public static void main(String[] args){
    	TestPurchaseWebservice pur = new TestPurchaseWebservice();
    	String json = "{\"bean\":[{\"PQTYPE\":\"1\",\"CHILD_CreatePR_SAP_1_LIST\":[{\"PROJECT\":\"1\",\"CLASSIFYCODE\":\"111\",\"MATERIALNO\":\"1123\",\"MATERIALDESC\":\"dfg\",\"PURCHASENUM\":\"2\",\"UNIT\":\"01\",\"OUTDATE\":\"2017-11-06 2017-11-07\",\"MAINCPTNO\":\"ddd\",\"COSTCENTER\":\"2223\",\"EXPACCOUNT\":\"12\",\"PRICE\":\"1.00\",\"CURRENCY\":\"CNY\",\"PRICE\":\"1.00\",\"CURRENCY\":\"CNY\",\"PRICE\":\"1.00\",\"CURRENCY\":\"CNY\",\"PRICEUNIT\":\"11\",\"GLANT1\":\"hn\",\"LOCATION\":\"sh\",\"PURGROUP\":\"123\",\"MATERIALGRO\":\"jk\",\"APPNAME\":\"ert\",\"REMARK\":\"wu\",\"INORDERNO\":\"3456\",\"KEEPERNAME\":\"dggh\",\"KEEPDEPTNAME\":\"cg\",\"DEPTNAME\":\"cg\"}]}";
    	String result = "";
        try {
            result = pur.purchaseWebserviceMethod(json);
            System.out.println("Result="+result);
        }catch (Exception e){
            System.out.println("ERROR");
        }
    }
}
