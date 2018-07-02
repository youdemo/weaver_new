package goodbaby.tmc.org;

public class ReturnInfo {

	// 是否正确  默认是正确的
	private boolean isTure = true;
	// 编码
	private String code = "0";
	// 消息描述
	private String remark = "";

	public void setMessage(String code,String remark) {
		this.code = code;
		this.remark = remark;
	}
	
	public void setMessage(boolean isTure,String code,String remark) {
		this.isTure = isTure;
		this.code = code;
		this.remark = remark;
	}
	
	public boolean isTure() {
		return isTure;
	}

	public void setTure(boolean isTure) {
		this.isTure = isTure;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString(){
		return "结果执行 " + (isTure?"成功!":"失败!") + " 具体代码：" + getCode() + ",详细:" + getRemark();
	}
	
	
}
