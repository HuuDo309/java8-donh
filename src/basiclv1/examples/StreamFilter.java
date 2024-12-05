package basiclv1.examples;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamFilter {

	public static void main(String[] args) {
		
		List<Person> persons = Arrays.asList(
				new Person("alice", 25),
                new Person("bob", 35),
                new Person("charlie", 28),
                new Person("david", 33),
                new Person("eve", 22),
                new Person("frank", 45),
                new Person("grace", 31)
		);
		
		Person result1 = persons.stream()										//Convert to stream
				.filter(x -> "alice".equals(x.getName()) && 25 == x.getAge())	//Condition
				.findAny()														//If findAny then return found
				.orElse(null);													//If not found, return null
		
		System.out.println(result1);
		
		Person result2 = persons.stream()										
				.filter(x -> "alice".equals(x.getName()) && 20 == x.getAge())	
				.findAny()														
				.orElse(null);
		
		System.out.println(result2);
		
		String name = persons.stream()
				.filter(n -> "alice".equals(n.getName()))
				.map(Person::getName)											//Convert stream to String
				.findAny()
				.orElse(null);
		
		System.out.println("name : " + name);
		
		 List<String> collect = persons.stream()
	                .map(Person::getName)
	                .collect(Collectors.toList());

	     collect.forEach(System.out::println);
        
	}

}
