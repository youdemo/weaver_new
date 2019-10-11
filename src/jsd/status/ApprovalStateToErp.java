package jsd.status;
import weaver.conn.RecordSet;
/**
 * 审批状态传erp
 * @author 张瑞坤   20181105
 *
 */
public class ApprovalStateToErp {
	public String ApprovalState(String requestid,String flag){
		GetInfo gi = new GetInfo();
		if(flag.equals("FI-HT-01")){//合同付款审批''
			
			String workflowid = "65";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-YF-01")){//预付款申请
			
			String workflowid = "83";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-YF-02")){//应付款申请
			
			String workflowid = "82";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-KP-01")){//开票收款审批流程

			String workflowid = "80";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-ZZ-01")){//制造单号申请-标准件	

			String workflowid = "94";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-ZZ-02")){// 制单--标准件修改

			String workflowid = "121";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-ZZ-03")){//制造单号申请-设备

			String workflowid = "119";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-ZZ-04")){//制单--设备修改

			String workflowid = "118";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-ZC-01")){//资产转移申请

			String workflowid = "120";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("FI-SB-01")){//设备维修报废

			String workflowid = "122";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-SW-01")){//庶务类请购单

			String workflowid = "110";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-CG-01")){//采购单

			String workflowid = "111";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-FX-01")){//返修采购单

			String workflowid = "112";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-KK-01")){//采购扣款单

			String workflowid = "113";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-GY-01")){//新增供应商申请表（财务）

			String workflowid = "117";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-JY-01")){//物品借用申请单

			String workflowid = "101";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-JY-02")){//借用采购单

			String workflowid = "114";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("PR-JY-03")){//借用退货申请单

			String workflowid = "115";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("HR-QJ-01")){//请假申请
			
			String workflowid  = "102";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("HR-XJ-01")){//销假申请
			
			String workflowid  = "98";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("HR-JB-01")){//加班申请
			
			String workflowid  = "99";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("HR-QK-01")){//签卡申请
			
			String workflowid  = "105";
			return gi.getInfos(workflowid, requestid);
			
		}else if(flag.equals("HR-CC-01")){//出差申请
			
			String workflowid  = "97";
			return gi.getInfos(workflowid, requestid);
			
		}
		return "[{\"nodename\":\"\",\"approvalDep\":\"\",\"approvalPeo\":\"\",\"approvalTim\":\"\",\"arriveTim\":\"\",\"approvalSta\":\"\",\"remark\":\"\"}]";
	}

}
