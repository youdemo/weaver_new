package TaiSon.kq;

import java.text.SimpleDateFormat;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.system.SysRemindWorkflow;

public class KqUtil {
	/**
	 * 创建提醒流程
	 * @param title
	 * @param creater
	 * @param receiver
	 * @param bz
	 */
	public void createRemindWorkflow(String title,int creater,String receiver,String bz) {
		BaseBean log = new BaseBean();
		SysRemindWorkflow remindwf=new SysRemindWorkflow();
		 try {
			 remindwf.make(title, 0, 0, 0, 0, creater, receiver, bz,0);
			}catch (Exception e) {
				// TODO Auto-generated catch block
				log.writeLog("推送提醒流程失败");
				log.writeLog(e);
			}
	}
	/**
	 * 插入漏打卡记录到中间表
	 * @param startDate
	 * @param endDate
	 */
	public void insertLdkData(String startDate,String endDate) {
		
		RecordSet rs = new RecordSet();
		String sql = "";
		String checkDate = "";
		sql = "delete from uf_kq_ldk_mid";
		rs.executeSql(sql);
		sql = "select * from (SELECT  TO_CHAR(TO_DATE('"+startDate+"', 'YYYY-MM-DD') + ROWNUM - 1, 'YYYY-MM-DD') DAY_ID " + 
				"  FROM DUAL " + 
				" CONNECT BY ROWNUM <= TO_DATE('"+endDate+"', 'YYYY-MM-DD') - TO_DATE('"+startDate+"', 'YYYY-MM-DD') + 1) where app_what_holiday(DAY_ID)=2";
		rs.executeSql(sql);
		while(rs.next()) {
			checkDate = Util.null2String(rs.getString("DAY_ID"));
			insertLdkDateByDate(checkDate);
		}
		//checkLdkDate();
		sql = "select * from (SELECT  TO_CHAR(TO_DATE('"+startDate+"', 'YYYY-MM-DD') + ROWNUM - 1, 'YYYY-MM-DD') DAY_ID " + 
				"  FROM DUAL " + 
				" CONNECT BY ROWNUM <= TO_DATE('"+endDate+"', 'YYYY-MM-DD') - TO_DATE('"+startDate+"', 'YYYY-MM-DD') + 1) where app_what_holiday(DAY_ID)<>2";
		rs.executeSql(sql);
		while(rs.next()) {
			checkDate = Util.null2String(rs.getString("DAY_ID"));
			insertLdkDateByDate2(checkDate);
		}
				
		
	}
	/**
	 * 针对一天插入当天漏打卡记录
	 * @param checkDate
	 */
	public void insertLdkDateByDate(String checkDate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String sql = "";
		String sql_dt = "";
		
		//校验考勤记录
		//上班漏打卡
	
		String checkNextDate = TimeUtil.dateAdd(checkDate, 1);
//		sql = "select id from hrmresource where id not in(select distinct userid from HRMSCHEDULESIGN where signDate ='"+checkDate+"' and signTime>='05:00:00' and signTime<='12:00:00') and status<5 and nvl(belongto,-1)<=0";
//		rs.executeSql(sql);
//		while(rs.next()) {
//			String ryid = Util.null2String(rs.getString("id"));
//			sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','0')";
//			rs_dt.executeSql(sql_dt);
//		}
		sql = "insert into uf_kq_ldk_mid(ryid,rq,type) select distinct emp_id,atten_day,'0' from uf_all_atten_info where isEx=1 and (min_time is null or min_time>='12:00:00') and atten_day='"+checkDate+"' and (emp_id in(select distinct xm from uf_tsmygskqry) or emp_id in(select distinct xm from uf_jtzbkqry))";
		rs.executeSql(sql);
		//下班打卡
//		sql = "select id from hrmresource where id not in(select distinct userid from HRMSCHEDULESIGN where signDate ='"+checkDate+"' and signTime>='13:30:00' and signTime<='23:59:59') and id not in(select distinct userid from HRMSCHEDULESIGN where signDate ='"+checkNextDate+"' and signTime>='00:00:00' and signTime<='04:59:59') and status<5 and nvl(belongto,-1)<=0 ";
//		rs.executeSql(sql);
//		while(rs.next()) {
//			String ryid = Util.null2String(rs.getString("id"));
//			sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','1')";
//			rs_dt.executeSql(sql_dt);
//		}
		//sql = "insert into uf_kq_ldk_mid(ryid,rq,type) select id,'"+checkDate+"','1' from hrmresource where id not in(select distinct userid from HRMSCHEDULESIGN where signDate ='"+checkDate+"' and signTime>='13:30:00' and signTime<='23:59:59') and id not in(select distinct userid from HRMSCHEDULESIGN where signDate ='"+checkNextDate+"' and signTime>='00:00:00' and signTime<='04:59:59') and status<5 and nvl(belongto,-1)<=0 and subcompanyid1 in(253,256)";
		sql = "insert into uf_kq_ldk_mid(ryid,rq,type) select distinct emp_id,atten_day,'1' from uf_all_atten_info where isEx=1 and (max_time is null or max_time<'12:00:00') and atten_day='"+checkDate+"' and (emp_id in(select distinct xm from uf_tsmygskqry) or emp_id in(select distinct xm from uf_jtzbkqry))";
		rs.executeSql(sql);
	}
	
