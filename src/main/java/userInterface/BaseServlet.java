package userInterface;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/*
 * Base class for all Servlets in this application.
 * Provides general helper methods.
 */
public class BaseServlet extends HttpServlet {
	
	public static final String SONGS = "songs";
	public static final String ARTISTS = "artists";
	public static final String NAME = "name";
	
	public static final String UUID = "uuid";
	public static final String ITEM = "item";
	public static final String PRIVATE = "private";
	public static final String MODE = "mode";
	public static final String USER = "user";
	public static final String USERNAME = "userName";
	public static final String LOGIN ="login";
	public static final String ADMINISTRATORS = "administrators";
	
	public static final String DELETE = "delete";
	public static final String STATUS = "status";
	public static final String ERROR = "error";
	public static final String NOT_LOGGED_IN = "not_logged_in";
	public static final String HISTORY ="history";
	
	/*
	 * Prepare a response of HTML 200 - OK.
	 * Set the content type and status.
	 * Return the PrintWriter.
	 */
	protected PrintWriter prepareResponse(HttpServletResponse response) throws IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		return response.getWriter();

	}
	
	/*
	 * Return the beginning part of the HTML page.
	 * Css reference: W3School
	 */
	protected String header(String title) {
		return "<html><head><title>" + title + "</title>"
				+ "<style>\n"
				+ "body {\n background-image: url(\"https://cms-assets.tutsplus.com/uploads/users/107/posts/26488/image/41-space-scrolling-background850.jpg\"); background-color: #B4D7FB; background-repeat: no-repeat; background-size: 1500px 7400px;\n color: white;}\n"
				+ "a:link, a:visited{\n background-color: #f44336; color: white; padding: 8px 20px; text-align: center; text-decoration: none; display: inline-block }\n"
				+ "a:hover, a:active { background-color: #0E906B;}\n"
				+ "body { font-size: 200%;}\n"
				+ "table {font-family: arial, sans-serif; border-collapse: collapse;width tr:nth-child(even) { background-color: tomato; }}\n" 
				+ ".button { -webkit-transition-duration: 0.4s;  transition-duration: 0.4s; border-radius: 4px; padding: 20px;}\n"
//				+ "form, input[type=text] { background-color: #3CBC8D; color: white;}"
				+ "input[type=text], input[type=password] {padding: 5px 10px;}"
			    + "button:hover { background-color: #4CAF50;color: white;}"
				+ "input[type=submit] { background-color: #4CAF50;color: white;}"
			    + "</style></head><body>";
	}
		
	
	
	/*
	 * Return the last part of the HTML page. 
	 */
	protected String footer() {
		return "</body></html>";
	}
	
	/*
	 * Given a request, return the name found in the 
	 * Cookies provided.
	 */
	protected String getCookieValue(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();

		String name = null;
		
		if(cookies != null) {
			//for each cookie, if the key is name, store the value
			for(Cookie c: cookies) {
				if(c.getName().equals(key)) {
					name = c.getValue();
				}
			}
		}
		return name;
	}
	
	/*
	 * Given a request, return the value of the parameter with the
	 * provided name or null if none exists.
	 */
	protected String getParameterValue(HttpServletRequest request, String key) {
		return request.getParameter(key);
	}
		
	
	
	public void main(String[] args) {
		
	}
}
