package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import songfinder.SongsLibrary;

public class ArtistList extends BaseServlet{
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("SongFinder"));
		SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(DATA);
		Set<String> artistsList = library.listArtists();
		out.println(this.header("SongFinder"));
		out.println("<h1>ARTIST LIST</h1><br><a href=\"search\">Get Back</a></br><p><table border=\"3\"><thead><th>number</th><th>Artist's Name</th><thead><tbody>");
		int number = 1;
		for(String artist: artistsList) {
			out.println("<tr><td>" + number + "</td><td>" + artist + "</td></tr>");
			number++;
		}
		out.println("</tbody><table>");
		out.println(this.footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

	}
	
}	
