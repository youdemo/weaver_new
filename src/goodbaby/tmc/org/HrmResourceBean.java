package goodbaby.tmc.org;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import tmc.util.TmcDBUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;

public class HrmResourceBean {
	
	// 工号
	private String workcode;
	// 登录名
	private String loginid;
	// 状态  0：试用   1：正式    2：临时   3：试用延期    4：解聘   5：离职   6：退休    7：无效
	private String status = "1";
	// 姓名
	private String lastname;
	// 性别   0 男性   1女性
	private String sex;
	// 出生日期  格式：2000-01-01
	private String birthday;
	// 安全级别
	private int seclevel;
	
	// 岗位关系    0通过ID  1是通过编码  
	private int jobIdOrCode;
 	// 人员岗位  ID
	private String jobtitle;
 	// 人员岗位  Code
	private String jobtitleCode;
	
	// 所属部门    0通过ID  1是通过编码  
	private int deptIdOrCode;
	// 部门  ID
	private String departmentid;
	// 部门  Code
	private String departmentCode;

	// 直接上线   可以通过岗位识别或者通过编码或id识别  -1不通过这个来识别  0通过ID  1是通过编码    2通过岗位识别
	private int managerIdOrCode;
	// 直接上级 ID  
	private String managerid;
	// 直接上级   可以通过岗位识别或者通过编码或id识别
	private String managerCode;
	
	// 所属主帐号  通过id或Code识别   0通过id  1通过编码   -1是不启用【主账号】
	private int belongIdOrCode = -1;
	// 所属主帐号  如果这个有值代表是次账号   账号需要清空
	private String belongto;
	// 所属主帐号  如果这个有值代表是次账号   账号需要清空
	private String belongtoCode;
	
	// 主次账号类型     0 主账号  1次账号
	private int accounttype = 0;
	
	// 国家 ID     HrmCountry表里  没填默认1   中国     如有中文名称匹配
	private String nationality = "1";
	// 系统语言  7为简体中文  这个字段没有值是或值不对时会默认为7
	private String systemlanguage = "7";	
	// 登录密码   系统自定加密获取   不放值默认密码为1
	private String password;  
	// 婚姻状况 ID   0 未婚 1已婚  2离异
	private String maritalstatus;
	// 电话号码
	private String telephone;
	// 手机号码
	private String mobile;
	// 其他电话
	private String mobilecall;
	// 邮箱
	private String email;
	// 显示顺序 
	private int dsporder = 0;
	// 创建人id  系统人员ID  默认系统管理员 1
	private String createrid = "1";
	// 创建日期   格式：2000-01-01
	private String createdate;
	// 工作地点 ID   HrmLocations表里  通过中文名称直接识别
	private String locationid = "1";
	// 办公室
	private String workroom;
	// 家庭住址
	private String homeaddress;
	// 合同开始日期    格式：2000-01-01
	private String startdate;
	// 合同结束日期    格式：2000-01-01   注：服务每天晚上会检查，如果这个日期已经到了这个用户会被变成无效。
 	private String enddate;
 	
	// 冗余日期字段
	private String datefield1;
	private String datefield2;
	private String datefield3;
	private String datefield4;
	private String datefield5;
	
	// 冗余数字字段
	private String numberfield1;
	private String numberfield2;
	private String numberfield3;
	private String numberfield4;
	private String numberfield5;
	
	// 冗余文本字段
	private String textfield1;
	private String textfield2;
	private String textfield3;
	private String textfield4;
	private String textfield5;
	
	// 冗余判断字段
	private String tinyintfield1;
	private String tinyintfield2;
	private String tinyintfield3;
	private String tinyintfield4;
	private String tinyintfield5;
	
