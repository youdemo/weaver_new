package wldq.contract.template;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.encoding.Base64;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

import sun.misc.BASE64Decoder; 
import weaver.conn.RecordSet;
import weaver.docs.webservices.DocAttachment;
import weaver.docs.webservices.DocInfo;
import weaver.docs.webservices.DocServiceImpl;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.hrm.User;

/**
 * @author tangjianyong
 * @version v1.0
 * @Description 6.3.3.3 管理单次合同签订（李梦云）
 * @DateTime 2018-11-12
 */

public class CreateTemplate {
    public String CreateFile(Map<String, String> mapStr, String docCategory,String tablename,String mainid,String requestid) {
      
        String modeDocUrl = "";
        String outFileUrl = "";
        String pdfUrl = "";
        Map<String, String> bookMarkMap = new HashMap<String, String>();
        modeDocUrl = "D:\\Weaver2017_base\\ecology\\wldq\\contract\\template\\合同模板.docx";
        outFileUrl = "D:\\Weaver2017_base\\ecology\\wldq\\contract\\ContractFile\\合同模板-" + requestid + ".docx";
        pdfUrl = "D:\\Weaver2017_base\\ecology\\wldq\\contract\\ContractFile\\合同模板-" + requestid + "-pdf.pdf";
        

        RecordSet rs = new RecordSet();
        writeLog("开始生成文档");
        bookMarkMap.put("htbh", mapStr.get("htbh")); 
        bookMarkMap.put("htmc", mapStr.get("htmc")); 
        bookMarkMap.put("htdx", mapStr.get("htdx")); 
        bookMarkMap.put("htdx2", mapStr.get("htdx")); 
        
        String sqrq = mapStr.get("sqrq"); 
        String year = "";
        String month = "";
        String day = "";
        if (!"".equals(sqrq)&&sqrq.length()>=10) {
        	year = sqrq.substring(0, 4);
        	month = sqrq.substring(5, 7);
            day= sqrq.substring(8, 10);
            bookMarkMap.put("year", year); 
            bookMarkMap.put("month", month); 
            bookMarkMap.put("day", day); 
        }
        bookMarkMap.put("htjk", mapStr.get("htjk")); 
        bookMarkMap.put("htjkdx", new TransMoney().digitUppercase(Util.getDoubleValue(mapStr.get("htjk"), 0.0))); 

       
        writeLog("bookMarkMap:" + bookMarkMap.toString());

        try {
            modifyDocumentAndSave(modeDocUrl, bookMarkMap, outFileUrl,tablename,mainid);
            writeLog("生成合同模板成功:");
        } catch (Exception e) {
            writeLog(e);
            writeLog("生成合同模板失败 :" + e.getLocalizedMessage());
            return "-1";
        }
        writeLog("生123"); 
        writeLog("生124");
        int result = -1;      
        try {
        	writeLog("outFileUrl:"+outFileUrl+" pdfUrl:"+pdfUrl);
            result = office2PDF(outFileUrl, pdfUrl);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            writeLog(e);
            writeLog("转换pdf失败 :" + e.getMessage());
            result = -1;
        }
        if (result != 0) {
            writeLog("转换pdf失败");
            return "-1";
        }
        String filename = "合同"+mapStr.get("htbh") + ".pdf";
        String docid;
        try {
            writeLog("开始创建OA文档  pdfUrl:" + pdfUrl + " filename:" + filename + " application:" + mapStr.get("docCreator"));
            docid = createDoc(pdfUrl, docCategory, filename, mapStr.get("docCreator"));
            writeLog("创建文档结束 docid:" + docid);
        } catch (Exception e) {
            writeLog("生成文档失败 :" + e.getMessage());
            return "-1";
        }
        return docid;
    }

