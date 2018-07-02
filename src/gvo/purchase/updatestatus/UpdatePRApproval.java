package gvo.purchase.updatestatus;

import gvo.purchase.updatestatus.SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub.Response;

import java.util.HashMap;
import java.util.Map;

public class UpdatePRApproval {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub scpr = new SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub();
		SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub.SAPPR_MM_0_UpdatePRApproval sappr = new SAPPR_MM_0_UpdatePRApproval_pttBindingQSServiceStub.SAPPR_MM_0_UpdatePRApproval();
		sappr.setData(json);
		Response result = scpr.SAPPR_MM_0_UpdatePRApproval(sappr);
		return result;
	}

}
