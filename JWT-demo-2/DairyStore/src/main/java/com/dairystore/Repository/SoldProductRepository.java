package com.dairystore.Repository;

import com.dairystore.Models.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {
    @Query("SELECT soldProduct FROM SoldProduct soldProduct WHERE soldProduct.buyer_id = ?1")
    List<SoldProduct> findSoldProductByBuyer_id(Long user_id);
}

