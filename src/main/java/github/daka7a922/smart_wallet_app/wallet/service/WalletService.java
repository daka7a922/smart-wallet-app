package github.daka7a922.smart_wallet_app.wallet.service;

import github.daka7a922.smart_wallet_app.exception.DomainException;
import github.daka7a922.smart_wallet_app.subscription.model.Subscription;
import github.daka7a922.smart_wallet_app.subscription.model.SubscriptionType;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.model.TransactionStatus;
import github.daka7a922.smart_wallet_app.transaction.model.TransactionType;
import github.daka7a922.smart_wallet_app.transaction.service.TransactionService;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.wallet.model.Wallet;
import github.daka7a922.smart_wallet_app.wallet.model.WalletStatus;
import github.daka7a922.smart_wallet_app.wallet.repository.WalletRepository;
import github.daka7a922.smart_wallet_app.web.dto.TransferRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class WalletService {

    private static final String SMART_WALLET_LTD = "Smart Wallet Ltd";

    private final WalletRepository walletRepository;
    private final TransactionService transactionService;

    @Autowired
    public WalletService(WalletRepository walletRepository, TransactionService transactionService) {
        this.walletRepository = walletRepository;
        this.transactionService = transactionService;
    }

    public Wallet initializeFirstWallet(User user) {

        List<Wallet> allByOwnerUsername = walletRepository.findAllByOwnerUsername(user.getUsername());

        if (!allByOwnerUsername.isEmpty()) {
            throw new DomainException("User with id [%s] already has wallets".formatted(user.getUsername()));
        }

        Wallet wallet = walletRepository.save(initializeWallet(user));
        log.info("Successfully create new wallet with id [%s] and balance [%.2f].".formatted(wallet.getId(), wallet.getBalance()));

        return wallet;

    }

    @Transactional
    public Transaction transferFunds(User sender, TransferRequest transferRequest) {

        Wallet senderWallet = getWalletById(transferRequest.getFromWalletId());

        Optional<Wallet> optionalReceiverWallet = walletRepository.findAllByOwnerUsername(transferRequest.getToUsername())
                .stream()
                .filter(w -> w.getStatus() == WalletStatus.ACTIVE)
                .findFirst();

        String transferDescription = "Transfer from %s to %s for %.2f".formatted(sender.getUsername(), transferRequest.getToUsername(), transferRequest.getAmount());

        if (optionalReceiverWallet.isEmpty()) {

            return transactionService.createTransaction(
                    sender,
                    senderWallet.getId().toString(),
                    transferRequest.getToUsername(),
                    transferRequest.getAmount(),
                    senderWallet.getBalance(),
                    senderWallet.getCurrency(),
                    TransactionType.WITHDRAW,
                    TransactionStatus.FAILED,
                    transferDescription,
                    "Invalid transfer criteria"
            );
        }


        Transaction withdraw = charge(sender, senderWallet.getId(), transferRequest.getAmount(), transferDescription);

        if (withdraw.getStatus() == TransactionStatus.FAILED) {
            return withdraw;
        }
        Wallet receiverWallet = optionalReceiverWallet.get();

        receiverWallet.setBalance(receiverWallet.getBalance().add(transferRequest.getAmount()));
        receiverWallet.setUpdatedOn(LocalDateTime.now());

        walletRepository.save(receiverWallet);

        transactionService.createTransaction(receiverWallet.getOwner(),
                senderWallet.getId().toString(),
                receiverWallet.getId().toString(),
                transferRequest.getAmount(),
                receiverWallet.getBalance(),
                receiverWallet.getCurrency(),
                TransactionType.DEPOSIT,
                TransactionStatus.SUCCEEDED,
                transferDescription,
                "null"
        );

        return withdraw;
    }

    @Transactional
    public Transaction charge(User user, UUID walletId, BigDecimal amount, String description) {

        Wallet wallet = getWalletById(walletId);

        String failureReason = null;
        boolean isFailedTransaction = false;

        if (wallet.getStatus() == WalletStatus.INACTIVE) {
            failureReason = "Inactive wallet";
            isFailedTransaction = true;
        }
        if (wallet.getBalance().compareTo(amount) < 0) {
            failureReason = "Insufficient funds";
            isFailedTransaction = true;
        }

        if (isFailedTransaction) {

            return transactionService.createTransaction(
                    user,
                    wallet.getId().toString(),
                    SMART_WALLET_LTD,
                    amount,
                    wallet.getBalance(),
                    wallet.getCurrency(),
                    TransactionType.WITHDRAW,
                    TransactionStatus.FAILED,
                    description,
                    failureReason

            );
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setUpdatedOn(LocalDateTime.now());

        walletRepository.save(wallet);

        return transactionService.createTransaction(
                user,
                wallet.getId().toString(),
                SMART_WALLET_LTD,
                amount,
                wallet.getBalance(),
                wallet.getCurrency(),
                TransactionType.WITHDRAW,
                TransactionStatus.SUCCEEDED,
                description,
                null
        );
    }

    public Wallet getWalletById(UUID walletId) {

        return walletRepository.findById(walletId).orElseThrow(() -> new DomainException("Wallet with id [%s] does not exist".formatted(walletId)));

    }

    private Wallet initializeWallet(User user) {

        LocalDateTime now = LocalDateTime.now();

        return Wallet.builder()
                .owner(user)
                .balance(BigDecimal.valueOf(20.00))
                .status(WalletStatus.ACTIVE)
                .currency(Currency.getInstance("EUR"))
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

    public void unlockNewWallet(User user) {

        List<Wallet> allUserWallets = walletRepository.findAllByOwnerUsername(user.getUsername());
        Subscription activeSubscription = user.getSubscriptions().get(0);

        boolean isDefaultSubscriptionAndMaxWalletsUnlocked = activeSubscription.getType() == SubscriptionType.DEFAULT && allUserWallets.size() == 1;
        boolean isPremiumSubscriptionAndMaxWalletsUnlocked = activeSubscription.getType() == SubscriptionType.PREMIUM && allUserWallets.size() == 2;
        boolean isUltimateSubscriptionAndMaxWalletsUnlocked = activeSubscription.getType() == SubscriptionType.ULTIMATE && allUserWallets.size() == 3;

        if (isDefaultSubscriptionAndMaxWalletsUnlocked || isPremiumSubscriptionAndMaxWalletsUnlocked || isUltimateSubscriptionAndMaxWalletsUnlocked) {
            throw new DomainException("Cannot unlock new wallet. Max wallets unlocked for current subscription");
        }

        Wallet wallet = Wallet.builder()
                .owner(user)
                .status(WalletStatus.ACTIVE)
                .balance(BigDecimal.valueOf(0))
                .currency(Currency.getInstance("EUR"))
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        walletRepository.save(wallet);


    }
}
