package gvo.wsclient.hr;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.transport.http.HTTPConstants;
import org.apache.axis2.transport.http.HttpTransportProperties;
import weaver.general.BaseBean;
/**
 * @para 传入数据
 * @url 创建服务地址WebService的URL,注意不是WSDL的URL
 * @param （wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
 * @Namespace wsdl文档的targetNamespace，命名空间
 * @meth 指定方法
 */
public class EmployApprovalService {
    public String MobileCodeWS(String para) {
        BaseBean log = new BaseBean();
        try {
            ServiceClient serviceClient = new ServiceClient();
            HttpTransportProperties.Authenticator auth = new HttpTransportProperties.Authenticator();
            auth.setUsername("USEROA");
            auth.setPassword("PassOA1234");
            log.writeLog(auth.getUsername());
            log.writeLog(auth.getPassword());
            serviceClient.getOptions().setProperty(HTTPConstants.AUTHENTICATE, auth);

            //创建服务地址WebService的URL,注意不是WSDL的URL
            String url = "http://10.1.32.151:8010/WP_HNYG/APP_IFC_SERVICE/Proxy_Services/TA_OA/VXG-104_IFC_0_EmploymentApprovalService_PS";
            EndpointReference targetEPR = new EndpointReference(url);
            Options options = serviceClient.getOptions();
            options.setTo(targetEPR);
            //确定调用方法（wsdl 命名空间地址 (wsdl文档中的targetNamespace) 和 方法名称 的组合）
            options.setAction("http://www.ekingwin.com/esb/VXG-104_IFC_0_EmploymentApprovalService/VXG-104_IFC_0_EmploymentApprovalService");
            OMFactory fac = OMAbstractFactory.getOMFactory();
            /*
             * 指定命名空间，参数：
             * uri--即为wsdl文档的targetNamespace，命名空间
             * perfix--可不填
             */
            OMNamespace omNs = fac.createOMNamespace("http://www.ekingwin.com/esb/VXG-104_IFC_0_EmploymentApprovalService", "");
            // 指定方法
            OMElement method = fac.createOMElement("VXG-104_IFC_0_EmploymentApprovalService", omNs);
            // 指定方法的参数
            OMElement mobileCode = fac.createOMElement("Data", omNs);
            mobileCode.setText(para);
            // OMElement userID = fac.createOMElement("userID", omNs);
            // userID.setText("");
            method.addChild(mobileCode);
            //method.addChild(userID);
            method.build();

            //远程调用web服务
            OMElement result = serviceClient.sendReceive(method);
            serviceClient.cleanupTransport();
            serviceClient.cleanup();
            log.writeLog("result-->" + result);
            return result.toString();
        } catch (AxisFault axisFault) {
            axisFault.printStackTrace();
            log.writeLog("axisFault-->" + axisFault.getMessage());
        }
        return "";
    }
}
