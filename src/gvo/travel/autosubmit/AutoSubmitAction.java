package gvo.travel.autosubmit;

import gvo.travel.ECFI_WF_0_CreateRequestServiceECSoapBindingQSServiceStub.Response;
import gvo.util.xml.*;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AutoSubmitAction implements Action {
	BaseBean log = new BaseBean();

	/**
	 * oa与ec出差申请（含借款）流程对接
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-20
	 **/
	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程AutoSubmitAction————————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		RecordSetDataSource rsd = new RecordSetDataSource("EC");
		String tableName = "";
		String sql = "";
		String mainID = "";
		String ecrqid2 = "";
		String requestname="";
		String requestmark="";
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,ecrqid2 from "+tableName+" where requestid="+requestid;
		res.executeSql(sql);
		if(res.next()){
			mainID = Util.null2String(res.getString("id"));
			ecrqid2 = Util.null2String(res.getString("ecrqid2"));
			
		}
		sql="select requestname,requestmark from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			requestname = Util.null2String(rs.getString("requestname"));
			requestmark = Util.null2String(rs.getString("requestmark"));
		}
		if(!"".equals(ecrqid2)){
			String sql_ec="update workflow_requestbase set requestname='"+requestname+"',requestmark='"+requestmark+"' where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			sql_ec="update formtable_main_2 set sfyxtj='0' where requestid="+ecrqid2;
			rsd.executeSql(sql_ec);
			log.writeLog("开始————————json" + sql_ec);
			Data data = new Data();
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String time = sdf.format(date);
			Head head = new Head("EC.WF_HNYG-001_" + time, "1", "OA",
					"1", "userEC", "P@ss0rd",
					"", "");
			List<Head> heads = new ArrayList<Head>();
			ItemEsb itemEsb = new ItemEsb();
			if (!"".equals(requestid)) {
				itemEsb.setRequestId(ecrqid2);
			}

			List<ItemEsb> itemEsbs = new ArrayList<ItemEsb>();
			itemEsbs.add(itemEsb);
			ListEsb listEsb = new ListEsb();
			List<ListEsb> listEsbs = new ArrayList<ListEsb>();
			listEsbs.add(listEsb);
			listEsb.setITEM(itemEsbs);
			heads.add(head);
			head.setComments(requestid);
			data.setHeads(heads);
			data.setLIST(listEsbs);
			// 将对象转换成string类型的xml
			String json = XmlUtil.convertToXml(data);
			log.writeLog("开始————————json" + json);
			AutoSubmitService auto = new AutoSubmitService();
			String sign = "";
			String MESSAGE = "";
			try {
				Response result = auto.getResultMethod(json);
				sign = result.getSIGN();
			 	MESSAGE = result.getMessage();
			} catch (Exception e) {
				sql_ec="update formtable_main_2 set sfyxtj='1' where requestid="+ecrqid2;
				rsd.executeSql(sql_ec);
				log.writeLog("错误日志----" + e.getMessage());
				log.writeLog("oa流程"+requestid+" 自动提交EC流程"+ecrqid2+"调用接口失败");
			}
			SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
			String para = "MSG_CONTENT";
			String message = saxXmlUtil.getResult(para, MESSAGE);
			log.writeLog("oa流程"+requestid+" 自动提交EC流程"+ecrqid2+"结果:"+message);
			}
		return SUCCESS;
	}

}
