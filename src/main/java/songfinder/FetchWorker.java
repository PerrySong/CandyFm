package songfinder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Socket.HTTPFetcher;
import exception.LastFmException;

public class FetchWorker implements Runnable{
	
	private ArtistsLibrary artistsLibrary;
	private String artist;
	
	public FetchWorker(ArtistsLibrary artistsLibrary, String artist) {
		this.artistsLibrary = artistsLibrary;
		this.artist = artist;
	}
	
	public void run() {
		String apiKey = "208cc84232196cb023b4280f4fbdafdb";
		JsonObject newArtist = new JsonObject();
		try {
			//Some of the songs have multiple artists, we split the artist and down load each artist information separately.
	//		System.out.println(artist);
			if(artist.contains("&")) {
				String[] arList = artist.split("&");
				System.out.println(artist);
				for(String a: arList) {
					//Important!!!!, IF YOU DON'T encode, THE REQUEST IS WRONG!!!!
					String url = URLEncoder.encode("/2.0/?artist=" + artist + 
							"&api_key=" + apiKey + 	
							"&method=artist.getInfo&format=json", "UTF-8");
					
					String jsonInString = HTTPFetcher.download("ws.audioscrobbler.com", url);
					System.out.println(a);
					if(jsonInString != null) {
						JsonParser parser = new JsonParser();
						newArtist = (JsonObject)parser.parse(jsonInString);
					}
					this.artistsLibrary.saveToFile(newArtist, "artist/" + a + ".json");
					System.out.println("saved sucessful");
				}
			} else {
				String url = URLEncoder.encode("/2.0/?artist=" + artist + 
						"&api_key=" + apiKey + 	
						"&method=artist.getInfo&format=json", "UTF-8");
				
				String jsonInString = HTTPFetcher.download("ws.audioscrobbler.com", url);
				System.out.println("Fetch is ok");
				if(jsonInString != null) {
					JsonParser parser = new JsonParser();
					newArtist = (JsonObject)parser.parse(jsonInString);
				}
				System.out.println("parse is ok");
				if(newArtist.get("artist") != null) {
					this.artistsLibrary.saveToFile(newArtist, "artist/" + artist + ".json");
					System.out.println("saved sucessful");
				
				} else {
					System.out.println("This artist has no information!!");
				}
			}	
		} catch (LastFmException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
//	System.out.println("Finished!");
}
