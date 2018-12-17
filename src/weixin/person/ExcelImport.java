package weixin.person;

import java.util.HashMap;
import java.util.Map;

import com.informix.msg.rds_en_US;

import jxl.Cell;
import jxl.Sheet;
import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.BaseBean;
import weaver.general.Util;

public class ExcelImport {
	public String readAndExceSheet(Sheet sheet) {
		BaseBean log = new BaseBean();
		log.writeLog("进入Excel");
		RecordSet rs = new RecordSet();
		String sql = "";
		int rows = sheet.getRows();
		String modeid = getModeId("uf_RZXXB");
		InsertUtilNew iu = new InsertUtilNew();
		try {
			for (int i = 1; i < rows; i++) {
				Cell[] cells = sheet.getRow(i);

				String IDNo = getStr(cells[0].getContents());
				String Name = getStr(cells[1].getContents());
				String ApplyDate = getStr(cells[2].getContents());
				String Country = getStr(cells[3].getContents());
				String Ethnic = getStr(cells[4].getContents());
				String Gender = getStr(cells[5].getContents());
				String Marital = getStr(cells[6].getContents());
				String BirthDate = getStr(cells[7].getContents());
				String Htype = getStr(cells[8].getContents());
				String Registered = getStr(cells[9].getContents());
				String Province = getStr(cells[10].getContents());
				String MailingName = getStr(cells[11].getContents());
				String Graduate = getStr(cells[12].getContents());
				String GraduationDate = getStr(cells[13].getContents());
				String Major = getStr(cells[14].getContents());
				String Tel = getStr(cells[15].getContents());
				String Contacts = getStr(cells[16].getContents());
				String ContactsTel = getStr(cells[17].getContents());
				String Education = getStr(cells[18].getContents());
				String FlowNo = getStr(cells[19].getContents());
				String Experience = getStr(cells[20].getContents());
				String status = getStr(cells[21].getContents());
				String EmployeeNo = getStr(cells[22].getContents());
				String BusinessUnit = getStr(cells[23].getContents());
				String Supervisor = getStr(cells[24].getContents());
				String JobType = getStr(cells[25].getContents());
				String JobDesc = getStr(cells[26].getContents());
				String WorkingBlock = getStr(cells[27].getContents());
				String HireDate = getStr(cells[28].getContents());
				String DateStarted = getStr(cells[29].getContents());
				String DatePayStarts = getStr(cells[30].getContents());
				String EmploymentStatus = getStr(cells[31].getContents());
				String DispatchCompany = getStr(cells[32].getContents());
				String SDate = getStr(cells[33].getContents());
				if("".equals(IDNo)) {
					continue;
				}
				String billid="";
				sql="select * from uf_RZXXB where IDNo='"+IDNo+"' order by id desc";
				rs.executeSql(sql);
				if(rs.next()) {
					billid = Util.null2String(rs.getString("id"));
				}
				if("".equals(billid)) {
					continue;
				}
				
				Map<String, String> mapStr = new HashMap<String, String>();
				mapStr.put("IDNo", IDNo);
				mapStr.put("Name", Name);
				mapStr.put("ApplyDate", ApplyDate);
				mapStr.put("Country", Country);
				mapStr.put("Ethnic", Ethnic);
				mapStr.put("Gender", getSelectValue(Gender,"Gender"));
				mapStr.put("Marital",getSelectValue(Marital,"Marital"));
				mapStr.put("BirthDate", BirthDate);
				mapStr.put("Htype", getSelectValue(Htype,"Htype"));
				mapStr.put("Registered", Registered);
				mapStr.put("Province", Province);
				mapStr.put("MailingName", MailingName);
				mapStr.put("Graduate", Graduate);
				mapStr.put("GraduationDate", GraduationDate);
				mapStr.put("Major", Major);
				mapStr.put("Tel", Tel);
				mapStr.put("Contacts", Contacts);
				mapStr.put("ContactsTel", ContactsTel);
				mapStr.put("Education", Education);
				mapStr.put("FlowNo", FlowNo);
				mapStr.put("Experience", Experience);
				mapStr.put("status", getSelectValue(status,"status"));
				mapStr.put("EmployeeNo", EmployeeNo);
				mapStr.put("BusinessUnit", BusinessUnit);
				mapStr.put("Supervisor", Supervisor);
				mapStr.put("JobType", JobType);
				mapStr.put("JobDesc", JobDesc);
				mapStr.put("WorkingBlock", WorkingBlock);
				mapStr.put("HireDate", HireDate);
				mapStr.put("DateStarted", DateStarted);
				mapStr.put("DatePayStarts", DatePayStarts);
				mapStr.put("EmploymentStatus", EmploymentStatus);
				mapStr.put("DispatchCompany", DispatchCompany);
				mapStr.put("SDate", SDate);

				iu.updateGen(mapStr, "uf_RZXXB", "id", billid);
				
			}

		} catch (Exception e) {
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
	
	private String  getSelectValue(String selectName,String fieldName) {
		RecordSet rs = new RecordSet();
		String selectValue = "";
		String sql = "select selectvalue from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_RZXXB' and a.fieldname='"+fieldName+"' and upper(c.selectname)=upper('"+selectName+"') ";
		rs.executeSql(sql);
		if(rs.next()) {
			selectValue = Util.null2String(rs.getString("selectvalue"));
		}
		return selectValue;
	}
}
