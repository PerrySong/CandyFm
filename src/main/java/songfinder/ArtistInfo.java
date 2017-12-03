package songfinder;

public class ArtistInfo {
	/*
	 * This class store single artist information
	 */
	private String name;
	private int playcount;
	private int listeners;
	private String bio;
	private String image;
	
	public ArtistInfo(String name, int playcount, int listeners, String bio, String image) {
		this.name = name;
		this.playcount = playcount;
		this.listeners = listeners;
		this.bio = bio;
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPlayCount() {
		return playcount;
	}

	public void setPlayCount(int playCount) {
		this.playcount = playCount;
	}

	public int getListeners() {
		return listeners;
	}

	public void setListeners(int listeners) {
		this.listeners = listeners;
	}

	public String getBio() {
		return bio;
	}

	public void setBio(String bio) {
		this.bio = bio;
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	
	
	public ArtistInfo clone() {
		ArtistInfo result = new ArtistInfo(this.name, this.playcount, this.listeners, this.bio, this.image);
		return result;
	}
	
}
