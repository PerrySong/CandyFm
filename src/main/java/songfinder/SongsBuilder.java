package songfinder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.stream.Stream;

import concurrent.ExecutorService;
import exception.AddToQueueException;

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
	
	//Method: SongsBuilder
	
	public SongsBuilder(String directory) {
		this.songsLibrary = new SongsLibrary(); 
		this.directory = directory;
	}
	
	//This method is to process files in a given threads' number.
	public void buildMusicLibrary(int threads) {
		Path path = Paths.get(this.directory);
		ExecutorService threadPool = new ExecutorService(threads);
		//We process each json file that can be found under this directory.
		try(Stream<Path> paths = Files.walk(path)) {
			paths.forEach(p -> {
				try {
					threadPool.execute(new Worker(this.getSongsLibrary(), p));
				} catch (AddToQueueException e) {
					System.out.println(e.getMessage());
				}
			});
		} catch(Exception e) {
			System.out.println("Input directory is invalid" + e);
		}
		//After we call .shutdown(), the queue should not accept another request. 
		threadPool.shutdown();
		//When we call .awaitTermination(), we wait all the executing threads to finish and join all the threads. 
		threadPool.awaitTermination();
	}	
		
	public SongsLibrary getSongsLibrary() {
		return this.songsLibrary;
	}
	
}
