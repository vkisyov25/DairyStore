package com.dairystore.Controllers;

import com.dairystore.Models.dtos.PaymentRequestDTO;
import com.dairystore.Models.dtos.PaymentResponseDTO;
import com.dairystore.Services.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class StripeController {

    private final StripeService stripeService;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentResponseDTO> createPaymentIntent(@RequestBody PaymentRequestDTO request) throws StripeException {
        // Задаваме таен ключ на Stripe
        Stripe.apiKey = "sk_test_51R3g8hGrjmQKYSJDEkPSubK6XFM4Gh4gk78MWfbjKjOEqqjgRi5asgAwgxbyg24IFsXL1wtwEc3rs4fHStFcdNhX004Y0x9gq8";  // Заменете с вашия таен ключ


        // Получаваме сумата от DTO
        int amount = request.getAmount();

        // Получаваме paymentMethod от DTO (предполага се, че ще го изпращате от клиента)
        String paymentMethodId = request.getPaymentMethodId();  // Новото поле от клиента

        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount((long) amount) // Сумата
                .setCurrency("bgn")  // Валута (BGN)
                .setAutomaticPaymentMethods(
                        PaymentIntentCreateParams.AutomaticPaymentMethods.builder()
                                .setEnabled(true) // Активиране на автоматични методи за плащане
                                .build())
                .build();

        // Създаваме PaymentIntent
        PaymentIntent paymentIntent = PaymentIntent.create(params);

        // Създаваме отговорния DTO и го връщаме
        PaymentResponseDTO responseDTO = new PaymentResponseDTO(paymentIntent.getClientSecret());

        return ResponseEntity.ok(responseDTO);
    }


    @PostMapping("/create-seller-account")
    public ResponseEntity<String> createSellerAccount(@RequestParam String email) throws Exception {
        String accountId = stripeService.createConnectedAccount(email);
        String onboardingUrl = stripeService.generateOnboardingLink(accountId);

        // 👉 Тук запиши `accountId` в базата данни към съответния продавач

        return ResponseEntity.ok(onboardingUrl);


    }
}