package weaver.interfaces.web;

import java.security.MessageDigest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Hex;

import weaver.conn.RecordSet;
import weaver.file.Prop;
import weaver.general.BaseBean;
import weaver.general.TimeUtil;
import weaver.general.Util;
import weaver.workflow.msg.PoppupRemindInfoUtil;
import cn.com.weaver.ofs.webservices.AnyType2AnyTypeMapEntry;

public class PostWorkflowInf extends BaseBean {

	public boolean isvalid(String requestid){
		RecordSet rs = new RecordSet();
		// 查询流程基本信息
		String workflowid = "";
		String sql1 = "select * from workflow_requestbase where requestid=" + requestid;
		rs.executeSql(sql1);
		if (rs.next()) {
			workflowid = Util.null2String(rs.getString("workflowid"));
		}
		String sql="select * from workflow_base a where  isvalid=1 and id ="+workflowid;
		rs.execute(sql);
		return rs.next();
	}
	
	/**
	 * 操作统一待办库(待办,已办,办结)
	 * 
	 * @param requestid
	 */
	public void operateToDo(String requestid) {
		operateToDo(requestid,"");
	}
	
	/**
	 * 操作统一待办库(待办,已办,办结)
	 * 
	 * @param requestid
	 * @param 当前操作者 提醒的时候排除当前操作者
	 */
	public void operateToDo(String requestid,String userid) {
		
		if(!isvalid(  requestid)){
			return;
		}
		
		writeLog("===传输统一待办库---start" + " " + requestid + "===");
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		
		writeLog("syscode:" + " " + syscode);
		
		if (requestid == null || "".equals(requestid)) {
			return;
		}
		try {
			RecordSet rs = new RecordSet();
			RecordSet rs1 = new RecordSet();
			RecordSet rs2 = new RecordSet();
			// 查询流程基本信息
			String creater_loginid = "";
			String flowtitle = "";
			String createdatetime = "";
			String workflowname = "";
			String workflowid = "";
			String sql1 = "select * from workflow_requestbase where requestid=" + requestid;
			rs.executeSql(sql1);
			if (rs.next()) {
				String createrid = Util.null2String(rs.getString("creater"));
				workflowid = Util.null2String(rs.getString("workflowid"));
				if (!"".equals(createrid)) {
					rs1.executeSql("select * from hrmresource where id=" + createrid);
					if (rs1.next()) {
						creater_loginid = Util.null2String(rs1.getString("loginid"));
					}
					if (!"".equals(workflowid)) {
						rs2.executeSql("select * from workflow_base where id=" + workflowid);
						if (rs2.next()) {
							workflowname = Util.null2String(rs2.getString("workflowname"));
						}
					}
				}
				flowtitle = Util.null2String(rs.getString("requestname"));
				createdatetime = Util.null2String(rs.getString("createdate") + " " + rs.getString("createtime"));
			}
			// 查询操作记录
			RecordSet rs3 = new RecordSet();

			String sql2 = "select * from workflow_currentoperator where requestid =" + requestid + (!"".equals(userid)?" and userid = '" + userid + "'":"") + "  order by id";
			
			this.writeLog("PostWorkflowInf.class", "查询待推送的数据SQL:[" + sql2 + "]");
			
			rs3.executeSql(sql2);
			while (rs3.next()) {
				String receiverid = Util.null2String(rs3.getString("userid"));
				String receiver_loginid = "";
				String other_receiver_loginid = "";
				if (!"".equals(receiverid)) {
					rs1.executeSql("select * from hrmresource where id=" + receiverid);
					if (rs1.next()) {
						receiver_loginid = Util.null2String(rs1.getString("loginid"));
						
						String belongto = Util.null2String(rs1.getString("belongto"));
						if(belongto.length() > 0){
							rs1.executeSql("select * from hrmresource where id=" + belongto);
							if (rs1.next()) {
								other_receiver_loginid = Util.null2String(rs1.getString("loginid"));
							}
						}
						
					}
					
					if("".equals(other_receiver_loginid)){
						other_receiver_loginid = receiver_loginid;
					}
					
				}

				String node_id = Util.null2String(rs3.getString("nodeid"));
				String flownodename = "";
				rs1.executeSql("select nodename from workflow_nodebase where id= "+ node_id);
				if (rs1.next()) {
					flownodename = Util.null2String(rs1.getString("nodename"));
				}
				String receivedatetime = Util.null2String(rs3.getString("receivedate")+ " " + rs3.getString("receivetime"));

				String pcurl = getWf_url(receiverid, requestid);

				// String appurl = Util.null2String("&loginid=" +
				// receiver_loginid +
				// "&url=/mobile/plugin/1/view.jsp?detailid="+requestid);
				String timestamp=(new Date().getTime())+"";

				String appurl =  "/client.do?method=toDetail&loginid="+receiver_loginid+"&timestamp="
						+timestamp+"&loginTokenFromThird="+hexSHA1("weaver"+receiver_loginid+timestamp)
							+"&url=/mobile/plugin/1/view.jsp?detailid="+ requestid;

				String isremark = Util.null2String(rs3.getString("isremark")); // 操作类型
				String takisremark = Util.null2String(rs3.getString("takisremark")); // 意见征询
				
				// 被分配到主账号
				receiver_loginid = other_receiver_loginid;
				writeLog("接收者:" + receiver_loginid + "isremark状态:" + isremark);
				//whitelist=receiver_loginid;
				//for(String whiteuser:whitelist.split(",")){
					//if(receiver_loginid.equals(whiteuser)){
						if ("0".equals(isremark)) {
							// 待办事宜
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient.getClient().receiveTodoRequestByMap(dataArray);
						//	writeLog("PostWorkflowInf(143)");
							OfsClient.printResultArray(resultArray);
						}
						if ("1".equals(isremark) || "8".equals(isremark) || "9".equals(isremark)) {
							this.writeLog("requestid:[" + requestid + "],receiver_loginid:[" + receiver_loginid + "]");
							
							// 待办事宜 往统一待办库发待办消息
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient.buildDataArray(dataMap);
					//		writeLog("PostWorkflowInf(155)");
							OfsClient.printResultArray(dataArray);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient.getClient().receiveTodoRequestByMap(dataArray);
					//		writeLog("PostWorkflowInf(158)");
							OfsClient.printResultArray(resultArray);

						} else if ("2".equals(isremark)) {
							writeLog("已办参数:" + syscode + " " + requestid + " "
									+ flowtitle + " " + workflowname + " "
									+ flownodename + " " + receiver_loginid);
							String viewtype = "1";
							// 已办事宜 往统一待办库发已办消息
							/*
							 * Map<String,String> dataMap = buildDataMap( syscode,
							 * requestid, flowtitle, workflowname, flownodename,
							 * receiver_loginid ); AnyType2AnyTypeMapEntry[] dataArray =
							 * OfsClient.buildDataArray(dataMap);
							 * AnyType2AnyTypeMapEntry[] resultArray =
							 * OfsClient.getClient().processDoneRequestByMap(dataArray);
							 * OfsClient.printResultArray(resultArray);
							 */
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, isremark, viewtype, creater_loginid,
									createdatetime, receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							writeLog("PostWorkflowInf(182)");
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveRequestInfoByMap(dataArray);
							OfsClient.printResultArray(resultArray);

						} else if ("4".equals(isremark)) {
							writeLog("办结参数:" + syscode + " " + requestid + " "
									+ flowtitle + " " + workflowname + " "
									+ flownodename + " " + receiver_loginid);
							String viewtype = "0";
							// 办结 往统一待办库发办结消息
							/*
							 * Map<String,String> dataMap = buildDataMap( syscode,
							 * requestid, flowtitle, workflowname, flownodename,
							 * receiver_loginid ); AnyType2AnyTypeMapEntry[] dataArray =
							 * OfsClient.buildDataArray(dataMap);
							 * AnyType2AnyTypeMapEntry[] resultArray =
							 * OfsClient.getClient().processOverRequestByMap(dataArray);
							 * OfsClient.printResultArray(resultArray);
							 */
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, isremark, viewtype, creater_loginid,
									createdatetime, receiver_loginid, receivedatetime);
							writeLog("归档调用requestinfo接口的viewtype的值是" + viewtype);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient.getClient().receiveRequestInfoByMap(dataArray);
							writeLog("PostWorkflowInf(209)");
							OfsClient.printResultArray(resultArray);
							// 只要有归档则将待办和已办都变为办结状态
							RecordSet rs4 = new RecordSet();
							String sql3 = "select * from workflow_currentoperator where requestid =" + requestid + "  order by nodeid";
							rs4.executeSql(sql3);
							while (rs4.next()) {
								String receiverid1 = Util.null2String(rs4.getString("userid"));
								String receiver_loginid1 = "";
								if (!"".equals(receiverid1)) {
									rs1.executeSql("select * from hrmresource where id=" + receiverid1);
									if (rs1.next()) {
										receiver_loginid1 = Util.null2String(rs1.getString("loginid"));
									}
								}

								String node_id1 = Util.null2String(rs4.getString("nodeid"));
								String flownodename1 = "";
								rs1.executeSql("select nodename from workflow_nodebase where id= "+ node_id1);
								if (rs1.next()) {
									flownodename1 = Util.null2String(rs1.getString("nodename"));
								}
								String isremark1 = Util.null2String(rs4.getString("isremark")); // 操作类型
								writeLog("接收者:" + receiver_loginid1 + "isremark状态:" + isremark1);
								//whitelist=receiver_loginid;
								//for(String whiteuser1:whitelist.split(",")){
									//if(receiver_loginid1.equals(whiteuser1)){
										if ("0".equals(isremark1)||"2".equals(isremark1)) {
											// 办结 往统一待办库发办结消息
											Map<String, String> dataMap1 = buildDataMap(
													syscode, requestid, flowtitle,
													workflowname, flownodename1,
													receiver_loginid1);
											AnyType2AnyTypeMapEntry[] dataArray1 = OfsClient.buildDataArray(dataMap1);
											AnyType2AnyTypeMapEntry[] resultArray1 = OfsClient.getClient().processOverRequestByMap(dataArray1);
											OfsClient.printResultArray(resultArray1);
										}
									//}
								//}
							
							}
							break;
						}
						// 意见征询 发起人变已办
						if ("0".equals(isremark) && "-2".equals(takisremark)) {
							// 已办事宜 往统一待办库发已办消息
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									receiver_loginid);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().processDoneRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);
						}
						if ("0".equals(isremark) && "0".equals(takisremark)) {
							// 待办事宜 往统一待办库发待办消息
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							writeLog("PostWorkflowInf(272)");
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveTodoRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);
						}
					}
				//}
			
			//}

		} catch (Exception e) {
			writeLog(e);
		}

		writeLog("===传输统一待办库---end" + " " + requestid + "===");
	}
	public void operateZXToDo(String requestid,int id){
		if(!isvalid(  requestid)){
			return;
		}
		writeLog("===征询改待办--start" + " " + requestid + "===");
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		String whitelist = Util.null2String(Prop.getPropValue("bmd", "whitelist"));
		if (requestid == null || "".equals(requestid)) {
			return;
		}
		try {
			RecordSet rs = new RecordSet();
			RecordSet rs1 = new RecordSet();
			RecordSet rs2 = new RecordSet();
			// 查询流程基本信息
			String creater_loginid = "";
			String flowtitle = "";
			String createdatetime = "";
			String workflowname = "";
			String workflowid = "";
			String sql1 = "select * from workflow_requestbase where requestid="
					+ requestid;
			rs.executeSql(sql1);
			if (rs.next()) {
				String createrid = Util.null2String(rs.getString("creater"));
				workflowid = Util.null2String(rs.getString("workflowid"));
				if (!"".equals(createrid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ createrid);
					if (rs1.next()) {
						creater_loginid = Util.null2String(rs1.getString("loginid"));
					}
					if (!"".equals(workflowid)) {
						rs2.executeSql("select * from workflow_base where id="
								+ workflowid);
						if (rs2.next()) {
							workflowname = Util.null2String(rs2.getString("workflowname"));
						}
					}
				}
				flowtitle = Util.null2String(rs.getString("requestname"));
				createdatetime = Util.null2String(rs.getString("createdate")
						+ " " + rs.getString("createtime"));
			}
			// 查询操作记录
			RecordSet rs3 = new RecordSet();

			String sql2 = "select * from workflow_currentoperator where requestid ="
					+ requestid + " and id="+id +" order by id";
			rs3.executeSql(sql2);
			while(rs3.next()){
				String receiverid = Util.null2String(rs3.getString("userid"));
				String receiver_loginid = "";
				if (!"".equals(receiverid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ receiverid);
					if (rs1.next()) {
						receiver_loginid = Util.null2String(rs1.getString("loginid"));
					}
				}

				String node_id = Util.null2String(rs3.getString("nodeid"));
				String flownodename = "";
				rs1
						.executeSql("select nodename from workflow_nodebase where id= "
								+ node_id);
				if (rs1.next()) {
					flownodename = Util.null2String(rs1.getString("nodename"));
				}
				String receivedatetime = Util.null2String(rs3.getString("receivedate")
						+ " " + rs3.getString("receivetime"));

				String pcurl = getWf_url(receiverid, requestid);

				// String appurl = Util.null2String("&loginid=" +
				// receiver_loginid +
				// "&url=/mobile/plugin/1/view.jsp?detailid="+requestid);
				String timestamp=(new Date().getTime())+"";

				String appurl =  "/client.do?method=toDetail&loginid="+receiver_loginid+"&timestamp="+timestamp+"&loginTokenFromThird="+hexSHA1("weaver"+receiver_loginid+timestamp)+"&url=/mobile/plugin/1/view.jsp?detailid="
				+ requestid;

				String isremark = Util.null2String(rs3.getString("isremark")); // 操作类型
				whitelist=receiver_loginid;
				for(String whiteuser:whitelist.split(",")){
					if(receiver_loginid.equals(whiteuser)){
						//receivedatetime = TimeUtil.getCurrentTimeString();
						String viewtype = "1";
						Map<String, String> dataMap = buildDataMap(syscode,
								requestid, flowtitle, workflowname, flownodename,
								pcurl, appurl, isremark, viewtype, creater_loginid,
								createdatetime, receiver_loginid, receivedatetime);
						writeLog("意见征询调用requestinfo接口的viewtype的值是" + viewtype);
						AnyType2AnyTypeMapEntry[] dataArray = OfsClient
								.buildDataArray(dataMap);
						AnyType2AnyTypeMapEntry[] resultArray = OfsClient
								.getClient().receiveRequestInfoByMap(dataArray);
						OfsClient.printResultArray(resultArray);
					}
				}
				
			}
			writeLog("===征询改待办--end" + " " + requestid + "===");
		}catch(Exception e){
			writeLog(e);
		}
	}
	public void NoReadToRead(String requestid,String userid){
		if(!isvalid(  requestid)){
			return;
		}
		writeLog("===未读改已读--start" + " " + requestid + "=== "+userid);
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		String whitelist = Util.null2String(Prop.getPropValue("bmd", "whitelist"));
		if (requestid == null || "".equals(requestid)) {
			return;
		}
		try {
			RecordSet rs = new RecordSet();
			RecordSet rs1 = new RecordSet();
			RecordSet rs2 = new RecordSet();
			// 查询流程基本信息
			String creater_loginid = "";
			String flowtitle = "";
			String createdatetime = "";
			String workflowname = "";
			String workflowid = "";
			String sql1 = "select * from workflow_requestbase where requestid="+ requestid;
			rs.executeSql(sql1);
			if (rs.next()) {
				String createrid = Util.null2String(rs.getString("creater"));
				workflowid = Util.null2String(rs.getString("workflowid"));
				if (!"".equals(createrid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ createrid);
					if (rs1.next()) {
						creater_loginid = Util.null2String(rs1.getString("loginid"));
					}
					if (!"".equals(workflowid)) {
						rs2.executeSql("select * from workflow_base where id=" + workflowid);
						if (rs2.next()) {
							workflowname = Util.null2String(rs2.getString("workflowname"));
						}
					}
				} 
				flowtitle = Util.null2String(rs.getString("requestname"));
				createdatetime = Util.null2String(rs.getString("createdate")
						+ " " + rs.getString("createtime"));
			}
			// 查询操作记录
			RecordSet rs3 = new RecordSet();

			String sql2 = "";
			
			if(rs.getDBType().equals("oracle")){
				sql2 = "select * from workflow_currentoperator where requestid =" + requestid + " and userid="+userid +" and NVL(operatedate,'') = '' order by id";
			}else{
				sql2 = "select * from workflow_currentoperator where  requestid =" + requestid + " and userid="+userid +" and ISNULL(operatedate,'') = '' order by id";
			}

			rs3.executeSql(sql2);
	
			while(rs3.next()){
				System.out.println("viewtype:"+rs3.getString("viewtype"));
				System.out.println("requestid:"+rs3.getString("requestid"));
				String receiverid = Util.null2String(rs3.getString("userid"));
				String receiver_loginid = "";
				if (!"".equals(receiverid)) {
					rs1.executeSql("select * from hrmresource where id="+ receiverid);
					if (rs1.next()) {
						receiver_loginid = Util.null2String(rs1.getString("loginid"));
					}
				}

				String node_id = Util.null2String(rs3.getString("nodeid"));
				String flownodename = "";
				rs1.executeSql("select nodename from workflow_nodebase where id= " + node_id);
				if (rs1.next()) {
					flownodename = Util.null2String(rs1.getString("nodename"));
				}
				String receivedatetime = Util.null2String(rs3.getString("receivedate")
						+ " " + rs3.getString("receivetime"));

				String pcurl = getWf_url(receiverid, requestid);

				// String appurl = Util.null2String("&loginid=" +
				// receiver_loginid +
				// "&url=/mobile/plugin/1/view.jsp?detailid="+requestid);
				String timestamp=(new Date().getTime())+"";

				String appurl =  "/client.do?method=toDetail&loginid="+receiver_loginid+"&timestamp="+timestamp+"&loginTokenFromThird="+hexSHA1("weaver"+receiver_loginid+timestamp)+"&url=/mobile/plugin/1/view.jsp?detailid="
				+ requestid;

				String isremark = Util.null2String(rs3.getString("isremark")); // 操作类型
				whitelist=receiver_loginid;
				for(String whiteuser:whitelist.split(",")){
					if(receiver_loginid.equals(whiteuser)){
						String viewtype = "1";
						if(!isremark.equals("2")&&!isremark.equals("4")){
							isremark="0";
						}
						writeLog("接收者:" + receiver_loginid + "isremark状态:"
								+ isremark);
						Map<String, String> dataMap = buildDataMap(syscode,
								requestid, flowtitle, workflowname, flownodename,
								pcurl, appurl, isremark, viewtype, creater_loginid,
								createdatetime, receiver_loginid, receivedatetime);
						writeLog("未读改已读调用requestinfo接口的viewtype的值是" + viewtype);
						AnyType2AnyTypeMapEntry[] dataArray = OfsClient
								.buildDataArray(dataMap);
						AnyType2AnyTypeMapEntry[] resultArray = OfsClient
								.getClient().receiveRequestInfoByMap(dataArray);
						OfsClient.printResultArray(resultArray);
					}
				}
			
			}
			writeLog("===未读改已读--end" + " " + requestid + "===");
		}catch(Exception e){
			writeLog(e);
		}
		
	}
	/**
	 * 操作统一待办库(根据userid删除流程)
	 * 
	 * @param requestid
	 */
	public void deleteoonlyUserToDo(String requestid,String userid) {
		if(!isvalid(  requestid)){
			return;
		}
		writeLog("===删除用户流程传输统一代办库---start" + " " + requestid + "===");
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		try {
			RecordSet rs1 = new RecordSet();
			// 调用删除接口先删除待办库这条requestid的所有记录
			rs1.executeSql("select * from hrmresource where id="+userid);
			rs1.next();
			String receiver = rs1.getString("loginid");
			Map<String, String> dataMap1 = buildUserDataMap(syscode, requestid,receiver);
			AnyType2AnyTypeMapEntry[] dataArray1 = OfsClient
					.buildDataArray(dataMap1);
			AnyType2AnyTypeMapEntry[] resultArray1 = OfsClient.getClient()
					.deleteUserRequestInfoByMap(dataArray1);
			OfsClient.printResultArray(resultArray1);
		
		

		}catch(Exception e){
			writeLog(e);
		}

		writeLog("===删除用户流程传输统一代办库---end" + " " + requestid + "===");
	}
	

	/**
	 * 操作统一待办库(根据userid删除流程)
	 * 
	 * @param requestid
	 */
	public void deleteUserToDo(String requestid,String userid) {
		if(!isvalid(  requestid)){
			return;
		}
		writeLog("===删除用户流程传输统一代办库---start" + " " + requestid + "===userid===="+userid);
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		String whitelist = Util.null2String(Prop.getPropValue("bmd", "whitelist"));
		try {
			RecordSet rs = new RecordSet();
			RecordSet rs1 = new RecordSet();
			RecordSet rs2 = new RecordSet();
			// 查询流程基本信息
			String creater_loginid = "";
			String flowtitle = "";
			String createdatetime = "";
			String workflowname = "";
			String workflowid = "";
			String sql1 = "select * from workflow_requestbase where requestid="
					+ requestid;
			rs.executeSql(sql1);
			if (rs.next()) {
				String createrid = Util.null2String(rs.getString("creater"));
				workflowid = Util.null2String(rs.getString("workflowid"));
				if (!"".equals(createrid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ createrid);
					if (rs1.next()) {
						creater_loginid = Util.null2String(rs1.getString("loginid"));
					}
					if (!"".equals(workflowid)) {
						rs2.executeSql("select * from workflow_base where id="
								+ workflowid);
						if (rs2.next()) {
							workflowname = Util.null2String(rs2
									.getString("workflowname"));
						}
					}
				}
				flowtitle = Util.null2String(rs.getString("requestname"));
				createdatetime = Util.null2String(rs.getString("createdate")
						+ " " + rs.getString("createtime"));
			}
			// 调用删除接口先删除待办库这条requestid的所有记录
			rs1.executeSql("select * from hrmresource where id="+userid);
			rs1.next();
			String receiver = rs1.getString("loginid");
			whitelist=receiver;
			for(String whiteuser:whitelist.split(",")){
				if(receiver.equals(whiteuser)){
					Map<String, String> dataMap1 = buildUserDataMap(syscode, requestid,receiver);
					AnyType2AnyTypeMapEntry[] dataArray1 = OfsClient
							.buildDataArray(dataMap1);
					AnyType2AnyTypeMapEntry[] resultArray1 = OfsClient.getClient()
							.deleteUserRequestInfoByMap(dataArray1);
					OfsClient.printResultArray(resultArray1);
				}
			}
		

			// 查询操作记录,将currentoperator表中剩余的记录推到待办库中
			RecordSet rs3 = new RecordSet();
			String sql2 = "select * from workflow_currentoperator where requestid ="
					+ requestid + "  and userid="+userid+" and islasttimes=1 order by nodeid";
			rs3.executeSql(sql2);
			while (rs3.next()) {
				String receiverid = Util.null2String(rs3.getString("userid"));
				String receiver_loginid = "";
				if (!"".equals(receiverid)) {
					rs1.executeSql("select * from hrmresource where id=" + receiverid);
					if (rs1.next()) {
						receiver_loginid = Util.null2String(rs1.getString("loginid"));
					}
				}

				String node_id = Util.null2String(rs3.getString("nodeid"));
				String flownodename = "";
				rs1.executeSql("select nodename from workflow_nodebase where id= " + node_id);
				if (rs1.next()) {
					flownodename = Util.null2String(rs1.getString("nodename"));
				}
				String receivedatetime = Util.null2String(rs3.getString("receivedate")
						+ " " + rs3.getString("receivetime"));
				

				String pcurl = getWf_url(receiverid, requestid);

				String timestamp=(new Date().getTime())+"";

				String appurl =  "/client.do?method=toDetail&loginid="+receiver_loginid+"&timestamp="+timestamp+"&loginTokenFromThird="+hexSHA1("weaver"+receiver_loginid+timestamp)+"&url=/mobile/plugin/1/view.jsp?detailid="
				+ requestid;

				String isremark = Util.null2String(rs3.getString("isremark")); // 操作类型
				whitelist=receiver_loginid;
				for(String whiteuser:whitelist.split(",")){
					if(receiver_loginid.equals(whiteuser)){
						if ("0".equals(isremark)) {
							// 待办事宜
							receivedatetime = TimeUtil.getCurrentTimeString();
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveTodoRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);
						}
						if ("1".equals(isremark) || "8".equals(isremark)
								|| "9".equals(isremark)) {
							// 待办事宜 往统一待办库发待办消息
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveTodoRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);

						} else if ("2".equals(isremark)) {
							writeLog("已办参数:" + syscode + " " + requestid + " "
									+ flowtitle + " " + workflowname + " "
									+ flownodename + " " + receiver_loginid);
							String viewtype = "1";
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, isremark, viewtype, creater_loginid,
									createdatetime, receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveRequestInfoByMap(dataArray);
							OfsClient.printResultArray(resultArray);

						}
						
					}
				}
			
			}
		} catch (Exception e) {
			writeLog(e);
		}

		writeLog("===删除用户流程传输统一代办库---end" + " " + requestid + "===");
	}
	
	
	/**
	 * 操作统一待办库(删除流程)
	 * 
	 * @param requestid
	 */
	public void deleteToDo(String requestid) {
		if(!isvalid(  requestid)){
			return;
		}
		writeLog("===删除流程传输统一代办库---start" + " " + requestid + "===");
		String syscode = Util.null2String(Prop.getPropValue("HYJK_TOdoConfig", "syscode"));
		String whitelist = Util.null2String(Prop.getPropValue("bmd", "whitelist"));
		try {
			RecordSet rs = new RecordSet();
			RecordSet rs1 = new RecordSet();
			RecordSet rs2 = new RecordSet();
			// 查询流程基本信息
			String creater_loginid = "";
			String flowtitle = "";
			String createdatetime = "";
			String workflowname = "";
			String workflowid = "";
			String sql1 = "select * from workflow_requestbase where requestid="
					+ requestid;
			rs.executeSql(sql1);
			if (rs.next()) {
				String createrid = Util.null2String(rs.getString("creater"));
				workflowid = Util.null2String(rs.getString("workflowid"));
				if (!"".equals(createrid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ createrid);
					if (rs1.next()) {
						creater_loginid = Util.null2String(rs1.getString("loginid"));
					}
					if (!"".equals(workflowid)) {
						rs2.executeSql("select * from workflow_base where id=" + workflowid);
						if (rs2.next()) {
							workflowname = Util.null2String(rs2
									.getString("workflowname"));
						}
					}
				}
				flowtitle = Util.null2String(rs.getString("requestname"));
				createdatetime = Util.null2String(rs.getString("createdate")
						+ " " + rs.getString("createtime"));
			}
			// 调用删除接口先删除待办库这条requestid的所有记录

			Map<String, String> dataMap1 = buildDataMap(syscode, requestid);
			AnyType2AnyTypeMapEntry[] dataArray1 = OfsClient
					.buildDataArray(dataMap1);
			AnyType2AnyTypeMapEntry[] resultArray1 = OfsClient.getClient()
					.deleteRequestInfoByMap(dataArray1);
			OfsClient.printResultArray(resultArray1);

			// 查询操作记录,将currentoperator表中剩余的记录推到待办库中
			RecordSet rs3 = new RecordSet();
			String sql2 = "select * from workflow_currentoperator where requestid ="
					+ requestid + "  and islasttimes=1 order by nodeid";
			rs3.executeSql(sql2);
			while (rs3.next()) {
				String receiverid = Util.null2String(rs3.getString("userid"));
				String receiver_loginid = "";
				if (!"".equals(receiverid)) {
					rs1.executeSql("select * from hrmresource where id="
							+ receiverid);
					if (rs1.next()) {
						receiver_loginid = Util.null2String(rs1.getString("loginid"));
					}
				}

				String node_id = Util.null2String(rs3.getString("nodeid"));
				String flownodename = "";
				rs1.executeSql("select nodename from workflow_nodebase where id= "+ node_id);
				if (rs1.next()) {
					flownodename = Util.null2String(rs1.getString("nodename"));
				}
				String receivedatetime = Util.null2String(rs3
						.getString("receivedate")
						+ " " + rs3.getString("receivetime"));
				

				String pcurl = getWf_url(receiverid, requestid);
				String timestamp=(new Date().getTime())+"";

				String appurl =  "/client.do?method=toDetail&loginid="+receiver_loginid+"&timestamp="+timestamp+"&loginTokenFromThird="+hexSHA1("weaver"+receiver_loginid+timestamp)+"&url=/mobile/plugin/1/view.jsp?detailid="
				+ requestid;

				String isremark = Util.null2String(rs3.getString("isremark")); // 操作类型
//				String viewtype = "1";
				/*
				writeLog("接收者:" + receiver_loginid + "isremark状态:" + isremark);
				Map<String, String> dataMap = buildDataMap(syscode, requestid,
						flowtitle, workflowname, flownodename, pcurl, appurl,
						isremark, viewtype, creater_loginid, createdatetime,
						receiver_loginid, receivedatetime);
				AnyType2AnyTypeMapEntry[] dataArray = OfsClient
						.buildDataArray(dataMap);
				AnyType2AnyTypeMapEntry[] resultArray = OfsClient.getClient()
						.receiveRequestInfoByMap(dataArray);
				OfsClient.printResultArray(resultArray);*/
				whitelist = receiver_loginid;
				for(String whiteuser:whitelist.split(",")){
					if(receiver_loginid.equals(whiteuser)){
						if ("0".equals(isremark)) {
							// 待办事宜
							receivedatetime = TimeUtil.getCurrentTimeString();
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveTodoRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);
						}
						if ("1".equals(isremark) || "8".equals(isremark)
								|| "9".equals(isremark)) {
							// 待办事宜 往统一待办库发待办消息
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, creater_loginid, createdatetime,
									receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveTodoRequestByMap(dataArray);
							OfsClient.printResultArray(resultArray);

						} else if ("2".equals(isremark)) {
							writeLog("已办参数:" + syscode + " " + requestid + " "
									+ flowtitle + " " + workflowname + " "
									+ flownodename + " " + receiver_loginid);
							String viewtype = "1";
							// 已办事宜 往统一待办库发已办消息
							/*
							 * Map<String,String> dataMap = buildDataMap( syscode,
							 * requestid, flowtitle, workflowname, flownodename,
							 * receiver_loginid ); AnyType2AnyTypeMapEntry[] dataArray =
							 * OfsClient.buildDataArray(dataMap);
							 * AnyType2AnyTypeMapEntry[] resultArray =
							 * OfsClient.getClient().processDoneRequestByMap(dataArray);
							 * OfsClient.printResultArray(resultArray);
							 */
							Map<String, String> dataMap = buildDataMap(syscode,
									requestid, flowtitle, workflowname, flownodename,
									pcurl, appurl, isremark, viewtype, creater_loginid,
									createdatetime, receiver_loginid, receivedatetime);
							AnyType2AnyTypeMapEntry[] dataArray = OfsClient
									.buildDataArray(dataMap);
							AnyType2AnyTypeMapEntry[] resultArray = OfsClient
									.getClient().receiveRequestInfoByMap(dataArray);
							OfsClient.printResultArray(resultArray);

						}
					}
				}
			
			}
		} catch (Exception e) {
			writeLog(e);
		}

		writeLog("===删除流程传输统一代办库---end" + " " + requestid + "===");
	}

	public Map<String, String> buildDataMap(String syscode, String flowid,
			String requestname, String workflowname, String nodename,
			String pcurl, String appurl, String creator, String createdatetime,

			String receiver, String receivedatetime) {
		
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("syscode", syscode);
		dataMap.put("flowid", flowid);
		dataMap.put("requestname", requestname);
		dataMap.put("workflowname", workflowname);
		dataMap.put("nodename", nodename);
		dataMap.put("pcurl", pcurl);
		dataMap.put("appurl", appurl);
		dataMap.put("creator", creator);
		dataMap.put("createdatetime", createdatetime);
		dataMap.put("receiver", receiver);
		dataMap.put("receivedatetime", receivedatetime);

		return dataMap;
	}

	public Map<String, String> buildDataMap(String syscode, String flowid) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("syscode", syscode);
		dataMap.put("flowid", flowid);
		return dataMap;
	}
	public Map<String, String> buildUserDataMap(String syscode, String flowid,String userid) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("syscode", syscode);
		dataMap.put("flowid", flowid);
		dataMap.put("userid", userid);
		return dataMap;
	}

	public Map<String, String> buildDataMap(String syscode, String flowid,
			String requestname, String workflowname, String nodename,
			String receiver) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("syscode", syscode);
		dataMap.put("flowid", flowid);
		dataMap.put("requestname", requestname);
		dataMap.put("workflowname", workflowname);
		dataMap.put("nodename", nodename);
		dataMap.put("receiver", receiver);

		return dataMap;
	}

	public Map<String, String> buildDataMap(String syscode, String flowid,
			String requestname, String workflowname, String nodename,
			String pcurl, String appurl, String isremark, String viewtype,
			String creator, String createdatetime, String receiver,
			String receivedatetime) {
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("syscode", syscode);
		dataMap.put("flowid", flowid);
		dataMap.put("requestname", requestname);
		dataMap.put("workflowname", workflowname);
		dataMap.put("nodename", nodename);
		dataMap.put("pcurl", pcurl);
		dataMap.put("appurl", appurl);
		dataMap.put("isremark", isremark);
		dataMap.put("viewtype", viewtype);
		dataMap.put("creator", creator);
		dataMap.put("createdatetime", createdatetime);
		dataMap.put("receiver", receiver);
		dataMap.put("receivedatetime", receivedatetime);

		return dataMap;
	}

	private String getWf_url(String id, String requestid) {
		RecordSet rs = new RecordSet();
		String tempurl = "";
	//	String oaAddress = "";
		String loginPage = "login/VerifyRtxLogin.jsp";
		String para = "";
		String gotoPage = "";
		String password = "";
		String loginid = "";

		gotoPage = "workflow/request/ViewRequest.jsp";

		rs.executeSql("select password,loginid from hrmresource where id="+ id);
		if (rs.next()) {
			password = Util.null2String(rs.getString("password"));
			loginid = Util.null2String(rs.getString("loginid"));
		}
		para = "/" + gotoPage + "?requestid=" + requestid + "#" + loginid + "#" + password;

		try {
			para = PoppupRemindInfoUtil.encrypt(para);
		} catch (Exception e) {
			writeLog(e);
		}
		tempurl = "/" + loginPage + "?para=" + para;
		return tempurl;
	}
	public static String hexSHA1(String value) {

		try {
			MessageDigest md = MessageDigest.getInstance("SHA-1");
			md.update(value.getBytes("utf-8"));
			byte[] digest = md.digest();
			return byteToHexString(digest);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}


		public static String byteToHexString(byte[] bytes) {
			return String.valueOf(Hex.encodeHex(bytes));
		}
}
