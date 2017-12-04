package userInterface;

import java.io.IOException;

import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Socket.HTTPFetcher;
import UserInfo.UserAccounts;
import exception.LastFmException;
import songfinder.ArtistInfo;
import songfinder.ArtistsLibrary;


public class Artist extends BaseServlet{
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		ArtistsLibrary artistsLibrary = (ArtistsLibrary)getServletConfig().getServletContext().getAttribute(ARTISTS);
		UserAccounts accounts = (UserAccounts)getServletConfig().getServletContext().getAttribute(USER);
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		String artistName = java.net.URLDecoder.decode(request.getParameter("artistName"), "UTF-8");//Reference: https://stackoverflow.com/questions/6138127/how-to-do-url-decoding-in-java
		
		System.out.println(artistName.toLowerCase());
		ArtistInfo artist = artistsLibrary.getArtist(artistName.toLowerCase());
		

		if(request.getParameter("addToFav") != null && request.getParameter("addToFav").equals("true")){
			accounts.addFavoriteArtist(username, artist);
		}
		
		
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("Artist Information"));
		out.println("<a href=\"/search\">Main Page</a>");
		out.println("<br><a href=\"/artists\">All Artists</a>");
		if(artist == null) {
			out.println("<br><br><h1>Sorry, we currently do not have this artist information.</h1>");
			return;
		}
		out.println("<h1>Artist Information</h1><br>");
		out.println("<h5>Artist Name: " + artist.getName() + "</h5><br><img src=\""+ artist.getImage() +"\">");
		//This button allow login user to add this artist to his favorite list
		if(session.getAttribute(LOGIN) != null && (boolean)session.getAttribute(LOGIN)) {
			out.println("<form action=\"/artist\" method=\"get\">"
					+ "<input type=\"hidden\" name=\"addToFav\" value=\"true\">"
					+ "<input type=\"hidden\" name=\"artistName\" value=\""+ URLEncoder.encode((artist.getName()), "UTF-8") +"\">"
					+ "<button>Add to Favorite List!</button></form>");//IMPORTANT!!! Use <input type = submit> will cause your parameter become unreachble  
		}
		out.println("<br><h5>Number of listeners: " + splitNumber(artist.getListeners()) + "</h5>");
		out.println("<h5>Play count: " + splitNumber(artist.getPlayCount()) + "</h5>");
		out.println("<h5>Biography: </h5><p>" + artist.getBio() + "</p>");
		
		out.println(this.footer());
	}
	public void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
	
//	public String getArtistImage(String artist) {
//		String apiUrl = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + artist + "&api_key=208cc84232196cb023b4280f4fbdafdb&format=json";
//		JsonObject artistInfo = new JsonObject(apiUrl);
//		
//		
//		return "";
//	}
	
	/*The artist.getInfo link: https://www.last.fm/api/show/artist.getInfo
	 * Example to utilize the lastFm API to extract the image:
	  	public List<LastFmImage> getLastFmImages(String artistName, int limit, int page) throws IOException {
    			String apiUrl = "http://ws.audioscrobbler.com/2.0/?method=artist.getimages&artist="
            + URLEncoder.encode(artistName)
            + "&api_key="
            + APIKEY
            + "&limit=" + limit + "&page=" + page;

    			Document doc = Jsoup.connect(apiUrl).timeout(20000).get();
	 */
	private String splitNumber(int num) {
		StringBuffer str = new StringBuffer(String.valueOf(num));
		str = str.reverse();
		for(int i = 3; i < str.length(); i = i + 4) {
			str.insert(i, ",");
		}
		return str.reverse().toString();
	}
}
