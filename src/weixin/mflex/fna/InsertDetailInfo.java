package weixin.mflex.fna;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertDetailInfo implements Action {

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String now = df.format(new Date());
		String initiatorloc = "";// 申请人所在地
		String businessunit = "";// 成本中心
		String projecttitle = "";// Project Title
		String totalcost2 = "";// 项目总成本（含税）
		String ysbm = "";// 预算编码
		String projecttype = "";// 项目类别
		String sqr = "";// 申请人
		String sqbm = "";// 申请部门
		String cqcode = "";
		String cq = "";
		String account = "";
		String parcode = "";
		String ls = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			initiatorloc = Util.null2String(rs.getString("initiatorloc"));
			businessunit = Util.null2String(rs.getString("businessunit"));
			projecttitle = Util.null2String(rs.getString("projecttitle"));
			totalcost2 = Util.null2String(rs.getString("totalcost2"));
			ysbm = Util.null2String(rs.getString("ysbm"));
			projecttype = Util.null2String(rs.getString("projecttype"));
			sqr = Util.null2String(rs.getString("sqr"));
			sqbm = Util.null2String(rs.getString("sqbm"));
			ls = Util.null2String(rs.getString("ls"));
		}
		
//		sql = "select selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='"
//				+ tableName
//				+ "' and a.fieldname='initiatorloc' and c.selectvalue='"
//				+ initiatorloc + "'";
//		rs.executeSql(sql);
//		if (rs.next()) {
//			account = Util.null2String(rs.getString("selectname"));
//		}
		if ("0".equals(initiatorloc)) {
			cqcode = "410";
			cq="MCH";
		} else if ("1".equals(initiatorloc)) {
			cqcode = "401";
			cq="MFI";
		} else if ("2".equals(initiatorloc)) {
			cqcode = "405";
			cq="MSG";
		} else if ("3".equals(initiatorloc)) {
			cqcode = "418";
			cq="MFC";
		} else if ("4".equals(initiatorloc)) {
			cqcode = "420";
			cq="MYC";
		}
		GetSeqNum gsn = new GetSeqNum();
		parcode=gsn.getNum(cq, tableName, 3, now.substring(2, 4));
		ls=parcode;
		account = cqcode + ".1980.02";
		sql = "update " + tableName + " set ls=" + ls + " where requestid="
				+ requestid;
		rs.executeSql(sql);
		sql = "insert into "
				+ tableName
				+ "_dt1(mainid,Account,date1,ParCode,BU,Description,Amount,Owner,Department,BudgetNo,ProjectType)values("
				+ mainID + ",'" + account + "','" + now + "','" + parcode
				+ "','" + businessunit + "','" + projecttitle + "','"
				+ totalcost2 + "','" + sqr + "','" + sqbm + "','" + ysbm
				+ "','" + projecttype + "')";
		rs.executeSql(sql);
		return SUCCESS;
	}

	public String getStrNum(String value, int len) {
		String buff = value;
		int max = len - buff.length();
		for (int index = 0; index < max; index++) {
			buff = "0" + buff;
		}
		return buff;
	}
}
