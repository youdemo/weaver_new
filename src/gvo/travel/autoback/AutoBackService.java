package gvo.travel.autoback;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;
import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;

public class AutoBackService {
	public Response getResultMethod(String json) throws Exception {
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub crsec = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoBackV0006 crs = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoBackV0006();
		crs.setData(json);
		Response result = crsec.AutoBackV0006(crs).getResponse();
		return result;
	}

}
