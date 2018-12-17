package weaver.interfaces.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import weaver.general.BaseBean;
import weaver.general.Util;

import weaver.file.Prop;

import cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry;
import cn.com.weaver.ofs.webservices.OfsTodoDataWebServicePortTypeProxy;

public class OfsClient extends BaseBean {
	public static final String WSURL = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "wsurl"));
	
	
	/**
	 * 获取客户端
	 * @return
	 */
	public static OfsTodoDataWebServicePortTypeProxy getClient(){
		new BaseBean().writeLog("配置的webservice URL是"+WSURL);
		return new OfsTodoDataWebServicePortTypeProxy(WSURL);
	}
	
	public static AnyType2AnyTypeMapEntry[] buildDataArray(Map<String,String> dataMap){
		List<AnyType2AnyTypeMapEntry> list = new ArrayList<AnyType2AnyTypeMapEntry>();
		//遍历map中的键  
		for (String key : dataMap.keySet()) {  
			AnyType2AnyTypeMapEntry entry = new AnyType2AnyTypeMapEntry();
			entry.setKey(key);
			entry.setValue(dataMap.get(key));
			list.add(entry);
		}  
		
		Object[] array = list.toArray();
		AnyType2AnyTypeMapEntry[] resultArray = new AnyType2AnyTypeMapEntry[array.length];
		
		for(int i = 0;i<resultArray.length;i++){
			resultArray[i] = (AnyType2AnyTypeMapEntry)array[i];
		}
		return resultArray;
	}
	
	public static void printResultArray(AnyType2AnyTypeMapEntry[] resultArray){
		//遍历map中的键  
		for (int i=0;i<resultArray.length;i++) {  
//			System.out.println("key="+resultArray[i].getKey()+" , value="+resultArray[i].getValue());
//			new BaseBean().writeLog("key="+resultArray[i].getKey()+" , value="+resultArray[i].getValue());
		} 
		new BaseBean().writeLog("---------------------分割线----------------------");
	}
}
