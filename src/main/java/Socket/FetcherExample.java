package Socket;

import exception.LastFmException;

public class FetcherExample {
	public static void main(String[] args) {		
		//test out the HTTPFetcher
//		String page = HTTPFetcher.download("www.cs.usfca.edu", "/~srollins/test.html");
//		
		String apiKey = "208cc84232196cb023b4280f4fbdafdb";
		String artist = "Madonna";
		String track = "Holiday";
		String page;
		try {
			page = HTTPFetcher.download("ws.audioscrobbler.com", "/2.0/?method=artist.getinfo&artist=Cher&api_key=208cc84232196cb023b4280f4fbdafdb&format=json");
		} catch (LastFmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(track);
		
	}
}
