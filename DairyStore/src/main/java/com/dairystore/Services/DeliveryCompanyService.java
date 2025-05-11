package com.dairystore.Services;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.dtos.CreateDeliveryCompanyDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface DeliveryCompanyService {
    List<DeliveryCompany> getDeliveryCompanies();

    DeliveryCompany getDeliveryCompanyById(Long companyId);

    void deleteDeliveryCompanyById(Long deliveryCompanyId) throws Exception;

    void createDeliveryCompany(CreateDeliveryCompanyDto createDeliveryCompanyDto);

    void isExistCompanyByName(String name, BindingResult bindingResult);
}
