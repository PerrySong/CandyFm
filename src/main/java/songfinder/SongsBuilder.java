package songfinder;
import com.google.gson.JsonArray;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SongsBuilder {
	
	private SortedSongs songsLibrary; 
	
	public SongsBuilder(String directory) {
		this.songsLibrary = new SortedSongs(); 
		this.readFile(directory);
	}
	
	//This method takes path as an input, add new songInfo object to the data menber - songLibrary.
	//This method is only invoked in the readFile method.
	private void processPath(Path path) {
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
					songsLibrary.addSong(newSong);// there is a null pointer exception.
				}
			} catch(IOException ioe) {
				System.out.println("Exception in processPath: " + ioe);
			}
		}
	}
	
	// This method take a directory as an input, walk trough each file in this directory, and
	// call processPath method to convert each file to songInfo then add it to songLibrary.
	//This method is only invoked in this class constructor.
	private void readFile(String directory) {
		Path path = Paths.get(directory);
		try(Stream<Path> paths = Files.walk(path)){
			paths.forEach(p -> processPath(p));
		} catch(Exception e) {
			System.out.println("Input directory is invalid " + e);
		}
	}	
		
	public SortedSongs getSongsLibrary() {
		return this.songsLibrary;
	}
	
}
