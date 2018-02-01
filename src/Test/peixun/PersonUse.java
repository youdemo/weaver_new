package Test.peixun;

public class PersonUse {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Person p = new Person();
		System.out.println(p.getAge());
		Person p1 = new Person("zrk");
		p1.setName("rkz");
		System.out.println(p1.getName());
		

	}

}
