package songfinder;

import java.io.File;
import java.io.FileReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CommandParser {
	
/*
 * This class convert string command to HashMap. You can get order, input and output.
 * Method: parse, getInput, getOutput, getOrder, getThreads, getSearchOutput, getSearchRequest.
 * 
 * Work flow: initialize CommandParser object -> parse argument -> get(WhateverInfomation). 
 */
	
	private HashMap<String, String> command;
	private JsonObject searchRequest;
	
	public CommandParser() {
		this.command = new HashMap<String, String>();
		this.searchRequest = new JsonObject();
	}
	 
	//This method is to parse the arguments and update data member command.
	public void parse(String[] args) throws Exception {
		if(args.length < 6) {
			throw new Exception("No enough arguements");
		} else {
			//Pair the arguments, so that they associate with the relative information.
			for(int i = 0; i < args.length - 1; i += 2) {
				this.command.put(args[i], args[i + 1]);
			}
			if(!command.keySet().contains("-input") || !command.keySet().contains("-output") || !command.keySet().contains("-order")) {
				throw new Exception("The command format is not correct!");
			} else if(!command.get("-order").equals("title") && !command.get("-order").equals("artist") && !command.get("-order").equals("tag")) {
				throw new Exception("Check 2,4,6 args, they have some problems");
			} 
			
		}
		
		Path path = Paths.get(this.command.get("-searchInput"));
//		.out.println("search queries: " + this.command.get("-searchInput") + "asda");
		if(path.toString().toLowerCase().endsWith(".json")) {	
			File song = path.toFile();
			try(FileReader file = new FileReader(song)) {
				JsonParser parser = new JsonParser();
				JsonElement elt = parser.parse(file);
				this.searchRequest = (JsonObject)elt;
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
	
	
	
	public String getSearchOutput() {
		return this.command.get("-searchOutput");
	}
	
	public JsonObject getSearchRequest() {
		return this.searchRequest;
	}
	
}
