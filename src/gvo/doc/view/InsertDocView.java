package gvo.doc.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import gvo.doc.pdf.WorkflowToPdf;
import gvo.util.xml.Head;
import gvo.util.xml.SaxXmlUtil;
import gvo.wsclient.doc.sendZDResultToDoc;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class InsertDocView extends BaseCronJob{
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("文档集成定时开始");
		doservice();
		log.writeLog("文档集成定时结束");
	}
	
	public void doservice() {
		WorkflowToPdf wtp = new WorkflowToPdf();
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		sendZDResultToDoc  szdr = new sendZDResultToDoc();
		SaxXmlUtil saxXmlUtil = new SaxXmlUtil();
		PurXmlUtil tran = new PurXmlUtil();
		String sql = "";
		String sql_dt = "";
		String oaaddress = "";//oa地址
		String billid = "";
		String rqid = "";//流程id
		String pdfid = "";//表单文件id
		String typecode = "";
		String pdfmlid = "";//pdf文件目录
		String workflowid = "";
		String sfzx = "";//是否执行
		String esbresult = "";//中间表接口执行标识
		String sign = "";
		String retmsg = "";
		String workflowName = "";//流程名称
		String lastoperatedate = "";//最后操作时间
		String modeId = getModeId("uf_doc_oa_file");
		sql = "select oaaddress from uf_doc_oaadress where rownum=1";
		rs.execute(sql);
		if(rs.next()) {
			oaaddress = Util.null2String(rs.getString("oaaddress"));
		}
		if("".equals(oaaddress)) {
			log.writeLog("OA地址没维护执行失败");
			return;
		}
		sql="select id,rqid,sfzx,esbresult from uf_doc_esb_mid where nvl(sfzx,'E') <> 'S' or nvl(esbresult,'E') <>'S'";
		rs.executeSql(sql);
		while(rs.next()) {
			sign = "";
			retmsg = "";
			billid = Util.null2String(rs.getString("id"));
			rqid = Util.null2String(rs.getString("rqid"));
			sfzx = Util.null2String(rs.getString("sfzx"));
			esbresult = Util.null2String(rs.getString("esbresult"));
			sql_dt= "select typecode,pdfmlid,a.workflowid,lastoperatedate from workflow_requestbase a,uf_doc_flow_type b where a.workflowid=b.typeid and a.requestid="+rqid;
			log.writeLog(sql_dt);
			rs_dt.executeSql(sql_dt);
			if(rs_dt.next()) {
				typecode = Util.null2String(rs_dt.getString("typecode"));
				pdfmlid = Util.null2String(rs_dt.getString("pdfmlid"));
				workflowid = Util.null2String(rs_dt.getString("workflowid"));
				lastoperatedate = Util.null2String(rs_dt.getString("lastoperatedate"));
			}
			if("".equals(typecode)||"".equals(pdfmlid)) {
				log.writeLog("文档流程类型配置 不存在 rqid:"+rqid);
				continue;
			}
			sql_dt="select workflowname from workflow_base where id='"+workflowid+"'";
			rs_dt.execute(sql_dt);
			if(rs_dt.next()) {
				workflowName = Util.null2String(rs_dt.getString("workflowname"));
			}
			if(!"S".equals(sfzx)) {
				pdfid = wtp.createPdf(rqid, oaaddress,pdfmlid);
				if("ZD".equals(typecode)) {
					insertZDView(rqid,workflowid,pdfid,oaaddress,modeId);
				}else if("ZSWD".equals(typecode)) {
					insertZSWDView(rqid,workflowid,pdfid,oaaddress,modeId);
				}
			}
			if(!"S".equals(esbresult)) {
				if("ZD".equals(typecode)||"ZSWD".equals(typecode) ) {
					String datainfo = "{\"ProcessType\":\""+workflowName+"\",\"ProcessEndDate\":\""+lastoperatedate+"\",\"ProcessCode\":\""+rqid+"\"}";
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
					String time = sdf.format(new Date());
					Head head1 = new Head("DMC.VXG-105_" + time, "1", "OA", "1", "userSAP", "P@ss0rd", "", "");
					String json = tran.javaToXml(datainfo.toString(), "", rqid, "",head1);
					log.writeLog("sendZD json:"+json);
					String sendResult=szdr.getwebserviceResult(json);
					if("".equals(sendResult)) {
						sign = "E";
						retmsg = "调用失败";
					}else {
						  Map<String, Object> getxmlmap = SaxXmlUtil.getXmlMap(sendResult);
							//Object SIGN = (String) getxmlmap.get("SIGN");
						  sign = (String) getxmlmap.get("SIGN");
						  String E_MESSAGE = (String) getxmlmap.get("Message");
						  retmsg = saxXmlUtil.getResult("retMsg", E_MESSAGE);
						  log.writeLog("sendZD result:sign "+sign+" retmsg "+retmsg);
					}
				}
			}
			//调用esb接口
			sql_dt = "update uf_doc_esb_mid set sfzx='S',esbresult='"+sign+"',description='"+retmsg+"' where id="+billid;
			log.writeLog(sql_dt);
			rs_dt.executeSql(sql_dt);
			
		}
	}
	
	/**
	 * 制度文件插入视图
	 * @param requestid
	 * @param workflowid
	 * @param pdfid
	 * @param oaaddress
	 * @param modeId
	 */
	public void insertZDView(String requestid,String workflowid,String pdfid,String oaaddress,String modeId) {
		RecordSet rs = new RecordSet();
		String tableName = "";
		String zdwj = "";//制度文件
		String bdwj = "";//表单文件
		String xgfj= "";//相关附件
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			zdwj = Util.null2String(rs.getString("zdwj"));
			bdwj = Util.null2String(rs.getString("bdwj"));
			xgfj = Util.null2String(rs.getString("fj"));
		}
		if(!"".equals(zdwj)) {
			insertView(requestid,zdwj,"0",modeId,oaaddress);
			
		}
		if(!"".equals(bdwj)) {
			insertView(requestid,bdwj,"1",modeId,oaaddress);
			
		}
		if(!"".equals(xgfj)) {
			insertView(requestid,xgfj,"2",modeId,oaaddress);
			
		}
		if(!"".equals(pdfid)) {
			insertView(requestid,pdfid,"3",modeId,oaaddress);		
		}
	}
	/**
	 * 知识文档插入视图
	 * @param requestid
	 * @param workflowid
	 * @param pdfid
	 * @param oaaddress
	 * @param modeId
	 */
	public void insertZSWDView(String requestid,String workflowid,String pdfid,String oaaddress,String modeId) {
		RecordSet rs = new RecordSet();
		String tableName = "";
		String xgfj= "";//相关附件
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			xgfj = Util.null2String(rs.getString("fj"));
		}
		
		if(!"".equals(xgfj)) {
			insertView(requestid,xgfj,"2",modeId,oaaddress);
			
		}
		if(!"".equals(pdfid)) {
			insertView(requestid,pdfid,"3",modeId,oaaddress);		
		}
	}
	
	/**
	 * 插入文档视图表
	 * @param requestid 流程id
	 * @param docids 文档id
	 * @param type 文档类型
	 * @param modeId 表单建模id
	 * @param oaaddress oa地址
	 */
	public void insertView(String requestid,String docids,String type,String modeId,String oaaddress) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		String billid = "";
		String sql = "insert into uf_doc_oa_file(rqid,type,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid,imagefileid,fileid,docid,fileurl,createdate,version,createrid,creatercode,creatername,filename) " + 
				"select '"+requestid+"','"+type+"',to_char(sysdate,'yyyy-mm-dd'),'1','0','"+modeId+"',a.imagefileid,a.id as fileid,a.docid,'"+oaaddress+"/gvo/dmc/downloaddoc.jsp?fileid='||imagefileid as fileurl,b.doccreatedate,a.versionid,b.doccreaterid as createrid,"
				+ "(select workcode from hrmresource where id=doccreaterid) as creatercode,case when doccreaterid = '1' then '系统管理员' else (select lastname from hrmresource where id=doccreaterid) end as creatername,a.imagefilename from docimagefile a,docdetail b where a.docid=b.id and a.docid in ("+docids+")";
		rs.executeSql(sql);
		log.writeLog(sql);
		sql="select id from uf_doc_oa_file where rqid='"+requestid+"'";
		rs.executeSql(sql);
		while(rs.next()) {
			billid = Util.null2String(rs.getString("id"));
			ModeRightInfo.editModeDataShare(
					Integer.valueOf("1"),
					Util.getIntValue(modeId),
					Integer.valueOf(billid));
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
