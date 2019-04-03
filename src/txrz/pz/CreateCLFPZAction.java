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
 * 差旅费
 * @author tangj
 *
 */
public class CreateCLFPZAction implements Action{

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
		String wsjezh = "";//未税金额
		String sezh = "";//税额总和
		String fpjezh = "";//发票金额总和
		String qkr = "";//请款人
		String gh = "";//请款人工号
		String name = "";//请款人名
		String qkdwwb = "";//请款单位描述
		String pzzy = ""; //凭证摘要
		String gsdm = "";//公司代码
		String qkdw = "";//请款单位
		String kmlb = "";//科目类别
		String jfkm = "";
		String jfkmmc = "";
		String mainid = "";
		String dfkm = "";
		String requestnamenew = "";
		String account_name = "";
		String ccrq = "";
		String ccjssj = "";
		String sfsybyj = "";//sfsybyj
		String zjkdh_new = "";//暂借款 单号（新）
		String jine = "";//暂借款金额
		String ybje = "";
		String ytje = "";
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
		sql = "select ybje,ytje,sfsybyj,zjkdh_new,jine,id,djbh,wsjezh,sezh,qkr,gh,(select lastname from hrmresource where id=a.qkr) as name,qkdwwb,(select account_name from uf_gsb where id=a.gsb)  as account_name,gsdm,qkdw,sezh,ccrq,ccjssj from "+tableName+" a where requestid="+requestid;
		rs.executeSql(sql);
		if(rs.next()){
			djbh = Util.null2String(rs.getString("djbh"));
			wsjezh = Util.null2String(rs.getString("wsjezh"));
			sezh = Util.null2String(rs.getString("sezh"));
			gh = Util.null2String(rs.getString("gh"));
			name = Util.null2String(rs.getString("name"));
			qkdwwb = Util.null2String(rs.getString("qkdwwb"));
			account_name = Util.null2String(rs.getString("account_name"));
			gsdm = Util.null2String(rs.getString("gsdm"));
			qkdw = Util.null2String(rs.getString("qkdw"));
			mainid = Util.null2String(rs.getString("id"));
			sezh = Util.null2String(rs.getString("sezh"));
			ccrq = Util.null2String(rs.getString("ccrq"));
			ccjssj = Util.null2String(rs.getString("ccjssj"));
			sfsybyj = Util.null2String(rs.getString("sfsybyj"));
			zjkdh_new = Util.null2String(rs.getString("zjkdh_new"));
			jine = Util.null2String(rs.getString("jine"));
			ybje = Util.null2String(rs.getString("ybje"));
			ytje = Util.null2String(rs.getString("ytje"));
		}
		if("".equals(wsjezh)) {
			wsjezh = "0";
		}	
		if("".equals(sezh)) {
			sezh = "0";
		}	
		if("".equals(jine)) {
			jine = "0";
		}
		if("".equals(ybje)) {
			ybje = "0";
		}
		if("".equals(ytje)) {
			ytje = "0";
		}
		sql = "select "+wsjezh+"+"+sezh+" as fpjezh";
		rs.execute(sql);
		if(rs.next()) {
			fpjezh = Util.null2String(rs.getString("fpjezh"));
		}
		sql = "select kmlb from uf_bmfylxdzb where bmmc='"+qkdw+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			kmlb = Util.null2String(rs.getString("kmlb"));
		}
		if("1".equals(kmlb)) {
			jfkm = "6602.07.01";
			jfkmmc = "管理费用_差旅费_员工差旅费";
		}else{
			jfkm = "6601.07.01";
			jfkmmc = "销售费用_差旅费_员工差旅费";
			
		}
		pzzy = "付"+qkdwwb+name+ccrq+"-"+ccjssj+"差旅费";
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
		int flh = 1;
		sql = "select (select departmentname from hrmdepartment where id=a.bmmc) as bmmc,je,gsdm from "+tableName+"_dt7 a where mainid='"+mainid+"'";
		rs.executeSql(sql);
		while(rs.next()) {		
			String bmmc = Util.null2String(rs.getString("bmmc"));
			String je = Util.null2String(rs.getString("je"));
			String bmdm = Util.null2String(rs.getString("gsdm"));
			mapStr.put("lx", "0");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称			
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			if("3".equals(hsxmjf)) {
				mapStr.put("bmbm", bmdm);//部门编码
				mapStr.put("bmmc", bmmc);//部门名称
			}else {
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
			}
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", jfkm);//科目编码
			mapStr.put("KMMC", jfkmmc);//科目名称
			mapStr.put("JFJE", je);//借方金额
			//mapStr.put("DFJE", bs);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");	
			flh++;
		}
		if(Util.getFloatValue(sezh, 0)>0) {
			mapStr.put("lx", "0");//lx
			mapStr.put("gbdqbm", "");//国别/地区编码
			mapStr.put("gbdqmc", "");//国别/地区名称			
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称			
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", "2221.02.01");//科目编码
			mapStr.put("KMMC", "应缴税金-应缴增值税-进项税额");//科目名称
			mapStr.put("JFJE", sezh);//借方金额
			//mapStr.put("DFJE", bs);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
			flh++;
		}	
		if(!"0".equals(sfsybyj)&&!"".equals(zjkdh_new)) {
			if(Util.getFloatValue(ytje, 0)>0) {
				mapStr.put("lx", "0");//lx
				mapStr.put("gbdqbm", "");//国别/地区编码
				mapStr.put("gbdqmc", "");//国别/地区名称			
				mapStr.put("zybm", "");//职员编码
				mapStr.put("zymc", "");//职员名称
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称			
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				
				mapStr.put("KMBM", "1002.01.16");//科目编码
				mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
				mapStr.put("JFJE", ytje);//借方金额
				//mapStr.put("DFJE", bs);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				flh++;
			}
		}
		String pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='0'";
		rs.executeSql(sql);
		while(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
					Integer.valueOf(pzid));
			
		}
		if(!"0".equals(sfsybyj)&&"".equals(zjkdh_new)) {
			dfkm = "1002.01.16";
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
			mapStr.put("zybm", "");//职员编码
			mapStr.put("zymc", "");//职员名称
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", dfkm);//科目编码
			mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
			//mapStr.put("JFJE", "");//借方金额
			mapStr.put("DFJE", fpjezh);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
		}else if("0".equals(sfsybyj)) {
			dfkm = "1221.05";
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
			}
			mapStr.put("bmbm", "");//部门编码
			mapStr.put("bmmc", "");//部门名称
			mapStr.put("gysbm", "");//供应商编码
			mapStr.put("gysmc", "");//供应商名称
			
			mapStr.put("KMBM", dfkm);//科目编码
			mapStr.put("KMMC", "其他应收款备用金");//科目名称
			//mapStr.put("JFJE", "");//借方金额
			mapStr.put("DFJE", fpjezh);//贷方金额
			mapStr.put("ZY", pzzy);//摘要		
			mapStr.put("FLH", flh+"");//分录号
			iu.insert(mapStr, "uf_zjb");
		}else if(!"0".equals(sfsybyj)&&!"".equals(zjkdh_new)) {
			if(("0".equals(ybje)&&"0".equals(ytje))||Util.getFloatValue(ytje, 0)>0) {
				dfkm = "1221.06";
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
				mapStr.put("KMMC", "其他应收款-职员往来");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", jine);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
			}else if(Util.getFloatValue(ybje, 0)>0) {
				dfkm = "1221.06";
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
				mapStr.put("KMMC", "其他应收款-职员往来");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", jine);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				flh++;
				dfkm = "1002.01.16";
				hsxmdf = pu.getFZHS(dfkm);
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
				mapStr.put("zybm", "");//职员编码
				mapStr.put("zymc", "");//职员名称
				mapStr.put("bmbm", "");//部门编码
				mapStr.put("bmmc", "");//部门名称
				mapStr.put("gysbm", "");//供应商编码
				mapStr.put("gysmc", "");//供应商名称
				
				mapStr.put("KMBM", dfkm);//科目编码
				mapStr.put("KMMC", "银行存款-人民币-中国银行新城科技园分行");//科目名称
				//mapStr.put("JFJE", "");//借方金额
				mapStr.put("DFJE", ybje);//贷方金额
				mapStr.put("ZY", pzzy);//摘要		
				mapStr.put("FLH", flh+"");//分录号
				iu.insert(mapStr, "uf_zjb");
				
			}
		}
		//贷方
		
		pzid = "";
		sql = "select id from uf_zjb where xglcid='"+requestid+"' and lx='1'";
		rs.executeSql(sql);
		while(rs.next()){
			pzid = Util.null2String(rs.getString("id"));
			ModeRightInfo ModeRightInfo = new ModeRightInfo();
			ModeRightInfo.editModeDataShare(Integer.valueOf("1"), Integer.valueOf(modeid),
				Integer.valueOf(pzid));
		}
		
		return SUCCESS;
	}

}
