package zx.tmc.demo;

import zx.tmc.org.HrmDepartmentBean;
import zx.tmc.org.HrmJobTitleBean;
import zx.tmc.org.HrmOrgAction;
import zx.tmc.org.HrmResourceBean;
import zx.tmc.org.HrmSubCompanyBean;
import zx.tmc.org.ReturnInfo;

public class OrgDemo {
	
	public static void main(String[] args) {
		// 执行类  
		HrmOrgAction  hoa = new HrmOrgAction();  
		
		/**
		 	单个分部同步
		 */
		// 分部类
		HrmSubCompanyBean hsb = new HrmSubCompanyBean();
		hsb.setSubCompanyCode("003");
		hsb.setSubCompanyName("com1");
		hsb.setSubCompanyDesc("com1");
		// 上级的操作方式     0 是通过id获取  1是通过code获取
		hsb.setIdOrCode(0);
		hsb.setSuperID("");
		hsb.setSuperCode("");
		//排序字段
		hsb.setOrderBy(0);
		// 状态    0正常  1封装
		hsb.setStatus(0);
		// 自定义内容    例如:tt1、tt2是自定义的字段名     TEst1、TEst2 是自定义字段的值       HrmSubcompanyDefined记录
		hsb.addCusMap("tt1", "TEst1");
		hsb.addCusMap("tt2", "TEst2");
		// 执行结果  可以直接打印result 查看直接结果   
		ReturnInfo result = hoa.operSubCompany(hsb);
		// 
		if(result.isTure()){
			System.out.println("执行成功！警告内容："+result.getRemark());
		}else{
			System.out.println("执行失败！失败详细："+result.getRemark());
		}
		
		/**
	 		单个部门同步
		 */
		// 部门类
		HrmDepartmentBean hdb = new HrmDepartmentBean();
		hdb.setDepartmentcode("10013");
		hdb.setDepartmentname("testDept1");
		hdb.setDepartmentark("deptTest21");
		// 分部的获取操作方式     0 是通过id获取  1是通过code获取
		hdb.setComIdOrCode(1);
		hdb.setSubcompanyid1("0");
		hdb.setSubcompanyCode("001");
		// 上级的操作方式     0 是通过id获取  1是通过code获取
		hdb.setIdOrCode(1);
		hdb.setSuperID("0");
		hdb.setSuperCode("10012");
		//排序字段
		hdb.setOrderBy(0);
		// 状态    0正常  1封装
		hdb.setStatus(0);
		// 自定义内容    例如:t1、t2是自定义的字段名     123、456 是自定义字段的值       HrmDepartmentDefined记录
		hdb.addCusMap("t1", "123");
		hdb.addCusMap("t2", "456");
		// 执行结果  可以直接打印result 查看直接结果   
		result = hoa.operDept(hdb);
		if(result.isTure()){
			System.out.println("执行成功！警告内容："+result.getRemark());			
		}else{
			System.out.println("执行失败！失败详细："+result.getRemark());
		}
		
		/**
 			单个岗位同步
		 */		
		// 岗位类
		HrmJobTitleBean hjt = new HrmJobTitleBean();
		hjt.setJobtitlecode("80001020");
		hjt.setJobtitlename("软件开发经理");
		hjt.setJobtitlemark("软件开发经理");
		hjt.setJobtitleremark("软件开发经理");
		// 所属部门  0 是通过id获取  1是通过code获取
		hjt.setDeptIdOrCode(1);
		hjt.setJobdepartmentid("");
		hjt.setJobdepartmentCode("10013");
		hjt.setSuperJobCode("");
		// 职位 直接通过字段去查询，没有就添加，有就直接获取
		hjt.setJobactivityName("技术人员");
		// 职位 直接通过字段去查询，没有就添加，有就直接获取
		hjt.setJobGroupName("开发技术类");
		// 执行结果  可以直接打印result 查看直接结果   
		result = hoa.operJobtitle(hjt);
		if(result.isTure()){
			System.out.println("执行成功！警告内容："+result.getRemark());			
		}else{
			System.out.println("执行失败！失败详细："+result.getRemark());
		}
		
		/**
			单个人员同步
		 */	
		// 人员信息类
		HrmResourceBean hrb = new HrmResourceBean();
		hrb.setWorkcode("5003");
		hrb.setLoginid("5003");
		hrb.setLastname("王五三");
		hrb.setPassword("1234");
		// 所属分部   部门所对应的分部   省略
		// 所属部门  0 是通过id获取  1是通过code获取   
		hrb.setDeptIdOrCode(1);
		hrb.setDepartmentid("");
		hrb.setDepartmentCode("10012");
		// 所属岗位  0 是通过id获取  1是通过code获取     
		hrb.setJobIdOrCode(1);
		hrb.setJobtitle("");
		hrb.setJobtitleCode("80001021");
		// 上级领导  0 是通过id获取  1是通过code获取      2是通过岗位获取
		hrb.setManagerIdOrCode(1);
		hrb.setManagerid("");
		hrb.setManagerCode("5002");
		hrb.setSeclevel(10);
		
		/**
		 *  当然可以下还有 都可以设置  在HrmResourceBean中，可以set值
		 */
		
		// 执行结果  可以直接打印result 查看直接结果   
		result = hoa.operResource(hrb);
		if(result.isTure()){
			System.out.println("执行成功！警告内容："+result.getRemark());			
		}else{
			System.out.println("执行失败！失败详细："+result.getRemark());
		}
	}
}
