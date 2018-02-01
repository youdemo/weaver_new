package hsproject.dao;

import hsproject.bean.ProjectGroupBean;

import java.util.ArrayList;
import java.util.List;

import weaver.conn.RecordSet;
import weaver.general.Util;

/**
 * 获取项目分组信息
 * 
 * @author jianyong.tang 2018-01-16
 * 
 */
public class ProjectGroupDao {

	/**
	 * 获取项目分组信息
	 * @return
	 */
	public List<ProjectGroupBean> getGroupData(){
		List<ProjectGroupBean> list = new ArrayList<ProjectGroupBean>();
		RecordSet rs = new RecordSet();
		String sql="select * from uf_prj_groupinfo order by dsporder asc";
		rs.executeSql(sql);
		while(rs.next()){
			ProjectGroupBean pgb = new ProjectGroupBean();
			pgb.setId(Util.null2String(rs.getString("id")));
			pgb.setGroupname(Util.null2String(rs.getString("groupname")));
			pgb.setGroupname(Util.null2String(rs.getString("dsporder")));
			list.add(pgb);
			
		}
		return list;
	}
}
