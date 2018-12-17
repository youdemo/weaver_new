package goodbaby.contract;

import goodbaby.util.GetUtil;
import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 
 * @author 张瑞坤
 * 合同编号和名称
 *0626
 */
public class contractBM implements Action{
	GetUtil gu =new GetUtil();
	@Override
	public String execute(RequestInfo info) {
		String requestid = info.getRequestid();
		String tablename = info.getRequestManager().getBillTableName();
		RecordSet rs = new RecordSet(); 
		RecordSet res = new RecordSet(); 
		String sql = "select HTLX,GYSBH,HTBH,WLBH,TYWLJGK from "+tablename+" where requestid ='"+requestid+"'";
		String HTLX="";
		String GYSBH = "";
		String HTBH = "";
		String WLBH = "";
		String LS = "";
		String WLMC = "";
		//String wlmc1 = "";
		rs.executeSql(sql);
		if(rs.next()){
			HTLX = Util.null2String(rs.getString("HTLX"));
			GYSBH = Util.null2String(rs.getString("GYSBH"));
			WLBH = Util.null2String(rs.getString("WLBH"));
			WLMC = Util.null2String(rs.getString("TYWLJGK"));
		}
//		sql = "select WLMC from uf_materialDatas where id =(select WLNAME1  from uf_inquiryForm where id = '"+WLMC+"')";
//		rs.executeSql(sql);
//		if(rs.next()){
//			wlmc1 = Util.null2String(rs.getString("WLMC"));;
//			
//		}
		
		GetUtil gu = new GetUtil();
		String nowday = gu.getNowDate();
		if(HTLX.equals("0")){//框架   001
			String str = "select top 1 * from uf_contract where nyr = '"+nowday+"' and bhlx=0 order by id desc  ";
			rs.executeSql(str);
			if(rs.next()){
				String ls = Util.null2String(rs.getString("lsh"));
				int aaa = Util.getIntValue(ls, 0);
				int bbb = aaa+1;
				if(bbb<10){
					LS ="00"+bbb;
				}else if(bbb<100){
					LS ="0"+bbb;
				}
				HTBH = GYSBH+nowday+LS;
				str = "insert uf_contract(bhlx,rid,gysbh,lsh,nyr) values('0','"+requestid+"','"+GYSBH+"','"+LS+"','"+nowday+"') ";
				res.executeSql(str);
				str ="update "+tablename +" set HTBH ='"+HTBH+"',HTMC = '采购框架合同' where requestid = '"+requestid+"'";
				res.executeSql(str);	
			}else{
				HTBH = GYSBH+nowday+"001";
				str = "insert uf_contract(bhlx,rid,gysbh,lsh,nyr) values('0','"+requestid+"','"+GYSBH+"','001','"+nowday+"') ";
				res.executeSql(str);
				str ="update "+tablename +" set HTBH ='"+HTBH+"',HTMC = '采购框架合同' where requestid = '"+requestid+"'";
				res.executeSql(str);
			}	
		}else {//0726  修改
			String str = "select top 1 * from uf_contract where nyr = '"+nowday+"' and bhlx=1 order by id desc  ";
			rs.executeSql(str);
			if(rs.next()){
				String ls = Util.null2String(rs.getString("lsh"));
				int aaa = Util.getIntValue(ls, 0);
				int bbb = aaa+1;
				if(bbb<10){
					LS ="0"+bbb;
				}
				HTBH = WLBH+nowday+LS;
				str = "insert uf_contract(bhlx,rid,wlbh,lsh,nyr) values('1','"+requestid+"','"+WLBH+"','"+LS+"','"+nowday+"') ";
				res.executeSql(str);
				str ="update "+tablename +" set HTBH ='"+HTBH+"'  where requestid = '"+requestid+"'";
				res.executeSql(str);	
			}else{
				HTBH = WLBH+nowday+"01";
				str = "insert uf_contract(bhlx,rid,wlbh,lsh,nyr) values('1','"+requestid+"','"+WLBH+"','01','"+nowday+"') ";
				res.executeSql(str);
				str ="update "+tablename +" set HTBH ='"+HTBH+"'  where requestid = '"+requestid+"'";
				res.executeSql(str);
			}		
			
			
		}
		
		return SUCCESS;
	}

}
