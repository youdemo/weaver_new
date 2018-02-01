package gvo.travel.borrow;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub;

import java.util.HashMap;
import java.util.Map;

public class BorrowWebService {
	public Map getResultMethod(String json) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub crsec = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub();
		ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateBrrowService crs = new ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.CreateBrrowService();
		crs.setData(json);
		String sign = "";
		String message = "";
		sign = crsec.CreateBrrowService(crs).getResponse().getSIGN();
		message = crsec.CreateBrrowService(crs).getResponse().getMessage();
		map.put("sign", sign);
		map.put("message", message);
		return map;
	}

	public static void main(String[] args) {
		BorrowWebService loan = new BorrowWebService();
		String json = "<DATA><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
				+ " <workcode>3448</workcode>  <datainfo>{\"DETAILS\":{},\"HEADER\": {\"txr\":\"3448\",\"szgs\":\"70000\",\"szbm\":\"70000501\",\"sqr\":\"3448\",\"sqrq\":\"2017-10-12\",\"oarqid\":\"708328\",\"jkje\":\"100\""
				+ "}  }</datainfo>" + " </ITEM> </LIST></DATA>";
		System.out.println("json=" + json);
		String sign = "";
		String message = "";
		Map<String, String> map = new HashMap<String, String>();
		try {
			map = loan.getResultMethod(json);
			sign = map.get("sign");
			message = map.get("message");
			System.out.println("sign=" + sign);
			System.out.println("message=" + message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
