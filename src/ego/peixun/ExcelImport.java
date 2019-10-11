package ego.peixun;

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
	public String readAndExceSheet(Sheet sheet,int userid) {
		BaseBean log = new BaseBean();
		log.writeLog("进入Excel");
		RecordSet rs = new RecordSet();
		String sql = "";
		String creater = userid+"";
		if("".equals(creater)) {
			creater = "1";
		}
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
		int rows = sheet.getRows();
		String modeid = getModeId("uf_BTABLE");
		InsertUtilNew iu = new InsertUtilNew();
		try {
			for (int i = 1; i < rows; i++) {
				Cell[] cells = sheet.getRow(i);
				String GH = "";
				String FirstName = "";
				String LastName = "";
				String HTLX ="";
				String CBZX = "";
				String ZZDM = "";
				String ZZMC ="";
				String ZW ="";
				String KCQD = "";
				String PEMU = "";
				String PXNF = "";
				String KSSJ ="";
				String JSSJ = "";
				String SC = "";
				String PXZX = "";
				String CYQK = "";
				String GRFY ="";
				String ZFY = "";
				String ZT = "";
				try {
				GH = getStr(cells[0].getContents());
				FirstName = getStr(cells[1].getContents());
				LastName = getStr(cells[2].getContents());
				HTLX = getStr(cells[3].getContents());
				CBZX = getStr(cells[4].getContents());
				ZZDM = getStr(cells[5].getContents());
				ZZMC = getStr(cells[6].getContents());
				ZW = getStr(cells[7].getContents());
				KCQD = getStr(cells[8].getContents());
				PEMU = getStr(cells[9].getContents());
				PXNF = getStr(cells[10].getContents());
				KSSJ = getStr(cells[11].getContents());
				JSSJ = getStr(cells[12].getContents());
				SC = getStr(cells[13].getContents());
				PXZX = getStr(cells[14].getContents());
				CYQK = getStr(cells[16].getContents());
				GRFY = getStr(cells[17].getContents());
				ZFY = getStr(cells[18].getContents());
				ZT = getStr(cells[19].getContents());
				}catch(Exception e) {
					
				}
				String pxry = "-1";
				if (!"".equals(GH)) {
					sql = "select * from hrmresource where workcode='" + GH
							+ "' order by id desc";
					rs.executeSql(sql);
					if (rs.next()) {
						pxry = Util.null2String(rs.getString("id"));
					}
				}else {
					continue;
				}
				String bm = "-1";
				if (!"".equals(ZZDM)) {
					sql = "select * from HrmDepartment where departmentcode='"
							+ ZZDM + "' order by id desc";
					rs.executeSql(sql);
					if (rs.next()) {
						bm = Util.null2String(rs.getString("id"));
					}
				}

				Map<String, String> mapStr = new HashMap<String, String>();
				mapStr.put("GH", GH);
				mapStr.put("FirstName", FirstName);
				mapStr.put("LastName", LastName);
				mapStr.put("HTLX", HTLX);
				mapStr.put("CBZX", CBZX);
				mapStr.put("ZZDM", ZZDM);
				mapStr.put("ZZMC", ZZMC);
				mapStr.put("ZW", ZW);
				mapStr.put("KCQD", KCQD);
				mapStr.put("PEMU", PEMU);
				mapStr.put("PXNF", PXNF);
				mapStr.put("KSSJ", KSSJ);
				mapStr.put("JSSJ", JSSJ);
				mapStr.put("SC", SC);
				mapStr.put("PXZX", PXZX);
				mapStr.put("CYQK", CYQK);
				mapStr.put("GRFY", GRFY);
				mapStr.put("ZFY", ZFY);
				mapStr.put("ZT", ZT);
				mapStr.put("pxry", pxry);
				mapStr.put("bm", bm);
				mapStr.put("modedatacreater", creater);
				mapStr.put("modedatacreatertype", "0");
				mapStr.put("modedatacreatedate", now);
				mapStr.put("formmodeid", modeid);

				iu.insert(mapStr, "uf_BTABLE");
				String dataId = "";
				sql = "select max(id) as id from uf_BTABLE ";
				rs.executeSql(sql);

				if (rs.next()) {
					dataId = Util.null2String(rs.getString("id"));
				}
				if (!"".equals(dataId)) {
					ModeRightInfo ModeRightInfo = new ModeRightInfo();
					ModeRightInfo.editModeDataShare(Integer.valueOf("1"),
							Integer.valueOf(modeid), Integer.valueOf(dataId));
				}
			}

		} catch (Exception e) {
			log.writeLog(e);
			return "-1";
		}

		return "1";
	}

	public String getModeId(String tableName) {
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"
				+ tableName + "'";
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
