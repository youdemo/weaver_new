package Test.peixun;

public class TestMp4 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MP4 mp4 = new MP4("aaa","培训视频","培训文档");
		System.out.println(mp4.getNowSong());	
		System.out.println(mp4.getDqsp());
		System.out.println(mp4.getDqwd());
		mp4.dainGe("bbbb");
		System.out.println(mp4.getNowSong());
		System.out.println(mp4.getNextSong());
		mp4.setNextSong("cccc");
		System.out.println(mp4.getNextSong());
		mp4.xiayishou("ddd");
		System.out.println(mp4.getNowSong());
		System.out.println(mp4.getNextSong());

	}

}
