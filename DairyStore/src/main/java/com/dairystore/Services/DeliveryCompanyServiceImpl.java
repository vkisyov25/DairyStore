package com.dairystore.Services;

import com.dairystore.Models.DeliveryCompany;
import com.dairystore.Models.dtos.CreateDeliveryCompanyDto;
import com.dairystore.Repository.DeliveryCompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeliveryCompanyServiceImpl implements DeliveryCompanyService {
    private final DeliveryCompanyRepository deliveryCompanyRepository;

    @Override
    public List<DeliveryCompany> getDeliveryCompanies() {
        return deliveryCompanyRepository.findAll();
    }

    @Override
    public DeliveryCompany getDeliveryCompanyById(Long companyId) {
        return deliveryCompanyRepository.findDeliveryCompanyById(companyId);
    }

    @Override
    public void deleteDeliveryCompanyById(Long deliveryCompanyId) throws Exception {
        isExist(deliveryCompanyId);
        deliveryCompanyRepository.deleteById(deliveryCompanyId);
    }

    private void isExist(Long deliveryCompanyId) throws Exception {
        if (!deliveryCompanyRepository.existsById(deliveryCompanyId)) {
            throw new Exception("Фирмата не съществува в базата данни");
        }
    }

    @Override
    public void createDeliveryCompany(CreateDeliveryCompanyDto createDeliveryCompanyDto) {
        DeliveryCompany deliveryCompany = DeliveryCompany.builder()
                .name(createDeliveryCompanyDto.getName())
                .deliveryFee(createDeliveryCompanyDto.getDeliveryFee())
                .build();

        deliveryCompanyRepository.save(deliveryCompany);
    }

    @Override
    public void isExistCompanyByName(String name, BindingResult bindingResult) {
        if (deliveryCompanyRepository.existsByName(name)) {
            bindingResult.rejectValue("name", "error.name", "Името на фирмата вече съществува");
        }
    }
}
