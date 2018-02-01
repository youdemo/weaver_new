package hsproject.bean;
/**
 * 项目过程通用字段对象类
 * @author jianyong.tang 2018-01-30
 *
 */
public class ProcessCommonFieldBean {
	private String id = "";
	private String description = "";// 描述
	private String fieldname = "";// 数据库字段名称
	private String showname = "";// 字段显示名
	private String fieldtype = "";// 字段类型
	private String texttype = "";// 文本类型
	private String buttontype = "";// 浏览按钮类型
	private String textlength = "";// 文本长度
	private String floatdigit = "";// 浮点数位数
	private String selectbutton = "";// 选择框浏览按钮
	private String groupinfo = "";// 分组浏览按钮
	private String isused = "";// 启用
	private String ismust = "";// 必填
	private String dsporder = "";// 显示顺序
	private String isedit = "";// 编辑
	private String isreadonly = "";// 只读
    
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFieldname() {
		return fieldname;
	}

	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}

	public String getShowname() {
		return showname;
	}

	public void setShowname(String showname) {
		this.showname = showname;
	}

	public String getFieldtype() {
		return fieldtype;
	}

	public void setFieldtype(String fieldtype) {
		this.fieldtype = fieldtype;
	}

	public String getTexttype() {
		return texttype;
	}

	public void setTexttype(String texttype) {
		this.texttype = texttype;
	}

	public String getButtontype() {
		return buttontype;
	}

	public void setButtontype(String buttontype) {
		this.buttontype = buttontype;
	}

	public String getTextlength() {
		return textlength;
	}

	public void setTextlength(String textlength) {
		this.textlength = textlength;
	}

	public String getFloatdigit() {
		return floatdigit;
	}

	public void setFloatdigit(String floatdigit) {
		this.floatdigit = floatdigit;
	}

	public String getSelectbutton() {
		return selectbutton;
	}

	public void setSelectbutton(String selectbutton) {
		this.selectbutton = selectbutton;
	}

	public String getGroupinfo() {
		return groupinfo;
	}

	public void setGroupinfo(String groupinfo) {
		this.groupinfo = groupinfo;
	}

	public String getIsused() {
		return isused;
	}

	public void setIsused(String isused) {
		this.isused = isused;
	}

	public String getIsmust() {
		return ismust;
	}

	public void setIsmust(String ismust) {
		this.ismust = ismust;
	}

	public String getDsporder() {
		return dsporder;
	}

	public void setDsporder(String dsporder) {
		this.dsporder = dsporder;
	}

	public String getIsedit() {
		return isedit;
	}

	public void setIsedit(String isedit) {
		this.isedit = isedit;
	}

	public String getIsreadonly() {
		return isreadonly;
	}

	public void setIsreadonly(String isreadonly) {
		this.isreadonly = isreadonly;
	}
	

}
