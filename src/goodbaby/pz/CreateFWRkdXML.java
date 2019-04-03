package goodbaby.pz;

import java.util.Date;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class CreateFWRkdXML {
	public String CreateXML(String begindate,String enddate,String yjcbzx,String scr,String rqids) {
		RecordSet rs = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_rk = gg.getTableName("RKD");//入库单
		String tablename_dd = gg.getTableName("CGDD");//订单
		String tablename_ht = gg.getTableName("FKJHT");//非框架合同
		XmlUtil xu = new XmlUtil();
		String sql = "";
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
		xml.append("<fiscal_year>"+begindate.substring(0,4)+"</fiscal_year>").append("\n");
		xml.append("<accounting_period>"+begindate.substring(5,7)+"</accounting_period>").append("\n");
		xml.append("<voucher_id>3</voucher_id>").append("\n");
		xml.append("<attachment_number>0</attachment_number>").append("\n");
		xml.append("<prepareddate>"+enddate+"</prepareddate>").append("\n");
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
		sql="select c.pinpai,a.fykm,(select kmbm1 from uf_fykm where id=a.fykm) as fykmdm, sum(cast(d.HL*e.FKJE/(1+isnull(b.slrate,0)) as numeric(18,2))) as rmbje, (select cbzxbmmc from uf_cbzx where id=a.cbzx) as cbzxmc,a.cbzx from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e" + 
				" where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and c.contactDtId=e.id and a.requestid in("+rqids+")" + 
				" group by a.fykm,a.cbzx,c.pinpai";
		rs.executeSql(sql);
		while(rs.next()) {		
			String cbzxmc = Util.null2String(rs.getString("cbzxmc"));
			String rmbje = Util.null2String(rs.getString("rmbje"));
			String fyrzkm = Util.null2String(rs.getString("fykmdm"));
			String cbzx = Util.null2String(rs.getString("cbzx"));
			String pinpai = Util.null2String(rs.getString("pinpai"));
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
			xml.append("<abstract>"+cbzxmc+begindate.substring(0,4)+begindate.substring(5,7)+"NPP费用确认暂估</abstract>").append("\n");
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
			Map<String, String> fzhsMap = xu.getFZHSMap(fyrzkm, "1");
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
				xml.append("<item name=\"客商辅助核算\">"+xu.getKsfzhs("",fzhsMap.get("ksfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("wxckfzhs"))&&!"1".equals(fzhsMap.get("wxckfzhs"))) {
				xml.append("<item name=\"外销仓库辅助核算\">"+xu.getWxckfzhs("",fzhsMap.get("wxckfzhs"))+"</item>").append("\n");
			}
			if("0".equals(sfpphs)&&!"".equals(fzhsMap.get("brandfzhs"))&&!"1".equals(fzhsMap.get("brandfzhs"))) {
				xml.append("<item name=\"Brand辅助核算\">"+xu.getBrandfzhs(pinpai,fzhsMap.get("brandfzhs"))+"</item>").append("\n");
			}
			if(!"".equals(fzhsMap.get("xjllfzhs"))&&!"1".equals(fzhsMap.get("xjllfzhs"))) {
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs("1",fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
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
		sql="select sum(cast(d.HL*e.FKJE/(1+isnull(b.slrate,0)) as numeric(18,2))) as rmbje from "+tablename_rk+"  a,"+tablename_rk+"_dt1 b ,"+tablename_dd+" c,"+tablename_ht+" d,"+tablename_ht+"_dt1 e" + 
				" where a.id=b.mainid  and b.cgsqd = c. requestid and c.contactReq = d.requestid and d.id=e.mainid and c.contactDtId=e.id and a.requestid in("+rqids+")";
		rs.executeSql(sql);
		if(rs.next()) {
			rmbze = Util.null2String(rs.getString("rmbje"));
		}
		xml.append("<entry>").append("\n");
		xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
		xml.append("<account_code>220205010207</account_code>").append("\n");
		xml.append("<abstract>"+yjcbzx+begindate.substring(0,4)+begindate.substring(5,7)+"NPP费用确认暂估</abstract>").append("\n");
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
		Map<String, String> fzhsMap = xu.getFZHSMap("220205010207", "1");
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
			xml.append("<item name=\"客商辅助核算\">"+xu.getKsfzhs("",fzhsMap.get("ksfzhs"))+"</item>").append("\n");
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
		xml.append("</voucher_body>").append("\n");		
		xml.append("</voucher>").append("\n");
		xml.append("</ufinterface>");
		
		return xml.toString();
	}
}
