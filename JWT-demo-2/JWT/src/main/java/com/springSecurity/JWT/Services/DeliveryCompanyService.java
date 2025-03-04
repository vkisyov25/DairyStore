package com.springSecurity.JWT.Services;

import com.springSecurity.JWT.Models.DeliveryCompany;
import com.springSecurity.JWT.Repository.DeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyService {
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    public DeliveryCompany getDeliveryCompanyByName(String name) {
        return deliveryCompanyRepository.findDeliveryCompanyByName(name);
    }
}
