package com.digidinos.shopping.dao;

import java.io.IOException;
import java.util.Date;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.form.ProductForm;
import com.digidinos.shopping.model.ProductInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


@Transactional
@Repository
public class ProductDAO {

	@Autowired
	private SessionFactory sessionFactory;
	
	public Product findProduct(String code) {
		try {
			String sql = "Select from " + Product.class.getName() + "e Where e.code = :code";
			
			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);
			return (Product) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public ProductInfo findProductInfo(String code) {
		Product product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductForm productForm) {
		
		Session session = this.sessionFactory.getCurrentSession();
		String code = productForm.getCode();
		
		Product product = null;
		
		boolean isNew = false;
		if (code != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date());
		}
		
		product.setCode(code);
		product.setName(productForm.getName());
		product.setPrice(productForm.getPrice());
		
		if (productForm.getFileData() != null) {
			byte[] image = null;
			try {
				image = productForm.getFileData().getBytes();
			} catch (IOException e) {
				
			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			session.persist(product);
		}
		//Nếu có lỗi tại DB, ngoại lệ sẽ được ném ra ngay lập tức
		session.flush();
	}

}
