package userInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SearchSongs extends HttpServlet {
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
        
        
        response.setStatus(HttpServletResponse.SC_OK);//What is that?
		
		PrintWriter out = response.getWriter();
		out.println("<html><title>EchoServlet</title><body>");
		out.println("<h1>Song Finder</h1>");
		out.println("<h6>Welcome to song finder! Search for artist, song title, or tag and we will give you a list of similar songs you might like</6>");
		out.println("<hr>");
		out.println("<form action=\"songs\" method=\"get\">");
		out.println("<lable for=\"searchType\">Search Type:</lable>");
		out.println("<select>/n<option>Artist</option>/n<option>Song title</option>/n<option>Tag</option>/n</select>");
		out.println("<lable>Query:<input type=\"text\" name = \"query\"></lable>");
		out.println("<button>Submit</button></form>");
		
		out.println("</body></html>");
	}
	
	public void doPost() {
		
	}
}
