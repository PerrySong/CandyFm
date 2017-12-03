package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import UserInfo.UserAccounts;
import songfinder.ArtistInfo;
import songfinder.ArtistsLibrary;
import songfinder.SongInfo;

public class Favorite extends BaseServlet{
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		ArtistsLibrary artistsLibrary = (ArtistsLibrary) getServletConfig().getServletContext().getAttribute(ARTISTS);
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		if(request.getParameter("delete") != null) {
			userData.deleteFavoriteArtist(username, Integer.parseInt(request.getParameter("delete")));
		}
		LinkedList<ArtistInfo> fArtists = userData.getFavoriteArtists(username);
		//This is for deleting.
		//This is for pagination
		LinkedList<String> table = new LinkedList<String>();
		int i = 0; 
		for(ArtistInfo artist: fArtists) {
			table.add("<tr><td><img src=\"" + artist.getImage() + "\"></td>");
			table.add("<td><a href=\"artist?artistName=" + artist.getName() + "\">" + artist.getName() + "</a></td>");
			table.add("<td><form action=\"favorite?\" method=\"get\"><input type=\"hidden\" name=\"delete\" value=\"" + i++ + "\"><button>Don't love it anymore!</button></form></td></tr>");
		}
		
		int page;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;// Initialize the page in the first page.
		}
		int start = (page - 1)*20;// The songs table's first song's location of the variable (LinkList)table; 

		PrintWriter out = this.prepareResponse(response);
		out.println(header("Favorite"));
		out.println("<a href=\"search\">Main Page</a></br>");//Main Page
		out.println("<div><h1>My Favorite Songs</h1></div>");
		out.print("<form action=\"favorite\" method=\"post\"><button>View My Favorite Songs</button></form>");
		out.print("<table>");
		//Print 20 item for one page.
		for(int j = start; j < table.size() && j < start + 20; j++) {
			out.println(table.get(j));
		}
		out.println("</table>");
		out.println("Current Page: " + page + " Total Page: " + (int)Math.ceil((double)table.size()/20));
		if(start > 0) {//If this is first page, we do not need the prev button.
			out.println("<form action=\"/favorite\" method=\"get\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page -1) + "\">");
			out.println("<button>Prev</button>");
			out.println("</form>");
		}
		if(start < table.size() - 20) {//If there is no song on the next page, we do not need the next button. 
			out.println("<form action=\"/favorite\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page + 1) + "\">");
			out.println("<button>Next</button>");
			out.println("</form>");
		}
		
	}
	
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		ArtistsLibrary artistsLibrary = (ArtistsLibrary) getServletConfig().getServletContext().getAttribute(ARTISTS);
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		//
		if(request.getParameter("delete") != null) {
			userData.deleteFavoriteSong(username, Integer.parseInt(request.getParameter("delete")));
		}
		//This line must below the upper statement!!!! IMPORTANT!
		LinkedList<SongInfo> fSongs = userData.getFavoriteSongs(username);
		
		System.out.println("hey" + request.getParameter("delete"));
		//This is for deleting.
		
		
		//This is for pagination
		LinkedList<String> table = new LinkedList<String>();
		int i = 0; 
		for(SongInfo song: fSongs) {
			table.add("<tr><td><a href=\"/SongInformation?trackId=" + song.getTrackId() + "\">" + song.getTitle()+ "</a></td>");
			table.add("<td><form action=\"favorite?delete=" + i++ + "\" method=\"post\"><button>Don't love it anymore!</button></form></td></tr>");
		}
		
		int page;
		if(request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));
		} else {
			page = 1;// Initialize the page in the first page.
		}
		int start = (page - 1)*20;// The songs table's first song's location of the variable (LinkList)table; 

		PrintWriter out = this.prepareResponse(response);
		out.println(header("Favorite"));
		out.println("<a href=\"search\">Main Page</a></br>");//Main Page
		out.println("<div><h1>My Favorite Artists</h1></div>");
		out.print("<form action=\"favorite\" method=\"get\"><button>View My Favorite Artists</button></form>");
		out.print("<table>");
		//Print 20 item for one page.
		for(int j = start; j < table.size() && j < start + 20; j++) {
			out.println(table.get(j));
		}
		out.println("</table>");
		out.println("Current Page: " + page + " Total Page: " + (int)Math.ceil((double)table.size()/20));
		if(start > 0) {//If this is first page, we do not need the prev button.
			out.println("<form action=\"/favorite\" method=\"get\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page -1) + "\">");
			out.println("<button>Prev</button>");
			out.println("</form>");
		}
		if(start < table.size() - 20) {//If there is no song on the next page, we do not need the next button. 
			out.println("<form action=\"/favorite\" method=\"post\">");
			out.println("<input type=\"hidden\" name=\"page\" value=\"" + (page + 1) + "\">");
			out.println("<button>Next</button>");
			out.println("</form>");
		}
	}
}