    public  void doDetail2(Element child1,Element child2,String tableName,String mainid){
		RecordSet rs = new RecordSet();
	
		int count=0;
		Element root=(Element)child1.getParentNode();
		Element child = child1;
		String sql="select row_number() over(order by id asc) as num, fktj,fkbl,fkje from "+tableName+"_dt1 where mainid="+mainid+" order by id asc";

		rs.executeSql(sql);
		while(rs.next()){
			if(count==0){
				NodeList wtList=child1.getElementsByTagName("w:t");
				for(int i=1;i<=wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i-1);
					wtNode.setTextContent(Util.null2String(rs.getString(i)));
				}
			}else{
				root.appendChild(child1.cloneNode(true));
				Element clone=(Element)child.getNextSibling();
				NodeList wtList=clone.getElementsByTagName("w:t");
				for(int i=1;i<=wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i-1);
					wtNode.setTextContent(Util.null2String(rs.getString(i)));
				}
				child = clone;
			}
			count++;
		}
		if(count==0){
			NodeList wtList=child1.getElementsByTagName("w:t");
			for(int i=1;i<=wtList.getLength();i++){
				Element wtNode = (Element) wtList.item(i-1);
				if(i==1){
					wtNode.setTextContent(Util.null2String("1"));
				}else{
				wtNode.setTextContent(Util.null2String(""));
				}
			}
		}
		
	}
	public  void doDetail1(Element child1,Element child2,String tableName,String mainid){
		RecordSet rs = new RecordSet();
		int count=0;
		Element root=(Element)child1.getParentNode();
		Element child = child1;
		String sql="select row_number() over(order by id asc) as num, (select cpmc from uf_Productinfor where id=a.q) as q,w,y,d,i from "+tableName+"_dt2 a where mainid="+mainid;
		rs.executeSql(sql);
		while(rs.next()){
			if(count==0){
				NodeList wtList=child1.getElementsByTagName("w:t");
				for(int i=1;i<=wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i-1);
					wtNode.setTextContent(Util.null2String(rs.getString(i)));
				}
			}else{
				root.appendChild(child1.cloneNode(true));
				Element clone=(Element)child.getNextSibling();
				NodeList wtList=clone.getElementsByTagName("w:t");
				for(int i=1;i<=wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i-1);
					wtNode.setTextContent(Util.null2String(rs.getString(i)));
				}
				child = clone;
			}
			count++;
		}
		if(count==0){
			NodeList wtList=child1.getElementsByTagName("w:t");
			for(int i=1;i<=wtList.getLength();i++){
				Element wtNode = (Element) wtList.item(i-1);
				if(i==1){
					wtNode.setTextContent(Util.null2String("1"));
				}else{
				wtNode.setTextContent(Util.null2String(""));
				}
			}
		}
		
	}
    /**
     * 生成doc文档
     *
     * @param FileUrl 模板路径
     * @param bookMarkMap 书签内容map集合
     * @param outFileUrl 输出路径
     * @throws Exception 创建失败
     */
    public void modifyDocumentAndSave(String FileUrl, Map<String, String> bookMarkMap, String outFileUrl,String tablename,String mainid) throws Exception {
        writeLog("modifyDocumentAndSave### start");
        // 使用java.util打开文件
        File file = new File(FileUrl);
        boolean exist = file.exists();
        boolean read = file.canRead(); 
        boolean write = file.canWrite();
        //writeLog("exist:"+exist);
        //D:\\test\\2017年AFH订制品合同模板.docx 
        ZipFile docxFile = new ZipFile(file);
        // 返回ZipEntry应用程序接口
        ZipEntry documentXML = docxFile.getEntry("word/document.xml");

        InputStream documentXMLIS = docxFile.getInputStream(documentXML);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        Document doc = dbf.newDocumentBuilder().parse(documentXMLIS);

        /**
         * 书签列表
         */

        Element child1 = null;
        Element child2 = null;
        NodeList this_book_list = doc.getElementsByTagName("w:bookmarkStart");
        if (this_book_list.getLength() != 0) {
            for (int j = 0; j < this_book_list.getLength(); j++) {
                // 获取每个书签
                Element oldBookStart = (Element) this_book_list.item(j);
                // 书签名
                String bookMarkName = oldBookStart.getAttribute("w:name");
                // writeLog("bookMarkName:" + bookMarkName);
                if("mx1start".equals(bookMarkName)){
					child1 = (Element)oldBookStart.getParentNode().getNextSibling();
				}
				if("mx2start".equals(bookMarkName)){
        			child2 = (Element)oldBookStart.getParentNode().getNextSibling();
				}


                // 书签名，跟需要替换的书签传入的map集合比较
                for (Map.Entry<String, String> entry : bookMarkMap.entrySet()) {
                    // 书签处值开始
                    Node wr = doc.createElement("w:r");
                    Node wt = doc.createElement("w:t");
                    String entryVal = Util.null2String(entry.getValue());
                    // writeLog("entryVal：" + entryVal); 添加节点前要除去null值，防止异常
                    //if ("".equals(entryVal)) {
                        Node wt_text = doc.createTextNode(entry.getValue());
                        wt.appendChild(wt_text);
                        wr.appendChild(wt);
                        // 书签处值结束
                        if (entry.getKey().equals(bookMarkName)) {
                            writeLog("书签名：" + entry.getKey());
                            Element node = (Element) oldBookStart.getNextSibling();// 获取兄弟节点w:r
                            // 如果书签处无文字,则在书签处添加需要替换的内容，如果书签处存在描述文字，则替换内容,用w:r
                            NodeList wtList = node.getElementsByTagName("w:t");// 获取w:r标签下的显示书签处内容标签w:t
                            if (wtList.getLength() == 0) { // 如果不存在，即，书签处本来就无内容，则添加需要替换的内容
                                oldBookStart.appendChild(wr);
                                writeLog("书签值:" + wt_text.getNodeValue());
                            } else { // 如果书签处有内容，则直接替换内容
                                Element wtNode = (Element) wtList.item(0);
                                wtNode.setTextContent(entry.getValue());
                                writeLog("变更书签内容：" + entry.getValue());
                            }
                        }
                    //}
                }
            }
        	doDetail1(child1,null,tablename,mainid);
        	doDetail2(child2,null,tablename,mainid);
        }

        // writeLog("书签值处理完成，next:" + doc.toString());
        Transformer t = TransformerFactory.newInstance().newTransformer();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        t.transform(new DOMSource(doc), new StreamResult(baos));
        //D:\\test\\response.docx
        ZipOutputStream docxOutFile = new ZipOutputStream(new FileOutputStream(
                outFileUrl));
        // writeLog("docxOutFile，next:" + docxOutFile);
        Enumeration entriesIter = docxFile.entries();
        // writeLog("即将开始循环，next");
        while (entriesIter.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entriesIter.nextElement();
            // 如果是document.xml则修改，别的文件直接拷贝，不改变word的样式
            if (entry.getName().equals("word/document.xml")) {
                byte[] data = baos.toByteArray();
                docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
                docxOutFile.write(data, 0, data.length);
                docxOutFile.closeEntry();
            } else {
                InputStream incoming = docxFile.getInputStream(entry);
                ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = -1;
                while ((len = incoming.read(buffer)) != -1) {
                    outSteam.write(buffer, 0, len);
                }
                outSteam.close();
                incoming.close();

                docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
                docxOutFile.write(outSteam.toByteArray(), 0, (int) entry.getSize());
                docxOutFile.closeEntry();
            }
        }
        docxOutFile.close();
    }

    /**
     * 创建系统文档
     *
     * @param fileUrl    生成的PDF文档路径
     * @param docCategory 文档目录id
     * @param filename   文档名称
     * @param createrid  创建人id
     * @return 文档id
     * @throws Exception 创建失败
     */
    public String createDoc(String fileUrl, String docCategory, String filename, String createrid) throws Exception {
        String docid = "";
        String uploadBuffer = "";
        FileInputStream fi = new FileInputStream(new File(fileUrl));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int count = 0;
        while ((count = fi.read(buffer)) >= 0) {
            baos.write(buffer, 0, count);
        }
        uploadBuffer = Base64.encode(baos.toByteArray());
        // log.writeLog("uploadBuffer:"+uploadBuffer.length());
        docid = getDocId(filename, uploadBuffer, createrid, docCategory);
        return docid;
    }

    /**
     * 获取文档id
     *
     * @param name        文档名称
     * @param value       文档内容
     * @param createrid   创建人id
     * @param seccategory 目录
     * @return docId 文档id，-1失败
     * @throws Exception 异常
     */
    private String getDocId(String name, String value, String createrid, String seccategory) throws Exception {
        String docId = "";
        DocInfo di = new DocInfo();
        di.setMaincategory(0);
        di.setSubcategory(0);
        di.setSeccategory(Util.getIntValue(seccategory));
        di.setDocSubject(name.substring(0, name.lastIndexOf(".")));
        DocAttachment doca = new DocAttachment();
        doca.setFilename(name);
        byte[] buffer = new BASE64Decoder().decodeBuffer(value);
        String encode = Base64.encode(buffer);
        doca.setFilecontent(encode);
        DocAttachment[] docs = new DocAttachment[1];
        docs[0] = doca;
        di.setAttachments(docs);
        String departmentId = "-1";
        String sql = "select departmentid from hrmresource where id=" + createrid;
        RecordSet rs = new RecordSet();
        rs.executeSql(sql);
        User user = new User();
        if (rs.next()) {
            departmentId = Util.null2String(rs.getString("departmentid"));
        }
        user.setUid(Util.getIntValue(createrid));
        user.setUserDepartment(Util.getIntValue(departmentId));
        user.setLanguage(7);
        user.setLogintype("1");
        user.setLoginip("127.0.0.1");
        DocServiceImpl ds = new DocServiceImpl();
        try {
            docId = String.valueOf(ds.createDocByUser(di, user));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return docId;
    }

    /**
     * 开发技巧，一键开关日志，if(true)开启；if(false)关闭
     *
     * @param obj
     */
    private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }
    
    public  int office2PDF(String sourceFile, String destFile) throws FileNotFoundException {    
        try {    
            File inputFile = new File(sourceFile);    
            if (!inputFile.exists()) {    
                return -1;// 找不到源文件, 则返回-1    
            }    
    
            // 如果目标路径不存在, 则新建该路径    
            File outputFile = new File(destFile);    
            if (!outputFile.getParentFile().exists()) {    
                outputFile.getParentFile().mkdirs();    
            }    
         
            // connect to an OpenOffice.org instance running on port 8100    
            OpenOfficeConnection connection = new SocketOpenOfficeConnection(    
                    "127.0.0.1", 8100);    
            connection.connect();    
    
            // convert    
            DocumentConverter converter = new OpenOfficeDocumentConverter(    
                    connection);    
            converter.convert(inputFile, outputFile);    
    
            // close the connection    
            connection.disconnect();  
            return 0;    
        } catch (ConnectException e) {    
            e.printStackTrace();      
        } catch (IOException e) {    
            e.printStackTrace();    
        }    
    
        return 1;    
    }    

}
