package basiclv1.examples;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class FlatMap3 {

	public static void main(String[] args) throws IOException {
		
		Path path = Paths.get("C:\\Users\\huudo\\eclipse-workspace\\Java8\\src\\basiclv1\\examples\\test.txt");
		
		try (Stream<String> lines = Files.lines(path, StandardCharsets.UTF_8)) {
			
			Stream<String> words = lines.flatMap(line -> Stream.of(line.split(" +")));
			
			long noOfWords = words.count();
			System.out.println(noOfWords);
			
		}
		
	}

}
