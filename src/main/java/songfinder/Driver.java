package songfinder;

import java.util.HashMap;

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
		//Create a hashmap whose key are "-input", "-output" and "-order".
		//Check the args format.
		HashMap<String, String> command = new HashMap<String, String>();
		for(int i = 0; i < 5; i += 2) {
			command.put(args[i], args[i + 1]);
		}
		if(!command.keySet().contains("-input") || !command.keySet().contains("-output") || !command.keySet().contains("-order")) {
			System.exit(1);
		}
		//Create a songBuilder in 
		SongsBuilder songs = new SongsBuilder(command.get("-input"));
//		songs.writeFile(command.get("-order"), command.get("-output"));
//
		
	}

}
