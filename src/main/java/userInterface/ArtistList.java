package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import SearchHistory.SingleSearch;
import songfinder.ArtistInfo;
import songfinder.ArtistsLibrary;
import songfinder.SongsLibrary;

public class ArtistList extends BaseServlet{
	@SuppressWarnings("deprecation")
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("Artits List"));
		SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(SONGS);
		Set<String> artistsList = library.listArtists();
		
		LinkedList<String> table = new LinkedList<String>();
		//This is to store the table body to a LinkedList.
		int number = 0;
		for(String a: artistsList) {
			table.add("<tr><td>" + (++number) + "</td><td>"  + "<a href=\"artist?artistName=" + URLEncoder.encode(a,"UTF-8") + "\">" + a + "</td></tr>");
		}
		
		//This is for pagination
		int page;
		if(request.getParameter("page") != null) {
			try {
				page = Integer.parseInt(request.getParameter("page"));
			} catch(Exception e) {
				page = 1;
			}
		} else {
			page = 1;// Initialize the page in the first page.
		}
		int startArtist = (page - 1)*20;// The songs table's first song's location of the variable (LinkList)table; 
		
		
		out.println(this.header("SongFinder"));
		out.println("<h1>ARTIST LIST</h1><br>");
		out.println("<a href=\"search\">Main Page</a></br><p><table border=\"3\"><thead><th>number</th><th>Artist's Name</th><thead><tbody>");
		out.println("<form action=\"artists\" method=\"post\">");
		out.println("<button>Change to PlayCount Order</button>");
		out.println("</form><br>");
		//Print the table body
		for(int i = startArtist; i < startArtist + 20 && i < table.size() ; i++) {//Prevent index out of bounce exception in the last page.
			out.println(table.get(i));
		}
		//If the startSong is out of bound
		if(startArtist > table.size()) {
			out.println("<h1>No more Song!</h1>");
		}
		out.println("</tbody><table>");
		out.println("Current Page: " + page + " Total Page: " + (int)Math.ceil(table.size()/20.0) + "<br>");
		//This is a to page block
		
		
		if(startArtist > 0) {//If this is first page, we do not need the prev button.
			
			out.println("<form action=\"/artists\" method=\"get\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page -1) + "\">");
			out.println("<button>Prev</button>");
			out.println("</form>");
			
		}
		out.println("<form action=\"/artists\" method=\"get\">");
		out.println("<select name=\"page\">");
		for(int i = 1; i <= (int)Math.ceil(table.size()/20.0); i++) {
			out.println("<option>" + i + "</option>");
		}
		out.println("</select>");
		out.println("<button>To Page</button>");
		out.println("</form>");
		if(startArtist < table.size() - 20) {//If there is no song on the next page, we do not need the next button. 
			
			out.println("<form action=\"/artists\" method=\"get\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page + 1) + "\">");
			out.println("<button>Next</botton>");
			
		}
		out.println(this.footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("Artits List"));
		ArtistsLibrary library = (ArtistsLibrary) getServletConfig().getServletContext().getAttribute(ARTISTS);
		Set<ArtistInfo> artistsList = library.listArtistsByCount();
		//Store the table as a LInkedList<String>
		LinkedList<String> table = new LinkedList<String>();
		for(ArtistInfo a: artistsList) {
			table.add("<tr><td>" + this.splitNumber(a.getPlayCount()) + "</td><td>"  + "<a href=\"artist?artistName=" + a.getName() + "\">" + a.getName() + "</td></tr>");
		}
		
		//This is for pagination
		int page;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;// Initialize the page in the first page.
		}
		int startArtist = (page - 1)*20;// The songs table's first song's location of the variable (LinkList)table; 
		
		out.println(this.header("SongFinder"));
		out.println("<h1>ARTIST LIST</h1><br>");
		out.println("<a href=\"search\">Main Page</a></br><p><table border=\"3\"><thead><th>Play Count</th><th>Artist's Name</th><thead><tbody>");
		out.println("<form action=\"artists\" method=\"get\">");
		out.println("<button>Change to Alphabetical Order</button>");
		out.println("</form><br>");
		
		//Print the table body. The element is from the startSong to starSong+20 of the table.
		for(int i = startArtist; i < startArtist + 20 && i < table.size() ; i++) {//Prevent index out of bounce exception in the last page.
			out.println(table.get(i));
		}
		//If the startSong is out of bound
		if(startArtist > table.size()) {
			out.println("<h1>No more Song!</h1>");
		}
		out.println("</tbody><table>");
		out.println("Current Page: " + page + " Total Page: " + (int)Math.ceil(table.size()/20.0) + "<br>");
		//This is a to page block
		
		
		if(startArtist > 0) {//If this is first page, we do not need the prev button.
			
			out.println("<form action=\"/artists\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page -1) + "\">");
			out.println("<button>Prev</button>");
			out.println("</form>");
			
		}
		out.println("<form action=\"/artists\" method=\"post\">");
		out.println("<select name=\"page\">");
		for(int i = 1; i <= (int)Math.ceil(table.size()/20.0); i++) {
			out.println("<option>" + i + "</option>");
		}
		out.println("</select>");
		out.println("<button>To Page</button>");
		out.println("</form>");
		if(startArtist < table.size() - 20) {//If there is no song on the next page, we do not need the next button. 
			
			out.println("<form action=\"/artists\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page + 1) + "\">");
			out.println("<button>Next</button>");
			
		}
		out.println(this.footer());
	}
	
	//This method is a helper method to split the Number if needed.
	private String splitNumber(int num) {
		StringBuffer str = new StringBuffer(String.valueOf(num));
		str = str.reverse();
		for(int i = 3; i < str.length(); i = i + 4) {
			str.insert(i, ",");
		}
		return str.reverse().toString();
	}
	
}	
