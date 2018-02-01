package zj.certificate.cfimpl;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.CreateCertificate;
import zj.certificate.InsertUtil;
/**
 * 出差暂支
 * @author Administrator
 *
 */
public class DoTBImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> doTravelBorrow(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		String xm="";
		String tbrq="";
		String clkm="";
		String bib="";
		String zzje="0";
		String dfkm="";
		String mainId="";
		String wbje="";
		String ACCOUNTED_DR="";
		String ENTERED_DR="";
		String ccrq=""; 
		String jsrq="";
		String ccmdd="";
		String hl="";
		String zzsm="";
		String bmdm="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=xm) as xm,tbrq,clkm,bib,zzje,dfkm,wbje,hl,zzsm,bmdm,ccrq,jsrq,ccmdd from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			xm = Util.null2String(rs.getString("xm"));
			tbrq = Util.null2String(rs.getString("tbrq"));
			clkm = Util.null2String(rs.getString("clkm"));
			bib = geCurrencyCode(Util.null2String(rs.getString("bib")));
			zzje =Util.null2String(rs.getString("zzje")).replaceAll(",", "");
			dfkm = Util.null2String(rs.getString("dfkm"));
			wbje = Util.null2String(rs.getString("wbje")).replaceAll(",", "");
			hl = Util.null2String(rs.getString("hl"));
			zzsm = Util.null2String(rs.getString("zzsm"));
			bmdm = Util.null2String(rs.getString("bmdm"));
			ccrq = Util.null2String(rs.getString("ccrq"));
			jsrq = Util.null2String(rs.getString("jsrq"));
			ccmdd = Util.null2String(rs.getString("ccmdd"));
			
		}
		if("".equals(bmdm)){
			bmdm="0000";
		}
		if("".equals(wbje)){
			wbje="0";
		}
		if("".equals(zzje)){
			zzje="0";
		}
		if("".equals(clkm)){
			clkm = " ";
		}
		if("".equals(xm)){
			xm = " ";
		}
		if("".equals(dfkm)){
			dfkm = " ";
		}
		String pzsm=xm+" 暂支    "+ccrq+"~"+jsrq+" "+ccmdd+" 差旅费";
		returnMap.put("wb", wbje);
		returnMap.put("rmb", zzje);
		
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CURRENCY_CODE", bib);
		mapStr.put("DATE_CREATED","to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("SEGMENT2", "0000");//项目码
		mapStr.put("SEGMENT3", "000");//地区码
		mapStr.put("SEGMENT4", bmdm);//部门码
		mapStr.put("SEGMENT5", clkm);//科目
		mapStr.put("REFERENCE1","OA"+pch);
		mapStr.put("REFERENCE2",pzsm);
		mapStr.put("REFERENCE4","OA"+pch);
		mapStr.put("REFERENCE5",pzsm);
		mapStr.put("REFERENCE10",pzsm);
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
	
		ACCOUNTED_DR = getMoney(zzje);
		if("USD".equals(bib)){
			ENTERED_DR= getMoney(wbje);
		}else{
			ENTERED_DR= "";
		}
		mapStr.put("interface_seq", ccf.getInterfaceSeq());
		mapStr.put("SEGMENT5", clkm);
		mapStr.put("ENTERED_DR", wbje);
		if("USD".equals(bib)){
			mapStr.put("ACCOUNTED_DR", zzje);
		}
		mapStr.put("REFERENCE30", ccf.interfaceseq+"");
		 ccf.interfaceseq= ccf.interfaceseq+10;
		iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
		return returnMap;
	}
    
    public String geCurrencyCode(String currency){
    	String code="";
    	if("0".equals(currency)){
    		code="RMB";
    	}else{
    		code="USD";
    	}
    	return code;
    }
    
    public String getMoney(String money){
    	String result="";
    	if("".equals(money)){
    		result = null;
		}else{
			result = money;
		}
    	return result;
    }

    
}
