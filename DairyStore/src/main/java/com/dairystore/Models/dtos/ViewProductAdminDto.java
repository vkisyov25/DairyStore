package com.dairystore.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ViewProductAdminDto {
    private Long id;
    private String name;
    private String type;
    private double weight;
    private double price;
    private String description;
    private double discount;
    private int quantity;
    private String username;
}
