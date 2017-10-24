package songfinder;

import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
public class SortedSongs {
	
	/*
	 * This class store SongInfo object in different sort method.
	 */
//TODO: I do recommend that you use Maps instead of List data structures.
//This will make it easier to access data in later projects.
//I also recommend you use a sorted Map so that you do not have to do a sort each time.
//The sorted data structure will insert in O(log n) whereas a full sort is an nlogn operation.		
	private TreeMap<String, TreeSet<SongInfo>> sortedByTitleMap;
	private TreeMap<String, TreeSet<SongInfo>> sortedByArtistMap;
	//TreeMap sortedByTag key = Tag, value = trackId. 
	private TreeMap<String, TreeSet<SongInfo>> sortedByTagMap;
	
	public SortedSongs() {
		this.sortedByTitleMap = new TreeMap<String, TreeSet<SongInfo>>();
		this.sortedByArtistMap = new TreeMap<String, TreeSet<SongInfo>>();
		this.sortedByTagMap = new TreeMap<String, TreeSet<SongInfo>>();
	}
	
	public void addSong(SongInfo newSong) {
//		System.out.println("add Song invoked");
		try {
			this.addTitle(newSong); 		
			this.addArtist(newSong);
			this.addTag(newSong);
		} catch(Exception e) {
			System.err.println("add exception");
		}
	}
/*
	 * Data sorted by title will list the artist name, followed by a space, 
	 * followed by -, followed by a space, followed by title, followed by a new line, 
	 * and will be in alphabetical order by title. If two songs have the same title, 
	 * they will then be sorted by the artist. If two songs have the same title and artist, 
	 * they will be sorted by the track_id. Example:
	 * 
	 *	Hushabye Baby - Cry, Cry, Cry
	 *	Aerosmith - Cryin'
	 */
	//this method add SongInfo to arraylist and sort the arraylist as above.
	private void addTitle(SongInfo newSong) {
		if(newSong != null && this.sortedByTitleMap.keySet().contains(newSong.getTitle())) {
			TreeSet<SongInfo> oldSongsSet = sortedByTitleMap.get(newSong.getTitle());
			oldSongsSet.add(newSong);
		} else {
			SortedByTitle sba = new SortedByTitle();
			TreeSet<SongInfo> newSongsSet = new TreeSet<SongInfo>(sba);
			newSongsSet.add(newSong);
			this.sortedByTitleMap.put(newSong.getTitle(), newSongsSet);
		}
	}
	/*
	 * Data sorted by artist will list the artist name, followed by a space, 
	 * followed by -, followed by a space, followed by title, followed by a new 
	 * line, and will be in alphabetical order by artist. If two songs have the 
	 * same artist, they will then be sorted by the title. If two songs have the 
	 * same artist and title, they will be sorted by the track_id. Example:
	 * Steel Rain - Loaded Like A Gun
	 * Tom Petty - A Higher Place (Album Version)
	 */
	//this method add SongInfo object and sort arraylist as above.
	
	
	private void addArtist(SongInfo newSong) {
		if(newSong != null && this.sortedByArtistMap.keySet().contains(newSong.getArtist())) {
			TreeSet<SongInfo> oldSongsSet = sortedByArtistMap.get(newSong.getArtist());
			oldSongsSet.add(newSong);
		} else {
			SortedByArtist sba = new SortedByArtist();
			TreeSet<SongInfo> newSongsSet = new TreeSet<SongInfo>(sba);
			newSongsSet.add(newSong);
			this.sortedByArtistMap.put(newSong.getArtist(), newSongsSet);
		}
	}
	
	//this method add SongInfo object into treemap and sort its key and its  
	//value (arraylist).  
	private void addTag(SongInfo newSong) {
		String key = new String();
		JsonArray tags = newSong.getTag();
		try {
		for(JsonElement tag: tags) {
			if(tag != null) {
				if(tag.isJsonArray()) {
					key = ((JsonArray)tag).get(0).getAsString();
				}
			}
			//If there already exist that key, we update the TreeSet of the key.
			//If there is not taht key, we set the key and its value.
			if(key != null && this.sortedByTagMap.keySet().contains(key) ) {
				
				TreeSet<SongInfo> value = this.sortedByTagMap.get(key);
				value.add(newSong);
			} else if(key != null) {
				SortedByTrackId sbti = new SortedByTrackId();
				TreeSet<SongInfo> value = new TreeSet<SongInfo>(sbti);
				value.add(newSong);
				this.sortedByTagMap.put(key, value);
			}
		}
		} catch(Exception e) {
			System.out.println("add Tag exception");
		}
	}
	
	//TODO: I recommend moving the writeFile functionality to your songs library. It will be more clear why when we
	//discuss concurrency.	
		// This method takes sortWay and writePath as parameters, write songs info in the given writePath
		// in a wanted sortWay.
	public void writeFile(String order,String writePath) {
		TreeMap<String, TreeSet<SongInfo>> songsMap = new TreeMap<String, TreeSet<SongInfo>>();
		Path outpath = Paths.get(writePath);
		outpath.getParent().toFile().mkdir();
		try(BufferedWriter output = Files.newBufferedWriter(outpath)){
			// write files in different ways according to command.
			//1. When we write a songsByTitle.
			if(order.equals("title")) {
				songsMap = this.sortedByTitleMap;
			}	
			//2. When we write a songsByArtist.
			if(order.equals("artist")) {
				songsMap = this.sortedByArtistMap;
			}
			// When we want to wirte songsByArtist or songsByTitle.
			if(!order.equals("tag")) {
				//System.out.print(songsList.get(2).getArist());
				Set<String> keys = songsMap.keySet();
				for(String key: keys) {
					TreeSet<SongInfo> songList = songsMap.get(key); 
					for(SongInfo song: songList) {
						output.write(song.getArtist() + " - " + song.getTitle() + "\n");
					}
				}
			} else {
				songsMap = this.sortedByTagMap;
				Set<String> tags = this.sortedByTagMap.keySet();
				for(String tag: tags) {
					//Write every tag in the songsMap.
					output.write(tag + ": ");
					// For each tag, write every song's trackId which is in that tag.
					for(SongInfo song: songsMap.get(tag)) {
						output.write(song.getTrackId() + " ");
					}
					output.write("\n");
				}
			}
		} catch(IOException ioe) {
			System.out.println("Exception in writting file" + ioe);
		}
	}	

	//there is a test.
	
}
