package gvo.webservice;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.db2.jcc.sqlj.m;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;


public class CreateRequestServiceOANewImpl {
	public String CreatePurchaseOrder(String workcode, String dataInfo) throws Exception{
		RecordSet rs = new RecordSet();
		String result = "";
		String creater = "";
		String sql = "";
		String workflowid = "";
		String tablename ="";
		Map<String, String> retMap = new HashMap<String, String>();
		
		sql="select id from hrmresource where workcode='"+workcode+"' and status<4 and nvl(belongto,0)<=0";
		rs.executeSql(sql);
		if(rs.next()){
			creater = Util.null2String(rs.getString("id"));
		}
		if("".equals(creater)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "人员编号无法匹配");
			retMap.put("OA_ID", "0");	
			return getJsonStr(retMap);
		}
		JSONObject jo = new JSONObject(dataInfo);
		
		String workflowType = "";
		workflowType = jo.getJSONObject("HEADER").getString("POTYPE");
		sql="select wfid from uf_purchase_flow_mt where code='"+workflowType+"'";
		rs.executeSql(sql);
		if(rs.next()){
			workflowid = Util.null2String(rs.getString("wfid"));
		}
		if("".equals(workflowid)){
			retMap.put("MSG_TYPE", "E");
			retMap.put("MSG_CONTENT", "流程类型无法匹配");
			retMap.put("OA_ID", "0");
			return getJsonStr(retMap);
		}
		String jsonstr="";
		//一般物料采购
		if("NB".equals(workflowType)||"PO02".equals(workflowType)||"PO04".equals(workflowType)){
			jsonstr = getNBJson(dataInfo,creater);
		}else if("PO01".equals(workflowType)){//固定资产
			jsonstr = getPO01Json(dataInfo,creater);
		}else if("PO03".equals(workflowType)){//费用类采购订单
			jsonstr = getPO03Json(dataInfo,creater);
		}else if("PO07".equals(workflowType)){//现金购采购订单
			jsonstr = getPO07Json(dataInfo,creater);
		}
		if(!"".equals(jsonstr)){
			AutoRequestService ars = new AutoRequestService();
			result = ars.createRequest(workflowid, jsonstr, creater, "1");			
		}
		return result;
	}
	/**
	 * 一般物料采购
	 * 
	 * @param datainfo
	 * @param creater
	 * @return
	 * @throws Exception
	 */
	public String getNBJson(String datainfo,String creater) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String EBELN = "";// 采购订单号
		String BEDAT = "";// 凭证日期
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String EKORG = "";// 采购组织
		String EKOTX = "";// 采购组织（描述）
		String EKGRP = "";// 采购组
		String EKNAM = "";// 采购组（描述）
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String BRTWR = "";// 订单总金额
		String WKURS = "";//汇率
		// 明细1
		String EBELP = "";// 项目
		String MATNR = "";// 物料
		String TXZ01 = "";// 短文本
		String MENGE = "";// 采购订单数量
		String MEINS = "";// 订单单位
		String EINDT = "";// 交货日期
		String NETPR = "";// 净价
		String KBETR = "";// 含税价
		String WAERS = "";// 货币
		String MWSKZ = "";// 税码
		String PEINH = "";// 每
		String BPRME = "";// 价格单位
		String NAME1_dt1 = "";// 工厂
		String LGOBE = "";// 库存地点
		String WGBEZ = "";// 物料组
		String AFNAM = "";// 申请者
		String INFNR = "";// 采购信息记录
		String BANFN = "";// 采购申请
		String BNFPO = "";// 请求项目
		String KNTTP = "";// 科目分配类别
		String PSTYP = "";// 项目类别
		String SAKTO = "";// 总账科目
		String KOSTL = "";// 成本中心
		String AUFNR = "";// 订单
		String ANLN1 = "";// 固定资产编号
		String ANLN2 = "";// 次级编号
		String TEXT1 = "";// 税码描述
		String TXT20 = "";// 总账科目描述
		String LTEXT = "";// 成本中心描述
		String KTEXT = "";// 订单描述
		String TXT50 = "";// 固定资产描述
		String LOEKZ = "";// 删除标识
		String BRTWR_dt = "";// 行项目金额
		ResourceComInfo rci = new ResourceComInfo();
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(datainfo);
		JSONObject head = jo.getJSONObject("HEADER");
		// System.out.println(head.toString());
		EBELN = head.getString("EBELN");
		BEDAT = head.getString("BEDAT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		EKORG = head.getString("EKORG");
		EKOTX = head.getString("EKOTX");
		EKGRP = head.getString("EKGRP");
		EKNAM = head.getString("EKNAM");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		BRTWR = head.getString("BRTWR");
		WKURS = head.getString("WKURS");
		header.put("Applicant", creater);
		header.put("sqrbh", rci.getWorkcode(creater));
		header.put("dept", rci.getDepartmentID(creater));
		header.put("appDate", now);
		header.put("orderNo", EBELN);
		header.put("certifidate", BEDAT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("PurchaseOrg", EKORG);
		header.put("PurOrgDesc", EKOTX);
		header.put("PurchaseGro", EKGRP);
		header.put("PurGroDesc", EKNAM);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("brtwr", getmult(BRTWR, WKURS));
		header.put("hl", WKURS);
		header.put("ddzjeyb", BRTWR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO").replaceAll("^(0+)", "");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);//
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);//
			node.put("costcenter", KOSTL);//
			node.put("orders", AUFNR);//
			node.put("assetNum", ANLN1);//
			node.put("subNum", ANLN2);//
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);//
			node.put("KTEXT", KTEXT);//
			node.put("TXT50", TXT50);//
			node.put("loekz", LOEKZ);
			node.put("brtwr", getmult(BRTWR_dt, WKURS));//
			node.put("jeyb", BRTWR_dt);//
			Map<String, String> map = getPurchaseMiddleInfo(BANFN, BNFPO);
			node.put("cdztlx", "1");
			node.put("fycdbm", map.get("cdbm"));
			node.put("fykm", map.get("yskm"));
			node.put("fyqj", map.get("qj"));
			node.put("ysdjje", getJe(MENGE,NETPR,WKURS));
			dt11.put(node);
		}
		details.put("DT1", dt11);

		return json.toString();
	}
	/**
	 * 固定资产
	 * @param datainfo
	 * @param creater
	 * @return
	 * @throws Exception
	 */
	public String getPO01Json(String datainfo,String creater) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String EBELN = "";// 采购订单号
		String BEDAT = "";// 凭证日期
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String EKORG = "";// 采购组织
		String EKOTX = "";// 采购组织（描述）
		String EKGRP = "";// 采购组
		String EKNAM = "";// 采购组（描述）
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String BRTWR = "";// 订单总金额
		String WKURS = "";//汇率
		// 明细1
		String EBELP = "";// 项目
		String MATNR = "";// 物料
		String TXZ01 = "";// 短文本
		String MENGE = "";// 采购订单数量
		String MEINS = "";// 订单单位
		String EINDT = "";// 交货日期
		String NETPR = "";// 净价
		String KBETR = "";// 含税价
		String WAERS = "";// 货币
		String MWSKZ = "";// 税码
		String PEINH = "";// 每
		String BPRME = "";// 价格单位
		String NAME1_dt1 = "";// 工厂
		String LGOBE = "";// 库存地点
		String WGBEZ = "";// 物料组
		String AFNAM = "";// 申请者
		String INFNR = "";// 采购信息记录
		String BANFN = "";// 采购申请
		String BNFPO = "";// 请求项目
		String KNTTP = "";// 科目分配类别
		String PSTYP = "";// 项目类别
		String SAKTO = "";// 总账科目
		String KOSTL = "";// 成本中心
		String AUFNR = "";// 订单
		String ANLN1 = "";// 固定资产编号
		String ANLN2 = "";// 次级编号
		String TEXT1 = "";// 税码描述
		String TXT20 = "";// 总账科目描述
		String LTEXT = "";// 成本中心描述
		String KTEXT = "";// 订单描述
		String TXT50 = "";// 固定资产描述
		String LOEKZ = "";// 删除标识
		String BRTWR_dt = "";// 行项目金额
		ResourceComInfo rci = new ResourceComInfo();
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(datainfo);
		JSONObject head = jo.getJSONObject("HEADER");
		// System.out.println(head.toString());
		EBELN = head.getString("EBELN");
		BEDAT = head.getString("BEDAT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		EKORG = head.getString("EKORG");
		EKOTX = head.getString("EKOTX");
		EKGRP = head.getString("EKGRP");
		EKNAM = head.getString("EKNAM");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		BRTWR = head.getString("BRTWR");
		WKURS = head.getString("WKURS");
		header.put("Applicant", creater);
		header.put("sqrbh", rci.getWorkcode(creater));
		header.put("dept", rci.getDepartmentID(creater));
		header.put("appDate", now);
		header.put("orderNo", EBELN);
		header.put("certifidate", BEDAT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("PurchaseOrg", EKORG);
		header.put("PurOrgDesc", EKOTX);
		header.put("PurchaseGro", EKGRP);
		header.put("PurGroDesc", EKNAM);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("brtwr", getmult(BRTWR, WKURS));
		header.put("hl", WKURS);
		header.put("ddzjeyb", BRTWR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO").replaceAll("^(0+)", "");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);//
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);
			node.put("costcenter", KOSTL);//
			node.put("orders", AUFNR);//
			node.put("assetNum", ANLN1);
			node.put("subNum", ANLN2);
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);
			node.put("KTEXT", KTEXT);//
			node.put("TXT50", TXT50);//
			node.put("loekz", LOEKZ);
			node.put("brtwr", getmult(BRTWR_dt, WKURS));//
			node.put("jeyb", BRTWR_dt);//
			Map<String, String> map = getPurchaseMiddleInfo(BANFN, BNFPO);
			node.put("fycdzt", "1");
			node.put("fycdbm", map.get("cdbm"));
			node.put("fykm", map.get("yskm"));
			node.put("fyqj", map.get("qj"));
			node.put("ysdjje", getmult(BRTWR_dt, WKURS));
			dt11.put(node);
		}
		details.put("DT1", dt11);

		return json.toString();
	}
	/**
	 * 费用类采购订单
	 * @param datainfo
	 * @param creater
	 * @return
	 * @throws Exception
	 */
	public String getPO03Json(String datainfo,String creater) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String EBELN = "";// 采购订单号
		String BEDAT = "";// 凭证日期
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String EKORG = "";// 采购组织
		String EKOTX = "";// 采购组织（描述）
		String EKGRP = "";// 采购组
		String EKNAM = "";// 采购组（描述）
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String BRTWR = "";// 订单总金额
		String WKURS = "";//汇率
		// 明细1
		String EBELP = "";// 项目
		String MATNR = "";// 物料
		String TXZ01 = "";// 短文本
		String MENGE = "";// 采购订单数量
		String MEINS = "";// 订单单位
		String EINDT = "";// 交货日期
		String NETPR = "";// 净价
		String KBETR = "";// 含税价
		String WAERS = "";// 货币
		String MWSKZ = "";// 税码
		String PEINH = "";// 每
		String BPRME = "";// 价格单位
		String NAME1_dt1 = "";// 工厂
		String LGOBE = "";// 库存地点
		String WGBEZ = "";// 物料组
		String AFNAM = "";// 申请者
		String INFNR = "";// 采购信息记录
		String BANFN = "";// 采购申请
		String BNFPO = "";// 请求项目
		String KNTTP = "";// 科目分配类别
		String PSTYP = "";// 项目类别
		String SAKTO = "";// 总账科目
		String KOSTL = "";// 成本中心
		String AUFNR = "";// 订单
		String ANLN1 = "";// 固定资产编号
		String ANLN2 = "";// 次级编号
		String TEXT1 = "";// 税码描述
		String TXT20 = "";// 总账科目描述
		String LTEXT = "";// 成本中心描述
		String KTEXT = "";// 订单描述
		String TXT50 = "";// 固定资产描述
		String LOEKZ = "";// 删除标识
		String BRTWR_dt = "";// 行项目金额
		ResourceComInfo rci = new ResourceComInfo();
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(datainfo);
		JSONObject head = jo.getJSONObject("HEADER");
		// System.out.println(head.toString());
		EBELN = head.getString("EBELN");
		BEDAT = head.getString("BEDAT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		EKORG = head.getString("EKORG");
		EKOTX = head.getString("EKOTX");
		EKGRP = head.getString("EKGRP");
		EKNAM = head.getString("EKNAM");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		BRTWR = head.getString("BRTWR");
		WKURS = head.getString("WKURS");
		header.put("Applicant", creater);
		header.put("sqrbh", rci.getWorkcode(creater));
		header.put("dept", rci.getDepartmentID(creater));
		header.put("appDate", now);
		header.put("orderNo", EBELN);
		header.put("certifidate", BEDAT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("PurchaseOrg", EKORG);
		header.put("PurOrgDesc", EKOTX);
		header.put("PurchaseGro", EKGRP);
		header.put("PurGroDesc", EKNAM);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("brtwr", getmult(BRTWR, WKURS));
		header.put("hl", WKURS);
		header.put("ddzjeyb", BRTWR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO").replaceAll("^(0+)", "");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);//
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);//
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);
			node.put("costcenter", KOSTL);
			node.put("orders", AUFNR);//
			node.put("assetNum", ANLN1);//
			node.put("subNum", ANLN2);//
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);
			node.put("KTEXT", KTEXT);//
			node.put("TXT50", TXT50);//
			node.put("loekz", LOEKZ);
			node.put("brtwr", getmult(BRTWR_dt, WKURS));//
			node.put("jeyb", BRTWR_dt);//
			Map<String, String> map = getPurchaseMiddleInfo(BANFN, BNFPO);
			node.put("cdztlx", "1");
			node.put("fycdbm", map.get("cdbm"));
			node.put("fykm", map.get("yskm"));
			String qj=now;
			if(!"".equals(map.get("qj"))){
				if(!map.get("qj").substring(0,4).equals(now.substring(0, 4))){
					qj=map.get("qj");
				}
			}
			node.put("fyqj", qj);
			node.put("ysdjje",getJe(MENGE,NETPR,WKURS));
			dt11.put(node);
		}
		details.put("DT1", dt11);

		return json.toString();
	}
	/**
	 * 现金购采购订单
	 * @param datainfo
	 * @param creater
	 * @return
	 * @throws Exception
	 */
	public String getPO07Json(String datainfo,String creater) throws Exception{
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String EBELN = "";// 采购订单号
		String BEDAT = "";// 凭证日期
		String LIFNR = "";// 供应商
		String NAME1 = "";// 供应商名称
		String EKORG = "";// 采购组织
		String EKOTX = "";// 采购组织（描述）
		String EKGRP = "";// 采购组
		String EKNAM = "";// 采购组（描述）
		String BUKRS = "";// 公司代码
		String BUTXT = "";// 公司名称
		String BRTWR = "";// 订单总金额
		String WKURS = "";//汇率
		// 明细1
		String EBELP = "";// 项目
		String MATNR = "";// 物料
		String TXZ01 = "";// 短文本
		String MENGE = "";// 采购订单数量
		String MEINS = "";// 订单单位
		String EINDT = "";// 交货日期
		String NETPR = "";// 净价
		String KBETR = "";// 含税价
		String WAERS = "";// 货币
		String MWSKZ = "";// 税码
		String PEINH = "";// 每
		String BPRME = "";// 价格单位
		String NAME1_dt1 = "";// 工厂
		String LGOBE = "";// 库存地点
		String WGBEZ = "";// 物料组
		String AFNAM = "";// 申请者
		String INFNR = "";// 采购信息记录
		String BANFN = "";// 采购申请
		String BNFPO = "";// 请求项目
		String KNTTP = "";// 科目分配类别
		String PSTYP = "";// 项目类别
		String SAKTO = "";// 总账科目
		String KOSTL = "";// 成本中心
		String AUFNR = "";// 订单
		String ANLN1 = "";// 固定资产编号
		String ANLN2 = "";// 次级编号
		String TEXT1 = "";// 税码描述
		String TXT20 = "";// 总账科目描述
		String LTEXT = "";// 成本中心描述
		String KTEXT = "";// 订单描述
		String TXT50 = "";// 固定资产描述
		String LOEKZ = "";// 删除标识
		String BRTWR_dt = "";// 行项目金额
		ResourceComInfo rci = new ResourceComInfo();
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();

		json.put("HEADER", header);
		json.put("DETAILS", details);
		JSONObject jo = new JSONObject(datainfo);
		JSONObject head = jo.getJSONObject("HEADER");
		// System.out.println(head.toString());
		EBELN = head.getString("EBELN");
		BEDAT = head.getString("BEDAT");
		LIFNR = head.getString("LIFNR");
		NAME1 = head.getString("NAME1");
		EKORG = head.getString("EKORG");
		EKOTX = head.getString("EKOTX");
		EKGRP = head.getString("EKGRP");
		EKNAM = head.getString("EKNAM");
		BUKRS = head.getString("BUKRS");
		BUTXT = head.getString("BUTXT");
		BRTWR = head.getString("BRTWR");
		WKURS = head.getString("WKURS");
		header.put("Applicant", creater);
		header.put("sqrbh", rci.getWorkcode(creater));
		header.put("dept", rci.getDepartmentID(creater));
		header.put("appDate", now);
		header.put("orderNo", EBELN);
		header.put("certifidate", BEDAT);
		header.put("supplier", LIFNR);
		header.put("supplierName", NAME1);
		header.put("PurchaseOrg", EKORG);
		header.put("PurOrgDesc", EKOTX);
		header.put("PurchaseGro", EKGRP);
		header.put("PurGroDesc", EKNAM);
		header.put("companyCode", BUKRS);
		header.put("companyName", BUTXT);
		header.put("brtwr", getmult(BRTWR, WKURS));
		header.put("hl", WKURS);
		header.put("ddzjeyb", BRTWR);
		JSONObject dts = jo.getJSONObject("DETALIS");
		JSONArray dt1 = dts.getJSONArray("DT1");
		JSONArray dt11 = new JSONArray();
		for (int i = 0; i < dt1.length(); i++) {
			JSONObject arr = dt1.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO").replaceAll("^(0+)", "");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);//
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);//
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);
			node.put("costcenter", KOSTL);
			node.put("orders", AUFNR);//
			node.put("assetNum", ANLN1);//
			node.put("subNum", ANLN2);//
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);
			node.put("KTEXT", KTEXT);//
			node.put("TXT50", TXT50);//
			node.put("loekz", LOEKZ);
			node.put("brtwr", getmult(BRTWR_dt, WKURS));//
			node.put("jeyb", BRTWR_dt);//
			Map<String, String> map = getPurchaseMiddleInfo(BANFN, BNFPO);
			node.put("cdztlx", "0");
			node.put("fycdbm", map.get("cdbm"));
			node.put("fykm", map.get("yskm"));
			
			String qj=now;
			if(!"".equals(map.get("qj"))){
				if(!map.get("qj").substring(0,4).equals(now.substring(0, 4))){
					qj=map.get("qj");
				}
			}
			node.put("fyqj", qj);
			String type = map.get("type");
			if("0".equals(type)){
				node.put("ysdjje", getJe(MENGE,NETPR,WKURS));
			}else{
				node.put("ysdjje", getmult(BRTWR_dt, WKURS));
			}
			dt11.put(node);
		}
		details.put("DT1", dt11);
		
		JSONObject dts2 = jo.getJSONObject("DETALIS1");
		JSONArray dt2 = dts2.getJSONArray("DT1");
		JSONArray dt22 = new JSONArray();
		for (int i = 0; i < dt2.length(); i++) {
			JSONObject arr = dt2.getJSONObject(i);
			JSONObject node = new JSONObject();
			// System.out.println(arr.toString());
			EBELP = arr.getString("EBELP");
			MATNR = arr.getString("MATNR");
			TXZ01 = arr.getString("TXZ01");
			MENGE = arr.getString("MENGE");
			MEINS = arr.getString("MEINS");
			EINDT = arr.getString("EINDT");
			NETPR = arr.getString("NETPR");
			KBETR = arr.getString("KBETR");
			WAERS = arr.getString("WAERS");
			MWSKZ = arr.getString("MWSKZ");
			PEINH = arr.getString("PEINH");
			BPRME = arr.getString("BPRME");
			NAME1_dt1 = arr.getString("NAME1");
			LGOBE = arr.getString("LGOBE");
			WGBEZ = arr.getString("WGBEZ");
			AFNAM = arr.getString("AFNAM");
			INFNR = arr.getString("INFNR");
			BANFN = arr.getString("BANFN");
			BNFPO = arr.getString("BNFPO").replaceAll("^(0+)", "");
			KNTTP = arr.getString("KNTTP");
			PSTYP = arr.getString("PSTYP");
			SAKTO = arr.getString("SAKTO");
			KOSTL = arr.getString("KOSTL");
			AUFNR = arr.getString("AUFNR");
			ANLN1 = arr.getString("ANLN1");
			ANLN2 = arr.getString("ANLN2");
			TEXT1 = arr.getString("TEXT1");
			TXT20 = arr.getString("TXT20");
			LTEXT = arr.getString("LTEXT");
			KTEXT = arr.getString("KTEXT");
			TXT50 = arr.getString("TXT50");

			LOEKZ = arr.getString("LOEKZ");
			BRTWR_dt = arr.getString("BRTWR");

			node.put("project", EBELP);
			node.put("material", MATNR);//
			node.put("shorttext", TXZ01);
			node.put("purordernum", MENGE);
			node.put("unit", MEINS);
			node.put("deliveryDate", EINDT);
			node.put("netprice", NETPR);
			node.put("hastaxprice", KBETR);
			node.put("currency", WAERS);
			node.put("taxCode", MWSKZ);
			node.put("PEINH", PEINH);
			node.put("priceUnit", BPRME);
			node.put("glant", NAME1_dt1);
			node.put("stolocation", LGOBE);
			node.put("materialGro", WGBEZ);
			node.put("Applicant", AFNAM);
			node.put("purrecord", INFNR);//
			node.put("purrequest", BANFN);
			node.put("reqproject", BNFPO);
			node.put("asstype", KNTTP);
			node.put("protype", PSTYP);
			node.put("generAccount", SAKTO);
			node.put("costcenter", KOSTL);
			node.put("orders", AUFNR);//
			node.put("assetNum", ANLN1);//
			node.put("subNum", ANLN2);//
			node.put("TEXT1", TEXT1);
			node.put("TXT20", TXT20);
			node.put("LTEXT", LTEXT);
			node.put("KTEXT", KTEXT);//
			node.put("TXT50", TXT50);//
			node.put("loekz", LOEKZ);
			node.put("brtwr", getmult(BRTWR_dt, WKURS));//
			node.put("jeyb", BRTWR_dt);//
			Map<String, String> map = getPurchaseMiddleInfo(BANFN, BNFPO);
			node.put("cdztlx", "1");
			node.put("fycdbm", map.get("cdbm"));
			node.put("fykm", map.get("yskm"));
			node.put("fyqj", map.get("qj"));
			node.put("ysdjje", getJe(MENGE,NETPR,WKURS));
			
			
			dt22.put(node);
		}
		details.put("DT2", dt22);
		return json.toString();
	}
	
	public Map<String, String> getPurchaseMiddleInfo(String cgsqdh,String mxhid){
		RecordSet rs = new RecordSet();
		String cdbm="";
		String yskm="";
		String qj = "";
		String type = "";
		Map<String,String> map = new HashMap<String, String>();
		String sql="select * from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid = '"+mxhid+"' order by id asc";
		rs.executeSql(sql);
		if(rs.next()){
			cdbm = Util.null2String(rs.getString("cdbm"));
			yskm = Util.null2String(rs.getString("yskm"));
			qj = Util.null2String(rs.getString("qj"));	
			type = Util.null2String(rs.getString("type"));	
		}
		map.put("cdbm",cdbm);
		map.put("yskm",yskm);
		map.put("qj", qj);
		map.put("type",type);
		return map;
	}
	
	public String getJe(String sl,String price,String lv){
		RecordSet rs = new RecordSet();
		String je="0";
		String sql="select round(nvl('"+sl+"',0)*nvl('"+price+"',0)*nvl('"+lv+"',0),2) as je from dual";
		rs.executeSql(sql);
		if(rs.next()){
			je=Util.null2String(rs.getString("je"));
		}
		return je;
	}
	
	public String getmult(String value,String hl){
		RecordSet rs = new RecordSet();
		String mutvalue1 = value;
		String mutvalue2 = hl;
		String result = "";
		if("".equals(mutvalue1)){
			mutvalue1 = "0";
		}
		if("".equals(mutvalue2)){
			mutvalue2 = "0";
		}
		String sql = "select round("+mutvalue1+"*"+mutvalue2+",2) as result from dual";
		rs.executeSql(sql);
		if(rs.next()){
			result = Util.null2String(rs.getString("result"));
		}
		return result;
	}
	private String getJsonStr(Map<String, String> map) {
		JSONObject json = new JSONObject();
		Iterator<String> it = map.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			String value = map.get(key);
			try {
				json.put(key, value);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json.toString();
	}
}
