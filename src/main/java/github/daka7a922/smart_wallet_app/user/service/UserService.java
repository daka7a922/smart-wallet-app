package github.daka7a922.smart_wallet_app.user.service;

import github.daka7a922.smart_wallet_app.exception.DomainException;
import github.daka7a922.smart_wallet_app.subscription.model.Subscription;
import github.daka7a922.smart_wallet_app.subscription.service.SubscriptionService;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.model.UserRole;
import github.daka7a922.smart_wallet_app.user.repository.UserRepository;
import github.daka7a922.smart_wallet_app.wallet.model.Wallet;
import github.daka7a922.smart_wallet_app.wallet.service.WalletService;
import github.daka7a922.smart_wallet_app.web.dto.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final WalletService walletService;

    private final SubscriptionService subscriptionService;


    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, WalletService walletService, SubscriptionService subscriptionService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.walletService = walletService;
        this.subscriptionService = subscriptionService;
    }

    @Transactional
    public User register(RegisterRequest registerRequest){

        Optional<User> optionalUser = userRepository.findByUsername(registerRequest.getUsername());

        if (optionalUser.isPresent()){
            throw new DomainException("Username is already in use");
        }

        User user = userRepository.save(initializeUser(registerRequest));

        Subscription defaultSubscription = subscriptionService.createDefaultSubscription(user);
        user.setSubscriptions(List.of(defaultSubscription));

        Wallet standardWallet = walletService.createNewWallet(user);
        user.setWallets(List.of(standardWallet));

        log.info("Successfully create new user account for username [%s] and id [%s]".formatted(user.getUsername(), user.getId()));

        return user;
    }

    private User initializeUser(RegisterRequest registerRequest) {

        LocalDateTime now = LocalDateTime.now();

        return User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .country(registerRequest.getCountry())
                .isActive(true)
                .userRole(UserRole.USER)
                .createdOn(now)
                .updatedOn(now)
                .build();
    }

}
