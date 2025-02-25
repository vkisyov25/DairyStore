package com.springSecurity.JWT.Models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class BuyerAnalyticsDto {
    private double totalPurchasePrice;
    private double averagePurchasePrice;
    private String mostPurchasedType;
}
