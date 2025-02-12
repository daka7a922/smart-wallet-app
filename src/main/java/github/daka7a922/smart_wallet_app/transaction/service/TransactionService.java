package github.daka7a922.smart_wallet_app.transaction.service;

import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }


    public List<Transaction> getAllTransactionsByOwnerId(UUID ownerId){

        return transactionRepository.findAllByOwnerIdOrderByCreatedOnDesc(ownerId);
    }
}
