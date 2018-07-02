package goodbaby.hr.rz;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertHrmresoure implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tablename = info.getRequestManager().getBillTableName();
		BaseBean log = new BaseBean();
		log.writeLog("InsertHrmresoure start.....");
		String SFZ = "";//身份证
		String RZRQ = "";//入职日期
		String SYQ = "";//试用期（月）
		String ZZRQ = "";//转正日期
		String BM = "";//部门
		String GW = "";//岗位
		String HTLX = "";//合同类型
		String HTSX = "";//合同属性
		String QYDW = "";//签约单位
		String HTQ = "";//合同期（月）
		String HTJSQ = "";//合同结束日期
		String ZPJH = "";//招聘计划
		String XQLC = "";//用人需求流程
		String BZ = "";//备注
		String KQBM = "";//考勤部门
		String KH = "";//卡号
		String KQBMLX = "";//考勤保密类型
		String YGBH = "";//员工编号
		String DJR = "";//登记人
		String wb = "";//文本
		String BMXY = "";//保密协议签订日期
		String xm1 = "";//姓名
		String GZDD = "";//工作地点
		String zjl = "";//总经理
		String HTQSRQ = "";//合同起始日期
		String zjsj = "";//直接上级
		String sql="select * from  "+tablename+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			SFZ = Util.null2String(rs.getString("SFZ"));
			RZRQ = Util.null2String(rs.getString("RZRQ"));
			ZZRQ = Util.null2String(rs.getString("ZZRQ"));
			BM = Util.null2String(rs.getString("BM"));
			GW = Util.null2String(rs.getString("GW"));
			HTLX = Util.null2String(rs.getString("HTLX"));
			HTSX = Util.null2String(rs.getString("HTSX"));
			QYDW = Util.null2String(rs.getString("QYDW"));
			HTQ = Util.null2String(rs.getString("HTQ"));
			HTJSQ = Util.null2String(rs.getString("HTJSQ"));
			ZPJH = Util.null2String(rs.getString("ZPJH"));
			XQLC = Util.null2String(rs.getString("XQLC"));
			BZ = Util.null2String(rs.getString("BZ"));
			KQBM = Util.null2String(rs.getString("KQBM"));
			KH = Util.null2String(rs.getString("KH"));
			KQBMLX = Util.null2String(rs.getString("KQBMLX"));
			YGBH = Util.null2String(rs.getString("YGBH"));
			DJR = Util.null2String(rs.getString("DJR"));
			wb = Util.null2String(rs.getString("wb"));
			BMXY = Util.null2String(rs.getString("BMXY"));
			xm1 = Util.null2String(rs.getString("xm1"));
			GZDD = Util.null2String(rs.getString("GZDD"));
			zjl = Util.null2String(rs.getString("zjl"));
			HTQSRQ = Util.null2String(rs.getString("HTQSRQ"));
			zjsj = Util.null2String(rs.getString("zjsj"));
		}
		String subcompanyid1 = "";
		sql="select subcompanyid1 from HrmDepartment where id="+BM;
		rs.executeSql(sql);
		if(rs.next()){
			subcompanyid1 = Util.null2String(rs.getString("subcompanyid1"));
		}
		int currentid = 0;
        int nextid = 0;

        sql = "select indexdesc,currentid from SequenceIndex where indexdesc='resourceid'";
        rs.executeSql(sql);
        if (rs.next()) {
          currentid = rs.getInt("currentid");
        }
        sql = "select max(id)+1 as nextid from hrmresource";
        rs.executeSql(sql);
        if (rs.next()) {
          nextid = rs.getInt("nextid");
        }
        if (currentid != nextid) {
          rs.executeSql("update SequenceIndex set currentid=" + nextid + " where indexdesc='resourceid'");
        }
		Map<String, String> hrmmap = new HashMap<String,String>();
		Map<String, String> fieldmap = new HashMap<String,String>();
		hrmmap.put("id", String.valueOf(nextid));
		hrmmap.put("certificatenum", SFZ);
		hrmmap.put("subcompanyid1", subcompanyid1);
		hrmmap.put("departmentid", BM);
		hrmmap.put("jobtitle", GW);
		hrmmap.put("enddate", HTJSQ);
		hrmmap.put("workcode", YGBH);
		hrmmap.put("lastname", xm1);
		hrmmap.put("locationid", GZDD);//工作地点
		hrmmap.put("status", "0");
		hrmmap.put("startdate", HTQSRQ);
		hrmmap.put("managerid", zjsj);
        insert(hrmmap, "hrmresource");
        
		fieldmap.put("field34", BMXY);
		fieldmap.put("field39", RZRQ);
		fieldmap.put("field28", HTSX);

        fieldmap.put("scope", "HrmCustomFieldByInfoType");
        fieldmap.put("scopeid", "1");
        fieldmap.put("id", String.valueOf(nextid));
        insert(fieldmap, "cus_fielddata");
		//String HTLX = "";//合同类型
		//String QYDW = "";//签约单位 
		//String HTQ = "";//合同期（月）
		//String ZPJH = "";//招聘计划
		//String XQLC = "";//用人需求流程
		//String BZ = "";//备注
		//String KQBM = "";//考勤部门
		//String KH = "";//卡号
		//String KQBMLX = "";//考勤保密类型
		//String ZPJH = "";//招聘计划
		//String XQLC = "";//用人需求流程
		//String BZ = "";//备注
		//String KQBM = "";//考勤部门
		//String KH = "";//卡号
		//String KQBMLX = "";//考勤保密类型
		//String zjl = "";//总经理
		//String DJR = "";//登记人
		//String wb = "";//文本
		//String SYQ = "";//试用期（月）
		//String ZZRQ = "";//转正日期
		
		//1 个人信息 HrmCustomFieldByInfoType
        try
        {
          ResourceComInfo ResourceComInfo = new ResourceComInfo();
          ResourceComInfo.addResourceInfoCache(String.valueOf(nextid));
        } catch (Exception e) {
          e.printStackTrace();
          log.writeLog(e.getMessage());
        }
		return SUCCESS;
	}
	public boolean insert(Map<String,String> mapStr,String table){
		if(mapStr == null) return false;
		if(mapStr.isEmpty()) return false;

		RecordSet rs = new RecordSet();
		
		BaseBean log = new BaseBean();
		String sql_0 = "insert into "+table+"(";
		StringBuffer sql_1 = new StringBuffer();
		String sql_2 = ") values(";
		StringBuffer sql_3 = new StringBuffer();
		String sql_4 = ")";
		
		Iterator<String> it = mapStr.keySet().iterator();
		while(it.hasNext()){
			String tmp_1 = it.next();
			String tmp_1_str = mapStr.get(tmp_1);
			if(tmp_1_str == null) tmp_1_str = "";
			
			if(tmp_1_str.length() > 0){
				sql_1.append(tmp_1);sql_1.append(",");
				
				if(tmp_1_str.contains("##")){
					sql_3.append(tmp_1_str.replace("##", ""));sql_3.append(",");
				}else{
					sql_3.append("'");sql_3.append(tmp_1_str);sql_3.append("',");
				}
			}
		}
		
		String now_sql_1 = sql_1.toString();
		if(now_sql_1.lastIndexOf(",")>0){
			now_sql_1 = now_sql_1.substring(0,now_sql_1.length()-1);
		}
		
		String now_sql_3 = sql_3.toString();
		if(now_sql_3.lastIndexOf(",")>0){
			now_sql_3 = now_sql_3.substring(0,now_sql_3.length()-1);
		}
		
		String sql = sql_0 + now_sql_1 + sql_2 + now_sql_3 + sql_4;
		log.writeLog("insert(sql) = " + sql);
		return rs.executeSql(sql);
	}
}
