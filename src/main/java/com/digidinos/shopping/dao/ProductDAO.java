package com.digidinos.shopping.dao;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.entity.ProductReview;
import com.digidinos.shopping.form.ProductForm;
import com.digidinos.shopping.model.ProductInfo;
import com.digidinos.shopping.pagination.PaginationResult;

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
	        String sql = "Select e from " + Product.class.getName() + " e Where e.code = :code";

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
	    return new ProductInfo(product.getCode(), product.getName(), product.getPrice(), product.getQuantity());
	}

	public int getStockQuantityByProductCode(String productCode) {
        try {
            String sql = "Select e.quantity from " + Product.class.getName() + " e Where e.code = :code";

            Session session = this.sessionFactory.getCurrentSession();
            Query<Integer> query = session.createQuery(sql, Integer.class);
            query.setParameter("code", productCode);
            Integer quantity = query.getSingleResult();
            return quantity != null ? quantity : 0; 
        } catch (NoResultException e) {
            return 0; 
        }
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
	    product.setQuantity(productForm.getQuantity());

	    if (productForm.getFileData() != null) {
	        byte[] image = null;
	        try {
	            image = productForm.getFileData().getBytes();
	        } catch (IOException e) {
	            // Handle exception for image file
	        }
	        if (image != null && image.length > 0) {
	            product.setImage(image);
	        }
	    }
	    if (isNew) {
	        session.persist(product);
	    }
	    // Nếu có lỗi tại DB, ngoại lệ sẽ được ném ra ngay lập tức
	    session.flush();
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void updateProductQuantity(String productCode, int newQuantity) {
	    Product product = this.findProduct(productCode);
	    if (product != null) {
	        product.setQuantity(newQuantity); 
	        this.sessionFactory.getCurrentSession().flush();
	    }
	}

	public Product findProductWithReviews(String code) {
		try {
			String sql = "Select distinct p from " + Product.class.getName() + " p " + 
						"left join fetch p.reviews where p.code = :code";
			
			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<ProductReview> listReviewsByProductCode(String productId) {
		try {
			String sql = "Select r from " + ProductReview.class.getName() + " r " + 
						"where r.product.code = :productId";
			
			Session session = this.sessionFactory.getCurrentSession();
			Query<ProductReview> query = session.createQuery(sql, ProductReview.class);
			query.setParameter("productId", productId);
			return query.getResultList();
		} catch (NoResultException e) {
			return Collections.emptyList();
		}
		
	}
	
	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void delete(Product product) {
	    if (product != null) {
	    	product.setDeleted(true);
	        Session session = this.sessionFactory.getCurrentSession();
	        session.update(product); 
	        session.flush();
	    }
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName) {
	    String sql = "Select new " + ProductInfo.class.getName() 
	                + "(p.code, p.name, p.price, p.quantity, p.image ) from "
	                + Product.class.getName() + " p ";
	    
	    if (likeName != null && likeName.length() > 0) {
	        sql += "Where lower(p.name) like :likeName ";
	    }
	    
	    if (likeName != null && likeName.length() > 0) {
	        sql += " and p.isDeleted = false "; 
	    } else {
	        sql += "Where p.isDeleted = false "; 
	    }
	    
	    sql += " order by p.createDate desc ";

	    // Tạo câu truy vấn và trả về kết quả phân trang
	    Session session = this.sessionFactory.getCurrentSession();
	    Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);
	    
	    if (likeName != null && likeName.length() > 0) {
	        query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
	    }

	    return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
	    return queryProducts(page, maxResult, maxNavigationPage, null);
	}

}
