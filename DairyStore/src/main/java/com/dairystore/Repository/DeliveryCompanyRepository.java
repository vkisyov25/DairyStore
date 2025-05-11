package com.dairystore.Repository;

import com.dairystore.Models.DeliveryCompany;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, Long> {
    DeliveryCompany findDeliveryCompanyById(Long id);
    @NotNull
    List<DeliveryCompany> findAll();

    void deleteById(Long id);

    boolean existsByName(String name);
}
