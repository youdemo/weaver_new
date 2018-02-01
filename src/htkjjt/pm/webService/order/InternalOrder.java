package htkjjt.pm.webService.order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import htkjjt.pm.peek.order.InternalOrderDBean;
import htkjjt.pm.peek.order.InternalOrderMBean; 
import htkjjt.pm.util.ReturnInfo1;
import htkjjt.pm.util.ReturnInfo2;
import htkjjt.webservice.util.TmcUtil;

public class InternalOrder {
	BaseBean log=new BaseBean();
	public ReturnInfo1 sysDataInf(InternalOrderMBean[] ioList){
		RecordSet rs= new RecordSet();
		RecordSet res= new RecordSet();
		ReturnInfo1 ri = new ReturnInfo1();
		List<ReturnInfo2> InfoList=new ArrayList<ReturnInfo2>();
		boolean flag=true;
		if(ioList == null || ioList.length < 1){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递不能为空 {Data transfer cannot be empty} ");
			return ri;
		}
		if(ioList.length > 1000){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递超过指定大小(1000) {Data transfer exceeds the specified size (1000)} ");
			return ri;
		}
		TmcUtil tu = new TmcUtil();//
		for(InternalOrderMBean ior : ioList){
			Map<String,String> mapStr  = new HashMap<String, String>();
			Map<String,String> whereMap = new HashMap<String, String>();
			ReturnInfo2 rf = new ReturnInfo2();
			mapStr.put("aufnr", Util.null2String(ior.getAufnr()));
			mapStr.put("ktext",Util.null2String( ior.getKtext()));
			mapStr.put("erdat",Util.null2String( ior.getErdat()));
			mapStr.put("wawrk",Util.null2String( ior.getWawrk()));
			mapStr.put("vaplz",Util.null2String( ior.getVaplz()));
			mapStr.put("gstrp",Util.null2String( ior.getGstrp()));
			mapStr.put("gltrp",Util.null2String( ior.getGltrp()));
			mapStr.put("kbtext",Util.null2String( ior.getKbtext()));
			mapStr.put("eqfnr",Util.null2String( ior.getEqfnr()));
			mapStr.put("innam",Util.null2String( ior.getInnam()));
			mapStr.put("sname",Util.null2String( ior.getSname()));
			mapStr.put("workcode",Util.null2String( ior.getParnr()));
			log.writeLog("ior.getAufnr()---------"+Util.null2String(ior.getAufnr()));
			List<InternalOrderDBean> list=ior.getList();
			for(InternalOrderDBean iodb : list){
				mapStr.put("aufnr", Util.null2String(iodb.getAufnr()));
				mapStr.put("posnr", Util.null2String(iodb.getPosnr()));
				mapStr.put("matnr", Util.null2String(iodb.getMatnr()));
				mapStr.put("bdmng",Util.null2String( iodb.getBdmng()));
				mapStr.put("meins",Util.null2String( iodb.getMeins()));
				mapStr.put("maktx",Util.null2String( iodb.getMaktx()));
				mapStr.put("bismt",Util.null2String( iodb.getBismt()));
				mapStr.put("zzcxh",Util.null2String( iodb.getZzcxh()));
				mapStr.put("zzcgg",Util.null2String( iodb.getZzcgg()));
				mapStr.put("zyclt",Util.null2String( iodb.getZyclt()));
				mapStr.put("labst",Util.null2String( iodb.getLabst()));
				mapStr.put("verpr",Util.null2String( iodb.getVerpr()));
				mapStr.put("zj", Util.null2String(iodb.getZj()));
				mapStr.put("cgsl", Util.null2String(iodb.getCgsl()));
				mapStr.put("cgzj",Util.null2String( iodb.getCgzj()));
				mapStr.put("aufnr", Util.null2String( iodb.getAufnr()));
				log.writeLog("posnr---------"+Util.null2String(iodb.getPosnr()));
				log.writeLog("ior.getAufnr1()---------"+Util.null2String(iodb.getAufnr()));
			}
			
			
			
			
			
			rf.setIsSuccess("S");
			rf.setMessage("数据写入成功"); 
			log.writeLog("订单号为"+Util.null2String(ior.getAufnr())+"的数据是否更新成功------"+ri.getMessage());
			InfoList.add(rf);
		}
		ri.setInfoList2(InfoList);
		return ri;
	}
		
}
