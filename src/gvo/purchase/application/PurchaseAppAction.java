package gvo.purchase.application;

import gvo.purchase.application.SAPPR_MM_0_CreatePRService_pttBindingQSServiceStub.Response;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ibm.db2.jcc.am.p;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class PurchaseAppAction implements Action {
	/**
	 * oa与sap采购申请流程
	 * 
	 * @author daisy
	 * @version 2.0 2018-02-26
	 * 
	 **/
	BaseBean log = new BaseBean();
	public String execute(RequestInfo info) {
		log.writeLog("进入采购申请流程PurchaseAppAction——————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "";
		String tableName = "";
		String tableNamedt = "";// 明细表
		String mainID = "";// 主表id,关联明细表
		String PQTYPE = "";// 采购申请类型
		String sapstatus = "";
		String pqNo = "";
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		if (!"".equals(tableName)) {
			tableNamedt = tableName + "_dt1";
			// 查询主表
			sql = "select * from " + tableName + " where requestid="+ requestid;
			rs.execute(sql);
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("ID"));
				PQTYPE = Util.null2String(rs.getString("pqtype"));
				sapstatus = Util.null2String(rs.getString("sapstatus"));
				pqNo = Util.null2String(rs.getString("pqNo"));
			}
			if("S".equals(sapstatus)&&!"".equals(pqNo)){
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
				String PROJECT = Util.null2String(rs.getString("project"));// 项目
				String CLASSIFYCODE = Util.null2String(rs.getString("classifycode"));// 科目分类码
				String MATERIALNO = Util.null2String(rs.getString("MaterialNo"));// 物料编号
				String MATERIALDESC = Util.null2String(rs.getString("MaterialDesc"));// 物料描述
				String PURCHASENUM = Util.null2String(rs.getString("purchaseNum"));// 采购数量
				String UNIT = Util.null2String(rs.getString("unit"));// 采购单位
				String OUTDATE = Util.null2String(rs.getString("outdate"));// 交货日期
				String MAINCPTNO = Util.null2String(rs.getString("maincptno"));// 主资产号
				String COSTCENTER = Util.null2String(rs.getString("costcenter"));// 成本中心
				String EXPACCOUNT = Util.null2String(rs.getString("expaccount"));// 费用科目
				String PRICE = Util.null2String(rs.getString("price"));// 预估单价
				String CURRENCY = Util.null2String(rs.getString("currency"));// 币种
				String PRICEUNIT = Util.null2String(rs.getString("priceunit"));// 价格单位
				String GLANT1 = Util.null2String(rs.getString("glant1"));// 工厂
				String LOCATION = Util.null2String(rs.getString("location"));// 库存地点
				String PURGROUP = Util.null2String(rs.getString("purGroup"));// 采购组
				String MATERIALGRO = Util.null2String(rs.getString("Materialgro"));// 物料组
				String APPNAME = Util.null2String(rs.getString("appname"));// 申请人姓名
				String REMARK = Util.null2String(rs.getString("remark"));// 采购备注
				String INORDERNO = Util.null2String(rs.getString("inorderNO"));// 内部订单号
				String KEEPERNAME = Util.null2String(rs.getString("keepername"));// 保管人姓名
				String KEEPDEPTNAME = Util.null2String(rs.getString("keepdeptname"));// 保管部门名称
				String DEPTNAME = Util.null2String(rs.getString("deptname"));// 申请部门名称
				if (MATERIALDESC.length()>=40){
					MATERIALDESC = MATERIALDESC.substring(0,40);
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
					jsonObjSon.put("COSTCENTER", COSTCENTER);
					jsonObjSon.put("EXPACCOUNT", EXPACCOUNT);
					jsonObjSon.put("PRICE", PRICE);
					jsonObjSon.put("CURRENCY", CURRENCY);
					jsonObjSon.put("PRICEUNIT", PRICEUNIT);
					jsonObjSon.put("GLANT1", GLANT1);
					jsonObjSon.put("LOCATION", LOCATION);
					jsonObjSon.put("PURGROUP", PURGROUP);
					jsonObjSon.put("MATERIALGRO", MATERIALGRO);
					jsonObjSon.put("APPNAME", APPNAME);
					jsonObjSon.put("REMARK", REMARK);
					jsonObjSon.put("INORDERNO", INORDERNO);
					jsonObjSon.put("KEEPERNAME", KEEPERNAME);
					jsonObjSon.put("KEEPDEPTNAME", KEEPDEPTNAME);
					jsonObjSon.put("DEPTNAME", DEPTNAME);
					jsonArray.put(jsonObjSon);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("PQTYPE", PQTYPE);
				head.put("CHILD_CreatePR_SAP_1_LIST", jsonArray);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			jsonArr.put(head);
			PurXmlUtil tran = new PurXmlUtil();
			String json = tran.javaToXml(jsonArr.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			PurchaseWebservice pur = new PurchaseWebservice();
			String sign = "";
			String MESS = ""; 
			try {
				Response result = pur.getResultMethod(json);
				sign = result.getSIGN();
				MESS = result.getMessage(); 
				log.writeLog("sign = " + sign);
				log.writeLog("MESS = " + MESS);
			} catch (Exception e) {
				
				e.printStackTrace();
			}
			
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "E_MSGTX";
			String para1 = "E_BANFN";
			String banfn = saxXmlUtil.getResult(para1, MESS);
			String message = saxXmlUtil.getResult(para, MESS);
			log.writeLog("文本消息————————" + message);
			if(!"S".equals(sign)){
				banfn="";
			}
			String sql_update = "update " + tableName + " set sapstatus='" + sign + "',sapcontext='" + message + "',pqNo='" + banfn + "' where requestid=" + requestid;
			res.execute(sql_update);
		} else {
			return "-1";
		}
		return SUCCESS;
	}
	
}
