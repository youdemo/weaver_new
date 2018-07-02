package gvo.webservice;

public class OutHeadInfo {
	
	private String out;
	private String head;
	
	public void setOut(String json){
		this.out = json;
	}
	
	public void setHead(String head){
		this.head = head;
	}
	
	public String getOut(){
		return this.out; 
	}
	
	public String getHead(){
		return this.head;
	}
}