package com.springSecurity.JWT.Models.dtos;

import com.springSecurity.JWT.Models.enums.PaymentMethod;
import lombok.*;

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
