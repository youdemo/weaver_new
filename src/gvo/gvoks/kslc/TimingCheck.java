package gvo.gvoks.kslc;

import java.text.ParseException;

import gvo.gvoks.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-4-4 上午9:48:38
 * 类说明
 */
public class TimingCheck extends BaseCronJob{
	
	public void execute() {
		GetUtil gu = new GetUtil();
		
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		RecordSet res1 = new RecordSet();
		BaseBean log = new BaseBean();
		String sfzysj = "";//三分之一时间
		String cstxzq = "";//提醒周期
		String bzsj = "";//步骤时间
		String workflowid = "46262";
		String tablename = "";//
		String sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowid+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tablename = Util.null2String(rs.getString("tablename"));
		}
		sql = "select b.requestid,b.creater ,a.id,b.currentnodeid from "+tablename+" a,workflow_requestbase  b where a.requestid=b.requestid and" +
				" b.currentnodetype<3 and b.currentnodetype>0  and b.workflowid='"+workflowid+"' order by id desc";
		rs.execute(sql);
		while(rs.next()){
			String requestid = rs.getString("requestid");
			String currentnodeid = rs.getString("currentnodeid");//当前节点id
			String sqlstr = "select sfzysj,bzsj,cstxzq from uf_kssx where  lcid ='46262' and nodename = '"+currentnodeid+"'";
			res.executeSql(sqlstr);
			if(res.next()){
				sfzysj =  res.getString("sfzysj");
				bzsj =  res.getString("bzsj");
				cstxzq =  res.getString("cstxzq");
			}
			String str = "update uf_ksclsj set lcjd = '"+currentnodeid+"' where rid ='"+requestid+"' and lcjsbs = 0 ";
			res1.executeSql(str);
			String arrivetIme = "";//到达时间
			String nowtime = gu.getNowdate();//当前时间
			double stopTime = 0.00;//停留时间
			str = "select lcddsj,yjcs from uf_ksclsj where rid = '"+requestid+"' and lcjsbs ='0'  ";
			res1.executeSql(str);
			while(res1.next()){
				arrivetIme = res1.getString("lcddsj");
			}
			try {
				stopTime = gu.subDouble(arrivetIme,nowtime);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			double sfze = gu.mul(sfzysj, "2");
			log.writeLog("三分之二------------------"+sfze+"-------停留时间-----"+stopTime);
			if(stopTime>=Double.valueOf(sfze) && stopTime<=Double.valueOf(bzsj)){
				log.writeLog("三分之二---------邮件开始---------");
				SfzeToEmail(workflowid, requestid, currentnodeid,nowtime);
				log.writeLog("三分之二---------邮件结束---------");
			}else if(stopTime>Double.valueOf(bzsj)){
				log.writeLog("超期---------邮件开始---------");
				OverToEmail(workflowid, requestid, currentnodeid, nowtime, stopTime, cstxzq, bzsj);
				log.writeLog("超期---------邮件结束---------");
			}				
		}
	}
	/**
	 * 2/3提醒
	 * @param workflowid
	 * @param requestid
	 * @param currentnodeid
	 * @param nowtime
	 * @return
	 */
	public String SfzeToEmail(String workflowid,String requestid,String currentnodeid,String nowtime){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		SendEmail se = new SendEmail();
		int yjcs = 0;
		String str = "select yjcs from uf_ksclsj where rid = '"+requestid+"' and lcjsbs ='0' and lcjd =  '"+currentnodeid+"'";
		rs.executeSql(str);
		if(rs.next()){
			String yj = Util.null2String(rs.getString("yjcs"));
			if(yj.length()>0){
				yjcs = Util.getIntValue(yj,0) + 1;
			}else{
				yjcs = yjcs +1;
			}
		}
		log.writeLog("三分之二提醒-------1--------------------");
		str = "select userid from workflow_currentoperator where requestid = '"+requestid+"' and workflowid='"
				+workflowid+"' and nodeid = '"+currentnodeid+"' and ((isremark =0 and takisremark=0) or (isremark =0 and takisremark is null) or (isremark =9 and takisremark=2) )";
		rs.executeSql(str);
		log.writeLog("三分之二提醒  sql-------1--------------------"+str);
		while(rs.next()){
			String userid = rs.getString("userid");
			String str1 = "select count(id) as con  from uf_kssxtxjl where rid ='"+requestid+"' and lcjd =  '"
					+currentnodeid+"' and lcczr = '"+userid+"' and yjlx = 0 ";
			res.executeSql(str1);
			int con = 0;
			if(res.next()){
				con = res.getInt("con");
			}
			log.writeLog("三分之二提醒  con-------1--------------------"+con);
			if(con==0){
				str1 = "insert into uf_kssxtxjl(rid,lcjd,lcczr,yjlx,lcgccs,	yjfssj) values ('"+requestid+"','"+currentnodeid+"','"+userid+"','0',1,'"+nowtime+"')";
				res.executeSql(str1);
				se.sendEmails(requestid, userid, currentnodeid,"0");
				yjcs = yjcs + 1 ; 
				str1 = "update uf_ksclsj set yjcs = '"+yjcs+"' where rid ='"+requestid+"' and lcjd =  '"+currentnodeid+"'  and lcjsbs = 0 ";
				res.executeSql(str1);
				log.writeLog("三分之二提醒-------------2--------------");
			}	
		}
		return "";
		
	}
	/**
	 * 超时提醒
	 * @param workflowid
	 * @param requestid
	 * @param currentnodeid
	 * @param nowtime
	 * @param stopTime
	 * @param cstxzq
	 * @param bzsj
	 * @return
	 */
	public String OverToEmail(String workflowid,String requestid,String currentnodeid,String nowtime,double stopTime,String cstxzq,String bzsj){
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet res1 = new RecordSet();
		SendEmail se = new SendEmail();
		GetUtil gu = new GetUtil();
		log.writeLog("超时提醒---------------------------");
		log.writeLog( workflowid+"-----------"+requestid+"-----------"+ currentnodeid+"-----------"+ nowtime+"-----------"+stopTime+"-----------"+ cstxzq+"-----------"+ bzsj);
		double c = gu.subDouble1(bzsj, stopTime+"");
		int a = (int) (c / Double.valueOf(cstxzq));
		double b =  (c % Double.valueOf(cstxzq));
		log.writeLog("a-----"+a+"----b---"+b+"------------c---"+c);
		int yjcs = 0;
		String str = "select yjcs from uf_ksclsj where rid = '"+requestid+"' and lcjsbs ='0' and lcjd =  '"+currentnodeid+"'";
		rs.executeSql(str);
		if(rs.next()){
			String yj = Util.null2String(rs.getString("yjcs"));
			if(yj.length()>0){
				yjcs = Util.getIntValue(yj,0) + 1;
			}else{
				yjcs = yjcs +1;
			}
		}
		str = "select userid from workflow_currentoperator where requestid = '"+requestid+"' and workflowid='"
				+workflowid+"' and nodeid = '"+currentnodeid+"' and ((isremark =0 and takisremark=0) or (isremark =0 and takisremark is null) or (isremark =9 and takisremark=2) )";
		log.writeLog("超时提醒sql1---------------------------"+str);
		rs.executeSql(str);
		
		while(rs.next()){
			String userid = rs.getString("userid");
			if(a>0 && b ==0){
				log.writeLog("整数提醒-1--------------------------");
				String str1 = "insert into uf_kssxtxjl(rid,lcjd,lcczr,yjlx,lcgccs,yjfssj) values ('"+requestid+"','"+currentnodeid+"','"+userid+"','1','"+a+"','"+nowtime+"')";
				res1.executeSql(str1);
				se.sendEmails(requestid, userid, currentnodeid,c+"");
				str1 = "update uf_ksclsj set yjcs = '"+yjcs+"' where rid ='"+requestid+"' and lcjd =  '"+currentnodeid
						+"'  and lcjsbs = 0 ";
				res1.executeSql(str1);
				log.writeLog("整数提醒----2-----------------------");
			}else if(a>0){
				String lcgccs = "0";//跟催次数
				String str1 = "select lcgccs from uf_kssxtxjl where id in  (select max(id) from uf_kssxtxjl where rid = '"+requestid+"' and lcjd='"+currentnodeid+"' and lcczr = '"+userid+"')";
				log.writeLog("超时提醒sql2-----------------------"+str1);
				res1.executeSql(str1);
				if(res1.next()){
					lcgccs = Util.null2String(res1.getString("lcgccs"));
				}
				log.writeLog("超时提醒lcgccs-----------------------"+lcgccs);
				if(Util.getIntValue(lcgccs)<(a+1)){
					log.writeLog("非整数提醒---2------------------------");
					str1 = "insert into uf_kssxtxjl(rid,lcjd,lcczr,yjlx,lcgccs,yjfssj) values ('"+requestid+"','"+currentnodeid+"','"+userid+"','1','"+(a+1)+"','"+nowtime+"')";
					res1.executeSql(str1);
					se.sendEmails(requestid, userid, currentnodeid,c+"");
					yjcs = yjcs + 1 ; 
					str1 = "update uf_ksclsj set yjcs = '"+yjcs+"' where rid ='"+requestid+"' and lcjd =  '"+currentnodeid
							+"'  and lcjsbs = 0 ";
					res1.executeSql(str1);
					log.writeLog("非整数提醒------2---------------------");
				}
			}
		}
		return "";
		
	}
	
	
	

}
