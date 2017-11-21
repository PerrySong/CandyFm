package userInterface;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Artist extends BaseServlet{
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = this.prepareResponse(response);
		out.println(this.header("Artist Information"));
		out.println("<h1>Artist Information</h1><br>");
		out.println("Artist Name: " + request.getParameter("artistName"));
		out.println(this.footer());
	}
	public void  doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
	}
}
