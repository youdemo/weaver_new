package jsd.util;
/**
 * @author 作者  张瑞坤
 * @version 创建时间：2018-11-13 上午11:08:09
 * 类说明
 */



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
 
 
public class Tesalkt {
    public static void main(String[] args) throws Exception {
 
        StringBuffer buffer = new StringBuffer();
        HttpURLConnection httpConn = null;
        BufferedReader reader = null;
        try {
            //url远程接口
            String strURL = "http://172.16.1.168:8080/api/ERPInfoGet/?GetType=ORG";
            //用户名
            String username = "kem.peng";
            //密码
            String password = "jst4293!";
            //原先使用的时com.sun.org.apache.xml.internal.security.utils.Base64，这个包虽然在jdk中，
            //但并不是标准的包，所以在gradle编译打包时总是无法引入改包，所以不用这个包
//            String author = "Basic " + Base64.encode((username+":"+ password).getBytes());
            //使用jdk1.8中的java.util.Base64来对字符串加密
            String author = "Basic " + Base64.encode((username+":"+ password).getBytes());
 
            URL url = new URL(strURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setRequestProperty("Authorization", author);
            httpConn.connect();
 
            reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("接口返回值："+buffer.toString());
    }
    public String test(){
    	 StringBuffer buffer = new StringBuffer();
         HttpURLConnection httpConn = null;
         BufferedReader reader = null;
         try {
             //url远程接口
             String strURL = "http://172.16.1.168:8080/api/ERPInfoGet/?GetType=ORG";
             //用户名
             String username = "jst\\kem.peng";
             //密码
             String password = "jst4293!";
             //原先使用的时com.sun.org.apache.xml.internal.security.utils.Base64，这个包虽然在jdk中，
             //但并不是标准的包，所以在gradle编译打包时总是无法引入改包，所以不用这个包
//             String author = "Basic " + Base64.encode((username+":"+ password).getBytes());
             //使用jdk1.8中的java.util.Base64来对字符串加密
             String author = "Basic " + Base64.encode((username+":"+ password).getBytes()); 
             URL url = new URL(strURL);
             httpConn = (HttpURLConnection) url.openConnection();
             httpConn.setRequestMethod("GET");
             httpConn.setRequestProperty("Authorization", author);
             httpConn.connect();
  
             reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
             String line;
             while ((line = reader.readLine()) != null) {
                 buffer.append(line);
             }
         } catch (Exception e) {
             e.printStackTrace();
         } finally {
             if (httpConn != null) {
                 httpConn.disconnect();
             }
             if (reader != null) {
                 try {
                     reader.close();
                 } catch (IOException e) {
                     e.printStackTrace();
                 }
             }
         }
        return buffer.toString();
     }
}
