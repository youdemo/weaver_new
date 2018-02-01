package hsproject.bean;

/**
 * 项目分组对象类
 * @author jianyong.tang 2018-01-16
 *
 */
public class ProjectGroupBean { 
	private String id = "";
	private String groupname = "";//分组名称
	private String dsporder = "";//显示顺序
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getGroupname() {
		return groupname;
	}
	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}
	public String getDsporder() {
		return dsporder;
	}
	public void setDsporder(String dsporder) {
		this.dsporder = dsporder;
	}
	
}
