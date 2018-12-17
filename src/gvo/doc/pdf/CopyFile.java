package gvo.doc.pdf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import weaver.conn.RecordSet;
import weaver.file.AESCoder;
import weaver.general.BaseBean;
import weaver.general.GCONST;

public class CopyFile extends BaseBean{
	private static final String [] PATHS = {"image","css"};
	private String parentPath;
	private int filetype;
	
	/**
	 * @return the parentPath
	 */
	public String getParentPath() {
		return parentPath;
	}

	/**
	 * @param parentPath the parentPath to set
	 */
	public void setParentPath(String parentPath) {
		this.parentPath = parentPath;
	}

	/**
	 * @return the filetype
	 */
	public int getFiletype() {
		return filetype;
	}

	/**
	 * @param filetype the filetype to set
	 */
	public void setFiletype(int filetype) {
		this.filetype = filetype;
	}

	/**
	 * 根据路径，复制文件
	 * @param filePath
	 * @return
	 */
	public String [] copyFile(String filePath){
		String [] strs = {"",""};
		FileInputStream fis = null;
		FileOutputStream fos = null;
		if(filetype == 0){   //图片
			try{
				File sourceFile = new File(filePath);
				if(sourceFile.exists()){
					int index = filePath.lastIndexOf(".");
					String subfix = "";
					if(index > 0){
						subfix = filePath.substring(index);
					}
					
					fis = new FileInputStream(sourceFile);
					byte [] data = new byte[8*1024];
					
					//将图片copy到新的文件中
					String destPath = parentPath + PATHS[filetype];
					File dir = new File(destPath);
					if(!dir.exists()){
						dir.mkdirs();
					}
					String dfile =  UUID.randomUUID() + subfix;
					File destFile = new File(destPath,dfile);
					fos = new FileOutputStream(destFile);
					int count = 0;
					while((count = fis.read(data)) != -1){
						fos.write(data,0,count);
					}
					
					String absolutePath = destFile.getAbsolutePath();
					String relativePath = PATHS[filetype] + "/" + dfile;
					strs[0] = absolutePath;
					strs[1] = relativePath;
				}
			}catch(IOException e){
				writeLog("copy图片文件出错，path=" + filePath + "，信息:" + e.getMessage());
			}finally{
				try {
					if(fos != null){
						fos.close();
					}
					if(fis != null){
						fis.close();
					}
				} catch (Exception e2) {
				}
			}
		}else{   //css文件
			File sourceFile = new File(filePath);
			if(sourceFile.exists()){
				//将图片copy到新的文件中
				String destPath = parentPath + PATHS[filetype];
				File dir = new File(destPath);
				if(!dir.exists()){
					dir.mkdirs();
				}
				String dfile =  UUID.randomUUID() + ".css";
				File destFile = new File(destPath,dfile);
				
				//复制css文件
				copyCss(sourceFile, destFile);
				
				String absolutePath = destFile.getAbsolutePath();
				String relativePath = PATHS[filetype] + "/" + dfile;
				strs[0] = absolutePath;
				strs[1] = relativePath;
			}
		}
		return strs;
	}
	
