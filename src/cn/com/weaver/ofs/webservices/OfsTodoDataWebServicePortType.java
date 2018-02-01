/**
 * OfsTodoDataWebServicePortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.ofs.webservices;

public interface OfsTodoDataWebServicePortType extends java.rmi.Remote {
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processOverRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
    public java.lang.String receiveRequestInfoByXml(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String processDoneRequestByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteUserRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
    public java.lang.String deleteUserRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String processDoneRequestByXml(java.lang.String in0) throws java.rmi.RemoteException;
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
    public java.lang.String processOverRequestByXml(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String receiveRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String receiveTodoRequestByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String deleteRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String receiveTodoRequestByXml(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String deleteUserRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processDoneRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
    public java.lang.String deleteRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public java.lang.String processOverRequestByJson(java.lang.String in0) throws java.rmi.RemoteException;
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveTodoRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException;
}
