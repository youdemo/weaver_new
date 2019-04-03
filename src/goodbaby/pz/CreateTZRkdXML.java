package goodbaby.pz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.apache.axis.encoding.Base64;
import org.docx4j.model.datastorage.XPathEnhancerParser.relationalExpr_return;

import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateTZRkdXML implements Action{
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
       
		xmlName = requestmark+"投资入库凭证.xml";
		String xmlString = getXMLString(requestid, czz+"");
		if("".equals(xmlString)) {
			sql = "update " + tableName + " set pzfj='' where requestid=" + requestid;
			rs.executeSql(sql);
			return SUCCESS;
		}
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
	public String getXMLString(String rqid,String scr) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_rk = gg.getTableName("RKD");//入库单
		String tablename_dd = gg.getTableName("CGDD");//订单
		String tablename_ht = gg.getTableName("FKJHT");//非框架合同
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
		String cgdl = "";
		sql = "select (select yjcbzx from uf_cbzx where id=a.cbzx) as yjcbzx,a.cbzx,a.cgdl from "+tablename_rk+" a where requestid="+rqid;
		rs.executeSql(sql);
		if(rs.next()) {
			yjcbzx = Util.null2String(rs.getString("yjcbzx"));
			cgdl = Util.null2String(rs.getString("cgdl"));
		}
		if(!"3".equals(cgdl)) {
			return "";
		}
		String gszt = xu.getCompanyCode(yjcbzx);
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
			xml.append("<voucher_type>转05</voucher_type>").append("\n");
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
		String isBefore = "";
		String sfys = "";
		String htrqid = "";
		String htmxid = "";
		String rmbje = "";
		sql="select c.pinpai,a.shck,b.gys,a.fykm,(select kmbm1 from uf_fykm where id=a.fykm) as fykmdm,cast(d.HL*e.FKJE/(1+isnull(b.slrate,0)) as numeric(18,2)) as rmbje, (select cbzxbmmc from uf_cbzx where id=a.cbzx) as cbzxmc,a.cbzx,e.sfys,d.requestid as htrqid,e.id as htmxid  from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e" + 
				"	where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and c.contactDtId=e.id and a.requestid in("+rqid+")";
		rs.executeSql(sql);
		if(rs.next()) {		
			String cbzxmc = Util.null2String(rs.getString("cbzxmc"));
			rmbje = Util.null2String(rs.getString("rmbje"));
			String fyrzkm = Util.null2String(rs.getString("fykmdm"));
			String cbzx = Util.null2String(rs.getString("cbzx"));
			String gys = Util.null2String(rs.getString("gys"));
			String shck = Util.null2String(rs.getString("shck"));
			String pinpai = Util.null2String(rs.getString("pinpai"));
			sfys = Util.null2String(rs.getString("sfys"));
			htrqid = Util.null2String(rs.getString("htrqid"));
			htmxid = Util.null2String(rs.getString("htmxid"));
			
			if(!"1".equals(sfys)) {
				isBefore = checkIsBeforeYs(htrqid,htmxid);
			}
			if("1".equals(isBefore)) {
				return "";
			}else {
				fyrzkm = "160401";
			}
			if("1".equals(sfys)) {
				sql_dt = "select cast(d.HL*d.HTZJXX/(1+isnull(b.slrate,0)) as numeric(18,2)) as rmbje  from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e" + 
						"	where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and c.contactDtId=e.id and a.requestid in("+rqid+")";
				rs_dt.executeSql(sql_dt);
				if(rs_dt.next()) {
					rmbje = Util.null2String(rs_dt.getString("rmbje"));
				}				
				fyrzkm = "160103";
			}
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
			xml.append("<abstract>"+cbzxmc+nowDate.substring(0,4)+nowDate.substring(5,7)+"NPP费用确认暂估</abstract>").append("\n");
			xml.append("<settlement />").append("\n");
			xml.append("<document_id />").append("\n");
			xml.append("<document_date></document_date>").append("\n");
			xml.append("<currency>人民币</currency>").append("\n");
			xml.append("<unit_price>0.00000000</unit_price>").append("\n");
			xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
			xml.append("<exchange_rate2>1</exchange_rate2> ").append("\n");
			xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
			xml.append("<primary_debit_amount>"+rmbje+"</primary_debit_amount>").append("\n");
			xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>"+rmbje+"</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>0.00000000</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>0.00000000</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			Map<String, String> fzhsMap = xu.getFZHSMap(fyrzkm, "2");
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
				xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs(pinpai,fzhsMap.get("brandfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("0",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
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
		String rmbze = "";
		String cbzxmc = "";
		String fyrzkm = "";
		String cbzx = "";
		String gys = "";
		String shck = "";
		String pinpai = "";
		String syje = "";
		sql=" select c.pinpai,a.shck,b.gys,a.fykm,(select kmbm1 from uf_fykm where id=a.fykm) as fykmdm,cast(d.HL*e.FKJE/(1+isnull(b.slrate,0))as numeric(18,2)) as rmbje, (select cbzxbmmc from uf_cbzx where id=a.cbzx) as cbzxmc,a.cbzx  from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e" + 
				"	where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and c.contactDtId=e.id and a.requestid in("+rqid+")";
		rs.executeSql(sql);
		if(rs.next()) {		
			cbzxmc = Util.null2String(rs.getString("cbzxmc"));
			rmbze = Util.null2String(rs.getString("rmbje"));
			fyrzkm = Util.null2String(rs.getString("fykmdm"));
			cbzx = Util.null2String(rs.getString("cbzx"));
			gys = Util.null2String(rs.getString("gys"));
			shck = Util.null2String(rs.getString("shck"));
			 pinpai = Util.null2String(rs.getString("pinpai"));
		}
		fyrzkm = "220205010203";
		if("1".equals(sfys)) {
			sql = "select sum(cast(d.HL*e.FKJE/(1+isnull(b.slrate,0)) as numeric(18,2))) as rmbje  from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e " + 
					"		where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and e.id<=c.contactDtId and a.requestid in("+rqid+")";
			rs.executeSql(sql);
			if(rs.next()) {
				rmbze = Util.null2String(rs.getString("rmbje"));
			}
			sql = "select "+rmbje+"-"+rmbze+" as syje";
			rs.executeSql(sql);
			if(rs.next()) {
				syje = Util.null2String(rs.getString("syje"));
			}
			fyrzkm = "160401";
		}
		xml.append("<entry>").append("\n");
		xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
		xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
		xml.append("<abstract>"+yjcbzx+nowDate.substring(0,4)+nowDate.substring(5,7)+"NPP费用确认暂估</abstract>").append("\n");
		xml.append("<settlement></settlement>").append("\n");
		xml.append("<document_id></document_id>").append("\n");
		xml.append("<document_date></document_date>").append("\n");
		xml.append("<currency>人民币</currency>").append("\n");
		xml.append("<unit_price>0.00000000</unit_price>").append("\n");
		xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
		xml.append("<exchange_rate2>1</exchange_rate2> ").append("\n");
		xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
		xml.append("<primary_debit_amount>0.00000000</primary_debit_amount>").append("\n");
		xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
		xml.append("<natural_debit_currency>0.00000000</natural_debit_currency>").append("\n");
		xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
		xml.append("<primary_credit_amount>"+rmbze+"</primary_credit_amount>").append("\n");
		xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
		xml.append("<natural_credit_currency>"+rmbze+"</natural_credit_currency>").append("\n");
		xml.append("<bill_type />").append("\n");
		xml.append("<bill_id />").append("\n");
		xml.append("<bill_date />").append("\n");
		xml.append("<auxiliary_accounting>").append("\n");
		Map<String, String> fzhsMap = xu.getFZHSMap(fyrzkm, "2");
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
			xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs(pinpai,fzhsMap.get("brandfzhs"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
			xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("0",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
		}
		if(!"".equals(fzhsMap.get("wxzjgcfzhs"))&&!"1".equals(fzhsMap.get("wxzjgcfzhs"))) {
			xml.append("<item name=\"外销在建工程辅助核算\">L9</item>").append("\n");
		}
		xml.append("</auxiliary_accounting>").append("\n");
		xml.append("<detail></detail>").append("\n");
		xml.append("</entry>").append("\n");
		entryid++;
		if("1".equals(sfys) && Util.getFloatValue(syje, 0)>0) {
			fyrzkm = "220205010203";
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
			xml.append("<abstract>"+yjcbzx+nowDate.substring(0,4)+nowDate.substring(5,7)+"NPP费用确认暂估</abstract>").append("\n");
			xml.append("<settlement></settlement>").append("\n");
			xml.append("<document_id></document_id>").append("\n");
			xml.append("<document_date></document_date>").append("\n");
			xml.append("<currency>人民币</currency>").append("\n");
			xml.append("<unit_price>0.00000000</unit_price>").append("\n");
			xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
			xml.append("<exchange_rate2>1</exchange_rate2> ").append("\n");
			xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
			xml.append("<primary_debit_amount>0.00000000</primary_debit_amount>").append("\n");
			xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>0.00000000</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>"+syje+"</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>"+syje+"</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			fzhsMap = xu.getFZHSMap(fyrzkm, "2");
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
				xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs(pinpai,fzhsMap.get("brandfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("0",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxzjgcfzhs"))&&!"1".equals(fzhsMap.get("wxzjgcfzhs"))) {
				xml.append("<item name=\"外销在建工程辅助核算\">L9</item>").append("\n");
			}
			xml.append("</auxiliary_accounting>").append("\n");
			xml.append("<detail></detail>").append("\n");
			xml.append("</entry>").append("\n");
		}
		xml.append("</voucher_body>").append("\n");		
		xml.append("</voucher>").append("\n");
		xml.append("</ufinterface>");
		//new BaseBean().writeLog("测试aaa"+xml.toString());
		return xml.toString();
	}
	
	public String checkIsBeforeYs(String rqid,String mxid) {
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_ht = gg.getTableName("FKJHT");//非框架合同
		RecordSet rs = new RecordSet();
		String result = "";
		int count = 0;
		String sql = "select count(1) as count from "+tablename_ht+" a,"+tablename_ht+"_dt1 b where a.id=b.mainid and a.requestid="+rqid+" and b.id<"+mxid+" and b.sfys='1'";
		rs.executeSql(sql);
		if(rs.next()) {
			count = rs.getInt("count");
		}
		if(count > 0) {
			result = "1";
		}else {
			result = "0";
		}
		return result;
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
