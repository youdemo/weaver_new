package gvo.travel.autosubmit;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;
import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;

public class AutoSubmitService {
	public Response getResultMethod(String json) throws Exception {
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub ecfi = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoSubmitV0006 ecfiwf = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoSubmitV0006();
		ecfiwf.setData(json);
		Response result = ecfi.AutoSubmitV0006(ecfiwf).getResponse();
		return result;
	}

}
