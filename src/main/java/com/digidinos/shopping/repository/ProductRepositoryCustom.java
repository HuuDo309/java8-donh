package com.digidinos.shopping.repository;

import com.digidinos.shopping.entity.Product;
import com.digidinos.shopping.entity.Review;
import com.digidinos.shopping.form.ProductForm;
import com.digidinos.shopping.model.ProductInfo;
import com.digidinos.shopping.pagination.PaginationResult;

import java.util.List;

public interface ProductRepositoryCustom {
    Product findProduct(String code);
    ProductInfo findProductInfo(String code);
    int getStockQuantityByProductCode(String productCode);
    void updateProductQuantity(String productCode, int newQuantity);
    Product findProductWithReviews(String code);
    PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage, String likeName);
    PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage);
	void saveProduct(ProductForm productForm);
	List<Review> listReviewsByProductCode(String productId);
	void saveReview(Review review);
    
}
