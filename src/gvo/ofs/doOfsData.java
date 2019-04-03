package gvo.ofs;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
import weaver.interfaces.web.PostWorkflowInf;

public class doOfsData extends BaseCronJob{
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("开始处理统一代办异常数据");
		doUnPushData();
		doIncorrect();
		doIncorrect2();
		log.writeLog("处理统一代办异常数据 结束");
	}
	public void doUnPushData(){
		RecordSet rs = new RecordSet();

		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select requestid from workflow_requestbase  a where a.currentnodetype<3  and requestid not in(select distinct flowid from ofs_todo_data@dblink_oa where syscode='EC') and a.creater <> 1 and a.workflowid not in(644,1) and a.createdate>='2019-01-01'";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("requestid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			if(checkflowStatus(requestid)){
				continue;
			}
			pwf.operateToDo(requestid);
		}
	}
	
	public void doIncorrect(){
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("oatest");
		String sql_dt="";
		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select distinct a.flowid from ofs_todo_data@dblink_oa a where a.syscode='EC' and a.islasttimes='1' and a.isremark='0' and a.flowid in(select requestid from workflow_requestbase where workflowid not in(644,1)  and createdate>='2019-01-01' and currentnodetype<3) and (a.receiver,a.receivedate,a.receivetime ) not in(select (select case when belongto is not null then (select loginid from hrmresource where id=e.belongto) else loginid end  from hrmresource e where id=userid),receivedate,receivetime from workflow_currentoperator d where requestid=a.flowid and islasttimes=1 and isremark not in(2,4) and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 )";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("flowid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			if(checkflowStatus(requestid)){
				continue;
			}
			sql_dt="delete from ofs_todo_data where flowid= '"+requestid+"' ";
			rsd.executeSql(sql_dt);
			pwf.operateToDo(requestid);
		}
	}
	public void doIncorrect2(){
		RecordSet rs = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("oatest");
		String sql_dt="";
		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select t.flowid from (select flowid,count(1) as count from ofs_todo_data@dblink_oa where syscode='EC' and islasttimes='1' and isremark='0' group by flowid) t,(select count(d.id) as count,d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in(644,1) and b.creater <> 1 and b.createdate>='2019-01-01' and isremark not in(2,4) and islasttimes=1 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid) t1 where t.flowid=t1.requestid and t.count<>t1.count  "+
				   " union all "+
				   " select t.flowid from  (select flowid,count(1) as count from ofs_todo_data@dblink_oa where syscode='EC' and  islasttimes='1' and isremark='0' group by flowid) t, workflow_requestbase t1 where t.flowid=t1.requestid and t1.workflowid not in(644,1) and t1.creater <> 1 and t1.createdate>='2019-01-01' and t1.currentnodetype<3 and flowid not in (select d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in(644,1)  and b.creater <> 1 and isremark not in(2,4) and islasttimes=1 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid)  "+
				   " union all "+
				   " select to_char(t.requestid )as flowid from (select count(d.id) as count,d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in(644,1) and b.creater <> 1 and b.createdate>='2019-01-01' and b.currentnodetype<3 and isremark not in(2,4) and islasttimes=1 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid) t where t.requestid not in(select flowid from ofs_todo_data@dblink_oa where syscode='EC' and islasttimes='1' and isremark='0' group by flowid)  ";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("flowid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			if(checkflowStatus(requestid)){
				continue;
			}
			sql_dt="delete from ofs_todo_data where flowid='"+requestid+"' ";
			rsd.executeSql(sql_dt);
			pwf.operateToDo(requestid);
		}
	}
	
	public boolean checkPersonStatus(String requestid){
		RecordSet rs = new RecordSet();
		int count=0;
		String sql="select count(b.id) as count from workflow_requestbase a,hrmresource b where a.creater=b.id and a.requestid="+requestid+" and b.status in (0,1,2,3)";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count >0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean checkflowStatus(String requestid){
		RecordSet rs = new RecordSet();
		int count=0;
		String sql="select count(requestid) as count from workflow_requestbase where requestid="+requestid+" and currentnodetype=3";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count >0){
			return true;
		}else{
			return false;
		}
	}
}
