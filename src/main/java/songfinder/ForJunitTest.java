package songfinder;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import concurrent.ReentrantLock;

public class ForJunitTest {
	private ReentrantLock rwl;
	private SongsLibrary sl;
	
	public ForJunitTest(SongsLibrary sl) {
		this.sl = sl;
		rwl = new ReentrantLock();
	}
	
	//This method take JsonObject as input, which contains the search request and return the search result as JsonObject.
		private JsonObject search(JsonObject request) {
			JsonObject result = new JsonObject();
			JsonArray artists = new JsonArray();
			JsonArray titles = new JsonArray();
			JsonArray tags = new JsonArray();
			JsonArray arSimilars = new JsonArray();
			JsonArray tiSimilars = new JsonArray();
			JsonArray taSimilars = new JsonArray();
			//Individually search by artist, by title and by tag. 
			artists = request.getAsJsonArray("searchByArtist");
			titles = request.getAsJsonArray("searchByTitle");
			tags = request.getAsJsonArray("searchByTag");
			//If the request contains "artist", we search each artist's similar song, and add it to a JsonArray.
			if(artists != null && artists.size() >= 1) {
				for(JsonElement artist: artists) {
					arSimilars.add(this.sl.searchByArtist(artist.getAsString()));	
				}
				//We add the JsonArray to result, associated with "searchByArtist".
				result.add("searchByArtist", arSimilars);
			}
			//If the request contains "tag", we search each tag's similar song, and add it to a JsonArray.
			if(tags != null && tags.size() >= 1) {
				for(JsonElement tag: tags) {				
					taSimilars.add(this.sl.searchByTag(tag.getAsString()));				
				}
				//We add the JsonArray to result, associated with "searchByTag".
				result.add("searchByTag", taSimilars);
			}		
			//If the request contains title, we search each title's similar song, and add it to a JsonArray
			if(titles != null && titles.size() >= 1) {			
				for(JsonElement title: titles) {
					tiSimilars.add(this.sl.searchByTitle(title.getAsString()));
				}
				//We add the JsonArray to result, associated with "searchByTiltle".
				result.add("searchByTitle", tiSimilars);
			}
			return result;
		}
		
		//This method take JsonObject search request and save the result under the given directory.
		public void saveSearchTaskResult(String writePath, JsonObject request) {	
			this.rwl.lockRead();
			//Search songs according to request
			JsonObject result = this.search(request);	
			Path outpath = Paths.get(writePath);
			//Create the file.
			outpath.getParent().toFile().mkdir();
			try(BufferedWriter output = Files.newBufferedWriter(outpath)) {
				//Write the result to the file.
				output.write(result.toString());
			} catch (IOException e) {
				System.out.println("Exception in saveSearchTaskResult in SongLibrary class!! " + e.getMessage());
			}
			this.rwl.unlockRead();
		}
}	
		
		
