package gvo.purchase.information;

import gvo.purchase.information.SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub.Response;

import java.util.HashMap;
import java.util.Map;

public class UpdatePurchasingInfo {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub scpr = new SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub();
		SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub.SAPPR_MM_0_UpdatePurchasingInfo sappr = new SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub.SAPPR_MM_0_UpdatePurchasingInfo();
		sappr.setData(json);
		Response result = scpr.SAPPR_MM_0_UpdatePurchasingInfo(sappr);
		return result;
	}
}
