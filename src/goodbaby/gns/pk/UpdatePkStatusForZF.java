package goodbaby.gns.pk;

import weaver.conn.RecordSet;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

/**
 * 作废时更新排款状态，删除中间表数据
 * 
 * @author tangj
 *
 */
public class UpdatePkStatusForZF implements Action {

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String sql = "";
		sql = "update uf_payinternal set sfpk='1' where id in(select pkzjbid from uf_flow_payinternal where pklcid='"
				+ requestid + "')";
		rs.executeSql(sql);

		sql = "delete from uf_flow_payinternal where  pklcid='" + requestid + "'";
		rs.executeSql(sql);
		return SUCCESS;
	}

}
