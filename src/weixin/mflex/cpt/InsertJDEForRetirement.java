package weixin.mflex.cpt;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.conn.RecordSetDataSource;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertJDEForRetirement implements Action{

	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();// 请求ID
		String workflowID = info.getWorkflowid();// 流程ID
		String sql = "";
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		log.writeLog("start InsertJDEForRetirement");
		RecordSetDataSource rsd = new RecordSetDataSource("JDEpy");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String nowtime = sdf.format(new Date());
		String tableName = "";
		String mainId = "";
		String fxadd0 = "";//文件号
		String fxccid = "";//单据id
		String fxemal = "";//设备编号
		String fxa311 = "";//新置楼层
		String fxa312 = "";//新置地点
		String fxfil3 = "";//工号
		String fxgptn = "";//导入时间日期
		String fxmcu = "";//成本中心
		String fxfy = "";//年度
		String fxpn = "";//期间
		String fxtody = "";//天
		sql = " select tablename from workflow_bill where id in (select formid from workflow_base where id = "
				+ workflowID + ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			mainId = Util.null2String(rs.getString("id"));
			String CreateBy="";
			try {
				 CreateBy = new ResourceComInfo().getLoginID(Util.null2String(rs.getString("CreateBy")));
			} catch (Exception e) {
				log.writeLog(e);
			}
			fxadd0="FA-"+CreateBy+"-"+nowtime;
			fxmcu = Util.null2String(rs.getString("centercode"));
			
		}
		sql="select * from "+tableName+"_dt1 where mainid="+mainId;
		rs.executeSql(sql);
		while(rs.next()){
			fxemal=fxemal+Util.null2String(rs.getString("AssetNumber"))+";  ";
			fxa311=fxa311+Util.null2String(rs.getString("NewFloor"))+";  ";
			fxa312=fxa312+Util.null2String(rs.getString("NewLocation"))+";  ";
			fxfil3=fxfil3+Util.null2String(rs.getString("StaffNO"))+";  ";
		}
		fxemal=fxemal+"0";
		fxa311=fxa311+"0";
		fxa312=fxa312+"0";
		fxfil3=fxfil3+"0";
		fxfy = nowtime.substring(2, 4).replaceAll("^(0+)", "");
		fxpn = nowtime.substring(4, 6).replaceAll("^(0+)", "");
		fxtody = nowtime.substring(6, 8).replaceAll("^(0+)", "");
		sql="select to_char(to_date(createdate||createtime,'yyyy-mm-ddhh24:mi:ss'),'yyyymmddhh24miss') as fxgptn from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			fxgptn = Util.null2String(rs.getString("fxgptn"));
		}
		
		sql="select nvl(max(fxccid),0)+1 as fxccid from F5912001";
		rsd.executeSql(sql);
		if(rsd.next()){
			fxccid = Util.null2String(rsd.getString("fxccid"));
		}
	
		sql="insert into F5912001(fxadd0,fxccid,fxemal,fxa311,fxa312,fxfil3,fxgptn,fxmcu,fxfy,fxpn,fxtody) " +
				"values('"+fxadd0+"','"+fxccid+"','"+fxemal+"','"+fxa311+"','"+fxa312+"','"+fxfil3+"','"+fxgptn+"','"+fxmcu+"','"+fxfy+"','"+fxpn+"','"+fxtody+"')";
		log.writeLog("insert sql:"+sql);
		rsd.executeSql(sql);
		return SUCCESS;
	}
	
	

}
