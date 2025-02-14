package github.daka7a922.smart_wallet_app.transaction.service;

import github.daka7a922.smart_wallet_app.exception.DomainException;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.model.TransactionStatus;
import github.daka7a922.smart_wallet_app.transaction.model.TransactionType;
import github.daka7a922.smart_wallet_app.transaction.repository.TransactionRepository;
import github.daka7a922.smart_wallet_app.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public List<Transaction> getAllTransactionsByOwnerId(UUID ownerId) {

        return transactionRepository.findAllByOwnerIdOrderByCreatedOnDesc(ownerId);
    }

    public Transaction getById(UUID id) {

        return transactionRepository.findById(id).orElseThrow(() -> new DomainException("Transaction with id [%s] does not exist.".formatted(id)));
    }

    public Transaction createTransaction(User owner, String sender, String receiver, BigDecimal amount, BigDecimal balanceLeft, Currency currency, TransactionType type, TransactionStatus status, String description, String failureReason) {

        Transaction transaction = Transaction.builder()
                .owner(owner)
                .sender(sender)
                .receiver(receiver)
                .amount(amount)
                .balanceLeft(balanceLeft)
                .currency(currency)
                .type(type)
                .status(status)
                .description(description)
                .failureReason(failureReason)
                .createdOn(LocalDateTime.now())
                .build();
        return transactionRepository.save(transaction);
    }
}
