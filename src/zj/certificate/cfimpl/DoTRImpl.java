package zj.certificate.cfimpl;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.CreateCertificate;
import zj.certificate.InsertUtil;
/**
 * 出差报销
 * @author Administrator
 *
 */
public class DoTRImpl {
	BaseBean log = new BaseBean();
	public Map<String, String> doTravelReimbursement(String tableName,String requestId,String bookid,String pch,String now,String SEGMENT1) {
		RecordSet rs = new RecordSet();
		 Map<String, String> returnMap = new HashMap<String, String>();
		 CreateCertificate  ccf=new CreateCertificate();
		String xm="";
		String sqrq="";
		String ccmdd="";
		String clkm="";
		String biz="";
		String bxzje="0";
		String dfkm="";
		String mainId="";
		String wb="";
		String ACCOUNTED_DR="";
		String ENTERED_DR="";
		String lv="";
		String bxsm="";
		String bmdm="";
		String xmm = "";
		String dqm = "";
		String rq1="";
		String rq2="";
		InsertUtil iu = new InsertUtil();
		String sql = "select id,(select lastname from hrmresource where id=xm) as xm,sqrq,ccmdd,clkm,biz,bxzje,dfkm,wb,lv,bxsm,bmdm,rq1,rq2,xmm,dqm from "+tableName+" where requestId="+requestId;
		log.writeLog("workflowId sql"+sql);
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			xm = Util.null2String(rs.getString("xm"));
			sqrq = Util.null2String(rs.getString("sqrq"));
			ccmdd = Util.null2String(rs.getString("ccmdd"));
			clkm = Util.null2String(rs.getString("clkm"));
			biz = geCurrencyCode(Util.null2String(rs.getString("biz")));
			bxzje =Util.null2String(rs.getString("bxzje")).replaceAll(",", "");
			dfkm = Util.null2String(rs.getString("dfkm"));
			wb = Util.null2String(rs.getString("wb")).replaceAll(",", "");
			lv = Util.null2String(rs.getString("lv"));
			bxsm = Util.null2String(rs.getString("bxsm"));
			bmdm = Util.null2String(rs.getString("bmdm"));
			xmm = Util.null2String(rs.getString("xmm"));
			dqm = Util.null2String(rs.getString("dqm"));
			rq1 = Util.null2String(rs.getString("rq1"));
			rq2 = Util.null2String(rs.getString("rq2"));
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
		if("".equals(wb)){
			wb="0";
		}
		if("".equals(bxzje)){
			bxzje="0";
		}
		if("".equals(ccmdd)){
			ccmdd = " ";
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
		String pzsm=xm+" "+rq1+"~"+rq2+" "+bxsm+" 差旅费";
		returnMap.put("wb", wb);
		returnMap.put("rmb", bxzje);
		
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CURRENCY_CODE", biz);
		mapStr.put("DATE_CREATED","to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("SEGMENT2", xmm);//项目码
		mapStr.put("SEGMENT3", dqm);//地区码
		mapStr.put("SEGMENT4", bmdm);//部门码
		mapStr.put("SEGMENT5", clkm);//科目
		mapStr.put("REFERENCE1", "OA"+pch);
		mapStr.put("REFERENCE2", pzsm);
		mapStr.put("REFERENCE4", "OA"+pch);
		mapStr.put("REFERENCE5", pzsm);
		mapStr.put("REFERENCE10",pzsm);
		//mapStr.put("request_id", requestId);
		mapStr.put("GROUP_ID",pch);
		String fykm_dt1="";//费用科目
		String kmbm_dt1="";//科目编号
		String je_dt1 = "";//金额
		String dtid="";
		//String fplx_dt1="";//发票类型
		String sfhs_dt1 = "";
		String fph_dt1="";//发票号
		String kpf_dt1="";//开票方
		String jxsbm_dt1="";//进项税编号
		String se_dt1="";//税额
		String ccrq_dt1="";
		String jsrq_dt1="";
		String nr_dt1="";
		String cutmoney="0";
		sql="select * from "+tableName+"_dt1 where (fykm='0' or sfhs='0') and mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			dtid = Util.null2String(rs.getString("id"));
			fykm_dt1 = Util.null2String(rs.getString("fykm"));
			kmbm_dt1 = Util.null2String(rs.getString("kmbm"));
			je_dt1 = Util.null2String(rs.getString("je")).replaceAll(",", "");
			//fplx_dt1 = Util.null2String(rs.getString("fplx"));
			sfhs_dt1 = Util.null2String(rs.getString("sfhs"));
			fph_dt1 = Util.null2String(rs.getString("fph"));
			kpf_dt1 = Util.null2String(rs.getString("kpf"));
			ccrq_dt1 = Util.null2String(rs.getString("ccrq"));
			jsrq_dt1 = Util.null2String(rs.getString("jsrq"));
			nr_dt1 = Util.null2String(rs.getString("nr")).replace("<br/>", "").replace("<br>", "").replace("</br>", "");
			jxsbm_dt1 = Util.null2String(rs.getString("jxsbm"));
			se_dt1 = Util.null2String(rs.getString("se")).replaceAll(",", "");
			if("0".equals(fykm_dt1)){
				cutmoney=iu.add(cutmoney,je_dt1);
				if("0".equals(sfhs_dt1)){
					String REFERENCE10=fph_dt1+" "+kpf_dt1+" 进项税";
					mapStr.put("interface_seq", ccf.getInterfaceSeq());
					if("".equals(jxsbm_dt1)){
						jxsbm_dt1 = " ";
					}
					mapStr.put("SEGMENT5", jxsbm_dt1);
					mapStr.put("ENTERED_DR", se_dt1);
					if("USD".equals(biz)){
						mapStr.put("ACCOUNTED_DR", iu.mul(se_dt1, lv));
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
					je_dt1 = iu.sub(je_dt1, se_dt1);
				}
				mapStr.put("interface_seq", ccf.getInterfaceSeq());
				if("".equals(kmbm_dt1)){
					kmbm_dt1 = " ";
				}
				mapStr.put("SEGMENT5", kmbm_dt1);
				mapStr.put("ENTERED_DR", je_dt1);
				if("USD".equals(biz)){
					mapStr.put("ACCOUNTED_DR", iu.mul(je_dt1, lv));
				}
				mapStr.put("SEGMENT2", xmm);//项目码
				mapStr.put("SEGMENT3", dqm);//地区码
				mapStr.put("SEGMENT4", bmdm);//部门码
				mapStr.put("REFERENCE2", xm+" "+nr_dt1);
				mapStr.put("REFERENCE5", xm+" "+nr_dt1);
				mapStr.put("REFERENCE10",xm+" "+nr_dt1);
				mapStr.put("REFERENCE30", ccf.interfaceseq+"");
				 ccf.interfaceseq= ccf.interfaceseq+10;
				iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			}else if("0".equals(sfhs_dt1)){
				String REFERENCE10=fph_dt1+" "+kpf_dt1+" 进项税";
				mapStr.put("interface_seq", ccf.getInterfaceSeq());
				if("".equals(jxsbm_dt1)){
					jxsbm_dt1 = " ";
				}
				mapStr.put("SEGMENT5", jxsbm_dt1);
				mapStr.put("ENTERED_DR", se_dt1);
				if("USD".equals(biz)){
					mapStr.put("ACCOUNTED_DR", iu.mul(se_dt1, lv));
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
				cutmoney=iu.add(cutmoney,se_dt1);
			}
		}
		if("USD".equals(biz)){
			wb=iu.sub(wb, cutmoney);
			bxzje = iu.sub(bxzje, iu.mul(cutmoney, lv));
		}else{
			wb=iu.sub(wb, cutmoney);
            bxzje=iu.sub(bxzje, cutmoney);	
		}
	
		mapStr.put("interface_seq", ccf.getInterfaceSeq());
		mapStr.put("SEGMENT5", clkm);
		mapStr.put("ENTERED_DR", wb);
		if("USD".equals(biz)){
		mapStr.put("ACCOUNTED_DR", bxzje);
		}
		mapStr.put("SEGMENT2", xmm);//项目码
		mapStr.put("SEGMENT3", dqm);//地区码
		mapStr.put("SEGMENT4", bmdm);//部门码
		mapStr.put("REFERENCE2", pzsm);
		mapStr.put("REFERENCE5", pzsm);
		mapStr.put("REFERENCE10", pzsm);
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
