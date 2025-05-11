package com.dairystore.Controllers.Analyses;

import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.analyses.buyer.BuyerAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class BuyerAnalysisController {
    private final BuyerAnalysisService buyerAnalysisService;

    @GetMapping("/buyer")
    public ResponseEntity<BuyerAnalyticsDto> analyzeUserShoppingBehavior() {
        BuyerAnalyticsDto analytics = buyerAnalysisService.getBuyerAnalysis();
        return ResponseEntity.ok(analytics);
    }
}
