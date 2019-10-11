package TaiSon.kq;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.interfaces.schedule.BaseCronJob;

/**
 * 创建漏打卡流程
 * @author tangj
 *
 */
public class CreateLDKFlowAction extends BaseCronJob{
	
	
	public void execute(){
		writeLog("开始创建漏打卡流程");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String endDate =TimeUtil.dateAdd(sf.format(new Date()).substring(0, 7)+"-01", -1);
		String startDate = endDate.substring(0, 7)+"-01";
		createLDK(startDate,endDate);
		writeLog("创建漏打卡流程结束");
	}
	public void createLDK(String startDate,String endDate) {
		
		KqUtil kq = new KqUtil();
		RecordSet rs = new RecordSet();
		String ryid = "";
		kq.insertLdkData(startDate, endDate);
		String sql = "select distinct ryid from uf_kq_ldk_mid";
		rs.executeSql(sql);
		while(rs.next()) {
			ryid = Util.null2String(rs.getString("ryid"));
			try {
				createFlow(ryid);
			} catch (Exception e) {
				writeLog(e);
				e.printStackTrace();
			}
		}
		
	}
	
	public void createFlow(String ryid) throws Exception {
		String workflowid = Util.null2o(weaver.file.Prop.getPropValue("ldkconfig", "workflowid"));
		RecordSet rs = new RecordSet();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		String departmentid = "";
		String subcompanyid1 = "";
		JSONObject json = new JSONObject();
		JSONObject header = new JSONObject();
		JSONObject details = new JSONObject();
		json.put("HEADER", header);
		json.put("DETAILS", details);
		header.put("lddkz",ryid );
		header.put("sqrq", now);
		String sql = "select departmentid,subcompanyid1 from hrmresource where id="+ryid;
		rs.executeSql(sql);
		if(rs.next()) {
			departmentid = Util.null2String(rs.getString("departmentid"));
			subcompanyid1 = Util.null2String(rs.getString("subcompanyid1"));
		}
		header.put("bm", departmentid);
		header.put("ssgs", subcompanyid1);
		JSONArray dt1 = new JSONArray();
		sql = "select * from uf_kq_ldk_mid where ryid='"+ryid+"'";
		rs.executeSql(sql);
		while(rs.next()){
			String rq = Util.null2String(rs.getString("rq"));
			String type = Util.null2String(rs.getString("type"));
			String startTime = "";
			String endTime = "";
			if("0".equals(type)) {
				startTime = "08:30";
				endTime = "12:00";
			}else {
				startTime = "13:30";
				endTime = "17:30";
			}
			JSONObject jo = new JSONObject();
			jo.put("ldjkrq", rq);
			jo.put("ldkjd", type);
			jo.put("ldkkssj", startTime);
			jo.put("ldkjssj", endTime);
			jo.put("ldkjd", type);
			jo.put("ldkjsrq", rq);
			dt1.put(jo);
			
		}
		details.put("DT1", dt1);
		AutoRequestService ars = new AutoRequestService();
		String result = ars.createRequest(workflowid, json.toString(), ryid, "0");
		writeLog("result:ryid="+ryid+" result="+result);
	}
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }

}
