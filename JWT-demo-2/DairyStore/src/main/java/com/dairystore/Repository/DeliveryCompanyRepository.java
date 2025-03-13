package com.dairystore.Repository;

import com.dairystore.Models.DeliveryCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCompanyRepository extends JpaRepository<DeliveryCompany, Long> {
    @Query("SELECT deliveryCompany FROM DeliveryCompany deliveryCompany WHERE deliveryCompany.name = ?1")
    DeliveryCompany findDeliveryCompanyByName(String name);
}
