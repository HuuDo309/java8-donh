package com.digidinos.shopping.repository.impl;

import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.entity.Review;
import com.digidinos.shopping.form.ProductForm;
import com.digidinos.shopping.model.ProductInfo;
import com.digidinos.shopping.pagination.PaginationResult;
import com.digidinos.shopping.repository.ProductRepositoryCustom;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Repository
public class ProductRepositoryImpl implements ProductRepositoryCustom {

	@Autowired
    private SessionFactory sessionFactory;
	
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Product findProduct(String code) {
        try {
            String sql = "Select p from Product p where p.code = :code";
            Query<Product> query = (Query<Product>) entityManager.createQuery(sql, Product.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ProductInfo findProductInfo(String code) {
        Product product = this.findProduct(code);
        if (product != null) {
            return new ProductInfo(product.getCode(), product.getName(), product.getPrice(), product.getQuantity());
        }
        return null;
    }

    @Override
    public int getStockQuantityByProductCode(String productCode) {
        try {
            String sql = "Select p.quantity from Product p where p.code = :code";
            Query<Integer> query = (Query<Integer>) entityManager.createQuery(sql, Integer.class);
            query.setParameter("code", productCode);
            Integer quantity = query.getSingleResult();
            return quantity != null ? quantity : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public void saveProduct(ProductForm productForm) {
        String code = productForm.getCode();
        Product product = findProduct(code);  

        if (product == null) {
            product = new Product();  
            product.setCreateDate(new Date());
        }

        product.setCode(code);
        product.setName(productForm.getName());
        product.setPrice(productForm.getPrice());
        product.setQuantity(productForm.getQuantity());

        if (productForm.getFileData() != null) {
            try {
                byte[] image = productForm.getFileData().getBytes();
                if (image != null && image.length > 0) {
                    product.setImage(image);
                }
            } catch (IOException e) {
            }
        }

        entityManager.persist(product); 
    }

    
    @Override
    public void saveReview(Review review) {
        if (review.getReviewDate() == null) {
            review.setReviewDate(new Date());
        }

        if (review.getId() == 0) {
            entityManager.persist(review);
        } else {
            entityManager.merge(review);
        }

        entityManager.flush();
    }



    
    @Override
    public void updateProductQuantity(String productCode, int newQuantity) {
        Product product = this.findProduct(productCode);
        if (product != null) {
            product.setQuantity(newQuantity);
            entityManager.merge(product);
        }
    }

    @Override
    public List<Review> listReviewsByProductCode(String productId) {
        try {
            String jpql = "Select r from Review r where r.product.code = :productId";

            TypedQuery<Review> query = entityManager.createQuery(jpql, Review.class);
            query.setParameter("productId", productId);

            return query.getResultList();
        } catch (NoResultException e) {
            return Collections.emptyList();
        }
    }
    
    @Override
    public Product findProductWithReviews(String code) {
        try {
            String sql = "Select distinct p from Product p left join fetch p.reviews where p.code = :code";
            Query<Product> query = (Query<Product>) entityManager.createQuery(sql, Product.class);
            query.setParameter("code", code);
            return query.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName) {
    	String sql = "Select new " + ProductInfo.class.getName() //
                + "(p.code, p.name, p.price, p.quantity) " + " from "//
                + Product.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";

        Session session = this.sessionFactory.getCurrentSession();
        Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);

        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        };

        return new PaginationResult<>(query, page, maxResult, maxNavigationPage);
    }

    @Override
    public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage) {
        return queryProducts(page, maxResult, maxNavigationPage, null);
    }

}
