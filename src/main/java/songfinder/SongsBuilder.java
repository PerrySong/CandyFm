package songfinder;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Stream;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Socket.HTTPFetcher;
import concurrent.ExecutorService;
import exception.AddToQueueException;
import exception.LastFmException;

public class SongsBuilder {
	/*
	 * This class provides method buildMusicLibrary and getSongsLibrary.
	 * The constructor take the root directory as input, initialize the private data member: directory. 
	 * Method BuildMusicLibrary take the number of threads as input, process each json file through certain amount of threads.
	 * Method getSongsLibrary is to return the songsLibrary we have built.
	 * 
	 * Method: buildMusicLibrary, getSongsLibrary.
	 */
	
	private SongsLibrary songsLibrary; 
	private String directory;
	private ArtistsLibrary artistsLibrary;
	//Method: SongsBuilder
	
	public SongsBuilder(String directory) {
		this.songsLibrary = new SongsLibrary(); 
		this.directory = directory;
		this.artistsLibrary = new ArtistsLibrary();
	}
	
	//This method is to process files in a given threads' number.
	public void buildMusicLibrary(int threads) {
		Path path = Paths.get(this.directory);
		ExecutorService threadPool = new ExecutorService(threads);
		//We process each json file that can be found under this directory.
		try(Stream<Path> paths = Files.walk(path)) {
			paths.forEach(p -> {
				try {
					threadPool.execute(new Worker(songsLibrary, p));
				} catch (AddToQueueException e) {
					System.out.println(e.getMessage());
				}
			});
		} catch(IOException e) {
			System.out.println("Input directory is invalid" + e);
		}
		//After we call .shutdown(), the queue should not accept another request. 
		threadPool.shutdown();
		//When we call .awaitTermination(), we wait all the executing threads to finish and join all the threads. 
		threadPool.awaitTermination();
	}	
		
	//This method only for download the artists information from last.fm api.
	//Take tread number as input, store the file in "/artist"
	@SuppressWarnings("deprecation")
	public void buildArtistLibrary(int thread) {
		Set<String> artistList = this.songsLibrary.listArtists();
		ExecutorService threadPool = new ExecutorService(10);
		for(String artist: artistList) {
			try {
				threadPool.execute(new FetchWorker(this.artistsLibrary, artist));
			} catch (AddToQueueException e) {
				e.printStackTrace();
			}
		}
		//After we call .shutdown(), the queue should not accept another request. 
		threadPool.shutdown();
		//When we call .awaitTermination(), we wait all the executing threads to finish and join all the threads. 
		threadPool.awaitTermination();
	}
	

	//This method read artist information in the artist's folder,and build(10 threads) a artist library.
	//Argument: artist's folder directory. 
	public void buildArtistsLibrary(String directory) {
		Path path = Paths.get(directory);
		ExecutorService threadPool = new ExecutorService(10);
		try(Stream<Path> paths = Files.walk(path)){
			//For each path, we convert the artist information json File to artistInfo object, and add it to artistLibrary.
			paths.forEach( p -> {
				try {
					threadPool.execute(new ArtistWorker(this.artistsLibrary, p));
				} catch (AddToQueueException e) {
					System.out.println("Exception in buildArtistsLibrary!! " + e.getMessage());
				}
			});
		} catch(IOException e) {
			System.out.println("BuildArtistsLibrary Exception!!" + e.getMessage());
		}
		//After we call .shutdown(), the queue should not accept another request. 
		threadPool.shutdown();
		//When we call .awaitTermination(), we wait all the executing threads to finish and join all the threads. 
		threadPool.awaitTermination();
	}
	
	public SongsLibrary getSongsLibrary() {
		synchronized(this.songsLibrary) {
			return this.songsLibrary;
		}
	}
	
	public ArtistsLibrary getArtistsLibrary() {
		synchronized(this.artistsLibrary) {
			return this.artistsLibrary;
		}
	}
	
	public static void main(String[] args) {
		SongsBuilder builder = new SongsBuilder("input");
        builder.buildMusicLibrary(10);
        //Build ArtistLibrary stucked in the Http HTTPfetcher line: 36
        builder.buildArtistLibrary(10);
        builder.buildArtistsLibrary("artists");
        ArtistsLibrary lb = builder.getArtistsLibrary();
        for(String s: builder.songsLibrary.listArtists()) {
        		String[] sa = s.split("&");
        		for(int i = 0; i < sa.length; i++) {
        			if(!builder.getArtistsLibrary().listAllArtists().contains(sa[i].toLowerCase().trim())) System.out.println("88888" + sa[i].toLowerCase());
        		}
        }
        System.out.println("**********************");

//        for(String a: lb.listAllArtists()) System.out.println(a);
        System.out.println("finished!!!");
        System.out.println("(hed) Planet Earth".equals("(Hed) Planet Earth"));
		
		
	}
}
