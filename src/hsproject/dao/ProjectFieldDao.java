package hsproject.dao;

import hsproject.bean.ProjectFieldBean;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * 获取项目字段列表
 * 
 * @author jianyong.tang 2018-01-16
 * 
 */
public class ProjectFieldDao {
	/**
	 * 获取对应项目类型启用的字段
	 * 
	 * @param prjtype
	 * @param groupid
	 *            空就是所有字段，非空就是对应分组字段
	 */
	public List<ProjectFieldBean> getUsedPrjField(String prjtype, String groupid) {
		RecordSet rs = new RecordSet();
		List<ProjectFieldBean> list = new ArrayList<ProjectFieldBean>();
		String sql = "select * from uf_project_field where prjtype='" + prjtype
				+ "' and isused='1'";
		if (!"".equals(groupid)) {
			sql += " and groupinfo='" + groupid + "'";
		}
		sql += " order by dsporder asc";
		rs.executeSql(sql);
		while (rs.next()) {
			ProjectFieldBean pfb = new ProjectFieldBean();
			pfb.setId(Util.null2String(rs.getString("id")));
			pfb.setPrjtype(Util.null2String(rs.getString("prjtype")));
			pfb.setDescription(Util.null2String(rs.getString("description")));
			pfb.setFieldname(Util.null2String(rs.getString("fieldname")));
			pfb.setShowname(Util.null2String(rs.getString("showname")));
			pfb.setFieldtype(Util.null2String(rs.getString("fieldtype")));
			pfb.setTexttype(Util.null2String(rs.getString("texttype")));
			pfb.setButtontype(Util.null2String(rs.getString("buttontype")));
			pfb.setTextlength(Util.null2String(rs.getString("textlength")));
			pfb.setFloatdigit(Util.null2String(rs.getString("floatdigit")));
			pfb.setSelectbutton(Util.null2String(rs.getString("selectbutton")));
			pfb.setGroupinfo(Util.null2String(rs.getString("groupinfo")));
			pfb.setIsused(Util.null2String(rs.getString("isused")));
			pfb.setIsmust(Util.null2String(rs.getString("ismust")));
			pfb.setDsporder(Util.null2String(rs.getString("dsporder")));
			pfb.setIscommon(Util.null2String(rs.getString("iscommon")));
			pfb.setIsedit(Util.null2String(rs.getString("isedit")));
			pfb.setIsreadonly(Util.null2String(rs.getString("isreadonly")));
			list.add(pfb);
		}
		return list;
	}

	/**
	 * 获取所有自定义字段
	 * 
	 * @return
	 */
	public List<ProjectFieldBean> getAllDefineField() {
		RecordSet rs = new RecordSet();
		List<ProjectFieldBean> list = new ArrayList<ProjectFieldBean>();
		String sql = "select * from uf_project_field where (iscommon =null or iscommon ='1' or iscommon='')";
		sql += " order by dsporder asc";
		rs.executeSql(sql);
		while (rs.next()) {
			ProjectFieldBean pfb = new ProjectFieldBean();
			pfb.setId(Util.null2String(rs.getString("id")));
			pfb.setPrjtype(Util.null2String(rs.getString("prjtype")));
			pfb.setDescription(Util.null2String(rs.getString("description")));
			pfb.setFieldname(Util.null2String(rs.getString("fieldname")));
			pfb.setShowname(Util.null2String(rs.getString("showname")));
			pfb.setFieldtype(Util.null2String(rs.getString("fieldtype")));
			pfb.setTexttype(Util.null2String(rs.getString("texttype")));
			pfb.setButtontype(Util.null2String(rs.getString("buttontype")));
			pfb.setTextlength(Util.null2String(rs.getString("textlength")));
			pfb.setFloatdigit(Util.null2String(rs.getString("floatdigit")));
			pfb.setSelectbutton(Util.null2String(rs.getString("selectbutton")));
			pfb.setGroupinfo(Util.null2String(rs.getString("groupinfo")));
			pfb.setIsused(Util.null2String(rs.getString("isused")));
			pfb.setIsmust(Util.null2String(rs.getString("ismust")));
			pfb.setDsporder(Util.null2String(rs.getString("dsporder")));
			pfb.setIscommon(Util.null2String(rs.getString("iscommon")));
			pfb.setIsedit(Util.null2String(rs.getString("isedit")));
			pfb.setIsreadonly(Util.null2String(rs.getString("isreadonly")));
			list.add(pfb);
		}
		return list;
	}
}
