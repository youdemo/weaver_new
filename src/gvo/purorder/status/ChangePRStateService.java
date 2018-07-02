package gvo.purorder.status;

import gvo.purorder.status.SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.Response;


public class ChangePRStateService {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub crs = new SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub();
		SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.SAPPR_MM_0_ChangePRStateService cres = new SAPPR_MM_0_ChangePRStateService_pttBindingQSServiceStub.SAPPR_MM_0_ChangePRStateService();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_ChangePRStateService(cres);
		return result;
	}
}
