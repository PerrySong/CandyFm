package songfinder;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class Worker implements Runnable{
	
	//This method add songs to SongsLibrary's data structure and write SongsLibrary file.
	//This class take SongsLibrary object and directory as input, find the file and add songInfo to Library.
	
	private SongsLibrary sl;
	private Path path;
	
	public Worker(SongsLibrary sl, Path path) {
		this.sl = sl;
		this.path = path;
	}
	
	//This run add songInfo to songsLbrary.
	public void run() {
		if(path.toString().toLowerCase().endsWith(".json")) {	
			File song = path.toFile();
			try(FileReader file = new FileReader(song)) {
				JsonParser parser = new JsonParser();
				JsonElement elt = parser.parse(file);
				if(elt.isJsonObject()) {
					JsonObject jObject = (JsonObject)elt;
					String artist = jObject.get("artist").getAsString();
					String title = jObject.get("title").getAsString();
					String trackId = jObject.get("track_id").getAsString();
					JsonArray tags = jObject.get("tags").getAsJsonArray();
					SongInfo newSong = new SongInfo(artist, title, tags, trackId);
					sl.addSong(newSong);// there is a null pointer exception.
				}
			} catch(IOException ioe) {
				System.out.println("Exception in processPath: " + ioe);
			}
		}
	}
	
}
