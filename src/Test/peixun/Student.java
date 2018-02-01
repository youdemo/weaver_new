package Test.peixun;

public class Student extends Person {
	private String nianji = "";
	private String banji = "";
	public String getNianji() {
		return nianji;
	}
	public void setNianji(String nianji) {
		this.nianji = nianji;
	}
	public String getBanji() {
		return banji;
	}
	public void setBanji(String banji) {
		this.banji = banji;
	}
	
	public Student(String name,String banji){
		this.banji = banji;
		super.name=name;
	}
	
	public void  rename(String newName) {
		setName("aaa"+newName);
	}
	
	public void addage(String age){
		super.setAge(this.age+2);
	}
}
