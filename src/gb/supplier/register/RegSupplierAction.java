package gb.supplier.register;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class RegSupplierAction implements Action{

	@Override
	public String execute(RequestInfo request){
		String requestid = request.getRequestid();
		String workflowid = request.getWorkflowid();
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		String sql = "";
		String tableName = "";
		sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowid + ")";
		
		rs.execute(sql);
		if(rs.next()){
			tableName = Util.null2String(rs.getString("tablename"));
		}
		
		if(!"".equals(tableName)){
			sql = "select * from " + tableName + " where requestid = " + requestid;
			log.writeLog("sql = " + sql);
			rs.executeSql(sql);
			if(rs.next()){
				// WLLB 分类字段 ; GYSXZ 供应商性质   ;  GYSBM 供应商编码
				String code = Util.null2String(rs.getString("WLLB"));
				String type = Util.null2String(rs.getString("GYSXZ"));
				// DZXX 电子邮箱    GYSMC
				String email = Util.null2String(rs.getString("DZXX"));
				String name = Util.null2String(rs.getString("GYSMC"));
				
				String typeVal = "";
				if("0".equals(type)){
					typeVal = "N";
				}else if("1".equals(type)){
					typeVal = "T";
				}else if("2".equals(type)){
					typeVal = "B";
				}
				String codeVal = "";
				// 物料主分类
				sql = "select MCODE from uf_NPP WHERE ID = " + code ;
				rs.executeSql(sql);
				if(rs.next()){
					codeVal = Util.null2String(rs.getString("MCODE"));
				}
				String codeRes = getSupplierNo(codeVal, typeVal, 4);
				
				sql = "update " + tableName + " set GYSBM = '" + codeRes + "' where requestid = " + requestid;
				log.writeLog("sql = " + sql);
				rs.executeSql(sql);
				
				String loginid = codeRes;
				// 内容写入系统客户
				sql = "insert  into CRM_CustomerInfo(name,language,engname,email,source,manager,type,status,"
					+" deleted,seclevel,PortalLoginid,PortalPassword,PortalStatus,createdate) "
					+" values('" + name + "',7,'" + name + "','" + email + "',1,1,1,2,0,0,'"
						+ loginid + "','123456',2,convert(varchar(10),getdate(),121))";
				log.writeLog("sql = " + sql);
				rs.executeSql(sql);
			}
		}
		
		
		return SUCCESS;
	}
 
	/**
	 * 生成流水
	 * @param code 
	 * @param type 
	 * @param num 流水号几位
	 * @return
	 */
	private String getSupplierNo(String code,String type,int num){
		if(code == null) code = "";
		if(type == null) type = "";
		int flagNum = 0;
		// 记录系统流水记录表   uf_supplierCodelog  ： code 、 type 、num
		String sql = "select num from uf_supplierCodelog where code = '" + code + "' and type = '" + type + "'";
		BaseBean log = new BaseBean();
		log.writeLog("sql = " + sql);
		RecordSet rs = new RecordSet();
		rs.executeSql(sql);
		if(rs.next()){
			flagNum = rs.getInt("num");
		}
		// 记录不存在 插入一条
		if(flagNum < 1){
			flagNum = 1;
			sql = "insert into uf_supplierCodelog(code,type,num) values('" + code + "','" + type + "',2)";
		}else{
			// 有记录的话，需要自增长1
			sql = "update uf_supplierCodelog set num = num + 1 where code = '" + code + "' and type = '" + type + "'";
		}
		log.writeLog("sql = " + sql);
		rs.executeSql(sql);
		
		String codeRes = code + type + String.format("%0" + num + "d", flagNum);
		
		return codeRes;
	}
}
