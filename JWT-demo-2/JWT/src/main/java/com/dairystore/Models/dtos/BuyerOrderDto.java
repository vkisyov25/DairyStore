package com.springSecurity.JWT.Models.dtos;

import com.springSecurity.JWT.Models.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BuyerOrderDto {
    private LocalDateTime orderDate;
    private String addressToDelivery;
    private PaymentMethod paymentMethod;
    private String deliveryCompany;
    private double deliveryFee;
    private double priceWithDeliveryFee;
    private List<OrderProductDto> orderProductDtoList;
}
