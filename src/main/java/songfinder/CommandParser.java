package songfinder;

import java.util.HashMap;

public class CommandParser {
	
	public static void commandParser(String[] args) {
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
					SongsBuilder songs = new SongsBuilder(input);
					songs.writeFile(order, output);
				}
			}
		}	
	}
}