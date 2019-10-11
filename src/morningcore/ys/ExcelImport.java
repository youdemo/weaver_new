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

public class ExcelImport {
	public String readAndExceSheet(Sheet sheet, int userid, String year) {
		BaseBean log = new BaseBean();
		log.writeLog("进入Excel");
		RecordSet rs = new RecordSet();
		String timestamp = String.valueOf(new Date().getTime());
		String newVersion = "";
		String sql = "";
		String creater = userid + "";
		if ("".equals(creater)) {
			creater = "1";
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		int rows = sheet.getRows();
		String modeid = getModeId("uf_yssjdr");
		InsertUtilNew iu = new InsertUtilNew();
		StringBuffer result = new StringBuffer();
		String deleteFlag = "0";
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
				String isUsed = "";// 是否已发生预算
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
					try {
						fyxqrr = getStr(cells[14].getContents());
					}catch(Exception e) {
						fyxqrr = "";
					}
					try {
						isUsed = getStr(cells[15].getContents());
					}catch(Exception e) {
						isUsed = "";
					}
				} catch (Exception e) {
					log.writeLog(this.getClass().getName(), e);
				}
				if ("".equals(bb) || "".equals(nf) || "".equals(fsyf) || "".equals(rjbm) || "".equals(nbdd)
						|| "".equals(fykmbh) || "".equals(bb) ) {
					result.append("第" + i + "行必填数据不完整<br/>");
					deleteFlag = "1";
					continue;
				}
				String deptidej = "";
				sql = "select id from hrmdepartment where departmentname='" + rjbm + "'";
				// log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.executeSql(sql);
				if (rs.next()) {
					deptidej = Util.null2String(rs.getString("id"));
				}
				if ("".equals(deptidej)) {
					result.append("第" + i + "行二级部门不存在<br/>");
					deleteFlag = "1";
					continue;
				}
				String deptidsj = "";
				if (!"".equals(ejbm)) {
					sql = "select id from hrmdepartment where departmentname='" + ejbm + "' and supdepid='" + deptidej
							+ "'";
					// log.writeLog(this.getClass().getName(),"sql:"+sql);
					rs.executeSql(sql);
					if (rs.next()) {
						deptidsj = Util.null2String(rs.getString("id"));
					}
					if ("".equals(deptidsj)) {
						result.append("第" + i + "行三级部门不存在<br/>");
						log.writeLog(this.getClass().getName(), result.toString());
						deleteFlag = "1";
						continue;
					}
				}
				String xm = "";
				sql = "select id from uf_nbdd where SUBSTRING(internal_order,(PATINDEX('%[^0]%',internal_order)),LEN(internal_order))='"
						+ nbdd + "'";
				// log.writeLog(this.getClass().getName(),"sql:"+sql);
				rs.executeSql(sql);
				if (rs.next()) {
					xm = Util.null2String(rs.getString("id"));
				}
				if ("".equals(xm)) {
					result.append("第" + i + "行内部订单号不存在<br/>");
					deleteFlag = "1";
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
					deleteFlag = "1";
					continue;
				}
				String ryids = "";
				String flag = "";
				// log.writeLog("fyxqrr:"+fyxqrr);
				if (!"".equals(fyxqrr)) {
					String names[] = fyxqrr.split(",");
					for (String name : names) {
						if ("".equals(name)) {
							continue;
						}
						String ryid = "";
						sql = "select id from hrmresource where lastname='" + name
								+ "' and status<5 and isnull(belongto,-1)<=0";
						// log.writeLog(this.getClass().getName(),"sql:"+sql);
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
				
				String isUsedFlag = "0";
				if("是".equals(isUsed) || "1".equals(isUsed)|| "y".equalsIgnoreCase(isUsed)||"yes".equalsIgnoreCase(isUsed)){
					isUsedFlag = "1";
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
				mapStr.put("seqno", timestamp);
				mapStr.put("zt", "1");
				mapStr.put("sfyfsys", isUsedFlag);
				mapStr.put("sfxyzxqx", "0");
				mapStr.put("modedatacreater", creater);
				mapStr.put("modedatacreatertype", "0");
				mapStr.put("modedatacreatedate", now);
				mapStr.put("formmodeid", modeid);
				iu.insert(mapStr, "uf_yssjdr");

			}

		} catch (Exception e) {
			log.writeLog("testaaa");
			log.writeLog(e);
			deleteData(timestamp);
			return "-1";
		}
		
		if(result.toString().length()<1) {
			String resu =  checkData( timestamp, year);
			if(!"1".equals(resu)) {
				result.append(resu);
				log.writeLog(this.getClass().getName(), result.toString());
				return result.toString();
			}else {
				return "1";
			}
		}else {
			if("1".equals(deleteFlag)) {
				deleteData(timestamp);
			}
			log.writeLog(this.getClass().getName(), result.toString());
			return result.toString();
		}
	}

	
	public void deleteData(String seqno) {
		RecordSet rs = new RecordSet();
		String sql = "";
		if (!"".equals(seqno)) {
			sql = "delete from uf_yssjdr where seqno='" + seqno + "'";
			rs.execute(sql);
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
	
	public String checkData(String seqno,String year){
		RecordSet rs = new RecordSet();
		StringBuffer str = new StringBuffer();
		String sql = "select count(*) as con from uf_yssjdr where  seqno = '"+seqno+"' and (Len(bb)<1 or bb is null or Len(bb)<1 or"
				+ " nf is null or Len(nf)<1 or fsyf is null or Len(fsyf)<1 or rjbm is null or Len(rjbm)<1 or nbdd is null or Len(nbdd)<1 or fykmbh is null or Len(fykmbh)<1)" ;
		rs.executeSql(sql);
		if(rs.next()) {
			int con = rs.getInt("con");
			if(con>0) {
				str.append("版本，年份，发生月份，二级部门，内部订单，费用科目编号必填字段中存在部分缺失数据！<br/>");
				deleteData(seqno);
				return str.toString();
			}
		}
		sql = "select count(*) as con from (select bb  from uf_yssjdr where  seqno = '"+seqno+"' group by bb) u ";
		rs.executeSql(sql);
		if(rs.next()) {
			int con = rs.getInt("con");
			if(con>1) {
				deleteData(seqno);
				str.append("导入数据中，存在导入版本不一致（多个版本）的错误数据！<br/>");
				return str.toString();
			}
		}
		sql = "select count(*) as con  from uf_yssjdr where seqno = '"+seqno+"' and nf<> '"+year+"' ";
		rs.executeSql(sql);
		if(rs.next()) {
			int con = rs.getInt("con");
			if(con>0) {
				deleteData(seqno);
				str.append("导入数据中，预算年份和导入年份存在不一致的错误数据！<br/>");
				return str.toString();
			}
		}
		sql = "select count(*) as con from uf_yssjdr where  seqno <> '"+seqno+"' and bb in (select bb  from uf_yssjdr where  seqno = '"+seqno+"' group by bb)";
		rs.executeSql(sql);
		if(rs.next()) {
			int con = rs.getInt("con");
			if(con>0) {
				deleteData(seqno);
				str.append("导入的版本号已存在无法导入<br/>");
				return str.toString();
			}
		}
		//setPower(seqno);
		return "1";
		
		
	}
	public void setPower(String seqno) {
		String modeid = getModeId("uf_yssjdr");
		RecordSet rs = new RecordSet();
		String sql = "select id,modedatacreater from uf_yssjdr where seqno = '"+seqno+"'  ";
		rs.executeSql(sql);
		while(rs.next()){
			String billid = Util.null2String(rs.getString("id"));
			String creater = Util.null2String(rs.getString("modedatacreater"));
			 if (!"".equals(billid)) {
				 ModeRightInfo ModeRightInfo = new ModeRightInfo();
				 ModeRightInfo.editModeDataShare(Integer.valueOf(creater), Integer.valueOf(modeid),Integer.valueOf(billid));
			 }
		}
	}
	/**
	 * 手动更新权限
	 * @param version 版本
	 * @param year 年份
	 * @return
	 */
	public boolean setPower2(String version,String year) {
		if("".equals(version) || "".equals(year)) {
			return false;
		}		
		String modeid = getModeId("uf_yssjdr");
		RecordSet rs = new RecordSet();
		int count=0;
		String sql = "select count(1) as count from uf_yssjdr where nf='"+year+"' and bb='"+version+"'";
		rs.execute(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count == 0){
			return false;
		}
		sql = "select id,modedatacreater from uf_yssjdr where bb = '"+version+"' and nf='"+year+"' and sfxyzxqx='0' ";
		rs.executeSql(sql);
		while(rs.next()){
			String billid = Util.null2String(rs.getString("id"));
			String creater = Util.null2String(rs.getString("modedatacreater"));
			 if (!"".equals(billid)) {
				 ModeRightInfo ModeRightInfo = new ModeRightInfo();
				 ModeRightInfo.editModeDataShare(Integer.valueOf(creater), Integer.valueOf(modeid),Integer.valueOf(billid));
			 }
		}
		sql = "update uf_yssjdr set sfxyzxqx='1' where bb = '"+version+"' and nf='"+year+"' and sfxyzxqx='0' ";
		rs.execute(sql);
		return true;
	}
	
	
		

}
