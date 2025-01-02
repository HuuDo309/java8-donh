package com.digidinos.shopping.model;

import com.digidinos.shopping.entity.Product;

public class ProductInfo {

	private String code;
	private String name;
	private double price;
	private int quantity;
	private byte[] image;
	private String imageBase64;
	
	public ProductInfo() {
		
	}
	
	public ProductInfo(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
		this.quantity = product.getQuantity();
	}
	
	//Sử dụng trong JPA/Hibernate query
	public ProductInfo(String code, String name, double price, int quantity, byte[] image) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
	
	public ProductInfo(String code, String name, double price, int quantity) {
        this.code = code;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
	
	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getImageBase64() {
		return imageBase64;
    }
	
	public void setImageBase64(String imageBase64) {
		this.imageBase64 = imageBase64;
	}
}
