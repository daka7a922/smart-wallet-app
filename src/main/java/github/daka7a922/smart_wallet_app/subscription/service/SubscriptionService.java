package github.daka7a922.smart_wallet_app.subscription.service;

import github.daka7a922.smart_wallet_app.subscription.model.Subscription;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionPeriod;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionStatus;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionType;
import github.daka7a922.smart_wallet_app.subscription.repository.SubscriptionRepository;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Service
@Slf4j
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;

    @Autowired
    public SubscriptionService(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    public Subscription createDefaultSubscription(User user){

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
}
