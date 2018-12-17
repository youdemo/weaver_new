/**
 * 
 */
package gvo.doc.pdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.Util;

/**
 * @author fmj
 *
 */
public class ParseHtml extends BaseBean{
	private String isaesencrypt;
	private String aescode;
	private String parentPath;
	private String requestname;
	private String requestid;
	
	/**
	 * @return the isaesencrypt
	 */
	public String getIsaesencrypt() {
		return isaesencrypt;
	}

	/**
	 * @param isaesencrypt the isaesencrypt to set
	 */
	public void setIsaesencrypt(String isaesencrypt) {
		this.isaesencrypt = isaesencrypt;
	}

	/**
	 * @return the aescode
	 */
	public String getAescode() {
		return aescode;
	}

	/**
	 * @param aescode the aescode to set
	 */
	public void setAescode(String aescode) {
		this.aescode = aescode;
	}

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
	 * @return the requestname
	 */
	public String getRequestname() {
		return requestname;
	}

	/**
	 * @param requestname the requestname to set
	 */
	public void setRequestname(String requestname) {
		this.requestname = requestname;
	}

	/**
	 * 解析html文件，以能够离线查看
	 * @param htmlPath
	 */
	public String parseHtml(File htmlFile){
		CopyFile copyFile = new CopyFile();
		List<String> cssList = new ArrayList<String>();
		List<String> imageList = new ArrayList<String>();
		
		String charset = "UTF-8";
		String rootPath = GCONST.getRootPath();  //根目录
		String tempPath = parentPath + UUID.randomUUID() + "/";   //生成一个随机的目录
		File tempFile = new File(parentPath);
		if(!tempFile.exists()){
			tempFile.mkdirs();
		}
		copyFile.setParentPath(tempPath);
		
		Document root = null;
		try{
			root = Jsoup.parse(htmlFile, charset);
		}catch (IOException e) {
			writeLog("解析html出错：" + e.getMessage());
		}
		if(root == null){
			return null;
		}
		
		/**Elements searchDiv = root.select("#signscrollfixed");   //签字意见的搜索区域
		if(searchDiv.size() > 0){
			searchDiv.get(0).remove(); 
		}*/
		
		//所有的a标签，去除超链,改为文本
		Elements aelement = root.select("a[href]");
		for(Element a : aelement){
			a.tagName("span");
			a.removeAttr("href");
			a.removeAttr("target");
		}
		
		//删除所有的js
		Elements scripts = root.select("script");
		for(Element script : scripts){
			script.remove();
		}
		
		//删除所有的onclick,onmouseover,onmouseout,onload事件
		Elements evts1 = root.select("[onclick]");
		for(Element evt1 : evts1){
			evt1.removeAttr("onclick");
		}
		Elements evts2 = root.select("[onmouseover]");
		for(Element evt2 : evts2){
			evt2.removeAttr("onmouseover");
		}
		Elements evts3 = root.select("[onmouseout]");
		for(Element evt3 : evts3){
			evt3.removeAttr("onmouseout");
		}
		Elements evts4 = root.select("[onload]");
		for(Element evt4 : evts4){
			evt4.removeAttr("onload");
		}
		//所有的css，copy到html目录下
		Elements csss = root.select("link[href]");
		for(Element css : csss){
			String cssPath = Util.null2String(css.attr("href"));
			if("".equals(cssPath)){
				continue;
			}
			String realPath = "";
			if(cssPath.startsWith("/")){  //相对根目录的路径
				realPath = rootPath + cssPath;
			}else {    //相对当前目录的路径，/workflow/request/xxxxx
				realPath = rootPath + "/workflow/request/" + cssPath;
			}
			copyFile.setFiletype(1);  //css文件
			String [] paths = copyFile.copyFile(realPath);
			String absolutePath = paths[0]; //绝对路径
			String relativePath = paths[1];    //相对html目录的路径
			cssList.add(absolutePath);
			if(!"".equals(relativePath)){
				css.attr("href", relativePath);
			}
		}
		
		//所有的图片，copy到html目录下
		Elements imgs = root.select("img[src]");
		for(Element img : imgs){
			String imgPath = Util.null2String(img.attr("src"));
			if("".equals(imgPath)){
				continue;
			}
			String realPath = "";
			String [] paths;
			
			if(imgPath.indexOf("/weaver/weaver.file.FileDownload?fileid=") != -1){//附件上传
				int fileid = 0;
				int startIndex = imgPath.indexOf("fileid=");
				int endIndex = imgPath.indexOf("&", startIndex);
				if(endIndex > startIndex + 7){
					fileid = Util.getIntValue(imgPath.substring(startIndex + 7,endIndex),0);
				}else{
					fileid = Util.getIntValue(imgPath.substring(startIndex + 7),0);
				}
				copyFile.setFiletype(0);  //image文件
				paths = copyFile.copyFile1(fileid);
				
			}else if(imgPath.indexOf("/weaver/weaver.file.SignatureDownLoad?markId=") != -1){  //签章
				int markid = 0;
				int startIndex = imgPath.indexOf("markId=");
				int endIndex = imgPath.indexOf("&", startIndex);
				if(endIndex > startIndex + 7){
					markid = Util.getIntValue(imgPath.substring(startIndex + 7,endIndex),0);
				}else{
					markid = Util.getIntValue(imgPath.substring(startIndex + 7),0);
				}
				copyFile.setFiletype(0);  //image文件
				paths = copyFile.copyFile3(markid);
			}else if(imgPath.indexOf("/weaver/weaver.file.ImgFileDownload?userid=") != -1){  //签章
				int userid = 0;
				int startIndex = imgPath.indexOf("userid=");
				int endIndex = imgPath.indexOf("&", startIndex);
				if(endIndex > startIndex + 7){
					userid = Util.getIntValue(imgPath.substring(startIndex + 7,endIndex),0);
				}else{
					userid = Util.getIntValue(imgPath.substring(startIndex + 7),0);
				}
				copyFile.setFiletype(0);  //image文件
				paths = copyFile.copyFile2(userid);
			}else{
				if(imgPath.startsWith("/")){   //相对根目录的路径
					realPath = rootPath + imgPath;
				}else {   //相对当前目录的路径，/workflow/request/xxxxx
					realPath = rootPath + "/workflow/request/" + imgPath;
				}
				
				copyFile.setFiletype(0);  //image文件
				paths = copyFile.copyFile(realPath);
			}
			
			String absolutePath = paths[0]; //绝对路径
			String relativePath = paths[1];    //相对html目录的路径
			imageList.add(absolutePath);
			if(!"".equals(relativePath)){
				img.attr("src", relativePath);
			}
		}
		/************html处理完毕********/
		String htmlPath = tempPath + requestid + ".html";  //html文件的路径
		//指定输出流的编码
		File hfile = new File(htmlPath);
		File parent = hfile.getParentFile();
		if(!parent.exists()){
			parent.mkdirs();
		}
		BufferedWriter bw = null;
		String html = root.outerHtml();
		try{
			bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(hfile),"utf-8"));
			bw.write(html);
		}catch (IOException e) {
			writeLog("生成解析后的html文件出错:" + e.getMessage());
		}finally{
			if(bw != null){
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return  tempPath;
	}

	public void setRequestid(String requestid) {
		this.requestid = requestid;
	}

	public String getRequestid() {
		return requestid;
	}
}
