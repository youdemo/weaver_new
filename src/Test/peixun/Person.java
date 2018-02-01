package Test.peixun;

public class Person {
	public String name = "";
	public int age = 0;
	public String sex = "";

	private float weight = 200;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}
	
	public Person(){
		//this.age=10;
	}
	
	public Person(String name){
		this.name=name;
	}
	public Person(String name,int age,String sex){
		this.name=name;
		this.age=age;
		this.sex=sex;
		
	}
	protected void rename(String newName){
		setName(newName);
	}
	
	public void addage(int age){
		this.age=this.age+age;
	}

}
