package com.dairystore.Services.analyses.buyer;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.BuyerAnalyticsDto;

public interface BuyerAnalysisStrategy {
    void analyze(User user, BuyerAnalyticsDto.BuyerAnalyticsDtoBuilder builder);
}
