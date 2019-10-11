package morningcore.sap;

import com.sap.mw.jco.JCO;

public class SAPConn{
	final static long serialVersionUID=90;
	final static String POOL_NAME = "Pool";
	private static JCO.Client mConnection=null;

    private static String sapclient = "";
    private static String userid = "";
    private static String password = "";
    private static String hostname = "";
    private static String systemnumber = "";
    private static String Language = "";

	/*
	*初始化连接
	*/
	public void SAPConn () {
	}

	/*
	*初始化连接
	*/
	private void init(){
		try{			
			System.out.println("-------------SAP Client Connects Start ----------------"); 
			sapclient = "640";//SAP 客户端
			userid = "OAFW";//用户名
			password = "123456";//密码
			hostname = "172.21.2.5";//服务器
			systemnumber = "00";//编号
			Language = "ZH";//语言
			JCO.Pool pool = JCO.getClientPoolManager().getPool(POOL_NAME);
			if (pool == null) {
				JCO.addClientPool(POOL_NAME, // pool name
						100, // maximum number of connections
						sapclient,
						userid,
						password,
						Language,
						hostname,
						systemnumber);
			}
			setConnection(JCO.getClient(POOL_NAME));//获取连接
			System.out.println("get connection success");
		}catch (Exception e) {
			System.out.println("get connection error:"+e);
		}
	}
	/*
	*释放连接
	*/
	public void releaseC(){
		if(mConnection!=null)
			JCO.releaseClient(mConnection);
	}

	/*
	*获取连接
	*/
	public JCO.Client getConnection(){
		if (mConnection==null)
		{
			init();
		}
		return mConnection;
	}
	/*
	*设置连接
	*/
	public void setConnection(JCO.Client conn){
		mConnection = conn;
	}

    public static void main(String[] a)
    {
    	SAPConn SAPConn = new SAPConn();
    	SAPConn.init();
    	 
    }
	
}
