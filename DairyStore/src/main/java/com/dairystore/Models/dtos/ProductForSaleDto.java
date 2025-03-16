package com.dairystore.Models.dtos;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class ProductForSaleDto {
    private Long id;
    private String name;
    private String type;
    private double weight;
    private double price;
    private String description;
    private String availability;
}
