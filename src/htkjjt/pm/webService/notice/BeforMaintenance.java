package htkjjt.pm.webService.notice;


import htkjjt.pm.peek.notice.BeforMaintenanceMBean; 
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

public class BeforMaintenance {
	BaseBean log=new BaseBean(); 
	public ReturnInfo1 sysDataInf(BeforMaintenanceMBean[] bmList){
//		RecordSet rs= new RecordSet();
//		RecordSet res= new RecordSet();
		ReturnInfo1 ri = new ReturnInfo1();
		List<ReturnInfo2> InfoList=new ArrayList<ReturnInfo2>();
		boolean flag=true;
		if(bmList == null || bmList.length < 1){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递不能为空 {Data transfer cannot be empty} ");
			return ri;
		}
		if(bmList.length > 1000){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递超过指定大小(1000) {Data transfer exceeds the specified size (1000)} ");
			return ri;
		}
		TmcUtil tu = new TmcUtil();
		for(BeforMaintenanceMBean bm : bmList){
			ReturnInfo2 rf = new ReturnInfo2(); 
			Map<String,String> mapStr  = new HashMap<String, String>();
			Map<String,String> whereMap = new HashMap<String, String>();
			mapStr.put("nplda", Util.null2String(bm.getNplda()));
			mapStr.put("pak_text", Util.null2String(bm.getPak_text()));
			mapStr.put("wapos",Util.null2String(bm.getWapos())); 
			mapStr.put("iwerk", Util.null2String(bm.getIwerk())); 
			mapStr.put("equnr", Util.null2String(bm.getEqunr())); 
			mapStr.put("typbz", Util.null2String(bm.getTypbz())); 
			mapStr.put("eqktx", Util.null2String(bm.getEqktx())); 
			mapStr.put("eqfnr", Util.null2String(bm.getEqfnr())); 
			mapStr.put("msgrp", Util.null2String(bm.getMsgrp())); 
			mapStr.put("gwerk", Util.null2String(bm.getGwerk())); 
			mapStr.put("ktext", Util.null2String(bm.getKtext())); 
			mapStr.put("innam", Util.null2String(bm.getInnam())); 
			
			log.writeLog("bm.getNplda()----"+Util.null2String(bm.getNplda()));
			log.writeLog("bm.getBeforMaintenanceDBean().getWapos()----"+Util.null2String(bm.getWapos()));
			rf.setIsSuccess("S");
			rf.setMessage("数据写入成功");
			log.writeLog("计划日期为"+Util.null2String(bm.getNplda())+"的数据是否更新成功------"+ri.getMessage());
			InfoList.add(rf);
		}
		ri.setInfoList2(InfoList);
		return ri;
		}
		public static void main(String[] args) {
			BeforMaintenanceMBean[] bb=new BeforMaintenanceMBean[1];
			BeforMaintenanceMBean aa = new BeforMaintenanceMBean();
			aa.setNplda("1");
			bb[0]=aa;
			BeforMaintenance q=new BeforMaintenance();
			
			ReturnInfo1 ri = new ReturnInfo1();
			ri=q.sysDataInf(bb);
			System.out.println(ri.getMessage());
		}
	}
