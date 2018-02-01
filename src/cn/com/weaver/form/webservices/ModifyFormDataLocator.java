/**
 * ModifyFormDataLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.form.webservices;

public class ModifyFormDataLocator extends org.apache.axis.client.Service implements cn.com.weaver.form.webservices.ModifyFormData {

    public ModifyFormDataLocator() {
    }


    public ModifyFormDataLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public ModifyFormDataLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for ModifyFormDataHttpPort
    private java.lang.String ModifyFormDataHttpPort_address = "http://10.118.22.101:8080//services/ModifyFormData";

    public java.lang.String getModifyFormDataHttpPortAddress() {
        return ModifyFormDataHttpPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String ModifyFormDataHttpPortWSDDServiceName = "ModifyFormDataHttpPort";

    public java.lang.String getModifyFormDataHttpPortWSDDServiceName() {
        return ModifyFormDataHttpPortWSDDServiceName;
    }

    public void setModifyFormDataHttpPortWSDDServiceName(java.lang.String name) {
        ModifyFormDataHttpPortWSDDServiceName = name;
    }

    public cn.com.weaver.form.webservices.ModifyFormDataPortType getModifyFormDataHttpPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(ModifyFormDataHttpPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getModifyFormDataHttpPort(endpoint);
    }

    public cn.com.weaver.form.webservices.ModifyFormDataPortType getModifyFormDataHttpPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            cn.com.weaver.form.webservices.ModifyFormDataHttpBindingStub _stub = new cn.com.weaver.form.webservices.ModifyFormDataHttpBindingStub(portAddress, this);
            _stub.setPortName(getModifyFormDataHttpPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setModifyFormDataHttpPortEndpointAddress(java.lang.String address) {
        ModifyFormDataHttpPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (cn.com.weaver.form.webservices.ModifyFormDataPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                cn.com.weaver.form.webservices.ModifyFormDataHttpBindingStub _stub = new cn.com.weaver.form.webservices.ModifyFormDataHttpBindingStub(new java.net.URL(ModifyFormDataHttpPort_address), this);
                _stub.setPortName(getModifyFormDataHttpPortWSDDServiceName());
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
        if ("ModifyFormDataHttpPort".equals(inputPortName)) {
            return getModifyFormDataHttpPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("webservices.form.weaver.com.cn", "ModifyFormData");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("webservices.form.weaver.com.cn", "ModifyFormDataHttpPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("ModifyFormDataHttpPort".equals(portName)) {
            setModifyFormDataHttpPortEndpointAddress(address);
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
