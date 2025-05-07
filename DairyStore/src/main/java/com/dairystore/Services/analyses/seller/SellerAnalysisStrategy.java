package com.dairystore.Services.analyses.seller;

import com.dairystore.Models.User;
import com.dairystore.Models.dtos.SellerAnalyticsDto;

public interface SellerAnalysisStrategy {
    void analyze(User user, SellerAnalyticsDto.SellerAnalyticsDtoBuilder builder);
}
