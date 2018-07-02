package gvo.costcontrol.purchase.application;

import gvo.purchase.application.PurXmlUtil;
import gvo.purchase.application.PurchaseWebservice;
import gvo.purchase.application.SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.Response;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 固定资产类采购申请调用esb接口创建采购申请单号
 * 
 * @author tangj
 * 
 */
public class PurchaseAppWebserviceActionGDZC implements Action {

	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("Start PurchaseAppActionFY——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String sql = "";
		String tableName = "";
		String tableNamedt = "";// 明细表
		String mainID = "";// 主表id,关联明细表
		String fhzt = "";
		String cgsqdh = "";

		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}

		tableNamedt = tableName + "_dt1";
		// 查询主表
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("ID"));
			cgsqdh = Util.null2String(rs.getString("cgsqdh"));
			fhzt = Util.null2String(rs.getString("fhzt"));
		}
		if("S".equals(fhzt)&&!"".equals(cgsqdh)){
			return SUCCESS;
		}

		// 查询明细表
		JSONArray jsonArray = new JSONArray();
		JSONObject head = new JSONObject();
		JSONArray jsonArr = new JSONArray();
		sql = "select * from " + tableNamedt + " where mainid=" + mainID;
		rs.execute(sql);
		while (rs.next()) {
			JSONObject jsonObjSon = new JSONObject();
			String PROJECT = Util.null2String(rs.getString("xm"));// 项目
			String CLASSIFYCODE = Util.null2String(rs.getString("kmflm"));// 科目分类码
			String MATERIALNO = Util.null2String(rs.getString("wlh"));// 物料编号
			String MATERIALDESC = Util
					.null2String(rs.getString("wlms"));// 物料描述
			String PURCHASENUM = Util.null2String(rs.getString("sl"));// 采购数量
			String UNIT = Util.null2String(rs.getString("sldw"));// 采购单位
			String OUTDATE = Util.null2String(rs.getString("jhrq"));// 交货日期
			String MAINCPTNO = Util.null2String(rs.getString("zcch"));// 主资产号
			//String COSTCENTER = Util.null2String(rs.getString("cbzx"));// 成本中心
			//String EXPACCOUNT = Util.null2String(rs.getString("fylmbm"));// 费用科目
			String PRICE = Util.null2String(rs.getString("dj"));// 预估单价
			String CURRENCY = Util.null2String(rs.getString("bb"));// 币种
			String PRICEUNIT = Util.null2String(rs.getString("jgdw"));// 价格单位
			String GLANT1 = Util.null2String(rs.getString("gc"));// 工厂
			String LOCATION = Util.null2String(rs.getString("kcdd"));// 库存地点
			String PURGROUP = Util.null2String(rs.getString("cgz"));// 采购组
			String MATERIALGRO = Util.null2String(rs.getString("wlz"));// 物料组
			String APPNAME = Util.null2String(rs.getString("sqrxm"));// 申请人姓名
			//String REMARK = Util.null2String(rs.getString("bz"));// 采购备注
			String INORDERNO = Util.null2String(rs.getString("NBDDH"));// 内部订单号
			//String KEEPERNAME = Util.null2String(rs.getString("jhf"));// 保管人姓名
			//String KEEPDEPTNAME = Util
			//		.null2String(rs.getString("shd"));// 保管部门名称
			String DEPTNAME = Util.null2String(rs.getString("cgzz"));// 申请部门名称
			if (MATERIALDESC.length() >= 40) {
				MATERIALDESC = MATERIALDESC.substring(0, 40);
			}
			try {
				jsonObjSon.put("CLASSIFYCODE", CLASSIFYCODE);
				jsonObjSon.put("PROJECT", PROJECT);
				jsonObjSon.put("MATERIALNO", MATERIALNO);
				jsonObjSon.put("MATERIALDESC", MATERIALDESC);
				jsonObjSon.put("PURCHASENUM", PURCHASENUM);
				jsonObjSon.put("UNIT", UNIT);
				jsonObjSon.put("OUTDATE", OUTDATE);
				jsonObjSon.put("MAINCPTNO", MAINCPTNO);
				jsonObjSon.put("COSTCENTER", "");
				jsonObjSon.put("EXPACCOUNT", "");
				jsonObjSon.put("PRICE", PRICE);
				jsonObjSon.put("CURRENCY", CURRENCY);
				jsonObjSon.put("PRICEUNIT", PRICEUNIT);
				jsonObjSon.put("GLANT1", GLANT1);
				jsonObjSon.put("LOCATION", LOCATION);
				jsonObjSon.put("PURGROUP", PURGROUP);
				jsonObjSon.put("MATERIALGRO", MATERIALGRO);
				jsonObjSon.put("APPNAME", APPNAME);
				jsonObjSon.put("REMARK", "");
				jsonObjSon.put("INORDERNO", INORDERNO);
				jsonObjSon.put("KEEPERNAME", "");
				jsonObjSon.put("KEEPDEPTNAME", "");
				jsonObjSon.put("DEPTNAME", new DepartmentComInfo().getDepartmentname(DEPTNAME));
				jsonArray.put(jsonObjSon);
			} catch (Exception e) {
				log.writeLog(e);
				log.writeLog("json拼装失败");
			}
		}
		try {
			head.put("PQTYPE", "PR02");
			head.put("CHILD_CreatePR_SAP_1_LIST", jsonArray);
		} catch (JSONException e) {
			log.writeLog(e);
		}
		jsonArr.put(head);
		PurXmlUtil tran = new PurXmlUtil();
		String json = tran.javaToXml(jsonArr.toString(), "", requestid, "");
		log.writeLog("打印json————————" + json);
		PurchaseWebservice pur = new PurchaseWebservice();
		String sign = "";
		String MESS = "";
		Response result = null;
		try {
			result = pur.getResultMethod(json);
			
		} catch (Exception e) {
			log.writeLog(e);
			log.writeLog("接口调用失败");
		}
		sign = result.getSIGN();
		MESS = result.getMessage();
		log.writeLog("sign = " + sign);
		log.writeLog("MESS = " + MESS);
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		String E_MSGTX = saxXmlUtil.getResult("E_MSGTX", MESS);
		String E_BANFN = saxXmlUtil.getResult("E_BANFN", MESS);
		if(!"S".equals(sign)){
			E_BANFN="";
		}
		sql = "update " + tableName + " set fhzt='" + sign
				+ "',fhxx='" + E_MSGTX + "',cgsqdh='" + E_BANFN
				+ "' where requestid=" + requestid;
		rs.execute(sql);
		sql="update uf_pr_budget set cgsqdh='"+E_BANFN+"' where lcid='"+requestid+"'";
		rs.executeSql(sql);

		return SUCCESS;
	}
	
}
