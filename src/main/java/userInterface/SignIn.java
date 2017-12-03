package userInterface;

import java.io.IOException;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import UserInfo.UserAccounts;
import songfinder.SongsLibrary;

public class SignIn extends BaseServlet{
	/*
	 * Link: /singin
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		PrintWriter out = this.prepareResponse(response);
		out.println(header("Sign In"));
		//Body of the html
		out.println("<h1>Sign In</h1>");
		out.println("<form action=\"/signin\" method=\"post\">");
		out.println("<label>User Name: <input type=\"text\" name=\"username\" placeholder=\"username\" required><label>");
		out.println("<label>Password: <input type=\"password\" name=\"password\" placeholder=\"password\" required></label>");
		out.println("<button>submit</button>");
		out.println("</from>");
		out.println(footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		PrintWriter out = this.prepareResponse(response);
		out.println(header("Sign In"));
		if(userData.signIn(username, password)) {
			out.println("<h1>Sign in successful!!</h1>");
			out.println("<br><a href=\"login\">Go Log in !</a>");
			
		} else {
			//If the user name is already exist:
			out.println("<h1>User name already exist</h1>");
			out.println("<div><a href=\"/signin\">Try again</a></div>");
			out.println("<br><a href=\"login\">Log in</a>");
		}
		out.println(footer());
	}
}
