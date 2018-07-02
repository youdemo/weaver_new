
/**
 * ExceptionException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package gvo.travel;

public class ExceptionException0 extends Exception{
    
    private ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.ExceptionE faultMessage;
    
    public ExceptionException0() {
        super("ExceptionException0");
    }
           
    public ExceptionException0(String s) {
       super(s);
    }
    
    public ExceptionException0(String s, Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.ExceptionE msg){
       faultMessage = msg;
    }
    
    public ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.ExceptionE getFaultMessage(){
       return faultMessage;
    }
}
    