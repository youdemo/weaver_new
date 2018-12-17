package zj.certificate.cfimpl;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.CreateCertificate;
import zj.certificate.InsertUtil;
/**
 * 请款流程
 * @author Administrator
 *
 */
public class DoZZImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> doZZ(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		String xm="";
		String sqrq="";
		String bb="";
		String fkkm="";
		String mainId="";
		String wbje="0";
		String zzje="0";
		String hl="";
		String bmdm="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=xm) as xm,sqrq,bb,fkkm,hl,bmdm from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			xm = Util.null2String(rs.getString("xm"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			bb = geCurrencyCode(Util.null2String(rs.getString("bb")));
			fkkm = Util.null2String(rs.getString("fkkm"));
			hl = Util.null2String(rs.getString("hl"));
			bmdm = Util.null2String(rs.getString("bmdm"));
		}
		if("".equals(bmdm)){
			bmdm = "0000";
		}
		
		if("".equals(xm)){
			xm = " ";
		}
		if("".equals(fkkm)){
			fkkm = " ";
		}
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CURRENCY_CODE", bb);
		mapStr.put("DATE_CREATED","to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("REFERENCE1",  "OA"+pch);
		mapStr.put("REFERENCE2", " ");
		mapStr.put("REFERENCE4", "OA"+pch);
		mapStr.put("REFERENCE5", " ");
		mapStr.put("REFERENCE10", " ");
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
		String kmbm_dt1 ="";//科目编码
		String zzje_dt1 = "";//金额
		String zzsm_dt1 = "";//暂支说明
		String dtid="";
		String money="0";
		sql="select * from "+tableName+"_dt1 where mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			dtid = Util.null2String(rs.getString("id"));
			zzje_dt1 = Util.null2String(rs.getString("zzje")).replaceAll(",", "");
			kmbm_dt1 = Util.null2String(rs.getString("kmbm"));
			zzsm_dt1 = Util.null2String(rs.getString("zzsm")).replace("<br/>", "").replace("<br>", "").replace("</br>", ""); 
			money= iu.add(money, zzje_dt1);
			mapStr.put("SEGMENT2", "0000");//项目码
			mapStr.put("SEGMENT3", "000");//地区码
			mapStr.put("SEGMENT4", bmdm);//部门码
				
				mapStr.put("interface_seq", ccf.getInterfaceSeq());
				if("".equals(kmbm_dt1)){
					kmbm_dt1 = " ";
				}
				mapStr.put("SEGMENT5", kmbm_dt1);
				mapStr.put("ENTERED_DR", zzje_dt1);
				if("USD".equals(bb)){
					mapStr.put("ACCOUNTED_DR", iu.mul(zzje_dt1, hl));
				}
				mapStr.put("REFERENCE2", xm+" 暂支    "+zzsm_dt1);
				mapStr.put("REFERENCE5", xm+" 暂支    "+zzsm_dt1);
				mapStr.put("REFERENCE10",xm+" 暂支    "+zzsm_dt1);
				mapStr.put("REFERENCE30", ccf.interfaceseq+"");
				 ccf.interfaceseq= ccf.interfaceseq+10;
				iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			
		}
		wbje=money;
		if("USD".equals(bb)){
			zzje= iu.mul(money, hl);
		}else{
			zzje= money;
		}
		returnMap.put("wb", wbje);
		returnMap.put("rmb", zzje);
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
