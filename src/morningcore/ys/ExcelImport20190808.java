package morningcore.ys;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;

public class ExcelImport20190808 {
	public String readAndExceSheet(Sheet sheet, int userid, String year) {
		BaseBean log = new BaseBean();
		log.writeLog("进入Excel");
		RecordSet rs = new RecordSet();
		String sql = "";
		String creater = userid + "";
		if ("".equals(creater)) {
			creater = "1";
		}
		String isupdate = "0";
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		int rows = sheet.getRows();
		String modeid = getModeId("uf_yssjdr");
		InsertUtilNew iu = new InsertUtilNew();
		StringBuffer result = new StringBuffer();
		try {
			for (int i = 1; i < rows; i++) {
				Cell[] cells = sheet.getRow(i);
				String bb = "";// 版本
				String nf = "";// 年份
				String fsyf = "";// 发生月份
				String rjbm = "";// 二级部门
				String ejbm = "";// 三级部门
				String nbdd = "";// 内部订单
				String xmname = "";// 项目
				String fykmbh = "";// 费用科目编号
				String yskmmc = "";// 费用科目类型
				String sl = "";// 数量
				String jldw = "";// 计量单位
				String dj = "";// 单价（元）
				String hjje = "";// 合计金额（元）
				String jtsm = "";// 具体说明
				String fyxqrr = "";// 费用需求人员
				try {
					bb = getStr(cells[0].getContents());
					nf = getStr(cells[1].getContents());
					fsyf = getStr(cells[2].getContents());
					rjbm = getStr(cells[3].getContents());
					ejbm = getStr(cells[4].getContents());
					nbdd = getStr(cells[5].getContents());
					xmname = getStr(cells[6].getContents());
					fykmbh = getStr(cells[7].getContents());
					yskmmc = getStr(cells[8].getContents());
					sl = getStr(cells[9].getContents());
					jldw = getStr(cells[10].getContents());
					dj = getStr(cells[11].getContents());
					hjje = getStr(cells[12].getContents());
					jtsm = getStr(cells[13].getContents());
					fyxqrr = getStr(cells[14].getContents());
				} catch (Exception e) {
					log.writeLog(this.getClass().getName(),e);
				}
				if ("".equals(bb) || "".equals(nf) || "".equals(fsyf) || "".equals(rjbm) || "".equals(nbdd)
						|| "".equals(fykmbh) || "".equals(bb) || "".equals(bb)) {
					result.append("第" + i + "行必填数据不完整<br/>");
					continue;
				}
				if (!year.equals(nf)) {
					result.append("第" + i + "行导入年份不对<br/>");
					continue;
				}
				String deptidej = "";
				sql = "select id from hrmdepartment where departmentname='" + rjbm + "'";
				//log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.executeSql(sql);
				if (rs.next()) {
					deptidej = Util.null2String(rs.getString("id"));
				}
				if ("".equals(deptidej)) {
					result.append("第" + i + "行二级部门不存在<br/>");
					continue;
				}
				String deptidsj = "";
				if (!"".equals(ejbm)) {
					sql = "select id from hrmdepartment where departmentname='" + ejbm + "' and supdepid='" + deptidej
							+ "'";
					//log.writeLog(this.getClass().getName(),"sql:"+sql);
					rs.executeSql(sql);
					if (rs.next()) {
						deptidsj = Util.null2String(rs.getString("id"));
					}
					if ("".equals(deptidsj)) {
						result.append("第" + i + "行三级部门不存在<br/>");
						continue;
					}
				}
				String xm = "";
				sql = "select id from uf_nbdd where SUBSTRING(internal_order,(PATINDEX('%[^0]%',internal_order)),LEN(internal_order))='"
						+ nbdd + "'";
				//log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.executeSql(sql);
				if (rs.next()) {
					xm = Util.null2String(rs.getString("id"));
				}
				if ("".equals(xm)) {
					result.append("第" + i + "行内部订单号不存在<br/>");
					continue;
				}
				String yskm = "";
				sql = "select id from uf_yskmsj where bh='" + fykmbh + "'";
				rs.executeSql(sql);
				if (rs.next()) {
					yskm = Util.null2String(rs.getString("id"));
				}
				if ("".equals(yskm)) {
					result.append("第" + i + "行费用科目编码不存在<br/>");
					continue;
				}
				String ryids = "";
				String flag = "";
				//log.writeLog("fyxqrr:"+fyxqrr);
				if (!"".equals(fyxqrr)) {
					String names[] = fyxqrr.split(",");
					for (String name : names) {
						if ("".equals(name)) {
							continue;
						}
						String ryid = "";
						sql = "select id from hrmresource where lastname='" + name
								+ "' and status<5 and isnull(belongto,-1)<=0";
						//log.writeLog(this.getClass().getName(),"sql:"+sql);
						rs.execute(sql);
						if (rs.next()) {
							ryid = Util.null2String(rs.getString("id"));
						}
						if (!"".equals(ryid)) {
							ryids = ryids + flag + ryid;
							flag = ",";
						}
					}
				}
				int count = 0;
				sql = "select count(1) as count from uf_yssjdr where bb='" + bb + "' and nf='" + nf + "'";
				//log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.execute(sql);
				if (rs.next()) {
					count = rs.getInt("count");
				}
				if (count == 0 && "0".equals(isupdate)) {
					sql = "update uf_yssjdr set zt='1' where bb <> '" + bb + "' and nf = '" + nf + "'";
					//log.writeLog(this.getClass().getName(),"sql:"+sql);
					rs.execute(sql);
					isupdate = "1";
				}
				String billid = "";
				sql = "select id from uf_yssjdr where bb='" + bb + "' and nf='" + nf + "' and fsyf='" + fsyf
						+ "' and rjbm='" + deptidej + "' and nbdd='" + nbdd + "' and fykmbh='" + fykmbh + "' and jtsm='"
						+ jtsm + "'";
				//log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.executeSql(sql);
				if (rs.next()) {
					billid = Util.null2String(rs.getString("id"));
				}
				Map<String, String> mapStr = new HashMap<String, String>();
				mapStr.put("bb", bb);
				mapStr.put("nf", nf);
				mapStr.put("fsyf", fsyf);
				mapStr.put("rjbm", deptidej);
				mapStr.put("ejbm", deptidsj);
				mapStr.put("nbdd", nbdd);
				mapStr.put("xm", xm);
				mapStr.put("fykmbh", fykmbh);
				mapStr.put("fykm", yskm);
				mapStr.put("sl", sl);
				mapStr.put("jldw", jldw);
				mapStr.put("dj", dj);
				mapStr.put("hjje", hjje);
				mapStr.put("jtsm", jtsm);
				mapStr.put("fyxqrr", ryids);
				//log.writeLog("aaaa:" + mapStr.toString());
				if ("".equals(billid)) {
					mapStr.put("zt", "0");
					mapStr.put("modedatacreater", creater);
					mapStr.put("modedatacreatertype", "0");
					mapStr.put("modedatacreatedate", now);
					mapStr.put("formmodeid", modeid);
					iu.insert(mapStr, "uf_yssjdr");
					sql = "select max(id) as id from uf_yssjdr where bb='" + bb + "' and nf='" + nf + "' and fykmbh='"
							+ fykmbh + "' ";
					//log.writeLog(this.getClass().getName(),"sql:"+sql);
					rs.executeSql(sql);
					if (rs.next()) {
						billid = Util.null2String(rs.getString("id"));
					}
					if (!"".equals(billid)) {
						ModeRightInfo ModeRightInfo = new ModeRightInfo();
						ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
								Integer.valueOf(billid));
					}
				} else {
					iu.updateGen(mapStr, "uf_yssjdr", "id", billid);
				}

			}

		} catch (Exception e) {
			log.writeLog(e);
			return "-1";
		}
		if (!"".equals(result.toString())) {
			return result.toString();
		} else {
			return "1";
		}
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

	private String getStr(String str) {
		if (str == null)
			return "";
		return str.trim();
	}

	private String getNumStr(String str) {
		if (str == null || "".equals(str))
			return "0";
		return str.trim();
	}

}
