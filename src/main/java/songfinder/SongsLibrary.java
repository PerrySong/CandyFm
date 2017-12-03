package songfinder;

import java.util.TreeMap;
import java.util.TreeSet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import concurrent.ReentrantLock;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;
public class SongsLibrary {
	
	/*
	 * This class store SongInfo object in different sort method.
	 * method: addSong, saveToFile, searchByArtist, searchByTitle, 
	 * searchByTag, search, saveSearchTaskResult
	 */

	private TreeMap<String, TreeSet<SongInfo>> sortedByTitleMap;
	private TreeMap<String, TreeSet<SongInfo>> sortedByArtistMap;
	//TreeMap sortedByTag key = Tag, value = trackId. 
	private TreeMap<String, TreeSet<SongInfo>> sortedByTagMap;
	private TreeMap<String, SongInfo> trackIdMap;
	private HashMap<String, String> artistParser;//The key is lower case value is normal case
	private HashMap<String, String> titleParser;//The key is lower case value is normal case
	private HashMap<String, String> tagParser;//The key is lower case value is normal case
	
	private ReentrantLock rwl;
	private final SortedByTitle sbt;
	private final SortedByArtist sba;
	private final SortedByTrackId sbti;
	
	public SongsLibrary() {
		this.sortedByTitleMap = new TreeMap<String, TreeSet<SongInfo>>();
		this.sortedByArtistMap = new TreeMap<String, TreeSet<SongInfo>>();
		this.sortedByTagMap = new TreeMap<String, TreeSet<SongInfo>>();
		this.trackIdMap = new TreeMap<String, SongInfo>();
		this.artistParser = new HashMap<String, String>();
		this.titleParser = new HashMap<String, String>();
		this.tagParser = new HashMap<String, String>();
		this.rwl = new ReentrantLock();
		this.sbt = new SortedByTitle();
		this.sba = new SortedByArtist();
		this.sbti = new SortedByTrackId();
	}
	
	public void addSong(SongInfo newSong) {
		this.rwl.lockWrite();	
		this.addTitle(newSong); 		
		this.addArtist(newSong);
		this.addTag(newSong);
		this.addTrackId(newSong);
		this.rwl.unlockWrite();	
	}
	
/*
	 * Data sorted by title will list the artist name, followed by a space, 
	 * followed by -, followed by a space, followed by title, followed by a new line, 
	 * and will be in alphabetical order by title. If two songs have the same title, 
	 * they will then be sorted by the artist. If two songs have the same title and artist, 
	 * they will be sorted by the track_id. Example:
	 * 
	 *	Hushabye Baby - Cry, Cry, Cry
	 *	Aerosmith - Cryin'
	 */
	//this method add SongInfo to arraylist and sort the arraylist as above.
	
	private void addTitle(SongInfo newSong) {
		String title = newSong.getTitle();
		if(newSong != null && this.sortedByTitleMap.keySet().contains(title)) {
			TreeSet<SongInfo> oldSongsSet = sortedByTitleMap.get(title);
			oldSongsSet.add(newSong);
		} else {
			TreeSet<SongInfo> newSongsSet = new TreeSet<SongInfo>(this.sbt);
			newSongsSet.add(newSong);
			this.sortedByTitleMap.put(title, newSongsSet);
		}
		//update parser
		this.titleParser.put(title.toLowerCase(), title);
	}
	
	/*
	 * Data sorted by artist will list the artist name, followed by a space, 
	 * followed by -, followed by a space, followed by title, followed by a new 
	 * line, and will be in alphabetical order by artist. If two songs have the 
	 * same artist, they will then be sorted by the title. If two songs have the 
	 * same artist and title, they will be sorted by the track_id. Example:
	 * Steel Rain - Loaded Like A Gun
	 * Tom Petty - A Higher Place (Album Version)
	 */
	
	//this method add SongInfo object and sort arraylist as above.
	
