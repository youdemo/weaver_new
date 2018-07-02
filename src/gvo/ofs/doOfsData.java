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
	/**
	 * 没推送
	 */
	public void doUnPushData(){
		RecordSet rs = new RecordSet();
		String workflowId = getWorkFlowId();
		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select requestid from workflow_requestbase  a where a.currentnodetype<3  and requestid not in(select distinct flowid from ofs_todo_data@dblink_oa where syscode='EC') and a.creater <> 1 and a.workflowid not in("+workflowId+") and a.createdate>='2018-02-23'";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("requestid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			pwf.operateToDo(requestid);
		}
	}
	/**
	 * 两边操作者不一致
	 */
	public void doIncorrect(){
		RecordSet rs = new RecordSet();
		String workflowId = getWorkFlowId();
		RecordSetDataSource rsd = new RecordSetDataSource("oadb");
		String sql_dt="";
		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select distinct a.flowid from ofs_todo_data@dblink_oa a where a.syscode='EC' and a.islasttimes='1' and a.isremark='0' and a.flowid in(select requestid from workflow_requestbase where workflowid not in("+workflowId+")  and createdate>='2018-02-23' ) and (a.receiver,a.receivedate,a.receivetime ) not in(select (select case when belongto is not null then (select loginid from hrmresource where id=e.belongto) else loginid end  from hrmresource e where id=userid),receivedate,receivetime from workflow_currentoperator d where requestid=a.flowid and islasttimes=1 and isremark not in(2,4) and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 )";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("flowid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			sql_dt="delete from ofs_todo_data where syscode='EC' and flowid="+requestid;
			rsd.executeSql(sql_dt);
			pwf.operateToDo(requestid);
		}
	}
	public void doIncorrect2(){
		RecordSet rs = new RecordSet();
		String workflowId = getWorkFlowId();
		RecordSetDataSource rsd = new RecordSetDataSource("oadb");
		String sql_dt="";
		PostWorkflowInf pwf = new PostWorkflowInf();
		String requestid="";
		String sql="select t.flowid from (select flowid,count(1) as count from ofs_todo_data@dblink_oa where syscode='EC' and islasttimes='1' and isremark='0' group by flowid) t,(select count(d.id) as count,d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in("+workflowId+") and b.creater <> 1 and b.createdate>='2018-02-23' and isremark not in(2,4) and islasttimes=1 and b.currentnodetype<3 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid) t1 where t.flowid=t1.requestid and t.count<>t1.count  "+
				   " union all "+
				   " select t.flowid from  (select flowid,count(1) as count from ofs_todo_data@dblink_oa where syscode='EC' and islasttimes='1' and isremark='0' group by flowid) t, workflow_requestbase t1 where t.flowid=t1.requestid  and t1.currentnodetype<3 and t1.workflowid not in("+workflowId+") and t1.creater <> 1 and t1.createdate>='2018-02-23' and flowid not in (select d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in("+workflowId+")  and b.creater <> 1 and isremark not in(2,4) and islasttimes=1 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid)  "+
				   " union all "+
				   " select to_char(t.requestid )as flowid from (select count(d.id) as count,d.requestid from workflow_currentoperator d ,workflow_requestbase b  where  d.requestid=b.requestid and b.workflowid   not in("+workflowId+")  and b.currentnodetype<3 and b.creater <> 1 and b.createdate>='2018-02-23' and isremark not in(2,4) and islasttimes=1 and (select count(1) from workflow_currentoperator c  where c.id=d.id and c.takisremark = -2 and exists(select 1 from workflow_currentoperator where requestid=c.requestid and takisremark=2 and islasttimes=1 and isremark not in(2,4)  ))=0 group by d.requestid) t where t.requestid not in(select flowid from ofs_todo_data@dblink_oa where syscode='EC' and islasttimes='1' and isremark='0' group by flowid)  ";
		rs.executeSql(sql);
		while(rs.next()){
			requestid = Util.null2String(rs.getString("flowid"));
			if(!checkPersonStatus(requestid)){
				continue;
			}
			sql_dt="delete from ofs_todo_data where syscode='EC' and flowid="+requestid;
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
	
	public String getWorkFlowId(){
		RecordSet rs = new RecordSet();
		String workflowid="";
		String flag = "";
		String sql="select workflow from uf_ECPortBpmExclude";
		rs.executeSql(sql);
		while(rs.next()){
			if("".equals(workflowid)){
				workflowid = Util.null2String(rs.getString("workflow"));				
			}else{
				workflowid = workflowid + flag + Util.null2String(rs.getString("workflow"));
			}
			flag=",";
		}
		if("".equals(workflowid)){
			workflowid = "-1";
		}
		return workflowid;
	}
}
