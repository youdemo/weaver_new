package cn.com.weaver.form.webservices;

public class ModifyFormDataPortTypeProxy implements cn.com.weaver.form.webservices.ModifyFormDataPortType {
  private String _endpoint = null;
  private cn.com.weaver.form.webservices.ModifyFormDataPortType modifyFormDataPortType = null;
  
  public ModifyFormDataPortTypeProxy() {
    _initModifyFormDataPortTypeProxy();
  }
  
  public ModifyFormDataPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initModifyFormDataPortTypeProxy();
  }
  
  private void _initModifyFormDataPortTypeProxy() {
    try {
      modifyFormDataPortType = (new cn.com.weaver.form.webservices.ModifyFormDataLocator()).getModifyFormDataHttpPort();
      if (modifyFormDataPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)modifyFormDataPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)modifyFormDataPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (modifyFormDataPortType != null)
      ((javax.xml.rpc.Stub)modifyFormDataPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.com.weaver.form.webservices.ModifyFormDataPortType getModifyFormDataPortType() {
    if (modifyFormDataPortType == null)
      _initModifyFormDataPortTypeProxy();
    return modifyFormDataPortType;
  }
  
  public java.lang.String modeifyData(java.lang.String in0, cn.com.weaver.form.webservices.AnyType2AnyTypeMapEntry[] in1) throws java.rmi.RemoteException{
    if (modifyFormDataPortType == null)
      _initModifyFormDataPortTypeProxy();
    return modifyFormDataPortType.modeifyData(in0, in1);
  }
  
  
}