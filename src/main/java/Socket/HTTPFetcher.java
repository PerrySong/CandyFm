package Socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import com.google.gson.JsonObject;

import exception.LastFmException;

public class HTTPFetcher {
	public static int PORT = 80;
	
	public static String download(String host, String path) throws LastFmException {
		StringBuffer buf = new StringBuffer();
		
		try(
				Socket sock = new Socket(host, PORT);//create a connection to the web server
				OutputStream out = sock.getOutputStream();//get the output stream from socket
				InputStream instream = sock.getInputStream();//get the input stream from socket
				//wrap the input stream to make it easier to read from
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(instream))
		){
			//send request
			
			String request = getRequest(host, path);
			out.write(request.getBytes());
			out.flush();
			System.out.println(path);
			//receive response
			//note: a better approach would be to first read headers, determine content length
			//then read the remaining bytes as a byte stream
			
				String line = reader.readLine();
				
				System.out.println("line is:" + line);
				if(!line.contains("HTTP/1.1 200 OK")) throw new LastFmException(line);
				while(line != null) {
					buf.append(line + "\n");
					line = reader.readLine();
				}
				
				System.out.println("line append ok: ");
				
				int jsonLocation = 0;
				jsonLocation = buf.indexOf("\n\n");
				String json = buf.toString().substring(jsonLocation);
				return json;
		
		} catch (IOException e) {
			System.out.println("HTTPFetcher::download" + e.getMessage());
		}
		return null;
	}
	
	private static String getRequest(String host, String path) {
		String request = "GET " + path + " HTTP/1.1" + "\n" //GET request
						+ "Host: " + host + "\n" //Host header required for HTTP/1.1
						+ "Connection: close\n" //make sure the server closes the connection after we fetch one page
						+ "\r\n";
		return request;
	}
	
//	public static JsonObject downloadAsJson(String host, String path) throws Exception {
//		String info = download(host, path);
//		if()
//		
//		
//		JsonObject result;
//		
//		return  ;
//	}
	
	
		public static void main(String[] args) {		
			//test out the HTTPFetcher
//			String page = HTTPFetcher.download("www.cs.usfca.edu", "/~srollins/test.html");
//			
			String apiKey = "208cc84232196cb023b4280f4fbdafdb";
			String artist = "Cher";
			String page;
			try {
				page = HTTPFetcher.download("ws.audioscrobbler.com", "/2.0?artist=" + artist + 
								"&api_key=" + apiKey + 	
								"&method=artist.getInfo&format=json");
				System.out.println(page);
			} catch (LastFmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
