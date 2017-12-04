package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import SearchHistory.SearchHistory;
import SearchHistory.SingleSearch;
import UserInfo.UserAccounts;
import songfinder.SongsLibrary;


public class SongsServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		SearchHistory history = (SearchHistory)getServletConfig().getServletContext().getAttribute(HISTORY);
		SortedSet<Map.Entry<SingleSearch, Integer>> rankedSearch = history.getOrderedSearchTimes();
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		//Initialize the mode as public mode.
		String username = (String)session.getAttribute(USERNAME);
		
		if(session.getAttribute(PRIVATE) == null) {
			session.setAttribute(PRIVATE, false);
		}
//		//If Login && is not private mode, Update history:
//		if((boolean)session.getAttribute(LOGIN) && !(boolean)session.getAttribute(PRIVATE)) {
//			history.add
//		}
		
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("Candy Fm"));
		out.println("<h1>Candy Fm</h1>");
		
		//If the user logged in, we present his/her name and last visit time.
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {
			String lastLoginTime = userData.getLastVisitTime(username);
			out.println("<h3>Hi! " + username + ", you last visit time is: " + lastLoginTime + "</h3>");
			out.println("<a href=\"/logout\">Log out</a>");
		}
		
		out.println("<h6>Welcome to Candy Fm! Search for artist, song title, or tag and we will give you a list of similar songs you might like</6>");
		out.println("<br>Most popular search: <ol type=\"1\">");
		//Print as possible as top 5 popular suggested search.
		int count = 0;
		for(Map.Entry<SingleSearch, Integer> m: rankedSearch) {
			if(++count > 5) break;//If the iterations are more than 5, break the loop.
			out.println("<li>Search Type: " + m.getKey().getSearchType() + " Query: " + m.getKey().getQuery() + " Search Times: " + m.getValue() + "</li>");
		}
		out.println("</ol>");
		out.println("<hr>");
		//Search form
		out.println("<form action=\"search\" method=\"post\">");
		out.println("<lable for=\"searchType\">Search Type: </lable>");
		out.println("<select name=\"searchType\">/n<option>Artist</option>/n<option>Song title</option>/n<option>Tag</option>/n</select>");
		out.println("<lable>Query: <input type=\"text\" name=\"query\" placeholder=\"search\" required></lable>");
		out.println("<button>search</button></form></div>");
		//There is a link that provide artistsList & history. 
		out.println("<a href=\"/artists\">ARTIST LIST</a>");
		
		//Only if user log in, can they check search history.
		//Only if the user log in, they can turn on the private mode 
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {// Check if user has logined.
			out.println("<br><a href=\"/history\">HISTORY</a>");
			out.println("<br><a href=\"/favorite\">FAVORITE</a>");
			//This is for private search mode
			String mode;
			if((boolean)session.getAttribute(PRIVATE)) {
				mode = "Change to Public Mode";
			} else {
				mode = "Change to Private Mode";
			}	
			out.println("<form action=\"private?mode="+ mode +"\" method=\"post\" >");
			out.println("<button>"+ mode +"</button></div>");
			out.println("</form>");
		} else {
			//If the user did not login, provide the following link enable them to login.
			out.println("<br><a href=\"/login\">Log in</a>New bee?<a href=\"/signin\">Sign in</a>");//This is for login
		}
		out.println(this.footer());
	}

	
	
	
	
	@SuppressWarnings("unchecked")
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(SONGS);
        SearchHistory history = (SearchHistory) getServletConfig().getServletContext().getAttribute(HISTORY);
		HttpSession session = request.getSession();
		String username = "";
		String searchType = request.getParameter("searchType");
		String query = request.getParameter("query").trim();
		UserAccounts userData = (UserAccounts)getServletConfig().getServletContext().getAttribute(USER);
		//Part 1:
		//Only if the user log in, we track the history
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {
			//Only if this is not private mode that we update the history
			username = (String) session.getAttribute(USERNAME);
			if(session.getAttribute(PRIVATE) == null || !(boolean)session.getAttribute(PRIVATE)) {
				if(!query.isEmpty()) history.add(username, new SingleSearch(searchType, query)); //Only add to search history when query is not empty.
			}
		}
		
		//Part 2:
		//Search songs according to the search type.
		JsonObject songs = new JsonObject();
		if(searchType.equals("Artist")) {
			songs = library.partialSearchByArtist(query);
		}
		if(searchType.equals("Song title")) {
			songs = library.partialSearchByTitle(query);
		}
		if(searchType.equals("Tag")) {
			songs = library.partialSearchByTag(query);
		}
		
		//Add table information in linkedList
		LinkedList<String> table = new LinkedList<String>();
				
		//List each artit's name. Songs result is according to the search type(see part two)
		Set<String> keys = songs.keySet();
		
		//Add each song information to a line
		String artist = new String();
		String title = new String();
		String trackId = new String();
		for(String key: keys) {
			//Get each artist's similars
			JsonArray s = songs.get(key).getAsJsonObject().get("similars").getAsJsonArray();		
			//For each artist's similars, get each song.
			//Print the artist name on top of the table
			for(JsonElement obj: s) {
				//For each song of each artist's similars.
				artist = ((JsonObject)obj).get("artist").getAsString();
				title = ((JsonObject)obj).get("title").getAsString();
				trackId = ((JsonObject)obj).get("trackId").getAsString();
				//Each artist is a link that has artist information
				table.add("<tr><td>" + key + "</td><td><a href =\"/artist?artistName=" + artist + "\">" + artist + "</a></td><td>" + "<a href =\"/SongInformation?trackId=" + trackId + "\">"+ title + "</td></tr>");
			}
		}
		
		//This is for pagination
		int page;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;// Initialize the page in the first page.
		}
		int startSong = (page - 1)*20;// The songs table's first song's location of the variable (LinkList)table; 
		
		//Part 4:
		//Writing html
		PrintWriter out = this.prepareResponse(response);
		out.println(header("Candy Fm"));
		out.println("<h1>Candy Fm</h1>");
		
		//If the user logged in, we present his/her name and last visit time.
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {
			String lastLoginTime = userData.getLastVisitTime(username);
			out.println("<h3>Hi! " + username + ", you last visit time is: " + lastLoginTime + "</h3>");
			out.println("<a href=\"/logout\">Log out</a>");
		}
		
		out.println("<h6>Welcome to Candy Fm! Search for artist, song title, or tag and we will give you a list of similar songs you might like</6>");
		out.println("<br>Most popular search: <ol type=\"1\">");
		
		//Part 5 suggested search.
		SortedSet<Map.Entry<SingleSearch, Integer>> rankedSearch = history.getOrderedSearchTimes();// This is for suggested search.
		int count = 0;
		for(Map.Entry<SingleSearch, Integer> m: rankedSearch) {
			if(++count > 5) break;//If the iterations are more than 5, break the loop.
			out.println("<li>Search Type: " + m.getKey().getSearchType() + " Query: " + m.getKey().getQuery() + " Search times: " + m.getValue() + "</li>");
		}
		out.println("</ol><hr>");
		//Search form
		out.println("<form action=\"search\" method=\"post\">");
		out.println("<lable for=\"searchType\">Search Type: </lable>");
		out.println("<select name=\"searchType\">/n<option>Artist</option>/n<option>Song title</option>/n<option>Tag</option>/n</select>");
		out.println("<lable>Query: <input type=\"text\" name=\"query\" placeholder=\"search\" required></lable>");
		out.println("<button>search</button></form></div>");
		out.println("<p>Here are some song you might like!</p>");
		//This the lonk for artist list.
		out.println("<a href = \"/artists\">ARTIST LIST</a>");
		
		//If user did not log in, we provide the log in option
		if(session.getAttribute(LOGIN) == null || !(boolean)session.getAttribute(LOGIN)) {
		//This is for login
			out.println("<br><a href=\"/login\">Log in</a>New bee?<a href=\"/signin\">Sign in</a>");
		} 
		
		//Only if user log in, can they check search history.
		//Only if the user log in, they can change the private mode 
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {// check if user login
			out.println("<br><a href=\"/history\">HISTORY</a>");
			out.println("<br><a href=\"/favorite\">FAVORITE</a>");
			String mode;
			if(session.getAttribute(PRIVATE) != null && (boolean)session.getAttribute(PRIVATE)) {
				mode = "Change to Public Mode";
			} else {
				mode = "Change to Private Mode";
			}
			out.println("<form action=\"private?mode="+ mode +"\" method=\"post\" >");
			out.println("<button>"+ mode +"</button></div>");
			out.println("</form>");
		}
		
		//Print the songs' table
		out.println("<table border=\"3\">");
		out.println("<thead><tr><th>Search Query<th>Artist</th><th>Title</th></tr></thead>");
		//Print the table body. The element is from the startSong to starSong+20 of the table.
		for(int i = startSong; i < startSong + 20 && i < table.size() ; i++) {//Prevent index out of bounce exception in the last page.
			out.println(table.get(i));
		}
		//If the startSong is out of bound:
		if(startSong > table.size()) {
			out.println("<h1>No more Song!</h1>");
		}
		
		out.println("</table>");
		
		//Set the next page and previous page:
		//Set a hidden form which sent request to post
		
		out.println("Current Page: " + page + " Total Page: " + (int)Math.ceil((double)table.size()/20));
		
		if(startSong > 0) {//If this is first page, we do not need the prev button.
			
			out.println("<form action=\"/search\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"searchType\" value=\"" + searchType + "\">");//Reference: https://stackoverflow.com/questions/3915917/make-a-link-use-post-instead-of-get
			out.println("<input type=\"hidden\" name=\"query\" value=\"" + query + "\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page -1) + "\">");
			out.println("<button>Prev</button>");
			out.println("</form>");
			
		}
		if(startSong < table.size() - 20) {//If there is no song on the next page, we do not need the next button. 
			
			out.println("<form action=\"/search\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"searchType\" value=\"" + searchType + "\">");//Reference: https://stackoverflow.com/questions/3915917/make-a-link-use-post-instead-of-get
			out.println("<input type=\"hidden\" name=\"query\" value=\"" + query + "\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page + 1) + "\">");
			out.println("<button>Next</button>");
			out.println("</form>");
		}
//		if(startSong > 0)
//			out.println("<a href=\"/search?page=" + String.valueOf(page - 1) + "&searchType=" + searchType + "&query=" + query + "&method=post\">Next Page</a>");
//		if(startSong < table.size())
//			out.println("<a href=\"/search?page=" + String.valueOf(page + 1) + "&searchType=" + searchType + "&query=" + query + "&method=post\">Next Page</a>");
		
		
		
		out.println(footer());
	}

	
	
}




