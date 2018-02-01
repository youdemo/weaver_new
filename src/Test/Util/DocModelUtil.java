package Test.Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class DocModelUtil {
	public static void modifyDocumentAndSave(String FileUrl,Map<String, String> bkMap,String mxstart,String mxend,String outFileUrl) throws Exception {
		// 使用java.util打开文件
		File file = new File(FileUrl);
		boolean exist = file.exists();
		boolean read = file.canRead();
		boolean write = file.canWrite();
		//D:\\test\\2017年AFH订制品合同模板.docx
		ZipFile docxFile = new ZipFile(file);
		// 返回ZipEntry应用程序接口
		ZipEntry documentXML = docxFile.getEntry("word/document.xml");

		InputStream documentXMLIS = docxFile.getInputStream(documentXML);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document doc = dbf.newDocumentBuilder().parse(documentXMLIS);
		Map<String, String> bookMarkMap = bkMap;


		/**
		 * 书签列表
		 */

		Element child1=null;
		Element child2=null;
		NodeList this_book_list = doc.getElementsByTagName("w:bookmarkStart");
		if (this_book_list.getLength() != 0) {
			for (int j = 0; j < this_book_list.getLength(); j++) {
				// 获取每个书签
				Element oldBookStart = (Element) this_book_list.item(j);
				// 书签名
				String bookMarkName = oldBookStart.getAttribute("w:name");
				if(mxstart.equals(bookMarkName)){
					child1 = (Element)oldBookStart.getParentNode().getNextSibling();
				}
				if(mxend.equals(bookMarkName)){
					child2 = (Element)oldBookStart.getParentNode();
				}

			
				// 书签名，跟需要替换的书签传入的map集合比较
				for (Map.Entry<String, String> entry : bookMarkMap.entrySet()) {
					// 书签处值开始
					Node wr = doc.createElement("w:r");
					Node wt = doc.createElement("w:t");
					Node wt_text = doc.createTextNode(entry.getValue());
					wt.appendChild(wt_text);
					wr.appendChild(wt);
					// 书签处值结束
					if (entry.getKey().equals(bookMarkName)) {
						Element node = (Element) oldBookStart.getNextSibling();// 获取兄弟节点w:r
						// 如果书签处无文字,则在书签处添加需要替换的内容，如果书签处存在描述文字，则替换内容,用w:r
						NodeList wtList = node.getElementsByTagName("w:t");// 获取w:r标签下的显示书签处内容标签w:t
						if (wtList.getLength() == 0) { // 如果不存在，即，书签处本来就无内容，则添加需要替换的内容
							oldBookStart.appendChild(wr);
						} else { // 如果书签处有内容，则直接替换内容
							Element wtNode = (Element) wtList.item(0);
							wtNode.setTextContent(entry.getValue());
						}

					}
				}

			}
			copy(child1,child2);
//			
		}

		Transformer t = TransformerFactory.newInstance().newTransformer();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		t.transform(new DOMSource(doc), new StreamResult(baos));
		//D:\\test\\response.docx
		ZipOutputStream docxOutFile = new ZipOutputStream(new FileOutputStream(
				outFileUrl));
		Enumeration entriesIter = docxFile.entries();
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
				docxOutFile.write(outSteam.toByteArray(), 0,(int) entry.getSize());
				docxOutFile.closeEntry();
			}
		}
		docxOutFile.close();
	}
	
	public static void copy(Element child1,Element child2){
		Element root=(Element)child1.getParentNode();
		
		
		for(int j=0;j<5;j++){
			if(j==0){
				NodeList wtList=child1.getElementsByTagName("w:t");
				for(int i=0;i<wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i);
					wtNode.setTextContent("test"+i);
				}
			}else{
				root.insertBefore(child1.cloneNode(true), child2);
				Element clone=(Element)child2.getPreviousSibling();
				NodeList wtList=clone.getElementsByTagName("w:t");
				for(int i=0;i<wtList.getLength();i++){
					Element wtNode = (Element) wtList.item(i);
					wtNode.setTextContent("test"+i);
				}
			}
		}
	}
}
