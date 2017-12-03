package userInterface;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalTime;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import UserInfo.UserAccounts;
import songfinder.SongsLibrary;



public class Login extends BaseServlet {
	/*
	 * (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * path: /login
	 * This class is for user login
	 */
	
	
	
	public void  doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		PrintWriter out = this.prepareResponse(response);
		out.println(header("Login"));
		//Body of the html
		out.println("<h1>Login</h1>");
		out.println("<form action=\"/login\" method=\"post\">");
		out.println("<label>User Name: <input type=\"text\" name=\"username\"><label>");
		out.println("<label>Password: <input type=\"password\" name=\"password\"></label>");
		out.println("<div class=\"button\"><button>submit</button></div>");
		out.println("</from>");
		out.println(footer());
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		UserAccounts userData = (UserAccounts) getServletConfig().getServletContext().getAttribute(USER);
		HttpSession session = request.getSession();
		PrintWriter out = this.prepareResponse(response);
		//Get username and password from request
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		out.println(header("login"));
		//If login successfully.
		if(userData.login(username, password)) {
			out.println("<h1>Hi! " + username + "</h1>");
			out.println("<div><a href=\"/search\"><h4>Let's start it!!</h4></a></div>");
			//When logging in, set user name in session.
			session.setAttribute(LOGIN, true);
			session.setAttribute(USERNAME, username);
			//If fail to login, we provide a sign in option.
			userData.setLastVisitTime(username);
		} else {
			out.println("<h1>Wrong username or password!!!</h1>");
			out.println("<br><a href=\"/login\">Try again</a>");
			out.println("<br>Do not have an account? Let's <a href=\"/signin\">Sign in</a>");
		}
	}
	
}
