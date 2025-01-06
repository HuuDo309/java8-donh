package com.digidinos.shopping.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Users")
public class User implements Serializable {

	private static final long serialVersionUID = -2054386655979281969L;
	
	public static final String ROLE_MANAGER = "Manager";
	public static final String ROLE_EMPLOYEE = "Employee";
	public static final String ROLE_CUSTOMER = "Customer";
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders;
	
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> productReviews;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private Integer id;
	
	@Column(name = "User_Name", length = 20, nullable = false)
	private String userName;
	
	@Column(name = "Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;
	
	@Column(name = "Active", length = 1, nullable = false)
	private boolean active;
	
	@Column(name = "User_Role", length = 20, nullable = false)
	private String userRole;
	
	@Column(name = "Full_Name", length = 255, nullable = false)
	private String fullName;
	
	@Column(name = "Email", length = 128, nullable = false)
	private String email;
	
	@Column(name = "Phone", length = 128, nullable = false)
	private String phone;
	
	@Column(name = "Address", length = 255, nullable = false)
	private String address;
	
	public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    public List<Review> getProductReviews() {
    	return productReviews;
    }
    
    public void setProductReviews(List<Review> productReviews) {
    	this.productReviews = productReviews;
    }
    
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
        this.active = active;
    }
	
	public String getUserRole() {
		return userRole;
	}
	
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}
	
	public String getFullName() {
		return fullName;
	}
	
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String address) {
		this.address = address;
	}
}
