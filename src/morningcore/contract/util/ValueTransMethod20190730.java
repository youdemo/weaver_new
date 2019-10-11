package morningcore.contract.util;

import morningcore.contract.bean.ChangeFieldBean;
import morningcore.contract.dao.ChangeFieldDao;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class ValueTransMethod20190730 {
	/**
	 * 
	 * @param fieldname
	 * @param value
	 * @param type 0 项目信息 1 过程
	 */
	public String doTrans(String fieldid,String value){
		String tablename = "";
		String fieldtype = "";
		String texttype = "";
		String buttontype = "";
		RecordSet rs = new RecordSet();
		if(value==null||"".equals(value)){
			return "";
		}
		tablename = "uf_change_field_mt";
		
		String sql="select  fieldtype,texttype,buttontype from "+tablename+" where id='"+fieldid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			fieldtype = Util.null2String(rs.getString("fieldtype"));
			texttype = Util.null2String(rs.getString("texttype"));
			buttontype = Util.null2String(rs.getString("buttontype"));
		}
		try {
			return getTransValue(fieldtype,texttype,buttontype,value,fieldid);
		} catch (Exception e) {
			return value;
		}
	}
	
	public String getTransValue(String fieldtype,String texttype,String buttontype,String value,String fieldid) throws Exception{
		//BaseBean log = new BaseBean();
		String result ="";
		if("0".equals(fieldtype)){
			result = value;
		}else if("1".equals(fieldtype)){
			result = new BrowserInfoUtil().getBrowserFieldValue(buttontype, value);
		}else if("2".equals(fieldtype)){
			result = new TransUtil().getSelectValue(fieldid, value);
		}else if("3".equals(fieldtype)){
			result = new TransUtil().getAttachUrl(value);
		}else if("4".equals(fieldtype)){
			result = value;
		}else{
			result = value;
		}
		return result;
	}
	
	public String doTrans2(String fieldid,String value){
		String tablename = "";
		String fieldtype = "";
		String texttype = "";
		String buttontype = "";
		RecordSet rs = new RecordSet();
		if(value==null||"".equals(value)){
			return "";
		}
		tablename = "uf_change_field_mt";
		String sql="select  fieldtype,texttype,buttontype from "+tablename+" where id='"+fieldid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			fieldtype = Util.null2String(rs.getString("fieldtype"));
			texttype = Util.null2String(rs.getString("texttype"));
			buttontype = Util.null2String(rs.getString("buttontype"));
		}
		try {
			return getTransValue2(fieldtype,texttype,buttontype,value,fieldid);
		} catch (Exception e) {
			return value;
		}
	}
	
	public String getTransValue2(String fieldtype,String texttype,String buttontype,String value,String fieldid) throws Exception{
		//BaseBean log = new BaseBean();
		String result ="";
		if("0".equals(fieldtype)){
			result = value;
		}else if("1".equals(fieldtype)){
			result = new BrowserInfoUtil().getBrowserFieldValue2(buttontype, value);
		}else if("2".equals(fieldtype)){
			result = new TransUtil().getSelectValue(fieldid, value);
		}else if("3".equals(fieldtype)){
			result = new TransUtil().getAttachUrl2(value);
		}else if("4".equals(fieldtype)){
			result = value;
		}else{
			result = value;
		}
		return result;
	}
}
