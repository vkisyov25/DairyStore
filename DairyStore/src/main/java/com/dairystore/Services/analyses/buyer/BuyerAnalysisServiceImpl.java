package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;
import com.dairystore.Services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BuyerAnalysisServiceImpl implements BuyerAnalysisService {
    private final UserService userService;
    private final List<BuyerAnalysisStrategy> strategyList;

    @Override
    public BuyerAnalyticsDto getBuyerAnalysis() {
        User user = userService.getCurrentUser();
        BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder = BuyerAnalyticsDto.builder();
        strategyList.forEach(strategyList -> strategyList.analyze(user, builder));
        return builder.build();
    }
}
