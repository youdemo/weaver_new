/**
 * OfsTodoDataWebServiceHttpBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.ofs.webservices;

public class OfsTodoDataWebServiceHttpBindingStub extends org.apache.axis.client.Stub implements cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[18];
        _initOperationDesc1();
        _initOperationDesc2();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveRequestInfoByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processOverRequestByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveRequestInfoByXml");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processDoneRequestByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteUserRequestInfoByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteUserRequestInfoByXML");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[5] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processDoneRequestByXml");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[6] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteRequestInfoByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[7] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processOverRequestByXml");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[8] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveRequestInfoByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[9] = oper;

    }

    private static void _initOperationDesc2(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveTodoRequestByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[10] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteRequestInfoByXML");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[11] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveTodoRequestByXml");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[12] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteUserRequestInfoByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[13] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processDoneRequestByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[14] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("deleteRequestInfoByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[15] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("processOverRequestByJson");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        oper.setReturnClass(java.lang.String.class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[16] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("receiveTodoRequestByMap");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"), cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class, false, false);
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        param.setNillable(true);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap"));
        oper.setReturnClass(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
        oper.setReturnQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        param = oper.getReturnParamDesc();
        param.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[17] = oper;

    }

    public OfsTodoDataWebServiceHttpBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public OfsTodoDataWebServiceHttpBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public OfsTodoDataWebServiceHttpBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">anyType2anyTypeMap>entry");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByXML");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByXML.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByXMLResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteRequestInfoByXMLResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByXML");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByXML.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteUserRequestInfoByXMLResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.DeleteUserRequestInfoByXMLResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByXml");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByXml.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processDoneRequestByXmlResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessDoneRequestByXmlResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByXml");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByXml.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">processOverRequestByXmlResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ProcessOverRequestByXmlResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByXml");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByXml.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByXmlResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveRequestInfoByXmlResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByJson");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByJson.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByJsonResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByJsonResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByMap.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByMapResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByMapResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByXml");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByXml.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveTodoRequestByXmlResponse");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.ReceiveTodoRequestByXmlResponse.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "anyType2anyTypeMap");
            cachedSerQNames.add(qName);
            cls = cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">anyType2anyTypeMap>entry");
            qName2 = new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveRequestInfoByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processOverRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processOverRequestByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String receiveRequestInfoByXml(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveRequestInfoByXml"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String processDoneRequestByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processDoneRequestByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteUserRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteUserRequestInfoByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String deleteUserRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[5]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteUserRequestInfoByXML"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String processDoneRequestByXml(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[6]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processDoneRequestByXml"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] deleteRequestInfoByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[7]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteRequestInfoByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String processOverRequestByXml(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[8]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processOverRequestByXml"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String receiveRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[9]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveRequestInfoByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String receiveTodoRequestByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[10]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveTodoRequestByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String deleteRequestInfoByXML(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[11]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteRequestInfoByXML"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String receiveTodoRequestByXml(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[12]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveTodoRequestByXml"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String deleteUserRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[13]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteUserRequestInfoByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] processDoneRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[14]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processDoneRequestByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String deleteRequestInfoByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[15]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "deleteRequestInfoByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public java.lang.String processOverRequestByJson(java.lang.String in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[16]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "processOverRequestByJson"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (java.lang.String) _resp;
            } catch (java.lang.Exception _exception) {
                return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] receiveTodoRequestByMap(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[17]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "receiveTodoRequestByMap"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {in0});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[]) org.apache.axis.utils.JavaUtils.convert(_resp, cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
