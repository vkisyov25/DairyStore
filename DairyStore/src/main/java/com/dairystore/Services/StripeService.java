package com.dairystore.Services;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.AccountLink;
import com.stripe.model.Transfer;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.AccountLinkCreateParams;
import com.stripe.param.TransferCreateParams;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StripeService {

    public String createConnectedAccount(String email) throws Exception {
        AccountCreateParams params = AccountCreateParams.builder()
                .setType(AccountCreateParams.Type.EXPRESS)
                .setCountry("BG")
                .setEmail(email)
                .setCapabilities(
                        AccountCreateParams.Capabilities.builder()
                                .setTransfers(
                                        AccountCreateParams.Capabilities.Transfers.builder()
                                                .setRequested(true)
                                                .build()
                                )
                                .build()
                )
                .build();

        Account account = Account.create(params);
        return account.getId();
    }


    public String generateOnboardingLink(String accountId) throws Exception {
        AccountLinkCreateParams params = AccountLinkCreateParams.builder()
                .setAccount(accountId)
                .setRefreshUrl("https://your-site.bg/onboarding/refresh")
                .setReturnUrl("http://localhost:8080/html/login.html")
                .setType(AccountLinkCreateParams.Type.ACCOUNT_ONBOARDING)
                .build();

        AccountLink accountLink = AccountLink.create(params);
        return accountLink.getUrl();
    }

    public void distributeToSellers(String paymentIntentId, @NotNull Map<String, Long> sellerAmounts) throws StripeException {
        System.out.println("sellerAmaounts size in distributeZTzoSellers:" + sellerAmounts.size());
        System.out.println("hello");
        System.out.println(sellerAmounts.size());
        System.out.println(paymentIntentId);
        for (Map.Entry<String, Long> entry : sellerAmounts.entrySet()) {
            String sellerAccountId = entry.getKey();
            System.out.println("key"+sellerAccountId);
            Long amount = entry.getValue();
            System.out.println("value"+amount);
            /*acct_1RQbbmGdl1eBGqyT*/
            TransferCreateParams transferParams = TransferCreateParams.builder()
                    .setAmount(amount)
                    .setCurrency("bgn")
                    .setDestination(sellerAccountId)
                    /*.setSourceTransaction(paymentIntentId)*/
                    .build();

            Transfer.create(transferParams);
        }
    }
}
