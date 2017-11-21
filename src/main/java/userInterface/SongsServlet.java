package userInterface;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


import songfinder.SongsLibrary;


public class SongsServlet extends BaseServlet {
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("SongFinder"));
		out.println("<h1>Song Finder</h1>");
		out.println("<h6>Welcome to song finder! Search for artist, song title, or tag and we will give you a list of similar songs you might like</6>");
		out.println("<hr>");
		out.println("<form action=\"search\" method=\"post\">");
		//There is a link that provide artistsList.
		out.println("<lable for=\"searchType\">Search Type:</lable>");
		out.println("<select name=\"searchType\">/n<option>Artist</option>/n<option>Song title</option>/n<option>Tag</option>/n</select>");
		out.println("<lable>Query:<input type=\"text\" name=\"query\" placeholder=\"search\"></lable>");
		out.println("<input type=\"submit\" value=\"submit\" name = \"query\"></form>");
		out.println("<a href = \"/artists\">ARTIST LIST</a>");
		out.println(this.footer());
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SongsLibrary library = (SongsLibrary) getServletConfig().getServletContext().getAttribute(DATA);
		HttpSession session = request.getSession();
		//POST /echo
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);//What is that?
		PrintWriter out = response.getWriter();
		String searchType = request.getParameter("searchType");
		String query = request.getParameter("query").trim();
		JsonObject songs = new JsonObject();
		if(searchType.equals("Artist")) {
			songs = library.searchByArtist(query);
		}
		if(searchType.equals("Song title")) {
			songs = library.searchByTitle(query);
		}
		if(searchType.equals("Tag")) {
			songs = library.searchByTag(query);
		}
		JsonArray similars = songs.get("similars").getAsJsonArray();
		out.println("<html><title>SongFinder</title><body>");
		out.println("<h1>Song Finder</h1>");
		out.println("<h6>Welcome to song finder! Search for artist, song title, or tag and we will give you a list of similar songs you might like</6>");
		out.println("<hr>");	
		out.println("<form action=\"search\" method=\"post\">");
		out.println("<lable for=\"searchType\">Search Type:</lable>");
		out.println("<select name=\"searchType\">/n<option>Artist</option>/n<option>Song title</option>/n<option>Tag</option>/n</select>");
		out.println("<lable>Query: <input type=\"text\" name=\"query\" placeholder=\"search\"></lable>");
		out.println("<input type=\"submit\"></form>");
		out.println("<p>Here are some song you might like!</p>");
		out.println("<a href = \"/artists\">ARTIST LIST</a>");
		out.println("<table border=\"3\">");
		out.println("<thead><tr><th>Artist</th><th>Title</th></tr></thead>");
		String artist = new String();
		String title = new String();
		String trackId = new String();
		for(JsonElement obj: similars) {
			artist = ((JsonObject)obj).get("artist").getAsString();
			title = ((JsonObject)obj).get("title").getAsString();
			trackId = ((JsonObject)obj).get("trackId").getAsString();
			//Each artist is a link that has artist information
			out.println("<tr><td> <a href =\"/artist?artistName=" + artist + "\">" + artist + "</a></td><td>" + "<a href =\"/SongInformation?trackId=" + trackId + "\">"+ title + "</td></tr>");
		}
		out.println("</table>");
		out.println("</body></html>");
	}

	
}




