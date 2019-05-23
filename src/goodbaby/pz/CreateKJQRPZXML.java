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
/**
 * 会计确认凭证
 * @author tangj
 *
 */
public class CreateKJQRPZXML {
	public String CreateXML(String scids,String scr,String pzmlid) {
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql = "";
		String sql_dt = "";
		String xmlName = "";
		String pklcid = "";//排款流程id
		String pcno = "";//批次
		String scrname = "";
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_pk = gg.getTableName("PKLC");//排款
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String nowdate = sf.format(new Date());
		if("1".equals(scr)) {
			scrname = "系统管理员";
		}else {
			sql = "select lastname from hrmresource where id="+scr;
			rs.executeSql(sql);
			if(rs.next()) {
				scrname = Util.null2String(rs.getString("lastname"));
			}
		}
		String fkids[] = scids.split(",");		
		for(String fkid:fkids) {
			pklcid = "";
			pcno = "";
			if("".equals(fkid)) {
				continue;
			}
			sql = "select pklcid,pcno from uf_fkzjb where id="+fkid;
			rs.executeSql(sql);
			if(rs.next()) {
				pklcid = Util.null2String(rs.getString("pklcid"));
				pcno = Util.null2String(rs.getString("pcno"));
			}
			String gys = "";
			String cgdl = "";
			String pkbh = "";
			String gysmc = "";
			sql = "select pkbh from "+tablename_pk+" where requestid="+pklcid;
			rs.executeSql(sql);
			if(rs.next()) {
				pkbh = Util.null2String(rs.getString("pkbh"));
			}
			
			sql = "select gys,max(cgdl) as cgdl,(select gysmc from uf_suppmessForm where id=a.gys) as gysmc from uf_fkzjb a where pklcid='"+pklcid+"' and pcno='"+pcno+"' group by gys";
			rs.executeSql(sql);
			while(rs.next()) {
				gys = Util.null2String(rs.getString("gys"));
				cgdl = Util.null2String(rs.getString("cgdl"));
				gysmc = Util.null2String(rs.getString("gysmc"));
				xmlName = pkbh+"-"+pcno+gysmc+"付款确认凭证.xml";
				String xmlString =getxmlString(gys,pklcid,pcno,scrname,cgdl,pkbh,gysmc);
				try {
					//生成xml文档
					String docid = getDocId(xmlName, xmlString.getBytes("UTF-8"),
							String.valueOf(scr), pzmlid);
					//将文档放到指定表单字段上
					sql_dt = "update  uf_fkzjb  set pzfj='" + docid+ "',scr='"+scr+"',scsj='"+nowdate+"',pzzt='1' where pklcid='"+pklcid+"' and pcno='"+pcno+"' and gys='"+gys+"'";
					rs_dt.executeSql(sql_dt);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		return "1";
	}
	
	public String getxmlString(String gys,String pklcid,String pcno,String scrname,String cgdl,String pkbh,String gysmc) {
		RecordSet rs = new RecordSet();
		GetGNSTableName gg = new GetGNSTableName();
		String tablename_pk = gg.getTableName("PKLC");//排款
		XmlUtil xu = new XmlUtil();
		String sql = "";
		String voucherid = String.valueOf(new Date().getTime());
		String pkids = "";
		String kjqrqq = "";
		String gszt = "";
		String bz = "";
		String hl = "";
		sql = "select b.pkids,a.kjqrqq from uf_fkzjb a,"+tablename_pk+"_dt1 b where a.yfkmxid=b.id and a.gys='"+gys+"' and a.pklcid='"+pklcid+"' and pcno='"+pcno+"' and (b.pkids is not null and b.pkids <> '')";
		rs.executeSql(sql);
		if(rs.next()) {
			pkids = Util.null2String(rs.getString("pkids"));
			kjqrqq = Util.null2String(rs.getString("kjqrqq"));
		}
		if("".equals(pkids)) {
			return "";
		}
		
		sql = "select distinct b.dwzt,(select bz1 from uf_hl where id=a.bz) as bz1,(select hl from uf_hl where id=a.bz) as hl from uf_payinternal a,uf_company b where a.company=b.dw and a.id in("+pkids+")";
		rs.executeSql(sql);
		if(rs.next()) {
			gszt = Util.null2String(rs.getString("dwzt"));
			bz = Util.null2String(rs.getString("bz1"));
			hl = Util.null2String(rs.getString("hl"));
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
		xml.append("<voucher_type>银付</voucher_type>").append("\n");
		xml.append("<fiscal_year>"+kjqrqq.substring(0,4)+"</fiscal_year>").append("\n");
		xml.append("<accounting_period>"+kjqrqq.substring(5,7)+"</accounting_period>").append("\n");
		xml.append("<voucher_id>0</voucher_id>").append("\n");
		xml.append("<attachment_number>0</attachment_number>").append("\n");
		xml.append("<prepareddate>"+kjqrqq+"</prepareddate>").append("\n");
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
		String allpkids = "";
		String flag = "";
		String temppkid="";
		sql = "select b.pkids from uf_fkzjb a,"+tablename_pk+"_dt1 b where a.yfkmxid=b.id and a.gys='"+gys+"' and a.pklcid='"+pklcid+"' and pcno='"+pcno+"' and (b.pkids is not null and b.pkids <> '')";
		rs.executeSql(sql);
		if(rs.next()) {
			temppkid = Util.null2String(rs.getString("pkids"));
			if(!"".equals(temppkid)) {
				allpkids = allpkids +flag+temppkid;
				flag = ",";
			}
		}
		String fyrzkm = "";
		String xjlllx = "";//现金流量类型
		if("2".equals(cgdl)) {//零星
			xjlllx = "2";
			if("人民币".equals(bz)) {
				fyrzkm = "220205010107";
			}else {
				fyrzkm = "220205020106";
			}
		}else if("3".equals(cgdl)) {//服务
			xjlllx = "1";
			if("人民币".equals(bz)) {
				fyrzkm = "220205010108";
			}else {
				fyrzkm = "220205020107";
			}
		}else if("1".equals(cgdl)) {//投资
			xjlllx = "0";
			if("人民币".equals(bz)) {
				fyrzkm = "220205010102";
			}else {
				fyrzkm = "220205020102";
			}
		}
		int entryid = 1;
		sql="select yjcbzx,sum(sfje) as money from uf_payinternal a,uf_invoice b where a.fph=b.id and a.id in("+allpkids+") group by b.yjcbzx";
		rs.executeSql(sql);
		while(rs.next()) {		
			String yjcbzx = Util.null2String(rs.getString("yjcbzx"));
			String money = Util.null2String(rs.getString("money"));
			
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
			xml.append("<abstract>"+gysmc+"NPP支付货款"+pkbh+"</abstract>").append("\n");
			xml.append("<settlement />").append("\n");
			xml.append("<document_id />").append("\n");
			xml.append("<document_date></document_date>").append("\n");
			xml.append("<currency>"+bz+"</currency>").append("\n");
			xml.append("<unit_price>0.00000000</unit_price>").append("\n");
			xml.append("<exchange_rate1>0.00000000</exchange_rate1>").append("\n");
			xml.append("<exchange_rate2>"+hl+"</exchange_rate2> ").append("\n");
			xml.append("<debit_quantity>0.00000000</debit_quantity>").append("\n");
			xml.append("<primary_debit_amount>"+money+"</primary_debit_amount>").append("\n");
			xml.append("<secondary_debit_amount>0.00000000</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>"+money+"</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0.00000000</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>0.00000000</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>0.00000000</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			Map<String, String> fzhsMap = xu.getFZHSMap(fyrzkm, "5");
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
		//贷
		String bcsf = "";
		String dfkm = "";
		sql = "select sum(bcsf) as bcsf,(select kjkmdm from uf_yhzhdzb where id=max(a.xzyh)) as dfkm from uf_fkzjb a where pklcid='"+pklcid+"' and pcno='"+pcno+"' and gys='"+gys+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			bcsf = Util.null2String(rs.getString("bcsf"));
			dfkm = Util.null2String(rs.getString("dfkm"));
		}
		sql="select yjcbzx,sum(sfje) as money from uf_payinternal a,uf_invoice b where a.fph=b.id and a.id in("+allpkids+") group by b.yjcbzx";
		rs.executeSql(sql);
		while(rs.next()) {		
			String yjcbzx = Util.null2String(rs.getString("yjcbzx"));
			String money = Util.null2String(rs.getString("money"));
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			xml.append("<account_code>"+dfkm+"</account_code>").append("\n");
			xml.append("<abstract>NPP支付货款"+pkbh+"</abstract>").append("\n");
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
			xml.append("<primary_credit_amount>"+money+"</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0.00000000</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>"+money+"</natural_credit_currency>").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			Map<String, String> fzhsMap = xu.getFZHSMap(dfkm, "5");
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
				xml.append("<item name=\"现金流量项目\">"+xu.getXjllfzhs(xjlllx,fzhsMap.get("xjllfzhs"))+"</item>").append("\n");
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
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
}
