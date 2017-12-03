package userInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

import SearchHistory.SearchHistory;
import SearchHistory.SingleSearch;
import UserInfo.UserAccounts;
import songfinder.ArtistsLibrary;
import songfinder.CommandParser;
import songfinder.SongsBuilder;

import songfinder.SongsBuilder;
import songfinder.SongsLibrary;
public class ServletServer {
	public static void main(String args[]) throws Exception {
		
		Server server = new Server(8070);
		ServletContextHandler servhandler = new ServletContextHandler(ServletContextHandler.SESSIONS);        
        server.setHandler(servhandler);
        //Build the Songs library 
        SongsBuilder builder = new SongsBuilder("input");
        builder.buildMusicLibrary(10);
        //Build ArtistLibrary stucked in the Http HTTPfetcher line: 36
        builder.buildArtistsLibrary("artist");  
        SongsLibrary songsLibrary = builder.getSongsLibrary();
        ArtistsLibrary artistsLibrary = builder.getArtistsLibrary();
        UserAccounts userLibrary = new UserAccounts();
        Administrator administrators = new Administrator();
        servhandler.addEventListener(new ServletContextListener() {
        		public void contextDestroyed(ServletContextEvent sce) {
        		}
			public void contextInitialized(ServletContextEvent sce) {
				sce.getServletContext().setAttribute(BaseServlet.SONGS, songsLibrary);
				sce.getServletContext().setAttribute(BaseServlet.ARTISTS, artistsLibrary);
				sce.getServletContext().setAttribute(BaseServlet.USER, userLibrary);
				//For history, we use the HashMap and LinkedList to store the history.
				//In HashMap, the key is the user name, the value is the history the user search
				sce.getServletContext().setAttribute(BaseServlet.HISTORY, new SearchHistory());
				sce.getServletContext().setAttribute(BaseServlet.ADMINISTRATORS, administrators);
			}
        	});
		servhandler.addServlet(SongsServlet.class, "/search");
		servhandler.addServlet(ArtistList.class, "/artists");
		servhandler.addServlet(Artist.class, "/artist");
		servhandler.addServlet(SongInformation.class, "/SongInformation");
		servhandler.addServlet(History.class, "/history");
		servhandler.addServlet(PrivateMode.class, "/private");
		servhandler.addServlet(Login.class, "/login");
		servhandler.addServlet(SignIn.class, "/signin");
		servhandler.addServlet(Logout.class, "/logout");
		servhandler.addServlet(Administrator.class, "/administrator");
		servhandler.addServlet(Favorite.class, "/favorite");
		System.out.println("Server is ready");
		server.start();
		server.join();
	}
	
}
