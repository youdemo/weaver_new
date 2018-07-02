package gvo.travel.application;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;
import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;

public class TravelReimWebService {
	public Response getResultMethod(String json) throws Exception {
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub ecfi = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateECV0006Service ecfiwf = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateECV0006Service();
		ecfiwf.setData(json);
		Response result = ecfi.CreateECV0006Service(ecfiwf).getResponse();
		return result;
	}

}
