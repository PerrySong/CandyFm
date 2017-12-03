package SearchHistory;

import java.util.Comparator;

public class SingleSearch {
	/*
	 * This class is to store each single search history information.
	 */
	private String searchType;
	private String query;
//	private int searchTimes;
	public SingleSearch(String searchType, String query) {
		this.searchType = searchType;
		this.query = query;
//		searchTimes = 1;
	}
	
	public String getSearchType() {
		return this.searchType;
	}
	
	public String getQuery() {
		return this.query;
	}
	
	public boolean equals(SingleSearch s) {
		if(this.searchType.equals(s.getSearchType()) && this.query.equals(s.getQuery())) return true;
		return false;
	}
	
//	public void addSearch() {
//		this.searchTimes++;
//	}
//	
//	public int getSearchTimes() {
//		return this.searchTimes;
//	}
	
//	private void setSearchTime(int searchTimes) {
//		this.searchTimes = searchTimes;
//	}
	
//	public int compare(SingleSearch s1, SingleSearch s2) {
//		if(s1.getSearchTimes() != s2.getSearchTimes()) return s1.searchTimes - s2.getSearchTimes();
//		return 1;
//	}

	public SingleSearch clone() {
		SingleSearch result = new SingleSearch(this.searchType, this.query);
		return result; 
	}
	
}
