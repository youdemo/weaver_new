package gvo.costcontrol.purchase.order;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 费用采购订单操作中间表
 * @author tangj
 *
 */
public class PoBudgetEditActionFY implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String mainID = "";
		log.writeLog("PoBudgetEditActionFY------");
		String purrequest = "";//明细 采购申请
		String reqproject = "";//明细 请求项目
		String ysdjje = "";//预算冻结金额
		String tableName = "";
		String mxid = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("ID"));
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			mxid = Util.null2String(rs.getString("id"));
			purrequest = Util.null2String(rs.getString("purrequest"));
			reqproject = Util.null2String(rs.getString("reqproject")).replaceAll("^(0+)", "");
			ysdjje = Util.null2String(rs.getString("ysdjje"));
			if("".equals(ysdjje)){
				ysdjje = "0";
			}
			if(!"".equals(purrequest)&&!"".equals(reqproject)){
				updatePrBudgetTable(requestid,purrequest,reqproject,ysdjje,mxid);
			}
		}
		
		
		
		return SUCCESS;
	}
	
	public void updatePrBudgetTable(String requestid,String cgsqdh,String mxhid,String ysdjje,String mxid){
		RecordSet rs = new RecordSet();
		String modeid=getModeId("uf_pr_budget");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String amount="0";
		String cdbm = "";
		String yskm = "";
		String qj = "";
		String type = "";
		String gsdm = "";
		String sql="select 0-nvl(sum(nvl(je,0)),0) as amount from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid='"+mxhid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			amount = Util.null2String(rs.getString("amount"));
		}
	
		sql="select * from uf_pr_budget where cgsqdh='"+cgsqdh+"' and mxhid='"+mxhid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			cdbm = Util.null2String(rs.getString("cdbm"));
			yskm = Util.null2String(rs.getString("yskm"));
			qj = Util.null2String(rs.getString("qj"));
			type = Util.null2String(rs.getString("type"));
			gsdm = Util.null2String(rs.getString("gsdm"));
		}
		if(!"0".equals(amount)){
			sql="insert into uf_pr_budget (lcid,cgsqdh,mxhid,cdbm,yskm,qj,je,type,gsdm,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid,mxid)"
					+ " values('"+requestid+ "','"+cgsqdh+"','"+ mxhid+ "','"+ cdbm+ "','"
					+ yskm + "','" + qj + "','" + amount + "','"+type+"','"+gsdm+"','"+now+"','1','0','"+modeid+"','"+mxid+"') ";
			rs.executeSql(sql);		
			String prid="";
			sql = "select id from uf_pr_budget where lcid='"+ requestid + "' and mxhid='"+mxhid+"' and je='"+amount+"' order by id desc ";
			rs.executeSql(sql);
			if (rs.next()) {
				prid = Util.null2String(rs.getString("id"));
			}
			if (!"".equals(prid)) {
				ModeRightInfo ModeRightInfo = new ModeRightInfo();
				ModeRightInfo.editModeDataShare(
						Integer.valueOf("1"),
						Util.getIntValue(modeid),
						Integer.valueOf(prid));
			}
		}
		
	}
	
	public String getModeId(String tableName){
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"+tableName+"'";
		rs.executeSql(sql);
		if(rs.next()){
			formid = Util.null2String(rs.getString("id"));
		}
		sql="select id from modeinfo where  formid="+formid;
		rs.executeSql(sql);
		if(rs.next()){
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
}
