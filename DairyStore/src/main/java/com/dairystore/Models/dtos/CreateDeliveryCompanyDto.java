package com.dairystore.Models.dtos;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateDeliveryCompanyDto {
    @NotBlank(message = "Името не може да бъде празно")
    private String name;
    @DecimalMin(value = "0.01", message = "Таксата за доставка трябва да е положително число")
    private Double deliveryFee;
}
