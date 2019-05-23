package gvo.ScheduleTask.doc;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import gvo.webservice.AutoRequestService;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class AutoCreateRuleRoute extends BaseCronJob{
	/*
	 * 根据SVD-018-B-制度文件审批流程-VXG流程中的申请日期，每半年之后自动触发相同的审批流程
	 */
	public void execute() {
		BaseBean log = new BaseBean();
		log.writeLog("AutoCreateSVD018Route start....");
		run1();
	}
	
	public void run1() {
		BaseBean log = new BaseBean();
		log.writeLog("AutoCreateSVD018Route start....");
		String sqr = "";//申请人
		String sqbm = "";//申请部门
		String zddw = "";//文件主导单位
		String sqrq = "";//申请日期
		String szfb = "";//所在分部
		String xdfs = "";//文件修订方式
		String bdsfgx = "";//表单是否有更新
		String fjsfgx = "";//附件是否有更新
		String wjmj = "";//文件密级
		String wjbh = "";//文件编号
		String wjmc = "";//文件名称
		String wjlx = "";//文件类型
		String fffw = "";//分发范围
		String ybb = "";//原版本
		String gghbb = "";//更改后版本
		String hsry = "";//会审人员
		String wjyyz = "";//文件拥有者
		String ggyy = "";//新版/更改原因
		String ggnr = "";//新版/更改内容
		String zdwj = "";//文件
		String bz = "";//备注
		String bdwj = "";//表单
		String fj = "";//附件
		String nsy = "";//内审员/部门文档管理员
		String hsb = "";//制度文件会审意见表
		String sql = "";
		try {
			JSONObject json = new JSONObject();
			JSONObject header = new JSONObject();
			JSONObject DETAILS = new JSONObject();
			json.put("HEADER", header);
			json.put("DETAILS",DETAILS);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date d = new Date();
			sqrq = sdf.format(d);
			header.put("sqrq", sqrq);
			RecordSet rs  = new RecordSet();
			String wfid = getWorkFlowID(rs, "6");
			AutoRequestService ars = new AutoRequestService();
			sql = " select * from formtable_main_445 a join workflow_requestbase b on a.requestid=b.requestid "
					+ " where b.currentnodetype='3' and a.sqrq=to_char(sysdate-180,'yyyy-mm-dd') ";
			rs.execute(sql);
			while(rs.next()){
				sqbm = Util.null2String(rs.getString("sqbm"));
				zddw = Util.null2String(rs.getString("zddw"));
				szfb = Util.null2String(rs.getString("szfb"));
				sqr = getJKR(rs, zddw, szfb);
				if("".equals(sqr)){
					continue;
				}
				xdfs = Util.null2String(rs.getString("xdfs"));
				bdsfgx = Util.null2String(rs.getString("bdsfgx"));
				fjsfgx = Util.null2String(rs.getString("fjsfgx"));
				wjmj = Util.null2String(rs.getString("wjmj"));
				wjbh = Util.null2String(rs.getString("wjbh"));
				wjmc = Util.null2String(rs.getString("wjmc"));
				wjlx = Util.null2String(rs.getString("wjlx"));
				fffw = Util.null2String(rs.getString("fffw"));
				ybb = Util.null2String(rs.getString("ybb"));
				gghbb = Util.null2String(rs.getString("gghbb"));
				hsry = Util.null2String(rs.getString("hsry"));
				wjyyz = Util.null2String(rs.getString("wjyyz"));
				ggyy = Util.null2String(rs.getString("ggyy"));
				ggnr = Util.null2String(rs.getString("ggnr"));
				zdwj = Util.null2String(rs.getString("zdwj"));
				bz = Util.null2String(rs.getString("bz"));
				bdwj = Util.null2String(rs.getString("bdwj"));
				fj = Util.null2String(rs.getString("fj"));
				nsy = Util.null2String(rs.getString("nsy"));
				hsb = Util.null2String(rs.getString("hsb"));
				header.put("sqbm", sqbm);
				header.put("szfb", szfb);
				header.put("sqr", sqr);
				header.put("zddw", zddw);
				header.put("xdfs", xdfs);
				header.put("bdsfgx", bdsfgx);
				header.put("fjsfgx", fjsfgx);
				header.put("wjmj", wjmj);
				header.put("wjbh", wjbh);
				header.put("wjmc", wjmc);
				header.put("wjlx", wjlx);
				header.put("fffw", fffw);
				header.put("ybb", ybb);
				header.put("gghbb", gghbb);
				header.put("hsry", hsry);
				header.put("wjyyz", wjyyz);
				header.put("ggyy", ggyy);
				header.put("ggnr", ggnr);
				header.put("zdwj", zdwj);
				header.put("bz", bz);
				header.put("bdwj", bdwj);
				header.put("fj", fj);
				header.put("nsy", nsy);
				header.put("hsb", hsb);
				String res = ars.createRequest(wfid, json.toString(), sqr, "0");
				log.writeLog("AutoCreateSVD018Route运行结果："+res);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//获取体系接口人
	public String getJKR(RecordSet rs,String bmid,String compid){
		String fqr = "";
		String sql = " select a.txjkr as jkr from uf_txjkr_vxg a "+
				" join hrmresource b on a.txjkr=b.id and b.status<3 " + 
				" where a.dept='"+bmid+"' ";
		rs.execute(sql);
		if(rs.next()) {
			fqr = Util.null2String(rs.getString("jkr"));
		}else {
			sql = " select nvl(a.dcc,c.bmjl) as jkr from uf_nsry a "+
					" join hrmresource b on a.dcc=b.id and b.status<3 " + 
					" left join matrixtable_4 c on a.dccdept=c.bm " +
					" join hrmresource d on c.bmjl=d.id and d.status<3 " +
					" where a.company='"+compid+"' ";
			rs.execute(sql);
			if(rs.next()){
				fqr = Util.null2String(rs.getString("jkr"));
			}
		}
		return fqr;
	}
	//获取流程workflow id
	public String getWorkFlowID(RecordSet rs,String id) {
		String res = "";
		String sql = " select workflowID from uf_cflcpzb where type='" + id + "' and status='0' ";
		rs.execute(sql);
		if (rs.next()) {
			res = rs.getString("workflowID");
		}
		return res;
	}
}
