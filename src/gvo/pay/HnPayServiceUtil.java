package gvo.pay;

/**
 * 资金对接付款通用方法
 * @author adore
 * @version 1.0  2017-09-18
 * 
 **/
public class HnPayServiceUtil {

    public String hnPayService(String json) throws Exception {

        HnPayWebServiceStub hwss = new HnPayWebServiceStub();
        HnPayWebServiceStub.HnPayWS hpw = new HnPayWebServiceStub.HnPayWS();
        hpw.setXmlPay(json);

        String response = hwss.hnPayWS(hpw).get_return();
        return response;
    }

    public static void main(String[] args){
        HnPayServiceUtil hpsu = new HnPayServiceUtil();
        String json = "{\"bean\":[{\"rmk\":\"V001220170029\",\"payee_bank\":\"民生银行昆山支行\",\"voucher_type\":\"11\"," +
        		"\"serial_no_erp\":\"V001220170029\",\"wish_pay_day\":\"2017-10-14\",\"corp_code\":\"1100\",\"payer_acc_no\":\"05141900000018\"," +
        		"\"purpose\":\"wfdf\",\"gysdm\":\"B0322\",\"payee_name\":\"张盼盼\",\"amt\":\"200\",\"system_type\":\"0\",\"abs\":\"yhfg\"," +
        		"\"item_code\":\"2324\",\"zzbs\":\"\",\"payee_code\":\"4569\",\"req_date\":\"2017-10-14\",\"urgency_flag\":\"0\",\"zzkm\":\"\"," +
        		"\"jzdm\":\"21\",\"payee_acc_no\":\"6226202600976945\",\"cur\":\"CNY\",\"isforindividual\":\"1\",\"fkyydm\":\"201\"}]}";
        
        String result = "";
        try {
            result = hpsu.hnPayService(json);
            System.out.println("Result="+result);
        }catch (Exception e){
            System.out.println("ERROR");
        }
    }
}
