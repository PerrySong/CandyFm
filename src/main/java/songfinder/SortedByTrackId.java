package songfinder;

import java.util.Comparator;

public class SortedByTrackId implements Comparator<SongInfo>{
	/*
	 * This comparator implements Comparator<SongInfo>, compare SongInfo object base on their trackid.
	 */
	public int compare(SongInfo s1, SongInfo s2) {
		return s1.getTrackId().compareTo(s2.getTrackId());
	}
	
}
