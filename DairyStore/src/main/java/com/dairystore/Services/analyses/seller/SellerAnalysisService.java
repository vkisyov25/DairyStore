package com.dairystore.Services.analyses.seller;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.SellerAnalyticsDto;
import com.dairystore.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerAnalysisService {
    private final UserService userService;
    private final List<SellerAnalysisStrategy> strategyList;

    public SellerAnalyticsDto getSellerAnalysis() {
        User user = userService.getUserByUsername();
        SellerAnalyticsDto.SellerAnalyticsDtoBuilder builder = SellerAnalyticsDto.builder();
        strategyList.forEach(strategyList -> strategyList.analyze(user, builder));
        return builder.build();
    }
}
