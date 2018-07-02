package hsproject.dao;

import hsproject.bean.ProcessCommonFieldBean;
import hsproject.bean.ProcessFieldBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import weaver.conn.RecordSet;
import weaver.general.Util;
/**
 * 获取项目过程信息
 * @author jianyong.tang 2018-01-16
 *
 */
public class ProcessInfoDao {
	/**
	 * 获取项目通用字段值
	 * 
	 * @param prjid
	 * @return
	 */
	public Map<String, String> getProcessCommonData(String processId) {
		if ("".equals(processId)) {
			return null;
		}
		Map<String, String> commonMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProcessCommonFieldDao pdfd = new ProcessCommonFieldDao();
		String sql = "select * from hs_prj_process where id=" + processId;
		rs.executeSql(sql);
		if (rs.next()) {
			commonMap.put("id", Util.null2String(rs.getString("id")));
			List<ProcessCommonFieldBean> list = pdfd.getCommonProcessField();
			for (int i = 0; i < list.size(); i++) {
				ProcessCommonFieldBean pcfb = list.get(i);
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
	public Map<String, String> getProcessDefineData(String processId) {
		if ("".equals(processId)) {
			return null;
		}
		Map<String, String> definedMap = new HashMap<String, String>();
		RecordSet rs = new RecordSet();
		ProcessFieldDao pfd = new ProcessFieldDao();
		String sql = "select * from hs_prj_process_fielddata where processid=" + processId;
		rs.executeSql(sql);
		if (rs.next()) {
			definedMap.put("id", Util.null2String(rs.getString("id")));
			definedMap.put("prjid", Util.null2String(rs.getString("prjid")));
			definedMap.put("processid", Util.null2String(rs.getString("processid")));
			List<ProcessFieldBean> list = pfd.getAllDefineField();
			for (int i = 0; i < list.size(); i++) {
				ProcessFieldBean pfb = list.get(i);
				definedMap.put(pfb.getFieldname(),
						Util.null2String(rs.getString(pfb.getFieldname())));
			}
		}
		return definedMap;
	}
}
