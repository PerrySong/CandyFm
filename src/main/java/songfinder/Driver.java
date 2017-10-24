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
		HashMap<String, String> command = new HashMap<String, String>();
		if(args.length < 6) {
			System.out.println("Not enough arguements.");
		} else {
			for(int i = 0; i < 5; i += 2) {
				command.put(args[i], args[i + 1]);
			}
			if(!command.keySet().contains("-input") || !command.keySet().contains("-output") || !command.keySet().contains("-order")) {
				System.out.println("The command format is not correct!");
			//Create a songBuilder in 
			} else {	
				String order = command.get("-order");
				String input = command.get("-input");
				String output = command.get("-output");
				if(!order.equals("title") && !order.equals("artist") && !order.equals("tag")) {
					System.out.println("Check 2,4,6 args, they have some problems");
				} else {
//TODO: this is not really "Command Parsing" functionality. I recommend executing this code in the Driver.					
					SongsBuilder songs = new SongsBuilder(input);
					songs.getSongsLibrary().writeFile(order, output);
				}
			}
		}
	}	
}
