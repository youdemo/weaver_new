package morningcore.contract.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import morningcore.contract.bean.ChangeFieldBean;
import morningcore.contract.dao.ChangeFieldDao;
import morningcore.contract.util.InsertUtil;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class UpdateSupplierInfo implements Action{

	@Override
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		log.writeLog("开始插入历史记录");
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String sql_dt = "";
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		String mainid = "";
		String sqr = "";
		String xzkhmc = "";
		String tableName = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from "+tableName+" where requestid="+requestid;
		rs.execute(sql);
		if(rs.next()) {
			mainid = Util.null2String(rs.getString("id"));	
			sqr = Util.null2String(rs.getString("SQRBH"));	
			xzkhmc = Util.null2String(rs.getString("gyszsj"));	
		}
		sql = "select sysno,bgzd,oldvalue,newvalue from "+tableName+"_dt1  where mainid="+mainid;
		//log.writeLog("sql:"+sql);
		rs.execute(sql);
		while(rs.next()) {
			String seqno = Util.null2String(rs.getString("sysno"));
			String bgzd = Util.null2String(rs.getString("bgzd"));
			String oldshowvalue = Util.null2String(rs.getString("oldvalue"));
			String newshowvalue = Util.null2String(rs.getString("newvalue"));
			String fieldname = "";
			String oldvalue = "";
			String newvalue = "";
			sql_dt = "select fieldname,oldvalue,newvalue from uf_change_detail where seqno='"+seqno+"'";
			rs_dt.execute(sql_dt);
			if(rs_dt.next()) {
				newvalue = Util.null2String(rs_dt.getString("newvalue"));
				oldvalue = Util.null2String(rs_dt.getString("oldvalue"));
				fieldname = Util.null2String(rs_dt.getString("fieldname"));
			}
			Map<String, String> map = new HashMap<String, String>();
			map.put("changetype","1");
			map.put("bgzd",bgzd);
			map.put("seqno",seqno);
			map.put("fieldname",fieldname);
			map.put("oldvalue",oldvalue);
			map.put("newvalue",newvalue);
			map.put("oldshowvalue",oldshowvalue);
			map.put("newshowvalue",newshowvalue);
			map.put("rqid",requestid);
			map.put("customid",xzkhmc);
			map.put("bgr",sqr);
			map.put("bgrq",nowDate);
			map.put("bgsj",nowTime);
			InsertUtil iu = new InsertUtil();
			iu.insert(map, "uf_change_history");
			ChangeFieldDao cfd = new ChangeFieldDao();
			ChangeFieldBean cfb = cfd.getChangeFieldBean(bgzd);
			String dbfieldname = cfb.getFieldname();
			sql_dt = "update uf_fyszsj set "+dbfieldname+"=(select newvalue from uf_change_detail where seqno='"+seqno+"') where id="+xzkhmc;
			log.writeLog("sql_dt:"+sql_dt);
			rs_dt.execute(sql_dt);
		}
		
		
		return SUCCESS;
	}

}
