package com.dairystore.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class OrderProductDto {
    private String productName;
    private String productType;
    private int quantity;
}
