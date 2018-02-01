package Test.peixun;

public class TestStudent {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Student s = new Student("张三","三年二班");
		s.rename("aaa");

		System.out.println(s.getName());
		s.setsupername("aaa");
		
		System.out.println(s.getName());
		System.out.println(s.getBanji());
		
		s.addage(1);
		System.out.println(s.getAge());
		s.addage("123");
		System.out.println(s.getAge());
				

	}

}