	// 职责描述
	private String jobactivitydesc;
	// 身份证号码
	private String certificatenum;
	// 籍贯
	private String nativeplace;
	// 学历 ID   HrmEducationLevel  可以做对接  可为空
	private String educationlevel;
	// 户口
	private String regresidentplace;
	// 健康状况 ID   0优秀 1良好  2一般  3较差
	private String healthinfo;
	// 居住地
	private String residentplace;
	// 政治面貌
	private String policy;
	// 学位
	private String degree;
	// 身高
	private String height;
	// 职称 ID   HrmJobCall表中，  字符串直接对比
	private String jobcall = "0";
	// 公积金帐号
	private String accumfundaccount;
	// 出生地
	private String birthplace;
	// 民族
	private String folk;
	// 分机
	private String extphone;
	// 传真
	private String fax;
	// 体重
	private String weight;
	// 暂住证号码
	private String tempresidentnumber;
	// 试用期结束日期   格式：2000-01-01
	private String probationenddate;
	// 工资银行1
	private String bankid1;
	// 工资帐号1
	private String accountid1;
	// 
	private String joblevel = "0";
	// 
	private String costcenterid = "1";
	// 
	private String assistantid = "0";
	
	// 自定义字段  
	private Map<String,String> cusMap;
	
	public Map<String,String> getCusMap(){
		return cusMap;
	}
	
	public int getAccounttype(){
		if(belongIdOrCode >= 0)
			accounttype = 1;
		return accounttype; 
	}
	
	
	
	public String getJoblevel() {
		return joblevel;
	}

	public void setJoblevel(String joblevel) {
		this.joblevel = joblevel;
	}

	public String getCostcenterid() {
		return costcenterid;
	}

	public void setCostcenterid(String costcenterid) {
		this.costcenterid = costcenterid;
	}

	public String getAssistantid() {
		return assistantid;
	}

	public void setAssistantid(String assistantid) {
		this.assistantid = assistantid;
	}

	public int getBelongIdOrCode() {
		return belongIdOrCode;
	}

	public void setBelongIdOrCode(int belongIdOrCode) {
		this.belongIdOrCode = belongIdOrCode;
	}

	public String getBelongtoCode() {
		return belongtoCode;
	}

	public void setBelongtoCode(String belongtoCode) {
		this.belongtoCode = belongtoCode;
	}

	public int getManagerIdOrCode() {
		return managerIdOrCode;
	}

	public void setManagerIdOrCode(int managerIdOrCode) {
		this.managerIdOrCode = managerIdOrCode;
	}

	public String getManagerCode() {
		return managerCode;
	}

	public void setManagerCode(String managerCode) {
		this.managerCode = managerCode;
	}

	public void addCusMap(String key,String value){
		if(cusMap == null)
			cusMap = new HashMap<String,String>();
		cusMap.put(key, value);
	}
	
	public int getJobIdOrCode() {
		return jobIdOrCode;
	}

	public void setJobIdOrCode(int jobIdOrCode) {
		this.jobIdOrCode = jobIdOrCode;
	}

	public String getJobtitleCode() {
		return jobtitleCode;
	}

	public void setJobtitleCode(String jobtitleCode) {
		this.jobtitleCode = jobtitleCode;
	}

	public int getDeptIdOrCode() {
		return deptIdOrCode;
	}

	public void setDeptIdOrCode(int deptIdOrCode) {
		this.deptIdOrCode = deptIdOrCode;
	}