	private void addArtist(SongInfo newSong) {
		String artist = newSong.getArtist();
		if(newSong != null && this.sortedByArtistMap.keySet().contains(artist)) {
			TreeSet<SongInfo> oldSongsSet = sortedByArtistMap.get(artist);
			oldSongsSet.add(newSong);
		} else {
			TreeSet<SongInfo> newSongsSet = new TreeSet<SongInfo>(this.sba);
			newSongsSet.add(newSong);
			this.sortedByArtistMap.put(artist, newSongsSet);
		}
		this.artistParser.put(artist.toLowerCase(), artist);
	}
	
	//this method add SongInfo object into treemap and sort its key(tags) and its  
	//value (arraylist).  
	
	private void addTag(SongInfo newSong) {
		String key = new String();
		JsonArray tags = newSong.getTag();
		try {
			for(JsonElement tag: tags) {
				if(tag != null) {
					if(tag.isJsonArray()) {
						key = ((JsonArray)tag).get(0).getAsString();
					}
				}
			//If there already exist that key, we update the TreeSet of the key.
			//If there is not taht key, we set the key and its value.
				if(key != null && this.sortedByTagMap.keySet().contains(key) ) {
					TreeSet<SongInfo> value = this.sortedByTagMap.get(key);
					value.add(newSong);
				} else if(key != null) {
					TreeSet<SongInfo> value = new TreeSet<SongInfo>(this.sbti);
					value.add(newSong);
					this.sortedByTagMap.put(key, value);
				}
				this.tagParser.put(key.toLowerCase(), key);
			}
		} catch(Exception e) {
			System.out.println("add Tag exception");
		}
		
	}
	
	//This method add trackId to trackIdMap.
	
	private void addTrackId(SongInfo newSong) {
		if(newSong != null) {
			this.trackIdMap.put(newSong.getTrackId(), newSong);
		}
	}
	
		// This method takes sortWay and writePath as parameters, write songs info in the given writePath
		// in a wanted sortWay.
	
	public void saveToFile(String order,String writePath) {
		this.rwl.lockRead();
		TreeMap<String, TreeSet<SongInfo>> songsMap = new TreeMap<String, TreeSet<SongInfo>>();
		Path outpath = Paths.get(writePath);
		outpath.getParent().toFile().mkdir();
		try(BufferedWriter output = Files.newBufferedWriter(outpath)){
			// write files in different ways according to command.
			//1. When we write a songsByTitle.
			if(order.equals("title")) {
				songsMap = this.sortedByTitleMap;
			}	
			//2. When we write a songsByArtist.
			if(order.equals("artist")) {
				songsMap = this.sortedByArtistMap;
			}
			// When we want to wirte songsByArtist or songsByTitle.
			if(!order.equals("tag")) {
				//System.out.print(songsList.get(2).getArist());
				Set<String> keys = songsMap.keySet();
				for(String key: keys) {
					TreeSet<SongInfo> songList = songsMap.get(key); 
					for(SongInfo song: songList) {
						output.write(song.getArtist() + " - " + song.getTitle() + "\n");
					}
				}
			} else {
				songsMap = this.sortedByTagMap;
				Set<String> tags = this.sortedByTagMap.keySet();
				for(String tag: tags) {
					//Write every tag in the songsMap.
					output.write(tag + ": ");
					// For each tag, write every song's trackId which is in that tag.
					for(SongInfo song: songsMap.get(tag)) {
						output.write(song.getTrackId() + " ");
					}
					output.write("\n");
				}
			}
		} catch(IOException ioe) {
			System.out.println("Exception in writting file" + ioe);
		}
		this.rwl.unlockRead();
	}	
	
//	The following method take input as artist's name, return all similar songs as a json Object like this:
//	{
//        "artist":"Busta Rhymes",
//        "similars":[
//           {
//              "artist":"DJ Quik",
//              "trackId":"TRAAMJY128F92F5919",
//              "title":"Born and Raised In Compton"
//           },
//           {
//              "artist":"Sticky Fingaz",
//              "trackId":"TRAAZBD128F427A97D",
//              "title":"Oh My God"
//           }
	
