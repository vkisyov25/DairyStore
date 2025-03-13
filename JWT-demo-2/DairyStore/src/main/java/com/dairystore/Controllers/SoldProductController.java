package com.dairystore.Controllers;

import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.SoldProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/soldProduct")
@RequiredArgsConstructor
public class SoldProductController {
    private final SoldProductService soldProductService;

    @GetMapping("/buyerAnalytics")
    public ResponseEntity<BuyerAnalyticsDto> analyzeUserShoppingBehavior() {
        BuyerAnalyticsDto analytics = soldProductService.analyzeUserShoppingBehavior();
        return ResponseEntity.ok(analytics);
    }
}
