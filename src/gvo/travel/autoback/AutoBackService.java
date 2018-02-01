package gvo.travel.autoback;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;

import java.util.HashMap;
import java.util.Map;

public class AutoBackService {
	public Map getResultMethod(String json) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub crsec = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoBackV0006 crs = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.AutoBackV0006();
		crs.setData(json);
		String sign = "";
		String message = "";
		sign = crsec.AutoBackV0006(crs).getResponse().getSIGN();
		message = crsec.AutoBackV0006(crs).getResponse().getMessage();
		map.put("sign", sign);
		map.put("message", message);
		return map;
	}

	public static void main(String[] args) {
		AutoBackService back = new AutoBackService();
		String json = "<DATA><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
				+ " <requestid>4483</requestid> </ITEM> </LIST></DATA>";
		System.out.println("json=" + json);
		String sign = "";
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = back.getResultMethod(json);
			sign = map.get("sign");
			message = map.get("message");
			System.out.println("sign=" + sign);
			System.out.println("message=" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
