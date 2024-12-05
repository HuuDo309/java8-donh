package basiclv1.examples;

import java.util.HashSet;
import java.util.Set;

public class Reader {
	
	private Integer id;
    private String name;
    private Set<String> book;
    
    public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
    
    public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<String> getBook() {
		return book;
	}
	
	public void setBook(Set<String> book) {
		this.book = book;
	}
	
	public void addBook(String book) {
        if (this.book == null) {
            this.book = new HashSet<>();
        }
        this.book.add(book);
    }
	
}
