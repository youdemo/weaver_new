package gvo.purchase.revise;

import gvo.purchase.revise.SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub.Response;

public class RevisePRService {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub scpr = new SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub();
		SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub.SAPPR_MM_0_RevisePRService sappr = new SAPPR_MM_0_RevisePRService_pttBindingQSServiceStub.SAPPR_MM_0_RevisePRService();
		sappr.setData(json);
		Response result = scpr.SAPPR_MM_0_RevisePRService(sappr);
		return result;
	}

}
