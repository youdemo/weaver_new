package zx.purchase;

import weaver.conn.RecordSet;

public class CheckPRDetail {
	public String getCanCheck(String checkid){
		 String id=checkid.split("_")[0];
		 String rqid=checkid.split("_")[1];
		 RecordSet rs = new RecordSet();
		 int count = 0;
		 String sql="select count(b.id) as count from formtable_main_19 a,formtable_main_19_dt2 b where a.id=b.mainid and b.prdtid='"+id+"' and a.requestid not in("+rqid+")";
		 rs.executeSql(sql);
		 if(rs.next()){
			 count = rs.getInt("count");
		 }
		 if(count >0){
			 return "false";
		 }
        
		 return "true";
   }
}
