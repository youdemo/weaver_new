package htkjjt.pm.webService.order;


import htkjjt.pm.peek.order.ExternalOrderDBean1;
import htkjjt.pm.peek.order.ExternalOrderDBean2;
import htkjjt.pm.peek.order.ExternalOrderMBean; 
import htkjjt.pm.util.ReturnInfo1;
import htkjjt.pm.util.ReturnInfo2;

import htkjjt.webservice.util.TmcUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class ExternalOrder {
	BaseBean log=new BaseBean();
	public ReturnInfo1 sysDataInf(ExternalOrderMBean[] eoList){
		RecordSet rs= new RecordSet();
		RecordSet res= new RecordSet();
		ReturnInfo1 ri = new ReturnInfo1();
		List<ReturnInfo2> InfoList=new ArrayList<ReturnInfo2>();
		boolean flag=true;
		if(eoList == null || eoList.length < 1){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递不能为空 {Data transfer cannot be empty} ");
			return ri;
		}
		if(eoList.length > 1000){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递超过指定大小(1000) {Data transfer exceeds the specified size (1000)} ");
			return ri;
		}
		TmcUtil tu = new TmcUtil();//
		for(ExternalOrderMBean eor : eoList){
			Map<String,String> mapStr  = new HashMap<String, String>();
			Map<String,String> whereMap = new HashMap<String, String>();
			ReturnInfo2 rf = new ReturnInfo2();
			
			mapStr.put("aufnr", Util.null2String(eor.getAufnr()));
			mapStr.put("ktext", Util.null2String(eor.getKtext()));
			mapStr.put("vaplz", Util.null2String(eor.getVaplz()));
			mapStr.put("wawrk", Util.null2String(eor.getWawrk()));
			mapStr.put("kbtext", Util.null2String(eor.getKbtext()));
			mapStr.put("ingpr", Util.null2String(eor.getIngpr()));
			mapStr.put("innam", Util.null2String(eor.getInnam()));
			mapStr.put("sname", Util.null2String(eor.getSname()));
			mapStr.put("workcode", Util.null2String(eor.getErdat()));
			List< ExternalOrderDBean1> list1=eor.getList1();
			for(ExternalOrderDBean1 list : list1){
				ExternalOrderDBean1  ed=list;
				mapStr.put("aufnr", Util.null2String(ed.getAufnr()));
				mapStr.put("equnr", Util.null2String(ed.getEqunr()));
				mapStr.put("eqfnr", Util.null2String(ed.getEqfnr()));
				mapStr.put("eqktx",Util.null2String( ed.getEqktx()));
				mapStr.put("mapar",Util.null2String( ed.getMapar()));
				mapStr.put("typbz",Util.null2String( ed.getTypbz()));
				mapStr.put("ansdt", Util.null2String(ed.getAnsdt()));
				mapStr.put("inbdt", Util.null2String(ed.getInbdt()));
				mapStr.put("elief",Util.null2String(ed.getElief()));
				mapStr.put("herst",Util.null2String( ed.getHerst()));
				log.writeLog("obzae1------"+Util.null2String(ed.getObzae()));
				log.writeLog("aufnr1------"+Util.null2String(ed.getAufnr()));
			}
			List< ExternalOrderDBean2> list2=eor.getList2();
			for(ExternalOrderDBean2 list : list2){
				ExternalOrderDBean2  ed=list;
				mapStr.put("aufnr", Util.null2String(ed.getAufnr()));
				mapStr.put("vornr",Util.null2String( ed.getVornr()));
				mapStr.put("ltxa1", Util.null2String(ed.getLtxa1()));
				mapStr.put("menge", Util.null2String(ed.getMenge()));
				log.writeLog("vornr2------"+Util.null2String(ed.getVornr()));
				log.writeLog("aufnr2------"+Util.null2String(ed.getAufnr()));
			}
			log.writeLog("eor.getAufnr()------"+Util.null2String(eor.getAufnr()));
			
			
			rf.setIsSuccess("S");
			rf.setMessage("数据写入成功");
			log.writeLog("订单号为"+Util.null2String(eor.getAufnr())+"的数据是否更新成功------"+ri.getMessage());
			InfoList.add(rf);
		}
		ri.setInfoList2(InfoList);
		return ri;
	}
		
}
