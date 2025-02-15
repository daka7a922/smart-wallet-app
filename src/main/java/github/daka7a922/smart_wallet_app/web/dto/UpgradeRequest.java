package github.daka7a922.smart_wallet_app.web.dto;

import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionPeriod;
import github.daka7a922.smart_wallet_app.wallet.model.Wallet;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class UpgradeRequest {

    private SubscriptionPeriod subscriptionPeriod;

    private UUID walletId;


}
