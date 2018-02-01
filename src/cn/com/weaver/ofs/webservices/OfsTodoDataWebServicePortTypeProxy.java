package cn.com.weaver.ofs.webservices;

import weaver.interfaces.web.OfsClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
public class OfsTodoDataWebServicePortTypeProxy implements cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType {
	private static Log log = LogFactory.getLog(OfsTodoDataWebServicePortTypeProxy.class);
  private String _endpoint = null;
  private cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType ofsTodoDataWebServicePortType = null;
  
  public OfsTodoDataWebServicePortTypeProxy() {
    _initOfsTodoDataWebServicePortTypeProxy();
  }
  
  public OfsTodoDataWebServicePortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initOfsTodoDataWebServicePortTypeProxy();
  }
  
  private void _initOfsTodoDataWebServicePortTypeProxy() {
    try {
      ofsTodoDataWebServicePortType = (new cn.com.weaver.ofs.webservices.OfsTodoDataWebServiceLocator())
    		  	.getOfsTodoDataWebServiceHttpPort();
      if (ofsTodoDataWebServicePortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)ofsTodoDataWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)ofsTodoDataWebServicePortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (ofsTodoDataWebServicePortType != null)
      ((javax.xml.rpc.Stub)ofsTodoDataWebServicePortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType getOfsTodoDataWebServicePortType() {
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType;
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("receiveRequestInfoByMap");
		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveRequestInfoByMap(in0);
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processOverRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("processOverRequestByMap");

		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processOverRequestByMap(in0);
  }
  
  public java.lang.String receiveRequestInfoByXml(java.lang.String in0) throws java.rmi.RemoteException{
	  
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveRequestInfoByXml(in0);
  }
  
  public java.lang.String processDoneRequestByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processDoneRequestByJson(in0);
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteUserRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("deleteUserRequestInfoByMap");
		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteUserRequestInfoByMap(in0);
  }
  
  public java.lang.String deleteUserRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteUserRequestInfoByXML(in0);
  }
  
  public java.lang.String processDoneRequestByXml(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processDoneRequestByXml(in0);
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("deleteRequestInfoByMap");
		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteRequestInfoByMap(in0);
  }
  
  public java.lang.String processOverRequestByXml(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processOverRequestByXml(in0);
  }
  
  public java.lang.String receiveRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveRequestInfoByJson(in0);
  }
  
  public java.lang.String receiveTodoRequestByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveTodoRequestByJson(in0);
  }
  
  public java.lang.String deleteRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteRequestInfoByXML(in0);
  }
  
  public java.lang.String receiveTodoRequestByXml(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveTodoRequestByXml(in0);
  }
  
  public java.lang.String deleteUserRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteUserRequestInfoByJson(in0);
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processDoneRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("processDoneRequestByMap");

		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processDoneRequestByMap(in0);
  }
  
  public java.lang.String deleteRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.deleteRequestInfoByJson(in0);
  }
  
  public java.lang.String processOverRequestByJson(java.lang.String in0) throws java.rmi.RemoteException{
    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.processOverRequestByJson(in0);
  }
  
  public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveTodoRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException{
	  log.error("receiveTodoRequestByMap");
		OfsClient.printResultArray(in0);

    if (ofsTodoDataWebServicePortType == null)
      _initOfsTodoDataWebServicePortTypeProxy();
    return ofsTodoDataWebServicePortType.receiveTodoRequestByMap(in0);
  }
  
  
}