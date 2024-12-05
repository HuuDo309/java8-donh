package basiclv1.exercises;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

	public static void main(String[] args) {
		
		List<Product> listProducts = createProductList();
		
		/*		
		String nameUsingStream = filterProductByIdUsingStream(listProducts, 3);
        System.out.println("Dùng Stream: " + nameUsingStream);
		
        String nameWithoutStream = filterProductByIdWithoutStream(listProducts, 3);
        System.out.println("Không dùng Stream: " + nameWithoutStream);
        
        Product[] productUsingStream = filterProductByQulityUsingStream(listProducts);
        System.out.println("Dùng stream: " + Arrays.toString(productUsingStream));
        
        Product[] productWhithoutStream = filterProductByQulityUsingStream(listProducts);
        System.out.println("Không dùng Stream: " + Arrays.toString(productWhithoutStream));
        
        List<String> nameUsingStream = filterProductNameBySaleDateUsingStream(listProducts);
        System.out.println("Dùng Stream: " + nameUsingStream);
        
        List<String> nameWithoutStream = filterProductNameBySaleDateWithoutStream(listProducts);
        System.out.println("Không dùng Stream: " + nameWithoutStream);
        
        Integer totalUsingReducer = totalProductUsingReducer(listProducts);
        System.out.println("Dùng reducer: " + totalUsingReducer);
        
        Integer totalWithoutReducer = totalProductUsingReducer(listProducts);
        System.out.println("Không dùng reducer: " + totalWithoutReducer);
        
        boolean haveProductUsingStream = isHaveProductInCategoryUsingStream(listProducts, 111);
        System.out.println("Dùng Stream: " + haveProductUsingStream);
        
        boolean haveProductWithoutStream = isHaveProductInCategoryWithoutStream(listProducts, 111);
        System.out.println("Không dùng Stream: " + haveProductWithoutStream);
        
		*/        
		
        String[][] resultUsingStream = fiterProductBySaleDateUsingStream(listProducts);
        System.out.println("Dùng Stream: ");
        for (String[] product : resultUsingStream) {
        	System.out.println(Arrays.toString(product));
        }
        
        String[][] resultWithoutStream = filterProductBySaleDateWithoutStream(listProducts);
        System.out.println("Không dùng Stream: ");
        for (String[] product : resultWithoutStream) {
        	System.out.println(Arrays.toString(product));
        }

	}
	
	//Function tạo listProduct chứa 10 đối tượng product
	private static List<Product> createProductList() {

		List<Product> listProducts = new ArrayList<Product>();
		
		listProducts.add(new Product(1, "Laptop Dell XPS", 101, toDate(LocalDate.of(2025, 1, 15)), 0, false));
        listProducts.add(new Product(2, "Smartphone iPhone 14", 102, toDate(LocalDate.of(2024, 12, 10)), 15, false));
        listProducts.add(new Product(3, "Tablet Samsung Galaxy Tab", 103, toDate(LocalDate.of(2024, 11, 25)), 8, false));
        listProducts.add(new Product(4, "Smartwatch Apple Watch", 104, toDate(LocalDate.of(2024, 10, 5)), 5, true));
        listProducts.add(new Product(5, "Headphones Bose 700", 105, toDate(LocalDate.of(2025, 5, 20)), 12, false));
        listProducts.add(new Product(6, "Keyboard Logitech MX Keys", 106, toDate(LocalDate.of(2025, 8, 15)), 7, true));
        listProducts.add(new Product(7, "Mouse Logitech MX Master 3", 107, toDate(LocalDate.of(2025, 7, 30)), 20, false));
        listProducts.add(new Product(8, "Monitor LG UltraGear", 108, toDate(LocalDate.of(2025, 6, 10)), 6, true));
        listProducts.add(new Product(9, "Camera Sony Alpha A7", 109, toDate(LocalDate.of(2025, 5, 25)), 3, false));
        listProducts.add(new Product(10, "Printer HP LaserJet Pro", 110, toDate(LocalDate.of(2025, 4, 15)), 2, false));
		
		return listProducts;
		
	}
	
	private static Date toDate(LocalDate localDate) {
		return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	//Function trả về tên của product theo id 
	public static String filterProductByIdUsingStream(List<Product> listProducts, Integer idProduct) {
        return listProducts.stream()
                .filter(product -> product.getId().equals(idProduct)) 
                .map(Product::getName) 
                .findFirst() 
                .orElse("Product not found"); 
    }
	
	public static String filterProductByIdWithoutStream(List<Product> listProducts, Integer idProduct) {
        for (Product product : listProducts) {
            if (product.getId().equals(idProduct)) { 
                return product.getName();
            }
        }
        return "Product not found";
    }
	
	//Function trả về array product có quality > 0 và chưa bị xóa
	public static Product[] filterProductByQulityUsingStream(List<Product> listProducts) {
		return listProducts.stream()
				.filter(product -> product.getQulity() > 0 && !product.getIsDelete())
				.toArray(Product[]::new);
	}
		
	public static Product[] filterProductByQulityWithoutStream(List<Product> listProducts) {
		
		List<Product> filteredList = new ArrayList<>();
		
		for (Product product : listProducts) {
			if (product.getQulity() > 0 && !product.getIsDelete()) {
				filteredList.add(product);
			}
		}
		return filteredList.toArray(new Product[0]);
	}
	
	//Function trả về aray tên product có saleDate > ngày hiện tại và chưa bị xóa 
	public static List<String> filterProductNameBySaleDateUsingStream(List<Product> listProducts) {
		return listProducts.stream()
				.filter(product -> product.getSaleDate().after(new Date()) && !product.getIsDelete())
				.map(Product::getName)
				.collect(Collectors.toList());
	}
	
	public static List<String> filterProductNameBySaleDateWithoutStream(List<Product> listProducts) {
		List<String> nameList = new ArrayList<>();
		for (Product product : listProducts) {
			if (product.getSaleDate().after(new Date()) && !product.getIsDelete()) {
				nameList.add(product.getName()); 
			}
		}
		return nameList;
	}
	
	//Function trả về tổng số product (tổng số quality) chưa bị xóa 
	public static int totalProductUsingReducer(List<Product> listProducts) {
		return listProducts.stream()
				.filter(product -> !product.getIsDelete())
				.map(Product::getQulity)
				.reduce(0, (sum, qulity) -> sum + qulity);
	}
	
	public static int totalProductWithoutReducer(List<Product> listProducts) {
		/*
		
		Cách 1: Sử dụng vòng lặp for
		int total = 0;
		for (Product product : listProducts) {
			if (!product.getIsDelete()) {
				total = total + product.getQulity();
			}
		}
		return total;
		
		Cách 2: sử dụng sum()
		return listProducts.stream()
				.filter(product -> !product.getIsDelete())
                .mapToInt(Product::getQulity)
                .sum();
		*/
		
		//Cách 3: sử dụng IntSummaryStatistics
		IntSummaryStatistics stats = listProducts.stream()
				.filter(product -> !product.getIsDelete())
				.mapToInt(Product::getQulity)
				.summaryStatistics();
		
		return (int) stats.getSum();
	}
	
	//Function trả về true nếu có product thuộc category
	public static boolean isHaveProductInCategoryUsingStream(List<Product> listProducts, Integer categoryId) {
		return listProducts.stream()
				.anyMatch(product -> product.getCategoryId().equals(categoryId));
	}
	
	public static boolean isHaveProductInCategoryWithoutStream(List<Product> listProducts, Integer categoryId) {
		for (Product product : listProducts) {
			if (product.getCategoryId().equals(categoryId)) {
				return true;
			}
		}
		return false;
	}
	
	//Function trả về array chứa array String (id, tên ) product có saleDate > ngày hiện tại và quality > 0
	public static String [][] fiterProductBySaleDateUsingStream(List<Product> listProducts) {
		return listProducts.stream()
				.filter(product -> product.getSaleDate().after(new Date()) && product.getQulity() > 0)
				.map(product -> new String[] { product.getId().toString(), product.getName() })
				.toArray(String[][]::new);
	}
	
	public static String [][] filterProductBySaleDateWithoutStream(List<Product> listProducts) {
		List<String[]> resultList = new ArrayList<>();
		for (Product product : listProducts) {
			if (product.getSaleDate().after(new Date()) && product.getQulity() > 0) {
				resultList.add(new String[] { product.getId().toString(), product.getName() });
			}
		}
		return resultList.toArray(new String[0][]);
	}
}
