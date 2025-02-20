package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.DeliveryCompany;
import com.springSecurity.JWT.Repository.DeliveryCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeliveryCompanyService {
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    @Autowired
    public DeliveryCompanyService(DeliveryCompanyRepository deliveryCompanyRepository) {
        this.deliveryCompanyRepository = deliveryCompanyRepository;
    }

    public DeliveryCompany getDeliveryCompanyByName(String name) {
        return deliveryCompanyRepository.findDeliveryCompanyByName(name);
    }
}
