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
			//This method is to parse args, it updates the command's data members.
			command.parse(args);
		} catch (Exception e) {
			//If the argument format is not right, we catch the exception.
			e.getMessage();
		}
		String input = command.getInput();
		String order = command.getOrder();
		String output = command.getOutput();
		if(input != null && order != null && output != null) {
			int threads;
			try {
				threads = Integer.parseInt(command.getThreads());
				//If threads is smaller than 1 or bigger than 1000. We set 10 threads as default value.
				if(threads < 1 || threads >1000) threads = 10;
			} catch(Exception ignore) {
				//If the arguments of threads can not be parse in integer(It is a String for example), then we set 10 threads as default value. 
				threads = 10;
			}
			SongsBuilder songs = new SongsBuilder(input);
			//This method will build library in multithread way. 
			songs.buildMusicLibrary(threads);
			songs.getSongsLibrary().saveToFile(order, output);
		}
		
	}	
					
}
