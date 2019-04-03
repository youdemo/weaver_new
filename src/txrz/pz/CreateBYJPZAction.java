package txrz.pz;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.formmode.setup.ModeRightInfo;
import weaver.general.Util;
import weaver.interfaces.workflow.action.Action;
import weaver.soa.workflow.request.RequestInfo;
/**
 * 备用金流程生成凭证
 * @author tangj
 *
 */
public class CreateBYJPZAction implements Action{

	@Override
	public String execute(RequestInfo info) {
		String workflowID = info.getWorkflowid();// 获取工作流程Workflowid的值
		String requestid = info.getRequestid();
		RecordSet rs = new RecordSet();
		PzUtil pu = new PzUtil();
		InsertUtil iu = new InsertUtil();
		String modeid = pu.getModeId("uf_zjb");
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:MM:ss");
		SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy-MM-dd");
		String nowDate = dateFormate.format(new Date());
		String nowTime = sf.format(new Date());
		String tableName = "";
		String djbh = "";//单据编号
		String xj = "";//小计
		String qkr = "";//请款人
		String gh = "";//请款人工号
		String name = "";//请款人名
		String sybm = "";//所有部门
		String pzzy = ""; //凭证摘要
		String gsdm = "";//公司代码
		String jfkm = "1221.05";
		String dfkm = "1002.01.16";
		String requestnamenew = "";
		String account_name = "";
		String sql = " Select tablename From Workflow_bill Where id in ("
				+ " Select formid From workflow_base Where id= " + workflowID
				+ ")";
		rs.execute(sql);
		if (rs.next()) {
			tableName = Util.null2String(rs.getString("tablename"));
		}
		sql = "select requestnamenew from workflow_requestbase where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()) {
			requestnamenew = Util.null2String(rs.getString("requestnamenew"));
		}
		sql = "select djbh,xj,qkr,gh,(select lastname from hrmresource where id=a.qkr) as name,sybm,(select account_name from uf_gsb where id=a.gsb)  as account_name,gsdm from "+tableName+" a where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			djbh = Util.null2String(rs.getString("djbh"));
			xj = Util.null2String(rs.getString("xj"));
			gh = Util.null2String(rs.getString("gh"));
			name = Util.null2String(rs.getString("name"));
			sybm = Util.null2String(rs.getString("sybm"));
			account_name = Util.null2String(rs.getString("account_name"));
			gsdm = Util.null2String(rs.getString("gsdm"));
		}
		pzzy = "付"+sybm+"零用金";
		//借方
		String hsxmjf = pu.getFZHS(jfkm);
		Map<String, String> mapStr = new HashMap<String, String>();
		mapStr.put("xglcid", requestid);//相关流程id
		mapStr.put("PZZ", "AP");//凭证字
		mapStr.put("LSPZH", djbh);//系统临时凭证号
		mapStr.put("PZMC", requestnamenew);//凭证名称
		mapStr.put("PZMBID", workflowID);//凭证模板ID
		mapStr.put("CJSJ", nowTime);//创建时间
		mapStr.put("JDZTBH", gsdm);//金蝶账套编号
		mapStr.put("JDZTMC", account_name);//金蝶账套名称
		mapStr.put("modedatacreatedate", nowDate);
		mapStr.put("modedatacreater", "1");
		mapStr.put("modedatacreatertype", "0");
		mapStr.put("formmodeid", modeid);
		//mapStr.put("PZRQ", bs);//凭证日期
		//mapStr.put("ZXSJ", bs);//执行时间
		//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
		//mapStr.put("PZZT", bs);//凭证状态
		//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
		//mapStr.put("FHMS", bs);//返回描述			
		mapStr.put("lx", "0");//lx
		mapStr.put("gbdqbm", "");//国别/地区编码
		mapStr.put("gbdqmc", "");//国别/地区名称
		if("1".equals(hsxmjf)) {
			mapStr.put("zybm", gh);//职员编码
			mapStr.put("zymc", name);//职员名称
		}
		mapStr.put("bmbm", "");//部门编码
		mapStr.put("bmmc", "");//部门名称
		mapStr.put("gysbm", "");//供应商编码
		mapStr.put("gysmc", "");//供应商名称
		
		mapStr.put("KMBM", jfkm);//科目编码
		mapStr.put("KMMC", "其他应收款备用金");//科目名称
		mapStr.put("JFJE", xj);//借方金额
		//mapStr.put("DFJE", bs);//贷方金额
		mapStr.put("ZY", pzzy);//摘要		
		mapStr.put("FLH", "1");//分录号
		iu.insert(mapStr, "uf_zjb");
		String pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='0'";
		rs.executeSql(sql);
		if(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
		}
		if(!"".equals(pzid)){
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
				Integer.valueOf(pzid));
		}
		//贷方
		String hsxmdf = pu.getFZHS(dfkm);
		mapStr = new HashMap<String, String>();
		mapStr.put("xglcid", requestid);//相关流程id
		mapStr.put("PZZ", "AP");//凭证字
		mapStr.put("LSPZH", djbh);//系统临时凭证号
		mapStr.put("PZMC", requestnamenew);//凭证名称
		mapStr.put("PZMBID", workflowID);//凭证模板ID
		mapStr.put("CJSJ", nowTime);//创建时间
		mapStr.put("JDZTBH", gsdm);//金蝶账套编号
		mapStr.put("JDZTMC", account_name);//金蝶账套名称
		mapStr.put("modedatacreatedate", nowDate);
		mapStr.put("modedatacreater", "1");
		mapStr.put("modedatacreatertype", "0");
		mapStr.put("formmodeid", modeid);
		//mapStr.put("PZRQ", bs);//凭证日期
		//mapStr.put("ZXSJ", bs);//执行时间
		//mapStr.put("FHPZH", bs);//金蝶系统返回凭证号
		//mapStr.put("PZZT", bs);//凭证状态
		//mapStr.put("PZDRJGFHZ", bs);//凭证导入结果返回值
		//mapStr.put("FHMS", bs);//返回描述			
		mapStr.put("lx", "1");//lx
		mapStr.put("gbdqbm", "");//国别/地区编码
		mapStr.put("gbdqmc", "");//国别/地区名称
		if("1".equals(hsxmdf)) {
			mapStr.put("zybm", gh);//职员编码
			mapStr.put("zymc", name);//职员名称
		}else {
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称	
		}
		mapStr.put("bmbm", "");//部门编码
		mapStr.put("bmmc", "");//部门名称
		mapStr.put("gysbm", "");//供应商编码
		mapStr.put("gysmc", "");//供应商名称
		
		mapStr.put("KMBM", dfkm);//科目编码
		mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
		//mapStr.put("JFJE", "");//借方金额
		mapStr.put("DFJE", xj);//贷方金额
		mapStr.put("ZY", pzzy);//摘要		
		mapStr.put("FLH", "2");//分录号
		iu.insert(mapStr, "uf_zjb");
		pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='1'";
		rs.executeSql(sql);
		if(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
		}
		if(!"".equals(pzid)){
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
				Integer.valueOf(pzid));
		}
		return SUCCESS;
	}

}
