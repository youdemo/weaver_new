package goodbaby.pz;

import java.util.HashMap;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;

public class XmlUtil {

	public String getCompanyCode(String yjcbzx) {
		RecordSet rs = new RecordSet();
		String gszt = "";
		String sql = "select distinct gszt from uf_cbzx where yjcbzx='" + yjcbzx + "' ";
		rs.executeSql(sql);
		if (rs.next()) {
			gszt = Util.null2String(rs.getString("gszt"));
		}
		return gszt;
	}

	public String getSender(String gszt) {
		RecordSet rs = new RecordSet();
		String sender = "";
		String sql = "select jsf,fsf from uf_jsyfsf where gs = '" + gszt + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			sender = Util.null2String(rs.getString("fsf"));
		}
		return sender;

	}

	public String getReceiver(String gszt) {
		RecordSet rs = new RecordSet();
		String receiver = "";
		String sql = "select jsf,fsf from uf_jsyfsf where gs = '" + gszt + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			receiver = Util.null2String(rs.getString("jsf"));
		}
		return receiver;

	}
	public String getSFPPHS(String gszt) {
		RecordSet rs = new RecordSet();
		String sfpphs = "";
		String sql = "select sfpphs from uf_yjcbzxdzb ztdm='" + gszt + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			sfpphs = Util.null2String(rs.getString("sfpphs"));
		}
		return sfpphs;

	}

	public Map<String, String> getFZHSMap(String fykm, String ywlx) {
		RecordSet rs = new RecordSet();
		Map<String, String> map = new HashMap<String, String>();
		String wxsybfzhs = "";// 外销事业部辅助核算
		String wxywmsbfzhs = "";// 外销业务模式辅助核算
		String bmda = "";// 部门档案
		String ksfzhs = "";// 客商辅助核算
		String wxckfzhs = "";// 外销仓库辅助核算
		String brandfzhs = "";// Brand辅助核算
		String xjllfzhs = "";// 现金流量辅助核算
		String wxzjgcfzhs = "";//外销在建工程辅助核算
		String sql = "select wxsybfzhs,wxywmsbfzhs,bmda,ksfzhs,wxckfzhs,brandfzhs,xjllfzhs,wxzjgcfzhs from uf_kjkm where kmbm='"
				+ fykm + "' and ywlx='" + ywlx + "'";
		rs.executeSql(sql);
		if (rs.next()) {
			wxsybfzhs = Util.null2String(rs.getString("wxsybfzhs"));
			wxywmsbfzhs = Util.null2String(rs.getString("wxywmsbfzhs"));
			bmda = Util.null2String(rs.getString("bmda"));
			ksfzhs = Util.null2String(rs.getString("ksfzhs"));
			wxckfzhs = Util.null2String(rs.getString("wxckfzhs"));
			brandfzhs = Util.null2String(rs.getString("brandfzhs"));
			xjllfzhs = Util.null2String(rs.getString("xjllfzhs"));
			wxzjgcfzhs = Util.null2String(rs.getString("wxzjgcfzhs"));
		}
		map.put("wxsybfzhs", wxsybfzhs);
		map.put("wxywmsbfzhs", wxywmsbfzhs);
		map.put("bmda", bmda);
		map.put("ksfzhs", ksfzhs);
		map.put("wxckfzhs", wxckfzhs);
		map.put("brandfzhs", brandfzhs);
		map.put("xjllfzhs", xjllfzhs);
		map.put("wxzjgcfzhs", wxzjgcfzhs);
		return map;
	}

	/**
	 * 外销事业部辅助核算
	 * 
	 * @param yjcbzx
	 * @param type
	 * @return
	 */
	public String getWxsybfzhs(String yjcbzx, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String wxsybmc = "";
		if ("".equals(yjcbzx)) {
			return "";
		}
		if ("3".equals(type)) {
			sql = "select wxsybmc from uf_yjcbzxdzb where yjcbzxmc='" + yjcbzx + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				wxsybmc = Util.null2String(rSet.getString("wxsybmc"));
			}
		} else if ("4".equals(type)) {
			sql = "select wxsybdm from uf_yjcbzxdzb where yjcbzxmc='" + yjcbzx + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				wxsybmc = Util.null2String(rSet.getString("wxsybdm"));
			}
		}
		return wxsybmc;
	}

	/**
	 * 外销业务模式辅助核算
	 * 
	 * @param type
	 * @return
	 */
	public String getWxywmsbfzhs(String type) {
		RecordSet rSet = new RecordSet();

		return "";
	}

	/**
	 * 部门档案
	 * 
	 * @param cbzx
	 * @param type
	 * @return
	 */
	public String getBmda(String cbzx, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String cbzxbm = "";
		if ("".equals(cbzx)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select cbzxbm from uf_cbzx where id='" + cbzx + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				cbzxbm = Util.null2String(rSet.getString("cbzxbm"));
			}
		} else if ("3".equals(type)) {
			sql = "select cbzxbmmc from uf_cbzx where id='" + cbzx + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				cbzxbm = Util.null2String(rSet.getString("cbzxbmmc"));
			}
		}
		return cbzxbm;
	}

	/**
	 * 客商辅助核算
	 * 
	 * @param gys
	 * @param type
	 * @return
	 */
	public String getKsfzhs(String gys, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String gysyy = "";
		if ("".equals(gys)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select yygfbm,yygfmc from uf_suppmessForm where id='" + gys + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				gysyy = Util.null2String(rSet.getString("yygfbm"));
			}
		} else if ("3".equals(type)) {
			sql = "select yygfbm,yygfmc from uf_suppmessForm where id='" + gys + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				gysyy = Util.null2String(rSet.getString("yygfmc"));
			}
		}
		return gysyy;
	}

	/**
	 * 外销仓库辅助核算
	 * 
	 * @param shck
	 * @param type
	 * @return
	 */
	public String getWxckfzhs(String shck, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String wxck = "";
		if ("".equals(shck)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select bh,ckmc from uf_stocks where id='" + shck + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				wxck = Util.null2String(rSet.getString("bh"));
			}
		} else if ("3".equals(type)) {
			sql = "select bh,ckmc from uf_stocks where id='" + shck + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				wxck = Util.null2String(rSet.getString("ckmc"));
			}
		}
		return wxck;
	}

	/**
	 * Brand辅助核算
	 * 
	 * @param pp
	 * @param type
	 * @return
	 */
	public String getBrandfzhs(String pp, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String brand = "";
		if ("".equals(pp)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select dm,mc from uf_gfdzb where id='" + pp + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				brand = Util.null2String(rSet.getString("dm"));
			}
		} else if ("3".equals(type)) {
			sql = "select dm,mc from uf_gfdzb where id='" + pp + "' ";
			rSet.executeSql(sql);
			if (rSet.next()) {
				brand = Util.null2String(rSet.getString("mc"));
			}
		}
		return brand;
	}

	/**
	 * 现金流量辅助核算
	 * 
	 * @param lx
	 * @param type
	 * @return
	 */
	public String getXjllfzhs(String lx, String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String result = "";
		if ("".equals(lx)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select xjlldm,xjllmc from uf_xjlldzb where wldal='" + lx + "'";
			rSet.executeSql(sql);
			if (rSet.next()) {
				result = Util.null2String(rSet.getString("xjlldm"));
			}
		} else if ("3".equals(type)) {
			sql = "select xjlldm,xjllmc from uf_xjlldzb where wldal='" + lx + "'";
			rSet.executeSql(sql);
			if (rSet.next()) {
				result = Util.null2String(rSet.getString("xjllmc"));
			}
		}
		return result;
	}
	
	public String getAccountCode(String fylx,String fykmfl) {
		RecordSet rs = new RecordSet();
		String fylxname = "";
		String fykmflname = "";
		String kmmc = "";
		String accountCode = "";
		String sql = "select selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_cbzx' and a.fieldname='fylx' and c.selectvalue='"+fylx+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			fylxname = Util.null2String(rs.getString("selectname"));
		}
		sql = "select selectname from workflow_billfield a, workflow_bill b,workflow_selectitem c where a.billid=b.id and c.fieldid=a.id  and b.tablename='uf_NPP' and a.fieldname='fykmfl' and c.selectvalue='"+fykmfl+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			fykmflname = Util.null2String(rs.getString("selectname"));
		}
		sql="select kmbm1 from uf_fykm where kmmc='"+fylxname+"\\"+fykmflname+"'";
		rs.executeSql(sql);
		if(rs.next()) {
			accountCode = Util.null2String(rs.getString("kmbm1"));
		}
		return accountCode;
	}
	
	public String getSpecialKSFZHS(String yjcbzx,String type) {
		RecordSet rSet = new RecordSet();
		String sql = "";
		String result = "";
		if ("".equals(yjcbzx)) {
			return "";
		}
		if ("4".equals(type)) {
			sql = "select ksdm,ksmc from uf_yjcbzxdzb where yjcbzxmc='" + yjcbzx + "'";
			rSet.executeSql(sql);
			if (rSet.next()) {
				result = Util.null2String(rSet.getString("ksdm"));
			}
		} else if ("3".equals(type)) {
			sql = "select ksdm,ksmc from uf_yjcbzxdzb where yjcbzxmc='" + yjcbzx + "'";
			rSet.executeSql(sql);
			if (rSet.next()) {
				result = Util.null2String(rSet.getString("ksmc"));
			}
		}
		return result;
		
	}

}
