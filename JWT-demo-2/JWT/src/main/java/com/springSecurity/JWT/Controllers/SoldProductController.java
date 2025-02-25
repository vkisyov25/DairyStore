package com.springSecurity.JWT.Controllers;

import com.springSecurity.JWT.Models.dtos.BuyerAnalyticsDto;
import com.springSecurity.JWT.Services.SoldProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/soldProduct")
public class SoldProductController {
    private final SoldProductService soldProductService;

    public SoldProductController(SoldProductService soldProductService) {
        this.soldProductService = soldProductService;
    }

    @GetMapping("/buyerAnalytics")
    public ResponseEntity<BuyerAnalyticsDto> analyzeUserShoppingBehavior() {
        BuyerAnalyticsDto analytics = soldProductService.analyzeUserShoppingBehavior();
        return ResponseEntity.ok(analytics);
    }
}
