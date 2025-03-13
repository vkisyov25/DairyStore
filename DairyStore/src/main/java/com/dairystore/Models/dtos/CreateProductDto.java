package com.dairystore.Models.dtos;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateProductDto {

    @NotBlank(message = "Полето име не може да бъде празно")
    private String name;
    @NotBlank(message = "Полето тип не може да бъде празно")
    private String type;
    /*@NotNull(message = "Полето тегло не може да бъде празно")*/
    @Positive(message = "Теглото трябва да бъде положително число")
    private double weight;
    /* @NotNull(message = "Полето цена не може да бъде празно")*/
    @Positive(message = "Цената трябва да бъде положително число")
    private double price;
    @NotBlank(message = "Полето описание не може да бъде празно")
    @Size(max = 250, message = "Описанието не може да е повече от 250 символа")
    private String description;
    @Min(value = 0, message = "Отстъпката не може да бъде по-малка от 1%")
    @Max(value = 100, message = "Отстъпката не може да бъде повече от 100%")
    /*@NotNull(message = "Полето отстъпка не може да бъде празно")*/
    @Positive(message = "Отстъпката трябва да бъде положително число")
    private double discount;
    /*@NotNull(message = "Полето количество не може да бъде празно")*/
    @Min(value = 1, message = "Количеството не може да бъде по-малка от 1")
    private int quantity;
}
