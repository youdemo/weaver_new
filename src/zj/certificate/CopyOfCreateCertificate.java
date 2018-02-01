package zj.certificate;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import zj.certificate.cfimpl.DoHXImpl;
import zj.certificate.cfimpl.DoQKImpl;
import zj.certificate.cfimpl.DoTBImpl;
import zj.certificate.cfimpl.DoTRImpl;
import zj.certificate.cfimpl.DoYLBXImpl;
import zj.certificate.cfimpl.DoZZImpl;

public class CopyOfCreateCertificate {
	BaseBean log = new BaseBean();
	public static int interfaceseq=10;
	public void doCreate(String requestids) {
		interfaceseq=10;
		RecordSet rs = new RecordSet();
		GetSeqNum gsb = new GetSeqNum();
		String pch = "";
		//SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		//String now =sf.format(new Date());
		String now ="01-02-2016";
		String companyName = "";
		String requestid[] = requestids.split(",");
		for (int i = 0; i < requestid.length; i++) {
			String rqid = requestid[i];
			if ("".equals(rqid) || "0".equals(rqid)) {
				continue;
			}
			if ("".equals(pch)) {
				String sql = "select b.subcompanyname  from v_create_certificate a,HrmSubCompany b where a.gs=b.id and a.requestid="
						+ rqid;
				rs.executeSql(sql);
				if (rs.next()) {
					companyName = Util.null2String(rs.getString("subcompanyname"));
				}
				pch = gsb.getNum("", "zj_gl_interface_control", 4);
				insertControl(rqid,pch,companyName);
			}
			doRqid(rqid, pch,companyName,now);
		}
	}
	/**
	 * 插入
	 * @param rqid 流程ID
	 * @param pch 批次号
	 */
	public void insertControl(String rqid, String pch,String companyName) {
		RecordSetDataSource rs = new RecordSetDataSource("test");
		String sql="";
		String bookid = "";
		if ("中晶科技".equals(companyName)) {
			bookid = "2";
		} else if ("中晶医疗".equals(companyName)) {
			bookid = "164";
		} else {
			bookid = "3";
		}
		int maxid = 1;
		sql = "select nvl(MAX(interface_run_id),0)+1 as maxid  from zj_gl_interface_control";
		log.writeLog("testaa+sql"+sql);
		rs.executeSql(sql);
		if (rs.next()) {
			maxid = rs.getInt("maxid");
		}
		sql = "insert into zj_gl_interface_control(JE_SOURCE_NAME,STATUS,INTERFACE_RUN_ID,GROUP_ID,SET_OF_BOOKS_ID) " +
				"values('Manual','S','"+maxid+"','"+pch+"','"+bookid+"')";
		rs.executeSql(sql);
		
	}

	public void doRqid(String rqid, String pch,String companyName,String now) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String requestId = "";
		String sql_dt = "";
		String tableName = "";
		String workflowId = "";
		String bookid="";
		String SEGMENT1="";
		String rmb="0";
		String wb="0";
		String bz="";
		if ("中晶科技".equals(companyName)) {
			bookid = "2";
			SEGMENT1="02";
		} else if ("中晶医疗".equals(companyName)) {
			bookid = "164";
			SEGMENT1="29";
		} else {
			bookid = "3";
			SEGMENT1="19";
		}
		String sql="select requestid from request_fk_record where fkseq in(select fkseq from request_fk_record where requestid='"+rqid+"' ) order by seq asc";
	
