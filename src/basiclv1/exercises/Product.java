package basiclv1.exercises;

import java.util.Date;

public class Product {
	
	private Integer id;
	private String name;
	private Integer categoryId;
	private Date saleDate;
	private Integer qulity;
	private Boolean isDelete;
	
	public Product(Integer id, String name, Integer categoryId, Date saleDate, Integer qulity, Boolean isDelete) {
		this.id = id;
		this.name = name;
		this.categoryId = categoryId;
		this.saleDate = saleDate;
		this.qulity = qulity;
		this.isDelete = isDelete;
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}
	
	public Date getSaleDate() {
		return saleDate;
	}
	
	public void setSaleDate(Date saleDate) {
		this.saleDate = saleDate;
	}
	
	public Integer getQulity() {
		return qulity;
	}
	
	public void setQulity(Integer qulity) {
		this.qulity = qulity;
	}
	
	public Boolean getIsDelete() {
		return isDelete;
	}
	
	public void setIsDelete(Boolean isDelete) {
		this.isDelete = isDelete;
	}
	
	@Override
	public String toString() {
	    return "Product{" +
	            "id=" + id +
	            ", name='" + name + '\'' +
	            ", categoryId=" + categoryId +
	            ", saleDate=" + saleDate +
	            ", qulity=" + qulity +
	            ", isDelete=" + isDelete +
	            '}';
	}
	
}
