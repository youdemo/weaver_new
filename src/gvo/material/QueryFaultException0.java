
/**
 * QueryFaultException0.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.4.1  Built on : Aug 13, 2008 (05:03:35 LKT)
 */

package gvo.material;

public class QueryFaultException0 extends Exception{
    
    private gvo.material.SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.QueryFault faultMessage;
    
    public QueryFaultException0() {
        super("QueryFaultException0");
    }
           
    public QueryFaultException0(String s) {
       super(s);
    }
    
    public QueryFaultException0(String s, Throwable ex) {
      super(s, ex);
    }
    
    public void setFaultMessage(gvo.material.SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.QueryFault msg){
       faultMessage = msg;
    }
    
    public gvo.material.SAPPR_MM_0_MaterialAdmitInfo_pttBindingQSServiceStub.QueryFault getFaultMessage(){
       return faultMessage;
    }
}
    