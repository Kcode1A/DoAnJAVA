package com.quan.demo.repository;

import com.quan.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
//public interface ProductRepository extends JpaRepository<Product, Long> {
//    List<Product> findByKeyword(String keyword);
//}
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p WHERE lower(p.name) LIKE lower(concat('%', :keyword, '%')) OR lower(p.description) LIKE lower(concat('%', :keyword, '%'))")
    List<Product> findByKeyword(@Param("keyword") String keyword);
}