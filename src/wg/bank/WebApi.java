package wg.bank;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;


public class WebApi {
	public static String postConnection(String url, String param) throws Exception {
		PrintWriter printWriter = null;
		BufferedReader bufferedReader = null;			
		HttpURLConnection httpURLConnection = null;

		StringBuffer responseResult = new StringBuffer();

		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			httpURLConnection = (HttpURLConnection) realUrl.openConnection();
			httpURLConnection.setConnectTimeout(3000);
			httpURLConnection.setReadTimeout(3000);
			// 设置通用的请求属性
			httpURLConnection.setRequestProperty("accept", "*/*");
			httpURLConnection.setRequestProperty("connection", "Keep-Alive");
			httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			httpURLConnection.setRequestProperty("Charset", "UTF-8");
			//httpURLConnection.setRequestProperty("Content-type", "application/json;charset=UTF-8"); 
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			//printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			DataOutputStream  out =new DataOutputStream (httpURLConnection.getOutputStream());
			out.write(param.toString().getBytes("UTF-8"));
			// 发送请求参数
			//printWriter.write(new String(param.toString().getBytes(), "UTF-8"));
			// flush输出流的缓冲
			out.flush();
			out.close();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的ResponseData
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
	
	public static String testurl(String url,String msg) throws Exception   {
		HttpPost post = new HttpPost(url);

		ResponseHandler responseHandler = new BasicResponseHandler();
		StringEntity entity;
		try {
			entity = new StringEntity(msg, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new Exception(e);
		}
		post.setEntity(entity);
		HttpClient httpClient = new DefaultHttpClient();
		String response;
		try {
			response = (String) httpClient.execute(post,
					responseHandler);
			response=new String(response.getBytes("ISO-8859-1"),"UTF-8");
		} catch (ClientProtocolException e) {
			httpClient.getConnectionManager().shutdown();
			throw new Exception(e);
		} catch (IOException e) {
			httpClient.getConnectionManager().shutdown();
			throw new Exception(e);
		}	
		httpClient.getConnectionManager().shutdown();
		return response;
	}
	
	public static void main(String[] args) throws Exception {
		String parm="";
		//String zoataa="{'MANDT':'130','LC_TYPE':'OA002','REF_NO':'1000010440','INDEX_NO':'100000000000010','CRDATE':'2017-09-07','CRTIME':'14:46:34','CREATEDBY':'BC_CONNECT','STATUS':'N','LC_NO':'','OA_MD5':'','UPD_FLAG':'','OA_DATE':'0000-00-00','OA_TIME':'00:00:00','OA_ENDDATE':'0000-00-00','OA_ENDTIME':'00:00:00','UPD_SUC':'','UPD_DATE':'0000-00-00','UPD_TIME':'00:00:00','PERNR_F':52001990,'REMARK':''}";
	    //String headJson="{'BANFN':'1000010431','WERKS':'7600','NAME1':'金东纸业(江苏)股份有限公司工厂','ORGTX':'CIT','BSART':'NB','BATXT':'11','NETWR':15582.97,'WAERS':'RMB','BEDNR':'630000','PERNR':52001990}";
		//String itemsJson="{'ITEM1':[{'BNFPO':10,'TXZ01':'21000035测试','MATNR':'000000000021000035','MENGE':1.000,'MEINS':'909','PREIS':5194.97,'WAERS':'RMB','BADAT':'2017-09-05','LFDAT':'2017-09-05','EKGRP':'F0C','BEDNR':'630000','ERNAM':'WEIHUIQING','AFNAM':'zhurongxin','LABST':105.000,'PRLAB':0,'INSME':247.000,'SPEME':0,'KLABS':-8.000,'KINSM':18.000,'KSPEM':0},{'BNFPO':20,'TXZ01':'21000035测试','MATNR':'000000000021000035','MENGE':2.000,'MEINS':'909','PREIS':10388.00,'WAERS':'JPY','BADAT':'2017-09-07','LFDAT':'2011-09-07','EKGRP':'F0C','BEDNR':'630000','ERNAM':'WEIHUIQING','AFNAM':'weihuiqing','LABST':105.000,'PRLAB':0,'INSME':247.000,'SPEME':0,'KLABS':-8.000,'KINSM':18.000,'KSPEM':0}],'ATT':[{'REF_NO':'','ITEM':1,'ATT_ID':'00237D4570E21ED7A4F1CD78AD174306','ATT_NAME':'2016电子贺卡.jpg','ATT_TYPE':'JPG','ATT_URL':'http://172.18.95.47:8080/archive?get%26pVersion=0045%26contRep=EA%26docId=005056A878BB1EE7B6C6C1828655E609','ATT_BZ':'','REMARK1':'1000010431采购申请信息图片','REMARK2':'','CRUNAME':'DENGWEIJUN','CRDATE':'2017-09-07','CRTIME':'13:36:21'}],'WD':[]}";
		String zoataa="{\"MANDT\":\"ABI\",\"LC_TYPE\":\"ABI_001\",\"REF_NO\":\"1000010459\",\"INDEX_NO\":\"510000000000019\",\"CRDATE\":\"2017-09-07\",\"CRTIME\":\"14:46:34\",\"CREATEDBY\":\"张三\",\"STATUS\":\"N\",\"LC_NO\":\"\",\"OA_MD5\":\"22E3539C1935774A5856769F643971E81731\",\"UPD_FLAG\":\"Y\",\"OA_DATE\":\"0000-00-00\",\"OA_TIME\":\"00:00:00\",\"OA_ENDDATE\":\"0000-00-00\",\"OA_ENDTIME\":\"00:00:00\",\"UPD_SUC\":\"\",\"UPD_DATE\":\"0000-00-00\",\"UPD_TIME\":\"00:00:00\",\"PERNR_F\":\"52004218\",\"REMARK\":\"\"}";
		String headJson="{\"ACTION_CODE\":\"70004\", \"ACTION_NAME\":\"测试2\",\"REASON\":\"无\",\"APPROVER\":\"52001991,52001722\"}";
		//String itemsJson="{\"ITEM1\":[{\"ALARM_CODE\":\"4000017\",\"ALARM_NAME\":\"test114000017\",\"DIMENSIONALITY_GROUP_NAME\":\"公司代码-渠道-省办（金红叶）-日历年/周\",\"KPI_NAMES\":\"1021323895606743040\",\"LEVELS\":\"3\",\"PERIOD\":\"10\",\"TARGET_VALUE\":\"1\",\"MEASURE_VALUE\":\"2\",\"ALARM_ACHIEVE\":\"80%\",\"SEVERITY_GRAD\":\"1,2,3\"}],\"ITEM2\":[{\"ITEM\":\"10001\",\"ACTION_SAMPLE\":\"测试方案\",\"AIM\":\"测试目标\",\"START_CYCLE\":\"2018-8-3\",\"END_CYCLE\":\"2018-8-13\",\"COMPLETION\":\"已完成\",\"ALARM_ACHIEVE\":\"80%\",\"SDA\":\"100001S\"}],\"ATT\":[]}";
		String itemsJson="{\"ITEM1\":[{\"ALARM_CODE\":\"4000017\",\"ALARM_NAME\":\"test114000017\",\"DIMENSIONALITY_GROUP_NAME\":\"公司代码-渠道-省办（金红叶）-日历年/周\",\"KPI_NAMES\":\"1021323895606743040\",\"LEVELS\":\"3\",\"PERIOD\":\"10\",\"TARGET_VALUE\":\"1\",\"MEASURE_VALUE\":\"2\",\"ALARM_ACHIEVE\":\"80%25\",\"SEVERITY_GRAD\":\"1,2,3\"}],\"ITEM2\":[{\"ITEM\":\"10001\",\"ACTION_SAMPLE\":\"测试方案\",\"AIM\":\"测试目标\",\"START_CYCLE\":\"2018-8-3\",\"END_CYCLE\":\"2018-8-13\",\"COMPLETION\":\"已完成\",\"ALARM_ACHIEVE\":\"80%25\",\"SDA\":\"100001S\"}],\"ATT\":[]}";
		String textJson="[{'NAME':'LTXT1','TLINE':'据报道，因害怕被与其有经济往来的、被捕的赵某牵出，山东省济南市委原书记王敏惶惶不可终日，极度煎熬，几乎崩溃。他在《忏悔书》中说：\"夜夜难以入睡，几乎天天半夜惊出一身冷汗，醒来就再也睡不着，总想不知道什么时候就出事。白天常常魂不守舍，省委通知开会，怕在会场被带走；上班时怕回不了家；上级领导约去谈工作，也怕是借题下菜。开会时在台上坐着，往往心不在焉，只得强打精神撑着；一个人时，唉声叹气，多次用拳头敲打自己的脑袋，发泄胸中压力。\"\r\n有此感触的不止王敏。不少两面人都有类似的心理困境。到底是什么让王敏们变得惶恐不安？\r\n乍一看，转折点似乎是那个与他有经济往来之人的被捕，由此带来不安全感。其实，这不过是一个导火索，点燃了原本就埋藏在他们心中的恐惧之火，冲垮了他们维持两面人的最后一道精神防线。'}]";
		parm="ZOATJson="+zoataa+"&headJson="+headJson+"&itemsJson="+itemsJson;
		System.out.println(parm);
		//parm="ZOATJson={\"MANDT\":\"ABI\",\"LC_TYPE\":\"ABI_001\",\"REF_NO\":\"1000010459\",\"INDEX_NO\":\"510000000000014\",\"CRDATE\":\"2017-09-07\",\"CRTIME\":\"14:46:34\",\"CREATEDBY\":\"张三\",\"STATUS\":\"N\",\"LC_NO\":\"\",\"OA_MD5\":\"22E3539C1935774A5856769F643971E81731\",\"UPD_FLAG\":\"Y\",\"OA_DATE\":\"0000-00-00\",\"OA_TIME\":\"00:00:00\",\"OA_ENDDATE\":\"0000-00-00\",\"OA_ENDTIME\":\"00:00:00\",\"UPD_SUC\":\"\",\"UPD_DATE\":\"0000-00-00\",\"UPD_TIME\":\"00:00:00\",\"PERNR_F\":\"52004218\",\"REMARK\":\"\"}&headJson={\"ACTION_CODE\":\"70004\", \"ACTION_NAME\":\"测试2\",\"REASON\":\"无\",\"APPROVER\":\"52001991,52001722\"}&itemsJson={\"ITEM1\":[{\"ALARM_CODE\":\"4000017\",\"ALARM_NAME\":\"test114000017\",\"DIMENSIONALITY_GROUP_NAME\":\"公司代码-渠道-省办（金红叶）-日历年/周\",\"KPI_NAMES\":\"1021323895606743040\",\"LEVELS\":\"3\",\"PERIOD\":\"10\",\"TARGET_VALUE\":\"1\",\"MEASURE_VALUE\":\"2\",\"ALARM_ACHIEVE\":\"80%\",\"SEVERITY_GRAD\":\"1,2,3\"}],\"ITEM2\":[{\"ITEM\":\"10001\",\"ACTION_SAMPLE\":\"测试方案\",\"AIM\":\"测试目标\",\"START_CYCLE\":\"2018-8-3\",\"END_CYCLE\":\"2018-8-13\",\"COMPLETION\":\"已完成\",\"ALARM_ACHIEVE\":\"80%\",\"SDA\":\"100001S\"}],\"ATT\":[]}";
		//parm="request={\"JSONSTR\":[{\"LOGTYPE\":\"创建\",\"WORKCODE\":\"52004218\",\"APPROVETIME\":\"2018-08-10 14:49:33\",\"JOBTITLE\":\"OA开发工程师\",\"REMARK\":\"\",\"NODENAME\":\"10创建\",\"CODE\":\"24877\",\"LASTNAME\":\"李梦云\"},{\"LOGTYPE\":\"批准\",\"WORKCODE\":\"52001991\",\"APPROVETIME\":\"2018-08-10 15:30:35\",\"JOBTITLE\":\"OA开发工程师\",\"REMARK\":\"\",\"NODENAME\":\"20审批\",\"CODE\":\"24877\",\"LASTNAME\":\"朱林军\"},{\"LOGTYPE\":\"\",\"WORKCODE\":\"52001722\",\"APPROVETIME\":\"\",\"JOBTITLE\":\"IT开发中心高级经理\",\"REMARK\":\"\",\"NODENAME\":\"20审批\",\"CODE\":\"24877\",\"LASTNAME\":\"朱荣新\"}],\"OASTATUS\":\"F\",\"CODE\":\"24877\"}";
		//System.out.println(parm);
		String sr = testurl("http://172.18.95.96/APPDEV/HQ/SAPOAInterface/ABI/CreateRequestABIService/request",parm);
		//String sr = postConnection("http://172.18.209.187:8002/api/RuleMaintenance/InsertRuleApproveResult",parm);
		//String sr = postConnection("http://172.18.88.46:8080/testwxprj/Acceptsap","山东省济南市委原书记王敏惶惶不可终日");
		System.out.println(sr);
	}
	
	
}
