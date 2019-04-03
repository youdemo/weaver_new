package goodbaby.pz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.axis.encoding.Base64;

import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 付款申请
 * @author tangj
 *
 */
public class CreateFKSQLXXML implements Action{
	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();//流程类型唯一id
		String requestid = info.getRequestid();//具体单据的唯一id
		//tablename_dt1 tablename
		int czz  = info.getRequestManager().getUser().getUID();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String sql = "";
		String doccategory = "";
		String xmlName = "";
		String requestmark = "";
		String cbzx = "";
		String gzrq = "";
		sql = "select requestmark from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			requestmark = Util.null2String(rs.getString("requestmark"));
		}
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = " + workflowID + ")";
        rs.execute(sql);
        if(rs.next()){
            tableName = Util.null2String(rs.getString("tablename"));
        }
       
		xmlName = requestmark+"付款申请零星凭证.xml";
		String xmlString = getXMLString(requestid, czz+"",requestmark);
		sql = "select doccategory from workflow_base   where id=" + workflowID;
		rs.executeSql(sql);
		if (rs.next()) {
			doccategory = Util.null2String(rs.getString("doccategory"));
		}
		String dcg[] = doccategory.split(",");
		String seccategory = dcg[dcg.length - 1];
		try {
			//生成xml文档
			String docid = getDocId(xmlName, xmlString.getBytes("UTF-8"),
					String.valueOf(czz), seccategory);
			//将文档放到指定表单字段上
			sql = "update " + tableName + " set pzfj='" + docid
					+ "' where requestid=" + requestid;
			rs.executeSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	public String getXMLString(String rqid,String scr,String requestmark) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_rk = gg.getTableName("RKD");//入库单
		String tablename_dd = gg.getTableName("CGDD");//订单
		//String tablename_ht = gg.getTableName("FKJHT");//非框架合同
		String tablename_fk = gg.getTableName("FKSQD");//付款申请单
		XmlUtil xu = new XmlUtil();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String sql = "";
		String sql_dt = "";
		String yjcbzx = "";
		String voucherid = String.valueOf(new Date().getTime());
		String scrname = "";
		if("1".equals(scr)) {
			scrname = "系统管理员";
		}else {
			sql = "select lastname from hrmresource where id="+scr;
			rs.executeSql(sql);
			if(rs.next()) {
				scrname = Util.null2String(rs.getString("lastname"));
			}
		}
		String gys = "";
		String GYSMC = "";
		String gszt = "";
		String cgdl = "";
		String gzrq = "";
		sql = "select skrmc,(select GYSMC from uf_suppmessForm where id=a.skrmc) as GYSMC,(select distinct dwzt from uf_company where dw=a.fkgs) as zt,cglb,gzrq from "+tablename_fk+" a where a.requestid="+rqid;
		rs.executeSql(sql);
		if(rs.next()) {
			gys = Util.null2String(rs.getString("skrmc"));
			GYSMC = Util.null2String(rs.getString("GYSMC"));
			gszt = Util.null2String(rs.getString("zt"));
			cgdl = Util.null2String(rs.getString("cglb"));
			gzrq = Util.null2String(rs.getString("gzrq"));
		}
		String rmbws = "";
		sql = "select sum(cast((bcfk-sj)*hl as numeric(18,2))) as rmbws from "+tablename_fk+" a,"+tablename_fk+"_dt1 b where a.id=b.mainid and a.requestid='"+rqid+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			rmbws = Util.null2String(rs.getString("rmbws"));
		}
		String fphm = "";
		sql = "select fphm from "+tablename_fk+" a,"+tablename_fk+"_dt1 b where a.id=b.mainid and a.requestid="+rqid;
		rs.executeSql(sql);
		if(rs.next()) {
			fphm = Util.null2String(rs.getString("fphm"));
		}
		sql = "select (select (select yjcbzx from uf_cbzx where id=t.cbzx) as yjcbzx from "+tablename_rk+" t where t.requestid=b.rid) as yjcbzx from uf_invoice a,uf_invoice_dt1 b where  a.id=b.mainid and a.id="+fphm;
		rs.executeSql(sql);
		if(rs.next()) {
			yjcbzx = Util.null2String(rs.getString("yjcbzx"));
		}
		if(!"".equals(gzrq)) {
			nowDate = gzrq;
		}
		String sender = xu.getSender(gszt);
		String receiver = xu.getReceiver(gszt);
		String sfpphs = xu.getSFPPHS(gszt);
		StringBuffer xml = new StringBuffer();
		xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>").append("\n");
		xml.append("<ufinterface billtype=\"gl\" codeexchanged=\"y\" docid=\"989898989898\" proc=\"add\" receiver=\""+receiver+"\" roottag=\"voucher\" sender=\""+sender+"\">").append("\n");
		xml.append("<voucher id=\""+voucherid+"\">").append("\n");
		xml.append("<voucher_head>").append("\n");
		xml.append("<company>"+receiver+"</company>").append("\n");
		if(!"GCPC".equals(gszt)&&!"GCPC02".equals(gszt)&&!"GCPN".equals(gszt)&&!"GCPX".equals(gszt)&&!"PCPC01".equals(gszt)) {
			xml.append("<voucher_type>记账凭证transfer</voucher_type>").append("\n");
		}else {
			xml.append("<voucher_type>转01</voucher_type>").append("\n");
		}
		
		xml.append("<fiscal_year>"+nowDate.substring(0,4)+"</fiscal_year>").append("\n");
		xml.append("<accounting_period>"+nowDate.substring(5,7)+"</accounting_period>").append("\n");
		xml.append("<voucher_id>3</voucher_id>").append("\n");
		xml.append("<attachment_number>0</attachment_number>").append("\n");
		xml.append("<prepareddate>"+nowDate+"</prepareddate>").append("\n");
		xml.append("<enter>"+scrname+"</enter>").append("\n");
		xml.append("<cashier></cashier>").append("\n");
		xml.append("<signature>N</signature>").append("\n");
		xml.append("<checker></checker>").append("\n");
		xml.append("<posting_date></posting_date>").append("\n");
		xml.append("<posting_person></posting_person>").append("\n");
		xml.append("<voucher_making_system>总账</voucher_making_system>").append("\n");
		xml.append("<memo1></memo1>").append("\n");
		xml.append("<memo2></memo2>").append("\n");
		xml.append("<reserve1></reserve1>").append("\n");
		xml.append("<reserve2>N</reserve2>").append("\n");
		xml.append("<revokeflag />").append("\n");
		xml.append("</voucher_head>").append("\n");
		xml.append("<voucher_body>").append("\n");
		int entryid = 1;
		String fyrzkm = "";
		if("2".equals(cgdl)) {//零星
			fyrzkm = "220205010206";
		}else if("1".equals(cgdl)) {
			fyrzkm = "220205010207";//服务
		}else if("3".equals(cgdl)) {//
			fyrzkm = "220205010203";//投资
		}
		xml.append("<entry>").append("\n");
		xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
		xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
		xml.append("<abstract>"+GYSMC+"NPP支付货款"+requestmark+"</abstract>").append("\n");
		xml.append("<settlement />").append("\n");
		xml.append("<document_id />").append("\n");
		xml.append("<document_date></document_date>").append("\n");
		xml.append("<currency>人民币</currency>").append("\n");
		xml.append("<unit_price>0.00000000</unit_price>").append("\n");
		xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
		xml.append("<exchange_rate2>1</exchange_rate2> ").append("\n");
		xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
		xml.append("<primary_debit_amount>"+rmbws+"</primary_debit_amount>").append("\n");
		xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
		xml.append("<natural_debit_currency>"+rmbws+"</natural_debit_currency>").append("\n");
		xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
		xml.append("<primary_credit_amount>0.00000000</primary_credit_amount>").append("\n");
		xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
		xml.append("<natural_credit_currency>0.00000000</natural_credit_currency>").append("\n");
		xml.append("<bill_type />").append("\n");
		xml.append("<bill_id />").append("\n");
		xml.append("<bill_date />").append("\n");
		xml.append("<auxiliary_accounting>").append("\n");
		Map<String, String> fzhsMap = xu.getFZHSMap(fyrzkm, "4");
		if(!"".equals(fzhsMap.get("wxsybfzhs"))&&!"1".equals(fzhsMap.get("wxsybfzhs"))) {
			if(!"".equals(xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs")))) {
				xml.append("<item name=\"外销事业部辅助核算\">"+xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs"))+"</item>").append("\n");
			}
		}
		if(!"".equals(fzhsMap.get("wxywmsbfzhs"))&&!"1".equals(fzhsMap.get("wxywmsbfzhs"))) {
			xml.append("<item name=\"外销业务模式辅助核算\">"+xu.getWxywmsbfzhs(fzhsMap.get("wxywmsbfzhs"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("bmda"))&&!"1".equals(fzhsMap.get("bmda"))) {
			xml.append("<item name=\"部门档案\">"+xu.getBmda("",fzhsMap.get("bmda"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("ksfzhs"))&&!"1".equals(fzhsMap.get("ksfzhs"))) {
			xml.append("<item name=\"客商辅助核算\">"+xu.getKsfzhs(gys,fzhsMap.get("ksfzhs"))+"</item>").append("\n");
		}else {
			xml.append("<item name=\"客商辅助核算\">"+xu.getSpecialKSFZHS(yjcbzx,"4")+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("wxckfzhs"))&&!"1".equals(fzhsMap.get("wxckfzhs"))) {
			xml.append("<item name=\"外销仓库辅助核算\">"+xu.getWxckfzhs("",fzhsMap.get("wxckfzhs"))+"</item>").append("\n");
		}
		if("0".equals(sfpphs)&&!"".equals(fzhsMap.get("brandfzhs"))&&!"1".equals(fzhsMap.get("brandfzhs"))) {
			xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs("",fzhsMap.get("brandfzhs"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
			xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("wxzjgcfzhs"))&&!"1".equals(fzhsMap.get("wxzjgcfzhs"))) {
			xml.append("<item name=\"外销在建工程辅助核算\">L9</item>").append("\n");
		}
		xml.append("</auxiliary_accounting>").append("\n");
		xml.append("<detail></detail>").append("\n");
		xml.append("</entry>").append("\n");
		entryid++;
		sql="select  cast(b.sj*b.hl as numeric(18,2)) as sjrmb,sfdk,fphm,sl,cgdllcs,(select fphm from uf_invoice where id=b.fphm) as fphmname from "+tablename_fk+" a,"+tablename_fk+"_dt1 b where a.id=b.mainid and a.requestid="+rqid;
		rs.executeSql(sql);
		while(rs.next()) {		
			String sjrmb = Util.null2String(rs.getString("sjrmb"));
			String sfdk = Util.null2String(rs.getString("sfdk"));
			String fphmid = Util.null2String(rs.getString("fphm"));
			String sl = Util.null2String(rs.getString("sl"));
			String cgdllcs = Util.null2String(rs.getString("cgdllcs"));
			String fphmname = Util.null2String(rs.getString("fphmname"));
			String sjkm = "";
			String shck = "";
			String pp = "";
			String cbzx = "";
			if("1".equals(sfdk)) {
				if("2".equals(cgdl)) {
					sjkm = "140404";
					sql_dt = "select (select t.shck from "+tablename_rk+" t where t.requestid=b.rid) as shck from uf_invoice a,uf_invoice_dt1 b where  a.id=b.mainid and a.id="+fphmid;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						shck = Util.null2String(rs_dt.getString("shck"));
					}
				}else if("1".equals(cgdl) || "3".equals(cgdl)) {
					sql_dt = " select (select kmbm1 from uf_fykm where id=a.fykm) as fykmdm,pinpai from "+tablename_dd+" a  where requestid="+cgdllcs;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						sjkm = Util.null2String(rs_dt.getString("fykmdm"));
						pp = Util.null2String(rs_dt.getString("pinpai"));
					}
					
					sql_dt = "select t.cbzx from "+tablename_rk+" t where t.requestid=b.rid) as yjcbzx from uf_invoice a,uf_invoice_dt1 b where  a.id=b.mainid and a.id="+fphm;
					rs_dt.executeSql(sql_dt);
					if(rs_dt.next()) {
						cbzx = Util.null2String(rs_dt.getString("cbzx"));
					}
				}
			}else {
				sql_dt = "select kmdm from uf_sjdzb where sl='"+sl+"'";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()) {
					sjkm = Util.null2String(rs_dt.getString("kmdm"));
				}
			}
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+sjkm+"</account_code>").append("\n");
			xml.append("<abstract>NPP支付货款税金"+fphmname+"</abstract>").append("\n");
			xml.append("<settlement />").append("\n");
			xml.append("<document_id />").append("\n");
			xml.append("<document_date></document_date>").append("\n");
			xml.append("<currency>人民币</currency>").append("\n");
			xml.append("<unit_price>0.00000000</unit_price>").append("\n");
			xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
			xml.append("<exchange_rate2>1</exchange_rate2> ").append("\n");
			xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
			xml.append("<primary_debit_amount>"+sjrmb+"</primary_debit_amount>").append("\n");
			xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>"+sjrmb+"</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>0.00000000</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>0.00000000</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			fzhsMap = xu.getFZHSMap(sjkm, "4");
			if(!"".equals(fzhsMap.get("wxsybfzhs"))&&!"1".equals(fzhsMap.get("wxsybfzhs"))) {
				if(!"".equals(xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs")))) {
					xml.append("<item name=\"外销事业部辅助核算\">"+xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs"))+"</item>").append("\n");
				}
			}
			if(!"".equals(fzhsMap.get("wxywmsbfzhs"))&&!"1".equals(fzhsMap.get("wxywmsbfzhs"))) {
				xml.append("<item name=\"外销业务模式辅助核算\">"+xu.getWxywmsbfzhs(fzhsMap.get("wxywmsbfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("bmda"))&&!"1".equals(fzhsMap.get("bmda"))) {
				xml.append("<item name=\"部门档案\">"+xu.getBmda(cbzx,fzhsMap.get("bmda"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("ksfzhs"))&&!"1".equals(fzhsMap.get("ksfzhs"))) {
				xml.append("<item name=\"客商辅助核算\">"+xu.getKsfzhs(gys,fzhsMap.get("ksfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxckfzhs"))&&!"1".equals(fzhsMap.get("wxckfzhs"))) {
				xml.append("<item name=\"外销仓库辅助核算\">"+xu.getWxckfzhs(shck,fzhsMap.get("wxckfzhs"))+"</item>").append("\n");
			}
			if("0".equals(sfpphs)&&!"".equals(fzhsMap.get("brandfzhs"))&&!"1".equals(fzhsMap.get("brandfzhs"))) {
				xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs(pp,fzhsMap.get("brandfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxzjgcfzhs"))&&!"1".equals(fzhsMap.get("wxzjgcfzhs"))) {
				xml.append("<item name=\"外销在建工程辅助核算\">L9</item>").append("\n");
			}
			xml.append("</auxiliary_accounting>").append("\n");
			xml.append("<detail></detail>").append("\n");
			xml.append("</entry>").append("\n");
			entryid++;
		}
		//贷
		String ybze = "";
		String dfrzkm = "";
		String bz = "";
		String hl = "";
		
		
		sql=" select b.bcfk as yb,(select bz1 from uf_hl where id=b.bz) as bz1,b.hl from "+tablename_fk+" a,"+tablename_fk+"_dt1 b where a.id=b.mainid and a.requestid="+rqid;
		rs.executeSql(sql);
		while(rs.next()) {		
			bz = Util.null2String(rs.getString("bz1"));
			hl = Util.null2String(rs.getString("hl"));
			ybze = Util.null2String(rs.getString("yb"));
			if("2".equals(cgdl)) {//零星
				if("人民币".equals(bz)) {
					dfrzkm = "220205010107";
				}else {
					dfrzkm = "220205020106";
				}
			}else if("1".equals(cgdl)) {//服务
				if("人民币".equals(bz)) {
					dfrzkm = "220205010108";
				}else {
					dfrzkm = "220205020107";
				}
			}else if("3".equals(cgdl)) {//投资
				if("人民币".equals(bz)) {
					dfrzkm = "220205010102";
				}else {
					dfrzkm = "220205020102";
				}
			}
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+dfrzkm+"</account_code>").append("\n");
			xml.append("<abstract>NPP支付货款</abstract>").append("\n");
			xml.append("<settlement></settlement>").append("\n");
			xml.append("<document_id></document_id>").append("\n");
			xml.append("<document_date></document_date>").append("\n");
			xml.append("<currency>"+bz+"</currency>").append("\n");
			xml.append("<unit_price>0.00000000</unit_price>").append("\n");
			xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
			xml.append("<exchange_rate2>"+hl+"</exchange_rate2> ").append("\n");
			xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
			xml.append("<primary_debit_amount>0.00000000</primary_debit_amount>").append("\n");
			xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>0.00000000</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>"+ybze+"</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>"+ybze+"</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			 fzhsMap = xu.getFZHSMap(dfrzkm, "4");
			if(!"".equals(fzhsMap.get("wxsybfzhs"))&&!"1".equals(fzhsMap.get("wxsybfzhs"))) {
				if(!"".equals(xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs")))) {
					xml.append("<item name=\"外销事业部辅助核算\">"+xu.getWxsybfzhs(yjcbzx, fzhsMap.get("wxsybfzhs"))+"</item>").append("\n");
				}
			}
			if(!"".equals(fzhsMap.get("wxywmsbfzhs"))&&!"1".equals(fzhsMap.get("wxywmsbfzhs"))) {
				xml.append("<item name=\"外销业务模式辅助核算\">"+xu.getWxywmsbfzhs(fzhsMap.get("wxywmsbfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("bmda"))&&!"1".equals(fzhsMap.get("bmda"))) {
				xml.append("<item name=\"部门档案\">"+xu.getBmda("",fzhsMap.get("bmda"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("ksfzhs"))&&!"1".equals(fzhsMap.get("ksfzhs"))) {
				xml.append("<item name=\"客商辅助核算\">"+xu.getKsfzhs(gys,fzhsMap.get("ksfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxckfzhs"))&&!"1".equals(fzhsMap.get("wxckfzhs"))) {
				xml.append("<item name=\"外销仓库辅助核算\">"+xu.getWxckfzhs("",fzhsMap.get("wxckfzhs"))+"</item>").append("\n");
			}
			if("0".equals(sfpphs)&&!"".equals(fzhsMap.get("brandfzhs"))&&!"1".equals(fzhsMap.get("brandfzhs"))) {
				xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs("",fzhsMap.get("brandfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxzjgcfzhs"))&&!"1".equals(fzhsMap.get("wxzjgcfzhs"))) {
				xml.append("<item name=\"外销在建工程辅助核算\">L9</item>").append("\n");
			}
			xml.append("</auxiliary_accounting>").append("\n");
			xml.append("<detail></detail>").append("\n");
			xml.append("</entry>").append("\n");
		
			entryid++;
		}
		xml.append("</voucher_body>").append("\n");		
		xml.append("</voucher>").append("\n");
		xml.append("</ufinterface>");
		//new BaseBean().writeLog("测试aaa"+xml.toString());
		return xml.toString();
	}
	
	private String getDocId(String name, byte[] buffer, String createrid,
			String seccategory) throws Exception {
		String docId = "";
		DocInfo di = new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));
		di.setDocSubject(name.substring(0, name.lastIndexOf(".")));
		DocAttachment doca = new DocAttachment();
		doca.setFilename(name);
		// byte[] buffer = new BASE64Decoder().decodeBuffer(value);
		String encode = Base64.encode(buffer);
		doca.setFilecontent(encode);
		DocAttachment[] docs = new DocAttachment[1];
		docs[0] = doca;
		di.setAttachments(docs);
		String departmentId = "-1";
		String sql = "select departmentid from hrmresource where id="
				+ createrid;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		User user = new User();
		if (rs.next()) {
			departmentId = Util.null2String(rs.getString("departmentid"));
		}
		user.setUid(Integer.parseInt(createrid));
		user.setUserDepartment(Integer.parseInt(departmentId));
		user.setLanguage(7);
		user.setLogintype("1");
		user.setLoginip("127.0.0.1");
		DocServiceImpl ds = new DocServiceImpl();
		try {
			docId = String.valueOf(ds.createDocByUser(di, user));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return docId;
	}

	
}
