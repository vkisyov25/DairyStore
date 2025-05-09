package com.dairystore.Models.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class OrderItemResultDto {
    private final List<OrderProductDto> orderProductDtoList;
    private final double totalItemPrice;

}
