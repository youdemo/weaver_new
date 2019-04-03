/**
 * LdapUtil Created on 2005-6-16 14:24:56
 * 
 * Copyright(c) 2001-2004 Weaver Software Corp.All rights reserved.
 */
package Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.apache.commons.lang.StringUtils;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;

import ln.LN;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;

import com.alibaba.fastjson.JSON;

import weaver.GetProp.GetPropUtil;
import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.AES;
import weaver.general.BaseBean;
import weaver.general.GCONST;
import weaver.general.OrderProperties;
import weaver.general.Util;
import weaver.hrm.company.DepartmentComInfo;
import weaver.hrm.company.SubCompanyComInfo;
import weaver.hrm.resource.ResourceComInfo;
import weaver.hrm.webservice.HrmServiceAction;
import weaver.hrm.webservice.OrgXmlBean;
import weaver.matrix.MatrixUtil;
import weaver.rtx.OrganisationCom;
import weaver.soa.hrm.Department;
import weaver.soa.hrm.ExportResult;
import weaver.soa.hrm.HrmService;
import weaver.soa.hrm.SubCompany;
import weaver.soa.hrm.User;

/**
 * Description: LdapUtil
 * Company: 泛微软件
 *
 * @author xiaofeng.zhang
 * @version 1.0 2005-6-16
 */

/**
 * ldap 操作工具类
 */
public class MutiLdapUtil extends BaseBean {
    
    public static String DOMAIN = "ldap.domain";
    
    public static final String FACTORY_CLASS = "ldap.factoryclass";

    public static final String PROVIDER_URL = "ldap.provider";

    public static final String PRINCIPAL = "ldap.principal";

    public static final String CREDENTIALS = "ldap.credentials";
	public static final String NEED_SYNPASSWORD="ldap.needsynpassword";
    
    public static final String KEYSTORE_PATH="ldap.keystorepath";
    
    public static final String KEYSTORE_PASSWORD="ldap.keystorepassword";
    

    public static final String TYPE="ldap.type";
    private boolean istest = false;
    private String ldapfrom = "";
    private String isuseldap = "";
    private String type = "";
    private String factoryclass = "";
    private String ldapserverurl = "";
	private String ldapserverurl2 = "";
    private String ldaparea = "";
    private String ldapuser = "";
    private String ldappasswd = "";
    private String encriptpwd = "";//密码是否加密
    private String isUac = "";
    //private String uacValue = "";
    private String ldaplogin = "";
    private String ldapcondition = "";
    private String TimeModul = "0";
    private String Frequency = "";
    private String frequencyy = "";
    private String createType = "";
    private String createTime = "";
    private String errormsg = "";
    private Properties prop = null;
	private int group;
    private String subcompanycode = "";
    private String needSynPassword = "";
    private String keystorepath = "";
    private String keystorepassword = "";
    private String needSynOrg = "";
    private boolean ad2oa = false;
    private String tempDomain = "";
    private String needSynPerson = "";
    private String testUrl = "";
    private boolean test = false;
    public    String ldapId="";
    //private ArrayList<String> deparmentcodelist = new ArrayList<String>();
    private List ldaparealist = new ArrayList();
    private OrganisationCom rtxtmp = new OrganisationCom();
    
    private Map<String, String> subfield = new HashMap<String, String>();
    private Map<String, String> depfield = new HashMap<String, String>();
    private String ouattr = "";//ou类型
    private String subcompangyval = "";//表示分部的属性值
    private String departmentval = "";//表示部门的属性值
    
	
public static String SUBCOMPANYDOMAIN = "ldap.subcompanydomain";  //分部ou(ad域里的)
    public static String SUBCOMPANYCODE = "ldap.subcompanycode"; //指定分部code
    public static String SUBCOMUSERTODEPCODE = "ldap.subcomusertodepcode"; //指定分部下人员同步到的部门code
    private HashMap hmSubCompanyInfo = new HashMap();  //分部信息
    
    private DepartmentComInfo departmentComInfo;
    private SubCompanyComInfo subCompanyComInfo;
   // private SysMaintenanceLog sysMaintenanceLog = new SysMaintenanceLog();
   // private HrmServiceManager hrmServiceManager = new HrmServiceManager();
    //private OrganisationCom organisationCom = new OrganisationCom();
    private MutiLdapUtil() {
    	
    }

