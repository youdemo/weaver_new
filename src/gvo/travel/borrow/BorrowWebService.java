package gvo.travel.borrow;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;
import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;

public class BorrowWebService {
	public Response getResultMethod(String json) throws Exception {
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub crsec = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateBrrowService crs = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateBrrowService();
		crs.setData(json);
		Response result = crsec.CreateBrrowService(crs).getResponse();
		return result;
	}

}