	/**
	 * 非工作日漏打卡
	 * @param checkDate
	 */
	public void insertLdkDateByDate2(String checkDate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String sql = "";
		String sql_dt = "";
		
		//校验考勤记录
		//上班漏打卡
	
		String checkNextDate = TimeUtil.dateAdd(checkDate, 1);
		sql = "select userid,min(signtime) as signtime from HRMSCHEDULESIGN where signdate='"+checkDate+"' group by userid having count(1)=1 and (userid in(select distinct xm from uf_tsmygskqry) or userid in(select distinct xm from uf_jtzbkqry)) ";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("userid"));
			String signtime = Util.null2String(rs.getString("signtime"));
			if(signtime.compareTo("12:00:00")<=0) {
				sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','1')";
				rs_dt.executeSql(sql_dt);
			}else {
				sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','0')";
				rs_dt.executeSql(sql_dt);
			}
		}
		
		sql = " select userid,min(signtime) as signtime,floor((to_date('"+checkDate+"'||' '||max(signtime),'yyyy-mm-dd hh24-mi-ss')-to_date('"+checkDate+"'||' '||min(signtime),'yyyy-mm-dd hh24-mi-ss'))*24) as xs from HRMSCHEDULESIGN where signdate='"+checkDate+"' group by userid having count(1)>1 and (userid in(select distinct xm from uf_tsmygskqry) or userid in(select distinct xm from uf_jtzbkqry))";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("userid"));
			String signtime = Util.null2String(rs.getString("signtime"));
			int xs= Util.getIntValue(Util.null2String(rs.getString("xs")),0);
			if(xs < 2 && signtime.compareTo("12:00:00")<=0) {
				sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','1')";
				rs_dt.executeSql(sql_dt);
			}else if(xs < 2 && signtime.compareTo("12:00:00")>0){
				sql_dt = "insert into uf_kq_ldk_mid(ryid,rq,type) values('"+ryid+"','"+checkDate+"','0')";
				rs_dt.executeSql(sql_dt);
			}
		}
		
	}
	/**
	 * 根据中间表得漏打卡记录校验流程数据
	 */
	public void checkLdkDate() {
		RecordSet rs = new RecordSet();
		String sql = "select * from uf_kq_ldk_mid";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("ryid"));
			String rq = Util.null2String(rs.getString("rq"));
			String type = Util.null2String(rs.getString("type"));
			String billid = Util.null2String(rs.getString("id"));
			if(checkQjLCLdk(ryid,rq,type,billid)) {
				continue;
			}else if(checkTxLCLdk(ryid,rq,type,billid)) {
				continue;
			}else if(checkCCLCLdk(ryid,rq,type,billid)) {
				continue;
			}else if(checkWcLCLdk(ryid,rq,type,billid)) {
				continue;
			}
			
		}
	}
	/**
	 * 校验请假流程
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
	public boolean checkQjLCLdk(String ryid,String rq,String type,String billid ) {
		RecordSet rs = new RecordSet();
		String qjlcTableName = "formtable_main_251";//正式formtable_main_251 测试 formtable_main_256
		String sql = "";
		String result = "0";
		int count =0;
		if("0".equals(type)) {
			sql = "select count(1) as count from "+qjlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj>='00:00' and qjkssj<='12:00') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='08:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}else {
			sql = "select count(1) as count from "+qjlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=0 and sqr='"+ryid+"' and ((qjjssrq='"+rq+"' and qjjssj>'13:30' ) or (qjjssrq>'"+rq+"' and qjkssrq='"+rq+"' and qjkssj<'17:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}
		if("1".equals(result)) {
			deleteLdkData(billid);
			return true;
		}
		return false;
	}
	/**
	 * 校验调休流程
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
	public boolean checkTxLCLdk(String ryid,String rq,String type,String billid ) {
		RecordSet rs = new RecordSet();
		String txlcTableName = "formtable_main_256";
		String sql = "";
		String result = "0";
		int count =0;
		if("0".equals(type)) {
			sql = "select count(1) as count from "+txlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj>='00:00' and qjkssj<='12:00') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='08:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}else {
			sql = "select count(1) as count from "+txlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((qjjssrq='"+rq+"' and qjjssj>'13:30' ) or (qjjssrq>'"+rq+"' and qjkssrq='"+rq+"' and qjkssj<'17:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}
		if("1".equals(result)) {
			deleteLdkData(billid);
			return true;
		}
		return false;
	}
	/**
	 * 校验出差流程
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
		public boolean checkCCLCLdk(String ryid,String rq,String type,String billid ) {
			RecordSet rs = new RecordSet();
			String cclcTableName = "formtable_main_250";
			String sql = "";
			String result = "0";
			int count =0;
			if("0".equals(type)) {
				sql = "select count(1) as count from "+cclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((ccksrq='"+rq+"' and cckssj>='00:00' and cckssj<='12:00') or (ccksrq<'"+rq+"' and ccjsrq='"+rq+"' and ccjssj>='08:30' ) or (ccksrq<'"+rq+"' and ccjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}else {
				sql = "select count(1) as count from "+cclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((ccjsrq='"+rq+"' and ccjssj>'13:30' ) or (ccjsrq>'"+rq+"' and ccksrq='"+rq+"' and cckssj<'17:30' ) or (ccksrq<'"+rq+"' and ccjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}
			if("1".equals(result)) {
				deleteLdkData(billid);
				return true;
			}
			return false;
		}
		/**
		 * 校验外出流程
		 * @param ryid
		 * @param rq
		 * @param type
		 * @param billid
		 * @return
		 */
		public boolean checkWcLCLdk(String ryid,String rq,String type,String billid ) {
			RecordSet rs = new RecordSet();
			String wclcTableName = "formtable_main_249";
			String sql = "";
			String result = "0";
			int count =0;
			if("0".equals(type)) {
				sql = "select count(1) as count from "+wclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((wcqrq='"+rq+"' and wcqssj>='00:00' and wcqssj<='12:00') or (wcqrq<'"+rq+"' and wcjsrq='"+rq+"' and wcjssj>='08:30' ) or (wcqrq<'"+rq+"' and wcjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}else {
				sql = "select count(1) as count from "+wclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((wcjsrq='"+rq+"' and wcjssj>'13:30' ) or (wcjsrq>'"+rq+"' and wcqrq='"+rq+"' and wcqssj<'17:30' ) or (wcqrq<'"+rq+"' and wcjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}
			if("1".equals(result)) {
				deleteLdkData(billid);
				return true;
			}
			return false;
		}
	/**
	 * 删除漏打卡记录		
	 * @param billid
	 */
	public void deleteLdkData(String billid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		sql = "delete from uf_kq_ldk_mid where id="+billid;
		rs.executeSql(sql);
	}
	
	/**
	 * 插入缺勤中间表
	 * @param startDate
	 * @param endDate
	 */
	public void insertQQData(String startDate,String endDate) {
		
		RecordSet rs = new RecordSet();
		String sql = "";
		String checkDate = "";
		sql = "delete from uf_kq_qq_mid";
		rs.executeSql(sql);
		sql = "select * from (SELECT  TO_CHAR(TO_DATE('"+startDate+"', 'YYYY-MM-DD') + ROWNUM - 1, 'YYYY-MM-DD') DAY_ID " + 
				"  FROM DUAL " + 
				" CONNECT BY ROWNUM <= TO_DATE('"+endDate+"', 'YYYY-MM-DD') - TO_DATE('"+startDate+"', 'YYYY-MM-DD') + 1) where app_what_holiday(DAY_ID)=2";
		rs.executeSql(sql);
		while(rs.next()) {
			checkDate = Util.null2String(rs.getString("DAY_ID"));
			insertQqDateByDate(checkDate);
		}
		//checkQqDate();
				
		
	}
	
	public void insertQqDateByDate(String checkDate) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String sql = "";
		String sql_dt = "";
		
		//校验考勤记录
		//上班漏打卡
	
		String checkNextDate = TimeUtil.dateAdd(checkDate, 1);
		sql = "select id from hrmresource a where status<5 and nvl(belongto,-1)<=0 and (id in(select distinct xm from uf_tsmygskqry) or id in(select distinct xm from uf_jtzbkqry)) and exists(select 1 from uf_all_atten_info where nvl(late_times,0)>15 and atten_day='"+checkDate+"' and emp_id=a.id)";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("id"));
			sql_dt = "insert into uf_kq_qq_mid(ryid,rq,type,qqsj) values('"+ryid+"','"+checkDate+"','0','')";
			rs_dt.executeSql(sql_dt);
		}
		//下班打卡
		//sql = "select id from hrmresource a where status<5 and nvl(belongto,-1)<=0 and subcompanyid1 in(253,256) and exists(select 1 from  HRMSCHEDULESIGN where userId=a.id and signDate='"+checkDate+"' and signTime>='13:30:00' and signTime<='17:15:00') and not exists(select 1 from  HRMSCHEDULESIGN where userId=a.id and signDate='"+checkDate+"' and signTime>'17:15:00' and signTime<='23:59:59') and not exists(select 1 from  HRMSCHEDULESIGN where userId=a.id and signDate='"+checkNextDate+"' and signTime>='00:00:00' and signTime<'01:59:59')";
		sql = "select id from hrmresource a where status<5 and nvl(belongto,-1)<=0 and (id in(select distinct xm from uf_tsmygskqry) or id in(select distinct xm from uf_jtzbkqry)) and exists(select 1 from uf_all_atten_info where nvl(early_leave_times,0)>15 and atten_day='"+checkDate+"' and emp_id=a.id)";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("id"));
			sql_dt = "insert into uf_kq_qq_mid(ryid,rq,type,qqsj) values('"+ryid+"','"+checkDate+"','1','')";
			rs_dt.executeSql(sql_dt);
		}
	}
	public void checkQqDate() {
		RecordSet rs = new RecordSet();
		String sql = "select * from uf_kq_qq_mid";
		rs.executeSql(sql);
		while(rs.next()) {
			String ryid = Util.null2String(rs.getString("ryid"));
			String rq = Util.null2String(rs.getString("rq"));
			String type = Util.null2String(rs.getString("type"));
			String billid = Util.null2String(rs.getString("id"));
			String qqsj = Util.null2String(rs.getString("qqsj"));
			if(checkQjLCQq(ryid,rq,type,billid,qqsj)) {
				continue;
			}else if(checkTxLCQq(ryid,rq,type,billid,qqsj)) {
				continue;
			}else if(checkCCLCQq(ryid,rq,type,billid,qqsj)) {
				continue;
			}else if(checkWcLCQq(ryid,rq,type,billid,qqsj)) {
				continue;
			}else if(checkQqDateSpecial(ryid,rq,type,billid,qqsj)) {
				continue;
			}
			
		}
	}
	/**
	 * 前提21点后打卡，第二天9点30上班
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
	public boolean checkQqDateSpecial(String ryid,String rq,String type,String billid,String qqsj ) {
		RecordSet rs = new RecordSet();
		String sql = "";
		if("0".equals(type)&&qqsj.compareTo("09:46")<0) {
			String qday = TimeUtil.dateAdd(rq, -1);
			String maxsigntime = "";
			sql = "select nvl(max(signtime),'17:30:00') as maxsigntime from hrmschedulesign where userid="+ryid+" and signdate='"+qday+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				maxsigntime = Util.null2String(rs.getString("maxsigntime"));
			}
			if(maxsigntime.compareTo("21:00:00")>=0) {
				deleteQqData(billid);
				return true;
			}
			
		}
		return false;
	}
	public boolean checkQjLCQq(String ryid,String rq,String type,String billid,String qqsj ) {
		RecordSet rs = new RecordSet();
		String qjlcTableName = "formtable_main_251";//正式formtable_main_251 测试 formtable_main_256
		String sql = "";
		String result = "0";
		int count =0;
		if("0".equals(type)) {
			sql = "select count(1) as count from "+qjlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj<='08:30' and qjjssrq='"+rq+"' and qjjssj>='"+qqsj+"') or (qjkssrq='"+rq+"' and qjkssj<='08:30' and qjjssrq>'"+rq+"') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='"+qqsj+"' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}else {
			sql = "select count(1) as count from "+qjlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=0 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj<='"+qqsj+"' and qjjssrq='"+rq+"' and qjjssj>='17:30') or (qjkssrq='"+rq+"' and qjkssj<='"+qqsj+"' and qjjssrq>'"+rq+"') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='17:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}
		if("1".equals(result)) {
			deleteQqData(billid);
			return true;
		}
		return false;
	}
	/**
	 * 校验调休流程
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
	public boolean checkTxLCQq(String ryid,String rq,String type,String billid,String qqsj ) {
		RecordSet rs = new RecordSet();
		String txlcTableName = "formtable_main_256";
		String sql = "";
		String result = "0";
		int count =0;		
		if("0".equals(type)) {
			sql = "select count(1) as count from "+txlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj<='08:30' and qjjssrq='"+rq+"' and qjjssj>='"+qqsj+"') or (qjkssrq='"+rq+"' and qjkssj<='08:30' and qjjssrq>'"+rq+"') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='"+qqsj+"' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}else {
			sql = "select count(1) as count from "+txlcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=0 and sqr='"+ryid+"' and ((qjkssrq='"+rq+"' and qjkssj<='"+qqsj+"' and qjjssrq='"+rq+"' and qjjssj>='17:30') or (qjkssrq='"+rq+"' and qjkssj<='"+qqsj+"' and qjjssrq>'"+rq+"') or (qjkssrq<'"+rq+"' and qjjssrq='"+rq+"' and qjjssj>='17:30' ) or (qjkssrq<'"+rq+"' and qjjssrq>'"+rq+"' ))";
			rs.executeSql(sql);
			if(rs.next()) {
				count = rs.getInt("count");
			}
			if(count>0) {
				result = "1";
			}
		}
		if("1".equals(result)) {
			deleteQqData(billid);
			return true;
		}
		return false;
	}
	/**
	 * 校验出差流程
	 * @param ryid
	 * @param rq
	 * @param type
	 * @param billid
	 * @return
	 */
		public boolean checkCCLCQq(String ryid,String rq,String type,String billid ,String qqsj) {
			RecordSet rs = new RecordSet();
			String cclcTableName = "formtable_main_250";
			String sql = "";
			String result = "0";
			int count =0;
			
			if("0".equals(type)) {
				sql = "select count(1) as count from "+cclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((ccksrq='"+rq+"' and cckssj<='08:30' and ccjsrq='"+rq+"' and ccjssj>='"+qqsj+"') or (ccksrq='"+rq+"' and cckssj<='08:30' and ccjsrq>'"+rq+"') or (ccksrq<'"+rq+"' and ccjsrq='"+rq+"' and ccjssj>='"+qqsj+"' ) or (ccksrq<'"+rq+"' and ccjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}else {
				sql = "select count(1) as count from "+cclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=0 and sqr='"+ryid+"' and ((ccksrq='"+rq+"' and cckssj<='"+qqsj+"' and ccjsrq='"+rq+"' and ccjssj>='17:30') or (ccksrq='"+rq+"' and cckssj<='"+qqsj+"' and ccjsrq>'"+rq+"') or (ccksrq<'"+rq+"' and ccjsrq='"+rq+"' and ccjssj>='17:30' ) or (ccksrq<'"+rq+"' and ccjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}
			if("1".equals(result)) {
				deleteQqData(billid);
				return true;
			}
			return false;
		}
		/**
		 * 校验外出流程
		 * @param ryid
		 * @param rq
		 * @param type
		 * @param billid
		 * @return
		 */
		public boolean checkWcLCQq(String ryid,String rq,String type,String billid ,String qqsj) {
			RecordSet rs = new RecordSet();
			String wclcTableName = "formtable_main_249";
			String sql = "";
			String result = "0";
			int count =0;
			
			if("0".equals(type)) {
				sql = "select count(1) as count from "+wclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=3 and sqr='"+ryid+"' and ((wcqrq='"+rq+"' and wcqssj<='08:30' and wcjsrq='"+rq+"' and wcjssj>='"+qqsj+"') or (wcqrq='"+rq+"' and wcqssj<='08:30' and wcjsrq>'"+rq+"') or (wcqrq<'"+rq+"' and wcjsrq='"+rq+"' and wcjssj>='"+qqsj+"' ) or (wcqrq<'"+rq+"' and wcjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}else {
				sql = "select count(1) as count from "+wclcTableName+" a,workflow_requestbase b where  a.requestid=b.requestid and b.currentnodetype>=0 and sqr='"+ryid+"' and ((wcqrq='"+rq+"' and wcqssj<='"+qqsj+"' and wcjsrq='"+rq+"' and wcjssj>='17:30') or (wcqrq='"+rq+"' and wcqssj<='"+qqsj+"' and wcjsrq>'"+rq+"') or (wcqrq<'"+rq+"' and wcjsrq='"+rq+"' and wcjssj>='17:30' ) or (wcqrq<'"+rq+"' and wcjsrq>'"+rq+"' ))";
				rs.executeSql(sql);
				if(rs.next()) {
					count = rs.getInt("count");
				}
				if(count>0) {
					result = "1";
				}
			}
			if("1".equals(result)) {
				deleteQqData(billid);
				return true;
			}
			return false;
		}
	/**
	 * 删除漏打卡记录		
	 * @param billid
	 */
	public void deleteQqData(String billid) {
		RecordSet rs = new RecordSet();
		String sql = "";
		sql = "delete from uf_kq_qq_mid where id="+billid;
		rs.executeSql(sql);
	}
	
}
