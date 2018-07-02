package gvo.purchase.information;

import gvo.purchase.information.SAPPR_MM_0_UpdatePurchasingInfo_pttBindingQSServiceStub.Response;
import gvo.util.item.Data;
import gvo.util.item.Item;
import gvo.util.item.XStreamUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.util.List;

public class InfoUpdateAction extends BaseBean implements Action{
	/**
	 * oa与sap采购信息记录修改
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-23
	 * 
	 **/
	BaseBean log = new BaseBean();

	public String execute(RequestInfo info) {
		log.writeLog("进入采购信息记录修改 InfoUpdateAction——————");
		String workflowID = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		String sql = "";
		String requestid = info.getRequestid();// 流程请求ID
		String tableName = info.getRequestManager().getBillTableName();// 表单名称
		String mainID = "";//主表id
		String tableNamedt1 = "";//明细表1
		String tableNamedt2 = "";//明细表2
		String WFTYPE  = "";//流程类型
		String APPLINAME = "";// 申请人姓名
		if(!"".equals(tableName)){
			tableNamedt1 = tableName + "_dt1";
			tableNamedt2 = tableName + "_dt2";
			sql = "select * from " + tableName + " where requestid = " + requestid ;
			rs.execute(sql);
			if(rs.next()){
				mainID = Util.null2String(rs.getString("ID"));
				WFTYPE = Util.null2String(rs.getString("wftype"));
				APPLINAME = Util.null2String(rs.getString("appliname"));
			}
			JSONArray Array1 = new JSONArray();
			JSONObject head = new JSONObject();
			JSONArray Array2 = new JSONArray();
			JSONArray Array = new JSONArray();
			//查询明细表1
			sql = " select * from " + tableNamedt1 + " where mainid = " + mainID;
			rs.execute(sql);
			String OAID1 = "";
			while (rs.next()) {
				JSONObject json1 = new JSONObject();
				String CURRENY = Util.null2String(rs.getString("curreny"));// 货币代码
				String NUMERATOR = Util.null2String(rs.getString("numerator"));// 单位分子
				String GLANT = Util.null2String(rs.getString("glant"));// 工厂
				String PURGROUP = Util.null2String(rs.getString("purgroup"));// 采购组
				String PURORG = Util.null2String(rs.getString("purorg"));// 采购组织
				String DAYNUM = Util.null2String(rs.getString("daynum"));// 计划交货天数
				String RECORDTYPE = Util.null2String(rs.getString("recordtype"));// 信息记录类型
				String PRICEBASE = Util.null2String(rs.getString("pricebase"));// 价格基数
				String DENOMINATOR = Util.null2String(rs.getString("denominator"));// 单位分母
				String STARTDATE = Util.null2String(rs.getString("startdate"));// 价格生效日期始
				String SUPPLIERNO = Util.null2String(rs.getString("supplierno"));// 供应商编号
				String PRICE = Util.null2String(rs.getString("price"));// 净价
				String MATERIALNO = Util.null2String(rs.getString("materialNo"));// 物料编号
				String ENDDATE = Util.null2String(rs.getString("enddate"));// 价格生效日期终
				OAID1 = Util.null2String(rs.getString("oaid"));// OAID
				String TAXRATE = Util.null2String(rs.getString("Taxrate"));// 税率
				String UNIT = Util.null2String(rs.getString("unit"));// 订单单位
				String RECORDNO = Util.null2String(rs.getString("recordno"));// 信息记录编号
				String KSTBM2 = Util.null2String(rs.getString("KSTBM2"));// 阶梯二级数量
				String KBETR3 = Util.null2String(rs.getString("KBETR3"));// 三级单价
				String KBETR1 = Util.null2String(rs.getString("KBETR1"));// 一级单价
				String KBETR4 = Util.null2String(rs.getString("KBETR4"));// 四级单价
				String INCO2 = Util.null2String(rs.getString("INCO2"));// 国际贸易条件
				String KSTBM3 = Util.null2String(rs.getString("KSTBM3"));// 阶梯三级数量
				String KBETR2 = Util.null2String(rs.getString("KBETR2"));// 二级单价
				String ISJTPRICE = Util.null2String(rs.getString("isjtprice"));// 是否阶梯价
				String KSTBM1 = Util.null2String(rs.getString("KSTBM1"));// 阶梯一级数量
				String KSTBM4 = Util.null2String(rs.getString("KSTBM4"));// 阶梯四级数量
				String INCO1 = Util.null2String(rs.getString("INCO1"));// 国际贸易条款
				
				try {
					json1.put("CURRENY", CURRENY);
					json1.put("NUMERATOR", NUMERATOR);
					json1.put("GLANT", GLANT);
					json1.put("PURGROUP", PURGROUP);
					json1.put("PURORG", PURORG);
					json1.put("DAYNUM", DAYNUM);
					json1.put("RECORDTYPE", RECORDTYPE);
					json1.put("PRICEBASE", PRICEBASE);
					json1.put("DENOMINATOR", DENOMINATOR);
					json1.put("STARTDATE", STARTDATE);
					json1.put("SUPPLIERNO", SUPPLIERNO);
					json1.put("PRICE", PRICE);
					json1.put("MATERIALNO", MATERIALNO);
					json1.put("ENDDATE", ENDDATE);
					json1.put("OAID", OAID1);
					json1.put("TAXRATE", TAXRATE);
					json1.put("UNIT", UNIT);
					json1.put("RECORDNO", RECORDNO);
					json1.put("KSTBM2", KSTBM2);
					json1.put("KBETR3", KBETR3);
					json1.put("KBETR1", KBETR1);
					json1.put("KBETR4", KBETR4);
					json1.put("INCO2", INCO2);
					json1.put("KSTBM3", KSTBM3);
					json1.put("KBETR2", KBETR2);
					json1.put("ISJTPRICE", ISJTPRICE);
					json1.put("KSTBM1", KSTBM1);
					json1.put("KSTBM4", KSTBM4);
					json1.put("INCO1", INCO1);
					json1.put("APPLINAME", APPLINAME);
					Array1.put(json1);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			
			//查询明细表2
			sql = " select * from " + tableNamedt2 + " where mainid = " + mainID;
			res.execute(sql);
			while (res.next()) {
				JSONObject json2 = new JSONObject();
				String CURRENY = Util.null2String(res.getString("curreny"));// 币别
				String CONTYPE = Util.null2String(res.getString("contype"));// 条件类型
				String PRICEBASE = Util.null2String(res.getString("pricebase"));// 价格基数
				String PRICE = Util.null2String(res.getString("price"));// 单价
				String DELETEFLAG = Util.null2String(res.getString("pricebase"));// 删除标识
				String OAID = Util.null2String(res.getString("oaid"));// OAID
				String UNIT = Util.null2String(res.getString("unit"));// 单位
				String KSTBM2 = Util.null2String(res.getString("KSTBM2"));// 阶梯二级数量
				String KBETR3 = Util.null2String(res.getString("KBETR3"));// 三级单价
				String KBETR1 = Util.null2String(res.getString("KBETR1"));// 一级单价
				String KBETR4 = Util.null2String(res.getString("KBETR4"));// 四级单价
				String KSTBM3 = Util.null2String(res.getString("KSTBM3"));// 阶梯三级数量
				String KBETR2 = Util.null2String(res.getString("KBETR2"));// 二级单价
				String ISJTPRICE = Util.null2String(res.getString("isjtprice"));// 是否阶梯价
				String KSTBM1 = Util.null2String(res.getString("KSTBM1"));// 阶梯一级数量
				String KSTBM4 = Util.null2String(res.getString("KSTBM4"));// 阶梯四级数量
				
				try {
					json2.put("CURRENY", CURRENY);
					json2.put("CONTYPE", CONTYPE);
					json2.put("PRICEBASE", PRICEBASE);
					json2.put("PRICE", PRICE);
					json2.put("DELETEFLAG", DELETEFLAG);
					json2.put("OAID", OAID);
					json2.put("UNIT", UNIT);
					json2.put("KSTBM2", KSTBM2);
					json2.put("KBETR3", KBETR3);
					json2.put("KBETR1", KBETR1);
					json2.put("KBETR4", KBETR4);
					json2.put("KSTBM3", KSTBM3);
					json2.put("KBETR2", KBETR2);
					json2.put("ISJTPRICE", ISJTPRICE);
					json2.put("KSTBM1", KSTBM1);
					json2.put("KSTBM4", KSTBM4);
					Array2.put(json2);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			try {
				head.put("WFTYPE", WFTYPE);
				head.put("CHILD_POEINE_SAP_001_LIST", Array1);
				head.put("CHILD_POEINE_SAP_002_LIST", Array2);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			Array.put(head);
			InfoXmlUtil inf = new InfoXmlUtil();
			String json = inf.javaToXml(Array.toString(), "", requestid, "");
			log.writeLog("打印json————————" + json);
			UpdatePurchasingInfo cs = new UpdatePurchasingInfo();
			String SIGN = "";
			String MESSAGE = "";
			try {
				Response result = cs.getResultMethod(json);
				SIGN = result.getSIGN();
				MESSAGE = result.getMessage(); 
				log.writeLog("sign = " + SIGN);
				log.writeLog("MESS = " + MESSAGE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			Data data=(Data)XStreamUtils.parseData(MESSAGE);
			List<Item> itemList=data.getLIST().getITEM();
			for(Item item:itemList){
				String recordno = item.getINFNR();//信息记录编号
	        	String sapmessage = item.getMESSAGE();//消息文本
	        	String status = item.getSTATUS();//状态
	        	String oaid = item.getOAID();//OAID
	        	String sql_update = "update " + tableNamedt1 + " set recordno='" + recordno + "',sapmessage = '" + sapmessage + "',sapstatus = '" + status + "',oaid = '" + oaid + "' where mainid = " + mainID + " and oaid = " + oaid;
				res.execute(sql_update);
				log.writeLog("更新语句————————" + sql_update);
	        }
		}else {
			log.writeLog("流程表信息获取失败!");
			return "-1";
		}
		return SUCCESS;
	}
}
