package goodbaby.finance.xml;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.axis.encoding.Base64;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.ibm.db2.jcc.am.l;

import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateXMLForCLFBXD implements Action {

	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();//流程类型唯一id
		String requestid = info.getRequestid();//具体单据的唯一id
		String tablename = info.getRequestManager().getBillTableName();//表名
		//tablename_dt1 tablename
		int czz  = info.getRequestManager().getUser().getUID();
		RecordSet rs = new RecordSet();
		String id = "";
		String lcbh = "";
		String xmlName = "";
		String doccategory = "";
		String sql = "select * from " + tablename + " where requestid="
				+ requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			id = Util.null2String(rs.getString("id"));
			lcbh = Util.null2String(rs.getString("lcbh"));
		}
		xmlName = lcbh+"记帐凭证.xml";
		//拼接xml
		String xmlString = getXMLString(requestid,tablename,xmlName,String.valueOf(czz));
		//获取xml文件存放目录
		sql = "select doccategory from workflow_base   where id=" + workflowID;
		rs.executeSql(sql);
		if (rs.next()) {
			doccategory = Util.null2String(rs.getString("doccategory"));
		}
		String dcg[] = doccategory.split(",");
		String seccategory = dcg[dcg.length - 1];
		try {
			//生成xml文档
			String docid = getDocId(xmlName, xmlString.getBytes(),
					String.valueOf(czz), seccategory);
			//将文档放到指定表单字段上
			sql = "update " + tablename + " set pzfj='" + docid
					+ "' where requestid=" + requestid;
			rs.executeSql(sql);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}
	
	public String getXMLString(String requestid,String tablename,String xmlName,String czz){
		RecordSet rs = new RecordSet();
		StringBuffer xml = new StringBuffer();
		String sqr = "";
		String lcbh = "";
		String departmentid = "";
		String subcompanyid1 = "";
		String receiver = "";
		String sender = "";
		String czzname = "";
		String fyrzkm = "";
		String grjkkm = "";
		String grhkkm = "";
		String requestname = "";
		String zje = "";
		String bcsfjedx = "";
		String hkje = "";
		String wxsybfzhs = "";//外销事业部辅助核算
		String wxywms = "";//外销业务模式
		String bmda = "";//部门档案
		String ryda = "";//人员档案
		String cbzx = "";
		String gs1 = "";
		int entryid =2;
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String sql = "select a.*,zje-bcsfjedx as hkje from " + tablename + " a where a.requestid="+ requestid;
		rs.executeSql(sql);
		if(rs.next()){
			sqr = Util.null2String(rs.getString("sqr"));
			lcbh = Util.null2String(rs.getString("lcbh"));
			zje = Util.null2String(rs.getString("zje"));
			bcsfjedx = Util.null2String(rs.getString("bcsfjedx"));
			hkje = Util.null2String(rs.getString("hkje"));
			cbzx = Util.null2String(rs.getString("cbzx"));
			gs1 = Util.null2String(rs.getString("gs1"));
		}
		sql="select departmentid,subcompanyid1 from hrmresource where id="+sqr;
		rs.executeSql(sql);
		if(rs.next()){
			departmentid = Util.null2String(rs.getString("departmentid"));
			subcompanyid1 = Util.null2String(rs.getString("subcompanyid1"));
		}
		//?
		sql="select receiver,sender from uf_company_base where company='"+gs1+"'";
		rs.executeSql(sql);
		if(rs.next()){
			receiver = Util.null2String(rs.getString("receiver"));
			sender = Util.null2String(rs.getString("sender"));
		}
		sql="select lastname from hrmresource where id="+czz;
		rs.executeSql(sql);
		if(rs.next()){
			czzname = Util.null2String(rs.getString("lastname"));
		}
		//?
		sql="select fyrzkm,grjkkm,grhkkm from uf_department_map where oabm='"+cbzx+"' and gs='"+gs1+"'";
		rs.executeSql(sql);
		if(rs.next()){
			fyrzkm = Util.null2String(rs.getString("fyrzkm"));
			grjkkm = Util.null2String(rs.getString("grjkkm"));
			grhkkm = Util.null2String(rs.getString("grhkkm"));
		}
		//?
		sql="select * from 	uf_gs_km_map where 	gs='"+gs1+"' and km='"+fyrzkm+"'";
		rs.executeSql(sql);
		if(rs.next()){
			wxsybfzhs = Util.null2String(rs.getString("wxsybfzhs"));
			wxywms = Util.null2String(rs.getString("wxsybfzhs"));
			bmda = Util.null2String(rs.getString("bmda"));
			ryda = Util.null2String(rs.getString("ryda"));
		}
		
		sql="select requestname from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			requestname = Util.null2String(rs.getString("requestname"));
		}
		xml.append("<?xml version=\"1.0\" encoding=\"GB2312\" ?>").append("\n");
		xml.append("<ufinterface roottag=\"voucher\" billtype=\"gl\" subtype=\"run\" replace=\"Y\" receiver=\""+receiver+"\" sender=\""+sender+"\" isexchange=\"Y\" filename=\""+xmlName+"\" proc=\"add\" operation=\"req\">").append("\n");
		xml.append("<voucher id=\""+lcbh+"\">").append("\n");
		xml.append("<voucher_head>").append("\n");
		xml.append("<company>"+receiver+"</company>").append("\n");
		xml.append("<voucher_type>付D5</voucher_type>").append("\n");
		xml.append("<fiscal_year>"+nowDate.substring(0,4)+"</fiscal_year>").append("\n");
		xml.append("<accounting_period>"+nowDate.substring(5,7)+"</accounting_period>").append("\n");
		xml.append("<voucher_id>0</voucher_id>").append("\n");
		xml.append("<attachment_number>1</attachment_number>").append("\n");
		xml.append("<prepareddate>"+nowDate+"</prepareddate>").append("\n");
		xml.append("<enter>"+czzname+"</enter>").append("\n");
		xml.append("<cashier></cashier>").append("\n");
		xml.append("<signature>N</signature>").append("\n");
		xml.append("<checker></checker>").append("\n");
		xml.append("<posting_date></posting_date>").append("\n");
		xml.append("<posting_person></posting_person>").append("\n");
		xml.append("<voucher_making_system>XX</voucher_making_system>").append("\n");
		xml.append("<memo1></memo1>").append("\n");
		xml.append("<memo2></memo2>").append("\n");
		xml.append("<reserve1></reserve1>").append("\n");
		xml.append("<reserve2></reserve2>").append("\n");
		xml.append("</voucher_head>").append("\n");
		xml.append("<voucher_body>").append("\n");
		xml.append("<entry_id>1</entry_id> ").append("\n");
		xml.append("<account_code>"+fyrzkm+"</account_code>").append("\n");
		xml.append("<abstract>"+requestname+"OA平台生成凭证</abstract>").append("\n");
		xml.append("<settlement />").append("\n");
		xml.append("<document_id />").append("\n");
		xml.append("<document_date>"+nowDate+"</document_date>").append("\n");
		xml.append("<currency>人民币</currency>").append("\n");
		xml.append("<unit_price>0</unit_price>").append("\n");
		xml.append("<exchange_rate1>1.00000000</exchange_rate1>").append("\n");
		xml.append("<exchange_rate2>0</exchange_rate2> ").append("\n");
		xml.append("<debit_quantity>0</debit_quantity>").append("\n");
		xml.append("<primary_debit_amount>"+zje+"</primary_debit_amount>").append("\n");
		xml.append("<secondary_debit_amount>0</secondary_debit_amount>").append("\n");
		xml.append("<natural_debit_currency>"+zje+"</natural_debit_currency>").append("\n");
		xml.append("<credit_quantity>0</credit_quantity>").append("\n");
		xml.append("<primary_credit_amount>0</primary_credit_amount>").append("\n");
		xml.append("<secondary_credit_amount>0</secondary_credit_amount>").append("\n");
		xml.append("<natural_credit_currency>0</natural_credit_currency>").append("\n");
		xml.append("<bill_type />").append("\n");
		xml.append("<bill_id />").append("\n");
		xml.append("<bill_date />").append("\n");
		xml.append("<auxiliary_accounting>").append("\n");
		if(!"".equals(wxsybfzhs)){
			xml.append("<item name=\"外销事业部辅助核算\">"+wxsybfzhs+"</item>").append("\n");
		}
		if(!"".equals(wxywms)){
			xml.append("<item name=\"外销业务模式\">"+wxywms+"</item>").append("\n");
		}
		if(!"".equals(bmda)){
			xml.append("<item name=\"部门档案\">"+bmda+"</item>").append("\n");
		}
		if(!"".equals(ryda)){
			xml.append("<item name=\"人员档案\">"+ryda+"</item>").append("\n");
		}
		xml.append("</auxiliary_accounting>").append("\n");
		xml.append("<detail />").append("\n");
		for(int i=1;i<=30;i++){
			xml.append("<freeitem"+i+" />").append("\n");
		}
		if(Util.getFloatValue(hkje)>0){
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			//?
			xml.append("<account_code>"+grhkkm+"</account_code>").append("\n");
			xml.append("<abstract>OA平台生成凭证</abstract>").append("\n");
			xml.append("<settlement /> ").append("\n");
			xml.append("<document_id />").append("\n");
			xml.append("<document_date>"+nowDate+"</document_date> ").append("\n");
			xml.append("<currency>人民币</currency>").append("\n");
			xml.append("<unit_price>0</unit_price>").append("\n");
			xml.append("<exchange_rate1>1.00000000</exchange_rate1> ").append("\n");
			xml.append("<exchange_rate2>0</exchange_rate2>").append("\n");
			xml.append("<primary_debit_amount>0</primary_debit_amount> ").append("\n");
			xml.append("<secondary_debit_amount>0</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>0</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>"+hkje+"</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>"+hkje+"</natural_credit_currency> ").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			if(!"".equals(wxsybfzhs)){
				xml.append("<item name=\"外销事业部辅助核算\">"+wxsybfzhs+"</item>").append("\n");
			}
			if(!"".equals(wxywms)){
				xml.append("<item name=\"外销业务模式\">"+wxywms+"</item>").append("\n");
			}
			if(!"".equals(bmda)){
				xml.append("<item name=\"部门档案\">"+bmda+"</item>").append("\n");
			}
			if(!"".equals(ryda)){
				xml.append("<item name=\"人员档案\">"+ryda+"</item>").append("\n");
			}
			xml.append("</auxiliary_accounting>").append("\n");
			xml.append("<detail />").append("\n");
			for(int i=1;i<=30;i++){
				xml.append("<freeitem"+i+" />").append("\n");
			}
			xml.append("</entry>").append("\n");
			entryid=entryid+1;
		}
		if(Util.getFloatValue(bcsfjedx)>0){
			xml.append("<entry>").append("\n");
			xml.append("<entry_id>"+entryid+"</entry_id> ").append("\n");
			//?
			xml.append("<account_code>"+grjkkm+"</account_code>").append("\n");
			xml.append("<abstract>OA平台生成凭证</abstract>").append("\n");
			xml.append("<settlement /> ").append("\n");
			xml.append("<document_id />").append("\n");
			xml.append("<document_date>"+nowDate+"</document_date> ").append("\n");
			xml.append("<currency>人民币</currency>").append("\n");
			xml.append("<unit_price>0</unit_price>").append("\n");
			xml.append("<exchange_rate1>1.00000000</exchange_rate1> ").append("\n");
			xml.append("<exchange_rate2>0</exchange_rate2>").append("\n");
			xml.append("<primary_debit_amount>0</primary_debit_amount> ").append("\n");
			xml.append("<secondary_debit_amount>0</secondary_debit_amount>").append("\n");
			xml.append("<natural_debit_currency>0</natural_debit_currency>").append("\n");
			xml.append("<credit_quantity>0</credit_quantity>").append("\n");
			xml.append("<primary_credit_amount>"+bcsfjedx+"</primary_credit_amount>").append("\n");
			xml.append("<secondary_credit_amount>0</secondary_credit_amount>").append("\n");
			xml.append("<natural_credit_currency>"+bcsfjedx+"</natural_credit_currency> ").append("\n");
			xml.append("<bill_type />").append("\n");
			xml.append("<bill_id />").append("\n");
			xml.append("<bill_date />").append("\n");
			xml.append("<auxiliary_accounting>").append("\n");
			if(!"".equals(wxsybfzhs)){
				xml.append("<item name=\"外销事业部辅助核算\">"+wxsybfzhs+"</item>").append("\n");
			}
			if(!"".equals(wxywms)){
				xml.append("<item name=\"外销业务模式\">"+wxywms+"</item>").append("\n");
			}
			if(!"".equals(bmda)){
				xml.append("<item name=\"部门档案\">"+bmda+"</item>").append("\n");
			}
			if(!"".equals(ryda)){
				xml.append("<item name=\"人员档案\">"+ryda+"</item>").append("\n");
			}
			xml.append("</auxiliary_accounting>").append("\n");
			xml.append("<detail />").append("\n");
			for(int i=1;i<=30;i++){
				xml.append("<freeitem"+i+" />").append("\n");
			}
			xml.append("</entry>").append("\n");
		}
		xml.append("</voucher_body>").append("\n");
		xml.append("</voucher>").append("\n");
		xml.append("</ufinterface");
		
		return xml.toString();
	}
/**
 * 
 * @param name 文件名
 * @param buffer  文件字节数组
 * @param createrid 文件创建人id
 * @param seccategory 创建目录
 * @return
 * @throws Exception
 */
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
