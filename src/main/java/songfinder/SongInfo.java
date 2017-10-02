package songfinder;

public class SongInfo {

	private String artist;
	private String title;
	private String tag;
	private String trackId;
	
	public SongInfo(String artist, String title, String tag, String trackId) {
		this.artist = artist;
		this.title = title;
		this.tag = tag;
		this.trackId = trackId;
	}
	
	public String getArist() {
		return this.artist;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public String getTag() {
		return this.tag;
	}
	
	public String getTrackId() {
		return this.trackId;
	}
}
