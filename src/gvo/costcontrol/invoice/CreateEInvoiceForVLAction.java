package gvo.costcontrol.invoice;

import gvo.costcontrol.util.InsertUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import sun.util.logging.resources.logging;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class CreateEInvoiceForVLAction implements Action {

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("start CreateEInvoiceForVLAction");
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		InsertUtil iu = new InsertUtil();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String sql_dt = "";
		String tableName = "";
		String mainID = "";
		String requestmark = "";// 流程编号
		String creater = "";
		String fphm = "";// 发票号码
		String dtid = "";
		String bs = "";// 标识
		String invoiceid = "";
		String dzfp = "";
		String modeid = "3121";
		String fpid = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select id from " + tableName + " where requestid=" + requestid;

		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
		}
		sql = "select requestmark,creater from workflow_requestbase where requestid="
				+ requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			requestmark = Util.null2String(rs.getString("requestmark"));
			creater = Util.null2String(rs.getString("creater"));
		}
		String dzfpall = "-1";
		String flag = ",";
		for (int i = 4; i <= 7; i++) {
			sql = "select * from " + tableName + "_dt" + i + " where mainid="
					+ mainID;
			rs.executeSql(sql);
			while (rs.next()) {
				fphm = Util.null2String(rs.getString("fphm"));
				dtid = Util.null2String(rs.getString("id"));
				dzfp = Util.null2String(rs.getString("dzfp"));
				if (!"".equals(fphm)) {
					if (!"".equals(dzfp)) {
						sql_dt = "update uf_e_invoice set fphm='" + fphm
								+ "',lcbh='" + requestmark + "' where id="
								+ dzfp;
						rs_dt.executeSql(sql_dt);
						dzfpall = dzfpall + flag + dzfp;
						flag = ",";
					} else {
						fpid = "";
						sql_dt = "select id from uf_e_invoice where fphm='"
								+ fphm + "'and xglc='" + requestid + "'";
						
						rs_dt.executeSql(sql_dt);
						if (rs_dt.next()) {
							fpid = Util.null2String(rs_dt.getString("id"));

						}
						if ("".equals(fpid)) {
							bs = requestid +"_"+i+ "_" + dtid;
							Map<String, String> mapStr = new HashMap<String, String>();
							mapStr.put("fphm", fphm);
							mapStr.put("lcbh", requestmark);
							mapStr.put("xglc", requestid);
							mapStr.put("bs", bs);
							mapStr.put("modedatacreatedate", now);
							mapStr.put("modedatacreater", creater);
							mapStr.put("modedatacreatertype", "0");
							mapStr.put("formmodeid", modeid);
							iu.insert(mapStr, "uf_e_invoice");
							invoiceid = "";
							sql_dt = "select id from uf_e_invoice where bs='"
									+ bs + "'";
							rs_dt.executeSql(sql_dt);
							if (rs_dt.next()) {
								invoiceid = Util.null2String(rs_dt
										.getString("id"));
							}
							if (!"".equals(invoiceid)) {
								ModeRightInfo ModeRightInfo = new ModeRightInfo();
								ModeRightInfo.editModeDataShare(
										Integer.valueOf(creater),
										Util.getIntValue(modeid),
										Integer.valueOf(invoiceid));
								sql_dt = "update " + tableName + "_dt" + i
										+ " set dzfp='" + invoiceid
										+ "' where id=" + dtid;
								rs_dt.executeSql(sql_dt);
								dzfpall = dzfpall + flag + invoiceid;
								flag = ",";
							}
						} else {
							dzfpall = dzfpall + flag + fpid;
							flag = ",";
						}
					}
				}
			}

		}
		sql = "delete from uf_e_invoice where id not in(" + dzfpall
				+ ") and xglc=" + requestid;
		rs.executeSql(sql);
		return SUCCESS;
	}
}
