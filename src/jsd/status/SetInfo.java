package jsd.status;

import weaver.conn.RecordSet;
import weaver.general.BaseBean;

/**
 * @author 作者  张瑞坤
 * @version 创建时间：2018-11-14 下午6:46:17
 * 流程回传erp  数据状态写入
 */
public class SetInfo {
	
	public void setReturnInfo(String requestid,String type, String remark){
		RecordSet rs = new RecordSet();
		BaseBean log = new BaseBean();
		remark = remark.replaceAll("'", "''").replaceAll("\\\\", "");
		String sql = "insert uf_lchczt(rid,type,remark,crsj) values('"+requestid+"','"+type+"','"+remark+"',CONVERT(varchar,GETDATE(),120))";
		log.writeLog("sql------------"+sql);
		rs.executeSql(sql);		
	}
	
//	public static void main(String[] args) {
//		String aa = "'aaaaa\\ajjddddddssssll'll";
//		System.out.println(aa.replaceAll("'", "''").replaceAll("\\\\", "5"));
//		
//	}
//	

}
