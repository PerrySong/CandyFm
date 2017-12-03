package UserInfo;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.LinkedList;

import concurrent.ReentrantLock;
import songfinder.ArtistInfo;
import songfinder.SongInfo;

public class UserAccounts {
	/*
	 * This class provide user account library, store user account and associated password.
	 */
	private HashMap<String, UserInfo> account;
	private ReentrantLock rwl;
	
	
	public UserAccounts() {
		this.account = new HashMap<String, UserInfo>();
		this.rwl = new ReentrantLock();
	}
	
	public boolean signIn(String username, String password) {
		rwl.lockWrite();
		try {
			if(this.account.containsKey(username)) return false;
			UserInfo newUser = new UserInfo();
			newUser.setPassword(password);
			this.account.put(username, newUser);
			return true;
		} finally {
			rwl.unlockWrite();
		}
	}
	
	public boolean login(String username, String password) {
		rwl.lockRead();
			try {
				//It is thread safe.
			if(this.account.containsKey(username) && this.account.get(username).getPassword().equals(password)) return true;
			return false;
		} finally {
			rwl.unlockRead();
		}
	}
	
	public UserInfo getUser(String username) {
		rwl.lockRead();
		try {
			return this.account.get(username).clone();
		} finally {
			rwl.unlockRead();
		}
	}
	
	public String getLastVisitTime(String username) {
		rwl.lockRead();
		try {
			//We return the String, so it is thread safe.
			if(account.get(username) != null && this.account.get(username).getLastTime() != null) return this.account.get(username).getLastTime().toString();
			return "Never!!!";
		} finally {
			rwl.unlockRead();
		}
	}
	
	public void setLastVisitTime(String username) {
		rwl.lockWrite();
		try {
			this.account.get(username).setLastVisitTime(LocalTime.now());
		} finally {
			rwl.unlockWrite();
		}
	}
	
	//This method add user favorite song under target userInfo object.
	public boolean addFavoriteArtist(String username, ArtistInfo artist) {
		rwl.lockWrite();
		try {
			UserInfo user = this.account.get(username);
			return user.addFavoriteArtist(artist);
		} finally {
			rwl.unlockWrite();
		}
	}
	
	//This method add user favorite song under target userInfo object.
	public boolean addFavoriteSong(String username, SongInfo song) {
		rwl.lockWrite();
		try {
			System.out.println(username);
			UserInfo user = this.account.get(username);
			return user.addFavoriteSong(song);
		} finally {
			rwl.unlockWrite();
		}
	}
	
	//This method delete specific user's favorite song.
	public void deleteFavoriteSong(String username, int i) {
		rwl.lockWrite();
		try {
			UserInfo user = this.account.get(username);
			user.delFavSong(i);
		} finally {
			rwl.unlockWrite();
		}
	}
	
	public void deleteFavoriteArtist(String username, int i) {
		rwl.lockWrite();
		try {
			UserInfo user = this.account.get(username);
			user.delFavArtist(i);
		} finally {
			rwl.unlockWrite();
		}
	}
	
	//This method return the user's favorite artists' list
	public LinkedList<ArtistInfo> getFavoriteArtists(String username){
		//Linked list allow us to access the most current added item first.
		rwl.lockRead();
		try {
			return this.account.get(username).getFavoriteArtists();//getFavoriteArtists return the copy. It's thread safe.
		} finally {
			rwl.unlockRead();
		}
	}
	
	//This method return the user's favorite songs' list
	public LinkedList<SongInfo> getFavoriteSongs(String username){
		//Linked list allow us to access the most current added item first.
		rwl.lockRead();
		try {
			return this.account.get(username).getFavoriteSong();//getFavoriteSongs return the copy. It's thread safe.
		} finally {
			rwl.unlockRead();
		}
	}
	
}
