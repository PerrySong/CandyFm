package songfinder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.stream.Stream;

public class SongsBuilder {
	
	private void processPath(Path path) {
		File song = path.toFile();
		try(Scanner input = new Scanner(song)) {
			
		} catch(IOException ioe) {
			System.out.println("Exception in processPath: " + ioe);
		}
		
	}
	
	private void scanFile(String directory) {
		Path path = Paths.get(directory);
		try(Stream<Path> paths = Files.walk(path)) {
			path.forEach(p -> processPath(p));
		} catch(Exception e) {
			System.out.println("scanFile exception: " + e);
		}
	}
}
