package com.digidinos.shopping.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "Products")
public class Product implements Serializable {

	private static final long serialVersionUID = -1000119078147252957L;
	
	@OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Review> productReviews;
	
	@Id
	@Column(name = "Code", length = 20, nullable = false)
	private String code;
	
	@Column(name = "Name", length = 255, nullable = false)
	private String name;
	
	@Column(name = "Price", nullable = false)
	private double price;
	
	@Lob
	@Column(name = "image", length = Integer.MAX_VALUE, nullable = true)
	private byte[] image;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Date", nullable = false)
	private Date createDate;
	
	@Column(name = "Quantity")
	private int quantity;
	
	@Column(name = "IS_DELETED", nullable = false)
	private boolean isDeleted;
	
	public Product() {
	}
	
	public List<Review> getProductReviews() {
		return productReviews;
	}
	
	public void setProductReviews(List<Review> productReviews) {
		this.productReviews = productReviews;
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
	
	public Date getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	
	public byte[] getImage() {
		return image;
	}
	
	public void setImage(byte[] image) {
		this.image = image;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
}
