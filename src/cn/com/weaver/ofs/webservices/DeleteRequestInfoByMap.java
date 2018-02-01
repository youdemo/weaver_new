/**
 * DeleteRequestInfoByMap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package cn.com.weaver.ofs.webservices;

public class DeleteRequestInfoByMap  implements java.io.Serializable {
    private cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0;

    public DeleteRequestInfoByMap() {
    }

    public DeleteRequestInfoByMap(
           cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) {
           this.in0 = in0;
    }


    /**
     * Gets the in0 value for this DeleteRequestInfoByMap.
     * 
     * @return in0
     */
    public cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] getIn0() {
        return in0;
    }


    /**
     * Sets the in0 value for this DeleteRequestInfoByMap.
     * 
     * @param in0
     */
    public void setIn0(cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry[] in0) {
        this.in0 = in0;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof DeleteRequestInfoByMap)) return false;
        DeleteRequestInfoByMap other = (DeleteRequestInfoByMap) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.in0==null && other.getIn0()==null) || 
             (this.in0!=null &&
              java.util.Arrays.equals(this.in0, other.getIn0())));
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
        if (getIn0() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getIn0());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getIn0(), i);
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
        new org.apache.axis.description.TypeDesc(DeleteRequestInfoByMap.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", ">deleteRequestInfoByMap"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("in0");
        elemField.setXmlName(new javax.xml.namespace.QName("webservices.ofs.weaver.com.cn", "in0"));
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
