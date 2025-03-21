package com.dairystore.Models.dtos;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class PaymentRequestDTO {
    @Min(value = 1, message = "Сумата трябва да бъде по-голяма от 0")
    private int amount;  // Сума в стотинки
    private String paymentMethodId;

}
