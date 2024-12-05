package basiclv1.examples;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class FlatMap4 {

	public static void main(String[] args) {

		int[] array =  {1, 2, 3, 4, 5, 6};
		
		Stream<int[]> streamArray = Stream.of(array);
		
		IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));
		
		intStream.forEach(x -> System.out.println(x));

	}

}
