package hhgd.meeting;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;
/**
 *@Description: 定时任务 删除取消会议关联的预订流程 
* @version: 
* @author: jianyong.tang
* @date: 2019年8月21日 下午4:19:07
 */
public class DeleteCancelMeetingRequest extends BaseCronJob{

	public void execute(){
		BaseBean log = new BaseBean();
		log.writeLog("删除会议流程开始");
		delCanelMeeting();
		log.writeLog("删除会议流程结束");
	}
	
	/**
	 * 处理取消状态的会议但还没删除流程的数据
	 */
	public void delCanelMeeting() {
		RecordSet rs = new RecordSet();
		String requestid = "";
		String sql = "select b.requestid from meeting a,workflow_requestbase b where a.requestid = b.requestid and a.meetingstatus=4";
		rs.execute(sql);
		while(rs.next()) {
			requestid = Util.null2String(rs.getString("requestid"));
			deleteRequest(requestid);
		}
	}
	/**
	 * 删除流程
	 * @param requestid
	 */
	public void deleteRequest(String requestid) {
		RecordSet rs = new RecordSet();
		if("".equals(requestid)) {
			return;
		}
		rs.execute("delete workflow_currentoperator where requestid="+requestid);
		rs.execute("delete workflow_form where requestid="+requestid);
		rs.execute("delete workflow_formdetail where requestid="+requestid);
		rs.execute("delete workflow_requestLog where requestid="+requestid);
		rs.execute("delete workflow_requestViewLog where id="+requestid);
		rs.execute("delete workflow_requestbase where requestid="+requestid);
	
	}
}
