package basiclv1.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FlatMap1 {
	
	public static void main(String[] args) {
		
		Reader o1 = new Reader();
		o1.setName("mkyong");
	    o1.addBook("Java 8 in Action");
	    o1.addBook("Spring Boot in Action");
	    o1.addBook("Effective Java (3nd Edition)");
	     
	    Reader o2 = new Reader();
	    o2.setName("zilap");
        o2.addBook("Learning Python, 5th Edition");
        o2.addBook("Effective Java (3nd Edition)");
        
        List<Reader> list = new ArrayList<>();
        list.add(o1);
        list.add(o2);
        
        Set<String> collect =
                list.stream()
                        .map(x -> x.getBook())                              //  Stream<Set<String>>
                        .flatMap(x -> x.stream())                           //  Stream<String>
                        .filter(x -> !x.toLowerCase().contains("python"))   //  filter python book
                        .collect(Collectors.toSet());                       //  remove duplicated

        collect.forEach(System.out::println);

	}
	
}
