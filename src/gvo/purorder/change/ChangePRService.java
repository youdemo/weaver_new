package gvo.purorder.change;

import gvo.purorder.change.SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub.Response;

import java.util.HashMap;
import java.util.Map;

public class ChangePRService {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub crs = new SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub();
		SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub.SAPPR_CG_0_ChangePRService cres = new SAPPR_CG_0_ChangePRService_pttBindingQSServiceStub.SAPPR_CG_0_ChangePRService();
		cres.setData(json);
		Response result = crs.SAPPR_CG_0_ChangePRService(cres);
		return result;

	}

//	public static void main(String[] args) {
//		ChangePRService order = new ChangePRService();
//		String sign = "";
//		String message = "";
//		String json = "<DATA><HEAD><BIZTRANSACTIONID></BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
//				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
//				+ "<datainfo>[{\"CHILD_ChangePRService_SAP_1_LIST\":[{\"MATERIALNO\":\"1\",\"PROJECT\":\"1\",\"NEWPRICE\":\"1\",\"LOEKZ1\":\"1\",\"NEWTAXCODE\":\"1\",\"NEWNUM\":\"1\",\"NEWOUTDATE\":\"1\"}],\"REQUESTID\":\"1\",\"ORDERNO\":\"1\"}]</datainfo></ITEM> </LIST></DATA>";
//		try {
//			Response result = order.getResultMethod(json);
//			sign = result.getSIGN();
//			message = result.getMessage();
//			System.out.println("sign=" + sign);
//			System.out.println("message=" + message);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
}