	public String getDepartmentCode() {
		return departmentCode;
	}

	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}


	public int getDsporder() {
		return dsporder;
	}

	public void setDsporder(int dsporder) {
		this.dsporder = dsporder;
	}
	
	public String getLoginid() {
		return loginid;
	}

	public void setLoginid(String loginid) {
		this.loginid = loginid;
	}

	public String getPassword() {
		if(password == null || password.length() < 1) 
			password = "1";
		
		return Util.getEncrypt(password);
	}
	
	public String getPassword1() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public String getSex() {
		return sex;
	}
	
	public String getSexID() {
		if("女".equals(sex)){
			return "1";
		}
		if("woman".equalsIgnoreCase(sex)){
			return "1";
		}
		if("female".equalsIgnoreCase(sex)){
			return "1";
		}
		if("1".equalsIgnoreCase(sex)){
			return "1";
		}
		return "0";
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getNationality() {
		return nationality;
	}
	
	public String getNationalityID() {
		if("".equals(nationality)){
			return "1";
		}
		if("1".equals(nationality)||"2".equals(nationality))
			return nationality;
		if("中国".equals(nationality)||"中华人民共和国".equals(nationality))
			return "1";
		if("China".equalsIgnoreCase(nationality)||"Chinese".equalsIgnoreCase(nationality)){
			return "1";
		}
		RecordSet rs = new RecordSet();
		int nationalityID = 0;
		rs.executeSql("select  id from HrmCountry where countryname='"+nationality+"'");
		if(rs.next()){
			nationalityID = rs.getInt("id");
		}
		if(nationalityID < 1){
			String sql = "insert into HrmCountry(countryname,countrydesc) values('"+
					nationality+"','"+nationality+"')";
			rs.executeSql(sql);
			
			rs.executeSql("select  id from HrmCountry where countryname='"+nationality+"'");
			if(rs.next()){
				nationalityID = rs.getInt("id");
			}
		}
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getSystemlanguage() {
		if(systemlanguage == null){
			return "7";
		}
		if(!"8".equals(systemlanguage)&!"9".equals(systemlanguage)){
			return "7";
		}
		
		return systemlanguage;
	}

	public void setSystemlanguage(String systemlanguage) {
		this.systemlanguage = systemlanguage;
	}

	public String getMaritalstatus() {
		return maritalstatus;
	}

	public void setMaritalstatus(String maritalstatus) {
		this.maritalstatus = maritalstatus;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getMobilecall() {
		return mobilecall;
	}

	public void setMobilecall(String mobilecall) {
		this.mobilecall = mobilecall;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getLocationid() {
		RecordSet rs = new RecordSet();
		if(locationid == null || "".equals(locationid))
			return "1";
		String sql = "";
		int id = 0;
		// 如果传值是ID，或字符串都是可以的  如果是数字类型，就查询是否id存在
		if(locationid.matches("^\\d+$")){
			sql = "select id from HrmLocations where id='"+locationid+"'";
			rs.executeSql(sql);
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id > 0)  return String.valueOf(id);
			return "1";
 		}
		sql = "select id from HrmLocations where locationname='"+locationid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			id = rs.getInt("id");
		}
		if(id > 0) return String.valueOf(id);
		//  中文名称不存在，需要插入
		Map<String,String> mapStr = new HashMap<String,String>();
		mapStr.put("locationname", locationid);
		mapStr.put("locationdesc", locationid);
		mapStr.put("locationcity", "1");   // 城市按id 1标示，需要在系统中手动去维护
		mapStr.put("countryid", "1");      // 国家按1 即中国，如有不同需要在系统手动维护
		mapStr.put("showOrder", "0");      // 排序为0排序  如有不同需要在系统手动维护
		
		TmcDBUtil tdu = new TmcDBUtil();
		tdu.insert(mapStr, "HrmLocations");
		
		sql = "select id from HrmLocations where locationname='"+locationid+"'";
		rs.executeSql(sql);
		if(rs.next()){
			id = rs.getInt("id");
		}
		if(id > 0) return String.valueOf(id);
		return "1";
	}

	public void setLocationid(String locationid) {
		this.locationid = locationid;
	}

	public String getWorkroom() {
		return workroom;
	}

	public void setWorkroom(String workroom) {
		this.workroom = workroom;
	}

	public String getHomeaddress() {
		return homeaddress;
	}

	public void setHomeaddress(String homeaddress) {
		this.homeaddress = homeaddress;
	}

	public String getStartdate() {
		return startdate;
	}

	public void setStartdate(String startdate) {
		this.startdate = startdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getJobtitle() {
		return jobtitle;
	}

	public void setJobtitle(String jobtitle) {
		this.jobtitle = jobtitle;
	}

	public String getJobactivitydesc() {
		return jobactivitydesc;
	}

	public void setJobactivitydesc(String jobactivitydesc) {
		this.jobactivitydesc = jobactivitydesc;
	}

	public int getSeclevel() {
		return seclevel;
	}

	public void setSeclevel(int seclevel) {
		this.seclevel = seclevel;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

	public String getManagerid() {
		return managerid;
	}

	public void setManagerid(String managerid) {
		this.managerid = managerid;
	}

	public String getBankid1() {
		return bankid1;
	}

	public void setBankid1(String bankid1) {
		this.bankid1 = bankid1;
	}

	public String getAccountid1() {
		return accountid1;
	}

	public void setAccountid1(String accountid1) {
		this.accountid1 = accountid1;
	}

	public String getCreaterid() {
		return createrid;
	}

	public void setCreaterid(String createrid) {
		this.createrid = createrid;
	}

	public String getCreatedate() {
		if(createdate == null || "".equals(createdate)){
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			return df.format(new Date());
		}
		return createdate;
	}

	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}

	public String getDatefield1() {
		return datefield1;
	}

	public void setDatefield1(String datefield1) {
		this.datefield1 = datefield1;
	}

	public String getDatefield2() {
		return datefield2;
	}

	public void setDatefield2(String datefield2) {
		this.datefield2 = datefield2;
	}

	public String getDatefield3() {
		return datefield3;
	}

	public void setDatefield3(String datefield3) {
		this.datefield3 = datefield3;
	}

	public String getDatefield4() {
		return datefield4;
	}

	public void setDatefield4(String datefield4) {
		this.datefield4 = datefield4;
	}

	public String getDatefield5() {
		return datefield5;
	}

	public void setDatefield5(String datefield5) {
		this.datefield5 = datefield5;
	}

	public String getNumberfield1() {
		return numberfield1;
	}

	public void setNumberfield1(String numberfield1) {
		this.numberfield1 = numberfield1;
	}

	public String getNumberfield2() {
		return numberfield2;
	}

	public void setNumberfield2(String numberfield2) {
		this.numberfield2 = numberfield2;
	}

	public String getNumberfield3() {
		return numberfield3;
	}

	public void setNumberfield3(String numberfield3) {
		this.numberfield3 = numberfield3;
	}

	public String getNumberfield4() {
		return numberfield4;
	}

	public void setNumberfield4(String numberfield4) {
		this.numberfield4 = numberfield4;
	}

	public String getNumberfield5() {
		return numberfield5;
	}

	public void setNumberfield5(String numberfield5) {
		this.numberfield5 = numberfield5;
	}

	public String getTextfield1() {
		return textfield1;
	}

	public void setTextfield1(String textfield1) {
		this.textfield1 = textfield1;
	}

	public String getTextfield2() {
		return textfield2;
	}

	public void setTextfield2(String textfield2) {
		this.textfield2 = textfield2;
	}

	public String getTextfield3() {
		return textfield3;
	}

	public void setTextfield3(String textfield3) {
		this.textfield3 = textfield3;
	}

	public String getTextfield4() {
		return textfield4;
	}

	public void setTextfield4(String textfield4) {
		this.textfield4 = textfield4;
	}

	public String getTextfield5() {
		return textfield5;
	}

	public void setTextfield5(String textfield5) {
		this.textfield5 = textfield5;
	}

	public String getTinyintfield1() {
		return tinyintfield1;
	}

	public void setTinyintfield1(String tinyintfield1) {
		this.tinyintfield1 = tinyintfield1;
	}

	public String getTinyintfield2() {
		return tinyintfield2;
	}

	public void setTinyintfield2(String tinyintfield2) {
		this.tinyintfield2 = tinyintfield2;
	}

	public String getTinyintfield3() {
		return tinyintfield3;
	}

	public void setTinyintfield3(String tinyintfield3) {
		this.tinyintfield3 = tinyintfield3;
	}

	public String getTinyintfield4() {
		return tinyintfield4;
	}

	public void setTinyintfield4(String tinyintfield4) {
		this.tinyintfield4 = tinyintfield4;
	}

	public String getTinyintfield5() {
		return tinyintfield5;
	}

	public void setTinyintfield5(String tinyintfield5) {
		this.tinyintfield5 = tinyintfield5;
	}

	public String getCertificatenum() {
		return certificatenum;
	}

	public void setCertificatenum(String certificatenum) {
		this.certificatenum = certificatenum;
	}

	public String getNativeplace() {
		return nativeplace;
	}

	public void setNativeplace(String nativeplace) {
		this.nativeplace = nativeplace;
	}

	// 学历没有的 不新增 ，需要手动维护   只查询
	public String getEducationlevel() {
		if(educationlevel == null ||"".equals(educationlevel))
			return "";
		RecordSet rs = new RecordSet();
		String sql = "";
		int id = 0;
		// 如果传值是ID，或字符串都是可以的  如果是数字类型，就查询是否id存在
		if(educationlevel.matches("^\\d+$")){
			sql = "select id from HrmEducationLevel where id='"+educationlevel+"'";
			rs.executeSql(sql);
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id > 0)  return String.valueOf(id);
			return "";
 		}
		sql = "select id from HrmEducationLevel where name='"+educationlevel+"'";
		rs.executeSql(sql);
		if(rs.next()){
			id = rs.getInt("id");
		}
		if(id > 0)  return String.valueOf(id);
		return "";
	}

	public void setEducationlevel(String educationlevel) {
		this.educationlevel = educationlevel;
	}

	public String getWorkcode() {
		return workcode;
	}

	public void setWorkcode(String workcode) {
		this.workcode = workcode;
	}

	public String getRegresidentplace() {
		return regresidentplace;
	}

	public void setRegresidentplace(String regresidentplace) {
		this.regresidentplace = regresidentplace;
	}

	public String getHealthinfo() {
		// 0优秀 1良好  2一般  3较差
		if("优秀".equals(healthinfo)||"0".equals(healthinfo)){
			return "0";
		}
		if("良好".equals(healthinfo)||"1".equals(healthinfo)){
			return "1";
		}
		if("一般".equals(healthinfo)||"2".equals(healthinfo)){
			return "2";
		}
		if("较差".equals(healthinfo)||"3".equals(healthinfo)){
			return "3";
		}
		return "";
	}

	public void setHealthinfo(String healthinfo) {
		this.healthinfo = healthinfo;
	}

	public String getResidentplace() {
		return residentplace;
	}

	public void setResidentplace(String residentplace) {
		this.residentplace = residentplace;
	}

	public String getPolicy() {
		return policy;
	}

	public void setPolicy(String policy) {
		this.policy = policy;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getJobcall() {
		if(jobcall == null || "".equals(jobcall)){
			return "0";
		}
		RecordSet rs = new RecordSet();
		String sql = "";
		int id = 0;
		// 如果传值是ID，或字符串都是可以的  如果是数字类型，就查询是否id存在
		if(jobcall.matches("^\\d+$")){
			sql = "select id from HrmJobCall where id='"+jobcall+"'";
			rs.executeSql(sql);
			if(rs.next()){
				id = rs.getInt("id");
			}
			if(id > 0)  return String.valueOf(id);
			return "0";
 		}
		sql = "select id from HrmJobCall where name='"+jobcall+"'";
		rs.executeSql(sql);
		if(rs.next()){
			id = rs.getInt("id"); 
		}
		if(id > 0)  return String.valueOf(id);
		
		// 没有的需要新增下
		Map<String,String> mapStr = new HashMap<String,String>();
		mapStr.put("name", jobcall);
		mapStr.put("description", jobcall);
		
		TmcDBUtil tdu = new TmcDBUtil();
		tdu.insert(mapStr, "HrmJobCall");
		
		sql = "select id from HrmJobCall where name='"+jobcall+"'";
		rs.executeSql(sql);
		if(rs.next()){
			id = rs.getInt("id"); 
		}
		if(id > 0)  return String.valueOf(id);
		
		return "0";
	}

	public void setJobcall(String jobcall) {
		this.jobcall = jobcall;
	}

	public String getAccumfundaccount() {
		return accumfundaccount;
	}

	public void setAccumfundaccount(String accumfundaccount) {
		this.accumfundaccount = accumfundaccount;
	}

	public String getBirthplace() {
		return birthplace;
	}

	public void setBirthplace(String birthplace) {
		this.birthplace = birthplace;
	}

	public String getFolk() {
		return folk;
	}

	public void setFolk(String folk) {
		this.folk = folk;
	}

	public String getExtphone() {
		return extphone;
	}

	public void setExtphone(String extphone) {
		this.extphone = extphone;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getTempresidentnumber() {
		return tempresidentnumber;
	}

	public void setTempresidentnumber(String tempresidentnumber) {
		this.tempresidentnumber = tempresidentnumber;
	}

	public String getProbationenddate() {
		return probationenddate;
	}

	public void setProbationenddate(String probationenddate) {
		this.probationenddate = probationenddate;
	}

	public String getBelongto() {
		return belongto;
	}

	public void setBelongto(String belongto) {
		this.belongto = belongto;
	}
	
}
