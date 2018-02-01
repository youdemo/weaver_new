/**
 * OfsTodoDataWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.ofs.webservices;

public class OfsTodoDataWebServiceLocator extends org.apache.axis.client.Service implements cn.com.weaver.ofs.webservices.OfsTodoDataWebService {

    public OfsTodoDataWebServiceLocator() {
    }


    public OfsTodoDataWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public OfsTodoDataWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for OfsTodoDataWebServiceHttpPort
    private java.lang.String OfsTodoDataWebServiceHttpPort_address = "http://127.0.0.1:8082/services/OfsTodoDataWebService";

    public java.lang.String getOfsTodoDataWebServiceHttpPortAddress() {
        return OfsTodoDataWebServiceHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String OfsTodoDataWebServiceHttpPortWSDDServiceName = "OfsTodoDataWebServiceHttpPort";

    public java.lang.String getOfsTodoDataWebServiceHttpPortWSDDServiceName() {
        return OfsTodoDataWebServiceHttpPortWSDDServiceName;
    }

    public void setOfsTodoDataWebServiceHttpPortWSDDServiceName(java.lang.String name) {
        OfsTodoDataWebServiceHttpPortWSDDServiceName = name;
    }

    public cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType getOfsTodoDataWebServiceHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(OfsTodoDataWebServiceHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getOfsTodoDataWebServiceHttpPort(endpoint);
    }

    public cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType getOfsTodoDataWebServiceHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.com.weaver.ofs.webservices.OfsTodoDataWebServiceHttpBindingStub _stub = new cn.com.weaver.ofs.webservices.OfsTodoDataWebServiceHttpBindingStub(portAddress, this);
            _stub.setPortName(getOfsTodoDataWebServiceHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setOfsTodoDataWebServiceHttpPortEndpointAddress(java.lang.String address) {
        OfsTodoDataWebServiceHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.com.weaver.ofs.webservices.OfsTodoDataWebServiceHttpBindingStub _stub = new cn.com.weaver.ofs.webservices.OfsTodoDataWebServiceHttpBindingStub(new java.net.URL(OfsTodoDataWebServiceHttpPort_address), this);
                _stub.setPortName(getOfsTodoDataWebServiceHttpPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("OfsTodoDataWebServiceHttpPort".equals(inputPortName)) {
            return getOfsTodoDataWebServiceHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "OfsTodoDataWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "OfsTodoDataWebServiceHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("OfsTodoDataWebServiceHttpPort".equals(portName)) {
            setOfsTodoDataWebServiceHttpPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
