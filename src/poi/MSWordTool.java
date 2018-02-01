package poi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTHeight;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTrPr;
import org.w3c.dom.Node;

/**
 * ʹ��POI,����Word��صĲ���
 * 
 *
 * @author    xuyu
 * 
 * <p>Modification History:</p>
 * <p>Date       Author      Description</p>
 * <p>------------------------------------------------------------------</p>
 * <p> </p>
 * <p>  </p>
 */
public class MSWordTool {

	/** �ڲ�ʹ�õ��ĵ����� **/
	private XWPFDocument document;
	
	private BookMarks    bookMarks = null;
	
	/**
	 * Ϊ�ĵ�����ģ��
	 * @param templatePath  ģ���ļ�����
	 */
	public void setTemplate(String templatePath) {
		try {
			this.document = new XWPFDocument(
			         POIXMLDocument.openPackage(templatePath));
			
			bookMarks = new BookMarks(document);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}	
	
	
	/**
	 * ���б�ǩ�滻������,�����Map�У�key��ʾ��ǩ���ƣ�value���滻����Ϣ
	 * @param indicator
	 */
	public void  replaceBookMark(Map<String,String> indicator) {
		//ѭ�������滻
		Iterator<String> bookMarkIter = bookMarks.getNameIterator();
		while (bookMarkIter.hasNext()) {
			String bookMarkName = bookMarkIter.next();
			
			//�õ���ǩ����
			BookMark bookMark = bookMarks.getBookmark(bookMarkName);

			//�����滻
			if (indicator.get(bookMarkName)!=null) {
				bookMark.insertTextAtBookMark(indicator.get(bookMarkName), BookMark.INSERT_BEFORE);
			}
			
		}
				
	}
	
	public void fillTableAtBookMark(String bookMarkName,List<Map<String,String>> content) {
		
		//rowNum���Ƚϱ�ǩ�ڱ�����һ��
		int rowNum = 0;
		
		//���ȵõ���ǩ
		BookMark bookMark = bookMarks.getBookmark(bookMarkName);
		Map<String, String> columnMap = new HashMap<String, String>();
		Map<String, Node> styleNode = new HashMap<String, Node>();
		
		//��ǩ�Ƿ��ڱ����
		if(bookMark.isInTable()){
			
			//��ñ�ǩ��Ӧ��Table�����Row����
			XWPFTable table = bookMark.getContainerTable();
			XWPFTableRow row = bookMark.getContainerTableRow();
			CTRow ctRow = row.getCtRow();
			List<XWPFTableCell> rowCell = row.getTableCells();
			for(int i = 0; i < rowCell.size(); i++){
				columnMap.put(i+"", rowCell.get(i).getText().trim());
				//System.out.println(rowCell.get(i).getParagraphs().get(0).createRun().getFontSize());
				//System.out.println(rowCell.get(i).getParagraphs().get(0).getCTP());
				//System.out.println(rowCell.get(i).getParagraphs().get(0).getStyle());
				
				//��ȡ�õ�Ԫ������xml���õ����ڵ�
				Node node1 = rowCell.get(i).getParagraphs().get(0).getCTP().getDomNode();
				
				//�������ڵ�������ӽڵ�
				for (int x=0;x<node1.getChildNodes().getLength();x++) {
					 if (node1.getChildNodes().item(x).getNodeName().equals(BookMark.RUN_NODE_NAME)) {
						 Node node2 = node1.getChildNodes().item(x);
						 
						 //�������нڵ�Ϊ"w:r"�������Լ��㣬�ҵ��ڵ���Ϊ"w:rPr"�Ľڵ�
						 for (int y=0;y<node2.getChildNodes().getLength();y++) {
							 if(node2.getChildNodes().item(y).getNodeName().endsWith(BookMark.STYLE_NODE_NAME)){
								 
								 //���ڵ�Ϊ"w:rPr"�Ľڵ�(�����ʽ)�浽HashMap��
								 styleNode.put(i+"", node2.getChildNodes().item(y));
							 }
						 }
					 } else {
						 continue;
					 }
				 }
			}

			//ѭ���Աȣ��ҵ�����������λ�ã�ɾ������			
			for(int i = 0; i < table.getNumberOfRows(); i++){
				if(table.getRow(i).equals(row)){
					rowNum = i;
					break;
				}
			}
			table.removeRow(rowNum);
			
			for(int i = 0; i < content.size(); i++){
				//�����µ�һ��,��Ԫ�����Ǳ�ĵ�һ�еĵ�Ԫ����,
				//�����������ʱ��Ҫ�жϵ�Ԫ�����Ƿ�һ��
				XWPFTableRow tableRow = table.createRow();
				CTTrPr trPr = tableRow.getCtRow().addNewTrPr();
           	 	CTHeight ht = trPr.addNewTrHeight();
           	 	ht.setVal(BigInteger.valueOf(360));
			}
			
			//�õ��������
			int rcount = table.getNumberOfRows();
			for(int i = rowNum; i < rcount; i++){
				XWPFTableRow newRow = table.getRow(i);
				
				//�ж�newRow�ĵ�Ԫ�����ǲ��Ǹ���ǩ�����еĵ�Ԫ����
				if(newRow.getTableCells().size() != rowCell.size()){
					
					//����newRow����ǩ�����е�Ԫ������ľ���ֵ
					//���newRow�ĵ�Ԫ����������ǩ�����еĵ�Ԫ����������ͨ���˷�������������ͨ��������ı����滻�����
					//���newRow�ĵ�Ԫ����������ǩ�����еĵ�Ԫ������Ҫ���ٵĵ�Ԫ����
					int sub= Math.abs(newRow.getTableCells().size() - rowCell.size());
					//��ȱ�ٵĵ�Ԫ����
					for(int j = 0;j < sub; j++){
						newRow.addNewTableCell();
					}
				}

				List<XWPFTableCell> cells = newRow.getTableCells();

				for(int j = 0; j < cells.size(); j++){
					XWPFParagraph para = cells.get(j).getParagraphs().get(0);
					XWPFRun run = para.createRun();
					if(content.get(i-rowNum).get(columnMap.get(j+"")) != null){
						
						//�ı䵥Ԫ���ֵ�����������øı䵥Ԫ���ֵ 
						run.setText(content.get(i-rowNum).get(columnMap.get(j+""))+"");

						//����Ԫ�����������ʽ��Ϊԭ����Ԫ��������ʽ
						run.getCTR().getDomNode().insertBefore(styleNode.get(j+"").cloneNode(true), run.getCTR().getDomNode().getFirstChild());
					}
					
					para.setAlignment(ParagraphAlignment.CENTER);
				}
			}
		}
	}
	
	public void replaceText(Map<String,String> bookmarkMap, String bookMarkName) {
		
		//���ȵõ���ǩ
		BookMark bookMark = bookMarks.getBookmark(bookMarkName);
		//�����ǩ��ǵı��
		XWPFTable table = bookMark.getContainerTable();
		//������еı�
		//Iterator<XWPFTable> it = document.getTablesIterator();
		
		if(table != null){			
			//�õ��ñ��������
			int rcount = table.getNumberOfRows();
			for(int i = 0 ;i < rcount; i++){
				XWPFTableRow row = table.getRow(i);
				
				//�񵽸��е����е�Ԫ��
				List<XWPFTableCell> cells = row.getTableCells();
				for(XWPFTableCell c : cells){
					for(Entry<String,String> e : bookmarkMap.entrySet()){
						if(c.getText().equals(e.getKey())){
							
							//ɾ����Ԫ������
							c.removeParagraph(0);
							
							//����Ԫ��ֵ
							c.setText(e.getValue());
						}
					}
				}
			}
		}
	}

	public void saveAs() {
	    File newFile = new File("e:\\test\\Wordģ��_REPLACE.docx");
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(newFile);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.document.write(fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
}	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		MSWordTool changer = new MSWordTool();
		changer.setTemplate("e:\\test\\Wordģ��.docx");
		Map<String,String> content = new HashMap<String,String>();
		content.put("Principles", "��ʽ�淶����׼ͳһ����������");
		content.put("Purpose", "�淶�����������߻�������");
		content.put("Scope", "��˾���顢����֮��ҵ��Э������");
		
		content.put("customerName", "**���޹�˾");
		content.put("address", "����·2��");
		content.put("userNo", "3021170207");
		content.put("tradeName", "ˮ������");
		content.put("price1", "1.085");
		content.put("price2", "0.906");
		content.put("price3", "0.433");
		content.put("numPrice", "0.675");
		
		content.put("company_name", "**���޹�˾");
		content.put("company_address", "����·2��");
		changer.replaceBookMark(content);
		
		
		//�滻����ǩ
		List<Map<String ,String>> content2 = new ArrayList<Map<String, String>>();
		Map<String, String> table1 = new HashMap<String, String>();

		table1.put("MONTH", "*�·�");
		table1.put("SALE_DEP", "75��");
		table1.put("TECH_CENTER", "80��");
		table1.put("CUSTOMER_SERVICE", "85��");
		table1.put("HUMAN_RESOURCES", "90��");
		table1.put("FINANCIAL", "95��");
		table1.put("WORKSHOP", "80��");
		table1.put("TOTAL", "85��");
		
		for(int i = 0; i < 3; i++){
			content2.add(table1);
		}
		changer.fillTableAtBookMark("Table" ,content2);
		changer.fillTableAtBookMark("month", content2);
		
		//������ı����滻
		Map<String, String> table = new HashMap<String, String>();
		table.put("CUSTOMER_NAME", "**���޹�˾");
		table.put("ADDRESS", "����·2��");
		table.put("USER_NO", "3021170207");
		table.put("tradeName", "ˮ������");
		table.put("PRICE_1", "1.085");
		table.put("PRICE_2", "0.906");
		table.put("PRICE_3", "0.433");
		table.put("NUM_PRICE", "0.675");
		changer.replaceText(table,"Table2");

		//�����滻���WORD
		changer.saveAs();
		System.out.println("time=="+(System.currentTimeMillis() - startTime));

	}

}
