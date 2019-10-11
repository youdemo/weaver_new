package morningcore.contract.util;

import morningcore.contract.bean.ChangeFieldBean;
import weaver.general.Util;

public class EditTransMethod {
	/**
	 * 
	 * @param fieldname
	 * @param type
	 * @param isCommon
	 * @return
	 */
	public String doChangeEditTrans(ChangeFieldBean pfb,String value){
		
		try {
			return getTransValue(pfb.getFieldname(),pfb.getFieldtype(),pfb.getTexttype(),pfb.getButtontype(),pfb.getTextlength(),pfb.getFloatdigit(),value,pfb.getTbname(),pfb);
		} catch (Exception e) {
			return "";
		}
	}
	
	

	public String getTransValue(String fieldname,String fieldtype,String texttype,String buttontype,String textlength,String floatdigit,String value,String tbName,ChangeFieldBean pfb) throws Exception{
		//BaseBean log = new BaseBean();
		TransUtil eu = new TransUtil();
		String result ="";
		
		if("0".equals(fieldtype)){
			result = eu.getTextTransResult(fieldname, texttype, textlength, String.valueOf(Util.getIntValue(floatdigit,-1)+1),value);
			
		}else if("1".equals(fieldtype)){
				result = new BrowserInfoUtil().getBrowserFieldValue(buttontype, value);
			
		}else if("2".equals(fieldtype)){
			result = new TransUtil().getSelectFieldHtml(fieldname, tbName, value);
			
		}else if("3".equals(fieldtype)){
			result = "";			
		}else if("5".equals(fieldtype)){
			result = new BrowserInfoUtil().getZDYFieldValue(pfb,value);
		}else if("6".equals(fieldtype)){
			result = new BrowserInfoUtil().getZDYFieldValue(pfb,value);
		}else{
			result = value;
		}
		return result;
	}
}
