package songfinder;

import java.util.HashMap;

public class CommandParser {
	
/*
 * This class convert string command to HashMap. You can get order, input and output.
 * 
 */
	
	HashMap<String, String> command;
	
	public CommandParser() {
		this.command = new HashMap<String, String>();
	}
	 
	//This method is to parse the arguments and update data member command.
	public void parse(String[] args) throws Exception {
		if(args.length < 6) {
			throw new Exception("No enough arguements");
		} else {
			//Pair the arguments, so that they associate with the relative information.
			for(int i = 0; i < args.length; i += 2) {
				try {
					this.command.put(args[i], args[i + 1]);
				} catch(Exception e) {
					System.out.println("The number of arguements should not be odd");
				}
			}
			if(!command.keySet().contains("-input") || !command.keySet().contains("-output") || !command.keySet().contains("-order")) {
				throw new Exception("The command format is not correct!");
			} else if(!command.get("order").equals("title") && !command.get("order").equals("artist") && !command.get("order").equals("tag")) {
				throw new Exception("Check 2,4,6 args, they have some problems");
			} 
		}
	}
	
	public String getInput() {
		return this.command.get("-input");
	}
	
	public String getOutput() {
		return this.command.get("-output");
	}
	
	public String getOrder() {
		return this.command.get("-order");
	}
	
	public String getThreads() {
		return this.command.get("-threads");
	}
	
}
