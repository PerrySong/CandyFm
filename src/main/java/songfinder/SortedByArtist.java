package songfinder;

import java.util.Comparator;

import com.google.gson.JsonArray;

public class SortedByArtist implements Comparator<SongInfo>{
	
	/*
	 * Data sorted by artist will list the artist name, followed by a space, 
	 * followed by -, followed by a space, followed by title, followed by a new 
	 * line, and will be in alphabetical order by artist. If two songs have the 
	 * same artist, they will then be sorted by the title. If two songs have the 
	 * same artist and title, they will be sorted by the track_id. Example:
	 * Steel Rain - Loaded Like A Gun
	 * Tom Petty - A Higher Place (Album Version)
	 */
	//this method compare SongInfo as above.
	
	public int compare(SongInfo s1, SongInfo s2) {
		if(s1.getArtist().compareTo(s2.getArtist()) != 0) {
			return s1.getArtist().compareTo(s2.getArtist());
		} else if(s1.getTitle().compareTo(s2.getTitle()) != 0) {
			return s1.getTitle().compareTo(s2.getTitle());
		} else {
			return s1.getTrackId().compareTo(s2.getTrackId());
		}
	}
	
}
