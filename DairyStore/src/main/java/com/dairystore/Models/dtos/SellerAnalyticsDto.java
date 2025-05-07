package com.dairystore.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerAnalyticsDto {
    private double totalEarnings;
    private String topSellingProduct;
    private int buyerCount;
}
