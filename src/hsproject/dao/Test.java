package hsproject.dao;

import java.util.List;

import hsproject.bean.ProjectGroupBean;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ProjectGroupDao pgd = new ProjectGroupDao();
		List<ProjectGroupBean> pgblist = pgd.getGroupData();
		pgblist.size();

	}

}