	private JsonObject searchByArtist(String artist) {
		this.rwl.lockRead();
		String lowerCaseArtist = artist.toLowerCase();
		lowerCaseArtist = this.artistParser.get(lowerCaseArtist);
		String searchKey;
		if(lowerCaseArtist != null) {
			searchKey = lowerCaseArtist;
		} else {
			searchKey = artist;
		}
		JsonObject result = new JsonObject();
		//Create JsonArray contains all the similar songs for the title as following.
		JsonArray similarList = new JsonArray();
		TreeSet<SongInfo> songsList = this.sortedByArtistMap.get(searchKey);
		//Only if our library has the artist, we can find the similar songs.
		ArrayList<JsonObject> similarArray = new ArrayList<JsonObject>();
		if(songsList != null) {
			for(SongInfo song: songsList) {
				TreeSet<String> similarId = song.getSimilarId();
				if(similarId != null) {
					for(String id: similarId) {
						if(this.trackIdMap.keySet().contains(id) && !similarArray.contains
							(this.trackIdMap.get(id).castToJsonObject())) similarArray.add(this.trackIdMap.get(id).castToJsonObject());
					}
				}
			}
		}	
		Collections.sort(similarArray, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject obj1, JsonObject obj2) {
				return (obj1.get("trackId").getAsString()).compareTo(obj2.get("trackId").getAsString());
			}
		});	
		for(JsonObject jsobj: similarArray) {
			similarList.add(jsobj);
		}
		//Create JsonObject 
		result.addProperty("artist", artist);
		result.add("similars", similarList);
		this.rwl.unlockRead();
		return result;
	}
	
