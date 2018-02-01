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
public class DoQKImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> doQK(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		String hxr="";
		String sqrq="";
		String bb="";
		String dfkm="";
		String mainId="";
		String wbje="0";
		String zzje="0";
		String hl="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=hxr) as hxr,sqrq,bb,dfkm,hl from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			hxr = Util.null2String(rs.getString("hxr"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			bb = geCurrencyCode(Util.null2String(rs.getString("bb")));
			dfkm = Util.null2String(rs.getString("dfkm"));
			hl = Util.null2String(rs.getString("hl"));
		}
		
		if("".equals(dfkm)){
			dfkm = " ";
		}
		if("".equals(hxr)){
			hxr = " ";
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
		mapStr.put("REFERENCE1", "OA"+pch);
		mapStr.put("REFERENCE2", " ");
		mapStr.put("REFERENCE4", "OA"+pch);
		mapStr.put("REFERENCE5", " ");
		mapStr.put("REFERENCE10", " ");
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
		String xmbm_dt1 ="";//项目代码
		String dqm_dt1 ="";//地区码
		String bmdm_dt1 ="";//部门代码
		String bm_dt1 ="";//科目编码
		String hxje_dt1 = "";//金额
		String dtid="";
		String fplx_dt1="";//发票类型
		String fphm_dt1="";//发票号
		String kpf_dt1="";//开票方
		String zzsbm_dt1="";//进项税编号
		String fpse_dt1="";//税额
		String fbbm_dt1 = "";//公司编码
		String hxnr_dt1 = "";//请款说明
		String hxlx1 = "";//请款类型
		String money="0";
		sql="select * from "+tableName+"_dt1 where mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			dtid = Util.null2String(rs.getString("id"));
			xmbm_dt1 = Util.null2String(rs.getString("xmbm"));
			dqm_dt1 = Util.null2String(rs.getString("dqm"));
			bmdm_dt1 = Util.null2String(rs.getString("bmdm"));
			bm_dt1 = Util.null2String(rs.getString("bm"));
			dqm_dt1 = Util.null2String(rs.getString("dqm"));
			hxje_dt1 = Util.null2String(rs.getString("hxje")).replaceAll(",", "");
			fplx_dt1 = Util.null2String(rs.getString("fplx"));
			fphm_dt1 = Util.null2String(rs.getString("fphm"));
			kpf_dt1 = Util.null2String(rs.getString("kpf"));
			zzsbm_dt1 = Util.null2String(rs.getString("zzsbm"));
			fpse_dt1 = Util.null2String(rs.getString("fpse")).replaceAll(",", "");
			fbbm_dt1 = Util.null2String(rs.getString("fbbm"));
			hxnr_dt1 = Util.null2String(rs.getString("hxnr"));
			hxlx1 = Util.null2String(rs.getString("hxlx1"));
			money= iu.add(money, hxje_dt1);
			if("".equals(xmbm_dt1)){
				xmbm_dt1 = "0000";
			}
			if("".equals(dqm_dt1)){
				dqm_dt1 = "000";
			}
			if("".equals(bmdm_dt1)){
				bmdm_dt1 = "0000";
			}
			if("".equals(fbbm_dt1)){
				fbbm_dt1 = " ";
			}
			if(!"1".equals(hxlx1)){
				xmbm_dt1 = "0000";
				dqm_dt1 = "000";
				bmdm_dt1 = "0000";
			}
			//mapStr.put("REFERENCE1", fbbm_dt1);
				if("0".equals(fplx_dt1)){
					String REFERENCE10=fphm_dt1+" "+kpf_dt1+" 进项税";
					mapStr.put("interface_seq", ccf.getInterfaceSeq());	
					if("".equals(zzsbm_dt1)){
						zzsbm_dt1 = " ";
					}
					mapStr.put("SEGMENT5", zzsbm_dt1);
					mapStr.put("ENTERED_DR", fpse_dt1);
					if("USD".equals(bb)){
						mapStr.put("ACCOUNTED_DR", iu.mul(fpse_dt1, hl));
					}
					mapStr.put("SEGMENT2", "0000");//项目码
					mapStr.put("SEGMENT3", "000");//地区码
					mapStr.put("SEGMENT4", "0000");//部门码
					mapStr.put("REFERENCE2", REFERENCE10);
					mapStr.put("REFERENCE5", REFERENCE10);
					mapStr.put("REFERENCE10", REFERENCE10);
					mapStr.put("REFERENCE30", ccf.interfaceseq+"");
					 ccf.interfaceseq= ccf.interfaceseq+10;
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
					hxje_dt1 = iu.sub(hxje_dt1, fpse_dt1);
				}
				mapStr.put("interface_seq", ccf.getInterfaceSeq());
				if("".equals(bm_dt1)){
					bm_dt1 = " ";
				}
				mapStr.put("SEGMENT5", bm_dt1);
				mapStr.put("ENTERED_DR", hxje_dt1);
				if("USD".equals(bb)){
					mapStr.put("ACCOUNTED_DR", iu.mul(hxje_dt1, hl));
				}

				mapStr.put("SEGMENT2", xmbm_dt1);//项目码
				mapStr.put("SEGMENT3", dqm_dt1);//地区码
				mapStr.put("SEGMENT4", bmdm_dt1);//部门码
				mapStr.put("REFERENCE2", hxr+" "+hxnr_dt1);
				mapStr.put("REFERENCE5", hxr+" "+hxnr_dt1);
				mapStr.put("REFERENCE10", hxr+" "+hxnr_dt1);
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
