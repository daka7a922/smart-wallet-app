package github.daka7a922.smart_wallet_app.wallet.repository;

import github.daka7a922.smart_wallet_app.wallet.model.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, UUID> {

    List<Wallet> findAllByOwnerUsername(String ownerUsername);

    Optional<Wallet> findByIdAndOwnerId(UUID id, UUID ownerId);


}
