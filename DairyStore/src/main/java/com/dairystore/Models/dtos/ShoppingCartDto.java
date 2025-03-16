package com.dairystore.Models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ShoppingCartDto {
    private Long id;
    private String name;
    private String type;
    private double weight;
    private double price;
    private double discount;
    private int quantity;
    private double totalPricePerProduct;
}
