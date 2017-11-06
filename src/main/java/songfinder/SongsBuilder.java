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
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import threadpool.ExecutorService;

public class SongsBuilder {
	
	private SongsLibrary songsLibrary; 
	private String directory;
	public SongsBuilder(String directory) {
		this.songsLibrary = new SongsLibrary(); 
		this.directory = directory;
	}
	
	//This method take path as input, return SongInfo for the song under this path.
//	private SongInfo songParser(Path path) {
//		if(path.toString().toLowerCase().endsWith(".json")) {	
//			File song = path.toFile();
//			try(FileReader file = new FileReader(song)) {
//				JsonParser parser = new JsonParser();
//				JsonElement elt = parser.parse(file);
//				if(elt.isJsonObject()) {
//					JsonObject jObject = (JsonObject)elt;
//					String artist = jObject.get("artist").getAsString();
//					String title = jObject.get("title").getAsString();
//					String trackId = jObject.get("track_id").getAsString();
//					JsonArray tags = jObject.get("tags").getAsJsonArray();
//					SongInfo newSong = new SongInfo(artist, title, tags, trackId);
//					return newSong;
//				}
//			} catch(IOException ioe) {
//				System.out.println("Exception in processPath: " + ioe);
//			}
//		}
//		return null;
//	}
	
	//This method takes path as an input, add new songInfo object to the data menber - songLibrary.
	//This method is only invoked in the readFile method.
//	private void processPath(Path path) {
//		SongInfo newSong = this.songParser(path);
//		if(newSong != null) songsLibrary.addSong(newSong);
//		
//	}
	
	// This method take a directory as an input, walk trough each file in this directory, and
	// call processPath method to convert each file to songInfo then add it to songLibrary.
	//This method is only invoked in this class constructor.
	public void buildMusicLibrary(int threads) {
//		Path path = Paths.get(directory);
//		try(Stream<Path> paths = Files.walk(path)){
//			paths.forEach(p -> processPath(p));
//			
//		} catch(Exception e) {
//			System.out.println("Input directory is invalid " + e);
//		}
//		
		
		Path path = Paths.get(this.directory);
		
		ExecutorService threadPool = new ExecutorService(threads);
		
		try(Stream<Path> paths = Files.walk(path)) {
			paths.forEach(p -> {
				try {
					threadPool.execute(new Worker(this.getSongsLibrary(), p));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			});
		} catch(Exception e) {
			System.out.println("Input directory is invalid" + e);
		}
		
		
			
		threadPool.shutdown();
		threadPool.awaitTermination();
		
//This is a unlimited thread method 		
//		try(Stream<Path> paths = Files.walk(path)) {
//			paths.forEach(p -> {
//			Thread t = new Thread(new Worker(this.getSongsLibrary(), p));
//	
//			this.threadList.add(t);
//			
//			t.start();
//			});
//			
//			for(Thread thread: threadList) {
//				
//				try {
//					thread.join();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//				
//			}
//			
//		} catch(Exception e) {
//			System.out.println("Input directory is invalid " + e);
//		}
		//Join every thread.
		
	}	
		
	public SongsLibrary getSongsLibrary() {
		return this.songsLibrary;
	}
	
}
