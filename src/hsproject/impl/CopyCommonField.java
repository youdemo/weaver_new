package hsproject.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 新建项目类型时复制通用字段进去自定义字段
 * @author tangjianyong 2018/01/22
 *
 */
public class CopyCommonField implements Action {
	
	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		RecordSet rs = new RecordSet();
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		String tableName2 = "uf_project_field";
		String modeId2 = "";
		String tableName = "";
		String creater = "";
		String fieldid="";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			creater = Util.null2String(rs.getString("creater"));
		}
		sql = "select b.id from workflow_bill a,modeinfo b where a.id=b.formid and  a.tablename='"
				+ tableName2 + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			modeId2 = Util.null2String(rs.getString("id"));
		}
		sql="insert into uf_project_field(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,prjtype,description,fieldname,showname,fieldtype,texttype,buttontype,textlength,floatdigit,selectbutton,groupinfo,isused,ismust,dsporder,isedit,isreadonly,iscommon ) (select '"+modeId2+"','"+creater+"','0','"+nowDate+"','"+nowTime+"', '"+billId+"',description,fieldname,showname,fieldtype,texttype,buttontype,textlength,floatdigit,selectbutton,groupinfo,isused,ismust,dsporder,isedit,isreadonly,'0' from uf_prj_common_field )";
		rs.executeSql(sql);
		
		sql="select id from uf_project_field where prjtype ='"+billId+"'";
		rs.executeSql(sql);
		while(rs.next()){
			fieldid = Util.null2String(rs.getString("id"));
			ModeRightInfo.editModeDataShare(Integer.valueOf(creater), Integer.valueOf(modeId2),
					Integer.valueOf(fieldid));
		}
		
		
		return SUCCESS;
	}

}
