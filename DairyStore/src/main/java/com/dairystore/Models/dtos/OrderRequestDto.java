package com.dairystore.Models.dtos;

import com.dairystore.Models.enums.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderRequestDto {
    @JsonProperty("deliveryAddress")
    private String deliveryAddress;

    @JsonProperty("deliveryCompanyName")
    private String deliveryCompanyName;

    @JsonProperty("paymentMethod")
    private PaymentMethod paymentMethod;

    @JsonProperty("paymentIntentId")
    private String paymentIntentId;
}
