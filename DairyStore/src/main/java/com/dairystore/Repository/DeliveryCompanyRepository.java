package com.dairystore.Repository;

import com.dairystore.Models.DeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, Long> {
    DeliveryCompany findDeliveryCompanyById(Long id);
}
