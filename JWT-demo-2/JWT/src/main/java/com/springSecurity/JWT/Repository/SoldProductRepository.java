package com.springSecurity.JWT.Repository;

import com.springSecurity.JWT.Models.SoldProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SoldProductRepository extends JpaRepository<SoldProduct, Long> {
}
