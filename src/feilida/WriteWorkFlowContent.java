package feilida;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipInputStream;

//import org.apache.commons.net.ftp.FTPClient;
//import org.apache.commons.net.ftp.FTPReply;
/**
 * ���?�ֶ��е�ͼƬ�ȴ�ŵ�����Ȼ���͸�FTP���������
 * @author jianyong
 *
 */
public class WriteWorkFlowContent implements Action {

	BaseBean log = new BaseBean();
	// private  FTPClient ftp;
	public String execute(RequestInfo info) {

		String requestid = info.getRequestid();
		String workflow_id = info.getWorkflowid();
		RecordSet rs = new RecordSet();
		String tableName = "";
        String zp = "";
        String gh = "";
		String sql = "Select tablename From Workflow_bill Where id=(";
		sql += "Select formid From workflow_base Where id=" + workflow_id + ")";
		rs.executeSql(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
        
		sql = "select zp,gh from " + tableName + " where requestid= " + requestid;
		rs.executeSql(sql);
		if (rs.next()) {
			zp = Util.null2String(rs.getString("zp"));
			gh = Util.null2String(rs.getString("gh"));
		}
		SimpleDateFormat sf= new SimpleDateFormat("yyyy-MM-dd H:m:s");
		String now =sf.format(new Date());
		if(!"".equals(zp) && !"".equals(gh)){
			try {
				writeFile(zp,gh,now);
				
			} catch (Exception e) {
				log.writeLog("������Ƭʧ��"+requestid);
				e.printStackTrace();
			}
		}


		

		
		return SUCCESS;
	}



	
    /**
     * ����ͼƬ�ļ�
     * @param destFileName
     * @return
     * @throws IOException
     */
	public boolean createFile(String destFileName) throws IOException {
		File file = new File(destFileName);
		if (file.exists()) {
			file.delete();
		}
		if (destFileName.endsWith(File.separator)) {
			return false;
		}
		// �ж�Ŀ���ļ����ڵ�Ŀ¼�Ƿ����
		if (!file.getParentFile().exists()) {
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}
		// ����Ŀ���ļ�
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
    /**
     * ��ȡ���̱?�е�ͼƬ����������ͼƬ
     * @param docId
     * @param gh
     * @param sysdate
     * @throws Exception
     */
	private void writeFile(String docId,String gh, String sysdate) throws Exception {
		RecordSet rs = new RecordSet();
		String filerealpath = "";
		String iszip = "";
		int num=0;
		String name = "";
		String fileName="";
		String docName="/weaver/ecology/feilida/zp/";
		String sql = " select b.filerealpath,b.iszip,b.imagefilename,b.imagefileid from  "
				+ " imagefile b  where b.imagefileid in(select max(imagefileid) " 
				+ "from docimagefile where docid in("+docId+") group by docid)";
		rs.executeSql(sql);
		while (rs.next()) {
			
			filerealpath = Util.null2String(rs.getString("filerealpath"));
			iszip = Util.null2String(rs.getString("iszip"));
			name = Util.null2String(rs.getString("imagefilename"));	
			if(num>0){
				name =gh+"_"+num+name.substring( name.lastIndexOf("."),name.length());
			}else{
				name = gh+name.substring( name.lastIndexOf("."),name.length());
			}
			fileName = docName+name;
			File file = new File(fileName);
			if (file.exists()) {
				name =name.substring(0, name.lastIndexOf("."))+"_"+sysdate+name.substring( name.lastIndexOf("."),name.length());
				fileName=docName+name;
			}
			if (filerealpath.length() > 0) {
				InputStream imagefile = getFile(filerealpath, iszip);
				createFile(fileName);
				FileOutputStream fos = new FileOutputStream(fileName);
				int ch = 0;
				try {
					while ((ch = imagefile.read()) != -1) {
						fos.write(ch);
					}
				} catch (IOException e1) {
					e1.printStackTrace();
					log.writeLog("��������imagefile" + e1.getMessage());
				} finally {
					// �ر��������ȣ��ԣ�
					fos.close();
					imagefile.close();
				}
			//	upload(fileName,name);		
			}
			num++;
		}
			

	}
    /**
     * ��ȡ�?��Ƭ��
     * @param filerealpath
     * @param iszip
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
	/**
	 * ��������FTP
	 * @param path
	 * @param addr
	 * @param port
	 * @param username
	 * @param password
	 * @return
	 * @throws Exception
	 */
//	private  boolean connect(String path,String addr,int port,String username,String password) throws Exception {      
//        boolean result = false;      
//        ftp = new FTPClient();      
//        int reply;      
//        ftp.connect(addr, port);    
//        ftp.login(username,password);      
//        ftp.setFileType(FTPClient.BINARY_FILE_TYPE);      
//        reply = ftp.getReplyCode();   
//        log.writeLog("��ʼ����reply"+reply);
//        if (!FTPReply.isPositiveCompletion(reply)) {       
//            ftp.disconnect();      
//            return result;      
//        }      
//        ftp.changeWorkingDirectory(path);      
//        result = true;      
//        return result;      
//    } 
	/**
	 * �ϴ���Ƭ��FTP
	 * @param path
	 * @param name
	 * @throws Exception
	 */
//	public void upload(String path,String name) throws Exception{
//		 
//		 boolean flag=connect("ftp://172.20.70.200/Ready/","172.20.70.200",21,"ecctest","ecctest");		 
//		 if(!flag){
//			 log.writeLog("��������ʧ��:path:"+path+" name:"+name);
//			 return;
//		 }
//		 File file = new File(path);       
//         FileInputStream input = new FileInputStream(file);  
//         name="/Ready/"+name;
//         ftp.enterLocalPassiveMode();
//         ftp.storeFile(new String(name.getBytes("UTF-8"),"iso-8859-1"), input);         
//         input.close(); 
//         if (ftp.isConnected()) {     	 
//        	   ftp.disconnect();       	  
//         }	  
//
//	 }
}
