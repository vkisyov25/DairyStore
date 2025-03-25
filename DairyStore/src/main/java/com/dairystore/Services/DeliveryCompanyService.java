package com.dairystore.Services;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Repository.DeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyService {
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    public List<DeliveryCompany> getDeliveryCompanies() {
        return deliveryCompanyRepository.findAll();
    }

    public DeliveryCompany getDeliveryCompanyById(Long companyId) {
        return deliveryCompanyRepository.findDeliveryCompanyById(companyId);
    }
}
