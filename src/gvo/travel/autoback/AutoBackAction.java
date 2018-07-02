package gvo.travel.autoback;

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

public class AutoBackAction implements Action {
	BaseBean log = new BaseBean();
	/**
	 * oa与ec出差申请（含借款）流程对接
	 * 
	 * @author daisy
	 * @version 1.0 2017-11-20
	 **/

	public String execute(RequestInfo info) {
		log.writeLog("进入出差申请（含借款）流程AutoBackAction————————");
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String ecrqid2="";//
		String mainID="";//
		String sql = "";
		RecordSet rs = new RecordSet();
		RecordSet res = new RecordSet();
		sql = " Select tablename From Workflow_bill Where id in ( Select formid From workflow_base Where id= " + workflowID + ")";

		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select id,ecrqid2 from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainID = Util.null2String(rs.getString("id"));
			ecrqid2 = Util.null2String(rs.getString("ecrqid2"));
			
		}
		if(!"".equals(ecrqid2)){
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
//			log.writeLog("requestid=" + ecrqid2);
			log.writeLog("开始————————" + json);
			AutoBackService back = new AutoBackService();
			String sign = "";
			String message = "";
			try {
				Response result = back.getResultMethod(json);
				sign = result.getSIGN();
				message = result.getMessage();
//				log.writeLog("sign=" + sign);
//				log.writeLog("message=" + message);
				SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
				String para = "MSG_CONTENT";
				String mess = saxXmlUtil.getResult(para, message);
				if("success".equals(mess)){
					deleteRequestInfo(ecrqid2);
				}
			} catch (Exception e) {
				log.writeLog("错误日志----" + e.getMessage());
				log.writeLog("oa流程"+requestid+" 自动退回EC流程"+ecrqid2+"调用接口失败");
			}
			log.writeLog("oa流程"+requestid+" 自动退回EC流程"+ecrqid2+"结果:"+message);
		}
			
		return SUCCESS;
	}
	public void deleteRequestInfo(String requestid){
		 RecordSetDataSource rsd = new RecordSetDataSource("EC");
		 String sql="";
		 sql="delete workflow_currentoperator where requestid ="+requestid;
		 rsd.executeSql(sql);
		 sql="delete workflow_form where requestid ="+requestid;
		 rsd.executeSql(sql);
		 sql="delete workflow_formdetail where requestid ="+requestid;
		 rsd.executeSql(sql);
		 sql="delete workflow_requestLog where requestid ="+requestid;
		 rsd.executeSql(sql);
		 sql="delete workflow_requestViewLog where id ="+requestid;
		 rsd.executeSql(sql);
		 sql="delete workflow_requestbase where requestid ="+requestid;
		 rsd.executeSql(sql);
	 }
}
