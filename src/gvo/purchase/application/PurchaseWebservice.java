package gvo.purchase.application;

import gvo.purchase.application.SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.Response;


public class PurchaseWebservice {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub scpr = new SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub();
		SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.SAPPR_MM_0_CreatePRService sappr = new SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.SAPPR_MM_0_CreatePRService();
		sappr.setData(json);
		Response result = scpr.SAPPR_MM_0_CreatePRService(sappr);
		return result;
	}

}
