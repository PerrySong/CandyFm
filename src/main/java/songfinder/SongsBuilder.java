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
	
	
	public void buildMusicLibrary(int threads) {
		Path path = Paths.get(this.directory);
		ExecutorService threadPool = new ExecutorService(threads);
		try(Stream<Path> paths = Files.walk(path)) {
			paths.forEach(p -> {
				try {
					threadPool.execute(new Worker(this.getSongsLibrary(), p));
				} catch (Exception e) {
					e.printStackTrace();
				}
				
			});
		} catch(Exception e) {
			System.out.println("Input directory is invalid" + e);
		}	
		threadPool.shutdown();
		threadPool.awaitTermination();
	}	
		
	public SongsLibrary getSongsLibrary() {
		return this.songsLibrary;
	}
	
}
