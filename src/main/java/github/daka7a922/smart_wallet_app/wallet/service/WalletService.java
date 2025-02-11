package github.daka7a922.smart_wallet_app.wallet.service;

import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.wallet.model.Wallet;
import github.daka7a922.smart_wallet_app.wallet.model.WalletStatus;
import github.daka7a922.smart_wallet_app.wallet.repository.WalletRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Service
@Slf4j
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createNewWallet(User user){

        Wallet wallet = walletRepository.save(initializeWallet(user));
        log.info("Successfully create new wallet with id [%s] and balance [%.2f].".formatted(wallet.getId(), wallet.getBalance()));

        return wallet;

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
}
