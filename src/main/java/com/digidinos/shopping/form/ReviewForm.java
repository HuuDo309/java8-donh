package com.digidinos.shopping.form;

public class ReviewForm {
	private String productId;
    private int rating;
    private String comment;
    
    public ReviewForm() {
    	
    }
    
    public String getProductId() {
    	return productId;
    }
    
    public void setProductId(String productId) {
    	this.productId = productId;
    }
    
    public int getRating() {
    	return rating;
    }
    
    public void setRating(int rating) {
    	this.rating = rating;
    }
    
    public String getComment() {
    	return comment;
    }
    
    public void setComment(String comment) {
    	this.comment = comment;
    }
    
}
