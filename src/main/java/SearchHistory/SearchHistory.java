package SearchHistory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import concurrent.ReentrantLock;

public class SearchHistory {
	/*
	 * This class is for storing user's seach history.
	 */
	private HashMap<String, LinkedList<SingleSearch>> history;
	private HashMap<SingleSearch, Integer> searchTimes; //Account times of search for each particular search
	private ReentrantLock rwl;
	
	public SearchHistory() {
		this.history = new HashMap<String, LinkedList<SingleSearch>>();
		this.searchTimes = new HashMap<SingleSearch, Integer>();
		this.rwl = new ReentrantLock();
	}
	
	public LinkedList<SingleSearch> getUserHistory(String username){
		rwl.lockRead();
		try {
			return history.get(username);
		} finally {
			rwl.unlockRead();
		}
	}
	
	//If history contains the user, return true, otherwise false
	public boolean contains(String username) {
		rwl.lockRead();
		try {
		return this.history.containsKey(username);
		} finally {
			rwl.lockRead();
		}
	}
	
	public void cleanUserHistory(String username) {
		rwl.lockWrite();
		this.history.put(username, new LinkedList<SingleSearch>());
		rwl.unlockWrite();
		
	}
	
	public void add(String username, SingleSearch search) {
		rwl.lockWrite();
		if(history.containsKey(username) && history.get(username) != null) {
			this.history.get(username).add(search);
		} else {
			LinkedList<SingleSearch> newSearch = new LinkedList<SingleSearch>();
			newSearch.add(search);
			this.history.put(username, newSearch);
		}
		boolean updated = false;
		for(SingleSearch s: this.searchTimes.keySet()) {
			if(search.equals(s)) { 
				this.searchTimes.put(s, (this.searchTimes.get(s) + 1));
				updated = true;
				break; 
			}
		}
		if(!updated) searchTimes.put(search, 1);
		rwl.unlockWrite();
	}
	
	private
	/*
	 * Reference: https://stackoverflow.com/questions/2864840/treemap-sort-by-value
	 * Do not really know what is going on.
	 */
	 <K,V extends Comparable<? super V>> TreeSet<Map.Entry<K,V>> entriesSortedByValues(Map<K, V> map) {
		rwl.lockRead();
		try {
			TreeSet<Map.Entry<K,V>> sortedEntries = new TreeSet<Map.Entry<K,V>>(
		        new Comparator<Map.Entry<K,V>>() {
		            @Override public int compare(Map.Entry<K,V> e1, Map.Entry<K,V> e2) {
		                int res = e2.getValue().compareTo(e1.getValue());// We sort it on descending way.
		                if (e1.getKey().equals(e2.getKey())) {
		                    return res; // Code will now handle equality properly
		                } else {
		                    return res != 0 ? res : 1; // While still adding all entries
		                }
		            }
		        }
		    );
		    sortedEntries.addAll(map.entrySet());
		    return sortedEntries;
		} finally {
			rwl.unlockRead();
		}	
	}
	
	public SortedSet<Map.Entry<SingleSearch, Integer>> getOrderedSearchTimes(){
		rwl.lockRead();
		try {
		return entriesSortedByValues(this.searchTimes);
		} finally {
			rwl.unlockRead();
		}
	}
	
}
