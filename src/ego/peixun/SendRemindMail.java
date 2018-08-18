package ego.peixun;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.SendMail;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.system.SystemComInfo;

public class SendRemindMail implements Action{
	
	public String execute(RequestInfo info) {
		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		SystemComInfo sci = new SystemComInfo();
		String mailip = sci.getDefmailserver();//
		String mailuser = sci.getDefmailuser();
		String password = sci.getDefmailpassword();
		String needauth = sci.getDefneedauth();// 是否需要发件认证
		String mailfrom = sci.getDefmailfrom();
		SendMail sm = new SendMail();
		sm.setMailServer(mailip);// 邮件服务器IP
		if (needauth.equals("1")) {
			sm.setNeedauthsend(true);
			sm.setUsername(mailuser);// 服务器的账号
			sm.setPassword(password);// 服务器密码
		} else {
			sm.setNeedauthsend(false);
		}
		String tableName = "";
		String mainID = "";
		String docids = "";
		String filerealpath = "";
		String iszip = "";
		String name = "";
		String Courselist = "";//课程清单
		String Trainingmaterial = "";//培训资料
		String Name="";//培训人姓名
		String Dept="";//培训人部门
		String GH="";//培训人工号
		String startdate = "";
		String finishdate = "";
		String shi1 = "";
		String shi2 = "";
		String PXDD = "";//培训地点
		String Trainingtype = "";//培训类别
		String PXLXR = "";//培训联系人
		String Trainer = "";//培训师
		String Comments = "";//备注
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid=" + requestid;
		rs.execute(sql);
		if (rs.next()) {
			mainID = Util.null2String(rs.getString("id"));
			Courselist =  Util.null2String(rs.getString("Courselist"));
			Trainingmaterial = Util.null2String(rs.getString("Trainingmaterial"));
			startdate = Util.null2String(rs.getString("startdate"));
			finishdate = Util.null2String(rs.getString("finishdate"));
			shi1 = Util.null2String(rs.getString("shi1"));
			shi2 = Util.null2String(rs.getString("shi2"));
			PXDD = Util.null2String(rs.getString("PXDD"));
			Trainingtype = Util.null2String(rs.getString("Trainingtype"));
			PXLXR = Util.null2String(rs.getString("PXLXR"));
			Trainer = Util.null2String(rs.getString("Trainer"));
			Comments = Util.null2String(rs.getString("Comments"));
		}
		
		docids=Trainingmaterial;
		sql="select TrainingType from uf_YHDQD where id="+Trainingtype;
		rs.executeSql(sql);
		if(rs.next()){
			Trainingtype = Util.null2String(rs.getString("TrainingType"));
		}
//		sql="select CourseList from uf_CourseList where id="+Courselist;
//		rs.executeSql(sql);
//		if(rs.next()){
//			Courselist = Util.null2String(rs.getString("CourseList"));
//		}
		//String Courselist7=Courselist.substring(Courselist.indexOf("`~`7")+4, Courselist.indexOf("`~`8"));
		//String Courselist8=Courselist.substring(Courselist.indexOf("`~`8")+4, Courselist.indexOf("`~`~"));
		String Trainingtype7=Trainingtype.substring(Trainingtype.indexOf("`~`7")+4, Trainingtype.indexOf("`~`8"));
		String Trainingtype8=Trainingtype.substring(Trainingtype.indexOf("`~`8")+4, Trainingtype.indexOf("`~`~"));
		String subject = "E.G.O.China培训人员报备通知  E.G.O.China Trainee Training Notice";
		StringBuffer body = new StringBuffer();
		body.append("Hello All,<br>");
		body.append("    应公司发展需要，为全面提升员工能力，现决定开展一项题为（");body.append(Courselist);body.append("）的专题培训。为此，我们真诚地邀请以下员工参与此次培训<br>");
		body.append("    培训名单:<br>");
		body.append("<table border=\"2\"  style=\"width: 360px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>员工号</strong></td>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>姓名</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>部门</strong></td>" ); 
		body.append("</tr>");
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		sql="select * from "+tableName+"_dt3 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			Map<String, String> map = new HashMap<String,String>();
			Name = Util.null2String(rs.getString("Name"));
			Dept = Util.null2String(rs.getString("Dept"));
			GH = Util.null2String(rs.getString("GH"));
			map.put("sendemail", getSendMail(Name));
			map.put("ccemail", getCCMail(Name));
			list.add(map);
			String departmentname = "";
			String personname="";
			try{
				departmentname = new DepartmentComInfo().getDepartmentname(Dept);
				personname = new ResourceComInfo().getLastname(Name);
			}catch(Exception e){
				
			}
			body.append("<tr>"); 
			body.append("	<td style=\"text-align:left;\">");body.append(GH); body.append("</td>");
			body.append("	<td style=\"text-align:left;\">");body.append(personname); body.append("</td>");
			body.append("	<td style=\"text-align:left;\">");body.append(departmentname); body.append("</td>");
			body.append("</tr>");
			
		}
		body.append("</table>");
		body.append("    培训类别：");body.append(Trainingtype7);body.append("<br>");
		body.append("    培训起止时间： ");body.append(startdate+" "+shi1+"~"+finishdate+" "+shi2);body.append("<br>");
		body.append("    培训地点：");body.append(PXDD);body.append("<br>");
		body.append("    联系人：");body.append(PXLXR);body.append("<br>");
		body.append("    培训师：");body.append(Trainer);body.append("<br>");
		body.append("    备注：");body.append(Comments);body.append("<br>");
		body.append("    感谢各部门的支持<br><br>");
		body.append("    此邮件为系统自动发送，请不要回复！如果有疑问，请直接联系Sissi Qian或者Julie Yi。<br><br><br><br>");
		body.append("Hello All,<br>");
		body.append("    In order to improve employees' ability to meet company's development, Now we decide to start a topic training(");body.append(Courselist);body.append(").<br>");
		body.append("    For this purpose, we sincerely invite the following employees to participate in this training.");
		body.append("    Trainees' list:<br>");
		body.append("<table border=\"2\"  style=\"width: 360px;border-collapse: collapse;font-size:12px;\"> ");
		body.append("<tr>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Personal NO.</strong></td>"); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Name</strong></td>" ); 
		body.append("	<td style=\"background: LightGrey;text-align:center;width: 120px;\"><strong>Department</strong></td>" ); 
		body.append("</tr>");
		sql="select * from "+tableName+"_dt3 where mainid="+mainID;
		rs.executeSql(sql);
		while(rs.next()){
			Name = Util.null2String(rs.getString("Name"));
			Dept = Util.null2String(rs.getString("Dept"));
			GH = Util.null2String(rs.getString("GH"));
			String departmentname = "";
			String personname="";
			try{
				departmentname = new DepartmentComInfo().getDepartmentname(Dept);
				personname = new ResourceComInfo().getLastname(Name);
			}catch(Exception e){
				
			}
			body.append("<tr>"); 
			body.append("	<td style=\"text-align:left;\">");body.append(GH); body.append("</td>");
			body.append("	<td style=\"text-align:left;\">");body.append(personname); body.append("</td>");
			body.append("	<td style=\"text-align:left;\">");body.append(departmentname); body.append("</td>");
			body.append("</tr>");
		}	
		body.append("</table>");
		body.append("    Training type: ");body.append(Trainingtype8);body.append("<br>");
		body.append("    Training start time and finish time: ");body.append(startdate+" "+shi1+"~"+finishdate+" "+shi2);body.append("<br>");
		body.append("    Training center: ");body.append(PXDD);body.append("<br>");
		body.append("    Contact Person: ");body.append(PXLXR);body.append("<br>");
		body.append("    Trainer: ");body.append(Trainer);body.append("<br>");
		body.append("    Comment:");body.append(Comments);body.append("<br>");
		body.append("    Thanks for your support<br><br>");
		body.append("    This mail was sent by system automatically,Please do not reply! Please contact Sissi Qian/Julie Yi about any questions.");
		
		
		
