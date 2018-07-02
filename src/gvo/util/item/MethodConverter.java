package gvo.util.item;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
    
public class MethodConverter implements Converter {

	public boolean canConvert(Class clazz) {
	     return clazz.equals(Method.class);
	}
	 
	public void marshal(Object value, HierarchicalStreamWriter writer,
	   MarshallingContext context) {
	   Method method = (Method)value;
	   if (method!=null) {
	       writer.addAttribute("Action", method.getAction());
	       writer.setValue(method.getValue());
	   }
	}
	
	@Override
	public Object unmarshal(HierarchicalStreamReader reader,
	     UnmarshallingContext context) {
	     Method method = new Method();
	     String action = reader.getAttribute("Action");
	     method.setAction(action);
	     System.out.println(">>> Action: "+action);
	     String value = reader.getValue();
	     System.out.println(">>> method: "+value+", NodeName: "+reader.getNodeName());
	     method.setValue(value);
	      
	     return method;
	  }
}