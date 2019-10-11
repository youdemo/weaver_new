package wg.bank;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;

public class PostToBank {
	public String postConnection(String url, String param) throws Exception {
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
			//httpURLConnection.setRequestProperty("Content-Length", String.valueOf(param.length()));
			httpURLConnection.setRequestProperty("Charset", "GBK");
			httpURLConnection.setRequestProperty("Content-type", "application/xml;charset=GBK"); 
			// 发送POST请求必须设置如下两行
			httpURLConnection.setDoOutput(true);
			httpURLConnection.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			//printWriter = new PrintWriter(httpURLConnection.getOutputStream());
			DataOutputStream  out =new DataOutputStream (httpURLConnection.getOutputStream());
			out.write(param.toString().getBytes("GBK"));
			// 发送请求参数
			//printWriter.write(new String(param.toString().getBytes(), "UTF-8"));
			// flush输出流的缓冲
			out.flush();
			out.close();
			// 根据ResponseCode判断连接是否成功
			int responseCode = httpURLConnection.getResponseCode();
			if (responseCode == httpURLConnection.HTTP_OK) {
				// 定义BufferedReader输入流来读取URL的ResponseData
				bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream(),"GBK"));
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					responseResult.append(line);
				}
				return new String(responseResult.toString().getBytes("UTF-8"),"UTF-8").toString();
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
	
	
	
	
	
	public String doPost(String ids) {	
		RecordSet rs = new RecordSet();
		RecordSet rs_dt = new RecordSet();
		String LGNNAM;
		try {
			LGNNAM = Util.null2String(new String(weaver.file.Prop.getPropValue("wgwypz","LGNNAM").getBytes("ISO-8859-1"),"UTF-8"));
		} catch (Exception e1) {
			LGNNAM = "";
		}		
		String DBTACC = Util.null2String(weaver.file.Prop.getPropValue("wgwypz","DBTACC"));//付方帐号
		String DBTBBK = Util.null2String(weaver.file.Prop.getPropValue("wgwypz","DBTBBK"));//付方开户地区代码
		//BaseBean log = new BaseBean();
		writeLog("开始");
		Map<String, String> resultMap = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = df.format(new Date());
		String result = "";
		String jkresult = "0";
		String status = "";
		String errcode = "";
		String errtxt = "";
		String zxjg = "";
		String sql_dt = "";
		String sql = "select * from uf_netbank where id in("+ids+")";
		rs.execute(sql);
		while(rs.next()) {
			String id = Util.null2String(rs.getString("id")); 
			String bank = Util.null2String(rs.getString("bank"));//开户银行
			String account = Util.null2String(rs.getString("account"));//银行账号
			String accountname = Util.null2String(rs.getString("accountname"));//账户名称
			String paymount = Util.null2String(rs.getString("paymount"));//开户银行
			String reqman = Util.null2String(rs.getString("reqman"));//申请人
			String reqdept = Util.null2String(rs.getString("reqdept"));//申请部门
			String reqdate = Util.null2String(rs.getString("reqdate"));//申请日期
			String flowno = Util.null2String(rs.getString("flowno"));//单据编号
			String paydate = Util.null2String(rs.getString("paydate"));//付款日期
			String sf = Util.null2String(rs.getString("skyhsf"));//省份
			String cs = Util.null2String(rs.getString("skyhcs"));//城市
			String zy = Util.null2String(rs.getString("zy"));//摘要
			String sfwzh = Util.null2String(rs.getString("sfwzh"));//是否为招行
//			sql_dt = "select sf from uf_sf where id="+sf;
//			rs_dt.executeSql(sql_dt);
//			if(rs_dt.next()) {
//				sf = Util.null2String(rs_dt.getString("sf"));
//			}
//			sql_dt = "select cs from uf_cs where id="+cs;
//			rs_dt.executeSql(sql_dt);
//			if(rs_dt.next()) {
//				cs = Util.null2String(rs_dt.getString("cs"));
//			}
			if("0".equals(sfwzh)) {
				sfwzh = "Y";
			}else {
				sfwzh = "N";
			}
			resultMap = new HashMap<String, String>();
			result = "";
			status = "";
			errcode = "";
			errtxt = "";
			zxjg = "";
			String parm = "<?xml   version=\"1.0\" encoding=\"GBK\"?><CMBSDKPGK>\r\n" + 
					"                           <INFO>\r\n" + 
					"                              <FUNNAM>DCPAYREQ</FUNNAM>\r\n" + 
					"                              <DATTYP>2</DATTYP>\r\n" + 
					"                              <LGNNAM>"+LGNNAM+"</LGNNAM>\r\n" + 
					"                           </INFO>\r\n" + 
					"                           <SDKPAYRQX>\r\n" + 
					"                              <BUSCOD>N02030</BUSCOD>\r\n" + //业务类别 N02030:支付 N02040:集团支付
					"                              <BUSMOD>00001</BUSMOD>\r\n" + 
					"                           </SDKPAYRQX>\r\n" + 
					"                           <DCPAYREQX>\r\n" + 
					"                              <YURREF>"+flowno+"_"+id+"</YURREF>\r\n" + //业务号
					"                              <DBTACC>"+DBTACC+"</DBTACC>\r\n" + //付款账号
					"                              <DBTBBK>"+DBTBBK+"</DBTBBK>\r\n" + //地区
					"                              <BNKFLG>"+sfwzh+"</BNKFLG>\r\n" + //Y：招行；N：非招行；
					"                              <STLCHN>N</STLCHN>\r\n" + //结算方式
					"                              <TRSAMT>"+paymount+"</TRSAMT>\r\n" + //金额
					"                              <CCYNBR>10</CCYNBR>\r\n" + //人民币
					"                              <NUSAGE>"+zy+"</NUSAGE>\r\n" + //对应对账单中的摘要
					"                              <CRTACC>"+account+"</CRTACC>\r\n" + //收方账号
	                //"                              <BRDNBR>102100099996</BRDNBR>\r\n" + //收方行号
	                "                              <CRTBNK>"+bank+"</CRTBNK>\r\n" + //收方开户行名称
					"                              <CRTNAM>"+accountname+"</CRTNAM>\r\n" + //帐户名称
					"                              <CRTPVC>"+sf+"</CRTPVC>\r\n" + //收方省份
					"                              <CRTCTY>"+cs+"</CRTCTY>\r\n" + //收方城市
					//"                              <CRTDTR>石龙区</CRTDTR>\r\n" + 
					"                              <RCVCHK>1</RCVCHK>\r\n" + 
					"                              <BUSNAR>"+zy+"</BUSNAR>\r\n" + 
					"                           </DCPAYREQX>  \r\n" + 
					"                        </CMBSDKPGK>";
			
			try {
				writeLog("result parm="+parm);
				result = postConnection("http://192.168.7.26:8080", parm);
				zxjg = result.replace("'","''");
				writeLog("result aaa:id="+id+" result="+result);
			} catch (Exception e) {
				status = "1";//失败
				errtxt = "接口调用失败";
				jkresult = "1";
				writeLog(e);
			}
			if("".equals(status)) {
				try {
					resultMap = doxml(result);
					String RETCOD = Util.null2String(resultMap.get("RETCOD"));
					String ERRMSG = Util.null2String(resultMap.get("ERRMSG"));
					String ERRCOD = Util.null2String(resultMap.get("ERRCOD"));
					String OPRALS = Util.null2String(resultMap.get("OPRALS"));
					String ERRTXT = Util.null2String(resultMap.get("ERRTXT"));
					if("0".equals(RETCOD)) {
						if(!"".equals(OPRALS) && ("初级审批".equals(OPRALS)|| "终极审批".equals(OPRALS))) {
							status = "0";
							errtxt = OPRALS;
						}else {
							status = "1";
							errtxt = ERRTXT;
							errcode = ERRCOD;
							jkresult = "2";
						}
						
					}else {
						status = "1";
						if("".equals(ERRMSG)) {
							errtxt = ERRTXT;
						}else {
							errtxt = ERRMSG;
						}
						jkresult = "2";
						
					}
				} catch (Exception e) {
					status = "1";//失败
					errtxt = "解析excel失败";
					jkresult = "1";
					writeLog(e);
				}
			}
			sql_dt = "update uf_netbank set status='"+status+"',errcode='"+errcode+"',errtxt='"+errtxt+"',clrq='"+nowDate+"',zxjg='"+zxjg+"' where id="+id;
			rs_dt.execute(sql_dt);
		}
			
		
		return jkresult;
	}
	
	public Map<String, String> doxml(String result) throws Exception {
		Map<String,String> resultMap = new HashMap<String, String>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder
				.parse(new InputSource(new StringReader(result)));
		if(doc.getElementsByTagName("RETCOD").getLength()>0) {
			resultMap.put("RETCOD", doc.getElementsByTagName("RETCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRCOD").getLength()>0) {
			resultMap.put("ERRCOD", doc.getElementsByTagName("ERRCOD").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("OPRALS").getLength()>0) {
			resultMap.put("OPRALS", doc.getElementsByTagName("OPRALS").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRTXT").getLength()>0) {
			resultMap.put("ERRTXT", doc.getElementsByTagName("ERRTXT").item(0).getTextContent());
		}
		if(doc.getElementsByTagName("ERRMSG").getLength()>0) {
			resultMap.put("ERRMSG", doc.getElementsByTagName("ERRMSG").item(0).getTextContent());
		}
		return resultMap;
	}
	private void writeLog(Object obj) {
        if (true) {
            new BaseBean().writeLog(this.getClass().getName(), obj);
        }
    }

}