//	The following method take input as title, return all similar songs as a json Object like this:
//	{
//        "artist":"Busta Rhymes",
//        "similars":[
//           {
//              "artist":"DJ Quik",
//              "trackId":"TRAAMJY128F92F5919",
//              "title":"Born and Raised In Compton"
//           },
//           {
//              "artist":"Sticky Fingaz",
//              "trackId":"TRAAZBD128F427A97D",
//              "title":"Oh My God"
//           }
	
	private JsonObject searchByTitle(String title) {
		this.rwl.lockRead();
		String lowerCaseTitle = title.toLowerCase();
		lowerCaseTitle = this.titleParser.get(lowerCaseTitle);
		String searchKey;
		if(lowerCaseTitle != null) {
			searchKey = lowerCaseTitle;
		} else {
			searchKey = title;
		}
		JsonObject result = new JsonObject();
		JsonArray similarList = new JsonArray();
		TreeSet<SongInfo> songsList = this.sortedByTitleMap.get(searchKey);
		ArrayList<JsonObject> similarArray = new ArrayList<JsonObject>();
		if(songsList != null) {
			for(SongInfo song: songsList) {
				TreeSet<String> similarId = song.getSimilarId();
				if(similarId != null) {
					for(String id: similarId) {
						if(this.trackIdMap.keySet().contains(id) && !similarArray.contains
								(this.trackIdMap.get(id).castToJsonObject())) similarArray.add(this.trackIdMap.get(id).castToJsonObject());
					}
				}
			}
		}
		Collections.sort(similarArray, new Comparator<JsonObject>() {
			@Override
			public int compare(JsonObject obj1, JsonObject obj2) {
				return (obj1.get("trackId").getAsString()).compareTo(obj2.get("trackId").getAsString());
			}
		});
		for(JsonObject jsobj: similarArray) {
			similarList.add(jsobj);
		}
		//Create JsonObject 
		result.add("similars", similarList);
		result.addProperty("title", title);
		this.rwl.unlockRead();
		return result;
	}
	
	//This method take tag as input, return all song under the tag as JsonObject.
	private JsonObject searchByTag(String tag) {
		this.rwl.lockRead();
		String lowerCaseTag = tag.toLowerCase();
		lowerCaseTag = this.tagParser.get(lowerCaseTag);
		String searchKey;
		if(lowerCaseTag != null) {
			searchKey = lowerCaseTag;
		} else {
			searchKey = tag;
		}
		TreeSet<SongInfo> songsList = this.sortedByTagMap.get(searchKey);
		JsonArray similarList = new JsonArray();
		JsonObject result = new JsonObject();
		if(songsList != null) {
			for(SongInfo song: songsList) {
				similarList.add(song.castToJsonObject());
			}
		}	
		result.add("similars", similarList);
		result.addProperty("tag", tag);
		this.rwl.unlockRead();
		return result;
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
				arSimilars.add(searchByArtist(artist.getAsString()));	
			}
			//We add the JsonArray to result, associated with "searchByArtist".
			result.add("searchByArtist", arSimilars);
		}
		//If the request contains "tag", we search each tag's similar song, and add it to a JsonArray.
		if(tags != null && tags.size() >= 1) {
			for(JsonElement tag: tags) {				
				taSimilars.add(searchByTag(tag.getAsString()));				
			}
			//We add the JsonArray to result, associated with "searchByTag".
			result.add("searchByTag", taSimilars);
		}		
		//If the request contains title, we search each title's similar song, and add it to a JsonArray
		if(titles != null && titles.size() >= 1) {			
			for(JsonElement title: titles) {
				tiSimilars.add(searchByTitle(title.getAsString()));
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
	
	//This method is beta edition for partial search.
	public JsonObject partialSearchByArtist(String request) {
		rwl.lockRead();
		JsonObject result = new JsonObject();
		String lowerCaseRequest = request.toLowerCase();
		String search = null;
		Set<String> artistsList = this.artistParser.keySet();
		for(String a: artistsList) {
			if(a.contains(lowerCaseRequest)) {
				search = this.artistParser.get(a);
				result.add(search, this.searchByArtist(search));
			}
		}
		rwl.unlockRead();
		return result;
	}
	
	public JsonObject partialSearchByTitle(String request) {
		rwl.lockRead();
		JsonObject result = new JsonObject();
		String lowerCaseRequest = request.toLowerCase();
		String search = null;
		Set<String> titlesList = this.titleParser.keySet();
		for(String a: titlesList) {
			if(a.contains(lowerCaseRequest)) {
				search = this.titleParser.get(a);
				result.add(search, this.searchByTitle(search));
			}
		}
		rwl.unlockRead();
		return result;
	}
	
	public JsonObject partialSearchByTag(String request) {
		rwl.lockRead();
		JsonObject result = new JsonObject();
		String lowerCaseRequest = request.toLowerCase();
		String search = null;
		Set<String> tagsList = this.tagParser.keySet();
		for(String a: tagsList) {
			if(a.contains(lowerCaseRequest)) {
				search = this.tagParser.get(a);
				result.add(search, this.searchByTag(search));
			}
		}
		rwl.unlockRead();
		return result;
	}
	
	
	//This Method return the Artists list in alphabetical order. 
	public Set<String> listArtists(){
		rwl.lockRead();
		try {
			TreeSet<String> result = new TreeSet<String>();
			for(String a: this.sortedByArtistMap.keySet()) {
				result.add(a);
			}
			return result;
		} finally {
			rwl.unlockRead();
		}
		
	}
	
	//This Method take a song's Id as input, return a SongInfo object which have this Id.
	public SongInfo getSong(String trackId) {
		rwl.lockRead();
		try {
			if(trackIdMap.get(trackId) != null) return this.trackIdMap.get(trackId).clone();//Sometimes the requested song is not in the trackId map. Then we returen null.
			return null;
		} finally {
			rwl.unlockRead();
		}
	}
	
	public int size() {
		rwl.lockRead();
		try {
			return trackIdMap.size();
		} finally {
			rwl.unlockRead();
		}
	}
	
	
}
