package hhgd.sap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class SendQuotationToSAP {
	BaseBean log = new BaseBean();
	public String getResult(String workflowId, String detailids){
		Map<String, String> oaDatas = new HashMap<String, String>();
		List<Map<String,String>> list = getDetailData(detailids);
		log.writeLog("SendQuotationToSAP list:"+list.size());
		BringMainAndDetailByMain bmb = new BringMainAndDetailByMain("1");
		String result = bmb.getReturn(oaDatas,workflowId,"GT_ITEM",list);   
		return result;
   }
	
	public String getDetialinfo(String detailids){
		log.writeLog("SendQuotationToSAP detailids:"+detailids);
		String result=getResult("100",detailids);
		log.writeLog("SendQuotationToSAPresult:"+result);		
		return result;	
	}
	
	public List<Map<String,String>> getDetailData(String detailids){
		RecordSet rs = new RecordSet();
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		String sql="select * from uf_xsbjdd_dt1 where id in("+detailids+")";
		rs.executeSql(sql);
		while(rs.next()){
			Map<String, String> map = new HashMap<String, String>();
			map.put("NUMBER", Util.null2String(rs.getString("hxm")));//行项目
			map.put("KUNNR1", Util.null2String(rs.getString("khbm")));//客户编码
			//map.put("khmc", Util.null2String(rs.getString("khmc")));//客户名称
			map.put("MATNR", Util.null2String(rs.getString("wlbm")));//物料编码
			//map.put("wlms", Util.null2String(rs.getString("wlms")));//物料描述
			map.put("KWMENG", Util.null2String(rs.getString("xqsl")));//需求数量
			map.put("AESKD", Util.null2String(rs.getString("minddl")));//最小订单量
			//map.put("MEINS", Util.null2String(rs.getString("jbdw")));//基本单位
			map.put("KPEIN", Util.null2String(rs.getString("dj")));//单价
			map.put("KBETR", Util.null2String(rs.getString("hl")));//汇率
			map.put("ANGDT", Util.null2String(rs.getString("yxrq")));//有效起始日期
			map.put("BNDDT", Util.null2String(rs.getString("yxrqt")));//有效截止日期
			//map.put("bz", Util.null2String(rs.getString("bz")));//备注
			map.put("WERKS", Util.null2String(rs.getString("gc")));//工厂
			map.put("AUART", Util.null2String(rs.getString("bjdlx")));//报价单类型
			//map.put("xsz", Util.null2String(rs.getString("xsz")));//销售组
			map.put("VKGRP", Util.null2String(rs.getString("xzz")));//销售组织
			map.put("WAERS", Util.null2String(rs.getString("hb")));//货币
			map.put("VTWEG", Util.null2String(rs.getString("fxjd")));//分销渠道
			map.put("SPART", Util.null2String(rs.getString("fb")));//分部
			map.put("VKBUR", Util.null2String(rs.getString("xsbsc")));//销售办事处
			//map.put("fkfs", Util.null2String(rs.getString("fkfs")));//付款方式
			list.add(map);
		}
		return list;
	}
}