	/**
	 * 根据附件的id复制文件
	 * @param imageId
	 * @return
	 */
	public String [] copyFile1(int imageId){
		String [] strs = {"",""};
		String filerealpath = "";
		String imagefilename = "";
		String isaesencrypt = "";
		String aescode = "";
		String iszip = "";
		
		if(imageId > 0){
			String sql = "select imagefilename,filerealpath,isaesencrypt,aescode,iszip from ImageFile where imagefileid = " + imageId;
			RecordSet rs = new RecordSet();
			rs.executeSql(sql);
			if(rs.next()){
				filerealpath = rs.getString("filerealpath");
				imagefilename = rs.getString("imagefilename");
				isaesencrypt = rs.getString("isaesencrypt");
				aescode = rs.getString("aescode");
				iszip = rs.getString("iszip");
			}
		}
		
		InputStream is = null;
		if(!"".equals(filerealpath)){
			try{
				File file = new File(filerealpath);
				if("1".equals(iszip)){
					ZipFile zipFile = new ZipFile(file);
					Enumeration<ZipEntry> enu = (Enumeration<ZipEntry>) zipFile.entries();  
					//读取压缩的文件内容
					if(enu.hasMoreElements()){
						ZipEntry zipElement = enu.nextElement();
						is = zipFile.getInputStream(zipElement);
					}
				}else {
					is = new FileInputStream(file);
				}
				
				//解密
				if("1".equals(isaesencrypt)){
					is = AESCoder.decrypt(is, aescode);
				}
			}catch (Exception e) {
				writeLog("解压缩、解密文件出错：" + e.getMessage());
			}
		}
		
		if(is != null){
			int index = imagefilename.lastIndexOf(".");
			String subfix = "";
			if(index > 0){
				subfix = imagefilename.substring(index);
			}
			
			//将图片copy到新的文件中
			String destPath = parentPath + PATHS[filetype];
			File dir = new File(destPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String dfile =  UUID.randomUUID() + subfix;
			File destFile = new File(destPath,dfile);
			
			FileOutputStream fos = null;
			byte [] data = new byte[8*1024];
			try{
				fos = new FileOutputStream(destFile);
				int count = 0;
				while((count = is.read(data)) != -1){
					fos.write(data,0,count);
				}
				
				String absolutePath = destFile.getAbsolutePath();
				String relativePath = PATHS[filetype] + "/" + dfile;
				strs[0] = absolutePath;
				strs[1] = relativePath;
			}catch (Exception e) {
				writeLog("从文件系统copy文件出错1：" + e.getMessage());
			}finally{
				try {
					if(fos != null){
						fos.close();
					}
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		
		return strs;
	}
	
	/**
	 * 根据用户id，复制签章图片
	 * @param userid
	 * @return
	 */
	public String [] copyFile2(int userid){
		String [] strs = {"",""};
		String filerealpath = "";
		String subfix = "";
		if(userid > 0){
			String sql = "select * from DocSignature where hrmresid=" + userid + " order by markid";;
			RecordSet rs = new RecordSet();
			rs.executeSql(sql);
			if(rs.next()){
				filerealpath = rs.getString("markpath");
				subfix = rs.getString("marktype");
			}
		}
	
		if(!"".equals(filerealpath)){
			File file = new File(filerealpath);
			if(!file.exists()){
				return strs;
			}
			//将图片copy到新的文件中
			String destPath = parentPath + PATHS[filetype];
			File dir = new File(destPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String dfile =  UUID.randomUUID() + subfix;
			File destFile = new File(destPath,dfile);
			InputStream is = null;
			FileOutputStream fos = null;
			byte [] data = new byte[8*1024];
			try{
				is = new FileInputStream(file);
				fos = new FileOutputStream(destFile);
				int count = 0;
				while((count = is.read(data)) != -1){
					fos.write(data,0,count);
				}
				
				String absolutePath = destFile.getAbsolutePath();
				String relativePath = PATHS[filetype] + "/" + dfile;
				strs[0] = absolutePath;
				strs[1] = relativePath;
			}catch (Exception e) {
				writeLog("从文件系统copy文件出错2：" + e.getMessage());
			}finally{
				try {
					if(fos != null){
						fos.close();
					}
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		return strs;
	}
	
	/**
	 * 根据markid，复制签章图片
	 * @param userid
	 * @return
	 */
	public String [] copyFile3(int markid){
		String [] strs = {"",""};
		String filerealpath = "";
		String subfix = "";
		if(markid > 0){
			String sql = "select markPath from DocSignature where markId = " + markid;
			RecordSet rs = new RecordSet();
			rs.executeSql(sql);
			if(rs.next()){
				filerealpath = rs.getString("markpath");
				subfix = rs.getString("marktype");
			}
		}
	
		if(!"".equals(filerealpath)){
			File file = new File(filerealpath);

			//将图片copy到新的文件中
			String destPath = parentPath + PATHS[filetype];
			File dir = new File(destPath);
			if(!dir.exists()){
				dir.mkdirs();
			}
			String dfile =  UUID.randomUUID() + subfix;
			File destFile = new File(destPath,dfile);
			InputStream is = null;
			FileOutputStream fos = null;
			byte [] data = new byte[8*1024];
			try{
				is = new FileInputStream(file);
				fos = new FileOutputStream(destFile);
				int count = 0;
				while((count = is.read(data)) != -1){
					fos.write(data,0,count);
				}
				
				String absolutePath = destFile.getAbsolutePath();
				String relativePath = PATHS[filetype] + "/" + dfile;
				strs[0] = absolutePath;
				strs[1] = relativePath;
			}catch (Exception e) {
				writeLog("从文件系统copy文件出错3：" + e.getMessage());
			}finally{
				try {
					if(fos != null){
						fos.close();
					}
					if(is != null){
						is.close();
					}
				} catch (Exception e2) {
				}
			}
		}
		return strs;
	}
	
	private void copyCss(File sourceFile,File destFile){
		BufferedReader reader = null;
		BufferedWriter writer = null;
		String lineStr = "";
		try{
			reader = new BufferedReader(new FileReader(sourceFile));
			writer = new BufferedWriter(new FileWriter(destFile,true));
			while((lineStr = reader.readLine()) != null){
				if((lineStr.indexOf("import") != -1 || lineStr.indexOf("IMPORT") != -1) && lineStr.indexOf("url") != -1){
					int startindex = lineStr.indexOf("url(\"");
					int endindex = lineStr.indexOf("\")", startindex);
					String subpath = "";
					String subcss = "";
					if(endindex > startindex + 5){
						subpath = lineStr.substring(startindex + 5, endindex);
					}else{
						subpath = lineStr.substring(startindex + 5);
					}
					if(!"".equals(subpath) && subpath.endsWith(".css")){   //@import的css
						if(subpath.startsWith("/")){   //绝对目录
							String ppath = GCONST.getRootPath();
							subpath = subpath.substring(1);
							File subFile = new File(ppath,subpath);
							copyCss(subFile, destFile);
						}else{   //相对目录
							String ppath = sourceFile.getParent();
							File subFile = new File(ppath,subpath);
							copyCss(subFile, destFile);
						}
					}
				}else {
					writer.write(lineStr);
					writer.write("\r\n");
				}
			}
		}catch (IOException e) {
			writeLog("复制css文件出错:" + e.getMessage());
		}finally{
			try {
				if(writer != null){
					writer.close();
				}
				if(writer != null){
					writer.close();
				}
			} catch (Exception e2) {
			}
		}
	}
}
