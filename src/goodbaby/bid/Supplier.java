package goodbaby.bid;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class Supplier implements Action {

	@Override
	public String execute(RequestInfo info) {
		
		BaseBean log =new BaseBean();
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs =new RecordSet();
		RecordSet rs1 =new RecordSet();
		RecordSet res =new RecordSet();
		String jmgys1 = "";//建模供应商 
		String lcid = "";//流程id
		String sql = "select * from "+tablename+" where requestid = '"+requestid+"'";
		rs.executeSql(sql);
		while(rs.next()){
			jmgys1 = Util.null2String(rs.getString("ztbgysmc"));//建模
			lcid = Util.null2String(rs.getString("id"));//
		}
		if(jmgys1.length()>0){
			String[] kh1= jmgys1.split(",");
			String str = "";
			String xtkh= "";
			for(int i =0;i<kh1.length;i++){
				str="select id,name from crm_customerinfo where name =(select GYSMC from uf_suppmessForm where id ='"+kh1[i]
						+"') and crmcode =(select GYSBM from uf_suppmessForm where id ='"+kh1[i]+"')";
				rs1.executeSql(str);
				if(rs1.next()){
					xtkh = Util.null2String(rs1.getString("id"));//客户
					//xtkhname = Util.null2String(rs1.getString("name"));//客户
				}
				str = "insert into formtable_main_200_dt2(mainid,jmkh,gysmc,gysmc2) values ('"+lcid+"','"+kh1[i]+"','"+xtkh+"','"+kh1[i]+"')";
				log.writeLog(str);
				rs1.executeSql(str);
			}
			
		}
		
		return SUCCESS;
	}

}
