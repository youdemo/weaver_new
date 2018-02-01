package hsproject.impl;

import hsproject.dao.AlterTableFieild;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 增加项目过程通用字段
 * @author tangjianyong
 *
 */
public class AddProcessCommonField implements Action {

	@Override
	public String execute(RequestInfo info) {
		String modeId = info.getWorkflowid();
		String billId = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String tableName2 = "uf_prj_porcessfield";
		String alterTableName = "hs_prj_process";
		String modeId2 = "";
		String typeId = "";
		String processid="";
		String fieldname = "";
		String creater = "";
		String fieldtype = "";
		String texttype = "";
		String textlength = "";
		String floatdigit = "";
		String sql = "select b.tablename from modeinfo a,workflow_bill b where a.formid=b.id and a.id="
				+ modeId;
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where id=" + billId;
		rs.executeSql(sql);
		if (rs.next()) {
			fieldtype = Util.null2String(rs.getString("fieldtype"));
			fieldname=Util.null2String(rs.getString("fieldname"));
			texttype = Util.null2String(rs.getString("texttype"));
			textlength = Util.null2String(rs.getString("textlength"));
			floatdigit = Util.null2String(rs.getString("floatdigit"));
			creater = Util.null2String(rs.getString("modedatacreater"));
		}
		AlterTableFieild atf = new AlterTableFieild();
		atf.addTableField(alterTableName, fieldname, fieldtype, texttype, textlength, String.valueOf(Util.getIntValue(floatdigit,-1)+1));
		sql = "select b.id from workflow_bill a,modeinfo b where a.id=b.formid and  a.tablename='"
				+ tableName2 + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			modeId2 = Util.null2String(rs.getString("id"));
		}
		sql="select * from uf_prj_process order by id asc ";
		rs.executeSql(sql);
		while(rs.next()){
			processid = Util.null2String(rs.getString("id"));
			typeId = Util.null2String(rs.getString("prjtype"));
			insertField(processid,typeId,billId,modeId2,fieldname,creater);
		}
		
		return SUCCESS;
	}
	
	public void insertField(String processid,String typeid,String billid,String modeId2,String fieldname,String creater){
		ModeRightInfo ModeRightInfo = new ModeRightInfo();
		RecordSet rs = new RecordSet();
		String sql="";
		String fieldid = "";
		sql="insert into uf_prj_porcessfield(formmodeid,modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,projecttype,processtype,description,fieldname,showname,fieldtype,texttype,buttontype,textlength,floatdigit,selectbutton,groupinfo,isused,ismust,dsporder,isedit,isreadonly,iscommon ) (select '"+modeId2+"',modedatacreater,modedatacreatertype,modedatacreatedate,modedatacreatetime,'"+typeid+"','"+processid+"',description,fieldname,showname,fieldtype,texttype,buttontype,textlength,floatdigit,selectbutton,groupinfo,isused,ismust,dsporder,isedit,isreadonly,'0' from uf_prj_proc_comfld where id="+billid+")";
		rs.executeSql(sql);
		sql="select max(id) as id from uf_prj_porcessfield where projecttype ='"+typeid+"' and fieldname='"+fieldname+"' and processtype='"+processid+"' and iscommon='0'";
		rs.executeSql(sql);
		if(rs.next()){
			fieldid = Util.null2String(rs.getString("id"));
			ModeRightInfo.editModeDataShare(Integer.valueOf(creater), Integer.valueOf(modeId2),
					Integer.valueOf(fieldid));
		}
	}

}
