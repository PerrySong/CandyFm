package UserInfo;

import java.time.LocalTime;
import java.util.LinkedList;

import songfinder.ArtistInfo;
import songfinder.SongInfo;

public class UserInfo {
	/*
	 * This class is to store single user information
	 */
	private String password;
	private String email;
	private String name;
	private String history;
	private LocalTime lastTime;
	private LocalTime currentTime;
	private LinkedList<SongInfo> favoriteSongs;//This map store users' favorite songs' urls.
	private LinkedList<ArtistInfo> favoriteArtists;//This map store users' favorite artists' urls.
	
	public UserInfo() {
		this.password = new String();
		this.email = new String();
		this.name = new String();
		this.history = new String();
		this.lastTime = LocalTime.now();
		this.favoriteSongs = new LinkedList<SongInfo>();
		this.favoriteArtists = new LinkedList<ArtistInfo>();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHistory() {
		return history;
	}

	public void setHistory(String history) {
		this.history = history;
	}
	
	public void updateTime() {
		this.lastTime = LocalTime.now();
	}
	
	public LocalTime getLastTime() {
		return this.lastTime;
	}
	
	public void setLastVisitTime(LocalTime lt) {
		this.lastTime = this.currentTime;
		this.currentTime = lt;
	}
	
	public UserInfo clone() {
		UserInfo result = new UserInfo();
		result.setEmail(this.email);
		result.setHistory(this.history);
		result.setName(this.name);
		result.setPassword(this.password);
		return result;
	}
	
	public boolean addFavoriteArtist(ArtistInfo artist) {
		for(ArtistInfo a: this.favoriteArtists) {
			if(a.getName().equals(artist.getName())) return false;
		}
		this.favoriteArtists.add(artist);
		return true;
		
	}
	
	public boolean addFavoriteSong(SongInfo song) {
		for(SongInfo s: this.favoriteSongs) {
			if(s.getTrackId().equals(song.getTrackId())) return false;
		}
		this.favoriteSongs.add(song);
		return true;
	}
	
	//This method return the copy of this.favoritseSongs.
	public LinkedList<SongInfo> getFavoriteSong() {
		
		LinkedList<SongInfo> result = new LinkedList<SongInfo>();
		//The result's order is the reverse of original LinkedList.
		for(SongInfo item: this.favoriteSongs) {
			result.add(item.clone());
		}
		return result;
	}
	
	//This method return the copy of this.favoritseArtists.
	public LinkedList<ArtistInfo> getFavoriteArtists() {
		LinkedList<ArtistInfo> result = new LinkedList<ArtistInfo>();
		//The result's order is the same as original LinkedList.
		for(ArtistInfo item: this.favoriteArtists) {
			result.add(item.clone());
		}
		return result;
	}
	
	//Delete given song in user's favorite song list.
	public void delFavSong(int i) {
		this.favoriteSongs.remove(i);		
	}
	//Delete given artist in user's favorite artist list.
	public void delFavArtist(int i) {
		this.favoriteArtists.remove(i);	
	}
}
