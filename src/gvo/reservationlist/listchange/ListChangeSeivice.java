package gvo.reservationlist.listchange;

import gvo.reservationlist.listchange.SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.Response;


public class ListChangeSeivice {
	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub crs = new SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub();
		SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_ChangeReservationListService cres = new SAPPR_MM_0_ChangeReservationListService_pttBindingQSServiceStub.SAPPR_MM_0_ChangeReservationListService();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_ChangeReservationListService(cres);
		return result;
	}

//	public static void main(String[] args) throws Exception {
//		ListChangeSeivice list = new ListChangeSeivice();
//		String sign = "";
//		String message = "";
//		String json = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
//				+ "<DATA><HEAD><BIZTRANSACTIONID>?</BIZTRANSACTIONID> <COUNT>1</COUNT> <CONSUMER>EC</CONSUMER>   <SRVLEVEL>1</SRVLEVEL>"
//				+ "  <ACCOUNT></ACCOUNT> <PASSWORD></PASSWORD>  <USE></USE>   <COMMENTS>?</COMMENTS>     </HEAD>   <LIST>   <ITEM>"
//				+ " <datainfo>[{\"UMLGO\":\"1\",\"RSNUM\":\"1\",\"REQUESTID\":\"1\",\"ChangeReservationList_dt1\":[{\"RSPOS\":\"11\",\"BDMNG\":\"1\",\"DELETE_IND\":\"1\",\"MEASUREMENTUNIT\":\"1\","
//				+ "\"MOVEMENT\":\"1\",\"MATERIALDESCRIPTION\":\"1\",\"LGORT\":\"11\",\"CHARG\":\"11\",\"REQ_DATE\":\"1\"}],\"WEMPF\":\"1\"}]</datainfo>"
//				+ " </ITEM> </LIST></DATA>";
//		System.out.println("json=" + json);
//		Response result = list.getResultMethod(json);
//		try {
//			sign = result.getSIGN();
//			message = result.getMessage();
//			System.out.println("sign=" + sign);
//			System.out.println("message=" + message);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//
//	}
}
