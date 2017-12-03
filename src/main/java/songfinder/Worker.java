package songfinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Socket.HTTPFetcher;
import exception.LastFmException;

public class Worker implements Runnable{
	
	//This method add songs to SongsLibrary's data structure and write SongsLibrary file.
	//This class take SongsLibrary object and directory as input, find the file and add songInfo to Library.
	
	private SongsLibrary sl;
	
	private Path path;
	
	public Worker(SongsLibrary sl, Path path) {
		this.sl = sl;
		this.path = path;
		
	}
	
	//This method add songInfo to songsLbrary's data structure.
	public void run() {
		if(path.toString().toLowerCase().endsWith(".json")) {	
			File song = path.toFile();
			try(FileReader file = new FileReader(song)) {
				//This part is for building SongsLibrary:
				JsonParser parser = new JsonParser();
				JsonElement elt = parser.parse(file);
				if(elt.isJsonObject()) {
					JsonObject jObject = (JsonObject)elt;
					String artist = jObject.get("artist").getAsString();
					String title = jObject.get("title").getAsString();
					String trackId = jObject.get("track_id").getAsString();
					JsonArray tags = jObject.get("tags").getAsJsonArray();
					JsonArray similarSongs = new JsonArray();
					if(jObject.get("similars").getAsJsonArray() != null) similarSongs = jObject.get("similars").getAsJsonArray();
					SongInfo newSong = new SongInfo(artist, title, tags, trackId, similarSongs);
					sl.addSong(newSong);// there is a null pointer exception.	
				}
			} catch(IOException ioe) {
				System.out.println("Exception in processPath: " + ioe.getMessage());
			}
		}
	}
	
}
