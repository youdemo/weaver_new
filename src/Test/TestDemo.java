package Test;

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

public class TestDemo {

	public static void main(String args[]) throws Exception {
		modifyDocumentAndSave();
		
	}

	public static void modifyDocumentAndSave() throws Exception {
		// 使用java.util打开文件
		File file = new File("D:\\test\\2017年AFH订制品合同模板.docx");
		boolean exist = file.exists();
		boolean read = file.canRead();
		boolean write = file.canWrite();
		System.out.println(exist);
		System.out.println(read);
		System.out.println(write);
		ZipFile docxFile = new ZipFile(file);
		// 返回ZipEntry应用程序接口
		ZipEntry documentXML = docxFile.getEntry("word/document.xml");

		InputStream documentXMLIS = docxFile.getInputStream(documentXML);

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		Document doc = dbf.newDocumentBuilder().parse(documentXMLIS);

		// linkMan tel proCode companyName fundName fundCode sysProCode
		Map<String, String> bookMarkMap = new HashMap<String, String>();

		//bookMarkMap.put("test1", "张三");
		//bookMarkMap.put("password", "888888");
		bookMarkMap.put("合同编号", "201711120001");
		bookMarkMap.put("定制方", "昆山泛微");
		bookMarkMap.put("承揽方", "■■");

		/**
		 * 书签列表
		 */
		NodeList this_book_list = doc.getElementsByTagName("w:bookmarkStart");
		if (this_book_list.getLength() != 0) {
			for (int j = 0; j < this_book_list.getLength(); j++) {
				// 获取每个书签
				Element oldBookStart = (Element) this_book_list.item(j);
				// 书签名
				String bookMarkName = oldBookStart.getAttribute("w:name");
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
		}

		Transformer t = TransformerFactory.newInstance().newTransformer();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		t.transform(new DOMSource(doc), new StreamResult(baos));
		ZipOutputStream docxOutFile = new ZipOutputStream(new FileOutputStream(
				"D:\\test\\response.docx"));
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
				// 此处设定值需慎重，如果设置小了，会破坏word文档，至于为什么会破坏，自己去思考
				byte[] data = new byte[1024 * 512];
				int readCount = incoming.read(data, 0, (int) entry.getSize());
				docxOutFile.putNextEntry(new ZipEntry(entry.getName()));
				docxOutFile.write(data, 0, readCount);
				docxOutFile.closeEntry();
			}
		}
		docxOutFile.close();
	}
}
