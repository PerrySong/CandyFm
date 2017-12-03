package songfinder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ArtistWorker implements Runnable{
	
	private ArtistsLibrary al;
	private Path directory;
	
	public ArtistWorker(ArtistsLibrary al, Path p) {
		this.al = al;
		this.directory = p;
	}
	
	public void run(){
		//If directory is end with .json, convert the path to file.
		if(directory.toString().toLowerCase().endsWith(".json")) {
			//Path -> File -> JsonObject
			File artist = directory.toFile();
			try(FileReader file = new FileReader(artist)){
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject)parser.parse(file);
				//convert jsonObject to artistInfo object.
				String name = object.get("artist").getAsJsonObject().get("name").getAsString().toLowerCase();//Store artists's name as lower case.
				if(name.equals("cigala")) System.out.println("true");;
				int playcount = object.get("artist").getAsJsonObject().get("stats").getAsJsonObject().get("playcount").getAsInt();
				int listeners = object.get("artist").getAsJsonObject().get("stats").getAsJsonObject().get("listeners").getAsInt();
				String bio = object.get("artist").getAsJsonObject().get("bio").getAsJsonObject().get("summary").getAsString();
				String image = object.get("artist").getAsJsonObject().get("image").getAsJsonArray().get(4).getAsJsonObject().get("#text").getAsString();
				ArtistInfo artistResult = new ArtistInfo(name, playcount, listeners, bio, image);
				this.al.addArtist(artistResult);
			} catch (FileNotFoundException e) {
				System.out.println("Exception!! in Artist Worker: " + e.getMessage());
			} catch (IOException e) {
				System.out.println("Exception!! in Artist Worker: " + e.getMessage());
			}
		}	
	}
}
