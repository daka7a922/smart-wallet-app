package github.daka7a922.smart_wallet_app.subscription.repository;

import github.daka7a922.smart_wallet_app.subscription.model.Subscription;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByStatusAndOwnerId(SubscriptionStatus status, UUID ownerId);


}
