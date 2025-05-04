package com.dairystore.Controllers.Analyses;

import com.dairystore.Models.dtos.SellerAnalyticsDto;
import com.dairystore.Services.analyses.seller.SellerAnalysisService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/analysis")
public class SellerAnalysisController {

    private final SellerAnalysisService sellerAnalysisService;

    @GetMapping("/seller")
    public ResponseEntity<SellerAnalyticsDto> viewSellerAnalysis() {
        SellerAnalyticsDto analytics = sellerAnalysisService.getSellerAnalysis();
        return ResponseEntity.ok(analytics);
    }
}
