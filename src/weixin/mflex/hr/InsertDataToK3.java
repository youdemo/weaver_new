package weixin.mflex.hr;

import java.sql.CallableStatement;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.StaticObj;
import weaver.general.Util;
import weaver.interfaces.datasource.DataSource;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class InsertDataToK3 implements Action {
	BaseBean log = new BaseBean();
	@Override
	public String execute(RequestInfo info) {
		
		SimpleDateFormat dateFormate = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		String tableName = "";
		String mainID = "";
		String gh = "";// 工号
		String jbrq = "";//加班日期
		String rq = "";// 加班开始日期
		String kssj = "";// 加班开始时间
		String rq2 = "";// 加班结束日期
		String jssj = "";// 加班结束时间
		String jbxs = "";// 加班小时数
		String bcfs = "";// 补偿方式
		String jbyy = "";// 加班原因
		String bz = "";// 备注
		String xxsj = "";// 休息时长
		String cjr = "";// 申请人
		String cjrgh = "";// 申请人工号
		String shrgh = "";// 审核人工号
		String shsj = dateFormate.format(new Date());// 审核时间
		String shrq = df.format(new Date()) + " 00:00:00.000";// 审核日期
		String checkResult = "";
		StringBuffer message = new StringBuffer();
		String fhjg = "0";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			cjr = Util.null2String(rs.getString("cjr"));

		}

		sql = "select workcode from hrmresource where id="
				+ info.getLastoperator();
		rs.executeSql(sql);
		if (rs.next()) {
			shrgh = Util.null2String(rs.getString("workcode"));
		}
		sql = "select workcode from hrmresource where id=" + cjr;
		rs.executeSql(sql);
		if (rs.next()) {
			cjrgh = Util.null2String(rs.getString("workcode"));
		}
		sql = "select createdate||' 00:00:00.000' as shrq from workflow_requestbase where requestid="
				+ requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			shrq = Util.null2String(rs.getString("shrq"));
		}
		sql = "select * from " + tableName + "_dt1 where mainid=" + mainID;
		rs.executeSql(sql);
		while (rs.next()) {
			jbrq = Util.null2String(rs.getString("jbrq"));
			gh = Util.null2String(rs.getString("gh"));
			rq = Util.null2String(rs.getString("rq"));
			kssj = Util.null2String(rs.getString("kssj"));
			 rq2 = Util.null2String(rs.getString("rq2"));
			 jssj = Util.null2String(rs.getString("jssj"));
			checkResult = checkData(gh, jbrq + " 00:00:00.000", rq + " " + kssj
					+ ":00.000", rq2 + " " + jssj+ ":00.000");
			//log.writeLog("checkResult:"+checkResult);
			
			if (!"0".equals(checkResult)) {
				message.append(" " + checkResult);
				fhjg = "1";
			}
		}
		if ("0".equals(fhjg)) {
			sql = "select * from " + tableName + "_dt1 where mainid=" + mainID;
			rs.executeSql(sql);
			while (rs.next()) {
				jbrq = Util.null2String(rs.getString("jbrq"));
				gh = Util.null2String(rs.getString("gh"));
				rq = Util.null2String(rs.getString("rq"));
				kssj = Util.null2String(rs.getString("kssj"));
				rq2 = Util.null2String(rs.getString("rq2"));
				jssj = Util.null2String(rs.getString("jssj"));
				bcfs = Util.null2String(rs.getString("bcfs"));
				jbxs = Util.null2String(rs.getString("jbxs"));
				jbyy = Util.null2String(rs.getString("jbyy"));
				bz = Util.null2String(rs.getString("bz"));
				xxsj = Util.null2String(rs.getString("xxsj"));
				if ("0".equals(jbyy)) {
					jbyy = "9C45523E-2C24-473C-A592-979CBF2893BD";
				} else {
					jbyy = "A45FD6EF-6201-4EF5-AB6A-690D75497AC2";
				}
				if ("0".equals(bcfs)) {
					bcfs = "57347CCD-6ECD-46D0-B6E4-A1069AB7B637";
				} else {
					bcfs = "C85D931D-5C73-482E-9ECA-44DB722E3402";
				}
				if ("0".equals(xxsj)) {
					xxsj = "0";
				} else if ("1".equals(xxsj)) {
					xxsj = "30";
				} else {
					xxsj = "60";
				}
//				log.writeLog("insert " + gh + "," + rq + " 00:00:00.000" + ","
//						+ rq + " " + kssj + ":00.000" + "," + rq2 + " " + jssj
//						+ ":00.000" + "," + jbxs + "," + shrgh + "," + shsj
//						+ "," + shrq + "," + bz + "," + cjrgh + "," + jbyy
//						+ "," + xxsj  + ","
//						+ bcfs);
				insertData(gh, jbrq + " 00:00:00.000", rq + " " + kssj
						+ ":00.000", rq2 + " " + jssj + ":00.000", jbxs, shrgh,
						shsj, shrq, bz, cjrgh, jbyy, xxsj, bcfs);

			}
		}

		sql = "update " + tableName + " set fhjg='" + fhjg + "',message='"
				+ message.toString() + "' where requestid=" + requestid;
		rs.executeSql(sql);
		return SUCCESS;
	}

	public String checkData(String gh, String jbrq, String kssj,String jssj) {
		String result = "";
		DataSource ds = (DataSource) StaticObj.getServiceByFullname(
				("datasource.K3"), DataSource.class);
		java.sql.Connection conn = ds.getConnection();
		CallableStatement cs = null;
		try {
			cs = conn.prepareCall("{call P_HR_ATS_CUS_OverTimecheck(?,?,?,?,?)}");
			cs.setString(1, gh);
			cs.setString(2, jbrq);
			cs.setString(3, kssj);
			cs.setString(4, jssj);
			cs.registerOutParameter(5, Types.VARCHAR);
			cs.execute();
			result = cs.getString(5);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.writeLog(e.getMessage());
			try {
				cs.close();
				conn.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			cs.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String insertData(String gh, String jbrq, String kssj, String jssj,
			String jbxxs, String shrgh, String shsj, String shrq, String bz,
			String sqry, String jbyy, String xxsc,  String bcfs) {
		String result = "";
		DataSource ds = (DataSource) StaticObj.getServiceByFullname(
				("datasource.K3"), DataSource.class);
		java.sql.Connection conn = ds.getConnection();
		CallableStatement cs = null;
		try {
			cs = conn
					.prepareCall("{call P_HR_ATS_CUS_OverTimeInfo(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
			cs.setString(1, gh);
			cs.setString(2, jbrq);
			cs.setString(3, kssj);
			cs.setString(4, jssj);
			cs.setFloat(5, Float.valueOf(jbxxs));
			cs.setString(6, shrgh);
			cs.setString(7, shsj);
			cs.setString(8, shrq);
			cs.setString(9, sqry);
			cs.setString(10, bz);
			cs.setString(11, jbyy);
			cs.setInt(12, Integer.valueOf(xxsc));
			cs.setString(13, bcfs);
			cs.registerOutParameter(14, Types.VARCHAR);
			// cs.registerOutParameter(4, Types.VARCHAR);
			cs.execute();
			 result = cs.getString(14);
		} catch (Exception e) {
			log.writeLog(e.getMessage());
			e.printStackTrace();
			try {
				cs.close();
				conn.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		try {
			cs.close();
			conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.writeLog("result:"+result);
		return result;
	}

	public String getStrNum(String requestid, int len) {
		String buff = requestid;
		int max = len - buff.length();
		for (int index = 0; index < max; index++) {
			buff = "0" + buff;
		}
		return buff;
	}
}
