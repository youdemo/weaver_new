package zj.certificate;

import java.text.SimpleDateFormat;
import java.util.Date;
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

public class CreateCertificate20190909old {
	BaseBean log = new BaseBean();
	public static int interfaceseq=10;
	public void doCreate(String requestids) {
		interfaceseq=10;
		RecordSet rs = new RecordSet();
		GetSeqNum gsb = new GetSeqNum();
		String pch = "";
		SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy");
		String now =sf.format(new Date());
		//String now ="01-02-2016";
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
		RecordSetDataSource rs = new RecordSetDataSource("MPROD");
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
		sql = "select nvl(MAX(interface_run_id),90000000)+1 as maxid  from zj_gl_interface_control";
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
		String pzsm="";
		//出差暂支 
		String ccrq = "";//出差日期
		String jsrq = "";//结束日期
		String ccmdd = "";//出差目的地
		//出差报销
		String bxsm = "";//报销说明
		String rq1 = "";//日期1
		String rq2 = "";//日期2
		String yzzje = "";
		String bxje = "";
		String bt = "";
		String yzzje_hl = "";
		String bxje_hl = "";
		String bt_hl = "";
		String lv = "";
		String dfkm_bx = "";
		//暂支单
		String skzh = "";//收款单位
		String skzhname="";//收款单位名称
		String fkkm="";//贷方科目
		//请款单
		String skdy = "";//收款单位
		String skdyname="";//收款单位名称
		String dfkm="";//贷方科目
		//核销
		String type="";//类型 -1 小于   0 等于 1 大于
		String hxsm="";//核销说明
		String zzjr="";//暂支金额
		String kxje="";//核销金额
		String ybyt="";//应退应补
		String zzjr_hl="";//暂支金额 汇率
		String kxje_hl="";//核销金额 汇率
		String ybyt_hl="";//应退应补 汇率
		String hl="";//汇率
		String skdw="";//收款单位
		String skdwname="";//收款单位名称
		String dfkm_hx="";//贷方科目
		String pzsm2="";
		String zzd = "";
		String zzkm="";
		//医疗报销
		String ksrq="";//开始日期
		String jsrq_bx="";//结束日期
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
		int rqcount =0;
		String sql="select count(1) as count from request_fk_record where fkseq in(select fkseq from request_fk_record where requestid='"+rqid+"' )";
		rs.executeSql(sql);
		if(rs.next()){
			rqcount = rs.getInt("count");
		}
		String dfr="";
		String fkfsname="";
		String bm = "";
		sql="select (select lastname from hrmresource where id=sqr ) as sqr,(select skyh from uf_skfs where id=fkfs) as fkfsname,(select bm from uf_skfs where id=fkfs) as bm from v_fukuan_requestFlow_yf where requestid="+rqid;
		rs.executeSql(sql);
		if(rs.next()){
			dfr = Util.null2String(rs.getString("sqr"));
			fkfsname = Util.null2String(rs.getString("fkfsname"));
			bm = Util.null2String(rs.getString("bm"));
		}
		if("".equals(bm)){
			bm=" ";
		}
		Map<String, String> mapStr = new HashMap<String, String>();
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
		mapStr.put("REFERENCE1","OA"+pch);
		mapStr.put("REFERENCE4","OA"+pch);
		mapStr.put("SEGMENT5", bm);//科目
		sql="select requestid from request_fk_record where fkseq in(select fkseq from request_fk_record where requestid='"+rqid+"' ) order by seq asc";
	
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
			
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("biz")));
					}
				}
				if(rqcount == 1){
					rmb=iu.add(rmb, result.get("rmb"));
					wb=iu.add(wb, result.get("wb"));
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						ccmdd = Util.null2String(rs_dt.getString("ccmdd"));
						bxsm = Util.null2String(rs_dt.getString("bxsm"));
						rq1 = Util.null2String(rs_dt.getString("rq1"));
						rq2 = Util.null2String(rs_dt.getString("rq2"));
						yzzje = Util.null2String(rs_dt.getString("yzzje"));
						bxje = Util.null2String(rs_dt.getString("bxje"));
						bt = Util.null2String(rs_dt.getString("bt"));
						lv = Util.null2String(rs_dt.getString("lv"));
						dfkm_bx = Util.null2String(rs_dt.getString("dfkm"));
					}
					if("".equals(yzzje)){
						yzzje = "0";
					}
					if("".equals(bxje)){
						bxje = "0";
					}
					if("".equals(bt)){
						bt = "0";
					}
					if("".equals(lv)){
						lv = "1";
					}
					yzzje_hl=iu.mul(yzzje, lv);
					bxje_hl=iu.mul(bxje, lv);
					bt_hl=iu.mul(bt, lv);
					if(Util.getFloatValue(bxje)==Util.getFloatValue(yzzje)){
						type="0";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
					}else if(Util.getFloatValue(bxje)>Util.getFloatValue(yzzje)){
						type="1";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
						pzsm2=dfr+" 出差报销";
					}else{
						type="-1";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
						pzsm2=dfr+" 出差报销";
					}
				}else{
					
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						ccmdd = Util.null2String(rs_dt.getString("ccmdd"));
						bxsm = Util.null2String(rs_dt.getString("bxsm"));
						rq1 = Util.null2String(rs_dt.getString("rq1"));
						rq2 = Util.null2String(rs_dt.getString("rq2"));
						yzzje = Util.null2String(rs_dt.getString("yzzje"));
						bxje = Util.null2String(rs_dt.getString("bxje"));
						bt = Util.null2String(rs_dt.getString("bt"));
						lv = Util.null2String(rs_dt.getString("lv"));
						dfkm_bx = Util.null2String(rs_dt.getString("dfkm"));
					}
					if("".equals(yzzje)){
						yzzje = "0";
					}
					if("".equals(bxje)){
						bxje = "0";
					}
					if("".equals(bt)){
						bt = "0";
					}
					if("".equals(lv)){
						lv = "1";
					}
					yzzje_hl=iu.mul(yzzje, lv);
					bxje_hl=iu.mul(bxje, lv);
					bt_hl=iu.mul(bt, lv);
					if(Util.getFloatValue(yzzje)==Util.getFloatValue("0")){
						rmb=iu.add(rmb, result.get("rmb"));
						wb=iu.add(wb, result.get("wb"));
					}else if(Util.getFloatValue(bxje)==Util.getFloatValue(yzzje)){
						type="0";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(bxje));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(bxje_hl));
						}
						mapStr.put("SEGMENT5", dfkm_bx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
					}else if(Util.getFloatValue(bxje)>Util.getFloatValue(yzzje)){
						type="1";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
						pzsm2=dfr+" 出差报销";
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(yzzje));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(yzzje_hl));
						}
						mapStr.put("SEGMENT5", dfkm_bx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
						rmb=iu.add(rmb, bt_hl);
						wb=iu.add(wb, bt);
					}else{
						type="-1";
						pzsm=dfr+" 核销    "+rq1+"~"+rq2+" "+bxsm+" "+"差旅费";
						pzsm2=dfr+" 出差报销";
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(yzzje));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(yzzje_hl));
						}
						mapStr.put("SEGMENT5", dfkm_bx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
						rmb=iu.add(rmb, bt_hl);
						wb=iu.add(wb, bt);
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
				if(rqcount == 1){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						ccrq = Util.null2String(rs_dt.getString("ccrq"));
						jsrq = Util.null2String(rs_dt.getString("jsrq"));
						ccmdd = Util.null2String(rs_dt.getString("ccmdd"));
						dfkm = Util.null2String(rs_dt.getString("dfkm"));
					}
					pzsm=dfr+" "+"暂支差旅费";
				}
				updateRecordInfo(requestId,pch);
			}
			if("72".equals(workflowId)){//核销
				Map<String, String> result= new DoHXImpl().doHX(tableName,requestId,bookid,pch,now,SEGMENT1);
				
				if("".equals(bz)){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						bz = geCurrencyCode(Util.null2String(rs_dt.getString("bb")));
					}
				}
				if(rqcount == 1){
					rmb=iu.add(rmb, result.get("rmb"));
					wb=iu.add(wb, result.get("wb"));
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						hxsm = Util.null2String(rs_dt.getString("hxsm")).replace("<br/>", "").replace("<br>", "").replace("</br>", "");
						zzjr = Util.null2String(rs_dt.getString("zzjr")).replaceAll(",", "");
						kxje = Util.null2String(rs_dt.getString("kxje")).replaceAll(",", "");
						ybyt = Util.null2String(rs_dt.getString("ybyt")).replaceAll(",", "");
						hl = Util.null2String(rs_dt.getString("hl"));
						skdw = Util.null2String(rs_dt.getString("skdw"));
						dfkm_hx = Util.null2String(rs_dt.getString("dfkm"));
						zzd = Util.null2String(rs_dt.getString("zzd"));
					}
					if("".equals(zzjr)){
						zzjr = "0";
					}
					if("".equals(kxje)){
						kxje = "0";
					}
					if("".equals(ybyt)){
						ybyt = "0";
					}
					if("".equals(hl)){
						hl = "1";
					}
					zzjr_hl=iu.mul(zzjr, hl);
					kxje_hl=iu.mul(kxje, hl);
					ybyt_hl=iu.mul(ybyt, hl);
					sql_dt="select b.kmbm from uf_zzk a,uf_zzk_dt1 b where a.id=b.mainid and a.id="+zzd;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						zzkm = Util.null2String(rs_dt.getString("kmbm"));
					}
					sql_dt="select skdwmc from uf_skzh where id="+skdw;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						skdwname = Util.null2String(rs_dt.getString("skdwmc"));
					}
					if(Util.getFloatValue(kxje)==Util.getFloatValue(zzjr)){
						type="0";
						pzsm=dfr+" "+hxsm;
						
					}else if(Util.getFloatValue(kxje)>Util.getFloatValue(zzjr)){
						type="1";
						pzsm=dfr+" "+hxsm;
						pzsm2=skdwname+" "+hxsm;						
					}else{
						type="-1";
						pzsm=dfr+" "+hxsm;
						pzsm2=skdwname+" "+hxsm;
						
					}
				}else{
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						hxsm = Util.null2String(rs_dt.getString("skdy"));
						zzjr = Util.null2String(rs_dt.getString("zzjr")).replaceAll(",", "");
						kxje = Util.null2String(rs_dt.getString("kxje")).replaceAll(",", "");
						ybyt = Util.null2String(rs_dt.getString("ybyt")).replaceAll(",", "");
						hl = Util.null2String(rs_dt.getString("hl"));
						skdw = Util.null2String(rs_dt.getString("skdw"));
						dfkm_hx = Util.null2String(rs_dt.getString("dfkm"));
						zzd = Util.null2String(rs_dt.getString("zzd"));
					}
					if("".equals(zzjr)){
						zzjr = "0";
					}
					if("".equals(kxje)){
						kxje = "0";
					}
					if("".equals(ybyt)){
						ybyt = "0";
					}
					if("".equals(hl)){
						hl = "1";
					}
					zzjr_hl=iu.mul(zzjr, hl);
					kxje_hl=iu.mul(kxje, hl);
					ybyt_hl=iu.mul(ybyt, hl);
					sql_dt="select b.kmbm from uf_zzk a,uf_zzk_dt1 b where a.id=b.mainid and a.id="+zzd;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						zzkm = Util.null2String(rs_dt.getString("kmbm"));
					}
					sql_dt="select skdwmc from uf_skzh where id="+skdw;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						skdwname = Util.null2String(rs_dt.getString("skdwmc"));
					}

					if(Util.getFloatValue(zzjr)==Util.getFloatValue("0")){
						rmb=iu.add(rmb, result.get("rmb"));
						wb=iu.add(wb, result.get("wb"));
					}else if(Util.getFloatValue(kxje)==Util.getFloatValue(zzjr)){
						type="0";
						pzsm=dfr+" "+hxsm;
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(kxje));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(kxje_hl));
						}
						mapStr.put("SEGMENT5", dfkm_hx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
					}else if(Util.getFloatValue(kxje)>Util.getFloatValue(zzjr)){
						type="1";
						pzsm=dfr+" "+hxsm;
						pzsm2=skdwname+" "+hxsm;
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(zzjr));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(zzjr_hl));
						}
						mapStr.put("SEGMENT5", dfkm_hx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
						rmb=iu.add(rmb, ybyt_hl);
						wb=iu.add(wb, ybyt);
					}else{
						type="-1";
						pzsm=dfr+" "+hxsm;
						pzsm2=skdwname+" "+hxsm;
						mapStr.put("interface_seq", getInterfaceSeq());
						mapStr.put("CURRENCY_CODE", bz);
						mapStr.put("ENTERED_CR", getMoney(zzjr));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(zzjr_hl));
						}
						mapStr.put("SEGMENT5", dfkm_hx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	

						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
						rmb=iu.add(rmb, ybyt_hl);
						wb=iu.add(wb, ybyt);
					}
				}
				updateRecordInfo(requestId,pch);
			}
			if("71".equals(workflowId)){//请款
				
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
				if(rqcount == 1){
					String mainid_qk="";
					String qkhnr="";
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						mainid_qk=Util.null2String(rs_dt.getString("id"));
						skdy = Util.null2String(rs_dt.getString("skdy"));
						dfkm = Util.null2String(rs_dt.getString("dfkm"));
					}
					sql_dt="select * from "+tableName+"_dt1 where mainid="+mainid_qk;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						qkhnr  =Util.null2String(rs_dt.getString("hxnr")); 
					}
					sql_dt="select skdwmc from uf_skzh where id="+skdy;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						skdyname = Util.null2String(rs_dt.getString("skdwmc"));
					}
					pzsm=skdyname+" "+qkhnr+"等";
				}
				updateRecordInfo(requestId,pch);
			}
			if("70".equals(workflowId)){//暂支			
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
				if(rqcount == 1){
					String mainid_zz="";
					String zzsm="";
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						mainid_zz = Util.null2String(rs_dt.getString("id"));
						skzh = Util.null2String(rs_dt.getString("skzh"));
						fkkm = Util.null2String(rs_dt.getString("fkkm"));
						dfkm = Util.null2String(rs_dt.getString("fkkm"));
					}
					sql_dt="select * from "+tableName+"_dt1 where mainid="+mainid_zz;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						zzsm  =Util.null2String(rs_dt.getString("zzsm")); 
					}
					sql_dt="select skdwmc from uf_skzh where id="+skzh;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						skzhname = Util.null2String(rs_dt.getString("skdwmc"));
					}
					pzsm=skzhname+" "+zzsm;
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
				if(rqcount == 1){
					sql_dt="select * from "+tableName+" where requestid="+requestId;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()){
						ksrq = Util.null2String(rs_dt.getString("ksrq"));
						jsrq = Util.null2String(rs_dt.getString("jsrq"));
						dfkm = Util.null2String(rs_dt.getString("dfkm"));
					}
					
					pzsm=dfr+" "+ksrq+"~"+jsrq+" 医疗费报销";
				}
				updateRecordInfo(requestId,pch);
			}
			
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
		mapStr.put("interface_seq", getInterfaceSeq());
		mapStr.put("CURRENCY_CODE", bz);
		
		mapStr.put("SEGMENT1", SEGMENT1);//
		mapStr.put("SEGMENT2", "0000");//项目码
		mapStr.put("SEGMENT3", "000");//地区码
		mapStr.put("SEGMENT4", "0000");//部门码
		mapStr.put("REFERENCE1","OA"+pch);
		mapStr.put("REFERENCE4","OA"+pch);
		mapStr.put("SEGMENT5", bm);//科目
		
		if(rqcount >1){
			if(Util.getFloatValue(getMoney(wb),0)!= 0 || Util.getFloatValue(getMoney(rmb),0)!= 0) {
				if(Util.getFloatValue(getMoney(wb))<0){
					mapStr.put("ENTERED_CR", "");
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", "");
					}
					mapStr.put("ENTERED_DR", getMoney(wb).replace("-", ""));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_DR", getMoney(rmb).replace("-", ""));
					}
				}else{
					mapStr.put("ENTERED_CR", getMoney(wb));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(rmb));
					}
				}
				mapStr.put("REFERENCE2", "支付报销款");
				mapStr.put("REFERENCE5","支付报销款");
				mapStr.put("REFERENCE10", "支付报销款");
	
				mapStr.put("REFERENCE30", interfaceseq+"");
				 interfaceseq= interfaceseq+10;
				mapStr.put("GROUP_ID",pch);
				iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			}
		}else{
			if("57".equals(workflowId)||"70".equals(workflowId)||"71".equals(workflowId)||"61".equals(workflowId)){
				mapStr.put("ENTERED_CR", getMoney(wb));
				if("USD".equals(bz)){
				mapStr.put("ACCOUNTED_CR", getMoney(rmb));
				}
				mapStr.put("SEGMENT5", dfkm);
				mapStr.put("REFERENCE2", pzsm);
				mapStr.put("REFERENCE5", pzsm);
				mapStr.put("REFERENCE10",pzsm);	

				mapStr.put("REFERENCE30", interfaceseq+"");
				 interfaceseq= interfaceseq+10;
				mapStr.put("GROUP_ID",pch);
				iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			}else if("72".equals(workflowId)){
				if("0".equals(type)){
					mapStr.put("ENTERED_CR", getMoney(wb));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(rmb));
					}
					mapStr.put("SEGMENT5", dfkm_hx);
					mapStr.put("REFERENCE2", pzsm);
					mapStr.put("REFERENCE5", pzsm);
					mapStr.put("REFERENCE10",pzsm);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}else if("1".equals(type)){
					if(Util.getFloatValue(zzjr,0)!=Util.getFloatValue("0")){
						mapStr.put("ENTERED_CR", getMoney(zzjr));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(zzjr_hl));
						}
						mapStr.put("SEGMENT5", dfkm_hx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	
	
						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");					
						mapStr.put("interface_seq", getInterfaceSeq());
					}
					mapStr.put("ENTERED_CR", getMoney(ybyt));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(ybyt_hl));
					}
					mapStr.put("SEGMENT5", bm);
					mapStr.put("REFERENCE2", pzsm2);
					mapStr.put("REFERENCE5", pzsm2);
					mapStr.put("REFERENCE10",pzsm2);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}else{
					mapStr.put("ENTERED_CR", getMoney(zzjr));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(zzjr_hl));
					}
					mapStr.put("SEGMENT5", dfkm_hx);
					mapStr.put("REFERENCE2", pzsm);
					mapStr.put("REFERENCE5", pzsm);
					mapStr.put("REFERENCE10",pzsm);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
					
					mapStr.put("interface_seq", getInterfaceSeq());
					mapStr.put("ENTERED_CR", "");
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", "");
					}
					mapStr.put("ENTERED_DR", getMoney(ybyt).replace("-", ""));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_DR", getMoney(ybyt_hl).replace("-", ""));
					}
					mapStr.put("SEGMENT5", bm);
					mapStr.put("REFERENCE2", pzsm2);
					mapStr.put("REFERENCE5", pzsm2);
					mapStr.put("REFERENCE10",pzsm2);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}
				
			}else if("60".equals(workflowId)){
				if("0".equals(type)){
					mapStr.put("ENTERED_CR", getMoney(wb));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(rmb));
					}
					mapStr.put("SEGMENT5", dfkm_bx);
					mapStr.put("REFERENCE2", pzsm);
					mapStr.put("REFERENCE5", pzsm);
					mapStr.put("REFERENCE10",pzsm);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}else if("1".equals(type)){
					if(Util.getFloatValue(zzjr,0) != Util.getFloatValue("0")){
						mapStr.put("ENTERED_CR", getMoney(yzzje));
						if("USD".equals(bz)){
						mapStr.put("ACCOUNTED_CR", getMoney(yzzje_hl));
						}
						mapStr.put("SEGMENT5", dfkm_bx);
						mapStr.put("REFERENCE2", pzsm);
						mapStr.put("REFERENCE5", pzsm);
						mapStr.put("REFERENCE10",pzsm);	
	
						mapStr.put("REFERENCE30", interfaceseq+"");
						 interfaceseq= interfaceseq+10;
						mapStr.put("GROUP_ID",pch);
						iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
						
						mapStr.put("interface_seq", getInterfaceSeq());
					}
					mapStr.put("ENTERED_CR", getMoney(bt));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(bt_hl));
					}
					mapStr.put("SEGMENT5", bm);
					mapStr.put("REFERENCE2", pzsm2);
					mapStr.put("REFERENCE5", pzsm2);
					mapStr.put("REFERENCE10",pzsm2);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}else{
					mapStr.put("ENTERED_CR", getMoney(yzzje));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", getMoney(yzzje_hl));
					}
					mapStr.put("SEGMENT5", dfkm_bx);
					mapStr.put("REFERENCE2", pzsm);
					mapStr.put("REFERENCE5", pzsm);
					mapStr.put("REFERENCE10",pzsm);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
					
					mapStr.put("interface_seq", getInterfaceSeq());
					mapStr.put("ENTERED_CR", "");
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_CR", "");
					}
					mapStr.put("ENTERED_DR", getMoney(bt).replace("-", ""));
					if("USD".equals(bz)){
					mapStr.put("ACCOUNTED_DR", getMoney(bt_hl).replace("-", ""));
					}
					mapStr.put("SEGMENT5", bm);
					mapStr.put("REFERENCE2", pzsm2);
					mapStr.put("REFERENCE5", pzsm2);
					mapStr.put("REFERENCE10",pzsm2);	

					mapStr.put("REFERENCE30", interfaceseq+"");
					 interfaceseq= interfaceseq+10;
					mapStr.put("GROUP_ID",pch);
					iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
				}
				
			}else{
				mapStr.put("ENTERED_CR", getMoney(wb));
				if("USD".equals(bz)){
				mapStr.put("ACCOUNTED_CR", getMoney(rmb));
				}
				mapStr.put("REFERENCE2", dfr+" "+fkfsname+" "+bm);
				mapStr.put("REFERENCE5", dfr+" "+fkfsname+" "+bm);
				mapStr.put("REFERENCE10", dfr+" "+fkfsname+" "+bm);
				mapStr.put("REFERENCE30", interfaceseq+"");
				 interfaceseq= interfaceseq+10;
				mapStr.put("GROUP_ID",pch);
				iu.insertzj(mapStr, "ZJ_GL_INTERFACE");
			}
		}
		
		
		
		
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
    	RecordSetDataSource rs = new RecordSetDataSource("MPROD");
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
