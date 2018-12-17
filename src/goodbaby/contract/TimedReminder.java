package goodbaby.contract;

import goodbaby.util.GetUtil;

import java.util.ArrayList;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.system.SystemComInfo;

public class TimedReminder extends BaseCronJob{
	GetUtil gu = new GetUtil();
	
	public void execute() {
		RecordSet rs = new RecordSet ();
		RecordSet res = new RecordSet ();
		String email = "";
		String lastname ="";
		String htbh = "";
		String htmc = "";
		String sql = "select id,mfmc1,mfbh,htmc,htbh,datediff(day,CONVERT (nvarchar(10),GETDATE(),120),htjzrq) as cont  from uf_contracts where LEN(htjzrq)>8 and htxz = '1'";//htjzrq  mfmc1
		rs.executeSql(sql);
		while(rs.next()){
			String cont = Util.null2String(rs.getString("cont"));
			int  aa =Util.getIntValue(cont, 0);
			String mfmc1 = Util.null2String(rs.getString("mfmc1"));
			String mfbh = Util.null2String(rs.getString("mfbh"));
			htmc = Util.null2String(rs.getString("htmc"));
			htbh = Util.null2String(rs.getString("htbh"));
			String sub = mfbh.substring(0, 3);
			String mfmc= gu.getFieldVal("uf_suppmessForm", "GYSMC", "id", mfmc1);
			
			if(aa == 60){			
				String st = "select email,lastname from hrmresource where id = (select top 1 cgy from uf_NPP where MCODE = '"+sub+"' order by id desc)";
				res.executeSql(st);
				if(res.next()){
					email =res.getString("email");
					lastname = res.getString("lastname");
				}
				sendEmails(lastname,email,mfmc,htmc,htbh);
				
			}else if(aa<0){
				String st = "update uf_suppmessForm set GYSZT ='2' where GYSBM = '"+mfbh+"'";
				res.executeSql(st);
			}	
			
		}
		
	}
	
	
	
	//邮件
		public String sendEmails(String lastname,String email,String mfmc,String htmc,String htbh) {
			SystemComInfo si = new SystemComInfo();
			String from = si.getDefmailfrom();
			SendMail SendMail = new SendMail();
			BaseBean log = new BaseBean();
			SendMail.setMailServer("smtp.163.com");// 加邮箱服务器地址
			//String ID = docID;// -----------文档id
			String to = email;// 发送给谁
			String cc = "";// 抄送
			String bcc = "";// 密送
			String subject = "供应商合同到期提醒";// "Mflex知识系统供应商文档上传提醒";//标题
			//String body = "";// 发送的内容 (html) new StringBuffer();
			int char_set = 3;// 编码 固定 3
			boolean isSend = false;
			ArrayList filenames_f = new ArrayList();//
			ArrayList filecontents = new ArrayList();//
			String priority = "3";// 重要程度 固定：3
			StringBuffer sbu = new StringBuffer();
			
			sbu.append("<!DOCTYPE html><html><head lang=\"en\"><meta charset=\"UTF-8\"><title></title></head><body><p>");
			sbu.append(lastname);
			sbu.append("</p><p>&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;");
			sbu.append(mfmc+htmc+",合同("+htbh+"),60天后即将过期，请注意合同事项处理，谢谢！");
			sbu.append("</p><p><br/></p></body></html>");
			String body = sbu.toString() ;
			if (!to.equals("")) {
			isSend = SendMail.sendMiltipartHtml(from, to, cc, bcc, subject,body, char_set, filenames_f, filecontents, priority);
				if (isSend) {
					log.writeLog(body + "--------邮件发送成功！");
					log.writeLog( "email------------"+to);
				} else {
					log.writeLog(body + "-----------邮件发送失败！");
					log.writeLog( "email------------"+to);
				}
			} else {
					log.writeLog("邮件发送失败！----------" + body);
					log.writeLog( "email------------"+to);
			}
			return "";
		}
	

}
