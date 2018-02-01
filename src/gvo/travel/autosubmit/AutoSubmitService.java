package gvo.travel.autosubmit;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;

import java.util.HashMap;
import java.util.Map;

public class AutoSubmitService {
	public Map getResultMethod(String json) throws Exception {
		String sign = "";
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub ecfi = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoSubmitV0006 ecfiwf = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoSubmitV0006();
		ecfiwf.setData(json);
		sign = ecfi.AutoSubmitV0006(ecfiwf).getResponse().getSIGN();
		message = ecfi.AutoSubmitV0006(ecfiwf).getResponse().getMessage();
		map.put("sign", sign);
		map.put("message", message);
		return map;
	}

	public static void main(String[] args) {

		AutoSubmitService auto = new AutoSubmitService();
		String sign = "";
		String json = "<DATA><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
				+ "<requestid>4483</requestid></ITEM> </LIST></DATA>";
		String message = "";
		System.out.println("json=" + json);
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = auto.getResultMethod(json);
			sign = map.get("sign");
			message = map.get("message");
			System.out.println("sign=" + sign);
			System.out.println("message=" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
