package goodbaby.finance;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dc.engine.core.f;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class FreezeBackMoneyForHK implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String sqr = "";
		String hklx = "";
		String hkje = "";
		String tableName = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql="select * from "+tableName+" where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			sqr = Util.null2String(rs.getString("sqr"));
			hklx = Util.null2String(rs.getString("hklx"));
			hkje = Util.null2String(rs.getString("hkje")).replaceAll(",", "");
		}
		String hke="0";
		if(Util.getFloatValue(hkje,0)>0){
			hke="-"+String.valueOf(Util.getFloatValue(hkje,0));
		}else{
			hke="0";
		}
		sql="insert into uf_borrow_back_infp(rqid,sqr,rq,type,je,jklx,status) " +
				"values('"+requestid+"','"+sqr+"','"+nowDate+"','1','"+hke+"','"+hklx+"','0')";
		rs.execute(sql);
		return SUCCESS;
	}

}