    public static MutiLdapUtil getInstance(boolean istest,String ldapid) {
   MutiLdapUtil util = new MutiLdapUtil();

    	if(null!=util)
    	{
    		util.istest=istest;
    		util.getLdapSet(ldapid);
    	}
    	
        return util;
    }
    public static MutiLdapUtil getInstance(String ldapid) {
    	   MutiLdapUtil util = new MutiLdapUtil();

     
    		util.istest=false;
    		if(ldapid==null||"".equals(ldapid)||"0".equals(ldapid)){
    			util.getLdapSet();
    		}else{
    			util.getLdapSet(ldapid);
    		}
    	 
        return util;
    }
    private void getLdapSet()
    {
    	isuseldap ="1";
    	RecordSet rs = new RecordSet();
    	RecordSet rs1 = new RecordSet();
    	String sql = "select * from ldapset";
    	rs.executeSql(sql);
    	if(rs.next())
    	{
    		type = Util.null2String(rs.getString("ldaptype"));
    		isuseldap = Util.null2String(rs.getString("isuseldap"));
			//isuseldap ="1";//先在代码里写死直接打开
    		if("1".equals(isuseldap)||istest)
        	{
    			ldapfrom = "2";
				//
    			if (!"".equals(type)&&isuseldap.equals("1")) {
	    			String mode = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), "authentic"));
	    	        if ("".equals(mode)) {
						OrderProperties oprop = new OrderProperties();
						oprop.load(GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
						if(!oprop.containsKey("authentic"))
						{
							oprop.put("authentic", "ldap");
							oprop.store(oprop, GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
						}
						if(!oprop.containsKey("ldap.type"))
						{
							oprop.put("ldap.type", type);
							oprop.store(oprop, GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
						}
	    	        }
    			}
	    		factoryclass = Util.null2String(rs.getString("factoryclass"));
	    		ldapserverurl = Util.null2String(rs.getString("ldapserverurl"));
				ldapserverurl2 = Util.null2String(rs.getString("ldapserverurl2"));
	    		ldaparea = Util.null2String(rs.getString("ldaparea"));
	    		ldapuser = Util.null2String(rs.getString("ldapuser"));
	    		ldappasswd = Util.null2String(rs.getString("ldappasswd"));
	    		
	    		encriptpwd = Util.null2String(rs.getString("encriptpwd"));//密码是否加密
	    		if(encriptpwd.equals("1")){//加密，对密码解密
	    			String password=new BaseBean().getPropValue("AESpassword", "pwd");
	    			if(password.equals("")){//缺省解密密码
	    				password="1";
	    			}
	    			ldappasswd = AES.decrypt(ldappasswd, password);//解密
	    		}
	    		
	    		isUac = Util.null2String(rs.getString("isUac"));
	    		//uacValue = Util.null2String(rs.getString("uacValue"));
	    		keystorepath = Util.null2String(rs.getString("keystorepath"));
	    		keystorepassword = Util.null2String(rs.getString("keystorepassword"));
	    		needSynPerson =  Util.null2String(rs.getString("needSynPerson"));
	    		
	    		ldaplogin = Util.null2String(rs.getString("ldaplogin"));
	    		
				String domain = Util.null2String(Prop.getPropValue("ldap", "domain"));
	    		if ("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin)) {
	    			
					//OrderProperties ldapprop = OrderProperties.load(GCONST.getPropertyPath()+ "ldap.properties");
	    			OrderProperties ldapprop = new OrderProperties();
	    			ldapprop.load(GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
	    			if("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin))
				
    			{
					
						ldapprop.put("domain", ldaplogin);
						ldapprop.store(ldapprop, GCONST.getPropertyPath() + "ldap.properties");
					}
    	        }
	    		ldapcondition = Util.null2String(rs.getString("ldapcondition"));
	    		
	    		TimeModul = Util.null2String(rs.getString("TimeModul"));
	    		Frequency = Util.null2String(rs.getString("Frequency"));
	    		frequencyy = Util.null2String(rs.getString("frequencyy"));
	    		createType = Util.null2String(rs.getString("createType"));
	    		createTime = Util.null2String(rs.getString("createTime"));
					needSynPassword = Util.null2String(rs.getString("needSynPassword"));
					needSynOrg = Util.null2String(rs.getString("needSynOrg"));
	    		
	    		ldaparealist = Util.TokenizerString(ldaparea, "|");
	    		
	    		rs1.executeSql("SELECT * FROM ldapsetparam order by id");
	    		prop = new Properties();
	    		while (rs1.next()) 
	    		{
	    	        String ldapattr= Util.null2String(rs1.getString("ldapattr"));
	    	        String userattr= Util.null2String(rs1.getString("userattr"));
	    	        prop.put(userattr, ldapattr);
	    		}
    		
        	}
    		else
        	{
    			if("0".equals(isuseldap))
    			{
					//将authentic写入到配置文件中
	    			String mode = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), "authentic"));
	    	        if (!"".equals(mode)) {
						OrderProperties oprop = new OrderProperties();
						oprop.load(GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
						if(oprop.containsKey("authentic"))
						{
							oprop.remove("authentic");
						}
						if(oprop.containsKey("ldap.type"))
						{
							oprop.remove("ldap.type");
						}
						oprop.store(oprop, GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
	    	        }
    			} 
    	        ldapfrom = "1";
        		prop = Prop.loadTemplateProp("ldap");
        		//
        		factoryclass = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), FACTORY_CLASS));
            	type = Prop.getPropValue(GCONST.getConfigFile(), TYPE);
            	ldapserverurl = Prop.getPropValue(GCONST.getConfigFile(), PROVIDER_URL);
            	isUac = Util.null2String(Prop.getPropValue("ldap_uac","isUac"));
               // uacValue = Util.null2String(Prop.getPropValue("ldap_uac","uacValue"));
                
                //ldapuser = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), PRINCIPAL));
                try
                {
                	ldapuser = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), PRINCIPAL)).getBytes("ISO8859-1"));
                }catch(Exception e)
                {
                	
                }
                ldappasswd = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), CREDENTIALS));
        	}
    		String domain = Util.null2String(Prop.getPropValue("ldap", "domain"));
    		if ("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin)) {
				OrderProperties ldapprop = new OrderProperties();
				ldapprop.load(GCONST.getPropertyPath()+ "ldap.properties");
				if("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin))
				{
					ldapprop.put("domain", ldaplogin);
					ldapprop.store(ldapprop, GCONST.getPropertyPath() + "ldap.properties");
				}
	        }
    	}
    }
    private void getLdapSet(String ldapid)
    {
    	
    	ldapId=ldapid;
    	 String sql="select * from ldap_multiset where id='"+ldapid+"' order by id ";
    	 writeLog("多域同步查找域配置sql＝"+sql);
		  RecordSet rs=new RecordSet();
		  rs.executeSql(sql);
		  if(rs.next()){
			  ldaparea=Util.null2String(rs.getString("ldaparea"));
			  ldaparealist = Util.TokenizerString(ldaparea, "|");
			  factoryclass=Util.null2String(rs.getString("factoryclass"));
			  ldapserverurl=Util.null2String(rs.getString("ldapserverurl"));
			  ldapuser=Util.null2String(rs.getString("ldapuser"));
			  ldappasswd=Util.null2String(rs.getString("ldappasswd"));
			 //ldapcondition=Util.null2String(rs.getString("ldapcondition"));
			  ldaplogin=Util.null2String(rs.getString("ldaplogin"));
			  ldapserverurl2 =Util.null2String(rs.getString("ldapserverurl2"));
				keystorepath = Util.null2String(rs.getString("keystorepath"));
	    		keystorepassword = Util.null2String(rs.getString("keystorepassword"));
		  }else{
			  writeLog("没有找到相关域的配置!请配置域信息!");
			  return ;
		  }
    	
    	
    	
    	isuseldap ="1";
    	RecordSet rs1 = new RecordSet();
    	 sql = "select * from ldapset";
    	rs.executeSql(sql);
    	if(rs.next())
    	{
    		type = Util.null2String(rs.getString("ldaptype"));
    		isuseldap = Util.null2String(rs.getString("isuseldap"));
			//isuseldap ="1";//先在代码里写死直接打开
    		if("1".equals(isuseldap))
        	{
    			ldapfrom = "2";
				
	    		//factoryclass = Util.null2String(rs.getString("factoryclass"));
	    		//ldapserverurl = Util.null2String(rs.getString("ldapserverurl"));
				//ldapserverurl2 = "ldap://192.168.0.44:636";//Util.null2String(rs.getString("ldapserverurl2"));
	    		//ldaparea = Util.null2String(rs.getString("ldaparea"));
	    		//ldapuser = Util.null2String(rs.getString("ldapuser"));
	    		//ldappasswd = Util.null2String(rs.getString("ldappasswd"));
	    		
	    		/*encriptpwd = Util.null2String(rs.getString("encriptpwd"));//密码是否加密
	    		if(encriptpwd.equals("1")){//加密，对密码解密
	    			String password=new BaseBean().getPropValue("AESpassword", "pwd");
	    			if(password.equals("")){//缺省解密密码
	    				password="1";
	    			}
	    			ldappasswd = AES.decrypt(ldappasswd, password);//解密
	    		}*/
	    		
	    		isUac = Util.null2String(rs.getString("isUac"));
	    		//uacValue = Util.null2String(rs.getString("uacValue"));
	    
	    		needSynPerson =  Util.null2String(rs.getString("needSynPerson"));
	    		
	    		//ldaplogin = Util.null2String(rs.getString("ldaplogin"));
	    		
				String domain = Util.null2String(Prop.getPropValue("ldap", "domain"));
	    		if ("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin)) {
	    			
					//OrderProperties ldapprop = OrderProperties.load(GCONST.getPropertyPath()+ "ldap.properties");
	    			OrderProperties ldapprop = new OrderProperties();
	    			ldapprop.load(GCONST.getPropertyPath()+GCONST.getConfigFile()+ ".properties");
	    			if("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin))
				
    			{
					
						ldapprop.put("domain", ldaplogin);
						ldapprop.store(ldapprop, GCONST.getPropertyPath() + "ldap.properties");
					}
    	        }
	    		ldapcondition = Util.null2String(rs.getString("ldapcondition"));
	    		
	    		TimeModul = Util.null2String(rs.getString("TimeModul"));
	    		Frequency = Util.null2String(rs.getString("Frequency"));
	    		frequencyy = Util.null2String(rs.getString("frequencyy"));
	    		createType = Util.null2String(rs.getString("createType"));
	    		createTime = Util.null2String(rs.getString("createTime"));
					needSynPassword = Util.null2String(rs.getString("needSynPassword"));
					needSynOrg = Util.null2String(rs.getString("needSynOrg"));
	    		
	    		ldaparealist = Util.TokenizerString(ldaparea, "|");
	    		
	    		rs1.executeSql("SELECT * FROM ldapsetparam order by id");
	    		prop = new Properties();
	    		while (rs1.next()) 
	    		{
	    	        String ldapattr= Util.null2String(rs1.getString("ldapattr"));
	    	        String userattr= Util.null2String(rs1.getString("userattr"));
	    	        prop.put(userattr, ldapattr);
	    		}
    		
        	}
    		else
        	{
    			
    	        ldapfrom = "1";
        		prop = Prop.loadTemplateProp("ldap");
        		//
        		factoryclass = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), FACTORY_CLASS));
            	type = Prop.getPropValue(GCONST.getConfigFile(), TYPE);
            	ldapserverurl = Prop.getPropValue(GCONST.getConfigFile(), PROVIDER_URL);
            	isUac = Util.null2String(Prop.getPropValue("ldap_uac","isUac"));
               // uacValue = Util.null2String(Prop.getPropValue("ldap_uac","uacValue"));
                
                //ldapuser = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), PRINCIPAL));
                try
                {
                	ldapuser = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), PRINCIPAL)).getBytes("ISO8859-1"));
                }catch(Exception e)
                {
                	
                }
                ldappasswd = Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), CREDENTIALS));
        	}
    		String domain = Util.null2String(Prop.getPropValue("ldap", "domain"));
    		if ("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin)) {
				/*OrderProperties ldapprop = new OrderProperties();
				ldapprop.load(GCONST.getPropertyPath()+ "ldap.properties");
				if("".equals(domain)||!Util.null2String(domain).equalsIgnoreCase(ldaplogin))
				{
					ldapprop.put("domain", ldaplogin);
					ldapprop.store(ldapprop, GCONST.getPropertyPath() + "ldap.properties");
				}*/
	        }
    	}
    }
    /**
     * 从ldap服务器中导出用户
     * @return
     */
    public List export() {
        try {
			ad2oa = true;
        	String filter = "";
        	if(!"".equals(ldapcondition))
        	{
        		filter = ldapcondition;
        	}
			//先判断是否同步组织结构
        /*	if("y".equals(needSynOrg)) {
        		
        		synOrganization("");
        		
	            if (type.equals("ad"))
	            {
	            	filter = "".equals(filter)?"(&(objectCategory=person)(objectClass=user))":filter;
	            	return export(filter);
	            }
	            else
	            {
	            	filter = "".equals(filter)?"objectclass=person":filter;
	            	return export(filter);
	            }
			} else {*/
				if (type.equals("ad")){
					filter = "".equals(filter)?"(&(objectCategory=person)(objectClass=user))":filter;
				}else {
					filter = "".equals(filter)?"(objectClass=inetOrgPerson)":filter;	
				}
        		return exportWithoutOrg(filter);
        	//}
    		
        } catch (Exception e) {
            writeLog(e);
            return null;
        }
        
        
        
    }
	public List exportWithoutOrg(String filter) {
    	if(!"y".equals(needSynPerson)) {//判断同步人员的开关
    		return new ArrayList();
    	}
    	HrmService service = new HrmService();
    	ArrayList<ExportResult> exp_result = new ArrayList<ExportResult>(); 
        service.setExp_result(new ArrayList());
    	//删除中间表数据
    	RecordSet rs = new RecordSet();
    	
    	try {
    		
        	for(int i = 0; i < ldaparealist.size();i++) {
        		String domain = (String) ldaparealist.get(i);
        		domain = changeStr(domain);
        		domain = buildDomain(domain);
        		tempDomain = domain;
        		InitialDirContext context = (InitialDirContext)getInitialContext();
        		SearchControls sc = new SearchControls();
                sc.setSearchScope(2);
        
        		
        		NamingEnumeration<SearchResult> results = context.search(domain, filter, sc);
        		while(results.hasMoreElements()) {
        			User u = new User();
        		
        			Enumeration enu = prop.keys();
        			SearchResult sr = results.nextElement();
        			Attributes attributes = sr.getAttributes();
        			boolean userAccountflag = true;
        			String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
        			
        			if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
                      	userAccountflag = false;
//                      	break;
                    }
        			
        			if(Util.null2String(prop.getProperty("status")).equals("")){//界面状态值未配置默认为空
        				u.setStatus(-1);
        			}
        			if(Util.null2String(prop.getProperty("seclevel")).equals("")){//安全级别未配置则置为0
        				u.setSeclevel(0);
        			}
		            String subcompanyname=""; 
		            String departmentname=""; 
		            String jobtitlename=""; 
		            String managername="";
        			
        			while(enu.hasMoreElements()) {
        				String col = (String)enu.nextElement();
        				String attr = prop.getProperty(col);
        				if(attr.indexOf("$") == 0) {
        					attr = attr.substring(1);
        					Attribute attrval = attributes.get(attr);
            				if(attrval == null) {
            					continue;
            				}
            				
            				if ("userpassword".equalsIgnoreCase(attr)) {  //userpassword is a forbidden attribute
                                continue;
            				}
            				String val = (String)attrval.get();
            				
        				  
                            if (val == null)
                                continue;
							if(col.equalsIgnoreCase("subcompanyid1")){    //分部
								subcompanyname=val;
								writeLog("得到分部:名称，"+subcompanyname);
							}
							if(col.equalsIgnoreCase("departmentid")){    //部门
								departmentname=val;
								val="0";
								writeLog("得到部门:名称，"+departmentname);
							}
							if(col.equalsIgnoreCase("jobtitle")){    //岗位
								jobtitlename=val;
								val="0";
								writeLog("得到岗位:名称，"+jobtitlename);
							}
							if(col.equalsIgnoreCase("managerid")){    //上级
								managername=val;
								val="0";
								writeLog("得到直接上级:"+managername);
							}
							if(col.equalsIgnoreCase("seclevel")){
								writeLog("安全级别值:"+val);
							}
                            BeanUtils.setProperty(u, col, val);
                            this.writeLog("col : "+col+" val : "+val);
        				} else { //use defaulst value from ldap.properties
        			 	
						if(col.equalsIgnoreCase("subcompanyid1")){    //分部
							subcompanyname=attr;
						}
						if(col.equalsIgnoreCase("departmentid")){    //部门
							departmentname=attr;
							attr="0";
						}
						if(col.equalsIgnoreCase("jobtitle")){    //岗位
							jobtitlename=attr;
							attr="0";
							writeLog("得到岗位:名称，"+attr);
						}
						if(col.equalsIgnoreCase("managerid")){    //上级
							managername=attr;
							attr="0";
							writeLog("得到直接上级:"+attr);
						}
        				this.writeLog("col : "+col+" attr : "+attr);
                    	
                        BeanUtils.setProperty(u, col, attr);
                    }
        			
        		}
    			 if("y".equals(needSynPerson)) {//判断同步人员的开关状态
                	 if(userAccountflag) {
                		// u.setDepartmentid(18);
                			u.setLdapId(ldapId);
                     		u.setLdap_domainName(ldaplogin);
                		 System.out.println("Status:"+JSON.toJSON(u));
                	
						boolean res = service.addUser(u,departmentname,jobtitlename,subcompanyname,managername);
                     	if(res) {
                     		addUser2HrmResourceTemp(u, exp_result);
                     	}
         			}else{
         				String whenchanged = "";
         				//做人员离职操作ADD by liudl
         				if(this.type.equals("ad")){
         					whenchanged = attributes.get("whenchanged").get()==null?"":(String)attributes.get("whenchanged").get();
         					whenchanged = whenchanged.substring(0, 8);
         				}else{
         					whenchanged = attributes.get("whenchanged").get()==null?"":(String)attributes.get("whenchanged").get();
         				}
         				service.synDismiss(u, whenchanged);
//         				PersonDisMissUtil util = new PersonDisMissUtil();
//         				util.synDismiss(u.getLoginid(), whenchanged);
         			}
                }
    			
        	}
        		context.close();
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
		}
    	updateHrmResourceTemp();
		try {
			ResourceComInfo rci = new ResourceComInfo();
			rci.removeResourceCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
        
      
    	return service.getExp_result();
    }
    //将用户的信息添加到HrmResourceTemp表中
    public void addUser2HrmResourceTemp(User u, ArrayList<ExportResult> exp_result) {
    	try {
    		RecordSet rs = new RecordSet();
        	String loginid = u.getLoginid();
        	
        	String sql_cols = "";
            String sql_vals = "";
        	String sql = "select * from hrmresourcetemp where loginid = '" + loginid + "'";
        	rs.executeSql(sql);
        	if(rs.next()) {
        		//做更新
        		updateHrmResourceTemp(u, exp_result);
        	} else {
        		//插入新的记录
        		  Map attrs = BeanUtils.describe(u);
                  Set cols = attrs.keySet();
                  for(java.util.Iterator iter = cols.iterator(); iter.hasNext();) {
                	  String col = (String) iter.next();
                	  if("id".equalsIgnoreCase(col)){
                      	continue;
                      }
                      String val = (String) attrs.get(col);

                      if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
                          if (sql_cols.equals("")) {
                              sql_cols += col;
                              if (User.class.getDeclaredField(col).getType() .getName() .equals("java.lang.String"))
                                  sql_vals += "'" + val + "'";
                              else
                                  sql_vals += val;
                          } else {
                              sql_cols = sql_cols + "," + col;
                              if (User.class.getDeclaredField(col).getType() .getName() .equals("java.lang.String"))
                                  sql_vals = sql_vals + "," + "'" + val + "'";
                              else
                                  sql_vals = sql_vals + "," + val;
                          }
                      }
                  }
                  
                  String managerid = (String) attrs.get("managerid");
                  String managerstr = "";
                  if(managerid != null && !managerid.equals("") && !managerid.equals("-1")) {
                      String sql2 = "select managerstr from HrmResource where id = " + Util.getIntValue(managerid);
                      rs.executeSql(sql2);

                      while (rs.next()) {
                          managerstr += rs.getString("managerstr");
                          managerstr += managerid + ",";
                          break;
                      }
                  }
                  if(!managerstr.equals("")) {
                      sql_cols = sql_cols + ",managerstr";
                      sql_vals = sql_vals + "," + "'" + managerstr + "'";
                  }
                  sql = "insert into HrmResourceTemp (" + sql_cols + ",lloginid,isADAccount) values (" + sql_vals + ",'" + Util.getEncrypt(u.getLoginid()) + "','1')";
                  boolean flag = rs.executeSql(sql);
                  if (flag) {
                      ExportResult exportResult = new ExportResult();
                      exportResult.setAccount(u.getLoginid());
                      exportResult.setLastname(u.getLastname());
                      exportResult.setOperation("82");
                      exportResult.setStatus("15242");
                      exportResult.setDepartment(u.getDepartmentid());
                      exportResult.setJobtitle(u.getJobtitle());
                      exp_result.add(exportResult);
                  } else {
                      ExportResult exportResult = new ExportResult();
                      exportResult.setAccount(u.getLoginid());
                      exportResult.setLastname(u.getLastname());
                      exportResult.setOperation("82");
                      exportResult.setStatus("498");
                      exp_result.add(exportResult);
                     
                  }
        	}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    }
    /**
     * 有组织架构同步结束之后，将中间表中含有组织架构的人员信息删除掉
     */
    public void updateHrmResourceTemp(){
    	RecordSet rs  = new RecordSet();
    	rs.execute("delete from HrmResourceTemp where id in (select t1.id from HrmResourceTemp t1 ,HrmResource t2 where t1.loginid = t2.loginid and t2.departmentid is not null )");
    }
    public void updateHrmResourceTemp(User u, ArrayList<ExportResult> exp_result) {
    	try {
    	//更新数据
    		RecordSet rs  = new RecordSet();
	    	Map attrs = BeanUtils.describe(u);
	    	String loginid = u.getLoginid();
	        Set cols = attrs.keySet();
	        String sql_set = "";
	        String sql = "";
            for (Iterator iter = cols.iterator(); iter.hasNext();) {
                String col = (String) iter.next();
                String val = (String) attrs.get(col);
                if("id".equalsIgnoreCase(col)){
                	continue;
                }
                if (!col.equalsIgnoreCase("class")&&!col.equalsIgnoreCase("loginid")&&!col.equalsIgnoreCase("account")&&!col.equalsIgnoreCase("seclevel")&&!col.equalsIgnoreCase("systemlanguage")&&!col.equalsIgnoreCase("costcenterid")&&!col.equalsIgnoreCase("status") && val != null && !val.equals("") && !val.equals("-1")) {
                    if (sql_set.equals("")) {
                        if (User.class.getDeclaredField(col).getType().getName() .equals("java.lang.String"))
                            sql_set = col + "='" + val + "'";
                        else
                            sql_set = col + "=" + val;
                    } else
                    if (User.class.getDeclaredField(col).getType().getName() .equals("java.lang.String"))
                        sql_set = sql_set + "," + col + "='" + val + "'";
                    else
                        sql_set = sql_set + "," + col + "=" + val;

                }
            }
            if(!"".equals(sql_set)) {
            	sql_set = sql_set + ",isADAccount='1'";
            } else {
            	sql_set = sql_set + "isADAccount='1'";
            }
            
            sql = "update HrmResource set " + sql_set + "  where loginid='" + loginid + "'";
            boolean flag = rs.executeSql(sql);
            if (flag) {
                ExportResult exportResult = new ExportResult();
                exportResult.setAccount(u.getLoginid());
                exportResult.setLastname(u.getLastname());
                exportResult.setOperation("17744");
                exportResult.setStatus("15242");
                exportResult.setDepartment(u.getDepartmentid());
                exportResult.setJobtitle(u.getJobtitle());
                exp_result.add(exportResult);
            } else {
                ExportResult exportResult = new ExportResult();
                exportResult.setAccount(u.getLoginid());
                exportResult.setLastname(u.getLastname());
                exportResult.setOperation("17744");
                exportResult.setStatus("498");
                exp_result.add(exportResult);
               
            }
    	} catch(Exception e) {
    		
    	}	
    	
    }
    
    public String addUser2HrmResource() {
    	try {
    		OrganisationCom organisationcom = new OrganisationCom();
        	RecordSet rs = new RecordSet();
        	RecordSet rs2 = new RecordSet();
        	String loginid = "";
        	String departmentid = "";
        	String subcompanyid = "";
        	String id = "";
        	
        	String hrmresourcetemp = "select * from HrmResourceTemp where departmentid is not null";
        	 
        	rs.executeSql(hrmresourcetemp);
        	while(rs.next()) {
        		String hrmtempid = rs.getString("id");
        		loginid = rs.getString("loginid");
        		departmentid = rs.getString("departmentid");
        		subcompanyid = rs.getString("subcompanyid1");
        		rs2.executeSql("select * from HrmResource where loginid='"+loginid+"'");
        		
        		if(rs2.next()) {
        			id = rs2.getString("id");
        			
        			rs2.executeSql("update HrmResource set departmentid='"+departmentid+"',subcompanyid1='"
            				+subcompanyid+"' where loginid='"+loginid+"'");
        			
            		//同步RTX端的用户信息.
                    organisationcom.editUser(Integer.parseInt(id));
                    
        		}
        		//删除同步的人员---更改成更新状态
                rs2.executeSql("update  HrmResourceTemp set isADAccount= '2' where id="+hrmtempid);
        		
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return "error";
		}
    	return "success";
    }
    //ad中不存在的人员进行离职处理
    /*public void synDismiss(String subcompanyid,String domain) {
    	RecordSet rs = new RecordSet();
    	RecordSet rs2 = new RecordSet();
    	try {
	    	ResourceComInfo rc = new ResourceComInfo();
	    	OrganisationCom oc = new OrganisationCom();
	    	HrmServiceManager hm = new HrmServiceManager();
	    	char separator = Util.getSeparator();
	    	String changedate = TimeUtil.getCurrentDateString();
			String filter = "(&(objectCategory=person)(objectClass=user)(sAMAccountName=$account$))";
			
			InitialDirContext context = (InitialDirContext) getInitialContext();
			SearchControls sc = new SearchControls();
		    sc.setSearchScope(2);
		    NamingEnumeration<SearchResult> result = null;
		   
	        String statementSql = "select * from hrmresource where status = '1' and subcompanyid1 = '"+subcompanyid+"'";//还有哪些状态??
	        rs.executeSql(statementSql);
	        while(rs.next()) {
	        	//人员离职处理
	        	String type = "5";
	        	String account = rs.getString("account");
	        	int id = rs.getInt("id");
	        	filter = filter.replace("$account$", account);
	        	result = context.search(domain, filter, sc);
	        	if(!result.hasMore()) {
	        		String name = rc.getResourcename(""+id);
	
	                String oldjobtitleid = rc.getJobTitle(""+id);
	                String para = ""+id+separator+changedate+separator+""+separator+""+separator+""+separator+oldjobtitleid+separator+type+separator+id;
	                boolean flag=rs.executeProc("HrmResource_Dismiss",para);
	                
	                rs.executeSql("delete from hrmrolemembers where resourceid="+id);
	                //删除手机版中设置的登录人员
		            rs.executeSql("delete from PluginLicenseUser where plugintype='mobile' and sharetype='0' and sharevalue='"+id+"'");
		            //end
	                String sql = "update HrmResource set status =5, loginid='',password='' ,account='' where id = "+id;
	                rs.executeSql(sql);
	                sql="delete hrmgroupmembers where userid="+id;
	                rs.executeSql(sql);
	                sql = "select max(id) from HrmStatusHistory";
	                rs.executeSql(sql);
	                rs.next();
	                sql = "update HrmStatusHistory set isdispose = 1 where id="+rs.getInt(1);
	                rs.executeSql(sql);
	                
	                oc.checkUser(id);
	                oc.deleteUser2(rc.getLoginID(""+id));//人员离职从RTX中删除该人员
	
	    			//OA与第三方接口单条数据同步方法开始
	                hm.SynInstantHrmResource(""+id,"3");
	    			//OA与第三方接口单条数据同步方法结束 
	                
	                //离职时离职者参与的所有协作全部标记为已读 myq 修改 2008.2.18 开始
	                rs.executeSql("select id,readers from cowork_items where coworkers like '%"+id+"%' and readers not like '"+id+"%'");
	              	while(rs.next()){
	              		String cowork_id = Util.null2String(rs.getString(1));
	              		String readers = Util.null2String(rs.getString(2));
	              		if(!readers.equals("")){ readers = readers + id + ",";}
	              		else{ readers = "," + id + ",";}
	              		rs2.executeSql("update cowork_items set readers='"+readers+"' where id="+cowork_id);
	                }
	                //离职时离职者参与的所有协作全部标记为已读
	        	}
	        	
	        }
		    
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}*/
    
    
    
    
    public void synOrganization(String time) {
    	//取得分部、部门的参数值
		RecordSet rs = new RecordSet();
		//初始化变量值
		subfield = new HashMap<String, String>();
		depfield = new HashMap<String, String>();
		ouattr = "";
		subcompangyval = "";
		departmentval = "";
		
		rs.executeSql("select * from ldapsetsubparam");
		while(rs.next()) {
			String ldapsubparam = rs.getString("ldapsubattr");
			String subparam = rs.getString("subattr");
			
			subfield.put(subparam,ldapsubparam);
		}
		rs.executeSql("select * from ldapsetdepparam");
		while(rs.next()) {
			String ldapdepparam = rs.getString("ldapdepattr");
			String depparam = rs.getString("depattr");
			
			depfield.put(depparam,ldapdepparam);
		}
		
		rs.executeSql("select * from ldapsetoutype");
		
		if(rs.next()) {
			ouattr = rs.getString("ouattr");//ou类型
		    subcompangyval = rs.getString("subcompany");//表示分部的属性值
		    departmentval = rs.getString("department");//表示部门的属性值
		}
    	
    	String pv = "";
    	String sc = "";
    	String dp = "";
    	try {
    		rs.executeSql("update addepmap set status = '2' where 1=1");
    
    		hmSubCompanyInfo.clear();  //清空上次同步信息
    		rs.executeSql("select * from ldapsetdetail order by id");
    		
         	while(rs.next()) {	
         		
         		pv = rs.getString("subcompanydomain");
         		sc = rs.getString("subcompanycode");
         		dp = rs.getString("subcomusertodepcode");
         		group = rs.getInt("id");
 	        	pv = changeStr(pv);
 	        	sc = changeStr(sc);
 	        	tempDomain = pv;
 	        	if(!Util.null2String(pv).equals("")&&!Util.null2String(sc).equals("")) {
 	        		hmSubCompanyInfo.put(pv, dp);  
 	        		try {
 	        			//先同步组织架构
 	        			String filter = "(&(objectCategory=organizationalUnit)(objectClass=organizationalUnit)(ou=*))";
 	        			if(!"".equals(time)) {//如果时间不为空，添加时间条件
 	        				filter = getFilter(filter, "(whenchanged>="+time+")");
 	        			}
 	                	getOrganization(filter, pv,sc);
 	                
 	        		}
 	        		catch(Exception e) {
 	        			writeLog("Ldap load error: SUBCOMPANYDOMAIN="+pv);
 	        			writeLog("Ldap load error: "+e.getMessage());
 	        			e.printStackTrace();
 	        			return;
 	        		}
 	        	}
 	        	else {
 	        		break;
 	        	}
         	}
         	
         	//modifyDep();    //部门
         	subCompanyComInfo = new SubCompanyComInfo();
        	subCompanyComInfo.removeCompanyCache();
         	departmentComInfo = new DepartmentComInfo();
         	departmentComInfo.removeCompanyCache();
         	//更新系统矩阵
         	MatrixUtil.sysSubcompayData();
         	MatrixUtil.sysDepartmentData();
         } catch (Exception e) {
             writeLog(e);
            
         }
    }
    
    //封存部门的方法
    /*public void closeDep() {
    	try {
			departmentComInfo = new DepartmentComInfo();
			
	    	RecordSet rs = new RecordSet();
	    	ArrayList<String> depcodeList = new ArrayList<String>();
	     	ArrayList<Integer> depidList = new ArrayList<Integer>();
	     	ArrayList<String> depnameList = new ArrayList<String>();
	        rs.executeSql("select * from hrmdepartment");
	        while(rs.next()) {
	        	depcodeList.add(rs.getString("departmentcode"));
	        	depidList.add(rs.getInt("id"));
	        	depnameList.add(rs.getString("departmentname"));
	        }
	        for(int i = 0; i < depcodeList.size();i++) {
	        	if(!deparmentcodelist.contains(depcodeList.get(i))) {
	        		//删除部门下的人，以及部门封存
	        		int depatmentid = depidList.get(i);
	        		rs.executeSql("delete from hrmresource where departmentid="+depatmentid);
	        		rs.executeSql("update hrmdepartment set canceled = '1' where id ="+ depatmentid);
	        		
	        		departmentComInfo.removeCompanyCache();
			
					sysMaintenanceLog.resetParameter();
					sysMaintenanceLog.setRelatedId(depatmentid);
					sysMaintenanceLog.setRelatedName(depnameList.get(i));
					sysMaintenanceLog.setOperateType("10");
					sysMaintenanceLog.setOperateItem("12");
					sysMaintenanceLog.setOperateUserid(1);//默认的给一个值
					sysMaintenanceLog.setClientAddress("");//ip地址为空
					
					sysMaintenanceLog.setSysLogInfo();
					organisationCom.deleteDepartment(depatmentid);//部门封存时同步到RTX
	
					//OA与第三方接口单条数据同步方法开始
					hrmServiceManager.SynInstantDepartment(""+depatmentid,"3");
					//OA与第三方接口单条数据同步方法结束
	        	}
	        }
    	} catch (Exception e1) {
			e1.printStackTrace();
		}
    }*/
    //更新oa中的部门
    public void modifyDep(){
    	RecordSet rs = new RecordSet();
    	
    	HrmServiceAction ha = new HrmServiceAction();
    	OrgXmlBean orgXmlBean =new OrgXmlBean();
		rs.execute("select * from addepmap where orgtype = '2' and status!='2' order by id");
		while(rs.next()){
			orgXmlBean.setFullname(rs.getString("dep")); //全称
			orgXmlBean.setShortname(rs.getString("dep"));  //简称
			orgXmlBean.setCode(rs.getString("guid")); //部门code
			orgXmlBean.setParent_code(rs.getString("pguid"));//上级部门
			orgXmlBean.setOrg_code(rs.getString("subcomcode")); //上级分部
			ha.addDepartment("", orgXmlBean);
			//同步RTX的部门信息
			//deparmentcodelist.add(rs.getString("guid"));
			//writeLog("增加部门：名称："+rs.getString("dep")+",部门编号："+rs.getString("guid")+",上级部门："+rs.getString("pguid"));
		}
		try {
			//rs.execute("delete from addepmap where orgtype = '2' and status='2'");
			new DepartmentComInfo().removeCompanyCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    public void getOrganization(String filter,String domain, String subcompanycode){
    	String attrname = ""; //属性名称
    	InitialDirContext context = null;
    	RecordSet rs = new RecordSet();
    	//RecordSet rs2 = new RecordSet();

    	String companyid = "";
		try {
			context = (InitialDirContext) getInitialContext();
			SearchControls sc = new SearchControls();
			
			//同步分部名称
			String[] str = domain.split(",");
	    	if(str != null && str.length > 0) {
	    		String subcompanyname = domain.split(",")[0];
	    		subcompanyname = subcompanyname.substring(subcompanyname.indexOf("=")+1);
	        	writeLog("========================export ["+filter+"] begin==========================\n");
	        	String updateSubname = "select * from hrmsubcompany where subcompanycode='"+subcompanycode+"'";
	        	rs.execute(updateSubname);
	        	if(rs.next()) {
	        		//同步分部的参数
	        		companyid = rs.getString("companyid");
	        		//rs.executeSql("update hrmsubcompany set subcompanyname='"+subcompanyname+"',subcompanydesc='"+subcompanyname+"' where subcompanycode='"+subcompanycode+"'");
	        	}
	    	}
	    	SubCompanyComInfo rci = new SubCompanyComInfo();
    		rci.removeCompanyCache();
			//取得下一级ou
			sc.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration answer = context.search(domain,filter,sc);
			while (answer.hasMoreElements()) {
			    SearchResult sr = (SearchResult) answer.next();
			    Attributes attrs = sr.getAttributes();
			    if (attrs != null) {    	
			        try {
			        	String orgname = (String)attrs.get("name").get();
		            	String distinguished = (String)attrs.get("distinguishedName").get();
		            	String objectGUID = (String)attrs.get("objectGUID").get();
		            	objectGUID = getGUID(objectGUID.getBytes());  
		            	
		            	String orgtype = "";
			            //处理组织类型
		            	if(!"".equals(ouattr)) {
		            		if(attrs.get(ouattr) != null) {
		            			String orgtypeval = (String)attrs.get(ouattr).get();
			            		if(orgtypeval!=null && orgtypeval.equals(subcompangyval)) {
				            		orgtype = "1";//分部
				            	} else if(orgtypeval!=null && orgtypeval.equals(departmentval)) {
				            		orgtype = "2";//部门
				            	}
		            		} else {
		            			orgtype = "";
		            		}
		            		
		            	}
		            	
		            	if(orgtype == null || "".equals(orgtype)) {
		            		orgtype = getOrgType(distinguished);
		            	}
			            
			            if("1".equals(orgtype)){//如果该组织是分部的话 
			            	SubCompany bean = new SubCompany();
			            	Collection c = (Collection)subfield.keySet();
			            	Iterator ite = (Iterator) c.iterator();
			            	while(ite.hasNext()) {
			            		String fieldval = (String)ite.next();
			            		String key = subfield.get(fieldval);
			            		if(key != null) {
			            			if(key.startsWith("$")) {
			            				String atttibute = key.replace("$", "");
			            				String attrVal = (String)attrs.get(atttibute).get();
			            				if("objectGUID".equals(atttibute)) {
			            					attrVal = getGUID(attrVal.getBytes());  
			            				}
			            				if("subcompanyname".equals(fieldval)) {//分部名称根据分部对应设置取值
			            					orgname = attrVal;
			            				}
			            				BeanUtils.setProperty(bean, fieldval, attrVal);//把属性值放入bean中
			            			} else {
			            				BeanUtils.setProperty(bean, fieldval, key);//不以$开头的，默认为指定的值
			            			}
			            		}
			            	}
			            	
			            	String psubguid = getadPsub(distinguished,domain,subcompanycode);//上级分部编号
			            	if("-1".equals(psubguid)) {//如果分部的上级组织是部门  则该组织不进行同步
			            		writeLog("同步失败，请检查组织结构上下级类型是否配置正确:"+distinguished);
			            		continue;
			            	}
			            	//更新addepmap表的数据
			            	RecordSet rs2 = new RecordSet();
			            	rs2.executeSql("select * from addepmap where guid = '"+objectGUID+"'");
			            	if(!rs2.next()) {
			            		rs.executeSql("insert into addepmap(dep,pguid,distin,guid,subcomcode,orgtype,status) values ('"+orgname+"','"+psubguid+"','"+distinguished+"','"+objectGUID+"','"+subcompanycode+"','"+orgtype+"','0')");
			            	} else {
			            		rs.executeSql("update addepmap set dep = '"+orgname+"',pguid='"+psubguid+"',distin='"+distinguished+"',orgtype='"+orgtype+"',subcomcode='"+subcompanycode+"',status='1' where guid = '"+objectGUID+"'");
			            		rs.executeSql("select * from addepmap where guid = '"+objectGUID+"1"+"' and orgtype='"+2+"'");//查询是否该同名的部门是否已存在，存在的话更新该部门信息
			            		if(rs.next()) {
			            			rs.executeSql("update addepmap set dep = '"+orgname+"',pguid='"+0+"',distin='"+distinguished+"',orgtype='"+2+"',subcomcode='"+subcompanycode+"',status='1' where guid = '"+objectGUID+"1"+"'");
			            		}
			            	}
			            	
			            	if("0".equals(psubguid)) {
			            		rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+subcompanycode+"'");
			            	} else {
			            		rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+objectGUID+"'");
			            	}
			            	//更新分部的信息
			            	
							if(rs.next()) {
								int subcomid = Util.getIntValue(rs.getString("id"),0);
								Map beanattrs = BeanUtils.describe(bean);
	 			                String sql_vals = "";
	 			                Collection c1 = (Collection)subfield.keySet();
	 			                for (Iterator iter = (Iterator) c1.iterator(); iter.hasNext();) {
	 			                	String col = (String) iter.next();
	 			                    String val = (String) beanattrs.get(col);
	 			                    if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	 if (SubCompany.class.getDeclaredField(col).getType().getName().equals("java.lang.String"))
	 			                                sql_vals =  sql_vals + " "+col+"='" + val + "',";
	 			                            else
	 			                            	sql_vals =  sql_vals + " "+col+"=" + val + ",";
	 			                    }
	 			                }
	 			                if(sql_vals.endsWith(",")) {
	 			                	sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
	 			                }
	 			                
	 			                rs.executeSql("update HrmSubCompany set " +sql_vals + " where id='"+subcomid+"'");
								
	 			                OrgXmlBean orgXmlBean = new OrgXmlBean();
								orgXmlBean.setFullname(orgname); //全称
								orgXmlBean.setShortname(orgname);  //简称
								orgXmlBean.setCode(objectGUID); //code
								orgXmlBean.setParent_code(psubguid);//上级
								//orgXmlBean.setOrg_code(subcompanycode); 
								updateOraddSubCompany(orgXmlBean, "update");
	 			                
							} else {//创建分部
								Map beanattrs = BeanUtils.describe(bean);
	 			                //Set cols = beanattrs.keySet();
	 			                Collection c3 = (Collection)subfield.keySet();
	 			                String sql_cols = "";
	 			                String sql_vals = "";
	 			                sql_cols = sql_cols + "subcompanycode,companyid,";
	 			                sql_vals = sql_vals + "'" + objectGUID + "','"+companyid+"',";
	 			                for (Iterator iter = (Iterator) c3.iterator(); iter.hasNext();) {
	 			                	String col = (String) iter.next();
	 			                    String val = (String) beanattrs.get(col);
	 			                    if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	 if (SubCompany.class.getDeclaredField(col).getType().getName().equals("java.lang.String")) {
	 			                    		sql_cols = sql_cols + col +","; 
 			                                sql_vals =  sql_vals + "'"+val+"',";
	 			                    	 } else {
	 			                    		sql_cols = sql_cols + col + ","; 
	 			                    		sql_vals = sql_vals + val + ",";
	 			                         }
	 			                    }
	 			                }
	 			                if(sql_vals.endsWith(",")) {
	 			                	sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
		 			                sql_cols = sql_cols.substring(0, sql_cols.length()-1);//删除逗号
	 			                }
	 			                rs.executeSql("insert into HrmSubCompany ("+sql_cols+") values ("+sql_vals+")");
	 			               
								OrgXmlBean orgXmlBean = new OrgXmlBean();
								orgXmlBean.setFullname(orgname); //全称
								orgXmlBean.setShortname(orgname);  //简称
								orgXmlBean.setCode(objectGUID); //code
								orgXmlBean.setParent_code(psubguid);//上级
								updateOraddSubCompany(orgXmlBean, "add");
							}
							//更新与分部同名的部门
							rs.executeSql("select id from HrmDepartment where departmentcode = '"+objectGUID+ "1" + "'");
							if(rs.next()) {
								//更新该部门信息
								String departmentcode = objectGUID+ "1";
								Department bean2 = new Department();
				            	Collection c2 = (Collection)depfield.keySet();
				            	Iterator ite2 = (Iterator) c2.iterator();
				            	
				            	setBean(ite2,attrs,bean2);
				            	Map beanattrs = BeanUtils.describe(bean2);
	 			                String sql_vals = "";
	 			                
	 			                for (Iterator iter = (Iterator) c2.iterator(); iter.hasNext();) {
	 			                	String col = (String) iter.next();
	 			                    String val = (String) beanattrs.get(col);
	 			                    if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	 if (Department.class.getDeclaredField(col).getType().getName().equals("java.lang.String"))
	 			                                sql_vals =  sql_vals + " "+col+"='" + val + "',";
	 			                            else 
	 			                            	sql_vals =  sql_vals + " "+col+"=" + val + ",";
	 			                    }
	 			                }
	 			               if(sql_vals.endsWith(",")) {
	 			            	  sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
	 			               }
	 			                rs.executeSql("update hrmdepartment set " +sql_vals + " where departmentcode = '"+ departmentcode + "'");
	 			               
								OrgXmlBean orgXmlBean = new OrgXmlBean();
								orgXmlBean.setFullname(orgname); //全称
								orgXmlBean.setShortname(orgname);  //简称
								orgXmlBean.setCode(objectGUID); //部门code
								orgXmlBean.setParent_code("0");//上级部门  是分部
								orgXmlBean.setOrg_code("0".equals(psubguid) ? subcompanycode : objectGUID); //上级分部
								updateOraddDepartment(orgXmlBean, "update");
							}
			            } 
			            //如果该组织是部门的话
			            if(!"".equals(objectGUID) && !"".equals(orgname) && !"".equals(distinguished) && !"1".equals(orgtype)){
			            	Department bean = new Department();
			            	Collection c = (Collection)depfield.keySet();
			            	Iterator ite = (Iterator) c.iterator();
			            	
			            	setBean(ite,attrs,bean);
			            	if(bean.getDepartmentname() != null && !"".equals(bean.getDepartmentname())) {
			            		orgname = bean.getDepartmentname();
			            	}
			            	
	                		String pguid = getadPdep(distinguished,subcompanycode, domain);//上级组织的guid  0（分部）
	                		if("-1".equals(pguid)) {
	                			writeLog("同步失败，请检查组织结构上下级类型是否配置正确:"+distinguished);
	                			continue;
	                		}
	                		String sql="select id from hrmdepartment where departmentcode = '"+objectGUID+"'";
	                		/*if(!"0".equals(pguid))//0表示上一级为分部
	                		{
		                		 sql="select id,departmentcode from hrmdepartment where departmentname='"+orgname+
		            			"' and subcompanyid1=(select id from hrmsubcompany where subcompanycode='"+subcompanycode+
		            			"') and supdepid=(select id from hrmdepartment where departmentcode='"+pguid+"')";
	                		} else {
	                			 sql="select id,departmentcode from hrmdepartment where departmentname='"+orgname+
	 	            			"' and subcompanyid1=(select id from hrmsubcompany where subcompanycode='"+subcompanycode+
	 	            			"') and supdepid='"+pguid+"'";	
	                		}*/
	                		String subcomcode = getsubcompanyOfDep(distinguished,subcompanycode,domain);
	                		
	                		//更新一下addepmap数据，确保addepmap的准确性
	                		rs.executeSql("select * from addepmap where guid = '"+objectGUID+"'");
	                		if(rs.next()){
	                			rs.executeSql("update addepmap set dep = '"+orgname+"',pguid='"+pguid+"',distin='"+distinguished+"',orgtype='"+orgtype+"',subcomcode='"+subcomcode+"',status='1' where guid = '"+objectGUID+"'");
	                		}else{
	                			rs.executeSql("insert into addepmap(dep,pguid,distin,guid,subcomcode,orgtype,status) values ('"+orgname+"','"+pguid+"','"+distinguished+"','"+objectGUID+"','"+subcomcode+"','"+orgtype+"','0')");
	                		}
	                		writeLog("判断oa系统里有没有这个部门sql:"+sql);

	                		rs.executeSql(sql);
	                		///如果OA里已存在这个部门
	                		if(rs.next()) {
	                			String recordid = rs.getString("id");
	                			Map beanattrs = BeanUtils.describe(bean);
	 			                Collection c2 = (Collection)depfield.keySet();
	 			                String sql_vals = "";
	 			                sql_vals = sql_vals + "departmentcode='"+objectGUID+"', ";
	 			                for (Iterator iter = (Iterator) c2.iterator(); iter.hasNext();) {
	 			                	String col = (String) iter.next();
	 			                    String val = (String) beanattrs.get(col);
	 			                    if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	 if (Department.class.getDeclaredField(col).getType().getName().equals("java.lang.String"))
	 			                                sql_vals =  sql_vals + " "+col+"='" + val + "',";
	 			                            else 
	 			                            	sql_vals =  sql_vals + " "+col+"=" + val + ",";
	 			                    }
	 			                }
	 			               if(sql_vals.endsWith(",")) {
	 			            	  sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
	 			               }
	 			                //System.out.println("sql_vals:"+sql_vals);
	 			                
	 			                rs.executeSql("update hrmdepartment set " +sql_vals + " where id='"+recordid+"'");
	 			               
								OrgXmlBean orgXmlBean = new OrgXmlBean();
								orgXmlBean.setFullname(orgname); //全称
								orgXmlBean.setShortname(orgname);  //简称
								orgXmlBean.setCode(objectGUID); //部门code
								orgXmlBean.setParent_code(pguid);//上级部门  是分部
								orgXmlBean.setOrg_code(subcomcode); //上级分部
								updateOraddDepartment(orgXmlBean, "update");
	                			
	                		} else {//如果OA里不存在这个部门  则新建部门 
	                			Map beanattrs = BeanUtils.describe(bean);
	 			                //Set cols = beanattrs.keySet();
	 			                Collection c3 = (Collection)depfield.keySet();
	 			                String sql_cols = "";
	 			                String sql_vals = "";
	 			                sql_cols = sql_cols + "departmentcode,";
	 			                sql_vals = sql_vals + "'" + objectGUID + "',";
	 			                for (Iterator iter = (Iterator) c3.iterator(); iter.hasNext();) {
	 			                	String col = (String) iter.next();
	 			                    String val = (String) beanattrs.get(col);
	 			                    if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	 if (Department.class.getDeclaredField(col).getType().getName().equals("java.lang.String")) {
	 			                    		sql_cols = sql_cols + col +","; 
 			                                sql_vals =  sql_vals + "'"+val+"',";
	 			                    	 } else {
	 			                    		sql_cols = sql_cols + col + ","; 
	 			                    		sql_vals = sql_vals + val + ",";
	 			                         }
	 			                    }
	 			                }
	 			                if(sql_vals.endsWith(",")) {
	 			                	sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
		 			                sql_cols = sql_cols.substring(0, sql_cols.length()-1);//删除逗号
	 			                }
	 			                
	 			                
	 			                rs.executeSql("insert into hrmdepartment ("+sql_cols+") values ("+sql_vals+")");
	 			               
								OrgXmlBean orgXmlBean = new OrgXmlBean();
								orgXmlBean.setFullname(orgname); //全称
								orgXmlBean.setShortname(orgname);  //简称
								orgXmlBean.setCode(objectGUID); //部门code
								orgXmlBean.setParent_code(pguid);//上级部门  是分部
								orgXmlBean.setOrg_code(subcomcode); //上级分部
								updateOraddDepartment(orgXmlBean, "add");
	                			//System.out.println("OA里不存在这个部门");
	                		}
	                		writeLog("department:"+orgname+"-----pguid------"+pguid+"-----objectGUID------"+objectGUID+"-----distinguished------"+distinguished+"-----orgtype------"+orgtype);
	                	}
			        } catch (NamingException e) {
			            e.printStackTrace();
			            //System.err.println("Problem listing membership: " + e);
			            writeLog("Problem listing membership: "+ e);
			            
			            return;
			        }
			    }
			}
			writeLog("========================export ["+filter+"] end==========================\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
        	try {
        		if(context != null) {
        			context.close();
        		}
        		
			} catch (NamingException e) {
				e.printStackTrace();
			}
        }
		
    }
    
    
  //获取distinguished的类型(分部1，部门2)
    public String getOrgType(String distinguished){
    	String orgtype = "0";
    	String pv="";
		RecordSet rs = new RecordSet();
		String sql = "select * from  ldapsetdetail where id="+group;
		rs.executeSql(sql);
		
		if(rs.next()) {
			pv = rs.getString("subcompanydomain");
		}
    	writeLog("changeStr(distinguished)="+changeStr(distinguished)+",changeStr(pv)="+changeStr(pv));	
    	if(changeStr(distinguished).indexOf(changeStr(pv))>-1){
    	
		if(changeStr(distinguished).equalsIgnoreCase(changeStr(pv))){
    			orgtype="1";
    	} else {
			orgtype="2";
		}
    	}
    	writeLog("orgtype=="+orgtype);
    	return orgtype;
    }
    
   //获取ad部门的guid
    private static String getGUID(byte[] inArr) {
		StringBuffer guid = new StringBuffer();
		for (int i = 0; i < inArr.length; i++) {
			StringBuffer dblByte = new StringBuffer(Integer.toHexString(inArr[i] & 0xff));
			if (dblByte.length() == 1) {
				guid.append("0");
			}
			guid.append(dblByte);
		}
		return guid.toString();
	}
    
   //获取ad的上级部门
    public String getadPdep(String distinguished, String subcompanycode, String subcompanydoman){
    	String pguid = "-1";
    	RecordSet rs = new RecordSet();
    	String orgdistin = distinguished;
    	distinguished = distinguished.substring(distinguished.indexOf(",")+1,distinguished.length());
    	rs.execute("select * from addepmap where distin = '"+distinguished+"' and orgtype='2'");
    	if(rs.next()){
    		pguid = rs.getString("guid");
    		//String tempdis = distinguished.substring(distinguished.indexOf(",")+1,distinguished.length());
    		//if(tempdis.startsWith("DC") || tempdis.startsWith("dc") ) {
    		//	pguid = "0";
    		//}
    		//判断如果该ou的distinguished与分部的重名  则认为该部门的与分部同名的部门，该部门与同分部下的部门同级
    	}
    	rs.executeSql("select * from addepmap where distin = '"+distinguished+"' and orgtype='1'");
		if(rs.next()) {
			pguid = "0";
		}
		
    	
    	return pguid;
    }
    
    //获得部门所从属的分部
    public String getsubcompanyOfDep(String distinguished, String subcompanycode, String domain) {
    	RecordSet rs = new RecordSet();
    	
    	while(distinguished.indexOf(",") > -1) {
    		distinguished = distinguished.substring(distinguished.indexOf(",")+1,distinguished.length());
    		rs.executeSql("select * from addepmap where orgtype='1' and distin = '" + distinguished + "'");
    		if(rs.next()) {
    			String tempdomain = changeStr(domain);
        		String tempdis = changeStr(distinguished);
        		if(tempdis.equals(tempdomain) ) {
        			
        		} else {
        			subcompanycode = rs.getString("guid");
        		}
    			break;
    		}
    	}
    	return subcompanycode;
    }
    
    //获得上一级分部
    public String getadPsub(String distinguished, String subcompanydoman, String subcompanycode) {
    	String pguid = "0";
    	RecordSet rs = new RecordSet();
    	String orgdistin = distinguished;
    	distinguished = distinguished.substring(distinguished.indexOf(",")+1,distinguished.length());
    	rs.execute("select * from addepmap where distin = '"+distinguished+"' and orgtype='1'");
    	String tempdomain = changeStr(subcompanydoman);
		String tempdis = changeStr(distinguished);
		orgdistin = changeStr(orgdistin);
    	if(rs.next()){
    		pguid = rs.getString("guid");
    		
    		if(tempdis.equals(tempdomain) ) {
    			pguid = subcompanycode;
    		}
    	}
    	rs.execute("select * from addepmap where distin = '"+distinguished+"' and orgtype='2'");
    	if(rs.next()) {
    		pguid = "-1";
    	}
    	
    	if("0".equals(pguid) && !orgdistin.equals(tempdomain)) {
    		pguid = "-1";
    	}
    	
    	return pguid;
    }
    
    //判断账号是否禁用
    public boolean ifAccountControl(String controlValue) {
    	boolean result = true;
    	if(!Util.null2String(controlValue).equals("")){
    	try {
    		int controlIntVal = Integer.valueOf(controlValue);
    		String binaryValue = Integer.toBinaryString(controlIntVal);
    		String stateValue = binaryValue.substring(binaryValue.length()-2);
    		if(stateValue.startsWith("1")) {
    			result = true;
    		}else{
    			result = false;
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		result = false;
    	}
    	}else{
    		result = false;
    	}
    	return result;
    }
    /**
     * 从ldap服务器中导出用户
     * @return
     */
    public boolean testexport(String testUrl1) {
        try {
        	testUrl =testUrl1;
        	test = true;
        	String filter = "";
        	if(!"".equals(ldapcondition))
        	{
        		filter = ldapcondition;
        	}
            if (type.equals("ad"))
            {
            	filter = "".equals(filter)?"(&(objectCategory=person)(objectClass=user))":filter;
            	return exporttest(filter);
            }
            else
            {
            	filter = "".equals(filter)?"objectclass=person":filter;
            	return exporttest(filter);
            }
            
        } catch (Exception e) {
            writeLog(e);
            return false;
        }
    }
    public static void main(String[] args)
    {
    	String test = "(&(objectCategory=person)(objectClass=user)(whenchanged>=123))";
    	
    	int lastindex = test.lastIndexOf(")");
    	String test2 = test.substring(0,lastindex);
    	//System.out.println("test2 : "+test2+" lastindex : "+lastindex+"  "+(lastindex==(test.length()-1)));
    }
     public List exportByTime(String time) {
        try {
        	String filter = "";
        	if(!"".equals(ldapcondition))
        	{
        		filter = ldapcondition;
        	}
			//先判断是否同步组织结构
        /*	if("y".equals(needSynOrg)) {
        		synOrganization(time);
        	
            if (type.equals("ad")) {
            	if("".equals(filter))
            		filter = "(&(objectCategory=person)(objectClass=user))";
            	
            	filter = this.getFilter(filter, "(whenchanged>="+time+")");
            	//return export("(&(objectCategory=person)(objectClass=user)(whenchanged>="+time+"))");
            	return export(filter);
            
			} else{
            	if("".equals(filter))
            		filter = "(&(objectclass=person))";
            	filter = this.getFilter(filter, "(modifytimestamp>="+time+")");
            	//return export("(&(objectclass=person)(modifytimestamp>="+time+"))");
            	
            	return export(filter);
            }
		} else {*/
        		//只同步人员
        		if("".equals(filter))
            		filter = "(&(objectCategory=person)(objectClass=user))";
            	
            	filter = this.getFilter(filter, "(whenchanged>="+time+")");
        		return exportWithoutOrg(filter);
        //	}
        } catch (Exception e) {
            writeLog(e);
            return null;
        }
    }
     /**
      * 获取过滤条件
      * @param filter
      * @param appendFilter
      * @return
      */
    public String getFilter(String filter,String appendFilter)
    {
    	if(!"".equals(filter))
    	{
    		int lastindex = filter.lastIndexOf(")");
    		if(lastindex==(filter.length()-1)&&!"".equals(appendFilter))
    		{
    			String tempfilter = filter.substring(0,lastindex);
    			filter = tempfilter+appendFilter+")";
    		}
    		else
    		{
    			if(filter.indexOf("(&")!=0)
    				filter = "(&"+filter+appendFilter+")";
    			else
    				filter = filter+appendFilter+")";
    		}
    	}
    	return filter;
    }
    public boolean exporttest(String filter) {
         try 
         {
         	String bfDOMAIN = "ldap.domain";
         	if("2".equals(ldapfrom)&&!"".equals(ldaparea))
         	{
         		for(int i = 0;i<ldaparealist.size();i++)
         		{
         			String pv = Util.null2String((String)ldaparealist.get(i));
         			//pv = new String(pv.getBytes("ISO8859-1"));
         			pv = changeStr(pv);
         			pv = buildDomain(pv);
 		        	writeLog("test Ldap load : DOMAIN="+pv);
 		        	if(!Util.null2String(pv).equals("")) 
 		        	{
 		        		DOMAIN = ""+i;
 		        		try 
 		        		{
 		        			if(!exporttest(pv, filter))
 		        			{
 		        				return false;
 		        			}
 		        			
 		        			String msg=fieldTest(pv);
 		        			if(!msg.equals("")){
	 		        			if(msg.lastIndexOf("bucunzai")>-1){
	 		        				errormsg =msg;
	 		        				return false;
	 		        			}
 		        			}
 		        		}
 		        		catch(Exception e) {
 		        			writeLog("test Ldap load error: DOMAIN="+pv);
 		        			if(e.toString().indexOf("NamingException: Cannot parse url")>-1||e.toString().indexOf("javax.naming.CommunicationException")>-1)
 		        			{
 		        				errormsg = "1";
 		        			}
 		        			else if(e.toString().indexOf("javax.naming.AuthenticationException")>-1)
 		        			{
 		        				errormsg = "2";
 		        			}
 		        			else if(e.toString().indexOf("java.lang.ClassNotFoundException")>-1||e.toString().indexOf("javax.naming.NoInitialContextException")>-1)
 		        			{
 		        				errormsg = "3";
 		        			}
 		        			e.printStackTrace();
 		        			return false;
 		        		}
 		        	}
 	        	}
         		
         	
         		if("y".equals(needSynOrg)) {
					String msg=checkSubCompanyDoMain();
					if(!msg.equals("")){
							if(msg.lastIndexOf("bucunzai")>-1){
								errormsg =msg;
								return false;
							}
					}
				}
         	}
         } catch (Exception e) {
             writeLog(e);
             return false;
         }
         return true;
     }
    public List export(String filter) {
    	if(!"y".equals(needSynPerson)) {//判断同步人员的开关
    		return new ArrayList();
    	}
        try {
        	String bfDOMAIN = "ldap.domain";
        	List l = new ArrayList();
        	if("1".equals(ldapfrom))
        	{
	        	for(int i=0; i<1000000; i++) {
	        		String sp = "";
	        		if(i>0)
	        			sp = "."+i;
	        		String rnDOMAIN = bfDOMAIN+sp;
	        		List l0 = new ArrayList();
		        	String pv = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), rnDOMAIN)).getBytes("ISO8859-1"));
		        	pv = changeStr(pv);
		        	if(!Util.null2String(pv).equals("")) {
		        		DOMAIN = rnDOMAIN;
		        		try {
		        			l0 = export(pv, filter);
		        			if(l0!=null && l0.size()>0){
		        				l.addAll(l0);
		        			}
		        		}
		        		catch(Exception e) {
		        			writeLog("Ldap load error: DOMAIN="+pv);
		        			writeLog("Ldap load error: "+e.getMessage());
		        			e.printStackTrace();
		        		}
		        	}
		        	else {
		        		break;
		        	}
	        	}
        	}
        else if("2".equals(ldapfrom))//get ldap config from database
        	{
        	RecordSet rs = new RecordSet();
        		RecordSet rs2 = new RecordSet();
        		String sql = "select * from ldapsetdetail order by id";
        		rs.executeSql(sql);
        		while(rs.next()) {
        			String pv = Util.null2String(rs.getString("subcompanydomain"));
        			if(pv == null || "".equals(pv)) {
        				continue;
        			}
        			pv = changeStr(pv);
        			pv = buildDomain(pv);
					subcompanycode = rs.getString("subcompanycode");
        			
	        		List l0 = new ArrayList();
		        	writeLog("Ldap load : DOMAIN="+pv);
		        	if(!Util.null2String(pv).equals("")) {
		        		//DOMAIN = ""+i;
		        		try {
		        			l0 = export(pv, filter);
		        			if(l0!=null && l0.size()>0){
		        				l.addAll(l0);
		        			}
		        		}
		        		catch(Exception e) {
		        			writeLog("Ldap load error: DOMAIN="+pv);
		        			writeLog("Ldap load error: "+e.getMessage());
		        			e.printStackTrace();
		        		}
		        	}
		        	else {
		        		break;
		        	}
	        	}
			
        	}

        	updateHrmResourceTemp();
        	
        	subCompanyComInfo = new SubCompanyComInfo();
        	subCompanyComInfo.removeCompanyCache();
            DepartmentComInfo dcInfo = new DepartmentComInfo();
    		dcInfo.removeCompanyCache();
    		ResourceComInfo rci=new ResourceComInfo();
    		rci.removeResourceCache();
           
            return l;
        } catch (Exception e) {
            writeLog(e);
            return null;
        }
        
        

    }

    public boolean exporttest(String domain, String filter) throws Exception {
    	domain = changeStr(domain);
    	boolean test = false;
    	writeLog("========================exporttest ["+domain+"] begin==========================\n");
        InitialDirContext context = (InitialDirContext) getInitialContext();
        if(context!=null){
        	test = true;
        }
       /*  if("1".equals(isUac))
        {
	        if(uacValue.equals("")){
	        	uacValue = "514,66050";
	        }
        }*/
        /*SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        String domain_new = buildDomain(domain);//处理domain中的特殊字符
        NamingEnumeration enumeration = context.search(domain_new,filter,sc);
        
        if (enumeration.hasMoreElements()) {
        	test = true;
        }*/
        context.close();
        writeLog("========================exporttest ["+domain+"] end==========================\n");
        return test;
    }
    
    
    public String fieldTest(String domain) throws Exception { //检测域路径是否存在正确
    	 String msg="";
    	 try{
    		 if(testUrl.indexOf("636") <0) { 
		         InitialDirContext initialContext = (InitialDirContext) getInitialContext();
		         SearchControls sc = new SearchControls();
		         sc.setSearchScope(2);
		         domain = changeStr(domain);
		         domain = buildDomain(domain);
		         
		         String[] arr_baseDN = Util.TokenizerString2(domain, ",");
		         String baseDN = "";
		         String ouStr="";
		         for (int i = 0; i < arr_baseDN.length; i++) {
		         	if (arr_baseDN[i].indexOf("dc=") < 0){
		         		ouStr+=arr_baseDN[i]+",";
		         	}else{
		                 if (!baseDN.equals("")){
		                     baseDN = baseDN+"," + arr_baseDN[i];
		                 }else{
		                     baseDN += arr_baseDN[i];
		                 }
		         	}
		         }
		         baseDN = buildDomain(baseDN);//处理domain中的特殊字符
		         
		         if(!ouStr.equals("")){
		             String[] ouArray= Util.TokenizerString2(ouStr, ",");
		             String oupath="";
		             for(int i=ouArray.length-1;i>=0;i--){
		            	 String ou=ouArray[i].replaceAll("ou=", "");
		            	 oupath=ouArray[i]+","+oupath;
		            	 try{
		            		 NamingEnumeration iter = initialContext.search(oupath+baseDN, "(ou=" + ou + ")", sc);
			                 msg+=ou+"cunzai;";
		            	 }catch(Exception ex){
		            		 msg+=ou+"bucunzai;";
		            		 break;
		            	 }
			             
		             }
		         }
		         
		         initialContext.close();
    		 }
    	 }catch(Exception ex){
    		 msg="bucunzai";
    	 }
         return msg;
        
   }
    
    public String checkSubCompanyDoMain(){//检查分部域是否正确
    	
    	String msg="";
    	
    	try {
			RecordSet rs = new RecordSet();
			String sql = "select * from ldapsetdetail order by id";
			rs.executeSql(sql);
			List<String> list=new ArrayList<String>();
			while(rs.next()) {
				String pv = Util.null2String(rs.getString("subcompanydomain"));
				list.add(pv);
			}
			
			InitialDirContext initialContext = (InitialDirContext) getInitialContext();
			SearchControls sc = new SearchControls();
			sc.setSearchScope(2);
			
			for (String subDoMain : list) {
				if(!subDoMain.equals("")){
					subDoMain = changeStr(subDoMain);
					subDoMain = buildDomain(subDoMain);
			        String ou= subDoMain.substring(0,subDoMain.indexOf(","));
			        ou=ou.replaceAll("ou=", "");
			        try{
						 NamingEnumeration iter = initialContext.search(subDoMain, "(ou=" + ou + ")", sc);
				         msg+=subDoMain+"cunzai;";
					}catch(Exception ex){
						 msg+=subDoMain+"bucunzai;";
					}
				             
			   }
			         
			         
				
			}
			
			return msg;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "bucunzai";
		}
    	
    	
    }
    
    
    
    public List export(String domain, String filter) throws Exception {
    	domain = changeStr(domain);
		ad2oa = true;
    	tempDomain = domain;
        InitialDirContext context = (InitialDirContext) getInitialContext();
        
        SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        String domain_new = buildDomain(domain);//处理domain中的特殊字符
        NamingEnumeration enumeration = context.search(domain_new,filter,sc);
        writeLog("========================export ["+domain+"] begin==========================\n");
        HrmService service = new HrmService();
        service.setExp_result(new ArrayList());
        
        while (enumeration.hasMoreElements()) {
            LN l = new LN();
            if (l.CkHrmnum() >= 0) {  //reach the max hrm number
               break;
            }
            SearchResult result = (SearchResult) enumeration.nextElement();
            Attributes attributes = result.getAttributes();
            NamingEnumeration ne = attributes.getIDs();
            String distinguishedName=(String)attributes.get("distinguishedName").get();
            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
            Enumeration keys = prop.keys();
            User u = new User();

			boolean userAccountflag = true;
            int tmp=0;
            
            if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
            	userAccountflag = false;
            	//break;
            }
            
            if(Util.null2String(prop.getProperty("status")).equals("")){//界面状态值未配置默认为空
				u.setStatus(-1);
			}
            String subcompanyname=""; 
            String departmentname=""; 
            String jobtitlename=""; 
            String managername="";
            while (keys.hasMoreElements()) {
            	tmp++;
                String col = (String) keys.nextElement();
                String attr = prop.getProperty(col);
                
                if (attr.indexOf("$") == 0) { //use the value from ldap
                    attr = attr.substring(1);
                    
                    if (attr.equalsIgnoreCase("userpassword"))   //userpassword is a forbidden attribute
                        continue;
                    Attribute att=null;
                    try{
                    att=attributes.get(attr);
                    }catch(Exception e){
                        //do nothing
                    }
                    if(att==null)
                    continue;
                    String val = (String) att.get();
                    
                    if (val == null)
                        continue;
					if(col.equalsIgnoreCase("subcompanyid1")){    //分部
						subcompanyname=val;
						writeLog("得到分部:名称，"+subcompanyname);
					}
					if(col.equalsIgnoreCase("departmentid")){    //部门
						departmentname=val;
						val="0";
						writeLog("得到部门:名称，"+departmentname);
					}
					if(col.equalsIgnoreCase("jobtitle")){    //岗位
						jobtitlename=val;
						val="0";
						writeLog("得到岗位:名称，"+jobtitlename);
					}
					if(col.equalsIgnoreCase("managerid")){    //上级
						managername=val;
						val="0";
						writeLog("得到直接上级:"+managername);
					}
					if(col.equalsIgnoreCase("seclevel")){
						writeLog("安全级别值:"+val);
					}
                    BeanUtils.setProperty(u, col, val);
                } else { //use defaulst value from ldap.properties
        			 	
					if(col.equalsIgnoreCase("subcompanyid1")){    //分部
						subcompanyname=attr;
					}
					if(col.equalsIgnoreCase("departmentid")){    //部门
						departmentname=attr;
						attr="0";
					}
					if(col.equalsIgnoreCase("jobtitle")){    //岗位
						jobtitlename=attr;
						attr="0";
						writeLog("得到岗位:名称，"+attr);
					}
					if(col.equalsIgnoreCase("managerid")){    //上级
						managername=attr;
						attr="0";
						writeLog("得到直接上级:"+attr);
					}
                	this.writeLog("col : "+col+" attr : "+attr);
                    BeanUtils.setProperty(u, col, attr);
                }
            }
            String department = getDepid(distinguishedName,domain_new);
			BeanUtils.setProperty(u, "departmentid",department);//通过distinguishedName获取部门的值,由ad组织结构决定
			writeLog("userAccountflag:"+userAccountflag+",name:"+u.getLastname()+",accout:"+u.getAccount()+",status:"+u.getStatus());
			if("y".equals(needSynPerson)) {//判断同步人员的开关状态
            	 if(userAccountflag && !"".equals(department)) {
                     u.setLdapId(ldapId);
                     u.setLdap_domainName(ldaplogin);
					service.addUser(u,departmentname,jobtitlename,subcompanyname,managername);
     			}else{
     				String whenchanged = "";
     				//做人员离职操作ADD by liudl
     				if(this.type.equals("ad")){
     					whenchanged = attributes.get("whenchanged").get()==null?"":(String)attributes.get("whenchanged").get();
     					whenchanged = whenchanged.substring(0, 8);
     				}else{
     					whenchanged = attributes.get("whenchanged").get()==null?"":(String)attributes.get("whenchanged").get();
     				}
//     				PersonDisMissUtil util = new PersonDisMissUtil();
//     				util.synDismiss(u.getLoginid(), whenchanged);
     				service.synDismiss(u, whenchanged);
     				//writeLog("禁用AD帐号(不同步)：Loginid="+Util.null2String(u.getLoginid())+", lastname="+Util.null2String(u.getLastname()));
     			}
            }
        }
        
        writeLog("========================export ["+domain+"] end==========================\n");
       
        context.close();
        return service.getExp_result();
    }

	public String getDepid(String str, String domain){
    	String depid = "";
    	RecordSet rs = new RecordSet();
    	try {
			String pv = new String(domain);
			if(changeStr(str).indexOf(changeStr(pv))>-1){
				str = str.substring(str.indexOf(",")+1);
				rs.executeSql("select * from addepmap where orgtype='1' and lower(distin) = '"+str+"'");
				
				if(rs.next()) {//分部下的人员，创建新的部门
						//String depCode=(String)hmSubCompanyInfo.get(changeStr(str));
						rs.getString("distin");
						String depCode = "";
						//从ad域取得分部编号
						SearchControls sc = new SearchControls();
						sc.setSearchScope(SearchControls.OBJECT_SCOPE);
						InitialDirContext dircontext = (InitialDirContext) getInitialContext();
						String filter = "(&(objectCategory=organizationalUnit)" +
									"(objectClass=organizationalUnit)(ou=*))";
						NamingEnumeration answer = dircontext.search(str,filter,sc);
						if(answer.hasMore()) {
							SearchResult result = (SearchResult) answer.nextElement();
							Attributes attributes = result.getAttributes();
							String objectGUID = (String)attributes.get("objectGUID").get();
							String distinguished = (String)attributes.get("distinguishedName").get();
							String subcompanycode = getGUID(objectGUID.getBytes());
					        depCode = subcompanycode + "1";//加“1”防止新建部门存在上下级关系

					        //判断人员所在ou是分部还是部门
					        String orgtype = "";
					        
					        if(!"".equals(ouattr)) {
					        	if(attributes.get(ouattr) != null) {
					        		String orgtypeval = (String)attributes.get(ouattr).get();
				            		if(orgtypeval!=null && orgtypeval.equals(subcompangyval)) {
					            		orgtype = "1";//分部
					            	} else if(orgtypeval!=null && orgtypeval.equals(departmentval)) {
					            		orgtype = "2";//分部
					            	}
					        	} else {
					        		orgtype = "";
					        	}
			            		
			            	}
					        if("".equals(orgtype)) {
					        	orgtype = getOrgType(distinguished);
					        }
					        //如果是部门  直接返回部门id
					        if("2".equals(orgtype)) {
					        	rs.execute("select t1.id from hrmdepartment t1, addepmap t2 where t1.departmentcode = t2.guid and t2.distin = '"+str+"'");
								if(rs.next()){
									depid = rs.getString(1);
								}
								return depid;
					        } else {
					        	rs.execute("select id from hrmdepartment where departmentcode = '"+depCode+"'");
						        if(rs.next()) {
						        	depid = rs.getString("id");
						        } else {
						        	Department bean = new Department();
						            Collection c = (Collection)depfield.keySet();
						            Iterator ite = (Iterator) c.iterator();
						          
						            setBean(ite, attributes, bean);
						            Map beanattrs = BeanUtils.describe(bean);
			 			            Collection c3 = (Collection)depfield.keySet();
			 			            String insertSql = getInsertDepSql(c3, beanattrs, depCode);
			 			            rs.executeSql(insertSql);
			 			            
			 			           String departmentname = str.substring(0,str.indexOf(","));
									departmentname = departmentname.substring(str.indexOf("=")+1);
									String supdepid = "0";//上一级部门是分部
									rs.execute("select id from hrmdepartment where departmentcode = '"+depCode+"'");
									if(rs.next()) {
										//更新部门的必要信息
										OrgXmlBean orgXmlBean = new OrgXmlBean();
										orgXmlBean.setFullname(departmentname); //全称
										orgXmlBean.setShortname(departmentname);  //简称
										orgXmlBean.setCode(depCode); //部门code
										orgXmlBean.setParent_code(supdepid);//上级部门  是分部
										orgXmlBean.setOrg_code(subcompanycode); //上级分部
										updateOraddDepartment(orgXmlBean, "add");
									}
									//deparmentcodelist.add(depCode);
									//往addepmap插入部门信息
									rs.execute("select * from addepmap where guid = '"+depCode+"' and orgtype='2'");
									if(!rs.next()) {
										rs.execute("insert into addepmap(dep,pguid,distin,guid,subcomcode,orgtype,status) values ('"
												+departmentname+"','"+supdepid+"','"+str+"','"+depCode+"','"+subcompanycode+"','"+2+"','0')");
									}
									
									rs.execute("select id from hrmdepartment where departmentcode = '"+depCode+"'");
									if(rs.next()){
										depid = rs.getString(1);
									}
						        }
						        
					        }
					        
					        
	 			            /*String sql_cols = "";
	 			            String sql_vals = "";
	 			            sql_cols = sql_cols + "departmentcode,";
	 			            sql_vals = sql_vals + "'" + depCode + "',";
	 			            for (Iterator iter = (Iterator) c3.iterator(); iter.hasNext();) {
	 			                String col = (String) iter.next();
	 			                String val = (String) beanattrs.get(col);
	 			                if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
	 			                    	if (Department.class.getDeclaredField(col).getType().getName().equals("java.lang.String")) {
	 			                    	sql_cols = sql_cols + col +","; 
 			                            sql_vals =  sql_vals + "'"+val+"',";
	 			                    } else {
	 			                    	sql_cols = sql_cols + col + ","; 
	 			                    	sql_vals = sql_vals + val + ",";
	 			                    }
	 			                }
	 			            }
 			                if(sql_vals.endsWith(",")) {
 			                	sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
	 			                sql_cols = sql_cols.substring(0, sql_cols.length()-1);//删除逗号
 			                }*/
	 			            //rs.executeSql("insert into hrmdepartment ("+sql_cols+") values ("+sql_vals+")");
						}
						

				} else {
					rs.execute("select t1.id from hrmdepartment t1, addepmap t2 where t1.departmentcode = t2.guid and t2.distin = '"+str+"'");
					if(rs.next()){
						depid = rs.getString(1);
					}
				}
			}
			
			try {
				
				ResourceComInfo rci = new ResourceComInfo();
				rci.removeResourceCache();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
    	return depid;
    }
    /**
     * get accounts from ldap
     *
     * @param filter
     * @return account list
     * @throws Exception
     */
    public List getAccounts(String filter) throws Exception {
		//获取配置的禁用状态值
    	
        InitialDirContext context = (InitialDirContext) getInitialContext();
        Enumeration keys = prop.keys();
        String fieldname = "account";
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.equals("account")) {
                fieldname = prop.getProperty(key);
                fieldname = fieldname.substring(1);
                break;
            }
        }
        if (!filter.equals("*"))
            filter = "*" + filter + "*";
        filter = fieldname + "=" + filter;
        //if (type.equals("ad"))
      	//filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";
        //  else
      	//filter = "(&(objectCategory=person)("+filter+"))";
        if (type.equalsIgnoreCase("ad")){
          	filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";        	
        }else if (type.equalsIgnoreCase("OpenLDAP")){
          	        	
        }else {
        	filter = "(&(objectCategory=person)("+filter+"))";
        }
      	
        SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        ArrayList list = new ArrayList();
        String bfDOMAIN = "ldap.domain";
        if("1".equals(ldapfrom))
    	{
	    	for(int i=0; i<1000000; i++) {
	    		String sp = "";
	    		if(i>0)
	    			sp = "."+i;
	    		String rnDOMAIN = bfDOMAIN+sp;
	        	String pv = Prop.getPropValue(GCONST.getConfigFile(), rnDOMAIN);
	        	pv = changeStr(pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = rnDOMAIN;
	        		try {
	        			NamingEnumeration iter = context.search(new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1")), filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
							
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
	                    	if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
	                    	if(!list.contains(account)){
								list.add(account);
	                    	}
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
	        	else {
	        		break;
	        	}
	    	}
    	}
        else if("2".equals(ldapfrom)&&!"y".equals(needSynPassword)&&!"".equals(ldaparea)) {
    		for(int i = 0;i<ldaparealist.size();i++)
    		{
    			String pv = Util.null2String((String)ldaparealist.get(i));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
							
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
	                    	if(!list.contains(account)){
								list.add(account);
	                    	}
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	} else {
	        		break;
	        	}
        	}
    	} else if("2".equals(ldapfrom)&&"y".equals(needSynPassword)) {
    		String sql = "select * from ldapsetdetail order by id";
    		RecordSet rs = new RecordSet();
    		rs.executeSql(sql);
    		while(rs.next()) {
    			String pv = Util.null2String(rs.getString("subcompanydomain"));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		//DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
							
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
	                    	if(!list.contains(account)){
								list.add(account);
	                    	}
	        	        }
	        		}
	        		catch(Exception e) {
	        			e.printStackTrace();
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
        	}
    	}
        context.close();
        Collections.sort(list, new Comparator() {
            public int compare(Object o, Object o1) {
                return ((String) o).compareTo((String) o1);  //To change body of implemented methods use File | Settings | File Templates.
            }
        });
        return list;
    }
    
    public String getAccoutsJson(String filter) throws Exception{
    	//filter = "*";
    	JSONArray data = new JSONArray();
    	
    	InitialDirContext context = (InitialDirContext) getInitialContext();
        Enumeration keys = prop.keys();
        String fieldname = "account";
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.equals("account")) {
                fieldname = prop.getProperty(key);
                fieldname = fieldname.substring(1);
                break;
            }
        }
        if (!filter.equals("*"))
            filter = "*" + filter + "*";
        filter = fieldname + "=" + filter;
       
        //if (type.equals("ad"))
      	//filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";
        //  else
      	//filter = "(&(objectCategory=person)("+filter+"))";
        if (type.equalsIgnoreCase("ad")){
          	filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";        	
        }else if (type.equalsIgnoreCase("OpenLDAP")){
          	        	
        }else {
        	filter = "(&(objectCategory=person)("+filter+"))";
        }
      	
        SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        ArrayList list = new ArrayList();
        String bfDOMAIN = "ldap.domain";
        if("1".equals(ldapfrom))
    	{
	    	for(int i=0; i<1000000; i++) {
	    		String sp = "";
	    		if(i>0)
	    			sp = "."+i;
	    		String rnDOMAIN = bfDOMAIN+sp;
	        	String pv = Prop.getPropValue(GCONST.getConfigFile(), rnDOMAIN);
	        	pv = changeStr(pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = rnDOMAIN;
	        		try {
	        			NamingEnumeration iter = context.search(new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1")), filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
							
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
	                    	if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
	                    	JSONObject json = new JSONObject();
	                    	json.put("id", account);
	                    	json.put("name", account);
	                    	data.add(json);
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
	        	else {
	        		break;
	        	}
	    	}
    	}
        else if("2".equals(ldapfrom)&&!"y".equals(needSynPassword)&&!"".equals(ldaparea)) {
    		for(int i = 0;i<ldaparealist.size();i++)
    		{
    			String pv = Util.null2String((String)ldaparealist.get(i));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
	        	            
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
							JSONObject json = new JSONObject();
							json.put("id", account);
	                    	json.put("name", account);
	                    	data.add(json);
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	} else {
	        		break;
	        	}
        	}
    	} else if("2".equals(ldapfrom)&&"y".equals(needSynPassword)) {
    		String sql = "select * from ldapsetdetail order by id";
    		RecordSet rs = new RecordSet();
    		rs.executeSql(sql);
    		while(rs.next()) {
    			String pv = Util.null2String(rs.getString("subcompanydomain"));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		//DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
	        	            
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
							JSONObject json = new JSONObject();
							json.put("id", account);
	                    	json.put("name", account);
	                    	data.add(json);
	        	        }
	        		}
	        		catch(Exception e) {
	        			e.printStackTrace();
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
        	}
    	}
        context.close();
        
        return  data.toString();
    }
    

    public List<Map<String, String>> getAccoutList() throws Exception {
    	String filter = "*";
    	List<Map<String, String>> data = new ArrayList<Map<String, String>>();
    	
    	InitialDirContext context = (InitialDirContext) getInitialContext();
        Enumeration keys = prop.keys();
        String fieldname = "account";
        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.equals("account")) {
                fieldname = prop.getProperty(key);
                fieldname = fieldname.substring(1);
                break;
            }
        }
        if (!filter.equals("*"))
            filter = "*" + filter + "*";
        filter = fieldname + "=" + filter;
       
        //if (type.equals("ad"))
      	//filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";
        //  else
      	//filter = "(&(objectCategory=person)("+filter+"))";
        if (type.equalsIgnoreCase("ad")){
          	filter = "(&(objectCategory=person)(objectClass=user)("+filter+"))";        	
        }else if (type.equalsIgnoreCase("OpenLDAP")){
          	        	
        }else {
        	filter = "(&(objectCategory=person)("+filter+"))";
        }
      	
        SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        ArrayList list = new ArrayList();
        String bfDOMAIN = "ldap.domain";
        if("1".equals(ldapfrom))
    	{
	    	for(int i=0; i<1000000; i++) {
	    		String sp = "";
	    		if(i>0)
	    			sp = "."+i;
	    		String rnDOMAIN = bfDOMAIN+sp;
	        	String pv = Prop.getPropValue(GCONST.getConfigFile(), rnDOMAIN);
	        	pv = changeStr(pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = rnDOMAIN;
	        		try {
	        			NamingEnumeration iter = context.search(new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1")), filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
							
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
	                    	if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
	                    	Map newMap = new HashMap();
	                    	newMap.put("displayName", displayName);
	                    	newMap.put("account", account);
	                    	data.add(newMap);
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
	        	else {
	        		break;
	        	}
	    	}
    	}
        else if("2".equals(ldapfrom)&&!"y".equals(needSynPassword)&&!"".equals(ldaparea)) {
    		for(int i = 0;i<ldaparealist.size();i++)
    		{
    			String pv = Util.null2String((String)ldaparealist.get(i));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
	        	            
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
							Map newMap = new HashMap();
							newMap.put("displayName", displayName);
	                    	newMap.put("account", account);
	                    	data.add(newMap);
	        	        }
	        		}
	        		catch(Exception e) {
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	} else {
	        		break;
	        	}
        	}
    	} else if("2".equals(ldapfrom)&&"y".equals(needSynPassword)) {
    		String sql = "select * from ldapsetdetail order by id";
    		RecordSet rs = new RecordSet();
    		rs.executeSql(sql);
    		while(rs.next()) {
    			String pv = Util.null2String(rs.getString("subcompanydomain"));
    			//pv = new String(pv.getBytes("ISO8859-1"));
    			pv = changeStr(pv);
    			pv = buildDomain(pv);
        		List l0 = new ArrayList();
	        	writeLog("Ldap load : DOMAIN="+pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		//DOMAIN = ""+i;
	        		try {
	        			NamingEnumeration iter = context.search(pv, filter, sc);
	        	        while (iter.hasMoreElements()) {
	        	            SearchResult result = (SearchResult) iter.nextElement();
	        	            Attributes attributes = result.getAttributes();
	        	            String account = (String) attributes.get(fieldname).get();
	        	            String displayName = (String) attributes.get("displayName").get();
	        	            
	        	            String userAccountControl = this.type.equals("ad")?(String)attributes.get("userAccountControl").get():"";
							if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
	                    		continue;
	                    	}
							Map newMap = new HashMap();
							newMap.put("displayName", displayName);
	                    	newMap.put("account", account);
	                    	data.add(newMap);
	        	        }
	        		}
	        		catch(Exception e) {
	        			e.printStackTrace();
	        			writeLog("Ldap getAccounts error: DOMAIN="+DOMAIN);
	        		}
	        	}
        	}
    	}
        context.close();
        return  data;
    }
    
    /**
     * ldap login verify
     *
     * @param account
     * @param password
     * @return success or not
     */
    public boolean authentic(String account, String password) {
    	
    	boolean r=this.authentic2(account, password);
    	if(!r){
    		int a=999999999;
    		writeLog("用户密码校验失败，account："+account+",password,"+password+"ldap"+ldapId);
    		
    	}
    	return r;
    }
    /**
     * ldap login verify
     *
     * @param account
     * @param password
     * @return success or not
     */
    public boolean authentic2(String account, String password) {
    	if(account.indexOf("\\")!=-1){
      	  account=account.substring(account.indexOf("\\")+1);
      	}
    	boolean checkStatus = false;
    	String bfDOMAIN = "ldap.domain";
    	if("1".equals(ldapfrom))
    	{
	    	for(int i=0; i<1000000; i++) {
	    		String sp = "";
	    		if(i>0)
	    			sp = "."+i;
	    		String rnDOMAIN = bfDOMAIN+sp;
	        	String pv = Prop.getPropValue(GCONST.getConfigFile(), rnDOMAIN);
	        	pv = changeStr(pv);
	        	if(!Util.null2String(pv).equals("")) {
	        		DOMAIN = rnDOMAIN;
	        		if (type.equals("ad"))
	        			checkStatus = authentic4AD(account,password);
	        		        else
	        		      checkStatus = authentic4iPlanet(account,password);
	        		if(checkStatus) break;
	        	}
	        	else {
	        		break;
	        	}
	    	}
    	}
    	else if("2".equals(ldapfrom)) {
    		if(!"y".equals(needSynOrg) && !"".equals(ldaparea)) {
    			for(int i = 0;i<ldaparealist.size();i++)
        		{
        			String pv = Util.null2String((String)ldaparealist.get(i));
        			pv = changeStr(pv);
        			pv = buildDomain(pv);
    	        	writeLog("Ldap load : DOMAIN="+pv);
    	        	if(!Util.null2String(pv).equals("")) {
    	        		DOMAIN = ""+i;
    	        		if (type.equals("ad"))
    	        			checkStatus = authentic4AD(account,password);
        		        else
        		        	checkStatus = authentic4iPlanet(account,password);
    	        		if(checkStatus) break;
    	        	}
    	        	else {
    	        		break;
    	        	}
        		}
    		} else {
    			try{
    				boolean isADAccount  = false;
    				RecordSet rs1 = new RecordSet();
    				rs1.executeSql("select * from ldapsetdetail where 1=1");
    				
    	            SearchControls sc = new SearchControls();
    	            sc.setSearchScope(2);
    				while(rs1.next()) {
    					String domain = rs1.getString("subcompanydomain");
    					domain = changeStr(domain);
    			        domain = buildDomain(domain);
    			        tempDomain = domain;
    			        InitialDirContext initialContext = (InitialDirContext) getInitialContext();
    			        
    		            NamingEnumeration iter = initialContext.search(domain, "(&(samaccountname=" + account + ")(objectclass=user))", sc);
    		            if (iter.hasMoreElements()) {
    		                SearchResult result = (SearchResult) iter.nextElement();
    		                Attributes attributes = result.getAttributes();
    		                Attribute att = attributes.get("distinguishedname");
    		                String dn = (String) att.get();
    		                Hashtable env = new Hashtable();
    		                env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
    		                env.put(Context.PROVIDER_URL, ldapserverurl);
    		                env.put(Context.SECURITY_PRINCIPAL, dn);
    		                env.put(Context.SECURITY_CREDENTIALS, password);
    		                env.put( Context.REFERRAL, "follow" );
    	
    		                InitialDirContext initialContext1 = new InitialDirContext(env);
    		                //initialContext1.search(dn, null);
    		                initialContext1.close();
    		                isADAccount  = true;
    		                break;
    	
    		            } else {
    		            initialContext.close();
    		            continue;
    		            }
    		           
    				}
    				return isADAccount;
    			}catch (Exception e) {
    				e.printStackTrace();
    				return false;
    			}
    		}
    	}
    	

		
	
//      if (type.equals("ad"))
//      return checkStatus;//authentic4AD(account,password);
//        else
//      return authentic4iPlanet(account,password);
    	return checkStatus;
    }

    private InitialContext getInitialContext() throws Exception {
    	
        Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
        //测试的时候，根据不同的人员，
        if(test) {
        	env.put(Context.PROVIDER_URL, testUrl);
        	if(testUrl.indexOf("636") > 0) {
        		//为636传证书
        		env.put(Context.SECURITY_PROTOCOL, "ssl");
        		env.put(Context.SECURITY_AUTHENTICATION, "simple");
        		System.setProperty("javax.net.ssl.trustStore", keystorepath); 
        		System.setProperty("javax.net.ssl.trustStorePassword", keystorepassword);
        		env.put("java.naming.ldap.factory.socket", "weaver.ldap.DummySSLSocketFactory");
        	
        	}
        } else {
        	env.put(Context.PROVIDER_URL, ldapserverurl);
        }
        
        env.put( Context.REFERRAL, "follow" );        
       
        String templdapuser = buildPrincipal(ldapuser);//处理账号中的特殊字符
        if (type.equals("ad")) {
        	String domain = "";
        	if("1".equals(ldapfrom))
        	{
        		domain = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1"));
            	domain = changeStr(domain);
        	}
        	else if("2".equals(ldapfrom)&&!"".equals(ldaparea))
        	{
				if(ad2oa) {//如果是ad同步到oa去的话
        			domain = tempDomain;
        			ad2oa = false;
        		} else {
        			domain = new String(Util.null2String((String)ldaparealist.get(Util.getIntValue(DOMAIN,0))));
				}
        	}
        	domain = changeStr(domain);
        	domain = buildDomain(domain);
            String[] arr_baseDN = Util.TokenizerString2(domain, ",");
            String baseDN = "";
            for (int i = 0; i < arr_baseDN.length; i++) {
            	if (arr_baseDN[i].indexOf("dc=") < 0)
                    continue;
                if (!baseDN.equals(""))
                    baseDN = baseDN+"," + arr_baseDN[i];
                else
                    baseDN += arr_baseDN[i];
            }
            baseDN = buildDomain(baseDN);//处理domain中的特殊字符
            if(templdapuser.toUpperCase().indexOf("CN=")<0&&templdapuser.toUpperCase().indexOf(",OU=")<0&&templdapuser.toUpperCase().indexOf(",DC=")<0){  
            	env.put(Context.SECURITY_PRINCIPAL, "cn=" + templdapuser + ",cn=users," + baseDN);
            }
            else{
            	 env.put(Context.SECURITY_PRINCIPAL, templdapuser);
            }
        } else
            env.put(Context.SECURITY_PRINCIPAL, templdapuser);
        env.put(Context.SECURITY_CREDENTIALS, ldappasswd);
       
        InitialDirContext initialContext = new InitialDirContext(env);
        return initialContext;
    }

   public boolean authentic4AD(String account, String password) {
        try {
	    	if(account.indexOf("\\")!=-1){
	      	  account=account.substring(account.indexOf("\\")+1);
	      	}
            InitialDirContext initialContext = (InitialDirContext) getInitialContext();
            SearchControls sc = new SearchControls();
            sc.setSearchScope(2);
            String domain = "";
        	if("1".equals(ldapfrom))
        	{
        		domain = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1"));
        	}
        	else if("2".equals(ldapfrom)&&!"".equals(ldaparea))
        	{
        		domain = new String(Util.null2String((String)ldaparealist.get(Util.getIntValue(DOMAIN,0))));
        	}
            domain = changeStr(domain);
            domain = buildDomain(domain);
            /*String[] arr_baseDN = Util.TokenizerString2(domain, ",");
            String baseDN = "";
            for (int i = 0; i < arr_baseDN.length; i++) {
                if(arr_baseDN[i].indexOf("dc")<0)
                continue;
                if (!baseDN.equals(""))
                    baseDN = "." + baseDN + arr_baseDN[i].substring(arr_baseDN[i].indexOf("=") + 1);
                else
                    baseDN += baseDN + arr_baseDN[i];
            }
            String userprincipalname = account + "@" + baseDN;*/
            NamingEnumeration iter = initialContext.search(domain, "(&(samaccountname=" + account + ")(objectclass=user))", sc);
            if (iter.hasMoreElements()) {
                SearchResult result = (SearchResult) iter.nextElement();
                Attributes attributes = result.getAttributes();
                Attribute att = attributes.get("distinguishedname");
                String dn = (String) att.get();
                Hashtable env = new Hashtable();
                env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
                env.put(Context.PROVIDER_URL, ldapserverurl);
                env.put(Context.SECURITY_PRINCIPAL, dn);
                env.put(Context.SECURITY_CREDENTIALS, password);
                env.put( Context.REFERRAL, "follow" );
                System.out.println(JSON.toJSONString(env));
                
                InitialDirContext initialContext1 = new InitialDirContext(env);
                //initialContext1.search(dn, null);
                initialContext1.close();

            }else{
            initialContext.close();
            return false;
            }
            initialContext.close();
            return true;
        } catch (Exception e) {
        	e.printStackTrace();
            return false;
        }
    }
   
   public String authenticuser(String account) {
	try {
    	if(account.indexOf("\\")!=-1){
      	  account=account.substring(account.indexOf("\\")+1);
      	}
		if(!"y".equals(needSynOrg)) {
			for(int i = 0;i<ldaparealist.size();i++) {
				String pv = Util.null2String((String)ldaparealist.get(i));
				pv = changeStr(pv);
				pv = buildDomain(pv);
				String domain  = "";
				writeLog("Ldap load : DOMAIN="+pv);
				if(!Util.null2String(pv).equals("")) {
					DOMAIN = ""+i;
					if (type.equals("ad"))
						domain = changeStr(pv);
	       				domain = buildDomain(pv);
	       			 InitialDirContext initialContext = (InitialDirContext) getInitialContext();
	                 SearchControls sc = new SearchControls();
	                 sc.setSearchScope(2);
	                 if (type.equals("ad")){
       				 NamingEnumeration iter = initialContext.search(domain, "(&(samaccountname=" + account + ")(objectclass=user))", sc);
       				 if (iter.hasMoreElements()) {
       					SearchResult result = (SearchResult) iter.nextElement();
       					Attributes attributes = result.getAttributes();
       					Attribute attr1 = attributes.get("samaccountname");
       					Attribute arrt2 = attributes.get("userAccountControl");
       					
       					String userAccountControl = (String)arrt2.get();
       					String samaccountname = (String)attr1.get();
       					initialContext.close();
       					
       					if(ifAccountControl(userAccountControl)){//检查禁止帐户状态
                    		return "uac";
                    	}
       					return samaccountname;
       				 }
	                 }else{
	                	 NamingEnumeration iter = initialContext.search(domain, "(&(uid=" + account + ")(objectclass=inetOrgPerson))", sc);
	       				 if (iter.hasMoreElements()) {
	       					SearchResult result = (SearchResult) iter.nextElement();
	       					Attributes attributes = result.getAttributes();
	       					Attribute attr1 = attributes.get("uid");
	       					
	       					String samaccountname = (String)attr1.get();
	       					initialContext.close();
	       					
	       					return samaccountname;
		       			}
	                 }
	       			initialContext.close();
	       				
				}
	       		
	       	}
		} else {
			RecordSet rs1 = new RecordSet();
			rs1.executeSql("select * from ldapsetdetail where 1=1");
			
            SearchControls sc = new SearchControls();
            sc.setSearchScope(2);
			while(rs1.next()) {
				String domain = rs1.getString("subcompanydomain");
				domain = changeStr(domain);
		        domain = buildDomain(domain);
		        tempDomain = domain;
		        InitialDirContext initialContext = (InitialDirContext) getInitialContext();
		        if (type.equals("ad")){
	            NamingEnumeration iter = initialContext.search(domain, "(&(samaccountname=" + account + ")(objectclass=user))", sc);
	            if (iter.hasMoreElements()) {
	            	SearchResult result = (SearchResult) iter.nextElement();
   					Attributes attributes = result.getAttributes();
   					Attribute attr1 = attributes.get("samaccountname");
   					Attribute arrt2 = attributes.get("userAccountControl");
   					
   					String userAccountControl = (String)arrt2.get();
   					String samaccountname = (String)attr1.get();
   					initialContext.close();
   					
   					if(ifAccountControl(userAccountControl)){//检查禁止帐户状态
                		return "uac";
                	}
   					return samaccountname;
	            }
		        }else{
               	 NamingEnumeration iter = initialContext.search(domain, "(&(uid=" + account + ")(objectclass=inetOrgPerson))", sc);
   				 if (iter.hasMoreElements()) {
   					SearchResult result = (SearchResult) iter.nextElement();
   					Attributes attributes = result.getAttributes();
   					Attribute attr1 = attributes.get("uid");
   					
   					String samaccountname = (String)attr1.get();
   					initialContext.close();
   					
   					return samaccountname;
       			}
             }
	            initialContext.close();
   				
			}
		}
		return null;
    } catch(Exception e) {
    	e.printStackTrace();
    	return null;
    }
   }

   private String getUserDN(String uid,String BASEDN) {
	   String userDN = "";
		try {
			InitialDirContext initialContext = (InitialDirContext) getInitialContext();
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration<SearchResult> en = initialContext.search(BASEDN, "uid=" + uid, constraints);
			if (en == null || !en.hasMoreElements()) {
				writeLog("LdapUtil: 未找到该用户");
			}
			while (en != null && en.hasMoreElements()) {
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					userDN += si.getName();
					userDN += "," + BASEDN;
				} else {
					//System.out.println(obj);
				}
			}
		} catch (Exception e) {
			writeLog("LdapUtil: 查找用户时产生异常: ");
			writeLog(e);
		}
		return userDN;
	}
   
    public boolean authentic4iPlanet(String account, String password) {
        try {
	    	if(account.indexOf("\\")!=-1){
	      	  account=account.substring(account.indexOf("\\")+1);
	      	}
            Hashtable env = new Hashtable();
            env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
            env.put(Context.PROVIDER_URL, ldapserverurl);
            String domain = "";
        	if("1".equals(ldapfrom))
        	{
        		domain = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1"));
            	domain = changeStr(domain);
        	}
        	else if("2".equals(ldapfrom))
        	{
        		domain = new String(Util.null2String((String)ldaparealist.get(Util.getIntValue(DOMAIN,0))));
        		domain = changeStr(domain);
        	}
            domain = changeStr(domain);
            domain = buildDomain(domain);
            String userDN = getUserDN(account,domain);
            env.put(Context.SECURITY_PRINCIPAL, userDN);
            env.put(Context.SECURITY_CREDENTIALS, password);
            env.put( Context.REFERRAL, "follow" );

            InitialDirContext initialContext = new InitialDirContext(env);
            initialContext.close();
            return true;
        } catch (Exception e) {
        	writeLog(e);
            return false;
        }
    }
  public HashMap updateUserInfo(String account_new,String oldpassword,String lastname,String password,String ou,String flage) {
		HashMap resultMap = new HashMap();
		boolean isSuccess = true;
		String errorMsg = "";
		String errorType = "";
		boolean isNew = false;
		boolean isAccountExist = false;
		if("0".equals(flage)) {//用户个人修改密码
			isAccountExist = authentic(account_new,oldpassword);
		} else {//系统管理员修改密码
			isAccountExist = true;
		}
		RecordSet rs = new RecordSet();
		ArrayList<String> domainList = new ArrayList<String>();
		//String syspassword = Prop.getPropValue(GCONST.getConfigFile(),NEED_SYNPASSWORD);
		if (needSynPassword != null && needSynPassword.equalsIgnoreCase("y")) {
			if("y".equals(needSynOrg)) {
				rs.executeSql("select * from ldapsetdetail");
				while(rs.next()) {
					domainList.add(rs.getString("subcompanydomain"));
				}
			} else {
				for(int i = 0; i < ldaparealist.size();i++) {
					domainList.add((String)ldaparealist.get(i));
				}
			}
			try {
				for(int i = 0; i < domainList.size(); i++) {
					DOMAIN = ""+i;
					String domain = domainList.get(i);
					DirContext ctx = (DirContext) this.getDirContext(domain);
					if (ctx != null) {
						//String userDN = getUserDN(ctx,account_old);
						String userDN_new = getUserDN(ctx,account_new);
						if(!userDN_new.equals("")){//账号已存在
							
							
							
						}else{
							//账号不存在， 则新建出用户--暂不新建
						}
						if(isAccountExist){
							ModificationItem modificationItem[] = new ModificationItem[1];
							//66080,Integer.toString(UF_NORMAL_ACCOUNT + UF_DONT_EXPIRE_PASSWD)
							modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("userAccountControl", 
									Integer.toString(66080)));//66080用户是启用状态
							ctx.modifyAttributes(userDN_new,modificationItem);
							
							try{
								String newQuotedPassword = "\"" + password + "\"";
								byte[] pwd;
								pwd = newQuotedPassword.getBytes("UTF-16LE");
								modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute("unicodePwd", pwd));
								ctx.modifyAttributes(userDN_new,modificationItem);
								writeLog("LdapUtil.updateUserInfo: 更新用户信息成功.dn="+userDN_new);
								isSuccess = true;
								break;
							}catch(Exception e){
								isSuccess = false;
								errorType = "1";
								errorMsg = "密码不符合密码策略，请重新设置。";
								writeLog("LdapUtil.updateUserInfo: 更新用户信息异常："+errorMsg);
								writeLog(e);
								e.printStackTrace();
								
							}
						}else{
							isSuccess = false;
							errorMsg = "账号b不存在，请重新设置。";
							errorType= "2";
						}
					} else {
						//证书密码为空 居然能修改密码成功  在这里加一个防御
						isSuccess = false;
						errorType = "6";
	     				errorMsg = "证书密码错误";
					}
					ctx.close();
				}
				
			} catch (Exception e) {
				isSuccess = false;
				if(e.toString().indexOf("timestamp check failed") > -1) {
					errorType = "7";
     				errorMsg = "证书过期";
				}else if(e.toString().indexOf("java.security.InvalidAlgorithmParameterException") > -1) {
					errorType = "5";
     				errorMsg = "证书路径错误";
				} else if(e.toString().indexOf("java.security.NoSuchAlgorithmException") > -1) {
					errorType = "6";
     				errorMsg = "证书密码错误";
				} else if(e.toString().indexOf("javax.naming.AuthenticationException")>-1 
						|| e.toString().indexOf("KIX path building failed")>-1
						){
        			errorType = "4";
     				errorMsg = "证书不可使用";
     			} else if(e.toString().indexOf("NamingException: Cannot parse url")>-1||
     					(e.toString().indexOf("javax.naming.CommunicationException")>-1
     							&&e.toString().indexOf("java.security.NoSuchAlgorithmException") < 0)){
					errorType = "3";
					errorMsg = "无法连接";
        		} 
				
				//errorMsg = processExceptionMessage(e.getMessage());
				writeLog("LdapUtil.updateUserInfo: 更新用户信息异常："+errorMsg);
				writeLog(e);
				e.printStackTrace();
				
			}
		}
		
		resultMap.put("isSuccess", isSuccess+"");
		resultMap.put("errorMsg", errorMsg+"");
		resultMap.put("errorType", errorType+"");
		return resultMap;
	}
    
    private String processExceptionMessage(String errorMsg){
		return errorMsg.substring(0,errorMsg.length()-3)+errorMsg.substring(errorMsg.length()-1);
	}
 public String getUserDN(DirContext ctx,String searchaccount) throws Exception{
		String distinguishedName = "";
		SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        //String root = "";
        /*if(ldapfrom.equals("1")) {//配置文件的方式
        	root = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1"));
        } else {//数据库的方式
        	root = (String)ldaparealist.get(0);
        }
        
		root = changeStr(root);*/
		String baseDN = getBaseDN(tempDomain);
		String filter = "*";
		if (type.equalsIgnoreCase("ad")){
          	filter = "(&(objectCategory=person)(objectClass=user)(sAMAccountName="+searchaccount+"))";        	
        }else if (type.equalsIgnoreCase("OpenLDAP")){
          	        	
        }else {
        	filter = "(&(objectCategory=person)(uid="+searchaccount+"))";
        }
        NamingEnumeration iter = ctx.search(baseDN, filter, sc);
        if (iter.hasMoreElements()) {
            SearchResult SearchResult = (SearchResult) iter.nextElement();
            Attributes attributes = SearchResult.getAttributes();
			
			String userAccountControl = (String)attributes.get("userAccountControl").get();
        	if(isUac.equals("1")&&ifAccountControl(userAccountControl)){//检查禁止帐户状态
        	}else{
        		distinguishedName = (String)attributes.get("distinguishedName").get();
        	}
        }
        
        return distinguishedName;
	}
    
    public DirContext getDirContext(String domain1) throws Exception {
    	if(keystorepassword == null || "".equals(keystorepassword)) {
    		return null;
    	}
    	Hashtable env = new Hashtable();
        env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
        env.put(Context.PROVIDER_URL, ldapserverurl2);
        env.put(Context.SECURITY_PROTOCOL, "ssl");
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		System.setProperty("javax.net.ssl.trustStore", keystorepath); 
		System.setProperty("javax.net.ssl.trustStorePassword", keystorepassword);
		env.put("java.naming.ldap.factory.socket", "weaver.ldap.DummySSLSocketFactory");
	
		
        
        ldapuser = buildPrincipal(ldapuser);//处理账号中的特殊字符
        if (type.equals("ad")) {
        	String domain = "";
        	if("1".equals(ldapfrom))
        	{
        		domain = new String(Util.null2String(Prop.getPropValue(GCONST.getConfigFile(), DOMAIN)).getBytes("ISO8859-1"));
            	domain = changeStr(domain);
        	}
        	else if("2".equals(ldapfrom))
        	{
        		if(!"y".equals(needSynOrg)&&!"".equals(ldaparea)) {
        			domain = domain1;
        			//domain = new String(Util.null2String((String)ldaparealist.get(Util.getIntValue(DOMAIN,0))));
        		} else if("y".equals(needSynOrg)){
        			domain = domain1;
        		}
        		
        	}
        	domain = changeStr(domain);
        	domain = buildDomain(domain);
            String[] arr_baseDN = Util.TokenizerString2(domain, ",");
            String baseDN = "";
            for (int i = 0; i < arr_baseDN.length; i++) {
            	if (arr_baseDN[i].indexOf("dc=") < 0)
                    continue;
                if (!baseDN.equals(""))
                    baseDN = baseDN+"," + arr_baseDN[i];
                else
                    baseDN += arr_baseDN[i];
            }
            baseDN = buildDomain(baseDN);//处理domain中的特殊字符
            tempDomain = baseDN;
            if(ldapuser.toUpperCase().indexOf("CN=")<0&&ldapuser.toUpperCase().indexOf(",OU=")<0&&ldapuser.toUpperCase().indexOf(",DC=")<0){  //原来的配置，默认在users组下
            	env.put(Context.SECURITY_PRINCIPAL, "cn=" + ldapuser + ",cn=users," + baseDN);
            }
            else{
            	 env.put(Context.SECURITY_PRINCIPAL, ldapuser);
            }
        } else {
        	 env.put(Context.SECURITY_PRINCIPAL, ldapuser);
        }
        	
       
        env.put(Context.SECURITY_CREDENTIALS, ldappasswd);
        
        InitialDirContext initialContext = new InitialDirContext(env);
        
        return initialContext;
    }
    
     String getBaseDN(String root) {
		String[] arr_baseDN = Util.TokenizerString2(root, ",");
		String baseDN = "";
		for (int i = 0; i < arr_baseDN.length; i++) {
		    if (arr_baseDN[i].indexOf("dc=") < 0)
		        continue;
		    if (!baseDN.equals(""))
		        baseDN = baseDN+"," + arr_baseDN[i];
		    else
		        baseDN += arr_baseDN[i];
		}
		return baseDN;
	}
    /**
     * 全部转换成小写，全角转半角
     *
     * @str string
     * @return newString
     */
    public static String changeStr(String str){
		str = str.trim();
		str = str.toLowerCase();
		char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == '\u3000') {
             c[i] = ' ';
          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
             c[i] = (char) (c[i] - 65248);
          }
        }
       return new String(c);
	}

	/**
     * 全角转半角,判断有没有包含特殊字符  ：/  \ # +  ; " , <>  = 空格
     * @param str
     * @return
     */
    public static boolean CheckDomain(String str){
		String str1=str;
		boolean bl=false;
		char c[] = str1.toCharArray();
        for (int i = 0; i < c.length; i++) {
          if (c[i] == '\u3000') {
             c[i] = ' ';
          } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
             c[i] = (char) (c[i] - 65248);
          }
        }
        str1= new String(c);
         bl=str1.matches(".*[\\x2f\\x5c\\x23\\x2b\\x3b\\x22\\x2c\\x3c\\x3e\\x3d\\x20\\x60].*");
		return bl;
	    	
	}
    
    /**
     * 处理domain中的特殊字符
     * @param domain
     * @return
     */
    public String buildDomain(String domain){
		String result = procSpecialChar(domain);
		result = result.replaceAll("ou\\\\=", "ou=").replaceAll("dc\\\\=", "dc=");
		result = result.replaceAll("\\\\,ou=", ",ou=").replaceAll("\\\\,dc=", ",dc=");
		
		result = result.replaceAll("OU\\\\=", "OU=").replaceAll("DC\\\\=", "DC=");
		result = result.replaceAll("\\\\,OU=", ",OU=").replaceAll("\\\\,DC=", ",DC=");
		
		result = result.replaceAll("\\\"", "\\\\\\\"");//处理双引号
		
		//解决非AD的LDAP使用o类型组织架构问题
		result = result.replaceAll("o\\\\=", "o=").replaceAll("dc\\\\=", "dc=");
		result = result.replaceAll("\\\\,o=", ",o=").replaceAll("\\\\,dc=", ",dc=");
		result = result.replaceAll("O\\\\=", "O=").replaceAll("DC\\\\=", "DC=");
		result = result.replaceAll("\\\\,O=", ",O=").replaceAll("\\\\,DC=", ",DC=");
		
		return result;
	}
    
    /**
     * 处理账号（dn）中的特殊字符
     * @param principal
     * @return
     */
    public String buildPrincipal(String principal){
		String result = procSpecialChar(principal);
		result = result.replaceAll("cn\\\\=", "cn=").replaceAll("ou\\\\=", "ou=").replaceAll("dc\\\\=", "dc=");
		result = result.replaceAll("\\\\,cn=", ",cn=").replaceAll("\\\\,ou=", ",ou=").replaceAll("\\\\,dc=", ",dc=");
		
		result = result.replaceAll("CN\\\\=", "CN=").replaceAll("OU\\\\=", "OU=").replaceAll("DC\\\\=", "DC=");
		result = result.replaceAll("\\\\,CN=", ",CN=").replaceAll("\\\\,OU=", ",OU=").replaceAll("\\\\,DC=", ",DC=");
		
		result = result.replaceAll("\\\"", "\\\\\\\"");//处理双引号
		
		//解决非AD的LDAP使用o类型组织架构问题
		result = result.replaceAll("cn\\\\=", "cn=").replaceAll("o\\\\=", "o=").replaceAll("dc\\\\=", "dc=");
		result = result.replaceAll("\\\\,cn=", ",cn=").replaceAll("\\\\,o=", ",o=").replaceAll("\\\\,dc=", ",dc=");
		result = result.replaceAll("CN\\\\=", "CN=").replaceAll("O\\\\=", "O=").replaceAll("DC\\\\=", "DC=");
		result = result.replaceAll("\\\\,CN=", ",CN=").replaceAll("\\\\,O=", ",O=").replaceAll("\\\\,DC=", ",DC=");
		
		return result;
	}

    /**
     * 处理特殊字符
     * @param principal
     * @return
     */
	private String procSpecialChar(String principal) {
		//特殊字符列表
		// 34 35 39 43 44 47 59 60 61 62 92 
		//  "  #  '  +  ,  /  ;  <  =  >  \ 
		char [] ccArray = new char[]{'"','#','\'','+',',','/',';','<','=','>','\\'};
		String ccStr = new String(ccArray);
		
		StringBuilder sb = new StringBuilder();
		principal = principal.replaceAll("\\\\", "\\\\\\\\");
		char[] domainCharArray = principal.toCharArray();
		for(int i=0;i<domainCharArray.length;i++){
			String cs = new String(new char[]{domainCharArray[i]});
			if(ccStr.indexOf(cs)>-1){
				sb.append("\\");
			}
			
			sb.append(domainCharArray[i]);
		}
		
		String result = sb.toString();
		return result;
	}
	
	
	public void updateOraddSubCompany(OrgXmlBean orgXmlBean,String operation) {
		try{
			RecordSet rs = new RecordSet();
			String code = orgXmlBean.getCode();
			String shortname = orgXmlBean.getShortname();
			String fullname = orgXmlBean.getFullname();
			String parent_code = orgXmlBean.getParent_code();
			int order = Util.getIntValue(orgXmlBean.getOrder(),0);
			String supsubcomid = "0";
			String companyid=""; //分部id
			String sql = "";
			//获取上级分部ID
			rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+parent_code+"'");
			if(rs.next()) {
				supsubcomid = Util.null2String(rs.getString("id"));
			}
			
			//更新分部信息
			sql = "update HrmSubCompany set subcompanyname='"+shortname+"',subcompanydesc='"+fullname+"',supsubcomid='"+supsubcomid+"',showorder="+order+" where subcompanycode='" + code + "'";
			rs.executeSql(sql);
			
		    rs.executeSql("select id from HrmSubCompany where  subcompanycode='" + code + "'");
		    if(rs.next()){
		    	companyid = Util.null2String(rs.getString("id"));
		    }
			
		    if(!companyid.equals("")){
		    
			    boolean leftmenu=true;
			    
			    boolean mainmenu=true;
			    
			    rs.executeSql("select * from leftmenuconfig where  resourceid=" + companyid + " and resourcetype=2 ");
			    if(rs.next()){
			    	leftmenu=false;
			    }
			    
			    rs.executeSql("select * from mainmenuconfig where  resourceid=" + companyid + " and resourcetype=2 ");
			    if(rs.next()){
			    	mainmenu=false;
			    }
			    
			    String strWhere="";
			    String strSql="";
			    if(leftmenu){
				strWhere=" where resourcetype=2 and resourceid="+supsubcomid; 
				if(supsubcomid.equals("0")) strWhere=" where resourcetype=1  and resourceid=1 "; 
				strSql="insert into leftmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex,"+companyid+",2,locked,lockedbyid,usecustomname,customname,customname_e from leftmenuconfig "+strWhere;
				rs.executeSql(strSql);
			    }
				
			    if(mainmenu){
				strWhere=" where resourcetype=2 and resourceid="+supsubcomid; 
				if(supsubcomid.equals("0")) strWhere=" where resourcetype=1  and resourceid=1 "; 
				strSql="insert into mainmenuconfig (userid,infoid,visible,viewindex,resourceid,resourcetype,locked,lockedbyid,usecustomname,customname,customname_e)  select  distinct  userid,infoid,visible,viewindex,"+companyid+",2,locked,lockedbyid,usecustomname,customname,customname_e from mainmenuconfig "+strWhere;
				rs.executeSql(strSql);
			    }
			
		    }
			int id=0;
			rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+code+"'");
			if(rs.next()) {
				id = rs.getInt(1);				
		    }
			//同步RTX
			if("add".equals(operation)) {
				rtxtmp.addSubCompany(id);
			} else if("update".equals(operation)) {
				rtxtmp.editSubCompany(id);
			}
		} catch(Exception e){
			writeLog("编辑分部失败,"+e);
		}
	}
	
	public void updateOraddDepartment(OrgXmlBean orgXmlBean,String operation) {
		boolean booleantmp = false;
		RecordSet rs = new RecordSet();
		String code = orgXmlBean.getCode();
		String shortname = orgXmlBean.getShortname();
		String fullname = orgXmlBean.getFullname();
		String parent_code = orgXmlBean.getParent_code();
		String org_code = orgXmlBean.getOrg_code();
		int order = Util.getIntValue(orgXmlBean.getOrder(),0);
		String sql = "";
		int subcomid = 0;
		int supdeptid = 0;
				
        //获取分部ID
		rs.executeSql("select id from HrmSubCompany where subcompanycode = '"+org_code+"'");
		if(rs.next()) {
			subcomid = Util.getIntValue(rs.getString("id"),0);
		}
		
		//获取上级部门ID
		rs.executeSql("select id from hrmdepartment where departmentcode = '"+parent_code+"'");
		if(rs.next()) {
			supdeptid = Util.getIntValue(rs.getString("id"),0);
		}
		
		//编辑部门
		sql = "update hrmdepartment set departmentname='"+shortname+"',departmentmark='"+fullname+"',subcompanyid1="+subcomid+",supdepid="+supdeptid+",showorder="+order+" where departmentcode='"+code+"'";				
		booleantmp = rs.executeSql(sql);
		//获取部门ID
		int id=0;
		rs.executeSql("select id from hrmdepartment where departmentcode = '"+code+"'");
		if(rs.next()) {
			id = rs.getInt(1);			
	    }
		//同步RTX
		if("add".equals(operation)) {
			rtxtmp.addDepartment(id);
		} else if("update".equals(operation)) {
			rtxtmp.editDepartment(id);
		}
	}
	
	
	public void setBean(Iterator ite2,Attributes attrs, Department bean2) {
		try{
			while(ite2.hasNext()) {
	    		String fieldval = (String)ite2.next();
	    		String key = depfield.get(fieldval);
	    		if(key != null) {
	    			if(key.startsWith("$")) {
	    				String atttibute = key.replace("$", "");
	    				String attrVal = (String)attrs.get(atttibute).get();
	    				if("objectGUID".equals(atttibute)) {//如果是GUID,需要进行编码转换
	    					attrVal = getGUID(attrVal.getBytes());  
	    				}
	    				BeanUtils.setProperty(bean2, fieldval, attrVal);//把属性值放入bean中
	    			} else {
	    				BeanUtils.setProperty(bean2, fieldval, key);//不以$开头的，默认为指定的值
	    			}
	    		}
	    	}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}
	
	public String getInsertDepSql(Collection c3,Map beanattrs, String depCode) throws SecurityException, NoSuchFieldException{
		
		 String sql_cols = "";
         String sql_vals = "";
         sql_cols = sql_cols + "departmentcode,";
         sql_vals = sql_vals + "'" + depCode + "',";
         for (Iterator iter = (Iterator) c3.iterator(); iter.hasNext();) {
             String col = (String) iter.next();
             String val = (String) beanattrs.get(col);
             if (!col.equalsIgnoreCase("class") && val != null && !val.equals("") && !val.equals("-1")) {
                 	if (Department.class.getDeclaredField(col).getType().getName().equals("java.lang.String")) {
                 	sql_cols = sql_cols + col +","; 
                     sql_vals =  sql_vals + "'"+val+"',";
                 } else {
                 	sql_cols = sql_cols + col + ","; 
                 	sql_vals = sql_vals + val + ",";
                 }
             }
         }
         if(sql_vals.endsWith(",")) {
         	sql_vals = sql_vals.substring(0, sql_vals.length()-1);//删除逗号
            sql_cols = sql_cols.substring(0, sql_cols.length()-1);//删除逗号
         }
         return "insert into hrmdepartment ("+sql_cols+") values ("+sql_vals+")";
	}
	/**
	 * 
	 * Title:
	 * @return
	 * @throws Exception
	 * @return boolean
	 */   
	public DirContext getDirContext() throws Exception {
		String root = new String(Util.null2String(ldaparea).getBytes("ISO8859-1"));
		root = changeStr(root);

		Hashtable env = new Hashtable();
		if (type.equals("ad")) {
            String baseDN = getBaseDN(root);
            env.put(Context.SECURITY_PRINCIPAL, "cn=" + ldapuser + ",cn=users," + baseDN);
            writeLog("SECURITY_PRINCIPAL==="+"cn=" + ldapuser + ",cn=users," + baseDN);
		}
		DirContext ctx = null;
		System.setProperty("javax.net.ssl.trustStore", keystorepath); 
		System.setProperty("javax.net.ssl.trustStorePassword", keystorepassword);
		env.put("java.naming.ldap.factory.socket", "weaver.ldap.DummySSLSocketFactory");

	    env.put(Context.SECURITY_PROTOCOL, "ssl");
	    env.put(Context.INITIAL_CONTEXT_FACTORY, factoryclass);
		String url = Prop.getPropValue(GCONST.getConfigFile(), PROVIDER_URL);
		if(url.indexOf("ldaps://")==-1){
        	url = url.replaceFirst("ldap://","ldaps://");
        }
		env.put(Context.PROVIDER_URL, ldapserverurl2);
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_CREDENTIALS,ldappasswd);
		
		  env.put(Context.REFERRAL,"ignore");
	
		//writeLog("INITIAL_CONTEXT_FACTORY===="+"cn=" + ldapuser + ",cn=users," + factoryclass);
		//writeLog("PROVIDER_URL==="+ldapserverurl2);
		//writeLog("SECURITY_CREDENTIALS==="+ldappasswd);
		try {
			ctx = new InitialDirContext(env);
		} catch (javax.naming.AuthenticationException e) {
			writeLog(this.getClass().toString(), "LdapUtil.getDirContext: 认证失败"+e.toString());
			writeLog(e);
		} catch (Exception e) {
			e.printStackTrace();
			writeLog(this.getClass().toString(), "LdapUtil.getDirContext: 认证出错"+e.toString());
			writeLog(e);
		}
		return ctx;
	}
	public String getUserDN(DirContext ctx,String attr,String searchaccount) throws Exception{
		String distinguishedName = "";
		SearchControls sc = new SearchControls();
        sc.setSearchScope(2);
        String root = new String(Util.null2String(ldaparea).getBytes("ISO8859-1"));
        
		root = changeStr(root);
		String baseDN = getBaseDN(root);
		
		String filter = "*";
		if (type.equalsIgnoreCase("ad")){
          	filter = "(&(objectCategory=person)(objectClass=user)("+attr+"="+searchaccount+"))";        	
        }else if (type.equalsIgnoreCase("OpenLDAP")){
          	        	
        }else {
        	//filter = "(&(objectCategory=person)(uid="+searchaccount+"))";
        }
        NamingEnumeration iter = ctx.search(baseDN, filter, sc);
        if (iter.hasMoreElements()) {
            SearchResult SearchResult = (SearchResult) iter.nextElement();
            Attributes attributes = SearchResult.getAttributes();
            String account = (String) attributes.get(attr).get();
			
			String userAccountControl = (String)attributes.get("userAccountControl").get();
        	if(isUac.equals("1") && ifAccountControl(userAccountControl)){//检查禁止帐户状态
        		
        	}else{
        		distinguishedName = (String)attributes.get("distinguishedName").get();
        	}
        }
        
        return distinguishedName;
	}
	public void modiyOu(DirContext ctx,String oldOu,String newOu) {
	       try {
	           ctx.rename(oldOu,newOu);
	       } catch (Exception e) {
	    	  e.printStackTrace();
	       }
	    }
	/**
	 * 修改AD域用户信息
	 * @param workcode
	 * @param account
	 * @param lastname
	 * @param department
	 * @param password
	 * @param ou
	 * @return
	 */
	public HashMap updateUserInfo(String id) {
		
		
		//读取配置文件
		weaver.GetProp.GetPropUtil propUtil=new GetPropUtil();
		// 取出各列名
		String accountField = "sAMAccountName";//sAMAccountName
//		String sexField =  getADField(getPropValue("ldap1", "sex"));
//		String lastnameField =  getADField(getPropValue("ldap1", "lastname"));//cn
//		String deptField =  getADField(getPropValue("ldap1", "dept"));
		String emailField =  "mail";
//		String telephoneField =  getADField(getPropValue("ldap1", "telephone"));
//		String cardnoField =  getADField(getPropValue("ldap1", "cardno"));
		
	//	String dc = "";
		//System.out.println("ou_dc::"+ou_dc);
		
		RecordSet rs =new RecordSet ();
		rs.execute("select * from hrmresource where id = "+id);
		rs.next();
		String account = rs.getString("loginid");
		String lastname = rs.getString("lastname");
		String departmentid = rs.getString("departmentid");
		String subcompanyid=null;
		String jobtitle= rs.getString("jobtitle");
		String locationid= rs.getString("locationid");

		
		String email = rs.getString("email");
		String telephone = rs.getString("telephone");
		String mobile = rs.getString("mobile");
		String mobilecall = rs.getString("mobilecall");
		String fax = rs.getString("fax");

		
		
		String workcode = Util.null2String(rs.getString("workcode"));
		if(!"".equals(workcode) && workcode.indexOf("0")>-1){
			while(true){
				if(workcode.substring(0, 1).equals("0")){
					workcode = workcode.substring(1, workcode.length());
				}else{
					break;
				}
			}
		}
		String displayName = "";
		if("".equals(workcode)){
			displayName = lastname;
		}else{
			displayName = lastname+workcode;
		}
		HashMap resultMap = new HashMap();
		boolean isSuccess = true;
		String errorMsg = "";
		boolean isNew = false;
		
			try {
				DirContext ctx = (DirContext) this.getDirContext();
					//System.out.println("lastnameField::"+lastnameField+",lastname::"+lastname+",accountField::"+accountField+",account::"+account);
					String userDN = getUserDN(ctx,accountField,account);//使用account取得用户
					int UF_ACCOUNTDISABLE = 0x0002;   
			        int UF_PASSWD_NOTREQD = 0x0020;  //32 
			        int UF_PASSWD_CANT_CHANGE = 0x0040;   
			        int UF_NORMAL_ACCOUNT = 0x0200;   //512
			        int UF_DONT_EXPIRE_PASSWD = 0x10000;   
			        int UF_PASSWORD_EXPIRED = 0x800000;
					ModificationItem modificationItem[] = new ModificationItem[1];
						modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(accountField, account));
						ctx.modifyAttributes(userDN,modificationItem);
						
						
						
						if (ctx != null && !account.equals("")) {
							Properties adprop=propUtil.getProp("ldasynfield");
							if(adprop.get("Department")!=null){
								if(departmentid!=null&&!"".equals(departmentid)){
								
									rs.executeSql("select a.subcompanyid1,a.supdepid ,a.departmentname from hrmdepartment a where a.id="+departmentid);
									if(rs.next()) {
										subcompanyid=rs.getString("subcompanyid1");
									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("Department").toString().replace("$", ""), rs.getString("departmentname")));
									ctx.modifyAttributes(userDN,modificationItem);
									}
								}else{
									try{
										modificationItem[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(adprop.get("Department").toString().replace("$", "")));
										ctx.modifyAttributes(userDN,modificationItem);
									}catch(Exception ex){
									}
								}
							}
							if(adprop.get("GivenName")!=null){
							 
									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("GivenName").toString().replace("$", ""), lastname));
									ctx.modifyAttributes(userDN,modificationItem);
								 
							}
							if(adprop.get("Company")!=null){
									if(subcompanyid!=null&&!"".equals(subcompanyid)){
										rs.executeSql("select a.SUPSUBCOMID,a.subcompanyname from hrmsubcompany a where a.id="+subcompanyid);
										if(rs.next()) {
											modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("Company").toString().replace("$", ""), rs.getString("subcompanyname")));
											ctx.modifyAttributes(userDN,modificationItem);
										}
									}else{
										try{
											modificationItem[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(adprop.get("Company").toString().replace("$", "")));
											ctx.modifyAttributes(userDN,modificationItem);
										}catch(Exception ex){
										}
									}
							}
							
							if(adprop.get("Title")!=null){
							
								if(jobtitle!=null&&!"".equals(jobtitle)){
									rs.executeSql(" SELECT jobtitlename   from    hrmjobtitles a where a.id="+jobtitle);
									if(rs.next()) {
										modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("Title").toString().replace("$", ""), rs.getString("jobtitlename")));
										ctx.modifyAttributes(userDN,modificationItem);
									}
								}else{
									try{
										modificationItem[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(adprop.get("Title").toString().replace("$", "")));
										ctx.modifyAttributes(userDN,modificationItem);
									}catch(Exception ex){
									}
								}
							}
							if(adprop.get("Office")!=null){
								if(locationid!=null&&!"".equals(locationid)){
									rs.executeSql("   SELECT *   from     hrmlocations where id="+locationid);
									if(rs.next()) {
										String officeName=rs.getString("locationname");
										modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("Office").toString().replace("$", ""), officeName));
										ctx.modifyAttributes(userDN,modificationItem);
									}
								}else{
									try{
										modificationItem[0] = new ModificationItem(DirContext.REMOVE_ATTRIBUTE, new BasicAttribute(adprop.get("Office").toString().replace("$", "")));
										ctx.modifyAttributes(userDN,modificationItem);
									}catch(Exception ex){
									}
								}
							}
							if( adprop.get("OfficePhone")!=null){

									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("OfficePhone").toString().replace("$", ""),telephone));
									ctx.modifyAttributes(userDN,modificationItem);
						
							}

							if( adprop.get("MobilePhone")!=null){

						
									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("MobilePhone").toString().replace("$", ""), mobile));
									ctx.modifyAttributes(userDN,modificationItem);
							
							}
							
							
							
							
							
							if( adprop.get("HomePhone")!=null){

								
									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("HomePhone").toString().replace("$", ""), mobilecall));
									ctx.modifyAttributes(userDN,modificationItem);
							
							}
							
							
							if( adprop.get("Fax")!=null){

							
									modificationItem[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, new BasicAttribute(adprop.get("Fax").toString().replace("$", ""), fax));
									ctx.modifyAttributes(userDN,modificationItem);
							
							}
							
							
						
						writeLog("LdapUtil.updateUserInfo: 更新用户信息成功.dn="+userDN);
				
				
				} else {
					isSuccess = false;
					errorMsg = "账号不能为空。";
				}
				if(ctx!=null)
				ctx.close();
			} catch (Exception e) {
				isSuccess = false;
				errorMsg = processExceptionMessage(e.getMessage());
				writeLog("LdapUtil.updateUserInfo: 更新用户信息异常："+errorMsg);
				writeLog(e);
				e.printStackTrace();
				
			}
		
		resultMap.put("isSuccess", isSuccess+"");
		resultMap.put("errorMsg", errorMsg+"");
		return resultMap;
	}
	
	public String getTimeModul()
	{
		return TimeModul;
	}

	public void setTimeModul(String timeModul)
	{
		TimeModul = timeModul;
	}

	public String getFrequency()
	{
		return Frequency;
	}

	public void setFrequency(String frequency)
	{
		Frequency = frequency;
	}

	public String getFrequencyy()
	{
		return frequencyy;
	}

	public void setFrequencyy(String frequencyy)
	{
		this.frequencyy = frequencyy;
	}

	public String getCreateType()
	{
		return createType;
	}

	public void setCreateType(String createType)
	{
		this.createType = createType;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public boolean isIstest()
	{
		return istest;
	}

	public void setIstest(boolean istest)
	{
		this.istest = istest;
	}

	public String getIsuseldap()
	{
		return isuseldap;
	}

	public void setIsuseldap(String isuseldap)
	{
		this.isuseldap = isuseldap;
	}

	public String getLdapcondition()
	{
		return ldapcondition;
	}

	public void setLdapcondition(String ldapcondition)
	{
		this.ldapcondition = ldapcondition;
	}

	public String getErrormsg()
	{
		return errormsg;
	}

	public String getLdappasswd() {
		return ldappasswd;
	}

	public void setLdappasswd(String ldappasswd) {
		this.ldappasswd = ldappasswd;
	}
}