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

}
