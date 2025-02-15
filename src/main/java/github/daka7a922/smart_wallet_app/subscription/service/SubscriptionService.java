package github.daka7a922.smart_wallet_app.subscription.service;

import github.daka7a922.smart_wallet_app.exception.DomainException;
import github.daka7a922.smart_wallet_app.subscription.model.Subscription;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionPeriod;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionStatus;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionType;
import github.daka7a922.smart_wallet_app.subscription.repository.SubscriptionRepository;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.model.TransactionStatus;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.wallet.service.WalletService;
import github.daka7a922.smart_wallet_app.web.dto.UpgradeRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;


@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final WalletService walletService;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository, WalletService walletService) {
        this.subscriptionRepository = subscriptionRepository;
        this.walletService = walletService;
    }

    public Subscription createDefaultSubscription(User user) {

        Subscription subscription = subscriptionRepository.save(initializeDefaultSubscription(user));
        log.info("Successfully create new subscription with id [%s] and type [%s].".formatted(subscription.getId(), subscription.getType()));

        return subscription;

    }

    private Subscription initializeDefaultSubscription(User user) {

        LocalDateTime now = LocalDateTime.now();

        return Subscription.builder()
                .owner(user)
                .period(SubscriptionPeriod.MONTHLY)
                .status(SubscriptionStatus.ACTIVE)
                .type(SubscriptionType.DEFAULT)
                .renewalAllowed(true)
                .price(BigDecimal.valueOf(0.00))
                .createdOn(now)
                .completedOn(now.plusMonths(1))
                .build();


    }

    @Transactional
    public Transaction upgrade(User user, SubscriptionType subscriptionType, UpgradeRequest upgradeRequest) {

        Optional<Subscription> optionalSubscription = subscriptionRepository.findByStatusAndOwnerId(SubscriptionStatus.ACTIVE, user.getId());
        if (optionalSubscription.isEmpty()) {
            throw new DomainException("No active subscription has been found for user [%s].".formatted(user.getId()));
        }

        Subscription currentSubscription = optionalSubscription.get();
        SubscriptionPeriod subscriptionPeriod = currentSubscription.getPeriod();
        String period = subscriptionPeriod.name().substring(0, 1).toUpperCase() + subscriptionPeriod.name().substring(1);
        String type = subscriptionType.name().substring(0, 1).toUpperCase() + subscriptionType.name().substring(1);

        String chargeDescription = "Purchase of %s %s subscription".formatted(period, type);
        BigDecimal subscriptionPrice = getSubscriptionPrice(subscriptionType, subscriptionPeriod);


        Transaction chargeResult = walletService.charge(user, upgradeRequest.getWalletId(), subscriptionPrice, chargeDescription);

        if (chargeResult.getStatus() == TransactionStatus.FAILED) {

            log.warn("Charge for subscription failed for user with id[%s], subscription type [%s]".formatted(user.getId(), subscriptionType));
            return chargeResult;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime completedOn;

        if (subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            completedOn = now.plusMonths(1);
        } else {
            completedOn = now.plusYears(1);
        }

        Subscription newSubscription = Subscription.builder()
                .owner(user)
                .status(SubscriptionStatus.ACTIVE)
                .period(subscriptionPeriod)
                .type(subscriptionType)
                .price(subscriptionPrice)
                .renewalAllowed(subscriptionPeriod == SubscriptionPeriod.MONTHLY)
                .createdOn(LocalDateTime.now())
                .completedOn(completedOn)
                .build();

        currentSubscription.setCompletedOn(now);
        currentSubscription.setStatus(SubscriptionStatus.COMPLETED);

        subscriptionRepository.save(currentSubscription);
        subscriptionRepository.save(newSubscription);


        return chargeResult;
    }

    private BigDecimal getSubscriptionPrice(SubscriptionType subscriptionType, SubscriptionPeriod subscriptionPeriod) {


        if (subscriptionType == SubscriptionType.DEFAULT) {
            return BigDecimal.ZERO;
        } else if (subscriptionType == SubscriptionType.PREMIUM && subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            return new BigDecimal("19.99");
        } else if (subscriptionType == SubscriptionType.PREMIUM && subscriptionPeriod == SubscriptionPeriod.YEARLY) {
            return new BigDecimal("199.99");
        } else if (subscriptionType == SubscriptionType.ULTIMATE && subscriptionPeriod == SubscriptionPeriod.MONTHLY) {
            return new BigDecimal("49.99");
        } else {
            return new BigDecimal("499.99");
        }
    }
}
