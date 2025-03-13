package com.dairystore.Services;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Repository.DeliveryCompanyRepository;
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
