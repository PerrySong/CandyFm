package songfinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonObject;

import concurrent.ReentrantLock;

public class ArtistsLibrary {
	/*
	 * In this class, we store every artist's Information 
	 * Thread safe, all methods return deep copy object and have rwl.
	 */
	private ReentrantLock rwl;
	private TreeMap<String, ArtistInfo> artistsMap; //For this TreeMap, the key is the artist mbid, the value is the artistInfo object
	private TreeSet<ArtistInfo> sortedByCount;
	
	public ArtistsLibrary() {
		this.artistsMap = new TreeMap<String, ArtistInfo>();
		this.sortedByCount = new TreeSet<ArtistInfo>(new SortedByCount());
		rwl = new ReentrantLock();
	}
	
	public void addArtist(ArtistInfo newArtist){
		rwl.lockWrite();
		sortedByCount.add(newArtist);
		if(!artistsMap.containsKey(newArtist.getName())) {
			this.artistsMap.put(newArtist.getName(), newArtist);
		}
		rwl.unlockWrite();
	}
	
	//This method return specific artist by the artist name;
	public ArtistInfo getArtist(String name) {
		rwl.lockRead();
		name = name.toLowerCase();
		try {
			ArtistInfo result = this.artistsMap.get(name);
			return result.clone();
		} finally {
			rwl.unlockRead();
		}
	}
	
	
	
	//This method is for download artist information from lastFm api and locally save them. 
	public void saveToFile(JsonObject songObject,String writePath) {
		this.rwl.lockRead();
		Path outpath = Paths.get(writePath);
		outpath.getParent().toFile().mkdir();
		try(BufferedWriter output = Files.newBufferedWriter(outpath)){
			output.write(songObject.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.rwl.unlockRead();
	}	
	
	//This method is to list all artists' name in this library
	public Set<String> listAllArtists() {
		this.rwl.lockRead();
		try {
			TreeSet<String> result = new TreeSet<String>();
			for(String a: this.artistsMap.keySet()) {
				result.add(new String(a));
			}
			return result;//return the deep copy of the keyset.
		} finally {
			this.rwl.unlockRead();
		}
	}
	
	public TreeSet<ArtistInfo> listArtistsByCount() {
		this.rwl.lockRead();
			try {
			TreeSet<ArtistInfo> result = new TreeSet<ArtistInfo>(new SortedByCount());
			for(ArtistInfo a: this.sortedByCount) {
				result.add(a.clone());
			}
			return result;
		} finally {
			rwl.unlockRead();
		}
	}
	
}
