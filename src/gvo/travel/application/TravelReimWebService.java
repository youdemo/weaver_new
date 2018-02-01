package gvo.travel.application;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;

import java.util.HashMap;
import java.util.Map;

public class TravelReimWebService {
	public Map getResultMethod(String json) throws Exception {
		String sign = "";
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub ecfi = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateECV0006Service ecfiwf = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateECV0006Service();
		ecfiwf.setData(json);
		sign = ecfi.CreateECV0006Service(ecfiwf).getResponse().getSIGN();
		message = ecfi.CreateECV0006Service(ecfiwf).getResponse().getMessage();
		map.put("sign", sign);
		map.put("message", message);
		return map;
	}

	public static void main(String[] args) {
		TravelReimWebService tral = new TravelReimWebService();
		String json = "<DATA><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
				+ " <workcode>3448</workcode>  <datainfo>[{\"DETAILS\":{\"DT1\": [{\"ndkyys\": \"94511\",\"fycd\": \"101\",\"fyssqj\": \"2017-10-12\",\"oamxid\": \"4\",\"ydkyys\": \"0\",\"yssqje\": \"100\","
				+ "\"jtsy\": \"\",\"yskm\": \"263\"}]},\"HEADER\": {\"fycdbm\": \"700005\",\"txr\": \"3448\",\"szgs\": \"70000\",\"sqr\": \"3448\",\"sqrq\": \"2017-10-12\", \"oarqid\": \"708328\", \"fyys\": \"100\""
				+ "}  }]</datainfo></ITEM> </LIST></DATA>";
		System.out.println("json=" + json);
		String sign = "";
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = tral.getResultMethod(json);
			sign = map.get("sign");
			message = map.get("message");
			System.out.println("sign=" + sign);
			System.out.println("message=" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
