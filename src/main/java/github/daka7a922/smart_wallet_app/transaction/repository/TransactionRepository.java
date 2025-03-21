package github.daka7a922.smart_wallet_app.transaction.repository;

import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    List<Transaction> findAllByOwnerIdOrderByCreatedOnDesc(UUID id);

    List<Transaction> findAllBySenderOrReceiverOrderByCreatedOnDesc(String sender, String receiver);


}
