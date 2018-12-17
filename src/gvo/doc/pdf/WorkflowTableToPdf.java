package gvo.doc.pdf;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.UUID;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.io.FileUtils;

import sun.util.logging.resources.logging;
import weaver.conn.RecordSet;
import weaver.conn.RecordSetTrans;
import weaver.file.AESCoder;
import weaver.file.FileUpload;
import weaver.general.BaseBean;
import weaver.general.FWHttpConnectionManager;
import weaver.general.GCONST;
import weaver.general.Util;
import weaver.hrm.User;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
import weaver.system.SystemComInfo;
import weaver.workflow.msg.PoppupRemindInfoUtil;
import weaver.workflow.request.RequestManager;

public class WorkflowTableToPdf extends BaseBean implements Action {
    private RequestManager requestManager;
    private String isaesencrypt="";
	private String aescode="";
    @Override
    public String execute(RequestInfo requestInfo) {
    	RecordSet rs = new RecordSet();
        //获得请求ID
        String Requestid = requestInfo.getRequestid();
        //获得流程ID
        String workflowid = requestInfo.getWorkflowid();
        this.requestManager = requestInfo.getRequestManager();
        this.requestManager = getRequestManager(this.requestManager,Requestid);
        String exepathString = "/usr/wkhtmltopdf/bin/wkhtmltopdf";
        //String exepathString = getPropValue("TD306624", "exepath");
        //String tablenameString = getPropValue("TD306624", workflowid+"_maintablename");
        //String tablepathString = getPropValue("TD306624", workflowid+"_maintablepath");
        String requestname = requestManager.getRequestname();
       // String usrid = Util.null2String(requestInfo.getLastoperator());
        String usrid = "1";
        String descri = Util.null2String(requestInfo.getDescription());
        String [] url = getUrl(Requestid, usrid);
        String loadPathString = "";
        if(url!=null && url.length==5){//避免url为空时，出现异常
			boolean hasNull = false;//检查5个值是不是有空，如果有，就不导为文档了
			for(int cx=0; cx<url.length; cx++){
				if(url[cx]==null || "".equals(url[cx])){
					hasNull = true;
				}
			}
			if(hasNull == false){
				loadPathString = getWorkflowHtml(url, Requestid, requestname, workflowid, usrid);
			}
		}
        String sql = "";
		String oaaddress = "";
		String pdfUrl = "";
		sql = "select oaaddress from uf_doc_oaadress where rownum=1";
		rs.execute(sql);
		if(rs.next()) {
			oaaddress = Util.null2String(rs.getString("oaaddress"));
		}
        String rootPath = GCONST.getRootPath();  //根目录
        //定义生成的PDF文件保存的目录
        String infopathString = "TableToPdf"+"/"+Requestid+"/";
        String savePathString = rootPath+infopathString;
        File makeFile = new File(savePathString);
		if(!makeFile.exists()){
			makeFile.mkdirs();
		}
        Html2PdfUtil hpf = new Html2PdfUtil();
        BaseBean log = new BaseBean();
        log.writeLog("html 11:"+oaaddress+"/TableToPdf/"+Requestid+"/"+Requestid+".html");
        log.writeLog("pdf 11:"+savePathString+Requestid+".pdf");
        String [] args ={exepathString,oaaddress+"/TableToPdf/"+Requestid+"/"+Requestid+".html",savePathString+Requestid+".pdf"};
        Boolean signBoolean= hpf.htmltopdf(args);
        File tempFile = new File(loadPathString);
        try{//删除之前生成的html文件
			if(tempFile.exists() && tempFile.isDirectory()){
				FileUtils.deleteDirectory(tempFile); 
			}
		}catch(IOException e){
			writeLog("删除临时目录出错：" + e);
		}
        if(!signBoolean){//如果生成失败，返回提示信息
        	requestInfo.getRequestManager().setMessageid("11111");
            requestInfo.getRequestManager().setMessagecontent("表单生成PDF文件失败!");
            return Action.FAILURE_AND_CONTINUE;
        }else{//如果生成成功，记录生活才能的文件地址，回填到表单
        	//rs.executeUpdate(" update "+tablenameString+" set "+tablepathString+" = '"+infopathString+Requestid+".pdf"+"' where  requestid = "+Requestid);
        }
        return Action.SUCCESS;
    }
    public String[] getUrl(String requestid, String userid) {
		String sql = "";
		String loginid = "";
		String password = "";
		int accounttype = 0;  //账号类型,0:主账号，1：次账号
		int belongto = 0;
		String para = "";
		String oaaddress = "";
		String isadaccount = "";
		String params[] = new String[5];
		
		RecordSet rs = new RecordSet();
		sql = "select * from SystemSet";
		rs.executeSql(sql);
		rs.next();
		oaaddress = Util.null2String(rs.getString("oaaddress"));
		if (oaaddress.equals("")) {
			return params;
		}

		sql = "select * from hrmresource where id = " + userid;
		rs.executeSql(sql);
		if (rs.next()) {
			loginid = rs.getString("loginid");
			password = rs.getString("password");
			isadaccount = rs.getString("isadaccount");
			accounttype = Util.getIntValue(Util.null2String(rs.getString("accounttype")),0);
			belongto = Util.getIntValue(Util.null2String(rs.getString("belongto")));
		}

		sql = "select * from HrmResourceManager where id = " + userid;
		rs.executeSql(sql);
		if (rs.next()) {
			loginid = rs.getString("loginid");
			password = rs.getString("password");
		}
		String f_weaver_belongto_userid = "";
		String f_weaver_belongto_usertype = "0";
		if(accounttype == 1 && belongto > 0){   //次账号，查询主账号，用主账号登录
			f_weaver_belongto_userid = userid;
			sql = "select * from hrmresource where id = " + belongto;
			rs.executeSql(sql);
			if(rs.next()){
				loginid = rs.getString("loginid");
				password = rs.getString("password");
				isadaccount = rs.getString("isadaccount");
			}
		}
		if ((!loginid.equals("") && !password.equals("")) || ((!(loginid.equals(""))) && (isadaccount.equals("1")))) {
			para = "/workflow/request/ViewRequest.jsp?requestid=" + requestid
					+ "&para2=" + loginid + "&para3=" + password;
		} else {
			return params;
		}

		


		params[0] = oaaddress+"/login/VerifyRtxLogin.jsp";
		params[1] = "workflowtodoc";
		String pageurl = "/workflow/request/ViewRequest.jsp?isworkflowhtmldoc=1&requestid=" + requestid;
		if(accounttype > 0 && belongto > 0){
			pageurl += "&f_weaver_belongto_userid=" + f_weaver_belongto_userid + "&f_weaver_belongto_usertype=" + f_weaver_belongto_usertype;
		}
		params[2] = pageurl;
		/*try {
			params[3] = new String(loginid.getBytes(), "8859_1");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		params[3] = PoppupRemindInfoUtil.encrypt(loginid);//解决中文账号问题
		params[4] = password;

		
		return params;
	}
    public String  getWorkflowHtml(String url[], String requestid,
			String requestname, String workflowid, String userid) {
    	String htmlPath = "";
		HttpClient client = FWHttpConnectionManager.getHttpClient();

		PostMethod method = new PostMethod(url[0]);//oa地址
		method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
				new DefaultHttpMethodRetryHandler(3, false));		
		try {
			NameValuePair[] params = {
					new NameValuePair("urlfrom", url[1]),//urlfrom
					new NameValuePair("para1",url[2]),//requesturl
					new NameValuePair("para2",url[3] ),//loginid
					new NameValuePair("para3",url[4])};//url4密码
			method.setRequestBody(params);		
			int statusCode = client.executeMethod(method);
			//String temppath = getFileSavePath();
			String temppath = GCONST.getRootPath()+"TableToPdf/"+requestid+"/";
			String filename = requestid + "";
			String htmlname = temppath + filename + ".html";
			File _temppath = new File(temppath);
			if(!_temppath.exists()){
				_temppath.mkdirs();
			}

			if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY
					|| statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
				Header locationHeader = method.getResponseHeader("location");
				if (locationHeader != null) {
					String tempurl = locationHeader.getValue();
					tempurl = getFinallyUrl(client, tempurl);
					tempurl = tempurl.replaceFirst(".jsp", "Iframe.jsp");
					GetMethod g = new GetMethod(tempurl);
					client.executeMethod(g);
					
					OutputStream os=new FileOutputStream(htmlname);
					SystemComInfo syscominfo=new SystemComInfo() ;
					this.isaesencrypt = syscominfo.getIsaesencrypt();
					this.aescode = Util.getRandomString(13);
					if("1".equals(this.isaesencrypt)){
						try{
							os=AESCoder.encrypt(os, this.aescode);
						} catch(Exception e){
							
						}
					}
					OutputStreamWriter output = new OutputStreamWriter(os, "UTF-8");
					BufferedWriter bw = new BufferedWriter(output);
					
					BufferedReader in = new BufferedReader(new InputStreamReader(g.getResponseBodyAsStream(), "UTF-8"));
					StringBuffer sb=new StringBuffer();
					String line = in.readLine();		
					while (line != null) {
						line = line.trim();
						
						if(line.indexOf("</a>")>=0&&line.indexOf("openSignPrint()")>=0&&line.indexOf("onclick")>=0){
							
						//去掉转发按钮
						}else if(line.indexOf("<img")>=0&&line.indexOf("class=\"transto\"")>=0&&line.indexOf("onclick")>=0&&line.indexOf("transtoClick(this)")>=0){
						    
						}else if(line.indexOf("var")>=0&&line.indexOf("bar")>=0&&line.indexOf("eval")>=0&&line.indexOf("handler")>=0&&line.indexOf("text")>=0){
							sb.append("var bar=eval(\"[]\");\n");
						}else{ 
							sb.append(line + "\n");
						}							
						line = in.readLine();
					}
					//去掉ext的button,<script type="text/javascript" src="/js/wf_wev8.js">var bar=eval("[]");</script>
					sb.append("<script type=\"text/javascript\">\n");
					sb.append("function drm4request2doc(){\n");
					sb.append("\tbar=eval(\"[]\");\n");
					sb.append("\tdocument.getElementById(\"rightMenu\").style.display=\"none\";\n");
					sb.append("}\n");
					sb.append("window.attachEvent(\"onload\", drm4request2doc);\n");
					sb.append("</script>");
					
					String sdata=sb.toString();
					bw.write(sdata,0,sdata.length());
					
					long size = 0l;
					File f = new File(htmlname);
					if (f.exists()) {
						ParseHtml parseHtml = new ParseHtml();
						parseHtml.setIsaesencrypt(isaesencrypt);
						parseHtml.setAescode(aescode);
						parseHtml.setParentPath(temppath);
						parseHtml.setRequestname(requestname);
						parseHtml.setRequestid(requestid);
						htmlPath = parseHtml.parseHtml(f);
					}
//					try{//删除之前生成的html文件
//						if(f.exists() && f.isDirectory()){
//							FileUtils.deleteDirectory(f); 
//						}
//					}catch(IOException e){
//						writeLog("删除临时目录出错：" + e);
//					}
					bw.flush();
					bw.close();
					in.close();
					
					if(g!=null){
						g.releaseConnection();
					}
					if(method!=null){
						method.releaseConnection();
					}
				}
			}			
			
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			method.releaseConnection();
		}
		return htmlPath;
	}
	public String getFinallyUrl(HttpClient client, String url) {
		PostMethod g = new PostMethod(url);
		try {
			client.executeMethod(g);
		} catch (HttpException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return url;
		}
		Header locationHeader = g.getResponseHeader("location");
		if (locationHeader != null) {
			url = locationHeader.getValue();
			url = getFinallyUrl(client, url);
		}
		if (g != null) {
			g.releaseConnection();
		}
		return url;
	}
	public String getFileSavePath() {
		SystemComInfo syscominfo = new SystemComInfo();
		String createdir = FileUpload.getCreateDir(syscominfo.getFilesystem());
		return createdir;
	}
    /**
     * 判断requestmanager是否为空
     * @param rmanager
     */
    public RequestManager getRequestManager(RequestManager rmanager,String requestid){
        
        if(rmanager==null){
            rmanager = new RequestManager();
            RecordSet rs = new RecordSet();
            String sql = "select * from workflow_requestbase where requestid = " + requestid;
            rs.executeSql(sql);
            while(rs.next()){
                int _requestId=rs.getInt("requestid");
                int workflowId=rs.getInt("workflowid");
                int creater=rs.getInt("creater");
                String requestname = rs.getString("requestname");
                String requestlevel = rs.getString("requestlevel");
                String messagetype = rs.getString("messagetype");       
                rmanager.setRequestid(_requestId);
                rmanager.setWorkflowid(workflowId);
                rmanager.setCreater(creater);
                rmanager.setRequestname(requestname);
                rmanager.setRequestlevel(requestlevel);
                rmanager.setMessageType(messagetype);
            }
        }
        
        return rmanager;
    }
    public RequestManager getRequestManager() {
        return requestManager;
    }
    public void setRequestManager(RequestManager requestManager) {
        this.requestManager = requestManager;
    }
}
