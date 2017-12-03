package songfinder;

import java.util.Comparator;
/*
 * This class is for sorting artists by their play count from large to small. 
 */

public class SortedByCount implements Comparator<ArtistInfo>{
	public int compare(ArtistInfo a1, ArtistInfo a2) {
		//If two artist have same amount of play count, we sort it alphabetically.
		if(a2.getPlayCount() - a1.getPlayCount() == 0) return a1.getName().compareTo(a2.getName());
		return a2.getPlayCount() - a1.getPlayCount();
	}
}
