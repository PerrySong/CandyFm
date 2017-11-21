package songfinder;


import java.util.ArrayList;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


/*
 * This class store single song informations.
 * Public Method: getArtist, getTitle, getTag, getTrackId, getSimilarId, castToJsonObject. 
 */

public class SongInfo {

	private String artist;
	private String title;
	private JsonArray tags;
	private String trackId;
	private JsonArray similarSong;// Format likes that: ["TRFKGXK128F14569CB", 0.0064378400000000002]
	private TreeSet<String> similarSongsId;
	
	public SongInfo(String artist, String title, JsonArray tags, String trackId, JsonArray similarSong) {
		this.artist = artist;
		this.title = title;
		this.tags = tags;
		this.trackId = trackId;
		this.similarSong = similarSong; 
		this.similarSongsId = new TreeSet<String>();
		this.setSimilarId();
	
	}
	
	//This method is to store all similarId of the singInfo object in the data member similarSongId(treeset).
	private void setSimilarId() {
		for(JsonElement e: this.similarSong) {
			if(e != null) {
				if(e.isJsonArray()) {
					this.similarSongsId.add(((JsonArray)e).get(0).getAsString());
				}
			}
		}
	}
	
	public String getArtist() {
		return this.artist;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public JsonArray getTag() {
		return this.tags;
	}
	
	public String getTrackId() {
		return this.trackId;
	}
	
	/**
	 * 
	 * @return similarSongsId of this particular song.
	 */
	public TreeSet<String> getSimilarId() {
		return this.similarSongsId;
	}

	//This method is to cast a songInfo object to JsonObject, which is used in SongsLibrary's method: search().
	public JsonObject castToJsonObject() {
		JsonObject songJson = new JsonObject();
		songJson.addProperty("artist", this.artist);
		songJson.addProperty("trackId", this.trackId);
		songJson.addProperty("title", this.title);
		return songJson;
	}
	
	
	
}
