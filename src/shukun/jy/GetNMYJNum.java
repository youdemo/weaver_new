package shukun.jy;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;

public class GetNMYJNum {

	
	public  String getFlowNumYear(String year, String bhbs, int ls) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
        RecordSet rs = new RecordSet();
        int nextNo = 1;
        int count = 0;
        String sql = "select count(1) as count from uf_flow_mark_seq where nf='" + year + "'  and bhbs='"+bhbs+"'";
        rs.executeSql(sql);
        if (rs.next()) {
            count = rs.getInt("count");
        }

        if (count > 0) {
            sql = "select seqno+1 as seqnum from uf_flow_mark_seq where nf='" + year + "'  and bhbs='"+bhbs+"'";
            rs.executeSql(sql);
            if (rs.next()) {
                nextNo = rs.getInt("seqnum");
            }

            sql = "update uf_flow_mark_seq set seqno=seqno+1 where  nf='" + year + "'  and bhbs='"+bhbs+"'";
            rs.executeSql(sql);
        } else {
        	sql="insert into uf_flow_mark_seq (bhbs,nf,seqno)"
					+ " values('"+bhbs+ "','"+year+"',1)";
			rs.executeSql(sql);	
			
        }
       

        return getStrNum(nextNo, ls);
    }
	
	
	
	 public  String getStrNum(int num, int len) {
	        String buff = String.valueOf(num);
	        int max = len - buff.length();
	        for (int index = 0; index < max; index++) {
	            buff = "0" + buff;
	        }
	        return buff;
	    }

}
