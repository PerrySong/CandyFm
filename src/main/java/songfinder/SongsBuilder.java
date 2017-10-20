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
			
//			System.out.println(song.getName());
			
			try(FileReader file = new FileReader(song)) {
				JsonParser parser = new JsonParser();
				JsonElement elt = parser.parse(file);
//				System.out.println("try file reader successful");
				
				if(elt.isJsonObject()) {
					JsonObject jObject = (JsonObject)elt;
//					System.out.println("elt is JsonObject: " + jObject.get("tags"));
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
		Stream<Path> a;
		try(Stream<Path> paths = Files.walk(path)){
			paths.forEach(p -> processPath(p));
		} catch(Exception e) {
			System.out.println("Input directory is invalid " + e);
			
		}
	}	
	
//TODO: I recommend moving the writeFile functionality to your songs library. It will be more clear why when we
//discuss concurrency.	
	// This method takes sortWay and writePath as parameters, write songs info in the given writePath
	// in a wanted sortWay.
	public void writeFile(String order,String writePath) {
		ArrayList<SongInfo> songsList = new ArrayList<SongInfo>();
		Path outpath = Paths.get(writePath);
		outpath.getParent().toFile().mkdir();
		try(BufferedWriter output = Files.newBufferedWriter(outpath)){
			// write files in different ways according to command.
			//1. When we write a songsByTitle.
			if(order.equals("title")) {
				songsList = this.songsLibrary.getSortedByTitle();
			}	
			//2. When we write a songsByArtist.
			if(order.equals("artist")) {
				songsList = this.songsLibrary.getSortedByArtist();
			}
			// When we want to wirte songsByArtist or songsByTitle.
			if(!order.equals("tag")) {
				//System.out.print(songsList.get(2).getArist());
				for(SongInfo song: songsList) {
					output.write(song.getArtist() + " - " + song.getTitle() + "\n");
					
				}
			} else {
				TreeMap<String, TreeSet<String>> songsMap = songsLibrary.getSortedByTag();
				Set<String> tags = songsLibrary.getSortedByTag().keySet();
				for(String tag: tags) {
					//Write every tag in the songsMap.
					output.write(tag + ": ");
					// For each tag, write every song's trackId which is in that tag.
					for(String trackId: songsMap.get(tag)) {
						output.write(trackId + " ");
					}
					output.write("\n");
				}
			}
		} catch(IOException ioe) {
			System.out.println("Exception in writting file" + ioe);
		}
	}
	
	public void printString() {
		ArrayList<SongInfo> a = songsLibrary.getSortedByTitle();
		ArrayList<SongInfo> b = songsLibrary.getSortedByArtist();
		TreeMap<String, TreeSet<String>> c = songsLibrary.getSortedByTag();
		System.out.print("hey");
		for(SongInfo song: a) {
			System.out.print(song.getArtist() + "printString !");
		}
	}
}
