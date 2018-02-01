package hsproject.dao;

import hsproject.bean.ProjectCommonFieldBean;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * 获取项目通用字段列表
 * 
 * @author jianyong.tang 2018-01-16
 * 
 */
public class ProjectCommonFieldDao {
	/**
	 * 获取项目通用类型的字段
	 */
	public List<ProjectCommonFieldBean> getCommonPrjField() {
		RecordSet rs = new RecordSet();
		List<ProjectCommonFieldBean> list = new ArrayList<ProjectCommonFieldBean>();
		String sql = "select * from uf_prj_common_field ";
		sql += " order by dsporder asc";
		rs.executeSql(sql);
		while (rs.next()) {
			ProjectCommonFieldBean pfb = new ProjectCommonFieldBean();
			pfb.setId(Util.null2String(rs.getString("id")));
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
			pfb.setIsedit(Util.null2String(rs.getString("isedit")));
			pfb.setIsreadonly(Util.null2String(rs.getString("isreadonly")));
			list.add(pfb);
		}
		return list;
	}
}
