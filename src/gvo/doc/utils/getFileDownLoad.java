package gvo.doc.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import DBstep.iMsgServer2000;
import weaver.alioss.AliOSSObjectManager;
import weaver.conn.RecordSet;
import weaver.file.AESCoder;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.HrmUserVarify;
import weaver.hrm.User;
import weaver.hrm.resource.ResourceComInfo;

public class getFileDownLoad extends HttpServlet{
	private boolean isCountDownloads = false;
    private String agent = "";
    private  List fileNameEcoding ;

	 public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {    
		 String type = Util.null2String(req.getParameter("type"));
	    	//GvoServiceFile gsf = new GvoServiceFile();
	        int nolog = Util.getIntValue(Util.null2String(req.getParameter("nolog")),0);
	        if(!"netimag".equals(type)){
	        String clientEcoding = "GBK";
	        try
	        {
	            String acceptlanguage = req.getHeader("Accept-Language");
	            if(!"".equals(acceptlanguage))
	                acceptlanguage = acceptlanguage.toLowerCase();
	            if(acceptlanguage.indexOf("zh-tw")>-1||acceptlanguage.indexOf("zh-hk")>-1)
	            {
	                clientEcoding = "BIG5";
	            }
	            else
	            {
	                clientEcoding = "GBK";
	            }
	        }
	        catch(Exception e)
	        {
	            
	        }
	        
	        agent = req.getHeader("user-agent");
	        String frompdfview = Util.null2String(req.getParameter("frompdfview"));
	        String downloadBatch = Util.null2String(req.getParameter("downloadBatch"));
	        //只下载附件，而不是下载文档的附件
	        String onlydownloadfj = Util.null2String(req.getParameter("onlydownloadfj"));


	        
	        int fileid = Util.getIntValue(req.getParameter("fileid"), -1);
	        String coworkid = Util.getFileidIn(Util.null2String(req.getParameter("coworkid")));
	        int requestid = Util.getIntValue(req.getParameter("requestid"));
	        String ipstring = req.getRemoteAddr();
	        if(fileid <= 0){//转化为int型，防止SQL注入
	            res.sendRedirect("/notice/noright.jsp");                                
	            return;
			}
			
	        /**流程下载附件，获取最新版本附件下载 start */
	        String fromrequest = req.getParameter("fromrequest");
	        if("1".equals(fromrequest)){
	            RecordSet rs = new RecordSet();
	            rs.executeSql("select doceditionid,id from DocDetail where id=(select max(docid) from DocImageFile where imagefileid=" + fileid + ")");
	            if(rs.next()){
	               int doceditionid = rs.getInt("doceditionid");
	               int docid = rs.getInt("id");
	               
	               if(doceditionid > 0){  //有多版本文档，取最新版本文档
	                   rs.executeSql("select id from DocDetail where doceditionid=" + doceditionid + " order by docedition desc");
	                   if(rs.next()){
	                       docid = rs.getInt("id");
	                   }
	               }
	               
	               //在新版本文档下取最新附件
	               rs.executeSql("select imagefileid from DocImageFile where docid=" + docid + " order by versionId desc");
	               int firstId = 0;
	               int i = 0;
	               while(rs.next()){
	            	   if(i == 0){
	            		   firstId = rs.getInt("imagefileid");
	            	   }else{
	            		   if(fileid == rs.getInt("imagefileid")){
	            			   firstId = fileid;
	            		   }
	            	   }
	                   i++;
	               }
	               fileid = firstId;
	            }
	        }
	        /**流程下载附件，获取最新附件 end */ 
			
	        //String strSql="select docpublishtype from docdetail where id in (select docid from docimagefile where imagefileid="+fileid+") and ishistory <> 1";    
	        RecordSet statement = new RecordSet();   //by ben  开启连接后知道文件下载才关闭，数据库连接时间太长
	        //RecordSet rs =new RecordSet();
	       // User user=null;
	        try {
	            //解决问题：一个附件或图片被一篇内部文档和外部新闻同时引用时，外部新闻可能查看不到附件或图片。 update by fanggsh fot TD5478  begin            
	            //调整为如下：
	            //默认需要用户登录信息,不需要登录信息的情形如下：
	            //1、非登录用户查看外部新闻
	            //boolean needUser= false;  
	           // boolean needUser= true; 
	            int docId=0;
	            String docIdsForOuterNews="";
	            String strSql="select id from DocDetail where exists (select 1 from docimagefile where imagefileid="+fileid+" and docId=DocDetail.id) and ishistory <> 1 and (docPublishType='2' or docPublishType='3')";
	            RecordSet rs =new RecordSet();
	            rs.executeSql(strSql);
	            while(rs.next()){
	                docId=rs.getInt("id");
	                if(docId>0){
	                    docIdsForOuterNews+=","+docId;
	                }
	            }
	            
	            if(!docIdsForOuterNews.equals("")){
	                docIdsForOuterNews=docIdsForOuterNews.substring(1);
	            }
	            
	            if(!docIdsForOuterNews.equals("")){
	                String newsClause="";
	                String sqlDocExist=" select 1 from DocDetail where id in("+docIdsForOuterNews+") "; 
	                String sqlNewsClauseOr="";
	                boolean hasOuterNews=false;
	                
	                rs.executeSql("select newsClause from DocFrontPage where publishType='0'");
	                while(rs.next()){
	                    hasOuterNews=true;
	                    newsClause=Util.null2String(rs.getString("newsClause"));
	                    if (newsClause.equals(""))
	                    {
	                        //newsClause=" 1=1 ";
	                        //needUser=false;
	                        break;
	                    }
	                    if(!newsClause.trim().equals("")){
	                        sqlNewsClauseOr+=" ^_^ ("+newsClause+")";
	                    }
	                }
	               
	                //System.out.print(sqlDocExist);
	              
	            }           


	            //处理外网查看默认图片
	               

	            

	            //解决问题：一个附件或图片被一篇内部文档和外部新闻同时引用时，外部新闻可能查看不到附件或图片。 update by fanggsh fot TD5478  end          
	            //statement.close();
	            String download = Util.null2String(req.getParameter("download"));
	            String contenttype = "";
	            String filename = "";
	            String filerealpath = "";
	            String iszip = "";
	            String isencrypt = "";
				String gvo_encrypt = "";
	            String isaesencrypt="";
	            String aescode = "";
	            String tokenKey="";
	            String storageStatus = "";
	            String comefrom="";

	            if ("1".equals(req.getParameter("countdownloads"))) {
	                isCountDownloads = true;
	            }
	            int byteread;
	            byte data[] = new byte[1024];
	           

	            
	            //String sql = "select imagefilename,filerealpath,iszip,isencrypt,imagefiletype , imagefile from ImageFile where imagefileid = " + fileid;
	            String sql = "select t1.imagefilename,t1.filerealpath,t1.iszip,t1.isencrypt,t1.imagefiletype , t1.imagefileid, t1.imagefile,t1.isaesencrypt,t1.aescode,t2.imagefilename as realname,t1.gvo_encrypt,t1.TokenKey,t1.StorageStatus,t1.comefrom from ImageFile t1 left join DocImageFile t2 on t1.imagefileid = t2.imagefileid where t1.imagefileid = "+fileid;
	            boolean isoracle = (statement.getDBType()).equals("oracle");
	            
	            String extName = "";
	            
	            statement.execute(sql);
	            //statement.executeQuery();
	            if (statement.next()) {
	                filename = Util.null2String(statement.getString("realname"));
	                if(filename.equals("")){
	                    filename = Util.null2String(statement.getString("imagefilename"));
	                }
	                if(filename.toLowerCase().endsWith(".pdf")){
	                    int decryptPdfImageFileId=0;
	                    rs.executeSql("select decryptPdfImageFileId from workflow_texttopdf where pdfImageFileId="+fileid);
	                    if(rs.next()){
	                        decryptPdfImageFileId=Util.getIntValue(rs.getString("decryptPdfImageFileId"),-1);
	                    }
	                    if(decryptPdfImageFileId>0){
	                        sql = "select t1.imagefilename,t1.filerealpath,t1.iszip,t1.isencrypt,t1.imagefiletype , t1.imagefileid, t1.imagefile,t1.isaesencrypt,t1.aescode,t2.imagefilename as realname,t1.TokenKey,t1.StorageStatus,t1.comefrom from ImageFile t1 left join DocImageFile t2 on t1.imagefileid = t2.imagefileid where t1.imagefileid = "+decryptPdfImageFileId;
	                        statement.execute(sql);
	                        if(!statement.next()){
	                            return ;
	                        }
	                    }
	                }
	                filerealpath = Util.null2String(statement.getString("filerealpath"));
	                iszip = Util.null2String(statement.getString("iszip"));
					gvo_encrypt = Util.null2String(statement.getString("gvo_encrypt"));
	                isencrypt = Util.null2String(statement.getString("isencrypt"));
	                isaesencrypt = Util.null2o(statement.getString("isaesencrypt"));
	                aescode = Util.null2String(statement.getString("aescode"));
	                tokenKey = Util.null2String(statement.getString("TokenKey"));
	                storageStatus = Util.null2String(statement.getString("StorageStatus"));
	                comefrom = Util.null2String(statement.getString("comefrom"));
	                                
	                if(filename.indexOf(".") > -1){
	                    int bx = filename.lastIndexOf(".");
	                    if(bx>=0){
	                        extName = filename.substring(bx+1, filename.length());                      
	                    }
	                }               

	                boolean isInline=false;
	                String cacheContorl="";
	                boolean isEnableForDsp=false;
	                if(!tokenKey.equals("")&&storageStatus.equals("1")&&AliOSSObjectManager.isEnableForDsp(req)){
	                    isEnableForDsp=true;
	                }
	                boolean isPic=false;
	                String lowerfilename = filename!=null ? filename.toLowerCase() : "";
	                boolean ishtmlfile = false;
					if(lowerfilename.endsWith(".html")||lowerfilename.endsWith(".htm")){
						RecordSet rs_tmp = new RecordSet();
						rs_tmp.executeQuery("select 1 from DocPreviewHtml where htmlfileid = ?",fileid);
						if(rs_tmp.next()){
							ishtmlfile = true;
						}
					}
					if (download.equals("")&&((!lowerfilename.endsWith(".ppt"))&&(!lowerfilename.endsWith(".pptx"))&&!lowerfilename.endsWith(".xps") && !lowerfilename.endsWith(".js"))||ishtmlfile){
	                    if(filename.toLowerCase().endsWith(".doc")) contenttype = "application/msword";
	                    else if(filename.toLowerCase().endsWith(".xls")) contenttype = "application/vnd.ms-excel";
	                    else if(filename.toLowerCase().endsWith(".gif")) {
	                        contenttype = "image/gif";  
	                        res.addHeader("Cache-Control", "private, max-age=8640000"); 
	                        isPic=true;
	                    }else if(filename.toLowerCase().endsWith(".png")) {
	                        contenttype = "image/png";
	                        res.addHeader("Cache-Control", "private, max-age=8640000"); 
	                        isPic=true;
	                    }else if(filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".jpeg")) {
	                        contenttype = "image/jpg";
	                        res.addHeader("Cache-Control", "private, max-age=8640000"); 
	                        isPic=true;
	                    }else if(filename.toLowerCase().endsWith(".bmp")) {
	                        contenttype = "image/bmp";
	                        res.addHeader("Cache-Control", "private, max-age=8640000"); 
	                        isPic=true;
	                    }
	                    else if(filename.toLowerCase().endsWith(".txt")) contenttype = "text/plain";
	                    else if(filename.toLowerCase().endsWith(".pdf")) contenttype = "application/pdf";
	                    else if(filename.toLowerCase().endsWith(".html")||filename.toLowerCase().endsWith(".htm")) contenttype = "text/html";               
	                    else {                  
	                        contenttype = statement.getString("imagefiletype");
	                    }
	                    try {
	                    	 if((agent.contains("Firefox")||agent.contains(" Chrome")||agent.contains("Safari") )&& !agent.contains("Edge")){
	                    		res.setHeader("content-disposition","inline; filename=\"" +  new String(filename.replaceAll("<", "").replaceAll(">", "").replaceAll("&lt;", "").replaceAll("&gt;", "").getBytes("UTF-8"),"ISO-8859-1"));
	                        }else{
	                            res.setHeader("content-disposition", "inline; filename=\"" + URLEncoder.encode(filename.replaceAll("<", "").replaceAll(">", "").replaceAll("&lt;", "").replaceAll("&gt;", ""),"UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "(").replaceAll("%29", ")")+"\"");
	                        }
	                    } catch (Exception ecode) {
	                    }
	                    isInline=true;
	                }else {
	                    contenttype = "application/octet-stream";
	                    try {
	                        //System.out.println(new String(new String(filename.getBytes(clientEcoding), "ISO8859_1").getBytes("ISO8859_1"),"utf-8"));
	                        //System.out.println(new String(filename.getBytes(clientEcoding)));
	                        if((agent.contains("Firefox")||agent.contains(" Chrome")||agent.contains("Safari") )&& !agent.contains("Edge")){
	                        	res.setHeader("content-disposition", "attachment; filename=\"" +  new String(filename.replaceAll("<", "").replaceAll(">", "").replaceAll("&lt;", "").replaceAll("&gt;", "").getBytes("UTF-8"),"ISO-8859-1"));
	                        }else{
	                            res.setHeader("content-disposition", "attachment; filename=\"" + 
	                                    URLEncoder.encode(filename.replaceAll("<", "").replaceAll(">", "").replaceAll("&lt;", "").replaceAll("&gt;", ""),"UTF-8").replaceAll("\\+", "%20").replaceAll("%28", "(").replaceAll("%29", ")")+"\"");
	                        }
	                    } catch (Exception ecode) {
	                    }
	                }
	                String iscompress = Util.null2String(req.getParameter("iscompress"));
	                String targetFilePath="";
	                if(isEnableForDsp){
	                    
	                    boolean  isAliOSSToServer=AliOSSObjectManager.isAliOSSToServer(comefrom);
	                    if(isAliOSSToServer||isPic || !frompdfview.isEmpty()){
	                        InputStream imagefile = null;
	                        ServletOutputStream out = null;
	                        try {
	                            imagefile=weaver.alioss.AliOSSObjectUtil.downloadFile(tokenKey);
	                            if("1".equals(iscompress)){
	                            	ImageCompressUtil imageCompressUtil=new ImageCompressUtil();
	        	                   	targetFilePath = imageCompressUtil.getTargetFilePath();
	        	                   	imagefile=imageCompressUtil.imageCompress(imagefile,targetFilePath);
	                            }
	                            out = res.getOutputStream();
	                            res.setContentType(contenttype);
	                                        
	                            while ((byteread = imagefile.read(data)) != -1) {
	                                out.write(data, 0, byteread);                   
	                                out.flush();
	                            }
	                        }
	                        catch(Exception e) {
	                            //do nothing
	                        }
	                        finally {
	                            if(imagefile!=null) imagefile.close();
	                            if(out!=null) out.flush();
	                            if(out!=null) out.close();
	                            if("1".equals(iscompress)&& StringUtils.isNotEmpty(targetFilePath)){
	                            	File targetfile = new File(targetFilePath);
	                                if(targetfile.exists()){
	                                	targetfile.delete();   
	                                }
	                            }
	                        }                           
	                        
	                        try{
	                            if(needUser&&nolog==0) {
	                                //记录下载日志 begin
	                                HttpSession session = req.getSession(false);
	                                if (session != null) {
	                                    user = (User) session.getAttribute("weaver_user@bean");
	                                    if (user != null) {
	                                        //董平修改　文档下载日志只记录了内部员工的名字，如果是客户门户来下载，则没有记录　for TD:1644
	                                        String userType = user.getLogintype();
	                                        if ("1".equals(userType)) {   //如果是内部用户　名称就是　lastName 外部则入在　firstName里面
	                                            downloadLog(user.getUID(), user.getLastname(), fileid, filename,ipstring);
	                                        } else {
	                                            downloadLog(user.getUID(), user.getFirstname(), fileid, filename,ipstring);
	                                        }
	    
	                                    }
	                                }
	                                //记录下载日志 end
	                            }
	    
	                            countDownloads(""+fileid);                              
	                        }catch(Exception ex){
	                            
	                        }                           
	                        return ;
	                    }else{
	                        boolean  isSafari=AliOSSObjectManager.isSafari(req);
	                        
	                        String urlString=weaver.alioss.AliOSSObjectUtil.generatePresignedUrl(tokenKey,filename,contenttype,isInline,cacheContorl,isSafari);
	                        if(urlString!=null){
	                            try{
	                                if(needUser&&nolog==0) {
	                                    //记录下载日志 begin
	                                    HttpSession session = req.getSession(false);
	                                    if (session != null) {
	                                        user = (User) session.getAttribute("weaver_user@bean");
	                                        if (user != null) {
	                                            //董平修改　文档下载日志只记录了内部员工的名字，如果是客户门户来下载，则没有记录　for TD:1644
	                                            String userType = user.getLogintype();
	                                            if ("1".equals(userType)) {   //如果是内部用户　名称就是　lastName 外部则入在　firstName里面
	                                                downloadLog(user.getUID(), user.getLastname(), fileid, filename,ipstring);
	                                            } else {
	                                                downloadLog(user.getUID(), user.getFirstname(), fileid, filename,ipstring);
	                                            }
	        
	                                        }
	                                    }
	                                    //记录下载日志 end
	                                }
	        
	                                countDownloads(""+fileid);                              
	                            }catch(Exception ex){
	                                
	                            }
	                            urlString=urlString+"&fileid="+fileid;
	                            res.sendRedirect(urlString); 
	                            return;                             
	                        }                           
	                    }
	                }
	                
	                InputStream imagefile = null;               
	                ZipInputStream zin = null;
	                /*if (filerealpath.equals("")) {         // 旧的文件放在数据库中的方式
	                    if (isoracle)
	                        imagefile = new BufferedInputStream(statement.getBlobBinary("imagefile"));
	                    else
	                        imagefile = new BufferedInputStream(statement.getBinaryStream("imagefile"));
	                } else*/       //目前已经不可能将文件存放在数据库中了
	                
	                    File thefile = new File(filerealpath);
	                    if (iszip.equals("1")) {
	                        zin = new ZipInputStream(new FileInputStream(thefile));
	                        if (zin.getNextEntry() != null) imagefile = new BufferedInputStream(zin);
	                    } else{
	                        imagefile = new BufferedInputStream(new FileInputStream(thefile));
	                    }
						if("1".equals(download)&&"1".equals(gvo_encrypt)){
						StringBuffer log_buff = new StringBuffer();
						log_buff.append("参数[ {imageid:");log_buff.append(fileid);
						log_buff.append(",filename:");log_buff.append(filename);
						log_buff.append(",filerealpath:");log_buff.append(filerealpath);
						log_buff.append("}]");
						// 加密
						// boolean isencfile = true;  //false 解密 true 加密
						imagefile = gsf.getGvoInputStream(log_buff.toString(),imagefile,true);
					}
	                    if(download.equals("1") && (isOfficeToDocument(extName))&&isMsgObjToDocument()) {
	                        //正文的处理
	                        ByteArrayOutputStream bout = null;
	                        try {
	                            bout = new ByteArrayOutputStream() ;
	                            while((byteread = imagefile.read(data)) != -1) {
	                                bout.write(data, 0, byteread) ;
	                                bout.flush() ;
	                            }
	                            byte[] fileBody = bout.toByteArray();
	                            iMsgServer2000 MsgObj = new DBstep.iMsgServer2000();
	                            MsgObj.MsgFileBody(fileBody);           //将文件信息打包
	                            fileBody = MsgObj.ToDocument(MsgObj.MsgFileBody());    //通过iMsgServer200 将pgf文件流转化为普通Office文件流
	                            imagefile = new ByteArrayInputStream(fileBody);
	                            bout.close();
	                        }
	                        catch(Exception e) {
	                            if(bout!=null) bout.close();
	                        }
	                    }           
	                


	                ServletOutputStream out = null;
	                try {
	                    out = res.getOutputStream();
	                    res.setContentType(contenttype);
	    
	                    if(isaesencrypt.equals("1")){
	                        imagefile = AESCoder.decrypt(imagefile, aescode); 
	                    }
	                    if("1".equals(iscompress)){
		                   	ImageCompressUtil imageCompressUtil=new ImageCompressUtil();
		                   	targetFilePath = imageCompressUtil.getTargetFilePath();
		                   	imagefile=imageCompressUtil.imageCompress(imagefile,targetFilePath);
		                }
	                    while ((byteread = imagefile.read(data)) != -1) {
	                        out.write(data, 0, byteread);                   
	                        out.flush();
	                    }
	                }
	                catch(Exception e) {
	                    //do nothing
	                }
	                finally {
	                    if(imagefile!=null) imagefile.close();
	                    if(zin!=null) zin.close();
	                    if(out!=null) out.flush();
	                    if(out!=null) out.close();
	                    if("1".equals(iscompress)&& StringUtils.isNotEmpty(targetFilePath)){
	                    	File targetfile = new File(targetFilePath);
	                        if(targetfile.exists()){
	                        	targetfile.delete();   
	                        }
	                    }
	                    
	                }
	                
	                if(needUser&&nolog==0) {
	                    //记录下载日志 begin
	                    HttpSession session = req.getSession(false);
	                    if (session != null) {
	                        user = (User) session.getAttribute("weaver_user@bean");
	                        if (user != null) {
	                            //董平修改　文档下载日志只记录了内部员工的名字，如果是客户门户来下载，则没有记录　for TD:1644
	                            String userType = user.getLogintype();
	                            if ("1".equals(userType)) {   //如果是内部用户　名称就是　lastName 外部则入在　firstName里面
	                                downloadLog(user.getUID(), user.getLastname(), fileid, filename,ipstring);
	                            } else {
	                                downloadLog(user.getUID(), user.getFirstname(), fileid, filename,ipstring);
	                            }

	                        }
	                    }
	                    //记录下载日志 end
	                }


	                countDownloads(""+fileid);
	            }
	        } catch (Exception e) {
	            BaseBean basebean = new BaseBean();
	            basebean.writeLog(e);
	        } //错误处理
	        
		 
	 }
	 
}
