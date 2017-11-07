package songfinder;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

/**
 * Main class for SongFinder lab and projects.
 * @author srollins
 *
 */
public class Driver {

	/**
	 * Main method.
	 * @param args
	 */
	
	public static void main(String[] args) {
		
		CommandParser command = new CommandParser();
		try {
			command.parse(args);
			
		} catch (Exception e) {
			e.getMessage();
		}
		String input = command.getInput();
		String order = command.getOrder();
		String output = command.getOutput();
		if(input != null && order != null && output != null) {
			int threads;
			try {
				threads = Integer.parseInt(command.getThreads());
				if(threads < 1 || threads >1000) threads = 10;
			} catch(Exception ignore) {
				threads = 10;
			}
			SongsBuilder songs = new SongsBuilder(input);
			songs.buildMusicLibrary(threads);
			songs.getSongsLibrary().saveToFile(order, output);
		}
	}	
					
}
