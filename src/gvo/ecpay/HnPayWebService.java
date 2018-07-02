package gvo.ecpay;

import gvo.ecpay.BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub.Response;

public class HnPayWebService {
	public Response getResultMethod(String json) throws Exception {
		BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub hnws = new BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub();
		BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub.BFSFI_WF_0_hnPayWebService hnpp = new BFSFI_WF_0_hnPayWebService_pttBindingQSServiceStub.BFSFI_WF_0_hnPayWebService();
		hnpp.setData(json);
		Response result = hnws.BFSFI_WF_0_hnPayWebService(hnpp);
		return result;
	}

}
