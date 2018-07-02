package peixun;

import hsproject.util.InsertUtil;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.apache.axis.encoding.Base64;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import de.schlichtherle.io.FileOutputStream;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.workflow.webservices.WorkflowRequestInfo;
import weaver.workflow.webservices.WorkflowServiceImpl;

public class Test {
	public void writeFile() throws Exception{
		String s1 = "今天天气好";
		String fileUrl = "D:\\test\\2017062101.txt";
		File newfile=new File(fileUrl); 
		if(newfile.exists())//若在该目录下已经有一个文件和新文件名相同，则不允许重命名 
            System.out.println("已经存在！"); 
        else{ 
        	//创建文件
        	newfile.createNewFile();
        	//文件输出流
	        FileOutputStream fos = new FileOutputStream(newfile);
	        //字符输出流
	        BufferedWriter output = new BufferedWriter(new OutputStreamWriter(fos,"unicode"));  
	        //将字符串写入到文件中
	        output.write(s1);  
	        //关闭输出流
	        output.close();  
        }
	}
	public void fileread() throws Exception{
		//文件路径
		String fileUrl = "D:\\test\\2017062101.txt";
		String uploadBuffer="";
		//根据路径 创建file对象
		File file =  new File(fileUrl);
		//读取文件流 读取文件有多种方式这是其中一种按字节读取
		FileInputStream fi = new FileInputStream(file);
		  ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		  	//按1024字节大写一次次读取文件
		    byte[] buffer = new byte[1024]; 
		    int count = 0; 
		    while((count = fi.read(buffer)) >= 0){ 
		        baos.write(buffer, 0, count); 
	        } 
		    uploadBuffer =  baos.toString();
	}
	public void testaa(){
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("hkr", hkr);// 
		mapStr.put("jkid", jkid);
		mapStr.put("hkje", hkje);
		mapStr.put("hkrq", hkrq);
		mapStr.put("xglc", xglc);
		mapStr.put("hkxh", sysNo);
		mapStr.put("bz", bz);
		mapStr.put("hkfs", hkfs);
		mapStr.put("pzhm", pzhm);
		//其他5个字段
		mapStr.put("modedatacreater", hkr);
		mapStr.put("modedatacreatertype", "0");
		mapStr.put("modedatacreatedate", nowDate);
		mapStr.put("modedatacreatetime", nowTime);
		mapStr.put("formmodeid", "239");
		iu.insert(mapStr, "tablename");
	}
	public void insertHistory(String billid, String table_name,
			String uqField, String uqVal) {

		BaseBean log = new BaseBean();
		RecordSet rs = new RecordSet();
		InsertUtil iu = new InsertUtil();
		String createrid = "";

		// 存放 表的字段
		List<String> list = new ArrayList<String>();
		String sql = "select fieldname from workflow_billfield where billid="
				+ billid + " order by dsporder";
		// log.writeLog("insertNow(1) = " + sql);
		rs.executeSql(sql);
		while (rs.next()) {
			String tmp_1 = Util.null2String(rs.getString("fieldname"));

			// 关联父类排除
			if (!"".equals(tmp_1) && !"superid".equalsIgnoreCase(tmp_1)) {
				list.add(tmp_1);
			}
		}
		if (!"".equals(table_name)) {

			Map<String, String> mapStr = new HashMap<String, String>();

			sql = "select * from " + table_name + "  where " + uqField + "='"
					+ uqVal + "'";
			// log.writeLog("insertNow(2) = " + sql);
			rs.execute(sql);
			if (rs.next()) {
				// 循环获取 不为空值的组合成sql
				for (String field : list) {
					String tmp_x = Util.null2String(rs.getString(field));
					if (tmp_x.length() > 0)
						mapStr.put(field, tmp_x);
				}
			}
			createrid = rs.getString("modedatacreater");
			// 最后需要补充关联父id
			if (mapStr.size() > 0) {
				mapStr.put("superid", Util.null2String(rs.getString("ID")));
				// 增加请求的id
				mapStr.put("requestid",
						Util.null2String(rs.getString("requestid")));
				mapStr.put("modedatacreater",
						Util.null2String(rs.getString("modedatacreater")));
				mapStr.put("modedatacreatertype",
						Util.null2String(rs.getString("modedatacreatertype")));
				mapStr.put("formmodeid",
						Util.null2String(rs.getString("formmodeid")));
				iu.insert(mapStr, table_name);
			}
		}

	}
	/**
	 * 根据关键字段获取下一个编号
	 * @param str 关键字段用于流水，比如根据类型流水，这就是类型值，
	 * 		  若关键字段有多个，也可以多加几个参数
	 * @param tableName 也是关键字段 
	 * @param index 流水号位数
	 * @return
	 */
	public String getNum(String str,String tableName,int index){
		if(tableName == null) return "";
		tableName = tableName.toUpperCase();
		InsertUtil iu = new InsertUtil();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String now = sdf.format(new Date());
		RecordSet rs = new RecordSet();
		
		int count_cc = 0;
		//首先通过关键字段判断这些关键字段组成的流水在表中数都存在过记录
		String sql = "select count(id) as count_cc from uf_SysNo where strsys='"+str+"' and " 
			 +" datesys='"+now+"' and tableName='"+tableName+"'"; 
		rs.executeSql(sql);
		if(rs.next()){
			count_cc = rs.getInt("count_cc");
		}
		
		int num  = 1;
		//若存在国基路就取下一个流水号
		if(count_cc > 0){
			sql = "select nextseq from uf_SysNo where strsys='"+str+"' and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";  
			rs.executeSql(sql);
			if(rs.next()){
				num = rs.getInt("nextseq");
			}
			//更新表中关键字段对应的流水号
			sql = "update uf_SysNo set nextseq=nextseq+1 where strsys='"+str+"' and " 
					 +" datesys='"+now+"' and tableName='"+tableName+"'";
			rs.executeSql(sql);
			
		}else{
			//若不存在关键字段对应的记录则新增一条
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("strsys", str);
			mapStr.put("datesys", now);
			mapStr.put("nextseq", "2");
			mapStr.put("tablename", tableName);
			
			iu.insert(mapStr, "uf_SysNo");
		}		
		//根据需求拼接编号
		String tmp = str + now + getStrNum(num,index);
		
		return tmp;
	}
	/**
	 * 根据流水位数和流水号拼接编号 比如3位流水，流水号2 =002
	 * @param num 流水号
	 * @param len 流水位数
	 * @return
	 */
	public String getStrNum(int num,int len){
		String buff = String.valueOf(num);
		int max = len - buff.length();
		for(int index = 0; index < max;index++){
			buff = "0" + buff;
		}
		return buff;
	}
	/**
	 * 退回流程
	 * @param requestid 流程号
	 * @param userid 用户id
	 * @return
	 */
	public String AutoBackV0006(String requestid,String userid){
		  WorkflowServiceImpl ws = new WorkflowServiceImpl();
		  WorkflowRequestInfo wri = new WorkflowRequestInfo();
		  String result=ws.submitWorkflowRequest(wri, Integer.valueOf(requestid), Integer.valueOf(userid), "reject", "自动退回");   
		  return result;
	  }
	/**
	 * 提交流程
	 * @param requestid 流程号
	 * @param userid 用户id
	 * @return
	 */
	public String AutoSubmitV0006(String requestid, String userid) {
		WorkflowServiceImpl ws = new WorkflowServiceImpl();
		WorkflowRequestInfo wri = new WorkflowRequestInfo();
		String result = ws.submitWorkflowRequest(wri,
				Integer.valueOf(requestid), Integer.valueOf(userid), "submit",
				"自动提交");
		return result;
	}
	//新建一个全局变量ftp
	 private  FTPClient ftp; 
	/**
	 * 建立连接FTP
	 * @param path 路径
	 * @param addr ip地址
	 * @param port 端口
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 * @throws Exception
	 */
	private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {      
        boolean result = false;      
        ftp = new FTPClient();      
        int reply;      
        ftp.connect(addr, port);    
        ftp.login(username,password);      
        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);      
        reply = ftp.getReplyCode();   
        log.writeLog("开始测试reply"+reply);
        if (!FTPReply.isPositiveCompletion(reply)) {       
            ftp.disconnect();      
            return result;      
        }      
        ftp.changeWorkingDirectory(path);      
        result = true;      
        return result;      
    } 
	/**
	 * 上传照片到FTP
	 * @param path 本地文件路径
	 * @param name 生成文件名
	 * @throws Exception
	 */
	public void upload(String path,String name) throws Exception{
		 
		 boolean flag=connect("ftp://172.20.70.200/Ready/","172.20.70.200",21,"ecctest","ecctest");		 
		 if(!flag){
			 log.writeLog("创建连接失败:path:"+path+" name:"+name);
			 return;
		 }
		 File file = new File(path);       
         FileInputStream input = new FileInputStream(file);  
         name="/Ready/"+name;
         ftp.enterLocalPassiveMode();
         ftp.storeFile(new String(name.getBytes("UTF-8"),"iso-8859-1"), input);         
         input.close(); 
         if (ftp.isConnected()) {     	 
        	   ftp.disconnect();       	  
         }	  

	 }
	/**
	 * 将多个系统文件添加到一个压缩文件里面
	 * @param docids
	 * @return
	 */
	public  String  getFile(String docids){
		String uploadBuffer = "";
		RecordSet rs = new RecordSet();
		String filerealpath = "";//文档存放路径
		String iszip = "";//是否是压缩包
		String name = "";//文档名字
        String zipName = "/weaver/ecology/gvo/file/testaa.zip";
        File zipFile = new File(zipName);
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(
                   zipFile));
        String sql = " select b.filerealpath,b.iszip,b.imagefilename,b.imagefileid from  "
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
                   InputStream is = getFile(filerealpath, iszip);
                   // log.writeLog("InputStream is"+is.toString()+" name:"+name);
                   zipOut.setEncoding("GBK");
                   zipOut.putNextEntry(new ZipEntry(name));
                   int temp = 0;
                   byte[] buffer = new byte[1024];
                   while ((temp = is.read(buffer)) >= 0) {
                        zipOut.write(buffer, 0, temp);
                   }
                   is.close();
             }
        }
        zipOut.close();
        FileInputStream fi = new FileInputStream(new File(zipName));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = fi.read(buffer)) >= 0) {
             baos.write(buffer, 0, count);
        }
        fi.close();
        uploadBuffer = new String(Base64.encode(baos.toByteArray()));
        File file = new File(zipName);
        if (file.exists()) {
             file.delete();
        }
        return uploadBuffer;
   }
   /**
    * 根据系统文档存放路径 获取文档流
    * @param filerealpath 文档存放路径
    * @param iszip 是否压缩包
    * @return
    * @throws Exception
    */
   private InputStream getFile(String filerealpath, String iszip)
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

}
