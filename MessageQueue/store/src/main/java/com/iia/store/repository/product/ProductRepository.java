package com.iia.store.repository.product;

import com.iia.store.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.store.id = :storeId")
    List<Product> findByStoreIdWithImages(@Param("storeId") Long storeId);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.images WHERE p.id = :productId")
    Optional<Product> findByIdWithImages(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p JOIN FETCH p.stock WHERE p.store.id = :storeId AND p.name = :productName")
    Optional<Product> findByStoreIdAndProductName(Long storeId, String productName);
}
