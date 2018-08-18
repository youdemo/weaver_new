package gvo.webservice;

import java.util.Iterator;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import weaver.general.BaseBean;

public class AttendanceIntegrateWebservice {
	/**
	 * 登陆验证
	 * @param loginid 登陆名
	 * @param password 密码
	 * @return 返回值 {"result":"S","workcode":"工号","name":"姓名","checkno":"123"}
	 *  result 校验结果S成功 E失败  workcode工号 name 姓名checkno 身份验证码
	 */
	public String logincheck(String loginid,String password){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice logincheck loginid: "+loginid+" |password: "+password);
		String result =adii.logincheck(loginid, password);
		log.writeLog("AttendanceIntegrateWebservice logincheck result: "+result);
		return result;
	}
	
	/**
	 * 获取选择框选项
	 * @param type 选择框类型
	 * @return {"result":"S","items":[{"value":"年假","key":"0"},{"value":"调休","key":"1"}]}
	 *   返回值说明  result 结果 S成功获取数据 E失败 items json数组 key 选择框值  value 显示值
	 */
//	public String getSelectItems(String type){
//		String result = "{\"result\":\"S\",\"items\":[{\"value\":\"年假\",\"key\":\"0\"},{\"value\":\"调休\",\"key\":\"1\"}]}";
//		return result;
//	}
	/**
	 * 根据人员姓名获取信息列表姓名
	 * @param name 完整人名
	 * @return {"context":"","result":"S","items":[{"workcode":"2","jobtitle":"IT岗位","department":"信息部门","name":"张三"},{"workcode":"1","jobtitle":"专员岗位","department":"人事部门","name":"李四"}]}
	  *   返回值说明  result 结果 S成功获取数据 E失败  context 说明 items json数组 "workcode 人员工号 name 姓名  jobtitle 岗位 department部门
	 */
	public String getPersonList(String name){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice getPersonList name: "+name);
		String result="";
		try {
			result = adii.getPersonListByName(name);
		} catch (Exception e) {
			log.writeLog(e);
			result="{\"result\":\"E\",\"items\":[],\"context\":\"json异常\"}";
		}
		log.writeLog("AttendanceIntegrateWebservice getPersonList result: "+result);
		return result;
	}
	/**
	 * 获取请假流程在途时数
	 * @param workcode 工号
	 * @param holidayType 假期类型
	 * @param date 请假日期
	 * @param checkno 身份校验码
	 * @return 假期余额小时数  {"result":"S","time":"16.5","context":"说明"}
	 *  返回值说明 result 结果 S成功获取数据 E失败 time 在途时数 context 说明
	 */
	public String getInProcessHours(String workcode,String holidayType,String date,String checkno){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice getInProcessHours workcode: "+workcode+" holidayType:"+holidayType+" date:"+date+" checkno:"+checkno);
		String result="";
		result = adii.getInProcessHours(workcode, holidayType, date, checkno);		
		log.writeLog("AttendanceIntegrateWebservice getPersonList result: "+result);
		return result;
	}
	
	/**
	 * 根据传入类型，创建不同流程
	 * @param workflowType 创建流程类型 请假 销假。。
	 * @param workcode 流程创建人工号
	 * @param dataInfo  单据json串格式 
	 * @param checkno 身份校验码
	 * @return {"OA_ID":"123","MSG_CONTENT":"","MSG_TYPE":"S"}  
	 * OA_ID生成的流程id MSG_CONTENT创建失败是的错误信息 MSG_TYPE 创建结果 S成功，E失败
	 */
	public String createRequestInfo(String workflowType,String workcode,String dataInfo,String checkno){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice createRequestInfo workflowType: "+workflowType+" workcode:"+workcode+" dataInfo:"+dataInfo+" checkno:"+checkno);
		String result="";
		result = adii.createRequestInfo(workflowType, workcode, dataInfo, checkno);
		
		log.writeLog("AttendanceIntegrateWebservice createRequestInfo result: "+result);
		return result;
	}
	/**
	 * 根据流程类型和工号，返回该员工的申请记录
	 * @param workflowType 流程类型
	 * @param workcode 工号
	 * @param num 次数 根据次数每次返回10条   1 返回 1-10条 2返回 11-20条。。
	 * @param checkno 身份校验码
	 * @return {"context":"说明","result":"S","items":[]}
	 * 返回值说明： result 结果 S成功获取数据 E失败 context 说明 items json数组 
	 */
	public String getOldRequestInfo(String workflowType,String workcode,int num,String checkno){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice getOldRequestInfo workflowType: "+workflowType+" workcode:"+workcode+" num:"+num+" checkno:"+checkno);
		String result="";
		try {
			result = adii.getOldRequestInfo(workflowType, workcode, num, checkno);
		} catch (Exception e) {
			log.writeLog(e);
			result="{\"result\":\"E\",\"items\":[],\"context\":\"json异常\"}";
		}
		
		log.writeLog("AttendanceIntegrateWebservice getOldRequestInfo result: "+result);
		return result;
	}
	/**
	 * 根据工号 获取下级姓名工号
	 * @param workcode 工号
	 * @return {"context":"说明","result":"S","items":[{"workcode":"工号","name":"姓名"},{"workcode":"工号1","name":"姓名2"}]}
	 * 返回值说明： result 结果 S成功获取数据 E失败 items json数组 workcode 工号,name 姓名  context:说明
	 */
	public String getLowerLevelPerson(String workcode){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice getLowerLevelPerson workcode: "+workcode);
		String result="";
		try {
			result = adii.getLowerLevelPerson(workcode);
		} catch (Exception e) {
			log.writeLog(e);
			result="{\"result\":\"E\",\"items\":[],\"context\":\"json异常\"}";
		}
		log.writeLog("AttendanceIntegrateWebservice getLowerLevelPerson result: "+result);
		return result;
	}
	
	/**
	 * 获取当月加班时数
	 * @param date 加班归属日期
	 * @param workcode 工号
	 * @param checkno 身份验证码
	 * @return {"result":"S","time":"16.5","context":"说明"}
	 * 返回值说明 result 结果 S成功获取数据 E失败 time 当月加班数 context 说明
	 */
	public String getOvertimeHours(String date,String workcode,String checkno ){
		BaseBean log = new BaseBean();
		AttendanceIntegrateImpl adii = new AttendanceIntegrateImpl();
		log.writeLog("AttendanceIntegrateWebservice getOvertimeHours date: "+date+" workcode:"+workcode+" checkno:"+checkno);
		String result = "";
		result = adii.getOvertimeHours(date, workcode, checkno);
		log.writeLog("AttendanceIntegrateWebservice getOvertimeHours result: "+result);
		return result;
	}
	
}
