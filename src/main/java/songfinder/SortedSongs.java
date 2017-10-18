package songfinder;

import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
public class SortedSongs {
	
	/*
	 * This class store SongInfo object in different sort method.
	 */
	
	private ArrayList<SongInfo> sortedByTitle;
	private ArrayList<SongInfo> sortedByArtist;
	//TreeMap sortedByTag key = Tag, value = trackId. 
	private TreeMap<String, TreeSet<String>> sortedByTag;
	
	public SortedSongs() {
		this.sortedByTitle = new ArrayList<SongInfo>();
		this.sortedByArtist = new ArrayList<SongInfo>();
		this.sortedByTag = new TreeMap<String, TreeSet<String>>();
	}
	
	public void addSong(SongInfo newSong) {
//		System.out.println("add Song invoked");
		this.addTitle(newSong); 		
		this.addArtist(newSong);
		this.addTag(newSong);
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
		this.sortedByTitle.add(newSong);
		//Collections! (!= Collection)
		Collections.sort(sortedByTitle, new Comparator<SongInfo>() {
			public int compare(SongInfo song1, SongInfo song2) {
				//Compare the title first;
				if(song1.getTitle().compareTo(song2.getTitle()) != 0) {
					return song1.getTitle().compareTo(song2.getTitle());
				} else if(song1.getArtist().compareTo(song2.getArtist()) != 0) {
					//If they have same title, compare the artist.
					return song1.getArtist().compareTo(song2.getArtist());
				} else {
					//If they have both same title, and artist, we compare its TrackId.
					return song1.getTrackId().compareTo(song2.getTrackId());
				}
			}
		});
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
		this.sortedByArtist.add(newSong);
		Collections.sort(sortedByArtist, new Comparator<SongInfo>() {
			public int compare(SongInfo song1, SongInfo song2) {
				//Compare the artist first.
				if(song1.getArtist().compareTo(song2.getArtist()) != 0) {
					return song1.getArtist().compareTo(song2.getArtist());
				} else if(song1.getTitle().compareTo(song2.getTitle()) != 0) {
					//If they have same artist, compare its title.
					return song1.getTitle().compareTo(song2.getTitle());
				} else {
					//If they have same artist and title, comapare is TrackId.
					return song1.getTrackId().compareTo(song2.getTrackId());
				}
			}
		});
	}
	
	//this method add SongInfo object into treemap and sort its key and its  
	//value (arraylist).  
	private void addTag(SongInfo newSong) {
		String key = new String();
		JsonArray tags = newSong.getTag();
		for(JsonElement tag: tags) {
			if(tag != null) {
				if(tag.isJsonArray()) {
					key = ((JsonArray)tag).get(0).getAsString();
				}
			}
			//If there already exist that key, we update the TreeSet of the key.
			//If there is not taht key, we set the key and its value.
			if(this.sortedByTag.containsKey(key) && key != null) {
				TreeSet<String> value = this.sortedByTag.get(key);
				value.add(newSong.getTrackId());
			} else if(key != null) {
				TreeSet<String> value = new TreeSet<String>();
				value.add(newSong.getTrackId());
				this.sortedByTag.put(key, value);
			}
		}
	}
	
	//These method is for getting wanted songs' ArrayList.
	
	public ArrayList<SongInfo> getSortedByTitle() {
		return this.sortedByTitle;
	}
	
	public ArrayList<SongInfo> getSortedByArtist() {
		return this.sortedByArtist;
		
	}
	
	public TreeMap<String, TreeSet<String>> getSortedByTag() {
		return this.sortedByTag;
	}
	
}
