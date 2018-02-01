package htkjjt.pm.webService.notice;

import htkjjt.pm.peek.notice.AfterMaintenanceMBean;
import htkjjt.pm.util.InsertUtil;
import htkjjt.pm.util.ReturnInfo1;
import htkjjt.pm.util.ReturnInfo2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class AfterMaintenance {
	BaseBean log=new BaseBean(); 
	public ReturnInfo1 sysDataInf(AfterMaintenanceMBean[] amList){
		RecordSet rs= new RecordSet();
		RecordSet res= new RecordSet();
		ReturnInfo1 ri = new ReturnInfo1();
		InsertUtil iu = new InsertUtil();
		int seqno = 0;
		String sql="";
		String tablename = "uf_nowday_remind";
		List<ReturnInfo2> InfoList=new ArrayList<ReturnInfo2>();
		boolean flag=true;
		if(amList == null || amList.length < 1){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递不能为空 {Data transfer cannot be empty} ");
			return ri;
		}
		if(amList.length > 1000){
			ri.setIsSuccess("E");
			ri.setMessage("数据传递超过指定大小(1000) {Data transfer exceeds the specified size (1000)} ");
			return ri;
		}
		for(AfterMaintenanceMBean am : amList){
			ReturnInfo2 rf = new ReturnInfo2(); 
			Map<String,String> mapStr  = new HashMap<String, String>();
			mapStr.put("aufnr", Util.null2String(am.getAufnr()));//订单编号
			mapStr.put("wawrk", Util.null2String(am.getWawrk()));//工厂
			mapStr.put("vaplz", Util.null2String(am.getVaplz()));//维护班组
			mapStr.put("werks", Util.null2String(am.getWerks()));//维护班组编号  
			mapStr.put("pak_text", Util.null2String(am.getPak_text()));//维护周期文本
			mapStr.put("ingpr", Util.null2String(am.getIngpr()));///计划员组
			mapStr.put("equnr", Util.null2String(am.getEqunr()));//设备编号
			mapStr.put("eqfnr", Util.null2String(am.getEqfnr()));//公司编码
			mapStr.put("msgrp", Util.null2String(am.getMsgrp()));//存放位置
			mapStr.put("typbz", Util.null2String(am.getTypbz()));//型号
			mapStr.put("eqktx", Util.null2String(am.getEqktx()));///设备描述
			mapStr.put("ktext", Util.null2String(am.getKtext()));//工作中心描述
			mapStr.put("innam", Util.null2String(am.getInnam()));//计划员组描述
			mapStr.put("gstrp", Util.null2String(am.getGstrp())); //计划日期
			mapStr.put("sfcfwc", "1");
			if(seqno <=0){
				sql="select nvl(max(seqno),0)+1 as seqno from "+tablename;
				rs.executeSql(sql);
				if(rs.next()){
					seqno = rs.getInt("seqno");
				}
			}
			mapStr.put("seqno", String.valueOf(seqno));
			iu.insert(mapStr, tablename);
			log.writeLog("am.getAufnr());//-----"+Util.null2String(am.getAufnr()));
			
			rf.setIsSuccess("S");
			rf.setMessage("数据写入成功");
			
			log.writeLog("订单编号为"+ Util.null2String(am.getWerks())+"的数据是否更新成功------"+ri.getMessage());
			InfoList.add(rf);
		}
		CreateService cs = new CreateService();
		
		cs.doServerForAfter(seqno);
		
		//doService(String seqno)
		ri.setInfoList2(InfoList);
		return ri;
		}
	
	
	
//	public static void main(String[] args) {
//		AfterMaintenance aa=new AfterMaintenance();
//		List<AfterMaintenanceMBean> amList =new ArrayList<AfterMaintenanceMBean>();
//		AfterMaintenanceMBean ab= new AfterMaintenanceMBean();
//		ReturnInfo1 bb=new ReturnInfo1();
//		amList.add(ab)
//		bb=aa.sysDataInf(amList);
//	}
	
	
	
	
	}
		
