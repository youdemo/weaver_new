package opple.doc;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 在套红节点生成发文字号
 * @author 汤健勇 2018-04-19 v1
 *
 */
public class CreateDocSeqno implements Action {

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String tableName = "";
		String yfsj = "";//印发时间
		String fwzh = "";//发文字号
		String gwlb1 = "";//公文类别
		String bmcjxzk = "";//发文部门
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from " + tableName + " where requestid= " + requestid;
		rs.executeSql(sql);
		if(rs.next()){
			yfsj = Util.null2String(rs.getString("yfsj"));
			fwzh = Util.null2String(rs.getString("fwzh"));
			gwlb1 = Util.null2String(rs.getString("gwlb1"));
			bmcjxzk = Util.null2String(rs.getString("bmcjxzk"));
		}
		
		if("".equals(fwzh)){
			if("".equals(yfsj)){
				yfsj=nowDate;
			}
			String year = yfsj.substring(0,4);
			if("0".equals(gwlb1)){
				fwzh = "欧普字["+year+"]"+getSeqNo(gwlb1, "-1", year)+"号";
			}else if("1".equals(gwlb1)){
				String bmjc=getbmjc(bmcjxzk);
				fwzh = "欧普"+bmjc+"["+year+"]"+getSeqNo(gwlb1,bmcjxzk, year)+"号";
			}
			sql="update "+tableName+" set fwzh='"+fwzh+"' where requestid= " + requestid;
			rs.executeSql(sql);
		}
		
		
		return SUCCESS;
	}

	public String getbmjc(String id){
		RecordSet rs = new RecordSet();
		String bmjc = "";
		String sql = "select * from uf_bmjc where id="+id;
		rs.executeSql(sql);
		if(rs.next()){
			bmjc = Util.null2String(rs.getString("bmjc"));
		}
		return bmjc;
	}
	
	public String getSeqNo(String gwlb,String bmid,String year ){
		InsertUtil iu = new InsertUtil();
		RecordSet rs = new RecordSet();
		int nextNo=1;
		int count=0;
		String sql="select count(1) as count from uf_doc_seq where gwlb='"+gwlb+"' and bmid='"+bmid+"' and year='"+year+"'";
		rs.executeSql(sql);
		if(rs.next()){
			count = rs.getInt("count");
		}
		
		if(count >0){
			sql = "select seqno+1 as seqno from uf_doc_seq where gwlb='"+gwlb+"' and bmid='"+bmid+"' and year='"+year+"'";
			rs.executeSql(sql);
			if(rs.next()){
				nextNo = rs.getInt("seqno");
			}
			
			sql = "update uf_doc_seq set seqno=seqno+1 where gwlb='"+gwlb+"' and bmid='"+bmid+"' and year='"+year+"'";
			rs.executeSql(sql);
		}else{
			Map<String,String> mapStr = new HashMap<String,String>();
			mapStr.put("gwlb", gwlb);
			mapStr.put("bmid", bmid);
			mapStr.put("year", year);
			mapStr.put("seqno", "1");
			
			iu.insert(mapStr, "uf_doc_seq");
		}
		
		return String.valueOf(nextNo);
	}
}
