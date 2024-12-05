package basiclv1.examples;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Lambda {

	public static void main(String[] args) {

		List<Developer> listDevs = getDevelopers();
		
		System.out.println("Before Sort");
		for (Developer developer : listDevs) {
			System.out.println(developer);
		}
		
		System.out.println("After Sort");
		
		listDevs.sort((Developer o1, Developer o2)->o1.getAge()-o2.getAge());
		
		listDevs.forEach((developer)->System.out.println(developer));
	}
	
	private static List<Developer> getDevelopers() {
		
		List<Developer> result = new ArrayList<Developer>();
		
		result.add(new Developer("alvin", new BigDecimal("80000"), 20));
        result.add(new Developer("jason", new BigDecimal("100000"), 10));
        result.add(new Developer("iris", new BigDecimal("170000"), 55));
        result.add(new Developer("bob", new BigDecimal("60000"), 25));
        result.add(new Developer("charlie", new BigDecimal("75000"), 28));
        result.add(new Developer("david", new BigDecimal("120000"), 30));
        result.add(new Developer("eve", new BigDecimal("95000"), 35));
        result.add(new Developer("frank", new BigDecimal("110000"), 40));
        result.add(new Developer("grace", new BigDecimal("105000"), 32));
		
		return result;
		
	}
	
}