		rs.executeSql(sql);
		while(rs.next()){
			requestId = Util.null2String(rs.getString("requestid"));
			sql_dt = "select c.tablename ,b.id as workflowid from workflow_requestbase a,workflow_base b,workflow_bill c where a.workflowid=b.id and b.formid = c.id  and a.requestid="
					+ requestId;
			rs_dt.executeSql(sql_dt);
			if (rs_dt.next()) {
				tableName = Util.null2String(rs_dt.getString("tablename"));
				workflowId = Util.null2String(rs_dt.getString("workflowid"));
			}
			if("60".equals(workflowId)){	//出差报销			
				Map<String, String> result= new DoTRImpl().doTravelReimbursement(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("biz")));
					}
				}
				updateRecordInfo(requestId,pch);
			}
			
			if("57".equals(workflowId)){//出差暂支
				
				Map<String, String> result= new DoTBImpl().doTravelBorrow(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bib")));
					}
				}
				updateRecordInfo(requestId,pch);
			}
			if("62".equals(workflowId)){//核销
				Map<String, String> result= new DoHXImpl().doHX(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bb")));
					}
				}
				updateRecordInfo(requestId,pch);
			}
			if("65".equals(workflowId)){//请款
				
				Map<String, String> result= new DoQKImpl().doQK(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bb")));
					}
				}
				updateRecordInfo(requestId,pch);
			}
			if("53".equals(workflowId)){//暂支			
				Map<String, String> result= new DoZZImpl().doZZ(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bb")));
					}
				}
				updateRecordInfo(requestId,pch);
			}
			if("61".equals(workflowId)){//医疗报销			
				Map<String, String> result= new DoYLBXImpl().doYLBX(tableName,requestId,bookid,pch,now,SEGMENT1);
				rmb=iu.add(rmb, result.get("rmb"));
				wb=iu.add(wb, result.get("wb"));
				if("".equals(bz)){
//					sql_dt="select * from "+tableName+" where requestid="+requestId;
//					rs_dt.executeSql(sql_dt);
//					if(rs_dt.next()){
//						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bb")));
//					}
					bz="RMB";
				}
				updateRecordInfo(requestId,pch);
			}
			
		}
		String dfr="";
		String fkfsname="";
		String bm = "";
		sql="select (select lastname from hrmresource where id=sqr ) as sqr,(select skyh from uf_skfs where id=fkfs) as fkfsname,(select bm from uf_skfs where id=fkfs) as bm from v_fukuan_requestFlow_yf where requestid="+requestId;
		rs.executeSql(sql);
		if(rs.next()){
			dfr = Util.null2String(rs.getString("sqr"));
			fkfsname = Util.null2String(rs.getString("fkfsname"));
			bm = Util.null2String(rs.getString("bm"));
		}
		String ENTERED_CR = "";
		String ACCOUNTED_CR = getMoney(rmb);
		if("USD".equals(bz)){
			ENTERED_CR= getMoney(wb);
		}else{
			ENTERED_CR= getMoney(rmb);
		}
		if("".equals(bm)){
			bm=" ";
		}
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("interface_seq", getInterfaceSeq());
		mapStr.put("STATUS", "NEW");
		mapStr.put("SET_OF_BOOKS_ID", bookid);
		mapStr.put("ACCOUNTING_DATE", "to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CURRENCY_CODE", bz);
		mapStr.put("DATE_CREATED","to_date('"+now+"','dd-mm-yyyy')");
		mapStr.put("CREATED_BY", "1069");
		mapStr.put("ACTUAL_FLAG","A");
		mapStr.put("USER_JE_CATEGORY_NAME", "Other");
		mapStr.put("USER_JE_SOURCE_NAME", "Manual");
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("SEGMENT2", "0000");//项目码
		mapStr.put("SEGMENT3", "000");//地区码
		mapStr.put("SEGMENT4", "0000");//部门码
		mapStr.put("SEGMENT5", bm);//科目
		mapStr.put("ENTERED_CR", getMoney(wb));
		if("USD".equals(bz)){
		mapStr.put("ACCOUNTED_CR", getMoney(rmb));
		}
		mapStr.put("REFERENCE1","OA"+pch);
		mapStr.put("REFERENCE2", dfr+" "+fkfsname+" "+bm);
		mapStr.put("REFERENCE4","OA"+pch);
		mapStr.put("REFERENCE5", dfr+" "+fkfsname+" "+bm);
		mapStr.put("REFERENCE10", dfr+" "+fkfsname+" "+bm);
		mapStr.put("REFERENCE30", interfaceseq+"");
		 interfaceseq= interfaceseq+10;
		mapStr.put("GROUP_ID",pch);
		iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
		Map<String, String> mapStrseq = new HashMap<String, String>();
		mapStrseq.put("BATCH_NAME",  "OA"+pch);
		mapStrseq.put("JOURNAL_NAME", "OA"+pch);
		iu.insertzj(mapStrseq, "ZJ_JOURNAL_SEQ");
		
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
    
    public String getInterfaceSeq(){
    	String nextSeq="";
    	RecordSetDataSource rs = new RecordSetDataSource("test");
    	String sql="select nvl(MAX(interface_seq),0)+1 as nextseq from zj_gl_interface";
    	rs.executeSql(sql);
    	if(rs.next()){
    		nextSeq = Util.null2String(rs.getString("nextseq"));
    	}
    	return nextSeq;
    }
    
    public void updateRecordInfo(String requestid,String pch){
    	RecordSet rs = new RecordSet();
    	String sql="update request_fk_record set sfscpz='1',pch='"+pch+"' where requestid="+requestid;
    	rs.executeSql(sql);
    	
    }
}
