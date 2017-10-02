package songfinder;

import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
public class SortedSongs {
	
	private ArrayList<SongInfo> songsList;
	private ArrayList<SongInfo> sortedByTitle;
	private ArrayList<SongInfo> sortedByArtist;
	//TreeMap sortedByTag key = Tag, value = trackId. 
	private TreeMap<String, TreeSet> sortedByTag;
	
	public SortedSongs() {
	}
	
	public void addSong(SongInfo newSong) {
		this.songsList.add(newSong);
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
	public void sortTitle() {
		this.sortedByTitle = this.songsList;
		//Collections! (!= Collection)
		Collections.sort(sortedByTitle, new Comparator<SongInfo>() {
			public int compare(SongInfo song1, SongInfo song2) {
				if(song1.getTitle().compareTo(song2.getTitle()) != 0) {
					return song1.getTitle().compareTo(song2.getTitle());
				} else if(song1.getArist().compareTo(song2.getArist()) != 0) {
					return song1.getArist().compareTo(song2.getArist());
				} else {
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
	public void sortArist() {
		this.sortedByArtist = this.songsList;
		Collections.sort(sortedByArtist, new Comparator<SongInfo>() {
			public int compare(SongInfo song1, SongInfo song2) {
				if(song1.getArist().compareTo(song2.getArist()) != 0) {
					return song1.getArist().compareTo(song2.getArist());
				} else if(song1.getTitle().compareTo(song2.getTitle()) != 0) {
					return song1.getTitle().compareTo(song2.getTitle());
				} else {
					return song1.getTrackId().compareTo(song2.getTrackId());
				}
			}
		});
	}
	
	public void sortTag() {
		for(SongInfo song: songsList) {
			TreeSet value = this.sortedByTag.get(song.getTag());
			value.add(song.getTrackId());
		}
	}
}
