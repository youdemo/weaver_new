package zyxj.finance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

@SuppressWarnings("serial")
public class CsvOut extends HttpServlet{ 
	/** 
	   * 文件输出 
	   * @param title 
	   *        列表抬头 
	   * @param backfields 
	   *        输出sql数据列
	   * @param table 
	   *        输出sql表 
	   * @param condition 
	   *        输出sql条件 
	   * @return 
	   */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	 public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		BaseBean log = new BaseBean();
		log.writeLog("doPost-----");
		 String title = Util.null2String(request.getParameter("title"));
		 String backfields = Util.null2String(request.getParameter("backfields"));
		 String table = Util.null2String(request.getParameter("table"));
		 String condition = Util.null2String(request.getParameter("condition"));
		 RecordSet rs = new RecordSet();
		 List exportData = new ArrayList<Map>();
		 //查询装载sql数据
		 rs.execute(" select "+backfields+" from "+table+condition);
		 while(rs.next()){
			 Map row = new LinkedHashMap<String, String>();
			 String[] strarray =backfields.split(","); 
			 for (int i = 0; i < strarray.length; i++){
				String data = Util.null2String(rs.getString(strarray[i]));
				row.put(String.valueOf(i+1),data);
				//log.writeLog("row----"+row);
		 	 } 
			 exportData.add(row);
		 }
		 //log.writeLog(exportData.toString());

		 //查询装载sql数据
		 LinkedHashMap map = new LinkedHashMap(); 
		 //log.writeLog("title-----"+title);
		 String[] titlearray=title.split(","); 
		 for (int j = 0; j < titlearray.length; j++){
			map.put(String.valueOf(j+1),titlearray[j]);
	 	 }
		 
		 //log.writeLog(map.toString());
		 String fileName = "csv文件导出.csv";
		 File file = createCSVFile(exportData, map, fileName); 
		 exportFile(response,file,fileName);
	 }
	
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doPost(request,response);
	} 
	/** 
	 	* 文件输出
		* @param file 文件
		* @param fileName 文件名
	 */
	 public static void exportFile(HttpServletResponse response, File file, String fileName) throws IOException {
		 response.setContentType("application/csv;charset=UTF-8");
		 response.setHeader("Content-Disposition",
				 "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
		 InputStream in = null;
		 try {
			 in = new FileInputStream(file);
			 int len = 0;
			 byte[] buffer = new byte[1024];
			 response.setCharacterEncoding("UTF-8");
			 OutputStream out = response.getOutputStream();
			 while ((len = in.read(buffer)) > 0) {
				 out.write(new byte[] { (byte) 0xEF, (byte) 0xBB, (byte) 0xBF });
				 out.write(buffer, 0, len);
			 }
		 } catch (FileNotFoundException e) {
			 System.out.println(e);
		 } finally {
			 if (in != null) {
				 try {
					 in.close();
				 } catch (Exception e) {
					 throw new RuntimeException(e);
				 }
			 }
		 }
	 }
	 
	/** 
	 	* 文件生成
		* @param exportData 数据列
		* @param map 标题
		* @param fileName 文件名
	 */
	 @SuppressWarnings("rawtypes")
	 public static File createCSVFile(List exportData, LinkedHashMap map,String fileName) {
		//BaseBean log = new BaseBean();
		File csvFile = null;
		BufferedWriter csvFileOutputStream = null;
		try {
		 //定义文件名格式
		 csvFile = File.createTempFile(fileName,".csv");
		 csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "UTF-8"), 1024);
		 System.out.println("csvFileOutputStream：" + csvFileOutputStream);
		 // 写入文件头部  
         for (Iterator propertyIterator = map.entrySet().iterator(); 
                 propertyIterator.hasNext();) {
             java.util.Map.Entry propertyEntry = 
                     (java.util.Map.Entry) propertyIterator.next();
             csvFileOutputStream.write(
                     "" + (String) propertyEntry.getValue() != null ?
                             (String) propertyEntry.getValue() : "" + "");
             if (propertyIterator.hasNext()) {
                 csvFileOutputStream.write(",");
             }
         }
		 csvFileOutputStream.newLine();
		 // 写入文件内容  
         for (Iterator iterator = exportData.iterator(); iterator.hasNext();) {
             Object row = (Object) iterator.next();
             for (Iterator propertyIterator = map.entrySet().iterator(); 
                     propertyIterator.hasNext();) {
                 java.util.Map.Entry propertyEntry = 
                         (java.util.Map.Entry) propertyIterator.next();
                 csvFileOutputStream.write((String) BeanUtils.getProperty(
                         row, (String) propertyEntry.getKey()));
                 if (propertyIterator.hasNext()) {
                     csvFileOutputStream.write(",");
                 }
             }
			 if (iterator.hasNext()) {
				 csvFileOutputStream.newLine();
			 }
		 }
		 csvFileOutputStream.flush();
		} catch (Exception e){
			e.printStackTrace();
		} finally {
			try {
				csvFileOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return csvFile;
	 }
}
