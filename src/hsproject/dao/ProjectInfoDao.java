package hsproject.dao;

import hsproject.bean.ProjectCommonFieldBean;
import hsproject.bean.ProjectFieldBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 获取项目信息
 * @author jianyong.tang 2018-01-16
 *
 */
public class ProjectInfoDao {
	/**
	 * 获取项目通用字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProjetCommonData(String prjid) {
		if ("".equals(prjid)) {
			return null;
		}
		Map<String, String> commonMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProjectCommonFieldDao pdfd = new ProjectCommonFieldDao();
		String sql = "select * from hs_projectinfo where id=" + prjid;
		rs.executeSql(sql);
		if (rs.next()) {
			commonMap.put("id", Util.null2String(rs.getString("id")));
			List<ProjectCommonFieldBean> list = pdfd.getCommonPrjField();
			for (int i = 0; i < list.size(); i++) {
				ProjectCommonFieldBean pcfb = list.get(i);
				commonMap.put(pcfb.getFieldname(),
						Util.null2String(rs.getString(pcfb.getFieldname())));
			}
		}
		return commonMap;
	}

	/**
	 * 获取项目自定义字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProjetDefineData(String prjid) {
		if ("".equals(prjid)) {
			return null;
		}
		Map<String, String> definedMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProjectFieldDao pfd = new ProjectFieldDao();
		String sql = "select * from hs_project_fielddata where prjid=" + prjid;
		rs.executeSql(sql);
		if (rs.next()) {
			definedMap.put("id", Util.null2String(rs.getString("id")));
			definedMap.put("prjid", Util.null2String(rs.getString("prjid")));
			List<ProjectFieldBean> list = pfd.getAllDefineField();
			for (int i = 0; i < list.size(); i++) {
				ProjectFieldBean pfb = list.get(i);
				definedMap.put(pfb.getFieldname(),
						Util.null2String(rs.getString(pfb.getFieldname())));
			}
		}
		return definedMap;
	}
}
