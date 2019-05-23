package gvo.gvoks.kslc;

import gvo.gvoks.util.GetUtil;

import java.util.ArrayList;
import java.util.Calendar;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.system.SystemComInfo;


/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-4-4 上午9:47:12
 * 类说明
 */
public class SendEmail {
	/**
	 * 
	 * @param requestid 请求id 
	 * @param hrs 人员ids
	 * @param nodeid 节点ID
	 *  @param cqsj 超期时间
	 * @return
	 */
	public String sendEmails(String requestid,String hrs,String nodeid,String cqsj) {
		/**
		 * from ： 发送人 SystemComInfo si = new SystemComInfo(); String from =
		 * si.getDefmailfrom();
		 */
		SystemComInfo si = new SystemComInfo();
		String mailip = si.getDefmailserver();//
		String mailuser = si.getDefmailuser();
		String password = si.getDefmailpassword();
		String needauth = si.getDefneedauth();// 是否需要发件认证
		SendMail SendMail = new SendMail();
//		SendMail.setMailServer(mailip);// 邮件服务器IP
		SendMail.setMailServer("smtp.163.com");// 加邮箱服务器地址
		if (needauth.equals("1")) {
			SendMail.setNeedauthsend(true);
			SendMail.setUsername(mailuser);// 服务器的账号
			SendMail.setPassword(password);// 服务器密码
		} else {
			SendMail.setNeedauthsend(false);
		}
		String from = si.getDefmailfrom();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("进入邮件发送！----------");
		log.writeLog("requestid----------"+requestid+"----hrs----"+hrs+"---nodeid---"+nodeid+"------cqsj---"+cqsj);
		GetUtil gu = new GetUtil();
		SendMail.setMailServer(si.getDefmailserver());// 加邮箱服务器地址
		String rid = requestid;// 
		String to = gu.getFieldVal("hrmresource", "email", "id", hrs);// 发送给谁
		String cc = "";// 抄送
		String bcc = "";// 密送
		String body = "";// 发送的内容 (html) new StringBuffer();
		int char_set = 3;// 编码 固定 3
		String times = getGccs(rid, nodeid, hrs);
		String requestinfo [] = getRequestInfo(rid);
		String subject = "跟催"+times+"次延期"+cqsj+"H"+requestinfo[0]+";"+requestinfo[1]+"---客诉流程需提供报告或审核，请责任部门或人员紧急处理。";//标题
		log.writeLog("标题！----------"+subject);
		log.writeLog("发送给谁！----------"+to);
		boolean isSend = false;
		ArrayList filenames_f = new ArrayList();// /
		ArrayList filecontents = new ArrayList();//
		String priority = "3";// 重要程度 固定：3
		StringBuffer sbu = new StringBuffer();
		String title = "<p> Dear Sir,</p>";//
		String date = "";// 日期
		//sbu.append("<div style=\"width: 80%\"><p style=\"word-wrap:break-word; word-break:break-all;\">");
		//sbu.append("<span style=\"font-family:宋体\">&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sbu.append("跟催"+times+"次 延期"+cqsj+"小时:<br/><br/>");
		sbu.append("链接:(<a style='text-decoration: underline; color: blue;cursor:hand'  target='_blank' href=\"http://10.1.96.39:8082/login/Login.jsp?gopage=/workflow/request/ViewRequest.jsp?requestid="+rid+"\" >"+requestinfo[2]+"</a>)<br/>");
//		sbu.append("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+requestinfo[1]+";---客诉流程需提供报告或审核，请责任部门或人员紧急处理。");
//		sbu.append("<span><a href=\"http://10.1.96.39:8082/workflow/request/ViewRequest.jsp?requestid="+rid+"&_workflowid=46262&_workflowtype=\">"+requestinfo[2]+"(流程编号："+requestinfo[3]+")</a></span>");
//		sbu.append("</p><br><p><br><br><br></p><p></p><p></p><p style=\"word-wrap:break-word; word-break:break-all;float: left\">客诉系统 &nbsp;&nbsp;&nbsp;&nbsp;</p><p></p>");
//		sbu.append("<p style=\"word-wrap:break-word; word-break:break-all;float: left\">");
		Calendar nowDate = Calendar.getInstance();
		String month = "";
		String day = "";
		if (nowDate.get(Calendar.MONTH) + 1 < 10) {
			month = "0" + (nowDate.get(Calendar.MONTH) + 1);
		} else {
			month = "" + (nowDate.get(Calendar.MONTH) + 1);
		}
		if (nowDate.get(Calendar.DAY_OF_MONTH) < 10) {
			day = "0" + nowDate.get(Calendar.DAY_OF_MONTH);
		} else {
			day = "" + nowDate.get(Calendar.DAY_OF_MONTH);
		}
		date = nowDate.get(Calendar.YEAR) + "年" + month + "月" + day + "日";
		int dw = nowDate.get(Calendar.DAY_OF_WEEK);
		String strWeek = "";
		if (dw == 1) {
			strWeek = "星期天";
		} else if (dw == 2) {
			strWeek = "星期一";
		} else if (dw == 3) {
			strWeek = "星期二";
		} else if (dw == 4) {
			strWeek = "星期三";
		} else if (dw == 5) {
			strWeek = "星期四";
		} else if (dw == 6) {
			strWeek = "星期五";
		} else if (dw == 7) {
			strWeek = "星期六";
		}
		date = date + strWeek;
		
		String sql = "select yx from uf_kscs  ";
		rs.executeSql(sql);
		while(rs.next()){
			String yx = rs.getString("yx");
			if(cc.length()<1){
				cc = cc + yx;
			}else{
				cc = cc + "," + yx;
			}
		}	
		log.writeLog("抄送人-----------"+cc);
		if (!to.equals("")) {
			body = title  + sbu.toString() + date + "&nbsp;&nbsp;&nbsp;&nbsp;";
			log.writeLog("内容-----------"+body);
			isSend = SendMail.sendMiltipartHtml(from, to, cc, bcc, subject,	body, char_set, filenames_f, filecontents, priority);
			if (isSend) {
				log.writeLog("-成功----body---"+body + "邮件发送成功！");
			} else {
				log.writeLog("-失败------body---"+body + "邮件发送失败！");
			}
		} else {
				log.writeLog("-失败 没找到邮箱----流程id---" + rid +"---提醒人--"+hrs+"------"+requestinfo.toString());
			}
		return "";
	}
	
	
	public String getGccs(String rid,String nodeid,String hr){
		RecordSet rs = new RecordSet();
		String  con = "0";
		String sql = "select yjcs as con from uf_ksclsj where rid = '"+rid+"' and lcjsbs ='0' and lcjd =  '"+nodeid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			con = Util.null2String(rs.getString("con"));
		}
		return con;
	}
	/**
	 * 获取不良名称和流程名称以及客户名称
	 * @param rid
	 * @return
	 */
	public String[] getRequestInfo(String rid){
		RecordSet rs = new RecordSet();
		StringBuffer sb = new StringBuffer();
		String tablename = "formtable_main_1001";
		String khdm1 = "";//客户名称
		String blmc = "";//不良名称
		String byzl2cpmc = "";//名称2
		String byzl3cpmc = "";//名称3
		String requstname = "";//
		String lcbh = "";//流程编号
		String sql = "select lcbh,khdm1,blmc,byzl2cpmc,byzl3cpmc from "+tablename+"  where requestid = '"+rid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			khdm1 = Util.null2String(rs.getString("khdm1"));
			blmc = Util.null2String(rs.getString("blmc"));
			byzl2cpmc = Util.null2String(rs.getString("byzl2cpmc"));
			byzl3cpmc = Util.null2String(rs.getString("byzl3cpmc"));
			lcbh = Util.null2String(rs.getString("lcbh"));
		}
		sql = "select uc.cus_name from  uf_kskx_customer uc join uf_khdm uk on uc.id = uk.cus_name where uk.id = '"+khdm1+"'";
		rs.executeSql(sql);
		if(rs.next()){
			khdm1 = rs.getString("cus_name");
		}
		
		sql = "select blmc from uf_blmc where id = '"+blmc+"' ";
		rs.executeSql(sql);
		if(rs.next()){
			blmc = rs.getString("blmc");
		}
		
		sql = "select requestnamenew from workflow_requestbase where requestid ='"+rid+"' and workflowid='46262'";
		rs.executeSql(sql);
		if(rs.next()){
			requstname = rs.getString("requestnamenew");
		}
		String str = "";
		if(blmc.length()>0) str = str + blmc;
		if(byzl2cpmc.length()>0 && str.length()>0){
			str = str+"," + byzl2cpmc;
		}else{
			str = str+ byzl2cpmc;
		}
		if(byzl3cpmc.length()>0 && str.length()>0) {
			str = str +","+ byzl3cpmc;
		}else{
			str = str+ byzl3cpmc;
		}
		String requestinfo[] = {khdm1,str,requstname,lcbh};		
		return requestinfo;
	}

}
