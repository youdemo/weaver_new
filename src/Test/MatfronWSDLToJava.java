package Test;

import org.apache.axis2.wsdl.WSDL2Java;

public class MatfronWSDLToJava {
	public static void main(String[] args) throws Exception {
		
		String wsdl = "D:/axis2-1.4.1/ManageCustomer.wsdl";

		
	    WSDL2Java.main(new String[] { "-ssi", "-ap", "f", "--noBuildXML","-uri", wsdl });
	}
}