		//String filepath = "D:\\weaver\\ecology\\emailfile\\";
		String zipName = "D:\\weaver\\ecology\\emailfile\\attach"+requestid+".zip";
		try {
			createFile(zipName);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		File zipFile = new File(zipName);
		ZipOutputStream zipOut = null;
		try {
			zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		sql = " select b.filerealpath,b.iszip,b.imagefilename,b.imagefileid from  "
				+ " imagefile b  where b.imagefileid in(select max(imagefileid) "
				+ "from docimagefile where docid in("
				+ docids
				+ ") group by docid)";
		rs.executeSql(sql);
		while (rs.next()) {
			filerealpath = Util.null2String(rs.getString("filerealpath"));
			iszip = Util.null2String(rs.getString("iszip"));
			name = Util.null2String(rs.getString("imagefilename"));
			if (filerealpath.length() > 0) {

				try {
					InputStream is = getFile(filerealpath, iszip);
					zipOut.setEncoding("GBK");
					zipOut.putNextEntry(new ZipEntry(name));
					int temp = 0;
					byte[] buffer = new byte[1024];
					while ((temp = is.read(buffer)) >= 0) {
						zipOut.write(buffer, 0, temp);
					}
					is.close();

				} catch (Exception e) {

					e.printStackTrace();
				}
			}

		}
		
		for(Map<String, String> map:list){
			if("".equals(map.get("sendemail"))){
				continue;
			}
			ArrayList<String> filenames = new ArrayList<String>();
			ArrayList<InputStream> filecontents = new ArrayList<InputStream>();
			InputStream fi = null;
			if(!"".equals(Trainingmaterial)){		
				try {
					
					fi = new FileInputStream(new File(zipName));
					filenames.add("attach.zip");
					filecontents.add(fi);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			boolean result = sm.sendMiltipartHtml(mailfrom, map.get("sendemail"),
					map.get("ccemail"), "", subject, body.toString(), 3, filenames,
					filecontents, "3");
			try {
				zipOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		File file = new File(zipName);
		if (file.exists()) {
			file.delete();
		}
		
		return SUCCESS;
	}
	
	public InputStream getFile(String filerealpath, String iszip)
			throws Exception {
		ZipInputStream zin = null;
		InputStream imagefile = null;
		File thefile = new File(filerealpath);
		if (iszip.equals("1")) {
			zin = new ZipInputStream(new FileInputStream(thefile));
			if (zin.getNextEntry() != null)
				imagefile = new BufferedInputStream(zin);
		} else {
			imagefile = new BufferedInputStream(new FileInputStream(thefile));
		}
		return imagefile;
	}

	public boolean createFile(String destFileName) throws IOException {
		File file = new File(destFileName);
		if (file.exists()) {
			file.delete();
		}
		if (destFileName.endsWith(File.separator)) {
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public String getSendMail(String chr){
		RecordSet rs = new RecordSet();
		String email = "";
		String managerid = "";
		String sql = "";
		boolean flag = true;
		sql="select email,managerid from hrmresource  where id="+chr;
		rs.executeSql(sql);
		if(rs.next()){
			email = Util.null2String(rs.getString("email"));
			managerid = Util.null2String(rs.getString("managerid"));
		}
		if("".equals(email)){
			while(flag){
				if("".equals(managerid)){
					break;
				}
				sql="select email,managerid from hrmresource  where id="+managerid;
				rs.executeSql(sql);
				if(rs.next()){
					email = Util.null2String(rs.getString("email"));
					managerid = Util.null2String(rs.getString("managerid"));
				}
				if(!"".equals(email)){
					flag = false;
				}
			}
		}
		return email;
	}
	
	public String getCCMail(String chr){
		RecordSet rs = new RecordSet();
		String email = "";
		String managerid = "";
		String sql = "";
		sql="select managerid from hrmresource  where id="+chr;
		rs.executeSql(sql);
		if(rs.next()){
			managerid = Util.null2String(rs.getString("managerid"));
		}
		if(!"".equals(managerid)){
			sql="select email from hrmresource  where id="+managerid;
			rs.executeSql(sql);
			if(rs.next()){
				email = Util.null2String(rs.getString("email"));
			}
		}
		return email;
	}
}
