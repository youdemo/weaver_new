package testdemo;

import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class TestAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String workflowid = info.getWorkflowid();
		info.getRequestManager().setMessagecontent("123123213");    
		info.getRequestManager().setMessageid("错误信息提示"); 
		return FAILURE_AND_CONTINUE;
	}

}
