package com.dairystore.Controllers;

import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.UserAnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analytics")
public class UserAnalyticsController {
    private final UserAnalyticsService userAnalyticsService;

    @GetMapping("/buyer")
    public ResponseEntity<BuyerAnalyticsDto> analyzeUserShoppingBehavior() {
        BuyerAnalyticsDto analytics = userAnalyticsService.analyzeUserShoppingBehavior();
        return ResponseEntity.ok(analytics);
    }
}
