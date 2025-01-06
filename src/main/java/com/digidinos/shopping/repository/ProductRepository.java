package com.digidinos.shopping.repository;

import com.digidinos.shopping.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, String>, ProductRepositoryCustom {

}
