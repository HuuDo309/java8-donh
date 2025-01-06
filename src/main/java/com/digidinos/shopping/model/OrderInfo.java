package com.digidinos.shopping.model;

import java.util.Date;
import java.util.List;

public class OrderInfo {

	private String id;
	private Date orderDate;
	private int orderNum;
	private double amount;
	private String status;
	private Integer userId;
	private String customerName;
	private String customerAddress;
	private String customerEmail;
	private String customerPhone;
	
	private List<OrderDetailInfo> details;
	
	public OrderInfo() {
		
	}
	
	//Sử dụng cho Hibernate Query.
	public OrderInfo(String id, Date orderDate, int orderNum, //
			double amount, String status,Integer userId, String customerName, String customerAddress, //
			String customerEmail, String customerPhone) {
		this.id = id;
		this.orderDate = orderDate;
		this.orderNum = orderNum;
		this.amount = amount;
		this.status = status;
		this.userId = userId;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.customerPhone = customerPhone;
	}
	
	public OrderInfo(String id, Date orderDate, int orderNum, //
            double amount,String status, String customerName, String customerAddress, //
            String customerEmail, String customerPhone) {
        this.id = id;
        this.orderDate = orderDate;
        this.orderNum = orderNum;
        this.amount = amount;
        this.status = status;
        this.customerName = customerName;
        this.customerAddress = customerAddress;
        this.customerEmail = customerEmail;
        this.customerPhone = customerPhone;
    }
	
	public OrderInfo(String id, Date orderDate, String customerName, String customerAddress, 
            String customerEmail, double amount, String status) {
		this.id = id;
		this.orderDate = orderDate;
		this.customerName = customerName;
		this.customerAddress = customerAddress;
		this.customerEmail = customerEmail;
		this.amount = amount;
		this.status = status;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public Date getOrderDate() {
		return orderDate;
	}
	
	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}
	
	public int getOrderNum() {
		return orderNum;
	}
	
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public Integer getUserId() {
		return userId;
	}
	
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	
	public String getCustomerName() {
		return customerName;
	}
	
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	public String getCustomerAddress() {
		return customerAddress;
	}
	
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}
	
	public String getCustomerEmail() {
		return customerEmail;
	}
	
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}
	
	public String getCustomerPhone() {
		return customerPhone;
	}
	
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}
	
	public List<OrderDetailInfo> getDetails() {
		return details;
	}
	
	public void setDetails(List<OrderDetailInfo> details) {
		this.details = details;
	}
	
}
