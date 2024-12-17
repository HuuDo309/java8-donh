package com.digidinos.shopping.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class BaseEntity implements Serializable{
	public static final long serialVersionUID = 1842915944086730414L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	protected String id;
	
	protected LocalDateTime createdAt;
	protected LocalDateTime updatedAt;
	protected LocalDateTime deletedAt;
	private boolean isDeleted;
	
	protected BaseEntity(String id, LocalDateTime createdAt, LocalDateTime updatedAt, LocalDateTime deletedAt, boolean isDeleted) {
		this.id =id;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.deletedAt = deletedAt;
		//this.setDeleted(isDeleted);
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	

	
}
