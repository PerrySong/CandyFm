package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import SearchHistory.SearchHistory;
import SearchHistory.SingleSearch;

public class History extends BaseServlet{
	
	/**
	 * This class provide a servlet that allow user to see the history.
	 */
	
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		SearchHistory history = (SearchHistory)getServletConfig().getServletContext().getAttribute(HISTORY);
		LinkedList<SingleSearch> userHistory = history.getUserHistory(username);
		//Prepare 
		response.setContentType("text/html");
	    response.setStatus(HttpServletResponse.SC_OK);//What is that?
		PrintWriter out = response.getWriter();
		out.println(header("Search History"));
		out.println("<h1>Search History</h1>");
		out.println("<div><a href=\"/search\">Main Page</a></div>");
		//Print a table of link of the search history.
		
		if(userHistory != null && !userHistory.isEmpty()) {
			out.println("<table>");
			for(SingleSearch eachSearch: userHistory) {
				String searchString = "Search Type: " + eachSearch.getSearchType() + " Query:" +  eachSearch.getQuery();
				out.println("<tr><td><a href=\"/search?method=post&searchType="+ eachSearch.getSearchType() + "&query=" + eachSearch.getQuery() + "\">" + searchString +"</td></tr>");
			}
			out.println("</table>");
			out.println("<form action=\"/history\" method=\"post\">");
			out.print("<button>Clean History</botton>");
			out.println("</form>");
		} else {
			out.println("<h3>No search history</h3>");
		}
		out.println(footer());
	}
	
	
	public void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession();
		String username = (String)session.getAttribute(USERNAME);
		SearchHistory history = (SearchHistory)getServletConfig().getServletContext().getAttribute(HISTORY);
		//remove the history by reseting the history 
		history.cleanUserHistory(username);;
		response.setContentType("text/html");
	    response.setStatus(HttpServletResponse.SC_OK);//What is that?
		PrintWriter out = response.getWriter();
		out.println(header("Search History"));
		out.println("<h1>Search History</h1>");
		out.println("<div><a href=\"/search\">Main Page</a></div>");
		out.println("<div><h3>No search history</h3></div>");
		out.println(footer());
		
	}
}
