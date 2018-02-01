/**
 * ReceiveRequestInfoByMapResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.ofs.webservices;

public class ReceiveRequestInfoByMapResponse  implements java.io.Serializable {
    private cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] out;

    public ReceiveRequestInfoByMapResponse() {
    }

    public ReceiveRequestInfoByMapResponse(
           cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] out) {
           this.out = out;
    }


    /**
     * Gets the out value for this ReceiveRequestInfoByMapResponse.
     * 
     * @return out
     */
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] getOut() {
        return out;
    }


    /**
     * Sets the out value for this ReceiveRequestInfoByMapResponse.
     * 
     * @param out
     */
    public void setOut(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] out) {
        this.out = out;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReceiveRequestInfoByMapResponse)) return false;
        ReceiveRequestInfoByMapResponse other = (ReceiveRequestInfoByMapResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.out==null && other.getOut()==null) || 
             (this.out!=null &&
              java.util.Arrays.equals(this.out, other.getOut())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getOut() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getOut());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getOut(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReceiveRequestInfoByMapResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">receiveRequestInfoByMapResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("out");
        elemField.setXmlName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "out"));
        elemField.setXmlType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">anyType2anyTypeMap>entry"));
        elemField.setNillable(true);
        elemField.setItemQName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "entry"));
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
