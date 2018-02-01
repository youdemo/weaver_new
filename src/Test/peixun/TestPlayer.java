package Test.peixun;

public class TestPlayer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Player p = new Player("aa");
		p.dainGe("bbb");
		p.xiayishou();
		p.dainGe("ccc");
		System.out.println(p.getOldSong());
		System.out.println(p.getNowSong());
		System.out.println(p.getNextSong());
	}

}
