package goodbaby.finance;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.dc.engine.core.f;

import weaver.conn.RecordSet;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;

public class FreezeBackMoneyForJJYC implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String sqr = "";
		String zje = "";
		String whbyj = "";
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
			zje = Util.null2String(rs.getString("zje")).replaceAll(",", "");
			whbyj = Util.null2String(rs.getString("whbyj")).replaceAll(",", "");
		}
		String hke="0";
		if(Util.getFloatValue(zje,0)>Util.getFloatValue(whbyj,0)){
			hke="-"+String.valueOf(Util.getFloatValue(whbyj,0));
		}else{
			hke="-"+String.valueOf(Util.getFloatValue(zje,0));
		}
		sql="insert into uf_borrow_back_infp(rqid,sqr,rq,type,je,jklx,status) " +
				"values('"+requestid+"','"+sqr+"','"+nowDate+"','1','"+hke+"','1','0')";
		rs.execute(sql);
		return SUCCESS;
	}

}
