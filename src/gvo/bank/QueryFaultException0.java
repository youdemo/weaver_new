
/**
 * QueryFaultException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package gvo.bank;

public class QueryFaultException0 extends Exception{
    
    private BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.QueryFault faultMessage;
    
    public QueryFaultException0() {
        super("QueryFaultException0");
    }
           
    public QueryFaultException0(String s) {
       super(s);
    }
    
    public QueryFaultException0(String s, Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.QueryFault msg){
       faultMessage = msg;
    }
    
    public BFSFI_WF_0_bankAccServiceWebService_pttBindingQSServiceStub.QueryFault getFaultMessage(){
       return faultMessage;
    }
}
    