package gvo.cowork;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;

public class AddContactInfo {
	public void addInfo(String attach,String creater,String info) {
		Map<String, String> map = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm:ss");
		String nowDate = dateFormate.format(new Date());
		String nowTime = timeFormate.format(new Date());
		InsertUtil iu = new InsertUtil();
		if("".equals(info)||"".equals(creater)) {
			return;
		}
		String modeid = getModeId("uf_co_contact");
		map.put("fj", attach);
		map.put("tjr", creater);
		map.put("nr", info);
		map.put("rjrq", nowDate);
		map.put("tjsj", nowTime.substring(0,5));
		map.put("modedatacreatedate", nowDate);
		map.put("modedatacreatetime", nowTime);
		map.put("modedatacreater", creater);
		map.put("modedatacreatertype", "0");
		map.put("formmodeid", modeid);
		iu.insert(map, "uf_co_contact");
		String billid="";
		String sql = "select max(id) as billid from uf_co_contact where tjr='"+ creater + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			billid = Util.null2String(rs.getString("billid"));
		}
		if (!"".equals(billid)) {
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(
					Integer.valueOf("1"),
					Util.getIntValue(modeid),
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
