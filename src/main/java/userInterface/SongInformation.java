package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import UserInfo.UserAccounts;
import songfinder.ArtistInfo;
import songfinder.ArtistsLibrary;
import songfinder.SongInfo;
import songfinder.SongsLibrary;

public class SongInformation extends BaseServlet {
	
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		
		SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(SONGS);
		UserAccounts accounts = (UserAccounts)getServletConfig().getServletContext().getAttribute(USER);
		HttpSession session = request.getSession();
		SongInfo song = library.getSong(request.getParameter("trackId"));
		String username = (String)session.getAttribute(USERNAME);

		if(request.getParameter("addToFav") != null && request.getParameter("addToFav").equals("true")){
			accounts.addFavoriteSong(username, song);
		}
		
		
		if(song != null) {
			TreeSet<String> similarIds = song.getSimilarId();
			out.println(this.header("Song Information"));
			out.println("<a href=\"/search\">Main Page</a>");
			out.println("<h1>Song Information</h1><br>");
			out.println("Title: " + song.getTitle() + "<br>" + "Artist Name: <a href=\"/artist?artistName=" + song.getArtist() + "\">" + song.getArtist() + "</a>");
			if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {
				out.println("<form action=\"/SongInformation\" method=\"get\">"
						+ "<input type=\"hidden\" name=\"addToFav\" value=\"true\">"
						+ "<input type=\"hidden\" name=\"trackId\" value=\""+ song.getTrackId() +"\">"
						+ "<button>Add to Favorite List!</button></form>");//IMPORTANT!!! Use <input type = submit> will cause your parameter become unreachble 
			}
			out.println("<table border=\"3\"><th><h5>TrackId</h5></th><th><h5>Similar Songs</h5></th>");
			for(String id: similarIds) {
				SongInfo similarSong = library.getSong(id);
				if(similarSong != null) out.println("<tr><td>" + id + "</td><td>" + "<a href=\"/SongInformation?trackId=" + similarSong.getTrackId() + "\">" + library.getSong(id).getTitle() + "</a></td></tr>");
			}
			out.println("</table>");
			out.println(this.footer());
		} else {
			out.println(this.header("Song Information"));
			out.println("<h1>Sorry, we do not have this song.</h1>" + request.getParameter("trackId"));
			out.println(this.footer());
		}
	}
	
	public void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
	
}
