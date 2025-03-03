package github.daka7a922.smart_wallet_app.web;

import github.daka7a922.smart_wallet_app.security.AuthenticationDetails;
import github.daka7a922.smart_wallet_app.transaction.model.Transaction;
import github.daka7a922.smart_wallet_app.transaction.service.TransactionService;
import github.daka7a922.smart_wallet_app.user.model.User;
import github.daka7a922.smart_wallet_app.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService) {
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @GetMapping
    public ModelAndView getTransactionsPage(@AuthenticationPrincipal AuthenticationDetails userDetails) {


        User user = userService.getByUsername(userDetails.getUsername());

        UUID userId = user.getId();
        List<Transaction> transactions = transactionService.getAllTransactionsByOwnerId(userId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transactions");
        modelAndView.addObject("transactions", transactions);
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @GetMapping("/{id}")
    public ModelAndView getTransactionById(@PathVariable UUID id, @AuthenticationPrincipal AuthenticationDetails userDetails) {

        Transaction transaction = transactionService.getById(id);
        User user = userService.getUserById(userDetails.getUserId());

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("transaction-result");
        modelAndView.addObject("transaction", transaction);
        modelAndView.addObject("user", user);

        return modelAndView;
    }
}
