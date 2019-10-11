package morningcore.ys;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

public class UpdateYSQX extends BaseCronJob{
	
	public void execute(){
		BaseBean log = new BaseBean();
		log.writeLog("开始更新预算数据权限");
		updateQx();
		log.writeLog("更新预算数据权限结束");
	}
	
	public void updateQx() {
		String modeid = getModeId("uf_yssjdr");
		RecordSet rs = new RecordSet();
		String sql = "select id,modedatacreater from uf_yssjdr where sfxyzxqx = '0'";
		rs.executeSql(sql);
		while(rs.next()){
			String billid = Util.null2String(rs.getString("id"));
			String creater = Util.null2String(rs.getString("modedatacreater"));
			 if (!"".equals(billid)) {
				 ModeRightInfo ModeRightInfo = new ModeRightInfo();
				 ModeRightInfo.editModeDataShare(Integer.valueOf(creater), Integer.valueOf(modeid),Integer.valueOf(billid));
			 }
		}
		sql = "update uf_yssjdr set sfxyzxqx='1' where sfxyzxqx = '0'";
		rs.execute(sql);
	}
	
	
	
	
	
	
	public String getModeId(String tableName) {
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='" + tableName + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			formid = Util.null2String(rs.getString("id"));
		}
		sql = "select id from modeinfo where  formid=" + formid;
		rs.executeSql(sql);
		if (rs.next()) {
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
	}
}
