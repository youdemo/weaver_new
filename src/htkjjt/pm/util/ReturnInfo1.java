package htkjjt.pm.util;

import java.util.List;
import java.util.Map;

public class ReturnInfo1 {

	public List<ReturnInfo2> getInfoList2() {
		return InfoList2;
	}

	public void setInfoList2(List<ReturnInfo2> infoList2) {
		InfoList2 = infoList2;
	}
	private String isSuccess;
	private String message;
	private List<ReturnInfo2> InfoList2;
	public ReturnInfo1(){
		isSuccess = "S";
		message="数据写入成功";
	}
	
	public String getIsSuccess() {
		return isSuccess;
	}
	public void setIsSuccess(String isSuccess) {
		this.isSuccess = isSuccess;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
