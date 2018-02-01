package Test.peixun;

public class Player {
	private String oldSong = "";
	private String nowSong = "";
	private String nextSong = "";
	public String getOldSong() {
		return oldSong;
	}
	public void setOldSong(String oldSong) {
		this.oldSong = oldSong;
	}
	public String getNowSong() {
		return nowSong;
	}
	public void setNowSong(String nowSong) {
		this.nowSong = nowSong;
	}
	public String getNextSong() {
		return nextSong;
	}
	public void setNextSong(String nextSong) {
		this.nextSong = nextSong;
	}
	public Player(){
		
	}
	public Player(String song){
		this.nowSong = song;
	}
	public void dainGe(String nextSong){
		this.nextSong = nextSong;
	}
	
	public void xiayishou(){
		setOldSong(this.nowSong);
		setNowSong(this.nextSong);
	}
	
}
