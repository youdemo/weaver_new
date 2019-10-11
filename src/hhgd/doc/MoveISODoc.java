package hhgd.doc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

import org.apache.axis.encoding.Base64;

import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

public class MoveISODoc {

	BaseBean log = new BaseBean();
	/**
	 * 根据两套系统的目录编号同步文档
	 * @param isoSecCode
	 * @param oaSecCode
	 * @return
	 */
	public String getDocList(String isoSecCode,String oaSecCode){
		log.writeLog("开始同步文档ISO目录："+isoSecCode+" OA目录："+oaSecCode);
		String iso_id = "";
		String applyid = "";
		String creater="1";
		String docid="";
		String secid=getSecId(oaSecCode);
		String docname = "";
		String ISO_NO = "";
		String iso_ver = "";
		if("".equals(secid)){
			log.writeLog("OA目录编码错误"+oaSecCode);
			return "OA目录编码错误";
		}
		RecordSet rsd = new RecordSet();
		String sql="select distinct a.id,b.applyid,a.docname,b.ISO_NO,b.iso_ver from [10.9.2.139].WEBISO4.dbo.ConvertPDFList a,[10.9.2.139].WEBISO4.dbo.AFU_Form_ISO b where a.id=b.serialid and b.check_ok = '1'and b.newver='1' and b.type_id='"+isoSecCode+"'  and b.drop_flag=0";
		//log.writeLog("查询ISO文档列表sql"+sql);
		rsd.executeSql(sql);
		while(rsd.next()){
			creater="1";
			iso_id = Util.null2String(rsd.getString("id"));
			applyid = Util.null2String(rsd.getString("applyid"));
			docname = Util.null2String(rsd.getString("docname"));
			ISO_NO = Util.null2String(rsd.getString("ISO_NO"));
			iso_ver = Util.null2String(rsd.getString("iso_ver"));
			if(checkExist(ISO_NO)){
				log.writeLog("该文档已同步到OA iso_id"+iso_id);
				continue;
			}
			if(!"".equals(applyid)){
				creater = getPersonId(applyid);
			}
			DocAttachment[] docs;
			try {
				docs = getDocFile(iso_id);
				log.writeLog("docs length"+docs.length);
			} catch (Exception e) {
				log.writeLog("error:"+e.getMessage()+"\n"+e.getLocalizedMessage());
				e.printStackTrace();
				log.writeLog("导出iSO文件失败 iso_id"+iso_id);
				continue;
				
			}
			if(docs !=null &&docs.length>0){
				try {
					log.writeLog("开始创建文件docname"+docname+"iso_id"+iso_id+"creater"+creater+"secid"+secid);
					docid=getDocId(docname,ISO_NO,docs,creater,secid);
					if(!"".equals(docid)){
						updateDocedition(docid,iso_ver);
					}
					log.writeLog("创建OA文件成功 docid"+docid);
				} catch (Exception e) {
					e.printStackTrace();
					log.writeLog("创建OA文件失败 iso_id"+iso_id);
				}
			}
		}
		return "成功";
	}
	/**
	 * 根据目录编号获取目录id
	 * @param code
	 * @return
	 */
	public String getSecId(String code){
		RecordSet rs = new RecordSet();
		String secid="";
		String sql = "select id from DocSecCategory  where coder='"+code+"'";
		rs.executeSql(sql);
		if(rs.next()){
			secid = Util.null2String(rs.getString("id"));
		}
		return secid;
	}
	/**
	 * 根据人员工号获取人员id
	 * @param workcode
	 * @return
	 */
	public String getPersonId(String workcode){
		RecordSet rs = new RecordSet();
		String id="1";
		String sql = "select id from hrmresource where workcode='"+ workcode + "' and status in(0,1,2,3) ";
		rs.executeSql(sql);
		if(rs.next()){
			id = Util.null2String(rs.getString("id"));
		}
		return id;
	}
	/**
	 * 根据iso文档编号获取文档附件
	 * @param iso_id
	 * @return
	 * @throws Exception
	 */
	public DocAttachment[] getDocFile(String iso_id) throws Exception{
		log.writeLog("导出iSO文件 文档编号"+iso_id);
		DocAttachment[] docs=null;
		String OriginalName = "";
		String source = "";
		RecordSet rsd = new RecordSet();
		int count=0;
		String sql="select count(1) as count from [10.9.2.139].WEBISO4.dbo.ConvertPDFList where id='"+iso_id+"' and source is not null";
		rsd.executeSql(sql);
		if(rsd.next()){
			count = rsd.getInt("count");
		}
		if(count>0){
			 docs= new DocAttachment[count];
			 sql="select OriginalName,source from [10.9.2.139].WEBISO4.dbo.ConvertPDFList where id='"+iso_id+"' and source is not null";
			 //log.writeLog("导出iSO文件 sql"+sql);
			 rsd.executeSql(sql);
			 int num=0;
			 while(rsd.next()){
				 OriginalName = Util.null2String(rsd.getString("OriginalName"));
				 source = Util.null2String(rsd.getString("source"));
				 log.writeLog("文件 路径OriginalName"+OriginalName+" source"+source);
				 if(!"".equals(source)&&!"".equals(OriginalName)){
					 int length=13;
					 length=source.indexOf("\\Object");
					 source = "\\\\10.9.2.57"+source.substring(length,source.length());
					 log.writeLog("source"+source);
					 DocAttachment doca = getDocAttachment(source,OriginalName);
					 docs[num]=doca;
					 num++;
				 }
			 }
		}else{
			log.writeLog("不存在导出iSO文件 文档编号"+iso_id);
		}
		
		
		
		return docs;
	}
	/**
	 * 根据文件路径获取附件流存入文档附件对象
	 * @param fileUrl
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public DocAttachment getDocAttachment(String fileUrl,String fileName) throws Exception{
		FileInputStream fi = new FileInputStream(new File(fileUrl));
		ByteArrayOutputStream baos = new ByteArrayOutputStream(); 
		byte[] buffer = new byte[1024]; 
		int count = 0; 
		while((count = fi.read(buffer)) >= 0){ 
		     baos.write(buffer, 0, count); 
	    } 
		DocAttachment doca = new DocAttachment();
		doca.setFilename(fileName);
		String encode=Base64.encode(baos.toByteArray());
		log.writeLog("encode:"+encode.length());
		doca.setFilecontent(encode);
		return doca;
	}
	/**
	 * 根据文档编号 判断文档是否导入
	 * @param doccode
	 * @return
	 */
	public boolean checkExist(String doccode){
		RecordSet rs = new RecordSet();
		int count =0;
		String sql="select count(1) as count from DocDetail where doccode='"+doccode+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		if(count>0){
			return true;
		}
		return false;
	}
	/**
	 * 调用oa系统方法 生成oa文档
	 * @param name 文档名称
	 * @param doccode 编号
	 * @param docs 附件
	 * @param createrid 创建人
	 * @param seccategory 目录
	 * @return 文档id
	 * @throws Exception
	 */
	private String getDocId(String name, String doccode, DocAttachment[] docs,String createrid,String seccategory) throws Exception {
		String docId = "";
		DocInfo di= new DocInfo();
		di.setMaincategory(0);
		di.setSubcategory(0);
		di.setSeccategory(Integer.valueOf(seccategory));	
		di.setDocSubject(name);	
		//di.setDoccontent(arg0);
		di.setAttachments(docs);
		di.setDocCode(doccode);
		String departmentId="-1";
		String sql="select departmentid from hrmresource where id="+createrid;
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		User user = new User();
		if(rs.next()){
			departmentId = Util.null2String(rs.getString("departmentid"));
		}	
		user.setUid(Integer.parseInt(createrid));
		user.setUserDepartment(Integer.parseInt(departmentId));
		user.setLanguage(7);
		user.setLogintype("1");
		user.setLoginip("127.0.0.1");
		DocServiceImpl ds = new DocServiceImpl();
		try {
			docId=String.valueOf(ds.createDocByUser(di, user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return docId;
	}
	/**
	 * 更新文档
	 * @param docid
	 * @param docedition
	 */
	public void updateDocedition(String docid,String docedition){
		RecordSet rs = new RecordSet();
		String sql="update DocDetail set docedition='"+docedition+"' where id="+docid;
		rs.executeSql(sql);
		
	}
}
