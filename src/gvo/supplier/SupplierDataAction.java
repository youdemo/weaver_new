package gvo.supplier;

import gvo.supplier.SAPPR_MM_0_SupplierDataCreate_pttBindingQSServiceStub.Response;
import gvo.util.xml.SaxXmlUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.Map;

public class SupplierDataAction extends BaseBean implements Action {
	/**
	 * oa与sap供应商主数据新增，删除接口
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-22
	 **/
	BaseBean log = new BaseBean();

	public Response getResultMethod(String json) throws Exception {
		SAPPR_MM_0_SupplierDataCreate_pttBindingQSServiceStub crs = new SAPPR_MM_0_SupplierDataCreate_pttBindingQSServiceStub();
		SAPPR_MM_0_SupplierDataCreate_pttBindingQSServiceStub.SAPPR_MM_0_SupplierDataCreate cres = new SAPPR_MM_0_SupplierDataCreate_pttBindingQSServiceStub.SAPPR_MM_0_SupplierDataCreate();
		cres.setData(json);
		Response result = crs.SAPPR_MM_0_SupplierDataCreate(cres);
		return result;

	}

	public String execute(RequestInfo info) {
		log.writeLog("进入供应商主数据创建 SupplierDataAction——————");
		String workflowID = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String requestid = info.getRequestid();// 流程请求ID
		String tableName = info.getRequestManager().getBillTableName();// 表单名称
		String tableNamedt = "";// 明细表名
		String mainID = "";// 主表id
		String KTOKK = "";// 供应商帐户组
		String SEARCH = "";// 搜索项
		String SPRAS = "";// 语言代码
		String ZTERM = "";// 付款条件代码（采购视图）
		String DELIVERYCODE = "";// 送货方代码
		String DELETESUPLER = "";// 删除所选采购组织数据
		String ADDRESS1 = "";// 地址
		String STCEG = "";// 增值税登记号
		String WEBRE = "";// 标识：基于收货的发票验证
		String RONGCHA = "";// 容差组
		String DELETFREEZEDATA = "";// 删除冻结常规数据
		String COUNTRYCODE = "";// 国家代码
		String NAME1 = "";// 名称
		String TITLE = "";// 标题
		String MONEYTYPE = "";// 采购订单货币
		String FREEZEDATA = "";// 过账冻结所有公司代码
		String LIFNPI = "";// 发票方
		String DELFRECOMPANY = "";// 删除冻结公司代码数据
		String SUPPLIERCODE = "";// 供应商代码
		String CITY = "";// 城市
		String TOTALACOUNTS = "";// 总帐中的统驭科目
		String FREEZECHOSE = "";// 过账冻结所选公司代码
		String TELF1 = "";// 第一个电话号
		String COMPCODE = "";// 公司代码
		String PSTLZ = "";// 邮政编码
		String CREATETIME = "";// 创建时间
		String PAYCODE = "";// 付款条件代码（财务视图）
		String SEALER = "";// 销售员
		String FREEZEALLBUYER = "";// 冻结所有采购组织
		String EMAIL = "";// E-MAIL
		String DELETEALL = "";// 删除所有公司数据
		String BUYER = "";// 采购组织
		String REGION = "";// 地区（省/自治区/直辖市、市、县）

		if (!"".equals(tableName)) {
			tableNamedt = tableName + "_dt1";

			JSONObject head = new JSONObject();
			JSONArray Arr = new JSONArray();
			JSONObject json1 = new JSONObject();
			JSONArray Array1 = new JSONArray();
			// 查询主表
			String sql = "select * from " + tableName + " where requestid= " + requestid;
			log.writeLog("错误信息————————" + sql);
			rs.execute(sql);
			if (rs.next()) {
				mainID = Util.null2String(rs.getString("ID"));
				KTOKK = Util.null2String(rs.getString("ktokk"));
				SEARCH = Util.null2String(rs.getString("search"));
				SPRAS = Util.null2String(rs.getString("spras"));
				ZTERM = Util.null2String(rs.getString("zterm"));
				DELIVERYCODE = Util.null2String(rs.getString("deliveryCode"));
				DELETESUPLER = Util.null2String(rs.getString("deleteSupler"));
				ADDRESS1 = Util.null2String(rs.getString("address1"));
				STCEG = Util.null2String(rs.getString("stceg"));
				WEBRE = Util.null2String(rs.getString("webre"));
				RONGCHA = Util.null2String(rs.getString("rongcha"));
				DELETFREEZEDATA = Util.null2String(rs.getString("deletFreezeData"));
				COUNTRYCODE = Util.null2String(rs.getString("countryCode"));
				NAME1 = Util.null2String(rs.getString("name1"));
				TITLE = Util.null2String(rs.getString("title"));
				MONEYTYPE = Util.null2String(rs.getString("moneyType"));
				FREEZEDATA = Util.null2String(rs.getString("freezeData"));
				LIFNPI = Util.null2String(rs.getString("lifnpi"));
				DELFRECOMPANY = Util.null2String(rs.getString("delFreCompany"));
				SUPPLIERCODE = Util.null2String(rs.getString("supplierCode"));
				CITY = Util.null2String(rs.getString("city"));
				TOTALACOUNTS = Util.null2String(rs.getString("totalAcounts"));
				FREEZECHOSE = Util.null2String(rs.getString("freezeChose"));
				TELF1 = Util.null2String(rs.getString("telf1"));
				COMPCODE = Util.null2String(rs.getString("compCode"));
				PSTLZ = Util.null2String(rs.getString("pstlz"));
				CREATETIME = Util.null2String(rs.getString("appDate"));
				PAYCODE = Util.null2String(rs.getString("payCode"));
				SEALER = Util.null2String(rs.getString("sealer"));
				FREEZEALLBUYER = Util.null2String(rs.getString("freezeAllBuyer"));
				EMAIL = Util.null2String(rs.getString("email"));
				DELETEALL = Util.null2String(rs.getString("deleteAll"));
				BUYER = Util.null2String(rs.getString("buyer"));
				REGION = Util.null2String(rs.getString("region"));
			}
			
			JSONArray Array = new JSONArray();
			// 查询明细表
			sql = " select * from " + tableNamedt + " where mainid = " + mainID;
			log.writeLog("错误信息————————" + sql);
			res.execute(sql);
			while (res.next()) {
				JSONObject jsonobj = new JSONObject();
				String CONBANKCODE = Util.null2String(res.getString("conBankCode"));// 银行国家代码
				String BANKCODE = Util.null2String(res.getString("bankCode"));// 银行代码
				String BANKCOUNT = Util.null2String(res.getString("bankCount"));// 银行账户
				String BANK = Util.null2String(res.getString("bank"));// 银行户主
				try {
					jsonobj.put("CONBANKCODE", CONBANKCODE);
					jsonobj.put("BANKCODE", BANKCODE);
					jsonobj.put("BANKCOUNT", BANKCOUNT);
					jsonobj.put("BANK", BANK);
					Array.put(jsonobj);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				json1.put("KTOKK", KTOKK);
				json1.put("SEARCH", SEARCH);
				json1.put("SPRAS", SPRAS);
				json1.put("ZTERM", ZTERM);
				json1.put("DELIVERYCODE", DELIVERYCODE);
				json1.put("DELETESUPLER", DELETESUPLER);
				json1.put("ADDRESS1", ADDRESS1);
				json1.put("STCEG", STCEG);
				json1.put("WEBRE", WEBRE);
				json1.put("RONGCHA", RONGCHA);
				json1.put("DELETFREEZEDATA", DELETFREEZEDATA);
				json1.put("COUNTRYCODE", COUNTRYCODE);
				json1.put("NAME1", NAME1);
				json1.put("TITLE", TITLE);
				json1.put("MONEYTYPE", MONEYTYPE);
				json1.put("FREEZEDATA", FREEZEDATA);
				json1.put("LIFNPI", LIFNPI);
				json1.put("DELFRECOMPANY", DELFRECOMPANY);
				json1.put("SUPPLIERCODE", SUPPLIERCODE);
				json1.put("CITY", CITY);
				json1.put("TOTALACOUNTS", TOTALACOUNTS);
				json1.put("FREEZECHOSE", FREEZECHOSE);
				json1.put("TELF1", TELF1);
				json1.put("COMPCODE", COMPCODE);
				json1.put("PSTLZ", PSTLZ);
				json1.put("CREATETIME", CREATETIME);
				json1.put("PAYCODE", PAYCODE);
				json1.put("SEALER", SEALER);
				json1.put("FREEZEALLBUYER", FREEZEALLBUYER);
				json1.put("EMAIL", EMAIL);
				json1.put("DELETEALL", DELETEALL);
				json1.put("BUYER", BUYER);
				json1.put("REGION", REGION);
				Array1.put(json1);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			try {
				head.put("CHILD_GYSZSJXKXD_SAP_001_LIST", Array);
				head.put("GYSZSJXKXD_SAP_001", Array1);
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
			Arr.put(head);
			SupplyXmlUtil tran = new SupplyXmlUtil();
			String json = tran.javaToXml(Arr.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			SupplierDataAction sda = new SupplierDataAction();
			String SIGN = "";
			String MESSAGE = "";
			try {
				Response result = sda.getResultMethod(json);
				SIGN = result.getSIGN();
				MESSAGE = result.getMessage();
				log.writeLog("返回结果————————" + SIGN);
				log.writeLog("返回结果————————" + MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "E_MESS";
			String para1 = "E_LIFNR";
			String para2 = "TYPE";
			String para3 = "MESSAGE";
			Map<String, Object> result = saxXmlUtil.getXmlMap(MESSAGE);
			Object sovleMess = result.get(para);
			Object supplierCode = result.get(para1);
			Object sign = result.get(para2);
			Object message = result.get(para3);
			String sql_update = "update " + tableName + " set sovleStuts='" + SIGN + "',sovleMess='" + sovleMess + "',supplierCode='"
					+ supplierCode + "' where requestid=" + requestid;
			rs.execute(sql_update);
			String sql_update_dt = "update " + tableNamedt + " set message	='" + sign + "',ruult='" + message + "' where mainid = "
					+ mainID;
			res.execute(sql_update_dt);
			log.writeLog("更新主表————————" + sql_update);
			log.writeLog("更新明细表————————" + sql_update_dt);

		} else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
}
