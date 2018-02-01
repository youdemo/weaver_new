package Test.peixun;

public class MP4 extends Player{
	private String dqsp = "";
	private String dqwd = "";
	public String getDqsp() {
		return dqsp;
	}
	public void setDqsp(String dqsp) {
		this.dqsp = dqsp;
	}
	public String getDqwd() {
		return dqwd;
	}
	public void setDqwd(String dqwd) {
		this.dqwd = dqwd;
	}
	public MP4(String song,String sp,String wd){
		super.setNowSong(song);
		this.dqsp=sp;
		this.dqwd=wd;
	}
	
	public void dainGe(String nextSong){
		super.setNextSong(nextSong);
		super.setNowSong(super.getNextSong());
	}
	
	public void xiayishou(String nextSong){
		super.xiayishou();
		super.setNextSong(nextSong);
	}
	
}
