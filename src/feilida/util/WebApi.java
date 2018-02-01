package feilida.util;


import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WebApi {
	/*
	public static void main1(String[] args) throws Exception {
		String json = "{\"Status\":true,\"Message\":\"已执行成功\",\"list\":{\"OADATE\":\"/Date(-62135596800000)/\",\"KOSTL\":null,\"KSTAR\":null,\"GSBER\":null,\"ANLKL\":null,\"CURRENCY\":null,\"AMOUNT\":0,\"EXECTYPE\":null,\"OPTTYPE\":null,\"OAKey\":null,\"STAFFID\":0,\"COMPID\":0,\"DEPTID\":0,\"YYYY\":null,\"MM\":null,\"GPKEY\":\"E_1100A00055,CSKU001\",\"AMOUNT0\":0,\"AMOUNT1\":285700.00000160,\"AMOUNT2\":200,\"AMOUNT3\":100,\"MSGTYP\":\"S\",\"MSGTXT\":\"已执行成功\",\"EXTSTRING\":null}}";
		JSONObject json1 = null;
		json1 = new JSONObject(json);
		if(json1.isNull("list"))
			System.out.println("12312313");
		System.out.println(json1.get("list"));
	}
	*/
	
	public String getPostConn(String urlCode,String param) {
		RecordSet rs = new RecordSet();
		rs.executeSql("select * from uf_interfaceMap where  code ='" + urlCode + "'");
		String url = "";
		if(rs.next()){
			url = Util.null2String(rs.getString("address"));
		}
		new BaseBean().writeLog("URL = " + url);
		if(url.length() < 5 ) return "F : code is not find or url is Error!";
		String res = "";
		try {
			res = postConnection(url,param);
		} catch (Exception e) {
			e.printStackTrace();
			return "F : postConnection is Error!";
		}
		
		return res;
	}
	
	public String postConnection1(String url, String param) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;			
		HttpURLConnection httpURLConnection = null;

		StringBuffer responseResult = new StringBuffer();

		try {
			URL realUrl = new URL(url);
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(1000);
			httpURLConnection.setReadTimeout(1000);
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			httpURLConnection.setRequestProperty("accept","application/json");
			printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			
			printWriter.write(param);
			printWriter.flush();
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"UTF-8"));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
				return responseResult.toString();
			}
			return null;
		} catch (ConnectException e) {
			throw new Exception(e);
		} catch (MalformedURLException e) {
			throw new Exception(e);
		} catch (IOException e) {
			throw new Exception(e);
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			httpURLConnection.disconnect();
			try {
				if (printWriter != null) {
					printWriter.close();
				}
				if (bufferedReader != null) {
					bufferedReader.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public  String postConnection(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) realUrl.openConnection();// 打开和URL之间的连接
           
            // 发送POST请求必须设置如下两行
            conn.setRequestMethod("POST"); // POST方法
            conn.setDoOutput(true);
            conn.setDoInput(true);
           
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0;   Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.connect();
           
            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");// 获取URLConnection对象对应的输出流
            out.write(param);// 发送请求参数
            out.flush();// flush输出流的缓冲
           
            in = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));//定义BufferedReader输入流来读取URL的响应
           
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
             //   System.out.println("OK");
            }
        } catch (Exception e) {
//            System.out.println("发送 POST 请求出现异常！"+e.getMessage());
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
               if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }
	
//	public static void main(String[] args) throws Exception {
//		feilida.util.WebApi wb = new feilida.util.WebApi();
//		String url = "http://172.20.20.42:9999/zh-CN/ECology/QueryBudget";
//		String OADATE = "2018-01-09";
//		String KOSTL = "1100CL0105";		
//		String Project = "";
//		String GSBER = "0001";
//		String KSTAR = "6601501106";		
//		String OAKEY = "1110012";
//		Map<String,String> mapStr = new HashMap<String, String>();
//	   	OADATE = OADATE.replace("-", ".");
//	   	mapStr.put("OADATE", OADATE);
//	   	mapStr.put("KOSTL", KOSTL);
//	   	mapStr.put("PROJECT", Project);
//	   	mapStr.put("GSBER", GSBER);
//	   	mapStr.put("KSTAR", KSTAR);
//	   	mapStr.put("OAKEY", OAKEY);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " URL : " + url );
//	    String ss = wb.getJsonStr(mapStr);
//	    System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " INP : " + ss );	
//		String sr = wb.postConnection(url,ss);
//		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date()) + " OUT : " + sr);
//	}
//	
	public String getJsonStr(Map<String,String> mapStr){
		if(mapStr == null || mapStr.isEmpty()) return "";
		Iterator<String> it = mapStr.keySet().iterator();
		StringBuffer buff = new StringBuffer();
		buff.append("{");
		String flag = "";
		while(it.hasNext()){
			String key = it.next();
			String val = mapStr.get(key);
			buff.append(flag);
			buff.append("\""); buff.append(key);buff.append("\":"); 
			buff.append("\""); buff.append(val);buff.append("\""); 
			flag = ",";
		}
		buff.append("}");
		return buff.toString();
	}
	
	public String getJsonStr(String selKey,List<Map<String,String>> listMap){
		if(listMap == null || listMap.size() < 1) return "";
		StringBuffer buff = new StringBuffer();
		buff.append("{\"");buff.append(selKey);buff.append("\":\"[");
		String flag = "";
		for(Map<String,String> mapStr : listMap){
			if(mapStr == null || mapStr.isEmpty()) continue;
			buff.append(flag);
			buff.append("{");
			Iterator<String> it = mapStr.keySet().iterator();
			String flag_1 = "";
			while(it.hasNext()){
				String key = it.next();
				String val = mapStr.get(key);
				buff.append(flag_1);
				buff.append("\\\""); buff.append(key);buff.append("\\\":"); 
				buff.append("\\\""); buff.append(val);buff.append("\\\""); 
				flag_1 = ",";
			}
			buff.append("}");
			flag = ",";
		}
		buff.append("]\"}");
		return buff.toString();
	}
}
