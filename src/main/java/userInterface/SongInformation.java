package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import songfinder.SongInfo;
import songfinder.SongsLibrary;

public class SongInformation extends BaseServlet {
	
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		
		SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(DATA);
		SongInfo song = library.getSong(request.getParameter("trackId"));
		if(song != null) {
			TreeSet<String> similarIds = song.getSimilarId();
			
			out.println(this.header("Song Information"));
			out.println("<h1>Artist Information</h1><br>");
			out.println("Title: " + song.getTitle() + "<br>" + "Artist Name: " + song.getArtist());
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
