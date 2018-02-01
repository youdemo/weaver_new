package zj.certificate.cfimpl;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.CreateCertificate;
import zj.certificate.InsertUtil;
/**
 * 出差报销流程
 * @author Administrator
 *
 */
public class DoYLBXImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> doYLBX(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		String sqr="";
		String sqrq="";
		String ylkm="";
		String bib="RMB";
		String bxzje="0";
		String dfkm="";
		String mainId="";
		String bxsq="";
		String bmdm="";
		String ksrq="";
		String jsrq="";
		String xmm="";
		String dqm ="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=sqr) as sqr,sqrq,ylkm,bxzje,dfkm,bxsq,bmdm,ksrq,jsrq,xmm,dqm from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			sqr = Util.null2String(rs.getString("sqr"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			ylkm = Util.null2String(rs.getString("ylkm"));
			bxzje =Util.null2String(rs.getString("bxzje")).replaceAll(",", "");
			dfkm = Util.null2String(rs.getString("dfkm"));
			//bxsq = Util.null2String(rs.getString("bxsq"));
			bmdm = Util.null2String(rs.getString("bmdm"));
			ksrq = Util.null2String(rs.getString("ksrq"));
			jsrq = Util.null2String(rs.getString("jsrq"));
			xmm = Util.null2String(rs.getString("xmm"));
			dqm = Util.null2String(rs.getString("dqm"));
		}
		if("".equals(bmdm)){
			bmdm="0000";
		}
		if("".equals(xmm)){
			xmm="0000";
		}
		if("".equals(dqm)){
			dqm="000";
		}
		if("".equals(bxzje)){
			bxzje="0";
		}
		
		if("".equals(ylkm)){
			ylkm = " ";
		}
		if("".equals(dfkm)){
			dfkm = " ";
		}
		bxsq = sqr+" "+ksrq+"~"+jsrq+" 医疗费报销";
		if("".equals(bxsq)){
			bxsq = " ";
		}
		returnMap.put("wb", bxzje);
		returnMap.put("rmb", bxzje);
		
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
		mapStr.put("SEGMENT2", xmm);//项目码
		mapStr.put("SEGMENT3", dqm);//地区码
		mapStr.put("SEGMENT4", bmdm);//部门码
		mapStr.put("SEGMENT5", ylkm);//科目
		mapStr.put("REFERENCE1","OA"+pch);
		mapStr.put("REFERENCE2",bxsq);
		mapStr.put("REFERENCE4","OA"+pch);
		mapStr.put("REFERENCE5",bxsq);
		mapStr.put("REFERENCE10",bxsq);
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
	
	
		mapStr.put("interface_seq", ccf.getInterfaceSeq());
		mapStr.put("SEGMENT5", ylkm);
		mapStr.put("ENTERED_DR", bxzje);
		if("USD".equals(bib)){
			mapStr.put("ACCOUNTED_DR", bxzje);
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
