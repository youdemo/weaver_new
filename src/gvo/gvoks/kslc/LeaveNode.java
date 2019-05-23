package gvo.gvoks.kslc;

import gvo.gvoks.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2019-4-8 上午9:47:10
 * 离开节点 更新记录表
 */
public class LeaveNode implements Action{

	@Override
	public String execute(RequestInfo info) {
		GetUtil gu = new GetUtil();
		RecordSet rs = new RecordSet();
		String  requestid = info.getRequestid();
		int nodeid = info.getRequestManager().getNodeid();
		String endTime = gu.getNowdate();
		String tablename = "uf_ksclsj";
		String sql = "update  "+ tablename + " set lcjd = '" + nodeid + "',lcjssj = '" + endTime + "' ,lcjsbs = '1' where rid = '" + requestid +  "' and lcjsbs = '0'";
		rs.executeSql(sql);
		return SUCCESS;
	}

}
