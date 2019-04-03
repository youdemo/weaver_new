package lx.flow;

import java.text.SimpleDateFormat;
import java.util.Date;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 *   接口生成流程编号
 * @author tangjy 2019/1/11
 *
 */
public class CreateWorkflowMark implements Action{

	@Override
	public String execute(RequestInfo info) {
		RecordSet rs = new RecordSet();
		String workflowID = info.getWorkflowid();
		String requestid = info.getRequestid();
		String tableName = "";
		String bhbs = "";//编号标识
		String bhzdm = "";//编号字段名
		String qz = "";//前缀
		String lsws = "";//流水位数
		String sfqynf = "";//是否启用年份
		String lcbh = "";//流程编号
		SimpleDateFormat sf = new SimpleDateFormat("yyyy");
		String year = sf.format(new Date());
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select * from uf_flow_mark_mt where wfid='"+workflowID+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			bhbs = Util.null2String(rs.getString("bhbs"));
			bhzdm = Util.null2String(rs.getString("bhzdm"));
			qz = Util.null2String(rs.getString("qz"));
			lsws = Util.null2String(rs.getString("lsws"));
			sfqynf = Util.null2String(rs.getString("sfqynf"));
		}
		if(!"".equals(bhbs)) {
			sql = "select "+bhzdm+" as lcbh from "+tableName+" where requestid="+requestid;
			rs.executeSql(sql);
			if(rs.next()) {
				lcbh = Util.null2String(rs.getString("lcbh"));
			}
			if("".equals(lcbh)) {
				if("0".equals(sfqynf)) {
					lcbh = qz+year+getFlowNumYear(year, bhbs,Util.getIntValue(lsws, 5));
				}else {
					lcbh = qz+getFlowNum(bhbs,Util.getIntValue(lsws, 5));
				}
				sql = "update "+tableName+" set "+bhzdm+" = '"+lcbh+"' where requestid="+requestid;
				rs.executeSql(sql);
			}
		}
		return SUCCESS;
	}
	public  String getFlowNum(String bhbs, int ls) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
        RecordSet rs = new RecordSet();
        String modeid = getModeId("uf_flow_mark_seq");
        int nextNo = 1;
        int count = 0;
        String sql = "select count(1) as count from uf_flow_mark_seq where bhbs='"+bhbs+"'";
        rs.executeSql(sql);
        if (rs.next()) {
            count = rs.getInt("count");
        }

        if (count > 0) {
            sql = "select seqno+1 as seqnum from uf_flow_mark_seq where  bhbs='"+bhbs+"'";
            rs.executeSql(sql);
            if (rs.next()) {
                nextNo = rs.getInt("seqnum");
            }

            sql = "update uf_flow_mark_seq set seqno=seqno+1 where  bhbs='"+bhbs+"'";
            rs.executeSql(sql);
        } else {
        	sql="insert into uf_flow_mark_seq (bhbs,seqno,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid)"
					+ " values('"+bhbs+ "',1,'"+now+"','1','0','"+modeid+"')";
			rs.executeSql(sql);	
			int billid = 0;
			sql = "select id from uf_flow_mark_seq where bhbs='"+bhbs+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				billid = rs.getInt("id");
			}
			if(billid > 0) {
				ModeRightInfo ModeRightInfo = new ModeRightInfo();
				ModeRightInfo.editModeDataShare(1,
						Util.getIntValue(modeid),
						billid);
			}
        }
       

        return getStrNum(nextNo, ls);
    }
	public  String getFlowNumYear(String year, String bhbs, int ls) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String now = sf.format(new Date());
        RecordSet rs = new RecordSet();
        String modeid = getModeId("uf_flow_mark_seq");
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
        	sql="insert into uf_flow_mark_seq (bhbs,nf,seqno,modedatacreatedate,modedatacreater,modedatacreatertype,formmodeid)"
					+ " values('"+bhbs+ "','"+year+"',1,'"+now+"','1','0','"+modeid+"')";
			rs.executeSql(sql);	
			int billid = 0;
			sql = "select id from uf_flow_mark_seq where bhbs='"+bhbs+"'";
			rs.executeSql(sql);
			if(rs.next()) {
				billid = rs.getInt("id");
			}
			if(billid > 0) {
				ModeRightInfo ModeRightInfo = new ModeRightInfo();
				ModeRightInfo.editModeDataShare(1,
						Util.getIntValue(modeid),
						billid);
			}
        }
       

        return getStrNum(nextNo, ls);
    }
	
	public String getModeId(String tableName) {
		RecordSet rs = new RecordSet();
		String formid = "";
		String modeid = "";
		String sql = "select id from workflow_bill where tablename='"
				+ tableName + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			formid = Util.null2String(rs.getString("id"));
		}
		sql = "select id from modeinfo where  formid=" + formid;
		rs.executeSql(sql);
		if (rs.next()) {
			modeid = Util.null2String(rs.getString("id"));
		}
		return modeid;
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
