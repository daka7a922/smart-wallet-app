package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.security.AuthenticationDetails;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.service.TransactionService;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import github.daka7a922.smart_wallet_app.wallet.service.WalletService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/wallets")
public class WalletController {

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Autowired
    public WalletController(UserService userService, WalletService walletService, TransactionService transactionService) {
        this.userService = userService;
        this.walletService = walletService;
        this.transactionService = transactionService;
    }


    @GetMapping()
    public ModelAndView getWalletsPage(@AuthenticationPrincipal AuthenticationDetails userDetails) {

        User user = userService.getByUsername(userDetails.getUsername());
        Map<UUID, List<Transaction>> lastFourTransactions = walletService.getLastFourTransactions(user.getWallets());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("wallets");
        modelAndView.addObject("user", user);
        modelAndView.addObject("lastFourTransactions", lastFourTransactions);


        return modelAndView;
    }

    @PostMapping
    public String addNewWallet(@AuthenticationPrincipal AuthenticationDetails userDetails){

    User user = userService.getByUsername(userDetails.getUsername());
    walletService.unlockNewWallet(user);

        return "redirect:/wallets";
    }

    @PutMapping("/{id}/status")
    public String updateWalletStatus(@AuthenticationPrincipal AuthenticationDetails userDetails, @PathVariable UUID id){

        walletService.changeWalletStatus(id, userDetails.getUserId());

        return "redirect:/wallets";
    }

    @PutMapping("/{id}/balance/top-up")
    public String topUpBalance(@PathVariable UUID id){

        walletService.topUp(id, BigDecimal.valueOf(20));

        return  "redirect:/wallets";
    }
}
