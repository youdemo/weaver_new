package morningcore.contract.util;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class TransUtil {
	BaseBean log = new BaseBean();
	/**
	 * 获取附件下载链接
	 * 
	 * @param value
	 * @return
	 */
	public String getAttachUrl(String value) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String url = "";
		String fileName = "";
		String flag = "";
		String fileId = "";
		if (!"".equals(value)) {
			sql = "select b.imagefileid,b.imagefilename from docimagefile a,imagefile b where a.imagefileid=b.imagefileid and a.docid in ("
					+ value + ")";
			rs.execute(sql);
			while (rs.next()) {
				url = url + flag;
				fileName = Util.null2String(rs.getString("imagefilename"));
				fileId = Util.null2String(rs.getString("imagefileid"));
				url = url
						+ "<a target=\"_blank\" href=\"/weaver/weaver.file.FileDownload?fileid="
						+ fileId + "&download=1\">"
						+ fileName + "</a>";
				flag = " <br/>";
			}
		}
		return url;
	} 
	public String getAttachUrl2(String value) {
		RecordSet rs = new RecordSet();
		String sql = "";
		String url = "";
		String fileName = "";
		String flag = "";
		String fileId = "";
		if (!"".equals(value)) {
			sql = "select b.imagefileid,b.imagefilename from docimagefile a,imagefile b where a.imagefileid=b.imagefileid and a.docid in ("
					+ value + ")";
			rs.execute(sql);
			while (rs.next()) {
				url = url + flag;
				fileName = Util.null2String(rs.getString("imagefilename"));
				fileId = Util.null2String(rs.getString("imagefileid"));
				url = url+fileName;
				flag = " <br/>";
			}
		}
		return url;
	} 
	
	public String getSelectValue(String fieldid,String value){
		String result = "";
		String sql="";
		String fieldname = "";
		String tbname = "";
		RecordSet rs = new RecordSet();
		if("".equals(fieldid)||"".equals(value)){
			result = "";
		}else{
			sql = "select fieldname,tbname from uf_change_field_mt where id="+fieldid;
			rs.executeSql(sql);
			if(rs.next()) {
				fieldname = Util.null2String(rs.getString("fieldname"));
				tbname = Util.null2String(rs.getString("tbname"));
			}
			sql = "select selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"+tbname+"' and a.fieldname='"+fieldname+"' and selectvalue='"+value+"'";
			
			rs.executeSql(sql);
			if(rs.next()) {
				result = Util.null2String(rs.getString("selectname"));
			}
		}
		return result;
		
	}
	
	/**
	 * 新建编辑页面单行文本转换html
	 * @param fieldname
	 * @param texttype
	 * @param textlength
	 * @param floatdigit
	 * @param value
	 * @return
	 */
	public String getTextTransResult(String fieldname,String texttype,String textlength,String floatdigit,String value){
		StringBuffer transResult = new StringBuffer();
		transResult.append("");
		String idName=fieldname;
		
		String imageName=idName+"image";
		if("0".equals(texttype)){
			transResult.append("<input class=\"inputstyle\" maxlength=\""+textlength+"\" size=\""+textlength+"\" id=\""+idName+"\" name=\""+idName+"\" onblur=\"checkLength()\"  value=\""+value+"\"> "+
		                   "<span id=\""+imageName+"\">");
			
			transResult.append( "<span id=\"remind\" style=\"cursor:hand\" title=\"文本长度不能超过"+textlength+"(1个中文字符等于3个长度)\">"+
	        			   "<img src=\"/images/remind_wev8.png\" align=\"absmiddle\">"+
	        			   "</span>");
		}else if("1".equals(texttype)){
			String viewtype = "0";			
			transResult.append("<input datatype=\"int\" onafterpaste=\"if(isNaN(value))execCommand('undo')\" style=\"ime-mode:disabled\" viewtype=\""+viewtype+"\" type=\"text\" class=\"InputStyle\" id=\""+idName+"\" name=\""+idName+"\" onkeypress=\"ItemCount_KeyPress()\" onblur=\"checkcount1(this);checkItemScale(this,'整数位数长度不能超过9位，请重新输入！',-999999999,999999999);checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'))\" value=\""+value+"\" onchange=\"\" onpropertychange=\"\" _listener=\"\">");
			transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");
			transResult.append("</span>");
		}else if("2".equals(texttype)){
			String viewtype = "0";			
			transResult.append("<input datalength=\""+floatdigit+"\" datatype=\"float\" style=\"ime-mode:disabled\" onafterpaste=\"if(isNaN(value))execCommand('undo')\" viewtype=\""+viewtype+"\" type=\"text\" class=\"InputStyle\" id=\""+idName+"\" name=\""+idName+"\" onkeypress=\"ItemDecimal_KeyPress('"+idName+"',15,"+floatdigit+")\" onblur=\"checkFloat(this);checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'))\" value=\""+value+"\" onchange=\"\" onpropertychange=\"\" _listener=\"\">");
			transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");			
			transResult.append("</span>");
		}
		return transResult.toString();
	}
	
	public String getSelectFieldHtml(String fieldname,String tbname,String value ){
		String sql = "";
		String selectId = "";
		String selectkey="";
		String selectvalue="";
		RecordSet rs = new RecordSet();
		StringBuffer transResult = new StringBuffer();
		transResult.append("");
		String idName=fieldname;
		
		String viewtype = "0";
		
		transResult.append("<select class=\"e8_btn_top middle\" id=\""+idName+"\" name=\""+idName+"\" viewtype=\""+viewtype+"\" onblur=\"checkinput2('"+idName+"','"+idName+"span',this.getAttribute('viewtype'));\">");
		transResult.append(" <option value=\"\"");
		if("".equals(value)){
			transResult.append("selected");
		} 
		transResult.append("></option>");
		
		sql="select selectname,selectvalue from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"+tbname+"' and a.fieldname='"+fieldname+"' and isnull(cancel,0)=0";
		rs.executeSql(sql);
		while(rs.next()){
			selectkey = Util.null2String(rs.getString("selectvalue"));
			selectvalue = Util.null2String(rs.getString("selectname"));
			transResult.append(" <option value=\""+selectkey+"\"");
			if(selectkey.equals(value)){
				transResult.append("selected");
			} 
			transResult.append(">"+selectvalue+"</option>");
		}
		transResult.append("</select>");
		transResult.append("<span id=\""+idName+"span\" style=\"word-break:break-all;word-wrap:break-word\">");
		transResult.append("</span>");
		return transResult.toString();
	}
	
	
}
