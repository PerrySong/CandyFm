package userInterface;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHandler;

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
        SongsLibrary library = builder.getSongsLibrary(); 
        servhandler.addEventListener(new ServletContextListener() {
        		public void contextDestroyed(ServletContextEvent sce) {
        		}
			public void contextInitialized(ServletContextEvent sce) {
				sce.getServletContext().setAttribute(BaseServlet.DATA, library);//Where we initialize the data?
			}
        	});
		servhandler.addServlet(SongsServlet.class, "/search");
		servhandler.addServlet(ArtistList.class, "/artists");
		servhandler.addServlet(Artist.class, "/artist");
		servhandler.addServlet(SongInformation.class, "/SongInformation");
		server.start();
		server.join();
	}
	
}
